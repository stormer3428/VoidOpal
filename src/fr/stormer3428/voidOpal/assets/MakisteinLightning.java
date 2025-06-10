package fr.stormer3428.voidOpal.assets;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.util.GeometryUtils;
import fr.stormer3428.voidOpal.util.ParticleUtils;

public class MakisteinLightning extends BukkitRunnable{

	private static final double STEP_LENGTH = .75;
	private static final double RAND_LENGTH = .55;
	private static final double BRANCHING_CHANCE = .25;
	private static final int SUBTICKS = 7;

	private final Random random = new Random();

	private Vector dir;
	private final int branchingLevel;
	public boolean instant = false;
	public boolean directed = true;

	private Location loc;
	private Vector lastRandDir = new Vector();
	private int lifespan;

	public MakisteinLightning(Location loc, Vector dir, int lifespan) {this(loc, dir, lifespan, 1);}
	public MakisteinLightning(Location loc, Vector dir, int lifespan, int branchingLevel) {this(loc, dir, lifespan, branchingLevel, false);}
	public MakisteinLightning(Location loc, Vector dir, int lifespan, int branchingLevel, boolean instant) {
		this.loc = loc.clone();
		this.dir = dir.clone();
		this.lifespan = lifespan;
		this.branchingLevel = branchingLevel;
		this.instant = instant;

		loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, lifespan/10f, 1.5f + random.nextFloat()*.5f);
		loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, lifespan/6f, .5f + random.nextFloat()*.1f);
		loc.getWorld().playSound(loc, Sound.ENTITY_WARDEN_HEARTBEAT, lifespan/2f, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, lifespan/3f, 1.7f + random.nextFloat()*.3f - lifespan/100f);
	}
	
	@Override public void run() {
		do {
			if(--lifespan<=0) {
				cancel();
				return;
			}
			for(int i = SUBTICKS; i > 0; i--) {
				Vector randDir = GeometryUtils.getRandomVector().subtract(lastRandDir.multiply(.5)).normalize();
				lastRandDir = randDir;
				if(branchingLevel > 0 && random.nextDouble() < BRANCHING_CHANCE) {
					MakisteinLightning branch = new MakisteinLightning(
					loc, 
					dir.clone().add(GeometryUtils.getRandomVector().multiply(RAND_LENGTH)).normalize(), 
					random.nextInt(3) + lifespan / 7,
					branchingLevel-1
					);
					if(instant) branch.instant();
					branch.runTaskTimer(OMCCore.getJavaPlugin(), 0, 1);
//					loc.getWorld().spawnParticle(Particle.FLASH, loc, 1, 0,0,0, 0, null, true);
				}
				Location newLoc = loc.clone()
					.add(dir.clone().multiply(STEP_LENGTH))
					.add(randDir.clone().multiply(RAND_LENGTH))
					;
				
				if(!directed) dir = newLoc.clone().subtract(loc).toVector().normalize();
	
				ParticleUtils.drawParticleLine(loc, newLoc, Particle.ELECTRIC_SPARK, .1, 1, new Vector(), 0, null, true);
	
				loc = newLoc;
			}
		}while(instant);
	}

	public MakisteinLightning instant() { this.instant = true; return this; }
	public MakisteinLightning undirected() { this.directed = false; return this; }

}
