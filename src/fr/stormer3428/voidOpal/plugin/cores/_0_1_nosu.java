package fr.stormer3428.voidOpal.plugin.cores;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.command.CommandSender;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;

@OMCKeep
@AutoConfig
public final class _0_1_nosu extends OMCCore{

	protected final HashSet<PluginTied> pluginTieds = new HashSet<>();

	@BooleanConfigValue(path = "debug") public static boolean DEBUG = false;
	@Override protected boolean _isDebugModeActive() {return DEBUG;}

	private final void loadLangAndLogger() {
		OMCLogger.debug("loading native lang"); OMCLang.loadFromConfig();
		OMCLogger.debug("requesting stranger lang loading"); OMCCore.getOMCChildPlugin().loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation"); OMCCore.getOMCChildPlugin().instantiateLogger();
	}

	@Override
	public final void onEnable() {
		autoconfigParser.registerAutoConfigClass(getClass());
		autoconfigParser.updateValues();
		new OMCLogger("INTERNAL", "INTERNAL ERR");
		loadLangAndLogger();

		OMCLogger.debug("registering of internal plugin tied classes"); registerIntegratedPluginTied(); 
		OMCLogger.debug("requesting registering of plugin tied classes"); OMCCore.getOMCChildPlugin().registerPluginTied();
		OMCLogger.debug("requesting registering autoconfig classes"); OMCCore.getOMCChildPlugin().registerAutoconfigClasses();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		OMCLogger.debug("requesting enabling of child plugin"); OMCCore.getOMCChildPlugin().onOMCEnable();
		OMCLogger.debug("requesting enabling of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginEnable();
	}

	@Override
	public final void reload() {
		onReload();
		OMCLogger.debug("requesting reload child plugin"); OMCCore.getOMCChildPlugin().onOMCReload();
		OMCLogger.debug("requesting reload of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginReload();
	}

	@Override
	public void onReload() {
		loadLangAndLogger();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
	}

	@Override
	public final void onDisable() {
		OMCLogger.debug("requesting disabling of plugin tied classes"); for(PluginTied pluginTied : pluginTieds) pluginTied.onPluginDisable();	
		OMCLogger.debug("requesting disabling of child plugin"); OMCCore.getOMCChildPlugin().onOMCDisable();
	}

	private final void registerIntegratedPluginTied() {}

	public final void registerPluginTied(PluginTied pluginTied) { this.pluginTieds.add(pluginTied); }
	
	@SuppressWarnings("unchecked")
	@Override public <T extends PluginTied> T _getPluginTied(Class<T> clazz) { 
		for(PluginTied pluginTied : pluginTieds) if(clazz.isInstance(pluginTied)) return (T) pluginTied;
		return null; 
	}

	public final boolean _isSuperAdmin(CommandSender sender) { return false; }
}
