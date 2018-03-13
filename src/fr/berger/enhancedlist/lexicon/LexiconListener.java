package fr.berger.enhancedlist.lexicon;

import java.io.Serializable;

@Deprecated
public interface LexiconListener<T> extends Serializable {
	
	void onLexiconCleared(Lexicon<T> lexicon);
}
