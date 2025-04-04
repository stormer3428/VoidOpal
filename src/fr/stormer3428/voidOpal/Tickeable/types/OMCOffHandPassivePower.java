package fr.stormer3428.voidOpal.Tickeable.types;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;

public abstract class OMCOffHandPassivePower extends OMCPassivePower {

	public abstract OMCItem getItem();
	public abstract void onPassiveTick(Player p, int ticker, ItemStack it);

	public OMCOffHandPassivePower(String registryName) { super(registryName); }

	@Override public boolean matches(Player p) { return PlayerConditions.holdsItemInOffHand(p, getItem()); }
	@Override public void onPassiveTick(Player p, int ticker) {	onPassiveTick(p, ticker, p.getInventory().getItemInOffHand()); }

}
