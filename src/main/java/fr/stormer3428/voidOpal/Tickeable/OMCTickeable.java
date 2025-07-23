package fr.stormer3428.voidOpal.Tickeable;

import fr.stormer3428.voidOpal.data.OMCNameable;

/**
 * Represents a class that requires a function to run every tick
 */
public interface OMCTickeable extends OMCNameable{
	
	public abstract void onTick(int ticker);
	
}
