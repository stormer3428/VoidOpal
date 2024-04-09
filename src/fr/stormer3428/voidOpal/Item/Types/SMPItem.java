package fr.stormer3428.voidOpal.Item.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.voidOpal.Power.Types.OMCPower;
import fr.stormer3428.voidOpal.util.OMCUtil;

public class SMPItem implements OMCItem {

	public SMPItem(String registryName) {
		this.registryName = registryName;
	}
	
	private Material material = Material.ENCHANTED_BOOK;
	private String displayName = null;
	private int CMD = 0;
	private String registryName = getClass().getSimpleName();
	private ArrayList<String> lore = new ArrayList<>();
	private ArrayList<ItemFlag> itemFlags = new ArrayList<>();
	private ArrayList<OMCPower> omcPowers = new ArrayList<>();
	private HashMap<Enchantment, Integer> enchants = new HashMap<>();
	
	private boolean unbreakeable = false;

	@Override public String getRegistryName() { return registryName;}
//	@Override public List<OMCPower> getPowers() {return omcPowers;}

	public Material getMaterial(){return material;}
	public String getDisplayName(){return displayName;}
	public int getCMD(){return CMD;}
	public ArrayList<String> getLore(){return lore;}
	public ArrayList<ItemFlag> getItemFlags(){return itemFlags;}
	public HashMap<Enchantment,Integer> getEnchants(){return enchants;}
	public ArrayList<OMCPower> getOmcPowers() {return omcPowers;}

	public SMPItem setMaterial(Material material){ this.material = material; return this;}
	public SMPItem setDisplayname(String displayname){ this.displayName = displayname; return this;}
	public SMPItem setCmd(int cmd){ this.CMD = cmd; return this;}
	public SMPItem setLore(List<String> lore){ this.lore.clear(); this.lore.addAll(lore); return this;}
	public SMPItem setItemflags(List<ItemFlag> itemflags){ this.itemFlags.clear(); this.itemFlags.addAll(itemflags); return this;}
	public SMPItem setEnchants(HashMap<Enchantment,Integer> enchants){ this.enchants = enchants; return this;}

	public SMPItem addItemflag(ItemFlag itemflag){ this.itemFlags.add(itemflag); return this;}
	public SMPItem addEnchant(Enchantment enchant, int level){ this.enchants.put(enchant, level); return this;}
	public SMPItem addPower(OMCPower omcPower) { omcPowers.add(omcPower); return this;}

	public SMPItem unbreakable() { this.unbreakeable = true; return this;}

	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		String displayName = getDisplayName();
		int CMD = getCMD();
		ArrayList<String> lore = getLore();
		ArrayList<ItemFlag> itemFlags = getItemFlags();
		HashMap<Enchantment,Integer> enchants = getEnchants();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || !material.isItem()) return it;
		ItemMeta itm = it.getItemMeta();
		if(displayName != null) itm.setDisplayName(ChatColor.RESET + OMCUtil.translateChatColor(displayName));
		if(CMD != 0) itm.setCustomModelData(CMD);
		if(!lore.isEmpty()) {
			ArrayList<String> translated = new ArrayList<>();
			for(String s : lore) translated.add(OMCUtil.translateChatColor(s));
			itm.setLore(lore);
		}
		if(!itemFlags.isEmpty()) for(ItemFlag flag : itemFlags) itm.addItemFlags(flag);
		if(!enchants.isEmpty()) for(Entry<Enchantment, Integer> entry : enchants.entrySet()) itm.addEnchant(entry.getKey(), entry.getValue(), true);
		if(unbreakeable) itm.setUnbreakable(true);
		it.setItemMeta(itm);
		return it;
	}

	@Override
	public boolean equals(ItemStack other) {
		if(other == null) return false;
		if(other.getType() != getMaterial()) return false;
		ItemMeta meta= other.getItemMeta();
		if(meta == null) return false;
		if(!meta.hasCustomModelData()) return false;
		if(meta.getCustomModelData() != getCMD()) return false;
		return true;
	}

}
