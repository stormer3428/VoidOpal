package fr.stormer3428.voidOpal.Power;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.voidOpal.Command.OMCCommand;
import fr.stormer3428.voidOpal.Command.OMCVariable;
import fr.stormer3428.voidOpal.Power.Types.OMCPower;
import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public abstract class OMCPowerManager extends BukkitRunnable implements Listener, PluginTied{

	private final ArrayList<OMCPower> registeredPowers = new ArrayList<>();
	private int ticker = 0;

	@Override
	public void run() {
		ticker++;
		for(OMCPower power : registeredPowers) if(power instanceof OMCTickable tickable){
			tickable.onTick(ticker);
		}
	}
	
	@Override
	public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
		registerPowers();
		for(OMCPower power : registeredPowers) power.onPluginEnable();
		runTaskTimer(OMCCore.getJavaPlugin(), 0, 1);	
	}
	
	@Override
	public void onPluginDisable() {
		for(OMCPower power : registeredPowers) power.onPluginDisable();
	}
	
	@Override
	public void onPluginReload() {
		for(OMCPower power : registeredPowers) {
			power.onPluginReload();
			power.clearCooldowns();
		}
	}

	public abstract void registerPowers();
	

	/**
	 * Creates a {@link OMCVariable} with the given signature that completes for registered {@link OMCPower}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerPower(OMCPower)
	 */
	public OMCVariable getPowerVariable(String variableSignature) {
		return new OMCVariable(variableSignature) {

			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				String lowercase = incomplete.toLowerCase();
				ArrayList<String> list = new ArrayList<>();
				for(OMCPower power : registeredPowers) {
					String uppercase = power.getRegistryName();
					String name = uppercase.toLowerCase();
					if(name.startsWith(lowercase) || name.contains(lowercase)) list.add(uppercase);
				}
				return list;
			}
		};
	}
	
	/**
	 * Creates a {@link OMCVariable} with the default signature "%POWER%" that completes for registered {@link OMCPower}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerPower(OMCPower)
	 */
	public OMCVariable getPowerVariable() {
		return getPowerVariable("%POWER%");
	}

	/**
	 * 
	 * Registers an {@link OMCPower} to the {@link OMCPowerManager}. A power registered to the powerManager will show up in the {@link #getPowerVariable(String)} completion list
	 * 
	 * @param power 
	 * the power to register
	 * @see #getPowerVariable(String)
	 */
	public void registerPower(OMCPower power) {
		if(power == null) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(power.getRegistryName() == null || power.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_POWER_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(power, OMCCore.getJavaPlugin());
		registeredPowers.add(power);
	}


	public OMCPower getPower(String registryName) { 
		for(OMCPower power : getPowers()) if(power.getRegistryName().equals(registryName)) return power;
		return null;
	}
	
	
	/**
	 * 
	 * @return a copy of the registered power list
	 * @see #registerPower(OMCPower)
	 */
	public ArrayList<OMCPower> getPowers() {
		return new ArrayList<>(registeredPowers);
	}
	

	/**
	 * Will return the corresponding {@link OMCPower} registered in this manager, or null if it is unrecognized
	 * @implNote
	 * This method uses {@link String#equalsIgnoreCase(String)} to find the correlating {@link OMCPower}
	 * 
	 * @param name
	 * The power name to search for
	 * @return The corresponding {@link OMCPower}
	 */
	public OMCPower fromName(String name) {
		for(OMCPower omcPower : registeredPowers) if(omcPower.getRegistryName().equalsIgnoreCase(name)) return omcPower;
		return null;
	}
	
}
