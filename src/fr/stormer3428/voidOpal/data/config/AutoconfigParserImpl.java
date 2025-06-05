package fr.stormer3428.voidOpal.data.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.reflections.Reflections;

import fr.stormer3428.voidOpal.data.config.annotations.AutoConfig;
import fr.stormer3428.voidOpal.data.config.annotations.BooleanConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.DoubleConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.FloatConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.IntConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.StringConfigValue;
import fr.stormer3428.voidOpal.data.config.annotations.StringListConfigValue;
import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

public final class AutoconfigParserImpl implements AutoconfigParser{

	@Override
	public void updateValues() {
		for(Class<?> clazz : getClassesAnnotatedWith(AutoConfig.class)) {

			File file = getConfigFile(clazz);
			if(file == null) {
				continue;
			}

			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

			for(Field field : clazz.getDeclaredFields()) {

				field.setAccessible(true);
				StringConfigValue stringConfigValue = field.getAnnotation(StringConfigValue.class);
				IntConfigValue intConfigValue = field.getAnnotation(IntConfigValue.class);
				DoubleConfigValue doubleConfigValue = field.getAnnotation(DoubleConfigValue.class);
				FloatConfigValue floatConfigValue = field.getAnnotation(FloatConfigValue.class);
				BooleanConfigValue booleanConfigValue = field.getAnnotation(BooleanConfigValue.class);
				StringListConfigValue stringListConfigValue= field.getAnnotation(StringListConfigValue.class);
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
				}else if(floatConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					float defaultValue = floatConfigValue.defaultValue();
					String path = floatConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					float value = (float) config.getDouble(path);
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
				}else if(stringListConfigValue != null) {
					OMCLogger.debug("Updating field " + field.getName());
					List<String> defaultValue = Arrays.asList(stringListConfigValue.defaultValue());
					String path = stringListConfigValue.path();
					if(!config.contains(path)) config.set(path, defaultValue);
					List<String> value = config.getStringList(path);
					config.set(path, value);
					try {
						field.set(null, value);
						OMCLogger.debug("Successfully updated String[] value!");
					} catch (IllegalArgumentException | IllegalAccessException e) {
						OMCLogger.systemError("Error, annotated field " + field.getName() + " in class " + clazz.getName() + " is either not static or of wrong type (expected String[])");
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
	
	@Override
	public void createConfigFile(String resourcePath) {
		File dataFolder = OMCCore.getJavaPlugin().getDataFolder();

		InputStream in = OMCCore.getJavaPlugin().getResource(resourcePath);
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
	
	@Override
	public File getConfigFile(String configName) {
		File file = new File(OMCCore.getJavaPlugin().getDataFolder(), configName);
		if(!file.exists()) {
			OMCLogger.systemNormal("could not find file \"" + configName + "\", creating...");
			file.getParentFile().mkdirs();
			createConfigFile(configName);
			OMCLogger.systemNormal("Success!");
		}
		return file;
	}

	@Override
	public File getConfigFile(Class<?> clazz) {
		AutoConfig autoConfig = clazz.getDeclaredAnnotation(AutoConfig.class);
		if(autoConfig == null) {
			OMCLogger.systemError("Error, missing @AutoConfig annotation");
			return null;
		}

		String configName = autoConfig.config();

		File file = getConfigFile(configName);
		return file;
	}

	@Override
	public void _write(Class<?> clazz, String fieldName, Object fieldValue) {
		File file = getConfigFile(clazz);
		if(file == null) {
			OMCLogger.systemError("Error, tried to save a non annotated value un a class missing @AutoConfig annotation");
			return;
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set(fieldName, fieldValue);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotationType) {
		Reflections reflections = new Reflections("org.package.foo");

		Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
		Set<Class<?>> annotatedClases = new HashSet<>();

		for (Class<?> clazz : allClasses) {
			Annotation[] annotations = clazz.getAnnotations();

			for (Annotation annotation : annotations) if (annotationType.isInstance(annotation)){
				annotatedClases.add(clazz);
				break;
			}
		}
		return annotatedClases;
	}
}