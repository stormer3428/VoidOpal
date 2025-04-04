package fr.stormer3428.voidOpal.Item.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.stormer3428.voidOpal.Power.Types.OMCPower;
import fr.stormer3428.voidOpal.Tickeable.OMCTickeable;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.util.OMCUtil;

public class SMPItem implements OMCItem {

	public SMPItem(String registryName) {
		this.registryName = registryName;
	}

	private Material material = Material.ENCHANTED_BOOK;
	private String displayName = null;
	private int CMD = 0;
	private String registryName = getClass().getSimpleName();
	private final ArrayList<String> lore = new ArrayList<>();
	private final ArrayList<ItemFlag> itemFlags = new ArrayList<>();
	private final ArrayList<OMCPower> omcPowers = new ArrayList<>();
	private final ArrayList<OMCTickeable> omcTickeables = new ArrayList<>();
	private final ArrayList<Listener> listeners = new ArrayList<>();
	private HashMap<Enchantment, Integer> enchants = new HashMap<>();
	private HashMap<PersistentDataType<Object, ? extends Object>, HashMap<String, Object>> data = new HashMap<>();
	private boolean unbreakeable = false;

	@Override public String getRegistryName() { return registryName;}
	public Material getMaterial(){return material;}
	public String getDisplayName(){return displayName;}
	public int getCMD(){return CMD;}
	public List<String> getLore(){return lore;}
	public List<ItemFlag> getItemFlags(){return itemFlags;}
	public Map<Enchantment,Integer> getEnchants(){return enchants;}
	public List<OMCPower> getOmcPowers() {return omcPowers;}
	public List<OMCTickeable> getOmcTickeables() {return omcTickeables;}
	public List<Listener> getListener() {return listeners;}

	public SMPItem setMaterial(Material material){ this.material = material; return this;}
	public SMPItem setDisplayname(String displayname){ this.displayName = displayname; return this;}
	public SMPItem setCmd(int cmd){ this.CMD = cmd; return this;}
	public SMPItem setLore(List<String> lore){ this.lore.clear(); this.lore.addAll(lore); return this;}
	public SMPItem setItemflags(List<ItemFlag> itemflags){ this.itemFlags.clear(); this.itemFlags.addAll(itemflags); return this;}
	public SMPItem setEnchants(HashMap<Enchantment,Integer> enchants){ this.enchants = enchants; return this;}

	public SMPItem addItemflag(ItemFlag itemflag){ this.itemFlags.add(itemflag); return this;}
	public SMPItem addEnchant(Enchantment enchant, int level){ this.enchants.put(enchant, level); return this;}
	public SMPItem addPower(OMCPower omcPower) { omcPowers.add(omcPower); return this;}
	public SMPItem addTickeable(OMCTickeable omcTickeable) { omcTickeables.add(omcTickeable); return this;}
	public SMPItem addListener(Listener listener) { listeners.add(listener); return this;}
	public <T extends Listener & OMCTickeable> SMPItem addTickeableListener(T tickeableListener) { return this;}
	@SuppressWarnings("unchecked")
	public <P, C extends Object> SMPItem addData(String name, PersistentDataType<P, C> dataType, C value) {data.computeIfAbsent((PersistentDataType<Object, ? extends Object>) dataType, (t) -> new HashMap<String, Object>()).put(name, value);return this;}

	public SMPItem unbreakable() { this.unbreakeable = true; return this;}

	public YamlConfiguration getConfig() {
		YamlConfiguration config = new YamlConfiguration();
		config.addDefault("material", material.name());
		config.addDefault("displayname", displayName);
		config.addDefault("cmd", CMD);
		config.addDefault("lore", lore);
		config.addDefault("itemflags", itemFlags.stream().map(f -> f.name()).collect(Collectors.toList()));
		for(Enchantment e : enchants.keySet()) if(e.isRegistered()) config.addDefault("enchants." + e.getKeyOrNull(), enchants.get(e));
		config.addDefault("unbreakable", unbreakeable);
		for(String s : config.getDefaults().getKeys(true)) config.set(s, config.get(s));
		return config;
	}

