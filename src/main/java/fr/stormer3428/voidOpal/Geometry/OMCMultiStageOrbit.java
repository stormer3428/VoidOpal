package fr.stormer3428.voidOpal.Geometry;

import java.util.ArrayList;

import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.util.GeometryUtils;
import fr.stormer3428.voidOpal.util.OMCUtil;

public class OMCMultiStageOrbit {

	private final ArrayList<Vector> vectors = new ArrayList<>();
	private final ArrayList<Vector> axises = new ArrayList<>();
	private final ArrayList<Double> speeds = new ArrayList<>();

	private final int orbits;

	public OMCMultiStageOrbit(int orbits, double maxSpeed, double minSpeed, double maxLength, double minLength) {
		this.orbits = orbits;
		for(int i=0;i<orbits;i++) {
			double length = OMCUtil.map(orbits, 0, maxLength, minLength, i);
			vectors.add(GeometryUtils.getRandomVector().multiply(length));
			axises.add(GeometryUtils.getRandomVector());
			speeds.add(OMCUtil.map(orbits, 0, maxSpeed, minSpeed, i));
		}
	}

	public void tick() {
		for(int i=0;i<orbits;i++) {
			double speed = speeds.get(i);
			vectors.get(i)
			.rotateAroundAxis(
					axises.get(i)
					.rotateAroundAxis(
							GeometryUtils.getRandomVector()
							, speed)
					, speed);

		}
	}
	
	public Vector getSum() {
		Vector v = new Vector();
		vectors.parallelStream().forEach(v2->v.add(v2));
		return v;
	}

}
