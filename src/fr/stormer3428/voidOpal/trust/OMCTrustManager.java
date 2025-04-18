package fr.stormer3428.voidOpal.trust;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public class OMCTrustManager implements PluginTied{

	private final ArrayList<OMCTrustGroup> trustGroups = new ArrayList<>();
	
	private final String configFileName;
	private File configurationFile;
	private YamlConfiguration config;
	
	public OMCTrustManager(String configFileName) {
		this.configFileName = configFileName;
		OMCCore.getOMCCore().registerPluginTied(this);
	}

	@Override public void onPluginEnable() {onPluginReload();}
	@Override public void onPluginDisable() {}
	@Override public void onPluginReload() {
		configurationFile = new File(OMCCore.getJavaPlugin().getDataFolder(), configFileName);
		config = YamlConfiguration.loadConfiguration(configurationFile);
		trustGroups.clear();
		for(String uuidString : config.getKeys(false)) {
			UUID uuid = UUID.fromString(uuidString);
			OMCTrustGroup trustGroup = new OMCTrustGroup(uuid, this);
			trustGroup.addAll(config.getStringList(uuidString).parallelStream().map(u->UUID.fromString(u)).toList());
			trustGroups.add(trustGroup);
		}
	}

	public boolean trusts(Player truster, Player trustee) { return trusts(truster.getUniqueId(), trustee.getUniqueId()); }

	public boolean trusts(UUID truster, UUID trustee) {
		if (truster.equals(trustee)) return true;
		OMCTrustGroup group = getTrustgroup(truster);
		return group.trusts(trustee);
	}

	public OMCTrustGroup getTrustgroup(Player truster) { return getTrustgroup(truster.getUniqueId()); }

	public OMCTrustGroup getTrustgroup(UUID truster) {
		return trustGroups.parallelStream().filter(g -> g.owner.equals(truster)).findFirst().orElseGet(() -> {
			OMCTrustGroup group = new OMCTrustGroup(truster, this);
			trustGroups.add(group);
			return group;
		});
	}

	public void saveGroup(OMCTrustGroup trustGroup) {
		config.set(trustGroup.owner.toString(), trustGroup.parallelStream().map(u -> u.toString()).toList());
		try {
			config.save(configurationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}














