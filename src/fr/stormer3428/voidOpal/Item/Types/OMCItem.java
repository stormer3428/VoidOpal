package fr.stormer3428.voidOpal.Item.Types;

import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.OMCItemManager;
import fr.stormer3428.voidOpal.data.OMCNameable;

/**
 * Represents a custom {@link ItemStack}
 * @author stormer3428
 * @see SMPItem
 * @see OMCItemManager
 *
 */
public interface OMCItem extends OMCNameable{

	//	public List<OMCPower> getPowers();
	public ItemStack createItemsStack(int amount);
	public boolean equals(ItemStack other);
	public String getRegistryName();
	
}
