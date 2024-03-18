package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

public abstract class OMCCommandExecutor{
	public abstract boolean execute(CommandSender sender, ArrayList<String> args);
}

