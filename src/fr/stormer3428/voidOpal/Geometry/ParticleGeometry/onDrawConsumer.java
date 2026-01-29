package fr.stormer3428.voidOpal.Geometry.ParticleGeometry;

import org.bukkit.Location;

@FunctionalInterface
public interface onDrawConsumer {

	public void onDraw(Drawable self, Location location, double size);

}