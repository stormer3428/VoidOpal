package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class MovingPoint implements Drawable{

	private Vector location = new Vector();
	private Particle particle = Particle.CRIT;
	private Object particleData = null;
	private Vector particleDirection = new Vector(0,0,0);
	private Vector particleOffdirection = new Vector(0,0,0);
	private boolean forceRender = true;
	private boolean staticDirection = false;
	private boolean scaleSpeed = false;

	public void draw(Location location) { draw(location, 1.0); }
	@Override public MovingPoint draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc, 
				0, 
				particleDirection.getX() + particleOffdirection.getX(),
				particleDirection.getY() + particleOffdirection.getY(),
				particleDirection.getZ() + particleOffdirection.getZ(),
				particleDirection.clone().add(particleOffdirection).length()*(scaleSpeed ? scale : 1), particleData, forceRender);
		return this;
	}


	@Override
	public MovingPoint rotateAroundAxis(Vector axis, double radians) {
		location.rotateAroundAxis(axis, radians);
		if(!staticDirection) {
			particleDirection.rotateAroundAxis(axis, radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundX(double radians) {
		location.rotateAroundX(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundX(radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundY(double radians) {
		location.rotateAroundY(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundY(radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundZ(double radians) {
		location.rotateAroundZ(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundZ(radians);
		}
		return this;
	}

	public boolean isScaleSpeed() { return scaleSpeed; }
	public boolean isStaticDirection() { return staticDirection; }
	public Vector getLocation() { return location; }
	public Vector getParticleDirection() { return particleDirection; }
	public Vector getParticleOffdirection() { return particleOffdirection; }
	public MovingPoint location(Vector location) { this.location = location; return this; }
	public MovingPoint scaleSpeed(boolean scaleSpeed) { this.scaleSpeed = scaleSpeed; return this;}
	public MovingPoint scaleSpeed() { scaleSpeed(true); return this;}
	public MovingPoint particleDirection(Vector particleDirection) { this.particleDirection = particleDirection; return this; }
	public MovingPoint staticDirection(boolean staticDirection) {this.staticDirection = staticDirection; return this;}
	public MovingPoint particleOffdirection(Vector particleOffdirection) { this.particleOffdirection = particleOffdirection; return this; }
	@Override public boolean isForceRendering() { return forceRender; }
	@Override public Particle getParticle() { return particle; }
	@Override public MovingPoint particle(Particle particle) { this.particle = particle; return this; }
	@Override public MovingPoint particleData(Object particleData) { this.particleData = particleData; return this; }
	@Override public MovingPoint particleSpeed(float particleSpeed) { this.particleDirection.normalize().multiply(particleSpeed); return this; }
	@Override public MovingPoint forceRendering(boolean forceRender) {this.forceRender = forceRender;return this;}
}
