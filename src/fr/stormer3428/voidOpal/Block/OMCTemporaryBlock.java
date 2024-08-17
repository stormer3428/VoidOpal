package fr.stormer3428.voidOpal.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.stormer3428.voidOpal.plugin.OMCPluginImpl;

public class OMCTemporaryBlock {

	private static BukkitRunnable blockChangeRunnable;
//	private static final Map<UUID, List<BlockState>> changes = new HashMap<>();
	private static final List<Change> changes = new ArrayList<OMCTemporaryBlock.Change>();

	private static final class Change{
		final UUID uuid;
		final BlockState blockState;
		
		public Change(UUID uuid, BlockState blockState) {
			this.uuid = uuid;
			this.blockState = blockState;
		}
	}
	
	private static void sendBlockChange(Player p, Location location, BlockData blockData) {
		if(blockChangeRunnable == null) {
			blockChangeRunnable = new BukkitRunnable() {
				@Override
				public void run() {
					Map<UUID, ArrayList<BlockState>> map = new HashMap<>();
					for(Change change : changes) map.put(change.uuid, new ArrayList<BlockState>());
					for(Change change : changes) map.get(change.uuid).add(change.blockState);
					for(Entry<UUID, ArrayList<BlockState>> entry : map.entrySet()) {
//					for(Entry<UUID, List<BlockState>> entry : changes.entrySet()) {
						Player p = Bukkit.getPlayer(entry.getKey());
						if(p == null) continue;
						p.sendBlockChanges(entry.getValue());
						entry.getValue().clear();
					}
					changes.clear();
				}
			};
			blockChangeRunnable.runTaskTimer(OMCPluginImpl.getJavaPlugin(), 0, 1);
		}
		BlockState blockState = blockData.createBlockState().copy(location);
//		List<BlockState> list = changes.get(p.getUniqueId());
//		if(list == null) {
//			list = new ArrayList<>();
//			changes.put(p.getUniqueId(), list);
//		}
		changes.add(new Change(p.getUniqueId(), blockState));
	}

	public static ArrayList<OMCTemporaryBlock> all = new ArrayList<>();

	private OMCTemporaryBlock instance = this;
	private BlockData blockData;
	private int remainingTicks;
	private Location location;
	private List<UUID> playerWhitelist;

	private OMCTemporaryBlock(BlockData blockData, int visibleTime, Location loc, UUID ... playerWhitelist) {
		this.location = loc;
		this.remainingTicks = visibleTime;
		this.playerWhitelist = Arrays.asList(playerWhitelist);
		setBlockData(blockData);
		all.add(this);
		startLoop();
	}

	public static OMCTemporaryBlock createNew(Material material, int visibleTime, Location loc, UUID ...  playerWhitelist) {
		return createNew(material.createBlockData(), visibleTime, loc, playerWhitelist);
	}

	public static OMCTemporaryBlock createNew(BlockData blockData, int visibleTime, Location loc, UUID ... playerWhitelist) {
		for(OMCTemporaryBlock block : all) if(block.location.equals(loc)) {
			block.setRemainingTicks(visibleTime);
			block.setBlockData(blockData);
			return block;
		}
		return new OMCTemporaryBlock(blockData, visibleTime, loc, playerWhitelist);
	}

	private void startLoop() {
		new BukkitRunnable() {
			@Override public void run() {
				if(OMCTemporaryBlock.this.remainingTicks <= 0) {
					for(Player p : Bukkit.getOnlinePlayers()) if(playerWhitelist.isEmpty() || playerWhitelist.contains(p.getUniqueId())) sendBlockChange(p, OMCTemporaryBlock.this.location, OMCTemporaryBlock.this.location.getBlock().getBlockData());
					all.remove(OMCTemporaryBlock.this.instance);
					cancel();
				}
				OMCTemporaryBlock.this.remainingTicks --;
			}
		}.runTaskTimer(OMCPluginImpl.getJavaPlugin(), 0, 1);
	}

	public int getRemainingTicks() {
		return this.remainingTicks;
	}

	public void setRemainingTicks(int remainingTicks) {
		this.remainingTicks = remainingTicks;
	}

	public BlockData getBlockData() {
		return blockData;
	}

	public void setBlockData(BlockData blockData) {
		this.blockData = blockData;
		for(Player p : Bukkit.getOnlinePlayers()) if(playerWhitelist.isEmpty() || playerWhitelist.contains(p.getUniqueId())) sendBlockChange(p, this.location, blockData);
	}

	public List<UUID> getPlayerWhitelist() {
		return playerWhitelist;
	}

	public void setPlayerWhitelist(UUID[] playerWhitelist) {
		this.playerWhitelist = Arrays.asList(playerWhitelist);
	}

	public void setPlayerWhitelist(List<UUID> playerWhitelist) {
		this.playerWhitelist = playerWhitelist;
	}

	public static boolean contains(Block b) {
		for(OMCTemporaryBlock a : all) if(a.location.equals(b.getLocation())) return true;
		return false;
	}

}
