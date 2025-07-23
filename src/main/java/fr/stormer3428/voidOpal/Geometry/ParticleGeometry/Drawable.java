package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public interface Drawable {

	public Drawable draw(Location location, double scale);

	public Drawable rotateAroundAxis(Vector axis, double radians);
	public Drawable rotateAroundX(double radians);
	public Drawable rotateAroundY(double radians);
	public Drawable rotateAroundZ(double radians);
	
	public Particle getParticle();
	public Drawable particle(Particle particle);
	public Drawable particleSpeed(float particleSpeed);
	public <T> Drawable particleData(T particleData);
	public Drawable forceRendering(boolean forceRender);
	public boolean isForceRendering();
	
}
