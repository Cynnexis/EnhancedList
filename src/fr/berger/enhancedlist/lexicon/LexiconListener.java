package fr.berger.enhancedlist.lexicon;

import java.io.Serializable;

public interface LexiconListener<T> extends Serializable {
	
	void onElementAdded(T element);
	void onElementAdded(int index, T element);
	void onElementGotten(T element);
	void onElementSet(int index, T element);
	void onElementRemoved(Object element);
	void onElementRemoved(int index, T element);
	void onLexiconCleared(Lexicon<T> lexicon);
}
