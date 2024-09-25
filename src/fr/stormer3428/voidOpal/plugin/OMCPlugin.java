package fr.stormer3428.voidOpal.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeepChildren;
import fr.stormer3428.voidOpal.util.providers.OMCProvider;

@OMCKeep
@OMCKeepChildren
public abstract class OMCPlugin extends JavaPlugin{

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

	@OMCKeep public final void registerPluginTied(PluginTied pluginTied) {
		OMCCore.getOMCCore().registerPluginTied(pluginTied);
	}
}
