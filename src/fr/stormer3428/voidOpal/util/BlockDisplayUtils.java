package fr.stormer3428.voidOpal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import fr.stormer3428.voidOpal.plugin.OMCCore;

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

		Location source = a.clone();
		if(containPoints) a.add(dir.clone().multiply(-width/2));
		return createPointingMatrix4d(source, dir, dist + (containPoints ? width : 0), width, rotation, (containPoints ? -width/2 : 0));
	}

	public static Matrix4d createPointingMatrix4d(Location start, Vector dir, double length, double width, double rotation) {return createPointingMatrix4d(start.clone().setDirection(dir), length, width, rotation, 0);}
	public static Matrix4d createPointingMatrix4d(Location start, Vector dir, double length, double width, double rotation, double translation) {return createPointingMatrix4d(start.clone().setDirection(dir), length, width, rotation, translation);}
	public static Matrix4d createPointingMatrix4d(Location start, double length, double width, double rotation, double translation) {
		Matrix4d m1 = new Matrix4d();

		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(0,-1,0), (float) Math.toRadians(start.getYaw())));
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(1,0,0), (float) Math.toRadians(start.getPitch())));
		m1.mul(getMatrix4dForRotationAlongAxis(new Vector(0,0,1), (float) rotation));
		m1.translate(0,0,translation);

		m1.translate(-width/2, -width/2, 0);
		m1.scale(width, width, length);

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

	public static final Matrix4f textBackgroundTransform = new Matrix4f();
	static {
		textBackgroundTransform.translate(0.4f, 0,0);
		textBackgroundTransform.scale(8,4,1);
	}

	@SuppressWarnings("deprecation")
	public static void screenFlash(Player p, int fadein, int stay, int fadeout, Color color, float size) {
		final List<Matrix4f> cardinalDirections = Arrays.asList(
			new Quaternionf(),
			new Quaternionf().rotateY((float) (Math.PI / 2 * 1)),
			new Quaternionf().rotateY((float) (Math.PI / 2 * 2)),
			new Quaternionf().rotateY((float) (Math.PI / 2 * 3)),
			new Quaternionf().rotateX((float) (Math.PI / 2)),
			new Quaternionf().rotateX((float) (Math.PI / -2))
			).stream().map(it ->new Matrix4f().rotate(it).scale(size, size, 1).translate(-0.5f, -0.5f, -size / 2).mul(textBackgroundTransform)).toList();
		
		World world = p.getWorld();
		List<TextDisplay> displays = new ArrayList<>();

		for (Matrix4f direction : cardinalDirections) {
			TextDisplay display = (TextDisplay) world.spawnEntity(p.getEyeLocation().toVector().toLocation(world), EntityType.TEXT_DISPLAY);
			display.setText(" ");
			display.setTransformationMatrix(direction);
			display.setBrightness(new Display.Brightness(15, 15));
			display.setBackgroundColor(color);
			display.setTeleportDuration(1);
			displays.add(display);
		}

		new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				if (++i == fadein) cancel();

				int alpha = (int) (((double) i/fadein) * 255);
				for (TextDisplay display : displays) {
					display.setBackgroundColor(Color.fromARGB(alpha, color.getRed(), color.getGreen(), color.getBlue()));
					display.teleport(p.getEyeLocation().toVector().toLocation(world));
				}
			}
		}.runTaskTimer(OMCCore.getJavaPlugin(), 0, 1);

		new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				if (++i == stay) {
					cancel();
				}
				p.setVelocity(new Vector());
				for (TextDisplay display : displays) {
					display.teleport(p.getEyeLocation().toVector().toLocation(world));
				}
			}
		}.runTaskTimer(OMCCore.getJavaPlugin(), fadein, 1);

		new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				if (++i == fadeout) {
					for (TextDisplay display : displays) display.remove();
					cancel();
				}

				int alpha = (int) (((double) i/fadeout) * 255);
				for (TextDisplay display : displays) {
					display.setBackgroundColor(Color.fromARGB(255 - alpha, color.getRed(), color.getGreen(), color.getBlue()));
					display.teleport(p.getEyeLocation().toVector().toLocation(world));
				}
			}
		}.runTaskTimer(OMCCore.getJavaPlugin(), fadein + stay, 1);
	}
}
