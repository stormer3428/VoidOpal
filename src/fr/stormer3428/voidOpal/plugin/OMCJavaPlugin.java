package fr.stormer3428.voidOpal.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.logging.OMCLogger;

public abstract class OMCJavaPlugin extends JavaPlugin implements OMCPlugin{

	public OMCPluginImpl omcPlugin = new OMCPluginImpl() {
		@Override public final void registerPluginTied() {OMCJavaPlugin.this.registerPluginTied();}
		@Override public final void onObsidianEnable() {OMCJavaPlugin.this.onObsidianEnable();}
		@Override public final void onObsidianDisable() {OMCJavaPlugin.this.onObsidianDisable();}
		@Override public final void loadLangFromConfig() {OMCJavaPlugin.this.loadLangFromConfig();}
		@Override public final OMCLogger instantiateLogger() {return OMCJavaPlugin.this.instantiateLogger();}
		@Override public final JavaPlugin _getJavaPlugin() {return OMCJavaPlugin.this;}
	};
	
	@Override public void onEnable() {omcPlugin.onEnable();}
	public void reload() {omcPlugin.reload();}
	@Override public void onDisable() {omcPlugin.onDisable();}
	public void registerPluginTied(PluginTied pluginTied) {omcPlugin.registerPluginTied(pluginTied);}
}
