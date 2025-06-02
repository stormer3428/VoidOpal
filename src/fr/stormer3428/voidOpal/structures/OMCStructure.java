package fr.stormer3428.voidOpal.structures;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.AirMode;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.BuildMode;
import fr.stormer3428.voidOpal.structures.OMCStructureBuildRunnable.TerrainMode;

public class OMCStructure {

	protected final HashMap<Vector, BlockData> map = new HashMap<>();
	protected Vector origin = new Vector();

	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector) { return  build(loc, buildMode, airMode, terrainMode, interval, vector, O -> {}); }
	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector, Consumer<Block> blockPlaceConsumer) { return  build(loc, buildMode, airMode, terrainMode, interval, vector, blockPlaceConsumer, ()->{});}
	public BukkitRunnable build(Location loc, BuildMode buildMode, AirMode airMode, TerrainMode terrainMode, int interval, Vector vector, Consumer<Block> blockPlaceConsumer, Runnable onFinish) { 
		BukkitRunnable runnable = new OMCStructureBuildRunnable(loc, origin, buildMode, airMode, terrainMode, vector, map, blockPlaceConsumer, onFinish);
		runnable.runTaskTimer(OMCCore.getJavaPlugin(), 0, interval); 
		return runnable;
	}
	
	public Vector getDimensions() {
		int x=0,y=0,z=0;
		for(Vector v : map.keySet()) {
			x = Math.max(x, v.getBlockX());
			y = Math.max(y, v.getBlockY());
			z = Math.max(z, v.getBlockZ());
		}
		return new Vector(x, y, z);
	}
	
	public Vector getOrigin() { return origin; }
	public void setOrigin(Vector origin) { this.origin = origin; }
	
}
