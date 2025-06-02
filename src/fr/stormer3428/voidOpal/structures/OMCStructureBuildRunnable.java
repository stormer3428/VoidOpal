package fr.stormer3428.voidOpal.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class OMCStructureBuildRunnable extends BukkitRunnable{

	public static enum BuildMode { NEAREST, NEAREST_SUPPORTED }
	public static enum AirMode { REPLACE_TERRAIN, IGNORE }
	public static enum TerrainMode { REPLACE_TERRAIN, KEEP_IF_NOT_PASSABLE, KEEP }

	private static final Collection<BlockFace> ORTHOGONAL = Arrays.asList(BlockFace.DOWN, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST);

	private final HashMap<Vector, BlockData> leftToBuild = new HashMap<>();
	private final Location loc;
	private final Location origin;
	private final Consumer<Block> blockPlaceConsumer;
	private final Runnable onFinish;

	@SuppressWarnings("unused")
	private final AirMode airMode;
	private final BuildMode buildMode;
	private final TerrainMode terrainMode;
	private final StructureRotation rotation;

	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector vector, HashMap<Vector, BlockData> map) { this(loc, origin, buildMode, airMode, terrainMode, vector, map, O -> {}); }
	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector vector, HashMap<Vector, BlockData> map, Consumer<Block> blockPlaceConsumer) { this(loc, origin, buildMode, airMode, terrainMode, vector, map, blockPlaceConsumer, () -> {}); }
	public OMCStructureBuildRunnable(Location loc, Vector origin, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, Vector vector, HashMap<Vector, BlockData> map, Consumer<Block> blockPlaceConsumer, Runnable onFinish) {
		this.blockPlaceConsumer = blockPlaceConsumer;
		this.onFinish = onFinish;
		this.buildMode = buildMode;
		this.airMode = airMode;
		this.terrainMode = terrainMode;

		this.loc = loc.getBlock().getLocation().add(.5,.5,.5);
		this.rotation = getRotationForVector(vector);
		this.origin = loc.clone().add(origin.clone().rotateAroundY(rotationMap.get(rotation)));

		map.forEach((bv,bd)->{
			bd = bd.clone();
			bd.rotate(rotation);
			bv = bv.clone();
			bv.rotateAroundY(rotationMap.get(rotation));
			leftToBuild.put(bv, bd);
		});

		if(airMode == AirMode.IGNORE) for(Entry<Vector, BlockData> entry : new ArrayList<>(leftToBuild.entrySet())) if(entry.getValue().getMaterial().isAir()) leftToBuild.remove(entry.getKey());
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
		Vector blockVector = getNextBlock();
		if(blockVector == null) {
			onFinish.run();
			cancel();
			return;
		}
		BlockData data = leftToBuild.remove(blockVector);

		Location placeLoc = loc.clone().add(blockVector);
		placeLoc.getBlock().setBlockData(data, true);
		blockPlaceConsumer.accept(placeLoc.getBlock());
	}

	private Vector getNextBlock() {
		ArrayList<Vector> options = new ArrayList<>(leftToBuild.keySet());

		for(Vector v : new ArrayList<>(options)) {
			Block present = loc.clone().add(v).getBlock();
			//			BlockData toPlace = leftToBuild.get(v);
			if(terrainMode == TerrainMode.KEEP && !present.getType().isAir()) options.remove(v);
			if(terrainMode == TerrainMode.KEEP_IF_NOT_PASSABLE && !present.isPassable()) options.remove(v);
		}

		if(buildMode == BuildMode.NEAREST_SUPPORTED) {
			ArrayList<Vector> supportedOptions = new ArrayList<>();
			for(Vector v : options) {
				Block present = loc.clone().add(v).getBlock();
				BlockData toPlace = leftToBuild.get(v);
				if(!toPlace.isSupported(present)) continue;
				if(isSupported(present, toPlace)) supportedOptions.add(v);
				for(BlockFace face : ORTHOGONAL) if(present.getRelative(face).getBlockData().isFaceSturdy(face.getOppositeFace(), BlockSupport.CENTER)){
					supportedOptions.add(v);
					break;
				}
			}
			if(!supportedOptions.isEmpty()) {
				options.clear();
				options.addAll(supportedOptions);
			}
		}

		TreeSet<Vector> sorted = new TreeSet<>((a,b)->(int) Math.ceil(
			loc.clone().add(a).distanceSquared(origin) - 
			loc.clone().add(b).distanceSquared(origin)
			));
		sorted.addAll(options);
		return sorted.isEmpty() ? null : sorted.getFirst();
	}
	
	public static boolean isSupported(Block present, BlockData toPlace) {
		for(BlockFace face : ORTHOGONAL) 
			if(present.getRelative(face).getBlockData().isFaceSturdy(face.getOppositeFace(), BlockSupport.CENTER))
			return true;
		return false; 

	}
}
