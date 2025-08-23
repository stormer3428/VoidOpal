package fr.stormer3428.voidOpal.plugin;

import fr.stormer3428.voidOpal.plugin.cores._0_1;
import fr.stormer3428.voidOpal.plugin.cores._0_1_nosu;

public enum _VersionEnum {
	V0_1(_0_1.class),
	V0_1_nosu(_0_1_nosu.class)
	
	;

	public final Class<? extends OMCCore> clazz;
	private _VersionEnum(Class<? extends OMCCore> clazz) {
		this.clazz = clazz;
	}
}
