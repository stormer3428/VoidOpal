package fr.stormer3428.voidOpal.data.config;

import java.io.File;

public interface AutoconfigParser {

	public void registerAutoConfigClass(Class<?> clazz);
	public void createConfigFile(String resourcePath);
	public File getConfigFile(String configName);
	public File getConfigFile(Class<?> clazz);
	public void updateValues();
	public void write(Class<?> clazz, String fieldName, Object fieldValue);

}




