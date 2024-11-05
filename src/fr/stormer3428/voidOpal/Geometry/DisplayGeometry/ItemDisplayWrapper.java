package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

public class ItemDisplayWrapper extends DisplayWrapper<ItemDisplay>{

	public ItemDisplayWrapper(Location origin) {this(origin, new Vector());}
	public ItemDisplayWrapper(Location origin, Vector location) {this(origin, location, origin.getDirection());}
	public ItemDisplayWrapper(Location origin, Vector location, Vector direction) {
		super(ItemDisplay.class, origin, location, direction);
		setBillboard(Billboard.CENTER);
		setShadowRadius(0);
	}

	private ItemDisplayTransform itemDisplayTransform;
	private ItemStack itemStack;
	
	@Override
	public boolean create() {
		if(!super.create()) return false;
		ItemDisplay display = getDisplay();
		display.setItemDisplayTransform(itemDisplayTransform);
		display.setItemStack(itemStack);
		return true;
	}

	public ItemDisplayWrapper setItemDisplayTransform(ItemDisplayTransform itemDisplayTransform) {this.itemDisplayTransform = itemDisplayTransform; return this;}
	public ItemDisplayWrapper setItemStack(ItemStack itemStack) {this.itemStack = itemStack; return this;}

	@Override public ItemDisplayWrapper setScale(double scale) {this.scale = scale; updatePosition(); return this;}
	@Override public ItemDisplayWrapper setWorld(World world) {super.setWorld(world); return this;}
	@Override public ItemDisplayWrapper setOrigin(Vector origin) {super.setOrigin(origin); return this;}
	@Override public ItemDisplayWrapper setOrigin(Location origin) {super.setOrigin(origin); return this;}
	@Override public ItemDisplayWrapper setGlowing(boolean glowing) {super.setGlowing(glowing); return this;}
	@Override public ItemDisplayWrapper setLocation(Vector loc) {super.setLocation(loc); return this;}
	@Override public ItemDisplayWrapper setBillboard(Billboard billboard) {super.setBillboard(billboard); return this;}
	@Override public ItemDisplayWrapper setDirection(Vector direction) {super.setDirection(direction); return this;}
	@Override public ItemDisplayWrapper setViewRange(float viewRange) {super.setViewRange(viewRange); return this;}
	@Override public ItemDisplayWrapper setBrightness(Brightness brightness) {super.setBrightness(brightness); return this;}
	@Override public ItemDisplayWrapper setShadowRadius(float shadowRadius) {super.setShadowRadius(shadowRadius); return this;}
	@Override public ItemDisplayWrapper setTransformation(Transformation transformation) {super.setTransformation(transformation); return this;}
	@Override public ItemDisplayWrapper setGlowColorOverride(Color glowColorOverride) {super.setGlowColorOverride(glowColorOverride); return this;}
	@Override public ItemDisplayWrapper setInterpolationDelay(int interpolationDelay) {super.setInterpolationDelay(interpolationDelay); return this;}
	@Override public ItemDisplayWrapper setTransformationMatrix(Matrix4f transformationMatrix) {super.setTransformationMatrix(transformationMatrix); return this;}

	public ItemDisplayTransform getItemDisplayTransform() {return itemDisplayTransform;}
	public ItemStack getItemStack() {return itemStack;}
}

