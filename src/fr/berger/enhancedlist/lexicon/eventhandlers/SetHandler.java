package fr.berger.enhancedlist.lexicon.eventhandlers;

public interface SetHandler<T> extends EventHandler<T> {
	
	void onElementSet(int index, T element);
}
