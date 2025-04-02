package fr.stormer3428.voidOpal.Power.Types;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.Power.OMCTickable;
import fr.stormer3428.voidOpal.logging.OMCLogger;


/**
 * use {@link OMCEitherHandPassivePower} instead
 */
@Deprecated
public abstract class OMCPassiveHandPower implements OMCTickable{
	
	public final String registryName;
	public final OMCItem omcItem;
	
	public OMCPassiveHandPower(String registryName, OMCItem omcItem) {
		this.registryName = registryName;
		this.omcItem = omcItem;
	}
	
	public abstract void onHoldingTick(Player p, int ticker, ItemStack it, boolean offHand);
	public abstract void onStopHolding(Player p, boolean offHand);
	public abstract void onStartHolding(Player p, boolean offHand);
	public abstract void onStopHolding(Player p);
	public abstract void onStartHolding(Player p);
	public abstract void onSwap(Player p, boolean offHand);

	private ArrayList<UUID> wasHoldingMainHand = new ArrayList<>();
	private ArrayList<UUID> wasHoldingOffHand = new ArrayList<>();
	
	@Override
	public void onTick(int ticker) {
		if(omcItem == null) {
			OMCLogger.systemError("Tried to tick a passive with no OMCItem (" + registryName + ")");
			return;
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			boolean newMain = PlayerConditions.holdsItemInMainHand(p, omcItem);
			boolean newOff = PlayerConditions.holdsItemInOffHand(p, omcItem);
			boolean oldMain = wasHoldingMainHand.contains(p.getUniqueId());
			boolean oldOff = wasHoldingOffHand.contains(p.getUniqueId());

			if(newMain && !oldMain) {
				onStartHolding(p, false);
				wasHoldingMainHand.add(p.getUniqueId());
			}
			if(newOff && !oldOff) {
				onStartHolding(p, true);
				wasHoldingOffHand.add(p.getUniqueId());
			}
			if(!newMain && oldMain) {
				onStopHolding(p, false);
				wasHoldingMainHand.remove(p.getUniqueId());
			}
			if(!newOff && oldOff) {
				onStopHolding(p, true);
				wasHoldingOffHand.remove(p.getUniqueId());
			}

			if(oldOff && !newOff && !oldMain && newMain) onSwap(p, false);
			if(!oldOff && newOff && oldMain && !newMain) onSwap(p, true);
			
			if((newMain || newOff) && !(oldMain || oldOff)) onStartHolding(p);
			if(!(newMain || newOff) && (oldMain || oldOff)) onStopHolding(p);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		boolean main = wasHoldingMainHand.contains(p.getUniqueId());
		boolean off = wasHoldingOffHand.contains(p.getUniqueId());
		
		if(main) onStopHolding(p, false);
		if(off) onStopHolding(p, true);
	}
}
