package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import fr.stormer3428.voidOpal.logging.OMCLogger;

/**
 * 
 * A variable used for {@link OMCCommand}, it simply defines a token to use in {@link OMCCommand#architecture}
 * 
 * @author stormer3428
 *
 */
public abstract class OMCVariable {
	
	/**
	 * @see OMCVariable
	 */
	protected final String signature;
	
	/**
	 * 
	 * @param commandStage
	 * @return whether the current commandStage matches this variable
	 */
	public boolean matches(String commandStage) {
		return signature.equalsIgnoreCase(commandStage);
	}
	
	public OMCVariable(String signature) {
		OMCLogger.debug("creating variable : " + signature);
		this.signature = signature;
	}
	
	/**
	 * 
	 * @param sender
	 * @param incomplete
	 * @return A list of all potential values that completes the given partial String in {@link #complete(CommandSender, String)} 
	 */
	protected abstract ArrayList<String> complete(CommandSender sender, String incomplete);
	
	@Override
	public String toString() {
		return "[OMCVariable : " + signature + "]";
	}
}
