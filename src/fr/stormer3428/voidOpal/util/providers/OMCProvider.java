package fr.stormer3428.voidOpal.util.providers;

@FunctionalInterface
public interface OMCProvider <T>{

	public T getData();
	
	public static <Y> OMCProvider<Y> of(Y data) {return ()->data;}
}
