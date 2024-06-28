package fr.stormer3428.voidOpal.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.logging.OMCLogger;

public interface OMCPlugin {

	public void loadLangFromConfig();
	public void registerPluginTied();
	public void onObsidianEnable();
	public void onObsidianDisable();
	public OMCLogger instantiateLogger();
	public JavaPlugin _getJavaPlugin();
	public OMCPluginImpl _getOMCPlugin();
	
}
