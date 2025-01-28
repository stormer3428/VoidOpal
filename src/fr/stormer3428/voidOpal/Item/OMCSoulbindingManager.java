package fr.stormer3428.voidOpal.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public abstract class OMCSoulbindingManager implements PluginTied, Listener{

	protected abstract boolean isSoulboundItem(ItemStack it);
	protected abstract boolean isSuitable(ArrayList<ItemStack> soulboundItems);
	protected abstract void fixInventory(ArrayList<ItemStack> soulboundItems, Player p);

	@Override
	public void onPluginEnable() {
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginReload() {}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		ItemStack item = e.getItemDrop().getItemStack();
		Player p = e.getPlayer();
		if(isSoulboundItem(item)) {
			if(!restituteMap.containsKey(p.getUniqueId())) restituteMap.put(p.getUniqueId(), new ArrayList<ItemStack>());
			restituteMap.get(p.getUniqueId()).add(new ItemStack(item));
			item.setAmount(0);
		}
		validate(p);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		validate((Player) e.getPlayer());
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		validate((Player) e.getPlayer());
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		Inventory top = e.getView().getTopInventory();
		if(!isSoulboundItem(e.getOldCursor())) return;
		if(top instanceof CraftingInventory ci && ci.getSize() == 5) {
			for(int i : e.getRawSlots()) if(i < 9){
				e.setCancelled(true);
				return;
			}
			return;
		}
		for(int i : e.getRawSlots()) {
			//			Bukkit.broadcastMessage(i + "");
			if(i == e.getView().convertSlot(i)) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler 
	public void inventoryClick(InventoryClickEvent e) {
		Inventory top = e.getView().getTopInventory();

		boolean clickedTop = e.getView().getTopInventory().equals(e.getClickedInventory());

		boolean offHandsoulbound = e.getClick().equals(ClickType.SWAP_OFFHAND) && isSoulboundItem(e.getWhoClicked().getInventory().getItemInOffHand());
		boolean cursorSoulbound = isSoulboundItem(e.getCursor());
		boolean hotbarSoulbound = e.getHotbarButton() != -1 && isSoulboundItem(e.getView().getBottomInventory().getItem(e.getHotbarButton()));

		boolean inSurvivalInv = top instanceof CraftingInventory ci && ci.getSize() == 5;
		boolean currentSoulbound = isSoulboundItem(e.getCurrentItem());
		boolean shiftClick = e.isShiftClick();

		boolean cursorBundle = e.getCursor() != null && e.getCursor().getType().equals(Material.BUNDLE);
		boolean currentBundle = e.getCurrentItem() != null && e.getCurrentItem().getType().equals(Material.BUNDLE);

		if 		((clickedTop && (!shiftClick && (offHandsoulbound || cursorSoulbound || hotbarSoulbound))) 
				|| (!clickedTop && shiftClick && !inSurvivalInv && currentSoulbound)
				|| (cursorSoulbound && currentBundle)
				|| (currentSoulbound && cursorBundle)
			) e.setCancelled(true);
	}

	protected final HashMap<UUID, ArrayList<ItemStack>> restituteMap = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		ArrayList<ItemStack> toRemove = new ArrayList<>();
		List<ItemStack> drops = e.getDrops();
		for(ItemStack item : drops) if(isSoulboundItem(item)) toRemove.add(item);
		for(ItemStack item : toRemove) {
			drops.remove(item);
			if(!restituteMap.containsKey(p.getUniqueId())) restituteMap.put(p.getUniqueId(), new ArrayList<ItemStack>());
			restituteMap.get(p.getUniqueId()).add(item);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		validate(p);
	}

	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack it = p.getInventory().getItem(e.getHand());
		if(it == null || !isSoulboundItem(it)) return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		ItemStack it = p.getInventory().getItem(e.getHand());
		if(it == null || !isSoulboundItem(it)) return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block clicked = e.getClickedBlock();
		if(clicked == null || !clicked.getType().isInteractable()) return;
		ItemStack it = p.getInventory().getItem(e.getHand());
		if(it == null || !isSoulboundItem(it)) return;
		e.setCancelled(true);
	}

	public ArrayList<ItemStack> getSoulboundItems(Player p){
		ArrayList<ItemStack> soulboundItems = new ArrayList<>(); 
		List<ItemStack> toCheck = StreamSupport.stream(p.getInventory().spliterator(), false)
				.collect(Collectors.toList());
		toCheck.add(p.getOpenInventory().getCursor());
		for(ItemStack it : toCheck) {
			if(it == null) continue;
			if(!isSoulboundItem(it)) continue;
			soulboundItems.add(it);
		}
		return soulboundItems;
	}

	public boolean validate(Player p) {
		if(p.isDead()) return false;
		if(restituteMap.containsKey(p.getUniqueId())) {
			ArrayList<ItemStack> list = restituteMap.remove(p.getUniqueId());
			ArrayList<ItemStack> remaining = new ArrayList<ItemStack>();
			for(ItemStack it : list) for(Entry<Integer, ItemStack> entry : p.getInventory().addItem(it).entrySet()) remaining.add(entry.getValue());
			restituteMap.put(p.getUniqueId(), remaining);
		}

		ArrayList<ItemStack> soulboundItems = getSoulboundItems(p);
		if(isSuitable(soulboundItems)) return true;
		fixInventory(soulboundItems, p);
		return false;
	}
}

