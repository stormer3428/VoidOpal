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
	private Vector particleOffsetDirection = new Vector(0,0,0);
	private boolean forceRender = true;
	private boolean staticDirection = false;
	private boolean scaleSpeed = false;
	
	@Override
	public MovingPoint draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc, 
				0, 
				particleDirection.getX() + particleOffsetDirection.getX(),
				particleDirection.getY() + particleOffsetDirection.getY(),
				particleDirection.getZ() + particleOffsetDirection.getZ(),
				particleDirection.clone().add(particleOffsetDirection).length()*(scaleSpeed ? scale : 1), particleData, forceRender);
		return this;
	}

	public void draw(Location location) {
		draw(location, 1.0);
	}

	@Override
	public MovingPoint rotateAroundAxis(Vector axis, double radians) {
		location.rotateAroundAxis(axis, radians);
		if(!staticDirection) {
			particleDirection.rotateAroundAxis(axis, radians);
			particleOffsetDirection.rotateAroundAxis(axis, radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundX(double radians) {
		location.rotateAroundX(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundX(radians);
			particleOffsetDirection.rotateAroundX(radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundY(double radians) {
		location.rotateAroundY(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundY(radians);
			particleOffsetDirection.rotateAroundY(radians);
		}
		return this;
	}

	@Override
	public MovingPoint rotateAroundZ(double radians) {
		location.rotateAroundZ(radians);
		if(!staticDirection) {
			particleDirection.rotateAroundZ(radians);
			particleOffsetDirection.rotateAroundZ(radians);
		}
		return this;
	}

	public Vector getLocation() {
		return location;
	}

	public MovingPoint setLocation(Vector location) {
		this.location = location;
		return this;
	}

	@Override
	public Particle getParticle() {
		return particle;
	}

	@Override
	public MovingPoint setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public Vector getParticleDirection() {
		return particleDirection;
	}

	public MovingPoint setParticleDirection(Vector particleDirection) {
		this.particleDirection = particleDirection;
		return this;
	}

	public Vector getParticleOffsetDirection() {
		return particleOffsetDirection;
	}

	public MovingPoint setParticleOffsetDirection(Vector particleOffsetDirection) {
		this.particleOffsetDirection = particleOffsetDirection;
		return this;
	}

	@Override
	public MovingPoint setParticleData(Object particleData) {
		this.particleData = particleData;
		return this;
	}

	@Override
	public MovingPoint setParticleSpeed(float particleSpeed) {
		this.particleDirection.normalize().multiply(particleSpeed);
		return this;
	}

	@Override
	public MovingPoint setForceRendering(boolean forceRender) {
		this.forceRender = forceRender;
		return this;
	}

	@Override
	public boolean isForceRendering() {
		return forceRender;
	}

	public MovingPoint setStaticDirection(boolean staticDirection) {
		this.staticDirection = staticDirection;
		return this;
	}

	public boolean isStaticDirection() {
		return staticDirection;
	}

	public boolean isScaleSpeed() {
		return scaleSpeed;
	}

	public void setScaleSpeed(boolean scaleSpeed) {
		this.scaleSpeed = scaleSpeed;
	}

	public MovingPoint scaleSpeed() {
		setScaleSpeed(true);
		return this;
	}

}
