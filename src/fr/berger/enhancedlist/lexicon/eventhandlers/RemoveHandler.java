package fr.berger.enhancedlist.lexicon.eventhandlers;

public interface RemoveHandler<T> extends EventHandler<T> {
	
	void onElementRemoved(int index, T element);
}
