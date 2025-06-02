package fr.stormer3428.voidOpal.Listener;

import java.util.ArrayList;

import org.bukkit.event.Listener;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.OMCNamedListenerHolder;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCNamedListenerManager implements Listener, PluginTied{

	private final ArrayList<OMCNamedListener> registeredListeners = new ArrayList<>();
	
	public OMCNamedListenerManager() { registerSelf(); }
	
	@Override public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
		for (OMCNamedListener power : registeredListeners) power.onPluginEnable();
	}

	@Override public void onPluginDisable() { for (OMCNamedListener power : registeredListeners) power.onPluginDisable(); }
	@Override public void onPluginReload() { for (OMCNamedListener power : registeredListeners) power.onPluginReload(); }

	public void registerListener(OMCNamedListener listener) {
		if(listener == null) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(listener.getRegistryName() == null || listener.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(listener, OMCCore.getJavaPlugin());
		registeredListeners.add(listener);

		if(listener instanceof OMCNamedListenerHolder h) h.getListeners().forEach(l->registerListener(l));
//		if(power instanceof OMCTickeableHolder h) h.getOmcTickeables().forEach(l->tickeableManager.registerTickeable(l));
//		if(power instanceof OMCPowerHolder h) h.getOmcPowers().forEach(l->powerManager.registerPower(l));
		
	}

	public OMCNamedListener getPower(String registryName) { 
		for(OMCNamedListener power : registeredListeners) if(power.getRegistryName().equals(registryName)) return power;
		return null;
	}

}
