package fr.stormer3428.voidOpal.plugin;

import org.objectweb.asm.commons.Remapper;

public class PackageRemapper extends Remapper {
	
    private final String oldPackageName;
    private final String newPackageName;

    public PackageRemapper(String oldPackageName, String newPackageName) {
        this.oldPackageName = oldPackageName.replace('.', '/');
        this.newPackageName = newPackageName.replace('.', '/');
    }

    @Override
    public String map(String internalName) {
        if (internalName.startsWith(oldPackageName)) {
//        	System.out.println("\nRemapped");
//        	System.out.println(oldPackageName);
//        	System.out.println(newPackageName);
            return internalName.replace(oldPackageName, newPackageName);
        }
        return internalName;
    }
}
