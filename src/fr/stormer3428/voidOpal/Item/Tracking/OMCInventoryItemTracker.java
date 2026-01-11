package fr.stormer3428.voidOpal.Item.Tracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCInventoryItemTracker implements PluginTied, Listener{

	public OMCInventoryItemTracker() {registerSelf();}
	
	@Override public void onPluginEnable() {OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());}
	@Override public void onPluginDisable() {}
	@Override public void onPluginReload() {}

	/*
	/(\w)(\w+)\n?/gm
	\tprivate final ArrayList<OMCItem> $1$2TrackedItems = new ArrayList<>();\n\tprivate final HashMap<UUID, ArrayList<OMCItem>> $1$2TrackerMap = new HashMap<>();\n\tpublic void register\U$1\E$2TrackedItem(OMCItem omcItem) {$1$2TrackedItems.add(omcItem);}\n\tpublic boolean is\U$1\E$2HoldingItem(Player p, OMCItem omcItem) {return is\U$1\E$2HoldingItem(p.getUniqueId(), omcItem);}\n\tpublic boolean is\U$1\E$2HoldingItem(UUID uuid, OMCItem omcItem) {return get\U$1\E$2TrackedItemsForPlayer(uuid).contains(omcItem);}\n\tpublic ArrayList<OMCItem> get\U$1\E$2TrackedItemsForPlayer(Player p){return get\U$1\E$2TrackedItemsForPlayer(p.getUniqueId());}\n\tpublic ArrayList<OMCItem> get\U$1\E$2TrackedItemsForPlayer(UUID uuid){\n\t\tif(!$1$2TrackerMap.containsKey(uuid)) $1$2TrackerMap.put(uuid, new ArrayList<>());\n\t\treturn $1$2TrackerMap.get(uuid);\n\t}\n\n

	hand
	mainHand
	offHand
	inventory
	//*/

	private final HashSet<OMCItem> inventoryTrackedItems = new HashSet<>();
	private final HashMap<UUID, ArrayList<OMCItem>> inventoryTrackerMap = new HashMap<>();
	public void registerInventoryTrackedItem(OMCItem omcItem) {inventoryTrackedItems.add(omcItem);}
	public boolean isInventoryHoldingItem(Player p, OMCItem omcItem) {return getInventoryTrackedItemsForPlayer(p).contains(omcItem);}
	public ArrayList<OMCItem> getInventoryTrackedItemsForPlayer(Player p){
		UUID uuid = p.getUniqueId();
		if(!inventoryTrackerMap.containsKey(uuid)) {
			ArrayList<OMCItem> list = new ArrayList<>();
			for(ItemStack it : p.getInventory().getContents()) if(it != null) for(OMCItem omcItem : inventoryTrackedItems) if(omcItem.equals(it)) list.add(omcItem);
			inventoryTrackerMap.put(uuid, list);
		}
		return inventoryTrackerMap.get(uuid);
	}

	public void clearCache(Player p) {
		OMCLogger.debug("Item cache cleared for " + p.getName());
		clearCache(p.getUniqueId());
	}

	public void clearCache(UUID uuid) { inventoryTrackerMap.remove(uuid); }

	@EventHandler public void onInventoryClick(InventoryClickEvent e) {
		for (HumanEntity he : e.getViewers()) if (he instanceof Player p) clearCache(p);
		if (e.getInventory().getHolder() instanceof Player p) clearCache(p);
	}

	@EventHandler public void onPickup(EntityPickupItemEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;
		clearCache(p);
	}

	@EventHandler public void onDeath(PlayerDeathEvent e) { clearCache(e.getEntity()); }
	@EventHandler public void onQuit(PlayerQuitEvent e) { clearCache(e.getPlayer()); }
	@EventHandler public void onJoin(PlayerJoinEvent e) { clearCache(e.getPlayer()); }
	@EventHandler public void onDrop(PlayerDropItemEvent e) { clearCache(e.getPlayer()); }
	@EventHandler public void onPlace(BlockPlaceEvent e) { clearCache(e.getPlayer()); }



}
