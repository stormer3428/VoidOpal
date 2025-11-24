package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.logging.OMCLogger;

/**
 * 
 * A variable used for {@link OMCCommand}, it simply defines a token to use in {@link OMCCommand#architecture}
 * 
 * @author stormer3428
 *
 */
public abstract class OMCVariable {

	public static final OMCVariable GENERIC_VARIABLE = new OMCVariable("%V%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			return new ArrayList<>();
		}
	};
	
	public static final OMCVariable PLAYER_VARIABLE = new OMCVariable("%P%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			final String lower = incomplete.toLowerCase();
			for(Player p : Bukkit.getOnlinePlayers()) if(p.getName().toLowerCase().startsWith(lower)) list.add(p.getName());
			return list;
		}
	};
	
	public static final OMCVariable WORLD_VARIABLE = new OMCVariable("%W%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			final String lower = incomplete.toLowerCase();
			for(World w : Bukkit.getWorlds()) if(w.getName().toLowerCase().startsWith(lower)) list.add(w.getName());
			return list;
		}
	};
	
	public static final OMCVariable PLAYER_X = new OMCVariable("%PX%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			if(sender instanceof Player p) list.add(p.getLocation().getBlockX() + "");
			return list;
		}
	};
	
	public static final OMCVariable PLAYER_Y = new OMCVariable("%PY%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			if(sender instanceof Player p) list.add(p.getLocation().getBlockY() + "");
			return list;
		}
	};
	
	public static final OMCVariable PLAYER_Z = new OMCVariable("%PZ%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			if(sender instanceof Player p) list.add(p.getLocation().getBlockZ() + "");
			return list;
		}
	};

	public static final OMCVariable BOOLEAN_VARIABLE = new OMCVariable("%B%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			final String lower = incomplete.toLowerCase();
			if("true".startsWith(lower)) list.add("True");
			if("false".startsWith(lower)) list.add("False");
			return list;
		}
	};

	public static final OMCVariable MATERIAL_VARIABLE = new OMCVariable("%MATERIAL%") {
		@Override
		protected ArrayList<String> complete(CommandSender sender, String incomplete) {
			final ArrayList<String> list = new ArrayList<>();
			final String lower = incomplete.toLowerCase();
			for(Material material : Material.values()) if(material.name().toLowerCase().startsWith(lower)) list.add(material.name());
			return list;
		}
	};
	
	public static final ArrayList<OMCVariable> VARIABLES = new ArrayList<>();
	static {
		VARIABLES.add(GENERIC_VARIABLE);
		VARIABLES.add(PLAYER_VARIABLE);
		VARIABLES.add(BOOLEAN_VARIABLE);
		VARIABLES.add(MATERIAL_VARIABLE);
		VARIABLES.add(WORLD_VARIABLE);
		VARIABLES.add(PLAYER_X);
		VARIABLES.add(PLAYER_Y);
		VARIABLES.add(PLAYER_Z);
	}
	
	public static void registerVariable(OMCVariable v) {
		OMCLogger.debug("registering variable : " + v.toString());
		VARIABLES.add(v);
	}

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
