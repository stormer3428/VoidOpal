package fr.stormer3428.voidOpal.plugin;

import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;

/**
 * 
 * Adds methods meant to be called at plugin startup, reload, and unload. Used for anything that needs to load data <br>
 * Intended implementation is to register it using {@link OMCPluginImpl#registerPluginTied(PluginTied)} in the {@link OMCPluginImpl#registerPluginTied()} method
 * 
 * @author stormer3428
 *
 */
@OMCKeep 
public interface PluginTied {

	@OMCKeep public void onPluginEnable();
	@OMCKeep public void onPluginDisable();
	@OMCKeep public void onPluginReload();
	@OMCKeep public default void registerSelf() { OMCCore.getOMCCore().registerPluginTied(this); }
	
}
