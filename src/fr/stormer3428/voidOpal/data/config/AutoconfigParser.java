package fr.stormer3428.voidOpal.data.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.DoubleConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.IntConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.StringConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCPluginImpl;

public class AutoconfigParser {

	final ArrayList<Class<?>> annotatedClasses = new ArrayList<>();

	public void updateValues() {
		OMCLogger.systemNormal("Updating values in classes");
		for(Class<?> clazz : annotatedClasses) {
			OMCLogger.systemNormal("Updating values of class " + clazz.getName());
			AutoConfig autoConfig = clazz.getDeclaredAnnotation(AutoConfig.class);
			if(autoConfig == null) {
				OMCLogger.systemError("Error, missing @AutoConfig annotation");
				continue;
			}

			String configName = autoConfig.config();

			File file = new File(OMCPluginImpl.getJavaPlugin().getDataFolder(), configName);
			if(!file.exists()) {
				OMCLogger.systemNormal("could not find file \"" + configName + "\", creating...");
				file.getParentFile().mkdirs();
				createConfigFile(configName);
				OMCLogger.systemNormal("Success!");
			}
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			for(Field field : clazz.getDeclaredFields()) {

				field.setAccessible(true);
				StringConfigValue stringConfigValue = field.getAnnotation(StringConfigValue.class);
				IntConfigValue intConfigValue = field.getAnnotation(IntConfigValue.class);
				DoubleConfigValue doubleConfigValue = field.getAnnotation(DoubleConfigValue.class);
				BooleanConfigValue booleanConfigValue = field.getAnnotation(BooleanConfigValue.class);
				if(stringConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					String defaultValue = stringConfigValue.defaultValue();
					String path = stringConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					String value = config.getString(path);
					config.set(path, value);
					try {
						field.set(null, config.get(path));
						OMCLogger.debug("Successfully updated string value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected String)");
						e.printStackTrace();
						continue;
					}
				}else if(intConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					int defaultValue = intConfigValue.defaultValue();
					String path = intConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					int value = config.getInt(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated integer value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Integer)");
						e.printStackTrace();
						continue;
					}
				}else if(doubleConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					double defaultValue = doubleConfigValue.defaultValue();
					String path = doubleConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					double value = config.getDouble(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated double value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Double)");
						e.printStackTrace();
						continue;
					}
				}else if(booleanConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					boolean defaultValue = booleanConfigValue.defaultValue();
					String path = booleanConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					boolean value = config.getBoolean(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated boolean value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected Boolean)");
						e.printStackTrace();
						continue;
					}
				}else {
					OMCLogger.debug("No annotation");
				}
			}
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public void registerAutoConfigClass(Class<?> clazz) {
		annotatedClasses.add(clazz);
	}

	public void createConfigFile(String resourcePath) {
		File dataFolder = OMCPluginImpl.getJavaPlugin().getDataFolder();

		InputStream in = OMCPluginImpl.getJavaPlugin().getResource(resourcePath);
		if (in == null) {
			try {
				new File(dataFolder, resourcePath).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		File outFile = new File(dataFolder, resourcePath);
		if(!outFile.exists())
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		if(in != null) try {
			OutputStream out = new FileOutputStream(outFile);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (IOException ex) {
			OMCLogger.systemError("Could not save " + outFile.getName() + " to " + outFile);
		}
	}


}
