package fr.stormer3428.voidOpal.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

public class OMCStructureBuildRunnable extends BukkitRunnable{

	public static enum BuildMode { NEAREST, NEAREST_SUPPORTED, FURTHEST_SUPPORTED, FURTHEST }
	public static enum AirMode { REPLACE_TERRAIN, IGNORE }
	public static enum TerrainMode { REPLACE_TERRAIN, KEEP_IF_NOT_PASSABLE, KEEP }

	private static final Collection<BlockFace> ORTHOGONAL = Arrays.asList(BlockFace.DOWN, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST);

	private final HashMap<Vector, BlockData> leftToBuild = new HashMap<>();
//	private final HashMap<Vector, BlockData> ImmutableBlockDataMap = new HashMap<>();
	private final ConcurrentSkipListSet<Vector> sorted;
	private final Location loc;
	private Vector origin;
	private final Consumer<Block> blockPlaceConsumer;
	private final Runnable onFinish;
	private int blocksPerCycle = 16;
	private boolean sortedRespectsConstraints = true;

	@SuppressWarnings("unused")
	private final AirMode airMode;
	private final BuildMode buildMode;
	private final TerrainMode terrainMode;
	private final StructureRotation rotation;

	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector orientation, HashMap<Vector, BlockData> map) { this(loc, origin, buildMode, airMode, terrainMode, orientation, map, O -> {}); }
	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector orientation, HashMap<Vector, BlockData> map, Consumer<Block> blockPlaceConsumer) { this(loc, origin, buildMode, airMode, terrainMode, orientation, map, blockPlaceConsumer, () -> {}); }
	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector orientation, HashMap<Vector, BlockData> map, Consumer<Block> blockPlaceConsumer, Runnable onFinish) {
		this.blockPlaceConsumer = blockPlaceConsumer;
		this.onFinish = onFinish;
		this.buildMode = buildMode;
		this.airMode = airMode;
		this.terrainMode = terrainMode;

		this.loc = loc.getBlock().getLocation().add(.5,.5,.5);
		this.origin = origin.toLocation(loc.getWorld()).getBlock().getLocation().add(.5,.5,.5).toVector();
		
		this.rotation = getRotationForVector(orientation);
		this.sorted = new ConcurrentSkipListSet<>((a,b)-> {
			if(a.equals(b)) return 0;

			double delta = a.length() - b.length();
//			double delta = a.clone().add(this.origin).length() - b.clone().add(this.origin).length();
			double sign = Math.signum(delta);
			if(buildMode == BuildMode.FURTHEST_SUPPORTED || buildMode == BuildMode.FURTHEST) sign=-sign;
			return sign > 0 ? 1 : -1;
		});

		if(airMode == AirMode.IGNORE) for(Entry<Vector, BlockData> entry : new ArrayList<>(map.entrySet())) if(entry.getValue().getMaterial().isAir()) map.remove(entry.getKey());

		map.forEach((bv,bd)->{
			if(bv == null || bd == null) {
				OMCLogger.error("Didnt include null entry : " + bv + " : " + bd);
				return;
			}
			bd = bd.clone();
			bv = bv.clone().subtract(this.origin).add(new Vector(.5,.5,.5));
			if(rotation != null) {
				bd.rotate(rotation);
				bv.rotateAroundY(rotationMap.get(rotation));
			}
			leftToBuild.put(bv, bd);
//			ImmutableBlockDataMap.put(bv.clone().subtract(origin), bd);
		});
	}

	private static final Map<StructureRotation, Double> rotationMap = Map.ofEntries(
		Map.entry(StructureRotation.NONE, 0d),
		Map.entry(StructureRotation.CLOCKWISE_90, Math.PI/2),
		Map.entry(StructureRotation.CLOCKWISE_180, Math.PI),
		Map.entry(StructureRotation.COUNTERCLOCKWISE_90, -Math.PI/2)
		);

	private static StructureRotation getRotationForVector(Vector vector) { 
		StructureRotation rotation = null;
		double minAngle = Math.PI;
		for(Entry<StructureRotation, Double> entry : rotationMap.entrySet()) {
			double angle = vector.angle(new Vector(1,0,0).rotateAroundY(entry.getValue()));
			if(angle > minAngle) continue;
			minAngle = angle;
			rotation = entry.getKey();
		}
		return rotation; 
	}

	@Override public void run() {
		for(int i = 0; i < blocksPerCycle; i++) {
			if(!sortedRespectsConstraints) sorted.clear();
			Vector blockVector = getNextBlock();
			if(blockVector == null) {
				Bukkit.getScheduler().runTask(OMCCore.getJavaPlugin(), ()->{
					onFinish.run();
				});
				return;
			}
			/*
			BlockData fromLeftToBuild = leftToBuild.remove(blockVector);
			BlockData data = ImmutableBlockDataMap.get(blockVector);
			if(fromLeftToBuild == null) {
				OMCLogger.systemError("Found fromLeftToBuild null blockdata value");
			}
			/*/
			BlockData data = leftToBuild.remove(blockVector);
			//*/
			
			if(data == null) {
				OMCLogger.systemError("Skipped immutable null blockdata value ?????????????????????????????????????");
				i--;
				continue;
			}

			Location placeLoc = loc.clone().add(blockVector);
			Bukkit.getScheduler().runTask(OMCCore.getJavaPlugin(), ()->{
				placeLoc.getBlock().setBlockData(data, true);
				blockPlaceConsumer.accept(placeLoc.getBlock());
			});
		}
		sorted.clear();
		Bukkit.getScheduler().runTaskAsynchronously(OMCCore.getJavaPlugin(), ()->{
			run();
		});
	}

	private Vector getNextBlock() {
		if(!sorted.isEmpty()) return sorted.pollFirst();

		sortedRespectsConstraints = true;

		ArrayList<Vector> options = new ArrayList<>();
		leftToBuild.keySet().forEach(v->{
			if(v!=null) options.add(v);
			else OMCLogger.systemError("Prevented null root insertion");
		});
		for(Vector v : new ArrayList<>(options)) {
			Block present = loc.clone().add(v).getBlock();
			if(terrainMode == TerrainMode.KEEP && !present.getType().isAir()) options.remove(v);
			if(terrainMode == TerrainMode.KEEP_IF_NOT_PASSABLE && !present.isPassable()) options.remove(v);
		}

		if (buildMode == BuildMode.NEAREST_SUPPORTED || buildMode == BuildMode.FURTHEST_SUPPORTED) {
			for (Vector v : options) {
				Block present = loc.clone().add(v).getBlock();
				BlockData toPlace = leftToBuild.get(v);
				if (!toPlace.isSupported(present)) continue;
				if (!hasAdjacentSupportingBlock(present)) continue;
				sorted.add(v);
			}
			if (sorted.isEmpty()) sortedRespectsConstraints = false;
		}
		if (sorted.isEmpty()) {
			sorted.clear();
			options.forEach(v->{
				if(v!=null) sorted.add(v);
				else OMCLogger.systemError("Prevented null root insertion");
			});
		}
		return sorted.pollFirst();
	}

	public static boolean hasAdjacentSupportingBlock(Block present) {
		for(BlockFace face : ORTHOGONAL) {
			if(present.getRelative(face).getBlockData().isFaceSturdy(face.getOppositeFace(), BlockSupport.CENTER)) {
				return true;
			}
		}
		return false; 
	}
	
	public void setBlocksPerCycle(int blocksPerCycle) { this.blocksPerCycle = blocksPerCycle; }
	public void setOrigin(Vector origin) { this.origin = origin; }
}






