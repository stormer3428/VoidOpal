package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public abstract class OMCCommandManager implements CommandExecutor, TabCompleter, PluginTied{

	public final ArrayList<OMCCommand> COMMANDS = new ArrayList<>();

	@Override
	public void onPluginEnable() {
		if(!COMMANDS.isEmpty()) return; //Already registered commands
		OMCLogger.debug("reguesting registering of custom variables");
		registerVariables();
		OMCLogger.debug("reguesting registering of custom commands");
		registerCommands();
		OMCLogger.debug("registering executors and tab completers");
		registerExecutorsAndTabCompelters();
	}

	@Override
	public void onPluginReload() {}
	
	@Override	
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		for(OMCCommand command : this.COMMANDS) if(command.matches(cmd.getName(), args)) return command.execute(sender, args);
		StringBuilder architecture = new StringBuilder(cmd.getName());
		for(String arg : args) architecture.append("." + arg);
		return OMCLogger.error(sender, OMCLang.COMMAND_SYNTAX_ERROR.toString().replace("<%SYNTAX>", architecture.toString().trim()));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		ArrayList<String> list = new ArrayList<>();
		for(OMCCommand command : this.COMMANDS) list.addAll(command.autocomplete(sender, cmd.getName(), args));
		list.sort(Comparator.naturalOrder());
		return list;
	}

	private void registerExecutorsAndTabCompelters() {
		for(OMCCommand cmd : this.COMMANDS)for(String alias : cmd.architecture.get(0)) {
			if(OMCCore.getJavaPlugin().getCommand(alias) == null) {
				OMCLogger.systemError("Failed to register command " + alias + ". It might not be preset in a depend's plugin.yml");
				continue;
			}
			OMCCore.getJavaPlugin().getCommand(alias).setExecutor(OMCCommandManager.this);
			OMCCore.getJavaPlugin().getCommand(alias).setTabCompleter(OMCCommandManager.this);
			OMCLogger.debug("Register executor and completer for " + alias + ".\nPermission : (" + cmd.getPermissionString() + ")\nSignature : (" + cmd.rawArchitecture + ")");
		}
	}

	@Override
	public void onPluginDisable() {}
	
	protected abstract void registerVariables();
	protected abstract void registerCommands();

	public void registerVariable(OMCVariable ... variable) {
		for(OMCVariable v : variable)
		OMCVariable.registerVariable(v);
	}
	
	public void registerCommand(OMCCommand ...command) {
		for(OMCCommand c : command)
		COMMANDS.add(c);
	}
	
	public void registerCommand(String givenArchitecture, boolean requiresPermission, OMCCommandExecutor t) {
		registerCommand(new OMCCommand(givenArchitecture, requiresPermission) {
			@Override
			public boolean execute(CommandSender sender, ArrayList<String> args) {
				return t.execute(sender, args);
			}
		});
	}
	
	static abstract class OMCCommandExecutor{public abstract boolean execute(CommandSender sender, ArrayList<String> args);}
	
	
}
