package fr.stormer3428.voidOpal.Power;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import fr.stormer3428.voidOpal.Command.OMCCommand;
import fr.stormer3428.voidOpal.Command.OMCVariable;
import fr.stormer3428.voidOpal.Listener.OMCNamedListenerManager;
import fr.stormer3428.voidOpal.Power.Types.OMCPower;
import fr.stormer3428.voidOpal.Tickeable.OMCTickeableManager;
import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.OMCNamedListenerHolder;
import fr.stormer3428.voidOpal.data.OMCPowerHolder;
import fr.stormer3428.voidOpal.data.OMCTickeableHolder;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCPowerManager implements Listener, PluginTied{

	private final OMCTickeableManager omcTickeableManager;
	private final OMCNamedListenerManager omcNamedListenerManager;
	private final ArrayList<OMCPower> registeredPowers = new ArrayList<>();

	public OMCPowerManager(OMCTickeableManager omcTickeableManager, OMCNamedListenerManager omcNamedListenerManager) {
		this.omcTickeableManager = omcTickeableManager;
		this.omcNamedListenerManager = omcNamedListenerManager;
		registerSelf();
	}
	
	@Override
	public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
		for(OMCPower power : registeredPowers) power.onPluginEnable();
	}

	@Override public void onPluginDisable() { for (OMCPower power : registeredPowers) power.onPluginDisable(); }

	@Override
	public void onPluginReload() {
		for(OMCPower power : registeredPowers) {
			power.onPluginReload();
			power.clearCooldowns();
		}
	}
	
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
	public OMCVariable getPowerVariable() { return getPowerVariable("%POWER%"); }

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
			
		omcNamedListenerManager.registerListener(power);
		omcTickeableManager.registerTickeable(power);
		registeredPowers.add(power);
		
		if(power instanceof OMCNamedListenerHolder h) h.getListeners().forEach(l->omcNamedListenerManager.registerListener(l));
		if(power instanceof OMCTickeableHolder h) h.getOmcTickeables().forEach(l->omcTickeableManager.registerTickeable(l));
		if(power instanceof OMCPowerHolder h) h.getOmcPowers().forEach(l->registerPower(l));
	}

	/**
	 * Will return the corresponding {@link OMCPower} registered in this manager, or null if it is unrecognized
	 * @implNote
	 * This method uses {@link String#equals(String)} to find the correlating {@link OMCPower}
	 * 
	 * @param name
	 * The power name to search for
	 * @return The corresponding {@link OMCPower}
	 */
	public OMCPower getPower(String registryName) { 
		for(OMCPower power : registeredPowers) if(power.getRegistryName().equals(registryName)) return power;
		return null;
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
	public OMCPower getPowerIgnoreCase(String registryName) { 
		for(OMCPower power : registeredPowers) if(power.getRegistryName().equals(registryName)) return power;
		return null;
	}
	
	
	
	/**
	 * 
	 * @return a copy of the registered power list
	 * @see #registerPower(OMCPower)
	 */
	public ArrayList<OMCPower> getPowers() { return new ArrayList<>(registeredPowers); }
	
}
