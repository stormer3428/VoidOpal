package fr.stormer3428.voidOpal.Power.Types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.Listener.OMCNamedListener;
import fr.stormer3428.voidOpal.Tickeable.OMCTickeable;

public abstract class OMCPower extends OMCNamedListener implements OMCTickeable{

	public OMCPower(String registryName) { super(registryName); }
	
	@Override public void onPluginDisable() {}
	@Override public void onPluginReload() {}
	@Override public void onPluginEnable() {}

	public boolean isEnabled() {return true;}
	public void onCooldownEnd(Player p) {}

	public abstract int getCooldown();
	public abstract boolean cast(ItemStack it, Player p, Map<String, Object> metadata);
	public abstract String getDisplayName();

	protected HashMap<UUID, Integer> onCooldown = new HashMap<>();
	
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
			iterator.remove();
			Player p = Bukkit.getPlayer(uuid);
			if(p == null) continue;
			onCooldownEnd(p);
		}
	}

	public boolean tryCast(ItemStack it, Player p) {return tryCast(it, p, new HashMap<String, Object>());}
	public boolean tryCast(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!isEnabled())return false;
		if(isOnCooldown(p)) return false;
		empower(it, p, metadata);
		return true;
	}

	public boolean empower(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!cast(it, p, metadata)) return false;
		putOnCooldown(p);
		return true;
	}
	
	@Override public String toString() { return getRegistryName(); }

	public HashMap<UUID, Integer> getCooldownMap() { return onCooldown; }
	public boolean isOnCooldown(Player p) {return isOnCooldown(p.getUniqueId());}
	public boolean isOnCooldown(UUID uuid) {return onCooldown.containsKey(uuid);}
	public void putOnCooldown(Player p) {putOnCooldown(p.getUniqueId());}
	public void putOnCooldown(Player p, int abilityCooldown) {putOnCooldown(p.getUniqueId(), abilityCooldown);}
	public void putOnCooldown(UUID uuid) {putOnCooldown(uuid, getCooldown());}
	public void putOnCooldown(UUID uuid, int abilityCooldown) {onCooldown.put(uuid, Math.max(getCooldown(uuid), abilityCooldown));}
	public void clearCooldown(Player p) {onCooldown.remove(p.getUniqueId());}
	public int getCooldown(Player p) {return getCooldown(p.getUniqueId());}
	public int getCooldown(UUID uuid) {if(!isOnCooldown(uuid)) return 0;return onCooldown.get(uuid);}
	public void clearCooldowns() {onCooldown.clear();}
}
