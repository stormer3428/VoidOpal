package fr.stormer3428.voidOpal.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;
import fr.stormer3428.voidOpal.util.OMCUtil;

@OMCKeep
public enum OMCLang {

	ERROR_GENERIC_NOPERMISSION("You do not have the permission to perform this command (<%PERMISSION>)"),
	ERROR_PLAYERONLY("You may only run this command as a player"),

	ERROR_MISSINGARG_NOPLAYER("You must specify a player"),
	ERROR_MISSINGARG_INTEGER("You must specify an integer"),
	ERROR_MISSINGARG_FLOAT("You must specify a float"),
	ERROR_MISSINGARG_MATERIAL("You must specify a material "),
	
	ERROR_INVALIDARG_NOPLAYER("No player with such name : <%PLAYER>"),
	ERROR_INVALIDARG_INTEGER("Invalid argument \"<%VALUE>\", expected an integer"),
	ERROR_INVALIDARG_FLOAT("Invalid argument \"<%VALUE>\", expected a float"),
	ERROR_INVALIDARG_MIN_GT_MAX("Invalid argument, the minimum value <%MIN> is greater than the maximum value <%MAX>"),
	ERROR_INVALIDARG_MATERIAL("Error, no material with such name '<%MATERIAL>'"),
	
	ERROR_MISSING_CONFIG("Error, failed to parse <%FILE>, tried to read <%PATH>"),
	ERROR_MISSING_CONFIG_SECTION("Error, missing a configuration section at '<%PATH>'"),
	ERROR_MISSING_CONFIG_FILE("Warning, File <%FILE> missing, importing from jar. (If this is the first time the plugin is loaded, you may ignore this warning)"),
	
	ERROR_INVALID_CMD_ID("Error, tried to retrieve custom model data <%CMDID> for SMPItem <%SMPITEM>, but only <%CMDS> available"),

	ERROR_ITEM_MANAGER_REGISTER_NULL("Tried to register a null item to the OMCItem manager"),
	ERROR_ITEM_MANAGER_REGISTER_NULL_NAME("Tried to register an item with no registry name to the OMCItem manager"),
	
	ERROR_POWER_MANAGER_REGISTER_NULL("Tried to register a null power to the OMCPower manager"),
	ERROR_POWER_MANAGER_REGISTER_NULL_NAME("Tried to register a power with no registry name to the OMCPower manager"),
	
	ERROR_NAMED_LISTENER_MANAGER_REGISTER_NULL("Tried to register a null namedListener to the NamedListener manager"),
	ERROR_NAMED_LISTENER_MANAGER_REGISTER_NULL_NAME("Tried to register a namedListener with no registry name to the NamedListener manager"),

	ERROR_POTION_MANAGER_NO_POTIONEFFECT("No potion effect with such name : <%INVALID> in file <%FILE>\n Valid options are <%VALID>"),
	ERROR_POTION_MANAGER_MISSING_POTIONEFFECT("Missing potion effect <%POTIONEFFECT> in file <%FILE>"),
	ERROR_POTION_MANAGER_NO_CONDITION("No potion effect condition with such name : <%INVALID> in file <%FILE>\n Valid options are <%VALID>"),
	ERROR_POTION_MANAGER_MISSING_CONDITION("Potion effect condition missing for potioneffect <%POTIONEFFECT> in file <%FILE>\n Valid options are <%VALID>"),
	
	ERROR_CONFIG_MISSING_PATH("Error, tried to access undefined path <%PATH> in config file '<%CONFIG>'"),
	
	COMMAND_SYNTAX_ERROR("No command with signature \n\"<%SYNTAX>\""), 
	
	NOTHING_CHANGED("Nothing changed"), 

	ERROR_MISSINGARG_ITEM("You need to specify an item"),
	ITEM_GIVE("Gave <%PLAYER> <%AMOUNT>x <%ITEM>"),
	ERROR_GENERIC_NOITEM("No item with such name : <%ITEM>"),
	ERROR_ITEM_GENERATION_FAILED("Failed to generate item '<%ITEM>'. Please contact an administrator"),	

	RELOADED_CONFIG("Config reloaded"),

	ERROR_MISSINGARG_POWER("You need to specify a power"),
	ERROR_GENERIC_NOPOWER("No power with such name : <%POWER>"),
	POWER_MANUALCAST("Manually casted power <%POWER>"),
	;

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	OMCLang(String d){
		this.path = this.name();
		this.def = d;
	}

	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		return OMCUtil.translateChatColor(LANG.getString(this.path, this.def));
	}

	public String getPath() {
		return this.path;
	}

	public String getDef() {
		return this.def;
	}

	@OMCKeep public static void loadFromConfig() {
		File lang = new File(OMCCore.getJavaPlugin().getDataFolder(), "lang.yml");
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(lang);
		if(!lang.exists()) try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to create language file");
			OMCLogger.systemError("Disabling...");
			OMCCore.getJavaPlugin().getServer().getPluginManager().disablePlugin(OMCCore.getJavaPlugin());
		}
		for(OMCLang l : OMCLang.values()) if(langConfig.getString(l.getPath()) == null) langConfig.set(l.getPath(), l.getDef());
		OMCLang.setFile(langConfig);
		try {
			langConfig.save(lang);
		}catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to save language file");
			OMCLogger.systemError("Disabling...");
			OMCCore.getJavaPlugin().getServer().getPluginManager().disablePlugin(OMCCore.getJavaPlugin());
		}
	}
}
