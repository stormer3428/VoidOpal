package fr.stormer3428.voidOpal.assets;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.stormer3428.voidOpal.Listener.OMCNamedListener;
import fr.stormer3428.voidOpal.Listener.OMCNamedListenerManager;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

public class OMCNoFallmanager extends OMCNamedListener{

	public OMCNoFallmanager() { super("omcNoFallManager"); }

	private static final HashSet<UUID> noFalllist = new HashSet<>();
	
	public static void run(LivingEntity p) { noFalllist.add(p.getUniqueId()); }

	@Override public void onPluginEnable() {
		OMCNamedListenerManager manager = OMCCore.getPluginTied(OMCNamedListenerManager.class);
		if(manager == null) {
			OMCLogger.systemError("Error, Attempted to run OMCNoFallmanager without an OMCNamedListenerManager on the classpath");
			return;
		}
		manager.registerListener(this);
	}

	@EventHandler
	public void onFall(EntityDamageEvent e) {
		if(e.getCause()!=DamageCause.FALL) return;
		if(!noFalllist.remove(e.getEntity().getUniqueId())) return;
		e.setCancelled(true);
	}
}