	public void syncToConfig(YamlConfiguration config) {
		String materialString = config.getString("material"); Material mat = Material.valueOf(materialString);
		if(mat == null) OMCLogger.systemError("Tried to set material of " + registryName + " to invalid material, " + materialString);
		else setMaterial(mat);
		setDisplayname(config.getString("displayname"));
		setCmd(config.getInt("cmd"));
		setLore(config.getStringList("lore"));
		setItemflags(config.getStringList("itemflags").stream().map(s -> {
			try {
				return ItemFlag.valueOf(s);
			}catch (Exception e) {
				OMCLogger.systemError("Invalid itemFlag " + s);
				return null;
			}
		}).filter(f -> f!=null).collect(Collectors.toList()));
		x:{
			getEnchants().clear();
			ConfigurationSection section = config.getConfigurationSection("enchants");
			if(section == null) break x;
			for(String key : section.getKeys(false)) {
				@SuppressWarnings("deprecation") Enchantment ench = Enchantment.getByName(key);
				if(ench == null) {
					OMCLogger.systemNormal("Invalid enchantment " + key);
					continue;
				}
				addEnchant(ench, section.getInt(key));
			}
		}
		unbreakeable = config.getBoolean("unbreakable");
	}

	@Override
	public ItemStack createItemsStack(int amount) {
		Material material = getMaterial();
		String displayName = getDisplayName();
		int CMD = getCMD();
		List<String> lore = getLore();
		List<ItemFlag> itemFlags = getItemFlags();
		Map<Enchantment,Integer> enchants = getEnchants();
		ItemStack it = new ItemStack(material, amount);
		if(amount == 0 || !material.isItem()) return it;
		ItemMeta itm = it.getItemMeta();
		if(itm != null) {
			if(displayName != null) itm.setDisplayName(ChatColor.RESET + OMCUtil.translateChatColor(displayName));
			if(CMD != 0) itm.setCustomModelData(CMD);
			if(!lore.isEmpty()) {
				ArrayList<String> translated = new ArrayList<>();
				for(String s : lore) translated.add(OMCUtil.translateChatColor(s));
				itm.setLore(translated);
			}
			if(!itemFlags.isEmpty()) for(ItemFlag flag : itemFlags) itm.addItemFlags(flag);
			if(!enchants.isEmpty()) for(Entry<Enchantment, Integer> entry : enchants.entrySet()) itm.addEnchant(entry.getKey(), entry.getValue(), true);
			if(unbreakeable) itm.setUnbreakable(true);
			if(!data.isEmpty()) {
				PersistentDataContainer dataContainer = itm.getPersistentDataContainer();
				for(Entry<PersistentDataType<Object, ? extends Object>, HashMap<String, Object>> dataTypeEntry : data.entrySet()) {
					@SuppressWarnings("unchecked")
					PersistentDataType<Object, Object> dataType = (PersistentDataType<Object, Object>) dataTypeEntry.getKey();
					HashMap<String, Object> valueMap = dataTypeEntry.getValue();
					if(valueMap.isEmpty()) continue;
					for(Entry<String, Object> entry : valueMap.entrySet()) {
						String name = entry.getKey();
						Object value = entry.getValue();
						NamespacedKey key = getNSK(name);
						dataContainer.set(key, dataType, value);
					}
				}
			}
			it.setItemMeta(itm);
		}
		return it;
	}

	private static final Map<String, NamespacedKey> NSKs = new HashMap<>();
	private NamespacedKey getNSK(String name) {
		return NSKs.computeIfAbsent(name, (s) -> new NamespacedKey(OMCCore.getJavaPlugin(), s));
	}

	@Override
	public boolean equals(ItemStack other) {
		if(other == null) return false;
		if(other.getType() != getMaterial()) return false;
		ItemMeta meta = other.getItemMeta();
		if(meta == null) return false;
		if(!meta.hasCustomModelData()) return CMD==0;
		if(meta.getCustomModelData() != getCMD()) return false;
		return true;
	}

}
