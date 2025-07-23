package fr.stormer3428.voidOpal.util;

import java.util.ArrayList;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class EntityUtils {

	public static final ArrayList<Material> SEE_THROUGH = new ArrayList<Material>();

	static {
		for(Material material : Material.values()) {
			if(material.isOccluding()) continue;
			SEE_THROUGH.add(material);
		}
	}
	
	public static boolean canSee(LivingEntity looker, LivingEntity target, double range) {
		if(!looker.getWorld().equals(target.getWorld())) return false;
		double rangeSq = range * range;
		if(looker.getLocation().distanceSquared(target.getLocation()) > rangeSq) return false;
		World w = looker.getWorld();

		if(true) {
			Location source = looker.getEyeLocation();
			Location targetLoc = target.getLocation();
			Vector toTarget = targetLoc.toVector().subtract(source.toVector()).normalize();
			double distanceLeft = range;
			
			while(distanceLeft > 0) {
				RayTraceResult rtr = w.rayTrace(source, toTarget, distanceLeft, FluidCollisionMode.NEVER, true, 0, (e) -> e.equals(target));
				if(rtr == null) break;
				if(rtr.getHitBlock() != null) {
					if(!SEE_THROUGH.contains(rtr.getHitBlock().getType())) break;
					distanceLeft -= rtr.getHitPosition().toLocation(w).distance(source);
					source = rtr.getHitPosition().toLocation(w).add(toTarget.clone().multiply(0.1));
					continue;
				}
				return true;
			}
		}
		if(true) {
			Location source = looker.getEyeLocation();
			Location targetLoc = target.getLocation().add(0, target.getHeight(), 0);
			Vector toTarget = targetLoc.toVector().subtract(source.toVector()).normalize();
			double distanceLeft = range;
			
			while(distanceLeft > 0) {
				RayTraceResult rtr = w.rayTrace(source, toTarget, distanceLeft, FluidCollisionMode.NEVER, true, 0, (e) -> e.equals(target));
				if(rtr == null) break;
				if(rtr.getHitBlock() != null) {
					if(!SEE_THROUGH.contains(rtr.getHitBlock().getType())) break;
					distanceLeft -= rtr.getHitPosition().toLocation(w).distance(source);
					source = rtr.getHitPosition().toLocation(w).add(toTarget.clone().multiply(0.1));
					continue;
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean isUnderRain(LivingEntity le) {
		return le.getWorld().hasStorm() && isUnderSky(le) && !isInHotArea(le);
	}

	public static boolean isUnderSnow(LivingEntity le) {
		return isUnderRain(le) && isInColdArea(le);
	}

	public static boolean isUnderSky(LivingEntity le) {
		return le.getWorld().getHighestBlockAt(le.getLocation()).getY() < le.getLocation().getY() + 1;
	}
	
	public static boolean isInColdArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() <= 0.15;
	}

	public static boolean isInWarmArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() >= 0.5;
	}

	public static boolean isInHotArea(LivingEntity le) {
		return le.getLocation().getBlock().getTemperature() > 1;
	}

	public static int getSunlight(LivingEntity le) {
		return le.getLocation().getBlock().getLightFromSky();
	}

	public static boolean isWet(LivingEntity le) {
		return le.isInWater() || isUnderRain(le);
	}
	
	public static boolean isSubmerged(LivingEntity le) {
		Material b = le.getEyeLocation().getBlock().getType();

		boolean bool = le.getEyeLocation().getBlock().getBlockData() instanceof Waterlogged w && w.isWaterlogged()
				|| b.equals(Material.WATER)
				|| b.equals(Material.KELP)
				|| b.equals(Material.SEAGRASS)
				|| b.equals(Material.TALL_SEAGRASS)
				|| b.equals(Material.BUBBLE_COLUMN)
				;
		return bool;
	}

	public static boolean isLookingAtEntity(LivingEntity looker, Entity target) {
		Location startLocation = looker.getEyeLocation();
		Vector direction = startLocation.getDirection();
		double maxDistance = startLocation.distance(target.getLocation()) + 1; //add 1 in case looking from bottom
		
		RayTraceResult rtr = startLocation.getWorld().rayTrace(startLocation, direction, maxDistance, FluidCollisionMode.NEVER, true, 0.0, (t) -> t.equals(target));
		return rtr != null;
	}

	public static Location interpolateLocation(Location first, Location second, float weight) {
		if(weight <= 0) return first.clone();
		if(weight >= 1) return second.clone();
		float w1 = 1-weight;
		float w2 = weight;
		Location loc = new Location(first.getWorld(), 
				first.getX() * w1 + second.getX() * w2,
				first.getY() * w1 + second.getY() * w2,
				first.getZ() * w1 + second.getZ() * w2
				);
		loc.setDirection(first.getDirection().multiply(w1).add(second.getDirection().multiply(w2)));
		return loc;
	}

	
}
