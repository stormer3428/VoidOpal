package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Point implements Drawable{

	protected Vector location = new Vector();
	protected Particle particle = Particle.CRIT;
	protected Object particleData = null;
	protected int particleAmount = 1;
	protected float particleSpreadX = 0;
	protected float particleSpreadY = 0;
	protected float particleSpreadZ = 0;
	protected float particleSpeed = 0;
	protected boolean forceRender = true;

	@Override
	public Point draw(Location location, double scale) {
		World world = location.getWorld();
		Location particleLoc = location.clone().add(this.location.clone().multiply(scale));
		world.spawnParticle(particle, particleLoc, particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
		return this;
	}

	public int getParticleAmount() {return particleAmount;}
	public float getParticleSpreadX() {return particleSpreadX;}
	public float getParticleSpreadY() {return particleSpreadY;}
	public float getParticleSpreadZ() {return particleSpreadZ;}
	public float getParticleSpeed() {return particleSpeed;}
	public Point setLocation(Vector location) {this.location = location;return this;}
	public Point setParticleAmount(int particleAmount) {this.particleAmount = particleAmount;return this;}
	public Point setParticleSpread(float particleSpread) {setParticleSpreadX(particleSpread); setParticleSpreadY(particleSpread); setParticleSpreadZ(particleSpread); return this;}
	public Point setParticleSpreadX(float particleSpreadX) {this.particleSpreadX = particleSpreadX;return this;}
	public Point setParticleSpreadY(float particleSpreadY) {this.particleSpreadY = particleSpreadY;return this;}
	public Point setParticleSpreadZ(float particleSpreadZ) {this.particleSpreadZ = particleSpreadZ;return this;}
	public Point setParticleSpeed(float particleSpeed) {this.particleSpeed = particleSpeed;return this;}
	public Vector getLocation() {return location;}
	@Override public Point rotateAroundAxis(Vector axis, double radians) {location.rotateAroundAxis(axis, radians);return this;}
	@Override public Point rotateAroundX(double radians) {location.rotateAroundX(radians);return this;}
	@Override public Point rotateAroundY(double radians) {location.rotateAroundY(radians);return this;}
	@Override public Point rotateAroundZ(double radians) {location.rotateAroundZ(radians);return this;}
	@Override public Point setParticle(Particle particle) {this.particle = particle;return this;}
	@Override public Point setParticleData(Object particleData) {this.particleData = particleData;return this;}
	@Override public Point setForceRendering(boolean forceRender) {this.forceRender = forceRender;return this;}
	@Override public boolean isForceRendering() {return forceRender;}
	@Override public Particle getParticle() {return particle;}

}
