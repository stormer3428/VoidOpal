package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.plugin.OMCCore;

public class MovingPoint implements Drawable{

	protected onDrawConsumer callback = null;
	protected Vector location = new Vector();
	protected Particle particle = Particle.CRIT;
	protected Object particleData = null;
	protected Vector particleDirection = new Vector(0,0,0);
	protected Vector particleOffdirection = new Vector(0,0,0);
	protected int delay = 0;
	protected double drawChance = 1;
	protected boolean forceRender = true;
	protected boolean staticDirection = false;
	protected boolean scaleSpeed = false;

	public void draw(Location location) { draw(location, 1.0); }
	@Override public MovingPoint draw(Location location, double scale) {
		if(Math.random() > drawChance) return this;
		if(callback != null) callback.onDraw(this, location, scale);
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		
		if(delay <= 0) {
			world.spawnParticle(particle, particleLoc, 
				0, 
				particleDirection.getX() + particleOffdirection.getX(),
				particleDirection.getY() + particleOffdirection.getY(),
				particleDirection.getZ() + particleOffdirection.getZ(),
				particleDirection.clone().add(particleOffdirection).length()*(scaleSpeed ? scale : 1), particleData, forceRender);
			return this;
		}
		Bukkit.getScheduler().runTaskLater(OMCCore.getJavaPlugin(), ()->{
			world.spawnParticle(particle, particleLoc, 
				0, 
				particleDirection.getX() + particleOffdirection.getX(),
				particleDirection.getY() + particleOffdirection.getY(),
				particleDirection.getZ() + particleOffdirection.getZ(),
				particleDirection.clone().add(particleOffdirection).length()*(scaleSpeed ? scale : 1), particleData, forceRender);
		}, delay);
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

	public int getDelay() { return delay; }
	public boolean isScaleSpeed() { return scaleSpeed; }
	public boolean isStaticDirection() { return staticDirection; }
	public double getDrawChance() { return drawChance; }
	public Vector getLocation() { return location; }
	public Vector getParticleDirection() { return particleDirection; }
	public Vector getParticleOffdirection() { return particleOffdirection; }
	public MovingPoint delay(int delay) { this.delay = delay; return this;}
	public MovingPoint location(Vector location) { this.location = location; return this; }
	public MovingPoint drawChance(double drawChance) {this.drawChance = drawChance; return this;}
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
	@Override public MovingPoint onDraw(onDrawConsumer consumer) { callback = consumer; return this; }
}
