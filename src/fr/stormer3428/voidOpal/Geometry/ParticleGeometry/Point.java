package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Point implements Drawable{

	protected Vector location;
	protected Particle particle = Particle.CRIT_MAGIC;
	protected Object particleData = null;
	protected int particleAmount = 1;
	protected float particleSpreadX = 0;
	protected float particleSpreadY = 0;
	protected float particleSpreadZ = 0;
	protected float particleSpeed = 0;
	protected boolean forceRender = true;

	@Override
	public void draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc, particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
	}

	@Override
	public Point rotateAroundAxis(Vector axis, double radians) {
		location.rotateAroundAxis(axis, radians);
		return this;
	}

	@Override
	public Point rotateAroundX(double radians) {
		location.rotateAroundX(radians);
		return this;
	}

	@Override
	public Point rotateAroundY(double radians) {
		location.rotateAroundY(radians);
		return this;
	}

	@Override
	public Point rotateAroundZ(double radians) {
		location.rotateAroundZ(radians);
		return this;
	}

	public Vector getLocation() {
		return location;
	}

	public Point setLocation(Vector location) {
		this.location = location;
		return this;
	}

	@Override
	public Particle getParticle() {
		return particle;
	}

	@Override
	public Point setParticle(Particle particle) {
		this.particle = particle;
		return this;
	}

	public int getParticleAmount() {
		return particleAmount;
	}

	public Point setParticleAmount(int particleAmount) {
		this.particleAmount = particleAmount;
		return this;
	}

	public Point setParticleSpread(float particleSpread) {
		setParticleSpreadX(particleSpread);
		setParticleSpreadY(particleSpread);
		setParticleSpreadZ(particleSpread);
		return this;
	}

	public float getParticleSpreadX() {
		return particleSpreadX;
	}

	public Point setParticleSpreadX(float particleSpreadX) {
		this.particleSpreadX = particleSpreadX;
		return this;
	}

	public float getParticleSpreadY() {
		return particleSpreadY;
	}

	public Point setParticleSpreadY(float particleSpreadY) {
		this.particleSpreadY = particleSpreadY;
		return this;
	}

	public float getParticleSpreadZ() {
		return particleSpreadZ;
	}

	public Point setParticleSpreadZ(float particleSpreadZ) {
		this.particleSpreadZ = particleSpreadZ;
		return this;
	}

	public float getParticleSpeed() {
		return particleSpeed;
	}

	public Point setParticleSpeed(float particleSpeed) {
		this.particleSpeed = particleSpeed;
		return this;
	}
	
	@Override
	public Point setParticleData(Object particleData) {
		this.particleData = particleData;
		return this;
	}
	
	@Override
	public Point setForceRendering(boolean forceRender) {
		this.forceRender = forceRender;
		return this;
	}

	@Override
	public boolean isForceRendering() {
		return forceRender;
	}
}
