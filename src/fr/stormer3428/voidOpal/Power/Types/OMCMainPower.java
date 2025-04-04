//package fr.stormer3428.voidOpal.Power.Types;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//
//import fr.stormer3428.voidOpal.Power.passives.OMCMainHandPassivePower;
//import fr.stormer3428.voidOpal.logging.OMCLogger;
//
//public abstract class OMCMainPower extends OMCMainHandPassivePower{
//
//	public OMCMainPower(String registryName) {
//		super(registryName);
//	}
//
//	public abstract ArrayList<OMCPower> getAvailablePowers(Player p);
//
//	protected HashMap<UUID, Integer> selectedPower = new HashMap<>();
//	
//	@Override
//	public boolean cast(ItemStack it, Player p, Map<String, Object> metadata) {
//		if(p.isSneaking()) {
//			cycle(p);
//			return true;
//		}
//		return castCurrent(it, p, metadata);
//	}
//
//	public int getSelected(Player p) {
//		if(!selectedPower.containsKey(p.getUniqueId())) selectedPower.put(p.getUniqueId(), 0);
//		return selectedPower.get(p.getUniqueId());
//	}
//
//	public boolean castCurrent(ItemStack it, Player p, Map<String, Object> metadata) {
//		ArrayList<OMCPower> powers = getAvailablePowers(p);
//		if(powers.isEmpty()) return false;
//		int selected = getSelected(p);
//		return powers.get(selected).tryCast(it, p, metadata);
//	}
//
//	public OMCPower getSelectedPower(Player p) {
//		ArrayList<OMCPower> powers = getAvailablePowers(p);
//		final int size = powers.size();		
//		if(size == 0) return null;
//		return powers.get(getSelected(p));
//	}
//
//	public void cycle(Player p) {
//		final int size = getAvailablePowers(p).size();				
//		int selected = getSelected(p);
//		if(size == 0) selected = 0;
//		else if(++selected >= size) selected = 0;
//
//		selectedPower.put(p.getUniqueId(), selected);
//	}
//
//
//	public abstract String getActionBar(Player p);
//	
//	public void onActionBarTick(Player p) {
//		OMCLogger.actionBar(p, getActionBar(p));
//	}
//	
//	@Override
//	public void onMainhandHoldingTick(Player p, int ticker, ItemStack it) {
//		onActionBarTick(p);
//	}
//
//	@Override
//	public void onMainhandStartHolding(Player p) {}
//
//	@Override
//	public void onMainhandStopHolding(Player p) {}
//	
//}
