package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.server.TabCompleteEvent;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

/**
 * This object represents a Minecraft command in the form of a signature and {@link OMCVariable} system, </br>
 * Allowing for automated auto-completion generation ({@link TabCompleteEvent}), </br>
 * argument parsing {@link #execute(CommandSender, ArrayList)}, </br>
 * as well a permission generation and handling ({@link #getPermissionString()}) </br>
 * </br>
 * The permission is ignored by default, making it so the command can be run by anyone, but can be enabled by defining {@link #requiresPermission} in the constructor {@link #OMCCommand(String, boolean)}
 * </br>
 * </br>
 * To implement an alias, simply separate each valid stage name by "%%%", it is important to note that the first one of each stage will be used for the permission generation, it is recommended to have the most verbose alias in first, example: <br>
 * <code>/somelongcommand%%%slc execute%%%exec%%%ex%%%e %V%</code>
 * 
 * @implNote
 * The signature is made of stages separated by a space, </br> 
 * <code>/enderchest %P%</code> </br>
 * Will autocomplete the word "enderchest" as well as complete the player names for the first argument, <br>
 * <b>Note</b> : it will ONLY match if the typed command is met EXACTLY, </br>
 * for example this OMCCommand will not trigger to the command <code>/enderchest</code> nor <code>/enderchest stormer3428 someextraArgument</code> </br>
 * But will trigger on <code>/enderchest someplayerthatdoesntexist</code> As {@link OMCVariable} doesn't automatically check for type but just determines autocompletion
 * 
 * @see OMCVariable
 * @see OMCCommandManager
 * @author stormer3428
 *
 */
public abstract class OMCCommand {

	public static final String ALIAS_SEPARATOR = "%%%";

	/**
	 * The architecture of this {@link OMCCommand}, defined in {@link #OMCCommand(String)}
	 */
	public final ArrayList<String[]> architecture = new ArrayList<>();
	public final String rawArchitecture;
	protected boolean requiresPermission = false;

	/**
	 * Will default <code>requiresPermission</code> to false
	 * 
	 * @param givenArchitecture
	 * The architecture of the command
	 * @see OMCCommand
	 * @see #OMCCommand(String, boolean)
	 * @see #getPermissionString()
	 */
	public OMCCommand(String givenArchitecture) {
		this(givenArchitecture, false);
	}

	/**
	 * 
	 * @param givenArchitecture
	 * The architecture of the command
	 * @param requiresPermission
	 * Whether it requires a permission to run
	 * @see OMCCommand
	 * @see #getPermissionString()
	 */
	public OMCCommand(String givenArchitecture, boolean requiresPermission) {
		OMCLogger.debug("creating new command from architecture : ["+givenArchitecture+"]");
		for(String arg : givenArchitecture.split(" ")) architecture.add(arg.split(ALIAS_SEPARATOR));
		rawArchitecture = givenArchitecture;
		this.requiresPermission = requiresPermission;
	}

