package fr.stormer3428.voidOpal.plugin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeepChildren;
import fr.stormer3428.voidOpal.util.providers.OMCProvider;

@OMCKeep
@OMCKeepChildren
public abstract class OMCPlugin extends JavaPlugin{
	
	@OMCKeep public OMCPlugin(OMCProvider<OMCCore> provider) {
		OMCCore.setJavaPlugin(this);
		OMCCore.setOMCChildPlugin(this);
		OMCLogger.systemNormal("Enabling core");
		if(OMCCore.getOMCCore() == null) {
			OMCLogger.systemNormal("Core is hasn't been initialized yet! Requesting...");
			Bukkit.getScheduler().runTaskAsynchronously(this, ()->{
				if(provider.getData() == null) {
					OMCLogger.systemError("Provider failed to load core!");
					throw new RuntimeException("Provider failed to load core!");
				}
				OMCLogger.systemNormal("Succesfully loaded core v" + OMCCore.getOMCCore().getClass().getSimpleName());
				OMCLogger.systemNormal("Scheduling delayed start");
				Bukkit.getScheduler().runTask(this, ()->onEnable());
			});
			return;
		}
		OMCLogger.systemNormal("Core v"+OMCCore.getOMCCore().getClass().getSimpleName()+" succesfully enabled! Welcome to Stormer's plugin");

	}

	@OMCKeep public abstract void registerPluginTied();
	@OMCKeep public abstract OMCLogger instantiateLogger();
	@OMCKeep public abstract void registerAutoconfigClasses();

	@OMCKeep public void loadLangFromConfig() {}
	@OMCKeep public void onOMCEnable() {}
	@OMCKeep public void onOMCReload() {}
	@OMCKeep public void onOMCDisable() {}

	@OMCKeep @Override public final void onEnable() { OMCCore.getOMCCore().onEnable(); }
	@OMCKeep @Override public final void onDisable() { if (OMCCore.getOMCCore() != null) OMCCore.getOMCCore().onDisable(); }

	public File getPluginJar() { return getFile(); }
	@OMCKeep public final void registerPluginTied(PluginTied pluginTied) { OMCCore.getOMCCore().registerPluginTied(pluginTied); }
	public void registerAutoConfigClass(Class<?>... classes) { for (Class<?> c : classes) OMCCore.getOMCCore().autoconfigParser.registerAutoConfigClass(c); }
	
}
