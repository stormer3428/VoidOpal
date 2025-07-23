package fr.stormer3428.voidOpal.Tickeable.types;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.Listener.OMCNamedListener;
import fr.stormer3428.voidOpal.Tickeable.OMCTickeable;

public abstract class OMCPassivePower extends OMCNamedListener implements OMCTickeable {

	public OMCPassivePower(String registryName) { super(registryName); }
	
	public abstract void onPassiveTick(Player p, int ticker);
	public abstract void onPassiveStop(Player p);
	public abstract void onPassiveStart(Player p);
	public abstract boolean matches(Player p);

	protected ArrayList<UUID> HOLDING = new ArrayList<>();

	@Override public void onTick(int ticker) {
		ArrayList<UUID> NEW_HOLDING = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			boolean holding = matches(p);
			boolean wasHolding = HOLDING.contains(p.getUniqueId());
			if (holding) {
				if (!wasHolding) onPassiveStart(p);
				onPassiveTick(p, ticker);
				NEW_HOLDING.add(p.getUniqueId());
			} else if (wasHolding) onPassiveStop(p);
		}
		HOLDING = NEW_HOLDING;
	}

}