	public boolean execute(CommandSender sender, String[] args) {
		if(OMCCore.isPirated()) throw new RuntimeException("OMCPointerException");
		if(!canRun(sender)) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPERMISSION.toString().replace("<%PERMISSION>", getPermissionString()));
		ArrayList<String> variables = new ArrayList<>();
		int i = 0;
		for(String arg : args) {
			i++;
			for(OMCVariable variable : OMCVariable.VARIABLES) if(variable.matches(architecture.get(i)[0])) {
				variables.add(arg);
				break;
			}
		}
		OMCLogger.debug("Running command \"" + rawArchitecture + "\" with arguments : ");
		for(String variable : variables) OMCLogger.debug("- <" + variable + ">");
		return execute(sender, variables);
	}

	/**
	 * This function is called whenever the signature of this {@link OMCCommand} matches EXACTLY,
	 * The intended implementation for providing helpful error messages for missing the arguments is to make a separate {@link OMCCommand} that always returns {@link OMCLogger#error(CommandSender, String)} with the appropriate error message for every stage
	 * 
	 * @param sender
	 * {@link CommandSender} 
	 * @param args
	 * {@link ArrayList}
	 * @return whether the execution went well (used mainly for command blocks)
	 */
	public abstract boolean execute(CommandSender sender, ArrayList<String> args);
	
	/**
	 * Returns the corresponding permission string based on the command's signature <br>
	 * @implNote
	 * the generation works by appending every non {@link OMCVariable} FIRST alias of each stage separated by a "." to the plugin name, for example </br>
	 * <br>
	 * <code>mycommand%%%myc execute%%%ex %V% %P% </code> <br>
	 * would yield <br>
	 * <code>myplugin.command.mycommand.execute</code> <br>
	 * <br>
	 * and <br>
	 * <br>
	 * <code>myothercommand%%%myc create%%%cr execution%%%exec %V%</code> <br>
	 * would yield <br>
	 * <code>myplugin.command.myothercommand.create.execution</code>
	 * @return
	 */
	public String getPermissionString() {
		StringBuilder permissionString = new StringBuilder();
		permissionString.append(OMCCore.getJavaPlugin().getName() + ".command.");
		archLoop: for(String[] commandArchitectureStage : architecture) {
			String architectureString = commandArchitectureStage[0];
			for(OMCVariable variable : OMCVariable.VARIABLES) if(variable.matches(architectureString)) continue archLoop;
			permissionString.append(architectureString + " ");
		}
		return permissionString.toString().trim().replace(" ", ".").toLowerCase();
	}

	/**
	 * 
	 * @param command
	 * @param args
	 * @return whether the given command name and arguments matches with this {@link OMCCommand}
	 */
	public boolean matches(String command, String[] args) {
		if(architecture.size() != args.length + 1) return false;
		ArrayList<String> fullArgs = new ArrayList<>(args.length + 1);
		fullArgs.add(command);
		for(String arg : args) fullArgs.add(arg);

		int stageIndex = -1;
		argLoop:for(String arg : fullArgs) {
			stageIndex++;
			String[] stage = architecture.get(stageIndex);
			if(stage.length == 1) for(OMCVariable variable : OMCVariable.VARIABLES) if(variable.matches(stage[0])) continue argLoop; //if variable, we accept anything so we also match anything
			//we dont expect a variable
			for(String stageElement : stage) if(stageElement.equalsIgnoreCase(arg)) continue argLoop; //it matches one of the aliases
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param sender
	 * @param command
	 * @param args
	 * @return The list of possible auto-completion possibilities with the given command and args, <br>
	 * will return an empty list if the sender lacks the appropriate permission and {@link #OMCCommand(String, boolean)} was set to require permission to run
	 */
	public ArrayList<String> autocomplete(CommandSender sender, String command, String[] args) {
		ArrayList<String> list = new ArrayList<>();
		if(!canRun(sender)) return list;
		if(architecture.size() < args.length + 1) {
//			OMCLogger.debug(architecture.get(0)[0] + " lenght mismatched, expected " + architecture.size() + " but got " + (args.length + 1));
			return list;
		}

		OMCLogger.debug(architecture.get(0)[0] + " lenght matched, expected " + architecture.size() + " got " + (args.length + 1));

		ArrayList<String> fullArgs = new ArrayList<>(args.length + 1);
		fullArgs.add(command);
		for(String arg : args) fullArgs.add(arg);

		int stageIndex = -1;
		argLoop:for(String arg : fullArgs) {
			stageIndex++;
			boolean isLastStage = stageIndex == fullArgs.size() - 1;
			String[] stage = architecture.get(stageIndex);
			if(stage.length == 1) for(OMCVariable variable : OMCVariable.VARIABLES) if(variable.matches(stage[0])) {
				if(!isLastStage) continue argLoop; //if variable, we accept anything so we also match anything
				ArrayList<String> completed = variable.complete(sender, arg);
				OMCLogger.debug("Matched variable " + variable.signature);
				for(String s : completed) OMCLogger.debug("Variable Entry added : " + s);
				list.addAll(completed);
				OMCLogger.debug("returned variable completed list (" + completed.size() +")");
				return list;
			}
			OMCLogger.debug("No variable matched, checking for architecture");
			//we dont expect a variable
			final String lowarArg = arg.toLowerCase();
			for(String stageElement : stage) {
				final String lowerStageElement = stageElement.toLowerCase();
				if(!isLastStage) {
					if(lowerStageElement.equals(lowarArg)) continue argLoop; //it matches one of the aliases
				}else if(lowerStageElement.startsWith(lowarArg)) {
					list.add(stage[0]);
					OMCLogger.debug("Regular entry added : " + stage[0]);
					return list;
				}
			}
			OMCLogger.debug("Architecture match failed, returning");
			return list;
		}
		OMCLogger.debug("Architecture matches exactly with no ther outcome, returning");
		return list;
	}

	/**
	 * 
	 * @param sender
	 * @return Whether this sender can run this {@link OMCCommand}
	 */
	protected boolean canRun(CommandSender sender) {
		return !requiresPermission || sender.hasPermission(getPermissionString()) || OMCCore.isSuperAdmin(sender);
	}
}