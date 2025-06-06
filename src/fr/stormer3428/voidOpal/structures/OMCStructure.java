package fr.stormer3428.voidOpal.structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.AirMode;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.BuildMode;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.TerrainMode;

public class OMCStructure {

	int x=0,y=0,z=0;
	
	private final HashMap<Vector, BlockData> map = new HashMap<>();
	private final HashMap<Material, Integer> materialsCount = new HashMap<>();
	protected Vector origin = new Vector();

	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector) { return  build(loc, buildMode, airMode, terrainMode, interval, vector, O -> {}); }
	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector, Consumer<Block> blockPlaceConsumer) { return  build(loc, buildMode, airMode, terrainMode, interval, vector, blockPlaceConsumer, ()->{});}
	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector, Consumer<Block> blockPlaceConsumer, Runnable onFinish) { 
		BukkitRunnable runnable = new OMCStructureBuildRunnable(loc, origin, buildMode, airMode, terrainMode, vector, map, blockPlaceConsumer, onFinish);
		runnable.runTaskTimer(OMCCore.getJavaPlugin(), 0, interval); 
		return runnable;
	}

	public Vector isStructureCompleted(Block placed) {
		Collection<Material> mats = getMaterials();
		if(!mats.contains(placed.getType())) return null;
		for(Entry<Vector, BlockData> entry : map.entrySet()) if(entry.getValue().matches(placed.getBlockData())){
			if(isStructureCompleted(placed.getLocation(), entry.getKey())) return entry.getKey();
		}
		return null;
	}
	
	private boolean isStructureCompleted(Location worldPos, Vector reference) { 
		Location origin = worldPos.clone().subtract(reference);
		for(Entry<Vector, BlockData> entry : map.entrySet()) {
			Vector localPos = entry.getKey();
			BlockData expected = entry.getValue();
			BlockData inWorld = origin.clone().add(localPos).getBlock().getBlockData();
			if(!expected.matches(inWorld)) return false;
		}
		return true;
	}
	
	
	private void updateDimensions() {
		x=0;
		y=0;
		z=0;
		for(Vector v : map.keySet()) {
			x = Math.max(x, v.getBlockX());
			y = Math.max(y, v.getBlockY());
			z = Math.max(z, v.getBlockZ());
		}
	}
	
	public void addBlock(Vector v, Material m) { addBlock(v, m.createBlockData()); }
	public void addBlock(Vector v, BlockData blockData) { 
		map.put(v, blockData);
		Material m = blockData.getMaterial();
		materialsCount.put(m, getMaterialCount(m) + 1);
		updateDimensions();
	}
	
	public HashMap<Material, Integer> getMaterialsCount() { return materialsCount; }
	public Vector getDimensions() { return new Vector(x, y, z); }
	public Vector getOrigin() { return origin; }
	public void setOrigin(Vector origin) { this.origin = origin; }
	public int getMaterialCount(Material m) { return materialsCount.getOrDefault(m, 0); }
	public Collection<Material> getMaterials() { return materialsCount.keySet(); }
	
}





