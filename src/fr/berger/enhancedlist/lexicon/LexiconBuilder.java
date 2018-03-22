package fr.berger.enhancedlist.lexicon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class LexiconBuilder<T> {
	
	@NotNull
	private Lexicon<T> lexicon;
	
	public LexiconBuilder(@NotNull Class<T> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		
		lexicon = new Lexicon<>(clazz);
	}
	public LexiconBuilder() {
		lexicon = new Lexicon<>();
	}
	
	@NotNull
	public LexiconBuilder add(@Nullable T element) {
		lexicon.add(element);
		return this;
	}
	
	@SafeVarargs
	@NotNull
	public final LexiconBuilder addAll(@Nullable T... elements) {
		lexicon.addAll(elements);
		return this;
	}
	@NotNull
	public LexiconBuilder addAll(@Nullable Collection<T> elements) {
		lexicon.addAll(elements);
		return this;
	}
	
	/**
	 * Interesting methods to add :
	 * set
	 * remove
	 * swap
	 * removeAll
	 * retainAll
	 * addHandlers
	 * getHandlers
	 * setHandlers
	 * removeHandlers
	 * acceptDuplicates
	 * acceptNullValue
	 * isSynchronizedAccess
	 */
	
	/**
	 * Create the lexicon according to all settings
	 * @return A lexicon instance
	 */
	@NotNull
	public Lexicon<T> createLexicon() {
		if (lexicon == null)
			lexicon = new Lexicon<>();
		
		return lexicon;
	}
}
