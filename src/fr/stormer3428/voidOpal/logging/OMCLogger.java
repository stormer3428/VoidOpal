package fr.stormer3428.voidOpal.logging;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.plugin.OMCPlugin;
import fr.stormer3428.voidOpal.util.OMCUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class OMCLogger {

	private static String PREFIX_COMMAND;
	private static String PREFIX_ERROR;

	public static OMCLogger instance;
	
	public OMCLogger(String prefix, String error) {
		setPrefixCommand(prefix);
		setPrefixError(error);
		instance = this;
	}

	public static void setPrefixCommand(String prefix) {PREFIX_COMMAND = prefix;}
	public static void setPrefixError(String prefix) {PREFIX_ERROR = prefix;}

	public static boolean normal(CommandSender p,String strg){return instance._normal(p,strg);}
	public boolean _normal(CommandSender p,String strg){
		String m = PREFIX_COMMAND + strg;
		p.sendMessage(m);
		return true;
	}

	public static boolean broadcastNormal(String strg){return instance._broadcastNormal(strg);}
	public boolean _broadcastNormal(String strg){
		for(Player p : Bukkit.getOnlinePlayers())normal(p, strg);
		return true;
	}

	public static boolean normal(String strg,List<String> p){return instance._normal(strg,p);}
	public boolean _normal(String strg,List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers())if(p.contains(pls.getName())) normal(pls, strg);
		return true;
	}

	public static boolean error(CommandSender p,String strg){return instance._error(p,strg);}
	public boolean _error(CommandSender p,String strg){
		String m = PREFIX_ERROR + strg;
		p.sendMessage(m);
		return false;
	}

	public static boolean error(String strg){return instance._error(strg);}
	public boolean _error(String strg){
		for(Player p : Bukkit.getOnlinePlayers()) error(p, strg);
		return false;
	}

	public static boolean error(String strg,List<String> p){return instance._error(strg,p);}
	public boolean _error(String strg,List<String> p){
		for(Player pls : Bukkit.getOnlinePlayers()) if(p.contains(pls.getName())) error(pls, strg);
		return false;
	}

	public static boolean systemNormal(String strg){return instance._systemNormal(strg);}
	public boolean _systemNormal(String strg){
		strg = PREFIX_COMMAND + " " + strg;
		Bukkit.getConsoleSender().sendMessage(strg);
		return true;
	}

	public static boolean systemError(String strg){return instance._systemError(strg);}
	public boolean _systemError(String strg){
		strg = PREFIX_ERROR + " " + strg;
		Bukkit.getConsoleSender().sendMessage(strg);
		return false;
	}

	public static boolean debug(String message){return instance._debug(message);}
	public boolean _debug(String message){
		if(OMCPlugin.DEBUG) return systemNormal("[DEBUG] " + message);
		return false;
	}

	public static boolean actionBar(Player p,String message){return instance._actionBar(p,message);}
	public boolean _actionBar(Player p,String message){
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(OMCUtil.translateChatColor(message)));
		return true;
	}



}
