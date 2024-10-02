package fr.stormer3428.voidOpal.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeepChildren;
import fr.stormer3428.voidOpal.util.providers.OMCProvider;

@OMCKeep
@OMCKeepChildren
public abstract class OMCPlugin extends JavaPlugin{

	private String LICENSE;
	public String getLicenseString() {
		if(LICENSE == null || LICENSE.isBlank() || LICENSE.isEmpty()) try {
			LICENSE = null;
			File licenseFile = new File(getDataFolder(), "license.yml");
			if(!licenseFile.exists()) {
				licenseFile.getParentFile().mkdirs();
				licenseFile.createNewFile();
			}
			System.out.println(licenseFile.toPath());
			try(BufferedReader bi = new BufferedReader(new FileReader(licenseFile))){
				LICENSE = bi.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return LICENSE;
	}

	private final OMCProvider<OMCCore> provider;

	@OMCKeep public OMCPlugin() {this(new OMCPluginProvider());}
	@OMCKeep public OMCPlugin(OMCProvider<OMCCore> provider) {
		OMCCore.setJavaPlugin(this);
		OMCCore.setOMCChildPlugin(this);
		this.provider = provider;
	}

	@OMCKeep public abstract void registerPluginTied();
	@OMCKeep public abstract OMCLogger instantiateLogger();

	@OMCKeep public void loadLangFromConfig() {}
	@OMCKeep public void onOMCEnable() {}
	@OMCKeep public void onOMCReload() {}
	@OMCKeep public void onOMCDisable() {}

	@OMCKeep @Override public final void onEnable() {
		OMCLogger.systemNormal("Enabling core");
		if(OMCCore.getOMCCore() == null) {
			OMCLogger.systemNormal("Core is hasn't been initialized yet! Requesting...");
			Bukkit.getScheduler().runTaskAsynchronously(this, ()->{
				if(provider.getData() == null) throw new RuntimeException("Provider failed to load core!");
				OMCLogger.systemNormal("Succesfully loaded core v" + OMCCore.getOMCCore().getClass().getSimpleName());
				OMCLogger.systemNormal("Scheduling delayed start");
				Bukkit.getScheduler().runTask(this, ()->onEnable());
			});
			return;
		}
		OMCCore.getOMCCore().onEnable();
		OMCLogger.systemNormal("Core v"+OMCCore.getOMCCore().getClass().getSimpleName()+" succesfully enabled! Welcome to Stormer's plugin");
	}
	@OMCKeep @Override public final void onDisable() {
		if(OMCCore.getOMCCore() != null) OMCCore.getOMCCore().onDisable();
	}

	public File getPluginJar() {
		return getFile();
	}

	@OMCKeep public final void registerPluginTied(PluginTied pluginTied) {
		OMCCore.getOMCCore().registerPluginTied(pluginTied);
	}
}
