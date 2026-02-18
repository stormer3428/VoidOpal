package fr.stormer3428.voidOpal.util.providers;

import org.bukkit.Location;
import org.bukkit.Particle;

import fr.stormer3428.voidOpal.Geometry.ParticleGeometry.Drawable;

@FunctionalInterface
public interface TrailGeometryParticleProvider extends OMCProvider<Particle.Trail>{

	public Particle.Trail getData(Drawable g, Location loc, double scale);

	public default Particle.Trail getData(Object ... ctx) {return getData((Drawable)ctx[0], (Location)ctx[1], (double)ctx[2]);}
	
}
