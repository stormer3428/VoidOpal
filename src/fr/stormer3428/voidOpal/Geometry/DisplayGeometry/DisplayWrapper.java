package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

public abstract class DisplayWrapper<T extends Display> {

	protected T display;
	protected Class<T> displayClass;
	protected Location origin;
	protected Vector location;
	protected Vector direction;
	protected double scale = 1.0;

	private int interpolationDelay = 0;
	private int teleportDuration = 0;
	private float shadowRadius = 0.0f;
	private float viewRange = 32.0f;
	private Color glowColorOverride = null;
	private boolean glowing = false;
	private Matrix4f transformationMatrix = null;
	private Billboard billboard = null;
	private Brightness brightness = null;
	private Transformation transformation = null;
	
	public DisplayWrapper<T> setScale(double scale) {this.scale = scale; this.display.setDisplayHeight((float) scale); this.display.setDisplayWidth((float) scale); return updatePosition();}
	public DisplayWrapper<T> setWorld(World world) {this.origin.setWorld(world); return updatePosition();}
	public DisplayWrapper<T> setOrigin(Vector origin) {return setOrigin(origin.toLocation(this.origin.getWorld()));}
	public DisplayWrapper<T> setOrigin(Location origin) {this.origin = origin;return updatePosition();}
	public DisplayWrapper<T> setGlowing(boolean glowing) {this.glowing = glowing;return this;}
	public DisplayWrapper<T> setLocation(Vector loc) {this.location = loc; return updatePosition();}
	public DisplayWrapper<T> setBillboard(Billboard billboard) {this.billboard = billboard; return this;}
	public DisplayWrapper<T> setDirection(Vector direction) {this.direction = direction;return updatePosition();}
	public DisplayWrapper<T> setViewRange(float viewRange) {this.viewRange = viewRange;return this;}
	public DisplayWrapper<T> setBrightness(Brightness brightness) {this.brightness = brightness;return this;}
	public DisplayWrapper<T> setShadowRadius(float shadowRadius) {this.shadowRadius = shadowRadius;return this;}
	public DisplayWrapper<T> setTransformation(Transformation transformation) {this.transformation = transformation;return this;}
	public DisplayWrapper<T> setGlowColorOverride(Color glowColorOverride) {this.glowColorOverride = glowColorOverride;return this;}
	public DisplayWrapper<T> setInterpolationDelay(int interpolationDelay) {this.interpolationDelay = interpolationDelay;return this;}
	public DisplayWrapper<T> setTransformationMatrix(Matrix4f transformationMatrix) {this.transformationMatrix = transformationMatrix;return this;}
	
	public T getDisplay() {return display;}
	public int getInterpolationDelay() {return interpolationDelay;}
	public float getShadowRadius() {return shadowRadius;}
	public float getViewRange() {return viewRange;}
	public Color getGlowColorOverride() {return glowColorOverride;}
	public Vector getDirection() {return direction;}
	public boolean isGlowing() {return glowing;}
	public Matrix4f getTransformationMatrix() {return transformationMatrix;}
	public Billboard getBillboard() {return billboard;}
	public Brightness getBrightness() {return brightness;}
	public Transformation getTransformation() {return transformation;}
	
	public DisplayWrapper(Class<T> displayClass, Location origin) {this(displayClass, origin, new Vector());}
	public DisplayWrapper(Class<T> displayClass, Location origin, Vector location) {this(displayClass, origin, location, origin.getDirection());}
	public DisplayWrapper(Class<T> displayClass, Location origin, Vector location, Vector direction) {
		this.displayClass = displayClass;
		this.origin = origin;
		this.location = location;
		this.direction = direction;
	}
	
	public DisplayWrapper<T> updatePosition() {
		if(display != null && display.isDead()) display = null; if(display == null) return this;
		display.teleport(getWorldLocation());
		return this;
	}

	public DisplayWrapper<T> rotateAroundAxis(Vector axis, double radians) {
		this.direction.rotateAroundAxis(axis, radians);
		this.location.rotateAroundAxis(axis, radians);
		return updatePosition();
	}

	public DisplayWrapper<T> rotateAroundX(double radians) {
		this.direction.rotateAroundX(radians);
		this.location.rotateAroundX(radians);
		return updatePosition();
	}

	public DisplayWrapper<T> rotateAroundY(double radians) {
		this.direction.rotateAroundY(radians);
		this.location.rotateAroundY(radians);
		return updatePosition();
	}

	public DisplayWrapper<T> rotateAroundZ(double radians) {
		this.direction.rotateAroundZ(radians);
		this.location.rotateAroundZ(radians);
		return updatePosition();
	}

	public Location getWorldLocation() {
		Location worldLocation = origin.clone().add(location.clone().multiply(scale));
		worldLocation.setDirection(direction);
		return worldLocation;
	}

	public boolean create() {
		if(display != null && display.isDead()) display = null; if(display != null) return false;
		Location loc = getWorldLocation();
		display = loc.getWorld().spawn(loc, displayClass);
		if(billboard != null) display.setBillboard(billboard);
		if(brightness != null) display.setBrightness(brightness);
		if(glowColorOverride != null) display.setGlowColorOverride(glowColorOverride);
		display.setInterpolationDelay(interpolationDelay);
		display.setShadowRadius(shadowRadius);
		display.setTeleportDuration(teleportDuration);
		if(transformation != null) display.setTransformation(transformation);
		if(transformationMatrix != null) display.setTransformationMatrix(transformationMatrix);
		display.setViewRange(viewRange);
		display.setGlowing(glowing);
		return true;
	}

	public boolean destroy() {
		if(display != null && display.isDead()) display = null; if(display == null) return false;
		display.remove();
		display = null;
		return true;
	}
	
}
