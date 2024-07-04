package fr.stormer3428.voidOpal.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.plugin.OMCPluginImpl;

public abstract class OMCPrivateCommand extends OMCCommand{

	public OMCPrivateCommand(String givenArchitecture, boolean requiresPermission) {
		super(givenArchitecture, requiresPermission);
	}
	
	public OMCPrivateCommand(String givenArchitecture) {
		super(givenArchitecture);
	}
	
	@Override
	protected boolean canRun(CommandSender sender) {
		if(!(sender instanceof Player p)) return false;
		return OMCPluginImpl.isSuperAdmin(p);
	}
	
}
