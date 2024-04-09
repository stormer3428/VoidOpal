package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public interface DisplayWrapper {

	public void kill();

	public DisplayWrapper setLocation(Vector loc);
	public DisplayWrapper setDirection(Vector direction);
	public DisplayWrapper setWorld(World world);
	public DisplayWrapper setOrigin(Vector origin);
	public DisplayWrapper setOrigin(Location origin);
	public DisplayWrapper setScale(double scale);

	public DisplayWrapper rotateAroundAxis(Vector axis, double radians);
	public DisplayWrapper rotateAroundX(double radians);
	public DisplayWrapper rotateAroundY(double radians);
	public DisplayWrapper rotateAroundZ(double radians);
}
