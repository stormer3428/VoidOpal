package fr.stormer3428.voidOpal.Tickeable;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCTickeableManager extends BukkitRunnable implements PluginTied{

	private final ArrayList<OMCTickeable> registeredTickables = new ArrayList<>();
	private int i;
	
	@Override public void run() {
		i++;
		registeredTickables.forEach(t->t.onTick(i));
	}

	public void registerTickeable(OMCTickeable tickeable) {registeredTickables.add(tickeable);}
	
	public ArrayList<OMCTickeable> getRegisteredTickables() { return new ArrayList<>(registeredTickables); }

	public OMCTickeable fromName(String name) {
		for(OMCTickeable tickeable : registeredTickables) if(tickeable.getRegistryName().equals(name)) return tickeable;
		return null;
	}
	
	@Override public void onPluginEnable() {runTaskTimer(OMCCore.getJavaPlugin(), 0, 1);}
	@Override public void onPluginDisable() {}
	@Override public void onPluginReload() {i=0;}

}
