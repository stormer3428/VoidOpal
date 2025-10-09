package fr.stormer3428.voidOpal.util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class TemporaryEntityManager implements PluginTied, Listener{

	private static final String TAG = "VoidOpalTemporaryEntity";

	@Override public void onPluginDisable() {}
	@Override public void onPluginEnable() { 
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin()); 
		purge();
	}

	@Override public void onPluginReload() { purge(); }

	public TemporaryEntityManager() { registerSelf(); }
	
	private void purge() {
		for(World w : Bukkit.getWorlds()) for(Entity e : w.getEntities()) if(e.getScoreboardTags().contains(TAG)) {
			e.remove();
		}
	}
	
	public void tag(Entity e) { e.addScoreboardTag(TAG); }
	
	@EventHandler public void onDeload(EntitiesUnloadEvent event) {
		event.getEntities().stream()
		.filter(e->e.getScoreboardTags().contains(TAG))
		.map(e->(BlockDisplay)e)
		.forEach(e->e.remove());
		;
	}

	@EventHandler public void onLoad(EntitiesLoadEvent event) {
		event.getEntities().stream()
		.filter(e->e.getScoreboardTags().contains(TAG))
		.forEach(e->e.remove());
		;
	}
	
}
