package fr.stormer3428.voidOpal.Listener;

import java.util.ArrayList;

import org.bukkit.event.Listener;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCNamedListenerManager implements Listener, PluginTied{

	private final ArrayList<OMCNamedListener> registeredListeners = new ArrayList<>();
	
	public OMCNamedListenerManager() { OMCCore.getOMCCore().registerPluginTied(this); }
	
	@Override public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
		for (OMCNamedListener power : registeredListeners) power.onPluginEnable();
	}

	@Override public void onPluginDisable() { for (OMCNamedListener power : registeredListeners) power.onPluginDisable(); }
	@Override public void onPluginReload() { for (OMCNamedListener power : registeredListeners) power.onPluginReload(); }

	public void registerListener(OMCNamedListener power) {
		if(power == null) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(power.getRegistryName() == null || power.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(power, OMCCore.getJavaPlugin());
		registeredListeners.add(power);
	}

	public OMCNamedListener getPower(String registryName) { 
		for(OMCNamedListener power : registeredListeners) if(power.getRegistryName().equals(registryName)) return power;
		return null;
	}

}
