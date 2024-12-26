package fr.stormer3428.voidOpal.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;

public class BlockDisplayUtils {
	
	public static BlockDisplay createBlockDisplayLine(Location a, Location b, Material material, double width) {
		if(a.getWorld() != b.getWorld()) return null;

		BlockDisplay display = a.getWorld().spawn(a, BlockDisplay.class);
		Matrix4f m1 = createLocationlinkingMatrix4f(a, b, width);

		display.setTransformationMatrix(m1);
		display.setBlock(material.createBlockData());

		return display;
	}
	
	public static Matrix4f createLocationlinkingMatrix4f(Location a, Location b, double width) {return createLocationlinkingMatrix4f(a, b, width, false);}
	public static Matrix4f createLocationlinkingMatrix4f(Location a, Location b, double width, boolean containPoints) {
		if(a.getWorld() != b.getWorld()) return null;

		double dist = a.distance(b);
		Location temp = a.clone().setDirection(b.toVector().subtract(a.toVector()).normalize());
		Matrix4d m1 = new Matrix4d();

		m1.mul(getMatrix4fForRotationAlongAxis(new Vector(0,-1,0), (float) Math.toRadians(temp.getYaw())));
		m1.mul(getMatrix4fForRotationAlongAxis(new Vector(1,0,0), (float) Math.toRadians(temp.getPitch())));
		m1.translate(-width/2, -width/2, containPoints ? -width/2 : 0);
		m1.scale(width, width, dist);
		
		return new Matrix4f(m1);
	}

	public static Matrix4f createOrientatedCubeMatrix4f(Location loc, Vector dir, double size) {return createOrientatedCubeMatrix4f(loc, dir, size, -size/2, -size/2, -size/2);}
	public static Matrix4f createOrientatedCubeMatrix4f(Location loc, Vector dir, double size, double translateX, double translateY, double translateZ) {
		Location temp = loc.clone().setDirection(dir);
		Matrix4d m1 = new Matrix4d();

		m1.mul(getMatrix4fForRotationAlongAxis(new Vector(0,-1,0), (float) Math.toRadians(temp.getYaw())));
		m1.mul(getMatrix4fForRotationAlongAxis(new Vector(1,0,0), (float) Math.toRadians(temp.getPitch())));
		m1.translate(translateX,translateY,translateZ);
		m1.scale(size, size, size);
		return new Matrix4f(m1);
	}
	
	public static Location neutralize(Location loc) {
		return loc.toVector().toLocation(loc.getWorld());
	}
	
	public static Matrix4f getMatrix4fForRotationAlongAxis(Vector axis, float angle) {
		Matrix4f m1 = new Matrix4f();
		new AxisAngle4f(angle, (float) axis.getX(), (float) axis.getY(), (float) axis.getZ()).get(m1);
		return m1;
	}
}
