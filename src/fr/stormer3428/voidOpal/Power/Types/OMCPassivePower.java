package fr.stormer3428.voidOpal.Power.Types;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.Power.OMCTickable;

public abstract class OMCPassivePower extends OMCPower implements OMCTickable{

	public OMCPassivePower(String registryName) {
		super(registryName);
	}
	
	public abstract OMCItem getItem();

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public boolean cast(ItemStack it, Player p, Map<String, Object> metadata) {
		return false;
	}

	@Override
	public void onCooldownEnd(Player p) {}

	@Override
	public String getDisplayName() {
		return "passive.displayname." + getClass().getSimpleName();
	}
}
