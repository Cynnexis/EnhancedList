package fr.berger.enhancedlist.lexicon.eventhandlers;

public interface GetHandler<T> extends EventHandler<T> {
	
	void onElementGotten(int index, T element);
}
