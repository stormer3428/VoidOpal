package fr.stormer3428.voidOpal.Power.passives;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;

public abstract class OMCHandPassivePower extends OMCPassivePower {

	public abstract OMCItem getItem();
	public abstract void onPassiveTick(Player p, int ticker, ItemStack it);

	public OMCHandPassivePower(String registryName) { super(registryName); }

	private ItemStack matchCache;

	@Override public synchronized void onTick(int ticker) { super.onTick(ticker); }
	
	@Override public synchronized boolean matches(Player p) { 
		if(PlayerConditions.holdsItemInMainHand(p, getItem())) {
			matchCache = p.getInventory().getItemInMainHand();
			return true; 
		}
		if(PlayerConditions.holdsItemInOffHand(p, getItem())) {
			matchCache = p.getInventory().getItemInOffHand();
			return true;
		}
		return false;
	}
	
	@Override public synchronized void onPassiveTick(Player p, int ticker) {	
		onPassiveTick(p, ticker, matchCache); 
		matchCache = null;
	}

}
