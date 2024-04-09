package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.util.GeometryUtils;

public class DisplayGeometry {
	
	private Vector direction = GeometryUtils.VERTICAL.clone();
	private Vector loc;
	private World world;
	
	private ArrayList<DisplayWrapper> wrappers = new ArrayList<>();
	
	public DisplayGeometry(Location loc) {
		this(loc.toVector(), loc.getWorld());
	}

	public DisplayGeometry add(DisplayWrapper ... wrappers) {
		this.wrappers.addAll(Arrays.asList(wrappers));
		return this;
	}
	
	public DisplayGeometry(Vector loc, World world) {
		this.loc = loc;
		this.world = world;
	}
	
	public DisplayGeometry setLocation(Location loc) {
		this.loc = loc.toVector();
		this.world = loc.getWorld();
		for(DisplayWrapper wrapper : wrappers) wrapper.setOrigin(loc);
		return this;
	}
	
	public DisplayGeometry setLocation(Vector loc) {
		this.loc = loc;
		for(DisplayWrapper wrapper : wrappers) wrapper.setOrigin(loc);
		return this;
	}
	
	public DisplayGeometry setWorld(World world) {
		this.world = world;
		for(DisplayWrapper wrapper : wrappers) wrapper.setWorld(world);
		return this;
	}

	public void kill() {
		for(DisplayWrapper wrapper : wrappers) wrapper.kill();
		wrappers.clear();
	}
	
	public DisplayGeometry rotateAroundAxis(Vector axis, double radians) {
		try {
			axis.checkFinite();
		}catch (Exception e) {
			OMCLogger.systemError("Error, attempted rotate around NaN (" + axis + ")");
			return this;
		}
		for(DisplayWrapper wrapper : wrappers) wrapper.rotateAroundAxis(axis, radians);
		direction.rotateAroundAxis(axis, radians);
		return this;
	}
	
	public DisplayGeometry rotateAroundX(double radians) {
		for(DisplayWrapper wrapper : wrappers) wrapper.rotateAroundX(radians);
		direction.rotateAroundX(radians);
		return this;
	}
	
	public DisplayGeometry rotateAroundY(double radians) {
		for(DisplayWrapper wrapper : wrappers) wrapper.rotateAroundY(radians);
		direction.rotateAroundY(radians);
		return this;
	}
	
	public DisplayGeometry rotateAroundZ(double radians) {
		for(DisplayWrapper wrapper : wrappers) wrapper.rotateAroundZ(radians);
		direction.rotateAroundZ(radians);
		return this;
	}
	
	public DisplayGeometry setDirection(Vector newDirection) {
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

	public Vector getDirection() {
		return direction;
	}

	public Vector getLoc() {
		return loc;
	}

	public World getWorld() {
		return world;
	}

	public DisplayGeometry setScale(double scale) {
		for(DisplayWrapper wrapper : wrappers) wrapper.setScale(scale);
		return this;
	}
}
