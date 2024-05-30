package fr.stormer3428.voidOpal.Power.Types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.stormer3428.voidOpal.plugin.OMCPlugin;

public abstract class SetDurationPower extends UnsetDurationPower{
	
	public abstract int getDuration();
	
	public SetDurationPower(String registryName/*, OMCPowerManager powerManager*/) {
		super(registryName/*, powerManager*/);
	}
	
	public boolean empower(ItemStack it, Player p) {
		if(!cast(it, p)) return false;
		empowered.add(p.getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCPlugin.getJavaPlugin(), () -> putOnCooldown(p), getDuration());
		return true;
	}
}
