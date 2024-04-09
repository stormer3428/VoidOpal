package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.util.GeometryUtils;

public class Geometry {

	ArrayList<Drawable> drawables = new ArrayList<>();
	private Vector direction = GeometryUtils.VERTICAL.clone();
	private Vector relativeUp = new Vector(-1,0,0);

	public Geometry() {}
	
	public Geometry add(Drawable ... drawables) {
		this.drawables.addAll(Arrays.asList(drawables));
		return this;
	}
	
	public Geometry draw(Location location) {
		return draw(location, 1.0);
	}
	
	public Geometry draw(Location location, double scale) {
		for(Drawable drawable : drawables) drawable.draw(location, scale);
		return this;
	}
	
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
	
	public Geometry rotateAroundX(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		relativeUp.rotateAroundX(radians);
		return this;
	}
	
	public Geometry rotateAroundY(double radians) {
		for(Drawable drawable : drawables) drawable.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		relativeUp.rotateAroundY(radians);
		return this;
	}
	
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

	public Geometry setParticleSpeed(float speed) {
		for(Drawable drawable : drawables) drawable.setParticleSpeed(speed);
		return this;
	}
	
	public Geometry setParticle(Particle particle) {
		for(Drawable drawable : drawables) drawable.setParticle(particle);
		return this;
	}

	public <T> Geometry setParticleData(T particleData) {
		for(Drawable drawable : drawables) drawable.setParticleData(particleData);
		return this;
	}

	public Geometry setForceRendering(boolean forceRender) {
		for(Drawable drawable : drawables) drawable.setForceRendering(forceRender);
		return this;
	}
	
	private ArrayList<Drawable> getDrawables() {
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



}
