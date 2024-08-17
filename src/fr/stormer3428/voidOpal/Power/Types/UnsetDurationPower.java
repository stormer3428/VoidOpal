package fr.stormer3428.voidOpal.Power.Types;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UnsetDurationPower extends OMCPower{

	protected ArrayList<UUID> empowered = new ArrayList<>();
	
	public void onDepower(Player p) {}
	public boolean onEmpoweredTryCast(ItemStack it, Player p) {return false;}

	public UnsetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}

	@Override
	public boolean tryCast(ItemStack it, Player p) {
		if(!isEnabled()) return false;
		if(isOnCooldown(p)) return false;
		if(empowered.contains(p.getUniqueId())) return onEmpoweredTryCast(it, p);
		empower(it, p);
		return true;
	}

	/*
	 * It is expected to call "putOnCooldown when ability ends"
	 */
	public boolean empower(ItemStack it, Player p) {
		if(!cast(it, p)) return false;
		empowered.add(p.getUniqueId());
		return true;
	}

	public void putOnCooldown(UUID uuid, int abilityCooldown) {
		Player p = Bukkit.getPlayer(uuid);
		if(p != null && empowered.contains(uuid)) onDepower(p);
		empowered.remove(uuid);
		super.putOnCooldown(uuid, abilityCooldown);
	}


	public boolean isEmpowered(Player p) {
		return empowered.contains(p.getUniqueId());
	}

}
