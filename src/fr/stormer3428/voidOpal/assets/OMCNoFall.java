package fr.stormer3428.voidOpal.assets;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCNoFall implements PluginTied, Listener{

	private static final ArrayList<UUID> noFalllist = new ArrayList<>();
	
	public static void run(LivingEntity p) {
		noFalllist.add(p.getUniqueId());
	}

	@Override public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
	}

	@Override public void onPluginDisable() {}

	@Override public void onPluginReload() {}

	@EventHandler
	public void onFall(EntityDamageEvent e) {
		if(e.getCause()!=DamageCause.FALL) return;
		if(!noFalllist.remove(e.getEntity().getUniqueId())) return;
		e.setCancelled(true);
	}
}
