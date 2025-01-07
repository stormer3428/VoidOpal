package fr.stormer3428.voidOpal.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;

public class BlockDisplayUtils {

	public static BlockDisplay createBlockDisplayLine(Location a, Location b, Material material, double width) {return createBlockDisplayLine(a, b, material, width, 0);}
	public static BlockDisplay createBlockDisplayLine(Location a, Location b, Material material, double width, double rotation) {return createBlockDisplayLine(a, b, material, width, rotation, false);}
	public static BlockDisplay createBlockDisplayLine(Location a, Location b, Material material, double width, double rotation, boolean containPoints) {
		if(a.getWorld() != b.getWorld()) return null;

		BlockDisplay display = a.getWorld().spawn(a, BlockDisplay.class);
		Matrix4d m1 = createLocationlinkingMatrix4d(a, b, width, rotation, containPoints);

		display.setTransformationMatrix(new Matrix4f(m1));
		display.setBlock(material.createBlockData());

		return display;
	}

	public static Matrix4d createLocationlinkingMatrix4d(Location a, Location b, double width) {return createLocationlinkingMatrix4d(a, b, width, 0);}
	public static Matrix4d createLocationlinkingMatrix4d(Location a, Location b, double width, double rotation) {return createLocationlinkingMatrix4d(a, b, width, rotation, false);}
	public static Matrix4d createLocationlinkingMatrix4d(Location a, Location b, double width, double rotation, boolean containPoints) {
		if(a.getWorld() != b.getWorld()) return null;

		double dist = a.distance(b);
		Vector dir = b.clone().subtract(a).toVector().normalize();
		Location temp = a.clone().setDirection(dir);
		Matrix4d m1 = new Matrix4d();
		
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(0,-1,0), (float) Math.toRadians(temp.getYaw())));
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(1,0,0), (float) Math.toRadians(temp.getPitch())));
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(0,0,1), (float) rotation));
		
		m1.translate(-width/2, -width/2, containPoints ? -width/2 : 0);
		m1.scale(width, width, containPoints ? dist + width : dist);

		return new Matrix4d(m1);
	}

	public static Matrix4d createOrientatedCubeMatrix4d(Location loc, Vector dir, double size) {return createOrientatedCubeMatrix4d(loc, dir, size, -size/2, -size/2, -size/2);}
	public static Matrix4d createOrientatedCubeMatrix4d(Location loc, Vector dir, double size, double translateX, double translateY, double translateZ) {
		Location temp = loc.clone().setDirection(dir);
		Matrix4d m1 = new Matrix4d();

		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(0,-1,0), (float) Math.toRadians(temp.getYaw())));
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(1,0,0), (float) Math.toRadians(temp.getPitch())));
		m1.translate(translateX,translateY,translateZ);
		m1.scale(size, size, size);
		return new Matrix4d(m1);
	}

	public static Location neutralize(Location loc) {
		return loc.toVector().toLocation(loc.getWorld());
	}

	public static Matrix4d getMatrix4dForRotationAlongAxis(Vector axis, float angle) {
		Matrix4d m1 = new Matrix4d();
		new AxisAngle4f(angle, (float) axis.getX(), (float) axis.getY(), (float) axis.getZ()).get(m1);
		return m1;
	}
}
