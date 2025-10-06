package fr.stormer3428.voidOpal.Projectile;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;

public abstract class OMCprojectile extends BukkitRunnable{

	protected Predicate<Entity> entityPredicate = (e) -> true;
	protected boolean blockColliding = true;
	protected boolean entityColliding = true;
	protected boolean inGround = false;
	protected int maxLifespan = 20 * 30;
	protected int afterImpactLifespan = 20 * 5;
	protected Location location;
	protected Vector velocity;
	protected double projectileWidth;

	public OMCprojectile(Location location, Vector velocity, double projectileWidth) {
		this.location = location;
		this.velocity = velocity;
		this.projectileWidth = projectileWidth;
	}

	/**
	 * 
	 * @param rtr the rayTraceResult that triggered impact
	 * @return whether the projectile should stop moving or not
	 */
	public abstract boolean onHit(RayTraceResult rtr);
	public abstract void inGroundTick();
	public abstract void display();

	public void move() {
		if(inGround) return;
		if(velocity.isZero()) return;
		if(blockColliding && entityColliding) {
			RayTraceResult rtr = location.getWorld().rayTrace(location, velocity, velocity.length(), FluidCollisionMode.NEVER, true, projectileWidth, entityPredicate);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}else if(blockColliding){
			RayTraceResult rtr = location.getWorld().rayTraceBlocks(location, velocity, velocity.length(), FluidCollisionMode.NEVER, true);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}else if(entityColliding) {
			RayTraceResult rtr = location.getWorld().rayTraceEntities(location, velocity, velocity.length(), projectileWidth, entityPredicate);
			if(rtr != null && onHit(rtr)) {
				inGround = true;
				return;
			}
		}
		location.add(velocity);
	}

	@Override
	public void run() {
		move();
		display();
		if(inGround) {
			if(--afterImpactLifespan <= 0) cancel();
			else inGroundTick();
		}else if(--maxLifespan <= 0) cancel();
	}

	public boolean isBlockColliding() {return blockColliding;}
	public OMCprojectile setBlockColliding(boolean blockColliding) {this.blockColliding = blockColliding;return this;}
	public boolean isEntityColliding() {return entityColliding;}public OMCprojectile setEntityColliding(boolean entityColliding) {this.entityColliding = entityColliding;return this;}
	public Predicate<Entity> getEntityPredicate() {return entityPredicate;}
	public OMCprojectile setEntityPredicate(Predicate<Entity> entityPredicate) {this.entityPredicate = entityPredicate;return this;}
	public int getAfterImpactLifespan() {return afterImpactLifespan;}
	public OMCprojectile setAfterImpactLifespan(int afterImpactLifespan) {this.afterImpactLifespan = afterImpactLifespan;return this;}
	public OMCprojectile setMaxLifespan(int maxLifespan) {this.maxLifespan = maxLifespan;return this;}
	public int getMaxLifespan() {return maxLifespan;}public Location getLocation() {return location;}
	public OMCprojectile setLocation(Location location) {this.location = location;return this;}
	public Vector getVelocity() {return velocity;}
	public OMCprojectile setVelocity(Vector velocity) {this.velocity = velocity;return this;}
}
