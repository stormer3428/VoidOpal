package fr.stormer3428.voidOpal.assets;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;

import fr.stormer3428.voidOpal.util.OMCUtil;

public class OMCExplosion {

	public OMCExplosion(Location loc, double yield) {
		this(loc, yield, null);
	}

	public OMCExplosion(Location loc, double yield, LivingEntity source) {
		this(loc, yield, source, (e) -> true);
	}
	
	public OMCExplosion(Location loc, double yield, LivingEntity source, Predicate<Entity> predicate) {
		if(loc == null) return;
		World world = loc.getWorld();
		world.spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1, 0.25,0,0.25, 0, null, true);
		world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, .8f);
		double radius = yield * 2;
		for(Entity e : world.getNearbyEntities(loc, radius, radius, radius)) if(e instanceof LivingEntity le){
			if(e.equals(source)) continue;
			if(!predicate.apply(e)) continue;
			double distance = le.getLocation().distance(loc);
			double impact = 1 - (distance / radius);
			double damage = Math.max(1, (impact * impact + impact) * 7 * yield + 1);
			if(source != null) le.damage(damage, source);
			else le.damage(damage);
			Vector delta = le.getLocation().toVector().subtract(loc.toVector()).normalize();
			le.setVelocity(delta);
		}
	}
	
	public OMCExplosion(Location loc, double radius, double damageCenter, double damageEdge, double pushForce) {
		this(loc, radius, damageCenter, damageEdge, pushForce, null);
	}
	
	public OMCExplosion(Location loc, double radius, double damageCenter, double damageEdge, double pushForce, LivingEntity source) {
		this(loc, radius, damageCenter, damageEdge, pushForce, source, (e) -> true);
	}
		
	public OMCExplosion(Location loc, double radius, double damageCenter, double damageEdge, double pushForce, LivingEntity source, Predicate<Entity> predicate) {
		if(loc == null) return;
		World world = loc.getWorld();
		world.spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1, 0.25,0,0.25, 0, null, true);
		world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5.0f, .8f);
		for(Entity e : world.getNearbyEntities(loc, radius, radius, radius)) if(e instanceof LivingEntity le){
			if(e.equals(source)) continue;
			if(!predicate.apply(e)) continue;
			double distance = le.getLocation().distance(loc);
			double damage = OMCUtil.map(0, radius, damageCenter, damageEdge, distance);
			if(source != null) le.damage(damage, source);
			else le.damage(damage);
			Vector delta = le.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(radius - distance).multiply(pushForce);
			le.setVelocity(delta);
		}
	}
	
}
