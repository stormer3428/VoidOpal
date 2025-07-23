package fr.stormer3428.voidOpal.Power.Types;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.plugin.OMCCore;

public abstract class SetDurationPower extends UnsetDurationPower{
	
	public abstract int getDuration();
	
	public SetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}

	@Override
	public boolean empower(ItemStack it, Player p, Map<String, Object> metadata) {
		if(!cast(it, p, metadata)) return false;
		empowered.add(p.getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCCore.getJavaPlugin(), () -> putOnCooldown(p), getDuration());
		return true;
	}
}
