package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;
//package fr.stormer3428.obsidianMC.Util.DisplayGeometry;
//
//import org.bukkit.Location;
//import org.bukkit.entity.Display.Brightness;
//import org.bukkit.entity.ItemDisplay;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Transformation;
//
//import fr.stormer3428.obsidianMC.OMCPlugin;
//import fr.stormer3428.obsidianMC.Item.VFXItem;
//
//public class VFXDrawable extends ItemDisplayWrapper{
//
////	private final VFXItem vfx;
////	private final int animationInterval;
//	
//	public VFXDrawable(Location location, VFXItem vfxItem, int animationInterval) {
//		this(location, vfxItem, animationInterval, 1.0);
//	}
//	
//	public VFXDrawable(Location location, VFXItem vfxItem, int animationInterval, double scale) {
//		this(createItemDisplay(location, vfxItem, scale), vfxItem, animationInterval);
//	}
//	
//	private static ItemDisplay createItemDisplay(Location location, VFXItem vfxItem, double scale) {
//		ItemDisplay display = location.getWorld().spawn(location, ItemDisplay.class);
//		display.setItemStack(vfxItem.createItemsStack(1));
//		display.setInterpolationDuration(1);
//		display.setBrightness(new Brightness(15,15));
//		Transformation t = display.getTransformation();
//		t.getScale().set(scale);
//		display.setTransformation(t);
//		return display;
//	}
//
//	public VFXDrawable(ItemDisplay itemDisplay, VFXItem vfx, int animationInterval) {
//		super(itemDisplay);
////		this.vfx = vfx;
////		this.animationInterval = animationInterval;
//		itemDisplay.getItemStack().setType(vfx.getMaterial());
//		new BukkitRunnable() {
//			int i = 0; 
//			@Override	
//			public void run() {
//				if(itemDisplay.isDead()) {
//					cancel();
//					return;
//				}
//				ItemStack it = itemDisplay.getItemStack();
//				ItemMeta itm = it.getItemMeta();
//				int CMD = vfx.getCMD(i);
//				itm.setCustomModelData(CMD);		
//				it.setItemMeta(itm);
//				itemDisplay.setItemStack(it);
//				if(++i>=vfx.getCMDCount() && !vfx.isLooping()) cancel();
//			}
//			@Override
//			public synchronized void cancel() throws IllegalStateException {
//				super.cancel();
//				kill();
//			}
//		}.runTaskTimer(OMCPlugin.i, 0, animationInterval);
//		
//	}
//}
//
//
//
//
