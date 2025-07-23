package fr.stormer3428.voidOpal.Power.Types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Item.Tracking.PlayerConditions;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.data.OMCPowerHolder;
import fr.stormer3428.voidOpal.logging.OMCLogger;

public abstract class OMCMainPower extends OMCPower implements OMCPowerHolder {

	public OMCMainPower(String registryName) { super(registryName); }

	public abstract String getActionBar(Player p);
	public abstract void onPassiveStop(Player p);
	public abstract void onPassiveStart(Player p);
	public abstract OMCItem getItem();
	
	protected Map<UUID, Integer> selectedPower = new HashMap<>();
	protected List<UUID> HOLDING = new ArrayList<>();

	@Override public boolean cast(ItemStack it, Player p, Map<String, Object> metadata) {
		if (p.isSneaking()) {
			cycle(p);
			return false;
		}
		return castCurrent(it, p, metadata);
	}

	public int getSelected(Player p) {
		if (!selectedPower.containsKey(p.getUniqueId())) selectedPower.put(p.getUniqueId(), 0);
		return selectedPower.get(p.getUniqueId());
	}

	public boolean castCurrent(ItemStack it, Player p, Map<String, Object> metadata) {
		OMCPower power = getSelectedPower(p);
		if(power == null) return false;
		return power.tryCast(it, p, metadata);
	}

	public OMCPower getSelectedPower(Player p) {
		List<OMCPower> powers = getOmcPowers();
		final int size = powers.size();
		final int selected = getSelected(p);
		if (size <= selected) return null;
		return powers.get(selected);
	}

	public void cycle(Player p) {
		final int size = getOmcPowers().size();
		int selected = getSelected(p);
		if (size == 0) selected = 0;
		else if (++selected >= size) selected = 0;

		selectedPower.put(p.getUniqueId(), selected);
	}

	@Override public void onTick(int ticker) {
		super.onTick(ticker);
		List<UUID> NEW_HOLDING = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			boolean holding = matches(p);
			boolean wasHolding = HOLDING.contains(p.getUniqueId());
			if (holding) {
				if (!wasHolding) onPassiveStart(p);
				onPassiveTick(p, ticker);
				NEW_HOLDING.add(p.getUniqueId());
			} else if (wasHolding) onPassiveStop(p);
		}
		HOLDING = NEW_HOLDING;
	}
	
	public boolean matches(Player p) { return PlayerConditions.holdsItemInMainHand(p, getItem()); }
	public void onPassiveTick(Player p, int ticker) { onPassiveTick(p, ticker, p.getInventory().getItemInMainHand()); }
	public void onPassiveTick(Player p, int ticker, ItemStack it) { onActionBarTick(p); }
	public void onActionBarTick(Player p) { OMCLogger.actionBar(p, getActionBar(p)); }
	@Override public String getDisplayName() { return "Internal, Should not display"; }
	@Override public int getCooldown() { return 0; }

}
