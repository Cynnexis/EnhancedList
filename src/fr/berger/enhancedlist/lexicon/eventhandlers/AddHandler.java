package fr.berger.enhancedlist.lexicon.eventhandlers;

public interface AddHandler<T> extends EventHandler<T> {
	
	void onElementAdded(int index, T element);
}
