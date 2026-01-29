package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.util.GeometryUtils;

public class Circle implements Drawable{

	protected onDrawConsumer callback = null;
	private Vector axis;
	private ArrayList<Vector> points = new ArrayList<>();
	
	private double radius;

	private Particle particle = Particle.CRIT;
	private Object particleData = null;
	private int particleAmount = 1;
	private float particleSpreadX = 0;
	private float particleSpreadY = 0;
	private float particleSpreadZ = 0;
	private float particleSpeed = 0;
	private double resolution = 0.1d;
	private double drawChance = 1;
	private boolean forceRender = true;

	public Circle(Vector axis, double radius) {
		this.axis = axis;
		this.radius = radius;
		updatePoints();
	}
	
	private void updatePoints() {
		points.clear();
		points.addAll(getInterpolatedPoints(axis.clone(), radius, resolution));
	}

	public void drawPoint(World world, Vector location) {
		if(Math.random() > drawChance) return;
		world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), particleAmount, particleSpreadX, particleSpreadY, particleSpreadZ, particleSpeed, particleData, forceRender);
	}

	@Override
	public Circle draw(Location location, double scale) {
		if(callback != null) callback.onDraw(this, location, scale);
		World world = location.getWorld();
		Vector locationVector = location.toVector();
		for(Vector point : points) drawPoint(world, point.add(locationVector));		
		return this;
	}

	public ArrayList<Vector> getInterpolatedPoints(Vector axis, double radius){return getInterpolatedPoints(axis, radius, resolution);}
	public static ArrayList<Vector> getInterpolatedPoints(Vector axis, double radius, double resolution){
		double circumference = Math.PI * 2 * radius;
		int amount = (int) (circumference/resolution) + 2;
		return GeometryUtils.getCircularVectors(amount, axis);
	}

	@Override
	public Circle rotateAroundAxis(Vector axis, double radians) {
		this.axis.rotateAroundAxis(axis, radians);
		points.forEach(v->v.rotateAroundAxis(axis, radians));
		return this;
	}

	@Override
	public Circle rotateAroundX(double radians) {
		this.axis.rotateAroundX(radians);
		points.forEach(v->v.rotateAroundX(radians));
		return this;
	}

	@Override
	public Circle rotateAroundY(double radians) {
		this.axis.rotateAroundY(radians);
		points.forEach(v->v.rotateAroundY(radians));
		return this;
	}

	@Override
	public Circle rotateAroundZ(double radians) {
		this.axis.rotateAroundZ(radians);
		points.forEach(v->v.rotateAroundZ(radians));
		return this;
	}

	public Circle particleSpread(float particleSpread) {
		particleSpreadX(particleSpread);
		particleSpreadY(particleSpread);
		particleSpreadZ(particleSpread);
		return this;
	}

	public int getParticleAmount() {return particleAmount;}
	public Vector getAxis() { return axis; }
	public Object getParticleData() { return particleData; }
	public Circle axis(Vector axis) {this.axis = axis; return this;}
	public Circle radius(double radius) { this.radius = radius; updatePoints(); return this;}
	public Circle drawChance(double drawChance) {this.drawChance = drawChance; return this;}
	public Circle resolution(double resolution) {this.resolution = resolution; updatePoints(); return this;}
	public Circle particleSpeed(float particleSpeed) {this.particleSpeed = particleSpeed;return this;}
	public Circle particleAmount(int particleAmount) {this.particleAmount = particleAmount; return this;}
	public Circle particleSpreadX(float particleSpreadX) {this.particleSpreadX = particleSpreadX;return this;}
	public Circle particleSpreadY(float particleSpreadY) {this.particleSpreadY = particleSpreadY;return this;}
	public Circle particleSpreadZ(float particleSpreadZ) {this.particleSpreadZ = particleSpreadZ;return this;}
	public float getParticleSpeed() {return particleSpeed;}
	public float getParticleSpreadX() {return particleSpreadX;}
	public float getParticleSpreadY() {return particleSpreadY;}
	public float getParticleSpreadZ() {return particleSpreadZ;}
	public double getRadius() { return radius; }
	public double getDrawChance() { return drawChance; }
	public double getResolution() {return resolution;}
	@Override public Circle particle(Particle particle) {this.particle = particle; return this;}
	@Override public Circle particleData(Object particleData) {this.particleData = particleData;return this;}
	@Override public Circle forceRendering(boolean forceRender) {this.forceRender = forceRender;return this;}
	@Override public boolean isForceRendering() {return forceRender;}
	@Override public Particle getParticle() {return particle;}
	@Override public Circle onDraw(onDrawConsumer consumer) { callback = consumer; return this; }

}
