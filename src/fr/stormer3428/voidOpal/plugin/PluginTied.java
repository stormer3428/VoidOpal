package fr.stormer3428.voidOpal.plugin;

/**
 * 
 * Adds methods meant to be called at plugin startup, reload, and unload. Used for anything that needs to load data <br>
 * Intended implementation is to register it using {@link OMCPlugin#registerPluginTied(PluginTied)} in the {@link OMCPlugin#registerPluginTied()} method
 * 
 * @author stormer3428
 *
 */
public interface PluginTied {

	public void onPluginEnable();
	public void onPluginDisable();
	public void onPluginReload();
	
}
