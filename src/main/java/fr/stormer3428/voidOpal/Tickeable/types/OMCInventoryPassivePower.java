package fr.stormer3428.voidOpal.Tickeable.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.OMCInventoryItemTracker;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;

public abstract class OMCInventoryPassivePower extends OMCPassivePower {

	private final OMCInventoryItemTracker inventoryItemTracker;

	public abstract OMCItem getItem();
	public abstract void onPassiveTick(Player p, int ticker, ItemStack it);

	public OMCInventoryPassivePower(String registryName, OMCInventoryItemTracker inventoryItemTracker) {
		super(registryName);
		this.inventoryItemTracker = inventoryItemTracker;
	}

	@Override public boolean matches(Player p) { return inventoryItemTracker.isInventoryHoldingItem(p, getItem()); }
	@Override public void onPassiveTick(Player p, int ticker) { onPassiveTick(p, ticker, null); }

}
