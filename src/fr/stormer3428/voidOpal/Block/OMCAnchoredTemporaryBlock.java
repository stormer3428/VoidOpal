package fr.stormer3428.voidOpal.Block;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.stormer3428.voidOpal.plugin.OMCPlugin;

public class OMCAnchoredTemporaryBlock {

	public static ArrayList<OMCAnchoredTemporaryBlock> all = new ArrayList<>();

	private OMCAnchoredTemporaryBlock instance = this;
	private Material material;
	private int remainingTicks;
	private Vector relativeLocation;
	private Entity anchor;
	private Location oldLocation;

	public OMCAnchoredTemporaryBlock(Material material, int visibleTime, Location loc, Entity entity) {

		this.oldLocation = loc.getBlock().getLocation();
		this.anchor = entity;
		this.material = material;
		this.remainingTicks = visibleTime;
		this.relativeLocation = loc.subtract(entity.getLocation()).toVector();
		all.add(this);
		startLoop();

	}

	private void startLoop() {
		BlockData BData = this.material.createBlockData();
		this.oldLocation = this.anchor.getLocation().add(this.relativeLocation);
		for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(this.anchor.getLocation().add(this.relativeLocation), BData);
		new BukkitRunnable() {

			@Override
			public void run() {
				if(OMCAnchoredTemporaryBlock.this.remainingTicks <= 0) {
					for(Player p : Bukkit.getOnlinePlayers()) p.sendBlockChange(OMCAnchoredTemporaryBlock.this.oldLocation, OMCAnchoredTemporaryBlock.this.oldLocation.getBlock().getBlockData());
					all.remove(OMCAnchoredTemporaryBlock.this.instance);
					cancel();
				}else {
					if(OMCAnchoredTemporaryBlock.this.anchor.getLocation().add(OMCAnchoredTemporaryBlock.this.relativeLocation).getBlock() != OMCAnchoredTemporaryBlock.this.oldLocation) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.sendBlockChange(OMCAnchoredTemporaryBlock.this.oldLocation, OMCAnchoredTemporaryBlock.this.oldLocation.getBlock().getBlockData());
							p.sendBlockChange(OMCAnchoredTemporaryBlock.this.anchor.getLocation().add(OMCAnchoredTemporaryBlock.this.relativeLocation), BData);
						}
					}
				}
				OMCAnchoredTemporaryBlock.this.oldLocation = OMCAnchoredTemporaryBlock.this.anchor.getLocation().add(OMCAnchoredTemporaryBlock.this.relativeLocation).getBlock().getLocation();
				OMCAnchoredTemporaryBlock.this.remainingTicks --;
			}
		}.runTaskTimer(OMCPlugin.getJavaPlugin(), 0, 1);
	}

	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getRemainingTicks() {
		return this.remainingTicks;
	}

	public void setRemainingTicks(int remainingTicks) {
		this.remainingTicks = remainingTicks;
	}

}
