package fr.stormer3428.voidOpal.Power.Types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class OMCMultichargePower extends OMCPower{

	protected final HashMap<UUID, Integer> ON_COOLDOWN_CHARGES = new HashMap<>(); // inverted so that players without that power are simply not in the map

	public OMCMultichargePower(String registryName) { super(registryName); }

	public abstract int getMaxCharges();

	@Override
	public boolean tryCast(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!isEnabled())return false;
		if(ON_COOLDOWN_CHARGES.computeIfAbsent(p.getUniqueId(), u->0) >= getMaxCharges()) return false;
		empower(it, p, metadata);
		return true;
	}

	@Override
	public void putOnCooldown(UUID uuid, int abilityCooldown) {
		int spentCharges = ON_COOLDOWN_CHARGES.computeIfAbsent(uuid, u->0);
		ON_COOLDOWN_CHARGES.put(uuid, spentCharges + 1);
		onCooldown.putIfAbsent(uuid, Math.max(getCooldown(uuid), abilityCooldown));
	}

	@Override
	public void onTick(int ticker) {
		Iterator<Entry<UUID, Integer>> iterator = onCooldown.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<UUID, Integer> entry = iterator.next();
			UUID uuid = entry.getKey();
			int cooldown = entry.getValue();
			cooldown --;
			if(cooldown > 0) {
				entry.setValue(cooldown);
				continue;
			}

			int spentCharges = ON_COOLDOWN_CHARGES.get(uuid); // a computeIfAbsent would be a failsafe, but there should never be a cooldown if theres no charges to refresh
			spentCharges--;// cooldown is over, we reset the cooldown and decrement the charge count
			if(spentCharges == 0) {// no more charges to fill up, we can remove him entirely 
				iterator.remove(); 
				ON_COOLDOWN_CHARGES.remove(uuid);
			}
			else{ // there are still spent charges to recharge, we reset the cooldown to refresh the next charge
				ON_COOLDOWN_CHARGES.put(uuid, spentCharges);
				entry.setValue(getCooldown());
			}
			Player p = Bukkit.getPlayer(uuid);
			if(p == null) continue;
			onCooldownEnd(p);
		}
	}

	public int getCharges(Player p) { return getCharges(p.getUniqueId()); }
	public int getCharges(UUID uuid) { return getMaxCharges() - ON_COOLDOWN_CHARGES.getOrDefault(uuid, 0); }
	
}
