package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class TerrainAdaptingPoint extends Point{

	private final Vector displacementAxis;
	private final double maxDisplacement;
	private boolean passIfMiss = false;

	public TerrainAdaptingPoint(Vector displacementAxis, double maxDisplacement) {
		this.displacementAxis = displacementAxis.normalize();
		this.maxDisplacement = maxDisplacement;
	}

	@Override
	public Point draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		particleLoc = displace(particleLoc);
		if(particleLoc != null) world.spawnParticle(particle, particleLoc, particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
		return this;
	}
	
	public Location displace(Location location) {
		World world = location.getWorld();

		
		Location raySource = location.clone();
		double effectiveMaxDisplacement = maxDisplacement;

		if(!location.getBlock().isPassable()) { // We're in a block
			while(!raySource.getBlock().isPassable() && effectiveMaxDisplacement < maxDisplacement*2) { // we move up until out of block, or out of range
				effectiveMaxDisplacement += .1;
				raySource = location.clone().add(displacementAxis.clone().multiply(-effectiveMaxDisplacement));
			}
			if(!raySource.getBlock().isPassable()) return null; //Out of range and still in block, fail
			//in range and successfully moved out
		}
		
		//Either we're in air, or have been moved into air
		
		RayTraceResult rtr = world.rayTraceBlocks(raySource, displacementAxis, effectiveMaxDisplacement);
		if(rtr != null) { // we hit a block!
			return rtr.getHitPosition().toLocation(world).add(displacementAxis.clone().multiply(-.1));
		}
		
		//We are in air
		
		return passIfMiss ? null : location;
	}

	public TerrainAdaptingPoint passIfMiss() { passIfMiss = true; return this; }

}
