package fr.stormer3428.voidOpal.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class GeometryUtils {

	public static final Vector VERTICAL = new Vector(0,1,0);
	public static final Random RANDOM = new Random();
	
	public static Vector lerp(Vector a, Vector b, double i) {
		return a.clone().multiply(1 - i).add(b.clone().multiply(i));
	}
	
	public static Vector validateVector(Vector v) {
		if(v.getX() == 0) v.setX(Float.MIN_VALUE);
		if(v.getY() == 0) v.setY(Float.MIN_VALUE);
		if(v.getZ() == 0) v.setZ(Float.MIN_VALUE);
		return v;
	}

	public static Vector getPerpendicularVector(Vector v) {
		Vector direction = validateVector(v.clone());
		return new Vector(1, 1, (-direction.getX() * 1 - direction.getY() * 1) / direction.getZ()).normalize();
	}

	public static Vector getRandomVector() {
		return getRandomVector(RANDOM);
	}

	public static Vector[] getRandomVectors(int amount) {
		Vector[] vectors = new Vector[amount];
		for(int i = amount;i>0;i--) vectors[i-1]=getRandomVector();
		return vectors;
	}

	public static Vector getRandomVector(Random r) {
		return new Vector((r.nextDouble() * 2)-1d,(r.nextDouble() * 2)-1d,(r.nextDouble() * 2)-1d).normalize();
	}

	public static Vector getRelativeUpUnitVector(Location loc) {
		final Location location = loc.clone();
		location.setPitch(loc.getPitch() + 90);
		return location.getDirection();
	}
	
	public static ArrayList<Vector> getCircularVectors(int amount){
		ArrayList<Vector> list = new ArrayList<>();
		if(amount <= 0) return list;
		double angle = Math.PI * 2 / amount;
		Vector v = new Vector(1,0,0);
		for(int i = amount; i > 0; i--) list.add(v.rotateAroundY(angle).clone());
		return list;
	}
	
	public static ArrayList<Vector> getCircularVectors(int amount, Vector axis){
		ArrayList<Vector> list = new ArrayList<>();
		if(amount <= 0) return list;
		double angle = Math.PI * 2 / amount;
		Vector v = getPerpendicularVector(axis);
		for(int i = amount; i > 0; i--) list.add(v.rotateAroundAxis(axis, angle).clone());
		return list;
	}
	
	public static ArrayList<Vector> getSphericalVectors(double radius, boolean hollow){
		ArrayList<Vector> list = new ArrayList<>();
		double radiusSQ = radius * radius;
		double innerRadiusSQ = (radius-1) * (radius-1);

		int min = (int) (- radius - 1);
		int max = (int) (radius + 1);
		
		for(int x = max; x > min; x--) for(int y = max; y > min; y--) for(int z = max; z > min; z--) {
			double distance = x*x + y*y + z*z;
			if(distance > radiusSQ) continue;
			if(hollow && distance < innerRadiusSQ) continue;
			list.add(new Vector(x,y,z));
		}
		return list;
	}
	
	public static ArrayList<Vector> getVectorCube(int size){
		ArrayList<Vector> list = new ArrayList<>();

		int min = - size;
		int max = size;
		
		for(int x = max; x > min; x--) for(int y = max; y > min; y--) for(int z = max; z > min; z--) list.add(new Vector(x,y,z));
		
		return list;
	}
	
	public static ArrayList<Location> getSphericalLocations(Block center, double radius, boolean hollow){
		ArrayList<Location> list = new ArrayList<Location>();
		for(Vector v : getSphericalVectors(radius, hollow)) list.add(center.getLocation().add(v));
		return list;
	}
	
	public static Transformation getCenteredBlockTransformation() {
		return new Transformation(new Vector3f(-.5f,-.5f,-.5f), new AxisAngle4f(), new Vector3f(1,1,1), new AxisAngle4f());
	}
}
