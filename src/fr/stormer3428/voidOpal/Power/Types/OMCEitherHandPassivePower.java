package fr.stormer3428.voidOpal.Power.Types;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.logging.OMCLogger;

public abstract class OMCEitherHandPassivePower extends OMCPassivePower{

	public OMCEitherHandPassivePower(String registryName) {
		super(registryName);
	}
	
	public abstract OMCItem getItem();
	public abstract void onHoldingTick(Player p, int ticker, ItemStack it);
	public abstract void onStopHolding(Player p);
	public abstract void onStartHolding(Player p);

	private ArrayList<UUID> HOLDING = new ArrayList<>();
	
	@Override
	public void onTick(int ticker) {
		OMCItem omcItem = getItem();
		if(omcItem == null) {
			OMCLogger.systemError("Tried to tick a passive with no OMCItem (" + getRegistryName() + ")");
			return;
		}
		
		ArrayList<UUID> NEW_HOLDING = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			boolean holding = PlayerConditions.holdsItemInHand(p, omcItem);
			boolean wasHolding = HOLDING.contains(p.getUniqueId());
			if(holding) {
				if(!wasHolding) onStartHolding(p);
				onHoldingTick(p, ticker, p.getInventory().getItemInOffHand());
				NEW_HOLDING.add(p.getUniqueId());
			} else if(wasHolding) onStopHolding(p);
		}
	}
}
