package fr.stormer3428.voidOpal.Power.Types;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UnsetDurationPower extends OMCPower{

	protected ArrayList<UUID> empowered = new ArrayList<>();
	
	public void onDepower(Player p) {}
	public boolean onEmpoweredTryCast(ItemStack it, Player p, Map<String, Object> metadata) {return false;}

	public UnsetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}

	@Override
	public boolean tryCast(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!isEnabled()) return false;
		if(empowered.contains(p.getUniqueId())) return onEmpoweredTryCast(it, p, metadata);
		if(isOnCooldown(p)) return false;
		empower(it, p, metadata);
		return true;
	}

	/*
	 * It is expected to call "putOnCooldown when ability ends"
	 */
	@Override
	public boolean empower(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!cast(it, p, metadata)) return false;
		empowered.add(p.getUniqueId());
		return true;
	}

	@Override
	public void putOnCooldown(UUID uuid, int abilityCooldown) {
		Player p = Bukkit.getPlayer(uuid);
		if(p != null && empowered.contains(uuid)) onDepower(p);
		empowered.remove(uuid);
		super.putOnCooldown(uuid, abilityCooldown);
	}

	public boolean isEmpowered(Player p) { return isEmpowered(p.getUniqueId()); }

	public boolean isEmpowered(UUID uuid) { return empowered.contains(uuid); }

}
