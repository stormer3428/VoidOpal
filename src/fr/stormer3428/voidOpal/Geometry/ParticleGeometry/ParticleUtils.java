package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class ParticleUtils {


	public static void drawParticleLine(Location a, Location b, Particle particle) {
		drawParticleLine(a, b, particle, 0.1d);
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution) {
		drawParticleLine(a, b, particle, resolution, 1);
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount) {
		drawParticleLine(a, b, particle, resolution, pamount, new Vector());
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount, Vector spread) {
		drawParticleLine(a, b, particle, resolution, pamount, spread, 0.0d);
	}

	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount, Vector spread, double speed) {
		drawParticleLine(a, b, particle, resolution, pamount, spread, speed, null);
	}
	
	public static void drawParticleLine(Location a, Location b, Particle particle, double resolution, int pamount, Vector spread, double speed, Object options) {
		double d = a.distance(b);
		int amount = (int) (d/resolution) + 2;

		Vector a2b = b.toVector().subtract(a.toVector()).normalize().multiply(d/amount);
		Location particleloc = a.clone();
		for(int c = amount; c > 0; c--) {
			particleloc.getWorld().spawnParticle(particle, particleloc, pamount, spread.getX(), spread.getY(), spread.getZ(), speed, options, true);
			particleloc.add(a2b);
		}
	}

	public static void spawnMovingParticle(Particle particle, Location loc, Vector dir, double speed) {
		loc.getWorld().spawnParticle(particle, loc, 0, dir.getX(), dir.getY(), dir.getZ(), speed, null, true);
	}

	
}
