package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.stormer3428.voidOpal.Item.OMCItemManager;
import fr.stormer3428.voidOpal.Item.Types.OMCItem;
import fr.stormer3428.voidOpal.Power.OMCPowerManager;
import fr.stormer3428.voidOpal.Power.Types.OMCPower;
import fr.stormer3428.voidOpal.Tickeable.OMCTickeableManager;
import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.data.OMCNameable;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

public class OMCPremadeCommandFactory {

	public static OMCCommand[] getGiveCommands(OMCItemManager itemManager, String root) {
		return new OMCCommand[] {
			new OMCCommand(root + " give", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					return OMCLogger.error(sender, OMCLang.ERROR_MISSINGARG_NOPLAYER.toString());
				}
			}
			,
			new OMCCommand(root + " give %P%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					return OMCLogger.error(sender, OMCLang.ERROR_MISSINGARG_ITEM.toString());
				}
			}
			,
			new OMCCommand(root + " give %P% %ITEM%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					List<Entity> targets = new ArrayList<Entity>();
					try {
						targets.addAll(OMCCore.getJavaPlugin().getServer().selectEntities(sender, args.get(0)));
					}catch (NoSuchMethodError e) {
						targets.add(OMCCore.getJavaPlugin().getServer().getPlayer(args.get(0)));
					}
					if(targets.isEmpty()) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
					OMCItem item = itemManager.fromName(args.get(1));
					if(item == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOITEM.toString().replace("<%ITEM>", args.get(1))); 
					ItemStack it = item.createItemsStack(1);
					if(it == null) return OMCLogger.error(sender, OMCLang.ERROR_ITEM_GENERATION_FAILED.toString().replace("<%ITEM>", item.getRegistryName())); 
					for(Entity e : targets) if(e instanceof InventoryHolder i) {
						i.getInventory().addItem(it);
						OMCLogger.normal(sender, OMCLang.ITEM_GIVE.toString().replace("<%AMOUNT>", "1").replace("<%PLAYER>", e.getName()).replace("<%ITEM>", item.getRegistryName()));
					}
					return true;
				}
			}
			,
			new OMCCommand(root + " give %P% %ITEM% %V%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					List<Entity> targets = new ArrayList<Entity>();
					try {
						targets.addAll(OMCCore.getJavaPlugin().getServer().selectEntities(sender, args.get(0)));
					}catch (NoSuchMethodError e) {
						targets.add(OMCCore.getJavaPlugin().getServer().getPlayer(args.get(0)));
					}
					if(targets.isEmpty()) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
					OMCItem item = itemManager.fromName(args.get(1));
					if(item == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOITEM.toString().replace("<%ITEM>", args.get(1))); 
					try {
						int amount = Integer.parseInt(args.get(2));
						for(Entity e : targets) if(e instanceof InventoryHolder i) {
							i.getInventory().addItem(item.createItemsStack(amount));
							OMCLogger.normal(sender, OMCLang.ITEM_GIVE.toString().replace("<%AMOUNT>", amount + "").replace("<%PLAYER>", e.getName()).replace("<%ITEM>", item.getRegistryName()));
						}
						return true;
					}catch (NumberFormatException e) {
						return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_INTEGER.toString().replace("<%VALUE>", args.get(2)));
					}
				}
			}
		};
	}

	public static OMCCommand[] getCreditsCommands(String root, String name) {
		return new OMCCommand[] {
			new OMCCommand(root, false) {
				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					return OMCLogger.normal(sender, name + " by stormer3428");
				}
			}
		};
	}

	public static OMCCommand[] getReloadCommands(String root) {
		return new OMCCommand[] {
			new OMCCommand(root + " reload", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					OMCCore.getOMCCore().reload();
					return OMCLogger.normal(sender, OMCLang.RELOADED_CONFIG.toString());}
			}
		};
	}

	public static OMCCommand[] getPowerCommands(OMCPowerManager powerManager, String root) {
		return new OMCCommand[] {
			new OMCCommand(root + " power", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					return OMCLogger.normal(sender, OMCLang.ERROR_MISSINGARG_POWER.toString());
				}
			}
			,
			new OMCCommand(root + " power %POWER%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
					OMCPower power = powerManager.getPowerIgnoreCase(args.get(0));
					if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
					power.tryCast(null, p);
					return OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
				}
			}
			,
			new OMCCommand(root + " power %POWER% cast %P%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					OMCPower power = powerManager.getPowerIgnoreCase(args.get(0));
					if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));

					List<Entity> targets = new ArrayList<Entity>();
					try {
						OMCCore.getJavaPlugin().getServer().selectEntities(sender, args.get(1));
					}catch (NoSuchMethodError e) {
						targets.add(OMCCore.getJavaPlugin().getServer().getPlayer(args.get(1)));
					}
					if(targets.isEmpty()) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(1)));
					
					for(Entity e : targets) if(e instanceof Player p) {
						power.tryCast(null, p);
						OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
					}
					
					return true;
				}
			}
			,
			new OMCCommand(root + " power %POWER% putOnCooldown %P% %V%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					OMCPower power = powerManager.getPowerIgnoreCase(args.get(0));
					if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
					
					Player p = Bukkit.getPlayer(args.get(1));
					if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(1)));

					int cooldown;
					try {
						cooldown = Integer.parseInt(args.get(2));
					}catch (NumberFormatException e) {
						return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_INTEGER.toString().replace("<%VALUE>", args.get(2)));
					}
					
					power.putOnCooldown(p, cooldown);
					return true;
				}
			}
			,
			new OMCCommand(root + " power %POWER% clearCooldown %P%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					OMCPower power = powerManager.getPowerIgnoreCase(args.get(0));
					if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
					
					Player p = Bukkit.getPlayer(args.get(1));
					if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(1)));

					power.clearCooldowns();
					return true;
				}
			}
		};
	}


	public static OMCCommand[] getDebugCommands(String root) {
		return new OMCCommand[] {
			new OMCCommand(root + " debug%%%d resetplayer %P%", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> args) {
					Player p = Bukkit.getPlayer(args.get(0));
					if(p == null) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
					p.setGravity(true);
					try {
						Registry.ATTRIBUTE.forEach(attribute ->{
							AttributeInstance att = p.getAttribute(attribute);
							if(att == null) return;
							for(AttributeModifier modifier : att.getModifiers()) {
								att.removeModifier(modifier);
							}
						});
					}catch (Exception e) {
						OMCLogger.error(sender, e.toString());
						for(StackTraceElement element : e.getStackTrace()) {
							OMCLogger.error(sender, element.toString());
						}
					}
					return OMCLogger.normal(sender, "Reset player " + p.getName());
				}
			}
			,
			new OMCCommand(root + " debug%%%d viewstringblob%%%viewblob%%%vb", true) {

				@Override
				public boolean execute(CommandSender sender, ArrayList<String> vars) {
					if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
					final ItemStack it = p.getInventory().getItemInMainHand();
					YamlConfiguration config = new YamlConfiguration();
					config.set("stringBlob", it);
					return OMCLogger.normal(p, config.saveToString());
				}
			}
			,
			new OMCCommand(root + " debug%%%d cmd %V%", true) {

				@SuppressWarnings("deprecation")
				@Override
				public boolean execute(CommandSender sender, ArrayList<String> vars) {
					if(!(sender instanceof Player p)) return OMCLogger.error(sender, OMCLang.ERROR_PLAYERONLY.toString());
					final ItemStack it = p.getInventory().getItemInMainHand();
					try {
						final ItemMeta itm = it.getItemMeta();
						itm.setCustomModelData(Integer.parseInt(vars.get(0)));
						it.setItemMeta(itm);
						p.getWorld().playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, 1, 1);
					}catch (NumberFormatException e) {
						return false;
					}
					return true;
				}
			}
		};
	}

	public static OMCCommand[] printItemRegistry(OMCItemManager itemManager, String root) {
		return new OMCCommand[] { 
			new OMCCommand(root + " debug%%%d printItemRegistry", true) { @Override public boolean execute(CommandSender sender, ArrayList<String> vars) {
				OMCLogger.normal(sender, "<Printing Item Registry>\n");
				for (OMCNameable nameable : itemManager.getItems()) OMCLogger.normal(sender, nameable.getRegistryName());
				return OMCLogger.normal(sender, "\n");
			}} 
		};
	}

	public static OMCCommand[] printPowerRegistry(OMCPowerManager powerManager, String root) {
		return new OMCCommand[] { 
			new OMCCommand(root + " debug%%%d printPowerRegistry", true) { @Override public boolean execute(CommandSender sender, ArrayList<String> vars) {
				OMCLogger.normal(sender, "<Printing Power Registry>\n");
				for (OMCNameable nameable : powerManager.getPowers()) OMCLogger.normal(sender, nameable.getRegistryName());
				return OMCLogger.normal(sender, "\n");
			}} ,
			new OMCCommand(root + " debug%%%d printCooldowns", true) { @Override public boolean execute(CommandSender sender, ArrayList<String> vars) {
				OMCLogger.normal(sender, "<Printing cooldowns Registry>\n");
				for (OMCPower power : powerManager.getPowers()) {
					for(Entry<UUID, Integer> entry : power.getCooldownMap().entrySet()) {
						OMCLogger.normal(sender, power.getRegistryName() + " " + entry.getKey() + " " + entry.getValue());
					}
				}
				return OMCLogger.normal(sender, "\n");
			}}
		};
	}

	public static OMCCommand[] printTickeableRegistry(OMCTickeableManager tickeableManager, String root) {
		return new OMCCommand[] { 
			new OMCCommand(root + " debug%%%d printTickeableRegistry", true) { @Override public boolean execute(CommandSender sender, ArrayList<String> vars) {
				OMCLogger.normal(sender, "<Printing Tickeable Registry>\n");
				for (OMCNameable nameable : tickeableManager.getRegisteredTickables()) OMCLogger.normal(sender, nameable.getRegistryName());
				return OMCLogger.normal(sender, "\n");
			}}
		};
	}

	

	public static final String serializeItemstack(ItemStack it) {
		YamlConfiguration config = new YamlConfiguration();
		config.set("stringBlob", it);
		return config.saveToString();
	}




}
