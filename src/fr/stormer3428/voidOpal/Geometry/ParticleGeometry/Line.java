package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.util.providers.OMCProvider;
import fr.stormer3428.voidOpal.util.providers.OMCProviderImpl;

public class Line implements Drawable{
	
	private OMCProvider<Vector> a;
	private OMCProvider<Vector> b;
	private Particle particle = Particle.CRIT;
	private Object particleData = null;
	private int particleAmount = 1;
	private float particleSpreadX = 0;
	private float particleSpreadY = 0;
	private float particleSpreadZ = 0;
	private float particleSpeed = 0;
	private double resolution = 0.1d;
	private boolean forceRender = true;

	public Line(Vector a, Vector b) {this(new OMCProviderImpl<>(a), new OMCProviderImpl<>(b));}
	public Line(OMCProvider<Vector> a, OMCProvider<Vector> b) {
		this.a = a;
		this.b = b;
	}
	
	public void drawPoint(World world, Vector location) {
		world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
	}

	@Override
	public Line draw(Location location, double scale) {
		World world = location.getWorld();
		Vector locationVector = location.toVector();
		for(Vector point : getInterpolatedPoints(a.getData().clone().multiply(scale), b.getData().clone().multiply(scale))) drawPoint(world, point.add(locationVector));		
		return this;
	}

	public ArrayList<Vector> getInterpolatedPoints(Vector a, Vector b){return getInterpolatedPoints(a, b, resolution);}
	public static ArrayList<Vector> getInterpolatedPoints(Vector a, Vector b, double resolution){
		double d = a.distance(b);
		int amount = (int) (d/resolution) + 2;
		Vector delta = b.clone().subtract(a).normalize().multiply(d/amount);
		ArrayList<Vector> points = new ArrayList<>();
		Vector point = a.clone();
		for(int c = amount; c > 0; c--) points.add(point.add(delta).clone());
		return points;
	}

	@Override
	public Line rotateAroundAxis(Vector axis, double radians) {
		a.getData().rotateAroundAxis(axis, radians);
		b.getData().rotateAroundAxis(axis, radians);
		return this;
	}

	@Override
	public Line rotateAroundX(double radians) {
		a.getData().rotateAroundX(radians);
		b.getData().rotateAroundX(radians);
		return this;
	}

	@Override
	public Line rotateAroundY(double radians) {
		a.getData().rotateAroundY(radians);
		b.getData().rotateAroundY(radians);
		return this;
	}

	@Override
	public Line rotateAroundZ(double radians) {
		a.getData().rotateAroundZ(radians);
		b.getData().rotateAroundZ(radians);
		return this;
	}

	public Line setParticleSpread(float particleSpread) {
		setParticleSpreadX(particleSpread);
		setParticleSpreadY(particleSpread);
		setParticleSpreadZ(particleSpread);
		return this;
	}
	
	public int getParticleAmount() {return particleAmount;}
	public void setA(Vector a) {setA(new OMCProviderImpl<>(a));}
	public void setB(Vector b) {setB(new OMCProviderImpl<>(b));}
	public void setA(OMCProvider<Vector> a) {this.a = a;}
	public void setB(OMCProvider<Vector> b) {this.b = b;}
	public Line setResolution(double resolution) {this.resolution = resolution;return this;}
	public Line setParticleSpeed(float particleSpeed) {this.particleSpeed = particleSpeed;return this;}
	public Line setParticleAmount(int particleAmount) {this.particleAmount = particleAmount; return this;}
	public Line setParticleSpreadX(float particleSpreadX) {this.particleSpreadX = particleSpreadX;return this;}
	public Line setParticleSpreadY(float particleSpreadY) {this.particleSpreadY = particleSpreadY;return this;}
	public Line setParticleSpreadZ(float particleSpreadZ) {this.particleSpreadZ = particleSpreadZ;return this;}
	public float getParticleSpeed() {return particleSpeed;}
	public float getParticleSpreadX() {return particleSpreadX;}
	public float getParticleSpreadY() {return particleSpreadY;}
	public float getParticleSpreadZ() {return particleSpreadZ;}
	public double getResolution() {return resolution;}
	public OMCProvider<Vector> getA() {return a;}
	public OMCProvider<Vector> getB() {return b;}
	@Override public Line setParticle(Particle particle) {this.particle = particle; return this;}
	@Override public Line setParticleData(Object particleData) {this.particleData = particleData;return this;}
	@Override public Line setForceRendering(boolean forceRender) {this.forceRender = forceRender;return this;}
	@Override public boolean isForceRendering() {return forceRender;}
	@Override public Particle getParticle() {return particle;}

}
