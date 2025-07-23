package fr.stormer3428.voidOpal.data.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import fr.stormer3428.voidOpal.plugin.annotations.OMCKeep;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@OMCKeep
public @interface AutoConfig {
	public String config() default "config.yml";
}

