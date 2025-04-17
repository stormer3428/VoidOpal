package fr.stormer3428.voidOpal.Listener;

import org.bukkit.event.Listener;

import fr.stormer3428.voidOpal.data.OMCNameable;

public abstract class OMCNamedListener implements Listener, OMCNameable{
	
	private final String registryName;
	
	@Override public String getRegistryName() { return registryName; }
	
	public OMCNamedListener(String registryName) {this.registryName = registryName;}
	
}
