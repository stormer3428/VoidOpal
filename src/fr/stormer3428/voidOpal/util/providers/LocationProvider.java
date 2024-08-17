package fr.stormer3428.voidOpal.util.providers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class LocationProvider implements OMCProvider<Location>{

	private Location location = null;
	private Entity entity = null;
	
	public LocationProvider(Location loc) {
		this.location = loc;
	}
	
	public LocationProvider(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Location getData() {
		return location == null ? entity.getLocation() : location.clone();
	}

	public void setData(Entity entity) {
		this.entity = entity;
		this.location = null;
	}

	@Override
	public void setData(Location location) {
		this.entity = null;
		this.location = location;
	}
	
}
