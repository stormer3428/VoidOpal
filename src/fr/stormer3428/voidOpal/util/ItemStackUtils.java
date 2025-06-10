package fr.stormer3428.voidOpal.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemStackUtils {

	public static NamespacedKey getNameSpacedKeyFromRecipe(Recipe recipe) {
		if(recipe instanceof ShapedRecipe keyHolder) return keyHolder.getKey();
		if(recipe instanceof ShapelessRecipe keyHolder) return keyHolder.getKey();
		if(recipe instanceof SmithingRecipe keyHolder) return keyHolder.getKey();
		if(recipe instanceof FurnaceRecipe keyHolder) return keyHolder.getKey();
		return null;
	}
	
	
	public static ItemStack createNamedItemstack(Material material, int stackSize, String name) {
		return createNamedItemstack(material, stackSize, name, true);
	}

	public static ItemStack createNamedItemstack(Material material, int stackSize, String name, boolean hideData) {
		return createNamedItemstack(new ItemStack(material, stackSize), name, hideData);
	}

	public static ItemStack createNamedItemstack(ItemStack it, String name) {
		return createNamedItemstack(it, name, true);
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createNamedItemstack(ItemStack it, String name, boolean hideData) {

		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(name);
		if(hideData) for(ItemFlag flag : ItemFlag.values()) itm.addItemFlags(flag);
		it.setItemMeta(itm);
		return it;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createCMDItemstack(Material material, String name, int CMD) {
		ItemStack it = new ItemStack(material);
		ItemMeta itm = it.getItemMeta();
		itm.setDisplayName(ChatColor.RESET + name);
		itm.setCustomModelData(CMD);
		it.setItemMeta(itm);
		return it;
	}
	
	
}
