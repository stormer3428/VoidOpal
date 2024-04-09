package fr.stormer3428.voidOpal.Block;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public class OMCExpectedBlock {

	private final Location location;
	private final Material displayMaterial;
	private final ArrayList<Material> acceptedMaterials = new ArrayList<>();
	
	public OMCExpectedBlock(Location location, Material displayMaterial, Material ... acceptedMaterials) {
		this.location = location.add(0.5,0.5,0.5);
		this.displayMaterial = displayMaterial;
		this.acceptedMaterials.addAll(Arrays.asList(acceptedMaterials));
	}
	
	public OMCExpectedBlock(Location location, Material displayMaterial, ArrayList<Material> acceptedMaterials) {
		this.location = location.add(0.5,0.5,0.5);
		this.displayMaterial = displayMaterial;
		this.acceptedMaterials.addAll(acceptedMaterials);
	}

	public boolean isValid() {
		if(acceptedMaterials.contains(location.getBlock().getType())) return true;
		if(displayMaterial != null) location.getWorld().spawnParticle(Particle.BLOCK_MARKER, location, 1, 0.0, 0.0, 0.0, 0.0, displayMaterial.createBlockData());
		OMCTemporaryBlock.createNew(displayMaterial == null ? Material.RED_STAINED_GLASS : Material.BARRIER, 20 * 4, location).getRemainingTicks();
		return false;
	}

	public Location getLocation() {
		return location;
	}
	
}
