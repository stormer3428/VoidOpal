package fr.stormer3428.voidOpal.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class LocationProvider {

	private Location location = null;
	private Entity entity = null;
	
	public LocationProvider(Location loc) {
		this.location = loc;
	}
	
	public LocationProvider(Entity entity) {
		this.entity = entity;
	}

	public Location getLocation() {
		return location == null ? entity.getLocation() : location.clone();
	}

	public void setLocation(Entity entity) {
		this.entity = entity;
		this.location = null;
	}

	public void setLocation(Location location) {
		this.entity = null;
		this.location = location;
	}
	
}
