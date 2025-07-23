package fr.stormer3428.voidOpal.data.config;

import java.io.File;

import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;

@OMCKeep
public interface AutoconfigParser {

	@OMCKeep public static AutoconfigParser instance = null;

	@OMCKeep public static void write(Class<?> clazz, String fieldName, Object fieldValue) {instance._write(clazz, fieldName, fieldValue);}
	@OMCKeep public void registerAutoConfigClass(Class<?> clazz);
	@OMCKeep public void createConfigFile(String resourcePath);
	@OMCKeep public File getConfigFile(String configName);
	@OMCKeep public File getConfigFile(Class<?> clazz);
	@OMCKeep public void updateValues();
	@OMCKeep public void _write(Class<?> clazz, String fieldName, Object fieldValue);

}




