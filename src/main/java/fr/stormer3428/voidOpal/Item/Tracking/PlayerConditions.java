package fr.stormer3428.voidOpal.Item.Tracking;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Types.OMCItem;

public class PlayerConditions {

	public static final boolean holdsItemInMainHand(Player p, OMCItem omcItem) {
		return omcItem.equals(p.getInventory().getItemInMainHand());
	}
	
	public static final boolean holdsItemInOffHand(Player p, OMCItem omcItem) {
		return omcItem.equals(p.getInventory().getItemInOffHand());
	}
	
	public static final boolean holdsItemInHand(Player p, OMCItem omcItem) {
		return holdsItemInMainHand(p, omcItem) || holdsItemInOffHand(p, omcItem);
	}

	public static final boolean holdsItemInInventory(Player p, OMCItem omcItem) {
		for(ItemStack it : p.getInventory().getContents()) if(omcItem.equals(it)) return true;
		return false;
	}
	
}
