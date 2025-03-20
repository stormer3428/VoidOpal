package fr.stormer3428.voidOpal.trust;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

public class OMCTrustGroup extends HashSet<UUID>{

	private static final long serialVersionUID = 5660925725346552354L;

	public final UUID owner;
	public final OMCTrustManager manager;
	
	public OMCTrustGroup(UUID owner, OMCTrustManager manager) {
		this.owner = owner;
		this.manager = manager;
	}

	public boolean trusts(Player trustee) {
		return trusts(trustee.getUniqueId());
	}

	public boolean trusts(UUID trustee) {
		if(owner.equals(trustee)) return true;
		return contains(trustee);
	}
	
	@Override
	public boolean add(UUID e) {
		if(!super.add(e)) return false;
		manager.saveGroup(this);
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		if(!super.remove(o)) return false;
		manager.saveGroup(this);
		return true;
	}
	
}
