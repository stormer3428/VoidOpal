package fr.stormer3428.voidOpal.Power.Types;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;

public abstract class OMCOffhandSwapMainPower extends OMCMainPower {

	public OMCOffhandSwapMainPower(String registryName) { super(registryName); }
	@Override public boolean cast(ItemStack it, Player p, Map<String, Object> metadata) { return castCurrent(it, p, metadata); }
	@Override public boolean matches(Player p) { return PlayerConditions.holdsItemInOffHand(p, getItem()); }
	@Override public void onPassiveStart(Player p) {}	
	@Override public void onPassiveStop(Player p) {}
	@Override public int getSelected(Player p) { return p.getInventory().getHeldItemSlot(); }

}