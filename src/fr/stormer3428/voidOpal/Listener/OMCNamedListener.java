package fr.stormer3428.voidOpal.Listener;

import org.bukkit.event.Listener;

import fr.stormer3428.voidOpal.data.OMCNameable;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public abstract class OMCNamedListener implements Listener, OMCNameable, PluginTied{
	
	protected final String registryName;
	
	@Override public String getRegistryName() { return registryName; }
	
	public OMCNamedListener(String registryName) {
		this.registryName = registryName;
		OMCCore.getOMCCore().registerPluginTied(this); 
	}

	@Override public void onPluginEnable() {}
	@Override public void onPluginReload() {}
	@Override public void onPluginDisable() {}
	
}
