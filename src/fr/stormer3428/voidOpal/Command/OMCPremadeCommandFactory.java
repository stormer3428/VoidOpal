package fr.stormer3428.voidOpal.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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
import fr.stormer3428.voidOpal.data.OMCLang;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCPlugin;

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
						List<Entity> targets = OMCPlugin.i.getServer().selectEntities(sender, args.get(0));
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
						List<Entity> targets = OMCPlugin.i.getServer().selectEntities(sender, args.get(0));
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
						OMCPlugin.getOMCPlugin().reload();
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
						OMCPower power = powerManager.fromName(args.get(0));
						if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
						power.tryCast(null, p);
						return OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
					}
				}
				,
				new OMCCommand(root + " power %POWER% %P%", true) {

					@Override
					public boolean execute(CommandSender sender, ArrayList<String> args) {
						List<Entity> targets = OMCPlugin.i.getServer().selectEntities(sender, args.get(0));
						if(targets.isEmpty()) return OMCLogger.error(sender, OMCLang.ERROR_INVALIDARG_NOPLAYER.toString().replace("<%PLAYER>", args.get(0)));
						OMCPower power = powerManager.fromName(args.get(0));
						if(power == null) return OMCLogger.error(sender, OMCLang.ERROR_GENERIC_NOPOWER.toString().replace("<%POWER>", args.get(0)));
						for(Entity e : targets) if(e instanceof Player p) {
							power.tryCast(null, p);
							OMCLogger.normal(sender, OMCLang.POWER_MANUALCAST.toString().replace("<%POWER>", power.getRegistryName()));
						}
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
							for(Attribute attribute : Attribute.values()) {
								AttributeInstance att = p.getAttribute(attribute);
								if(att == null) continue;
								for(AttributeModifier modifier : att.getModifiers()) {
									att.removeModifier(modifier);
								}
							}
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


	public static final String serializeItemstack(ItemStack it) {
		YamlConfiguration config = new YamlConfiguration();
		config.set("stringBlob", it);
		return config.saveToString();
	}




}
