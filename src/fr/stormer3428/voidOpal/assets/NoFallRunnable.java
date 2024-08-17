package fr.stormer3428.voidOpal.assets;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.voidOpal.plugin.OMCPluginImpl;

public class NoFallRunnable extends BukkitRunnable {

	public static NoFallRunnable run(LivingEntity p) {
		NoFallRunnable noFallRunnable = new NoFallRunnable(p);
		noFallRunnable.runTaskTimer(OMCPluginImpl.getJavaPlugin(), 1, 1);
		return noFallRunnable;
	}
	
	final LivingEntity p;
	
	public NoFallRunnable(LivingEntity p) {
		this.p = p;
	}
	
	@Override
	public void run() {
		p.setFallDistance(0);
		if(p.isOnGround()) cancel();
	}
}
