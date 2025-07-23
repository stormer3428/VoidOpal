package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.util.GeometryUtils;

public class Geometry implements Drawable{

	protected ArrayList<Drawable> drawables = new ArrayList<>();
	protected Vector direction = GeometryUtils.VERTICAL.clone();
	protected Vector relativeUp = new Vector(-1,0,0);

	public Geometry() {}
	
	public Geometry add(Drawable ... drawables) {
		this.drawables.addAll(Arrays.asList(drawables));
		return this;
	}
	
	public Geometry draw(Location location) {
		return draw(location, 1.0);
	}
	
	@Override
	public Geometry draw(Location location, double scale) {
		for(Drawable drawable : drawables) drawable.draw(location, scale);
		return this;
	}

	@Override
	public Geometry rotateAroundAxis(Vector axis, double radians) {
		try {
			axis.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted rotate around NaN (" + axis + ")");
			return this;
		}
		for(Drawable drawable : drawables) drawable.rotateAroundAxis(axis, radians);
		direction.rotateAroundAxis(axis, radians);
		relativeUp.rotateAroundAxis(axis, radians);
		return this;
	}

	@Override
	public Geometry rotateAroundX(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		relativeUp.rotateAroundX(radians);
		return this;
	}

	@Override
	public Geometry rotateAroundY(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		relativeUp.rotateAroundY(radians);
		return this;
	}

	@Override
	public Geometry rotateAroundZ(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundZ(radians);
		direction.rotateAroundZ(radians);
		relativeUp.rotateAroundZ(radians);
		return this;
	}
	
	public Geometry setDirection(Vector newDirection) {
		if(newDirection.equals(direction)) return this;
		try {
			newDirection.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted to set direction to NaN (" + newDirection + ")");
			return this;
		}
		Vector crossProduct = direction.getCrossProduct(newDirection);
		crossProduct.normalize();
		try {
			crossProduct.checkFinite();
		}catch (Exception e) {
			OMCLogger.debug("Error, the cross product from old direction " + direction + " and the new direction " + newDirection + " is NaN (delta is" + newDirection.clone().subtract(direction) + ")");
			return this;
		}
		rotateAroundAxis(crossProduct, direction.angle(newDirection));
		return this;
	}
	
	public Geometry setRelativeUp(Vector newRelativeUp) {
		if(newRelativeUp.equals(relativeUp)) return this;
		try {
			newRelativeUp.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted to set relativeUp to NaN (" + newRelativeUp + ")");
			return this;
		}
		Vector crossProduct = relativeUp.getCrossProduct(newRelativeUp);
		crossProduct.normalize();
		try {
			crossProduct.checkFinite();
		}catch (Exception e) {
			OMCLogger.debug("Error, the cross product from old relativeUp " + relativeUp + " and the new relativeUp " + newRelativeUp + " is NaN (delta is" + newRelativeUp.clone().subtract(relativeUp) + ")");
			return this;
		}
		rotateAroundAxis(crossProduct, relativeUp.angle(newRelativeUp));
		return this;
	}

	public Vector getDirection() {
		return direction;
	}
	
	public Vector getRelativeUp() {
		return relativeUp;
	}

	@Override
	public Geometry particleSpeed(float speed) {
		for(Drawable drawable : drawables) drawable.particleSpeed(speed);
		return this;
	}

	@Override
	public Geometry particle(Particle particle) {
		for(Drawable drawable : drawables) drawable.particle(particle);
		return this;
	}

	@Override
	public <T> Geometry particleData(T particleData) {
		for(Drawable drawable : drawables) drawable.particleData(particleData);
		return this;
	}

	@Override
	public Geometry forceRendering(boolean forceRender) {
		for(Drawable drawable : drawables) drawable.forceRendering(forceRender);
		return this;
	}
	
	protected ArrayList<Drawable> getDrawables() {
		return drawables;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Drawable>  ArrayList<T> getDrawables(Class<T> clazz) {
		ArrayList<T> list = new ArrayList<>();
		for(Drawable drawable : drawables) if(clazz.isInstance(drawable)) list.add((T) drawable);
		return list;
	}

	public Geometry merge(Geometry other) {
		for(Drawable drawable : other.getDrawables()) add(drawable);
		return this;
	}

	@Override
	public Particle getParticle() {
		if(getDrawables().isEmpty()) return null;
		return getDrawables().get(0).getParticle();
	}

	@Override
	public boolean isForceRendering() {
		if(getDrawables().isEmpty()) return false;
		return getDrawables().get(0).isForceRendering();
	}
}
