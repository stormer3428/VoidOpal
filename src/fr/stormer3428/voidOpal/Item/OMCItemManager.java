package fr.stormer3428.voidOpal.Item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import fr.stormer3428.voidOpal.Command.OMCCommand;
import fr.stormer3428.voidOpal.Command.OMCVariable;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.Item.Types.SMPItem;
import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCPluginImpl;
import fr.stormer3428.voidOpal.plugin.PluginTied;
import fr.stormer3428.voidOpal.util.ItemStackUtils;

public abstract class OMCItemManager implements Listener, PluginTied{

	private final ArrayList<OMCItem> registeredItems = new ArrayList<>();
	private final HashMap<SMPItem, YamlConfiguration> smpitemConfigs = new HashMap<>();

	protected abstract void registerItems();

	@Override
	public void onPluginEnable() {
		OMCPluginImpl.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCPluginImpl.getJavaPlugin());
		registerItems();
		registerRecipes();
		registerConfigs();
	}

	private void registerConfigs() {
		OMCLogger.systemNormal("Reloading all item configs...");
		smpitemConfigs.clear();
		for(OMCItem it : registeredItems) {
			if(!(it instanceof SMPItem smpItem)) continue;
			String registryName = smpItem.getRegistryName();
			if(registryName == null || registryName.isBlank()) {
				OMCLogger.systemError("Skipped null regirstry name item " + smpItem.getClass().getSimpleName());
				continue;
			}
			String fileName = registryName + ".yml";
			OMCLogger.systemNormal("Reloading " + fileName);
			File file = new File(OMCPluginImpl.getJavaPlugin().getDataFolder(), "items/" + fileName);
			if(!file.exists()) try {
				OMCLogger.systemNormal("File was missing, creating...");
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				OMCLogger.systemError("Skipped");
				continue;
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			smpitemConfigs.put(smpItem, config);
			syncItemToConfigFile(smpItem, config, file);
		}
	}

	private void syncItemToConfigFile(SMPItem it, YamlConfiguration config, File file) {
		config.setDefaults(it.getConfig());
		config.options().copyDefaults(true);
		try { 
			config.save(file); 
		} catch (IOException e) {
			e.printStackTrace();
			OMCLogger.systemError("Failed to save config file for SMPItem " + it.getRegistryName());
		}
		it.syncToConfig(config);
	}

	@Override
	public void onPluginDisable() {}

	public void registerRecipes() {
		for(OMCItem item : registeredItems) if(item instanceof OMCCraftable craftable) for(Recipe recipe : craftable.getRecipes()){
			Bukkit.removeRecipe(ItemStackUtils.getNameSpacedKeyFromRecipe(recipe));
			Bukkit.addRecipe(recipe);
		}
	}

	@Override
	public void onPluginReload() {
		registerRecipes();
		registerConfigs();
	}

	/**
	 * Creates a {@link OMCVariable} with the given signature that completes for registered {@link OMCItem}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerItem(OMCItem)
	 */
	public OMCVariable getItemVariable(String variableSignature) {
		return new OMCVariable(variableSignature) {

			@Override
			protected ArrayList<String> complete(CommandSender sender, String incomplete) {
				String lowercase = incomplete.toLowerCase();
				ArrayList<String> list = new ArrayList<>();
				for(OMCItem item : registeredItems) {
					String uppercase = item.getRegistryName();
					String name = uppercase.toLowerCase();
					if(name.startsWith(lowercase) || name.contains(lowercase)) list.add(uppercase);
				}
				return list;
			}
		};
	}

	/**
	 * Creates a {@link OMCVariable} with the default signature "%ITEM%" that completes for registered {@link OMCItem}
	 * 
	 * @param variableSignature
	 * the signature of the variable
	 * @return the variable
	 * @see OMCCommand
	 * @see #registerItem(OMCItem)
	 */
	public OMCVariable getItemVariable() {
		return getItemVariable("%ITEM%");
	}

	/**
	 * 
	 * Registers an {@link OMCItem} to the {@link OMCItemManager}. An item registered to the itemManager will show up in the {@link #getItemVariable(String)} completion list
	 * 
	 * @param item 
	 * the item to register
	 * @see #getItems()
	 */
	public void registerItem(OMCItem item) {
		if(item == null) {
			OMCLogger.systemError(OMCLang.ERROR_ITEM_MANAGER_REGISTER_NULL.toString());
			return;
		}
		if(item.getRegistryName() == null || item.getRegistryName().isBlank()) {
			OMCLogger.systemError(OMCLang.ERROR_ITEM_MANAGER_REGISTER_NULL_NAME.toString());
			return;
		}
		if(item instanceof SMPItem smpItem) for(Listener listener : smpItem.getListener()) OMCPluginImpl.getJavaPlugin().getServer().getPluginManager().registerEvents(listener, OMCPluginImpl.getJavaPlugin());
		registeredItems.add(item);
	}

	/**
	 * 
	 * @return a copy of the registered item list
	 * @see #registerItem(OMCItem)
	 */
	public ArrayList<OMCItem> getItems() {
		return new ArrayList<>(registeredItems);
	}

	/**
	 * Returns whether or not the manager recognizes this {@link ItemStack} as a registered {@link OMCItem}
	 * </br>
	 * 
	 * @param item
	 * The item to test for
	 * @return whether it is recognized
	 */
	public boolean contains(ItemStack item) {
		return fromItemStack(item) != null;
	}

	/**
	 * Will return the corresponding {@link OMCItem} registered in this manager, or null if it is unrecognized.
	 * @implNote
	 * Uses {@link OMCItem#matches(ItemStack)} to find the correlating {@link ItemStack}
	 * 
	 * @param item
	 * The {@link ItemStack} to correlate to
	 * @return The corresponding {@link OMCItem}
	 * @see #fromName(String)
	 */
	public OMCItem fromItemStack(ItemStack item) {
		return fromItemStack(item, registeredItems);
	}

	public OMCItem fromItemStack(ItemStack item, Collection<OMCItem> registeredItems) {
		if(item == null) return null;
		for(OMCItem omcItem : registeredItems) if(omcItem.equals(item)) return omcItem;
		return null;
	}

	/**
	 * Will return the corresponding {@link OMCItem} registered in this manager, or null if it is unrecognized
	 * @implNote
	 * This method uses {@link String#equalsIgnoreCase(String)} to find the correlating {@link OMCItem}
	 * 
	 * @param item
	 * The item name to search for
	 * @return The corresponding {@link OMCItem}
	 * @see #fromItemStack(ItemStack)
	 */
	public OMCItem fromName(String item) {
		for(OMCItem omcItem : registeredItems) if(omcItem.getRegistryName().equalsIgnoreCase(item)) return omcItem;
		return null;
	}
}