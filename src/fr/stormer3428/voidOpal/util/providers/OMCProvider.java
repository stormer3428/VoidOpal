package fr.stormer3428.voidOpal.util.providers;

@FunctionalInterface
public interface OMCProvider <T>{

	public T getData(Object ... context);
	
	public static <Y> OMCProvider<Y> of(Y data) {return ctx->data;}
}
