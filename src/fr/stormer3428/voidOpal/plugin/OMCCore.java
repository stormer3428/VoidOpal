package fr.stormer3428.voidOpal.plugin;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.data.config.AutoconfigParser;
import fr.stormer3428.voidOpal.data.config.AutoconfigParserImpl;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;

@OMCKeep
public abstract class OMCCore{

	@OMCKeep public final AutoconfigParser autoconfigParser = new AutoconfigParserImpl();

	@OMCKeep public abstract void registerPluginTied(PluginTied pluginTied);
	@OMCKeep public abstract <T extends PluginTied> T _getPluginTied(Class<T> clazz);
	@OMCKeep public static <T extends PluginTied> T getPluginTied(Class<T> clazz) {return getOMCCore()._getPluginTied(clazz);}
	
	@OMCKeep public abstract void reload();
	@OMCKeep public abstract void onEnable();
	@OMCKeep public abstract void onReload();
	@OMCKeep public abstract void onDisable();
	
	@OMCKeep protected abstract boolean _isDebugModeActive();
	@OMCKeep protected abstract boolean _isSuperAdmin(CommandSender sender);

	@OMCKeep public OMCCore() {omcCore = this;}
	@OMCKeep private static OMCCore omcCore; 
	@OMCKeep public static final OMCCore getOMCCore() {return omcCore;} 

	@OMCKeep private static OMCPlugin omcChildPlugin; 
	@OMCKeep public static final OMCPlugin getOMCChildPlugin() {return omcChildPlugin;}
	@OMCKeep public static final void setOMCChildPlugin(OMCPlugin childPlugin) {OMCCore.omcChildPlugin=childPlugin;} 

	@OMCKeep private static JavaPlugin javaPlugin;
	@OMCKeep public static final JavaPlugin getJavaPlugin() {return javaPlugin;} 
	@OMCKeep public static final void setJavaPlugin(JavaPlugin javaPlugin) {OMCCore.javaPlugin = javaPlugin;} 

	@OMCKeep public static final boolean isDebugModeActive() {return omcCore._isDebugModeActive();}
	@OMCKeep public static boolean isSuperAdmin(CommandSender sender) {return omcCore._isSuperAdmin(sender);}
}
