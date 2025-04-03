package fr.stormer3428.voidOpal.Power.passives;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;

public abstract class OMCMainHandPassivePower extends OMCPassivePower {

	public abstract OMCItem getItem();
	public abstract void onPassiveTick(Player p, int ticker, ItemStack it);

	public OMCMainHandPassivePower(String registryName) { super(registryName); }

	@Override public boolean matches(Player p) { return PlayerConditions.holdsItemInMainHand(p, getItem()); }
	@Override public void onPassiveTick(Player p, int ticker) {	onPassiveTick(p, ticker, p.getInventory().getItemInMainHand()); }

}
