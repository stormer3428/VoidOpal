package fr.stormer3428.voidOpal.damage;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

@SuppressWarnings("deprecation")
public class OMCTrueDamageManager implements Listener,PluginTied{

	public OMCTrueDamageManager() {registerSelf();}
	
	public class DamageRequest {

		public final double damage;
		public final Damageable damageable;
		public final Entity damager;

		public DamageRequest(Damageable damageable, double damage, Entity damager) {
			this.damage = damage;
			this.damageable = damageable;
			this.damager = damager;
		}
	}

	private final ArrayList<DamageRequest> damageBuffer = new ArrayList<>();

	public void trueDamage(Damageable damageable, double damage) {
		trueDamage(damageable, damage, null);
	}

	public void trueDamage(Damageable damageable, double damage, Entity source) {
		if(getRequest(damage, damageable, source) != null) return;
		DamageRequest request = new DamageRequest(damageable, damage, source);
		damageBuffer.add(request);
		if(source == null) damageable.damage(damage);
		else damageable.damage(damage, source);
		Bukkit.getScheduler().scheduleSyncDelayedTask(OMCCore.getJavaPlugin(), () -> {
			damageBuffer.remove(request);
		},1l);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Damageable dmg)) return;
		Entity damager = null;
		if(e instanceof EntityDamageByEntityEvent edbe) damager = edbe.getDamager();
		if(damageBuffer.isEmpty()) return;
		DamageRequest request = getRequest(e.getDamage(), dmg, damager);
		if(request == null) return;
		damageBuffer.remove(request);
		for(DamageModifier modifier : DamageModifier.values()) if(e.isApplicable(modifier)) e.setDamage(modifier, 0);
		e.setDamage(DamageModifier.BASE, request.damage);
	}

	public DamageRequest getRequest(double damage, Damageable damageable, Entity damager) {
		for(DamageRequest request : damageBuffer) {
			if(request.damage != damage) continue;
			if(request.damageable != damageable) continue;
			if(request.damager != damager) continue;
			return request;
		}
		return null;
	}
	
	@Override public void onPluginDisable() {}
	@Override public void onPluginEnable() { OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin()); }
	@Override public void onPluginReload() {}

}
