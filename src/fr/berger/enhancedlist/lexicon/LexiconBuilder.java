package fr.berger.enhancedlist.lexicon;

import fr.berger.enhancedlist.lexicon.eventhandlers.AddHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.GetHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.RemoveHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.SetHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

public class LexiconBuilder<T> implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 8735223388520088192L;
	@NotNull
	private Lexicon<T> lexicon;
	
	/* CONSTRUCTORS */
	
	public LexiconBuilder(@NotNull Class<T> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		
		lexicon = new Lexicon<>(clazz);
	}
	public LexiconBuilder(@NotNull Lexicon<T> lexicon) {
		if (lexicon == null)
			throw new NullPointerException();
		
		this.lexicon = lexicon;
	}
	public LexiconBuilder() {
		lexicon = new Lexicon<>();
	}
	
	/* SERIALIZATION METHODS */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(lexicon);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		this.lexicon = (Lexicon<T>) stream.readObject();
	}
	
	/* LEXICONBUILDER METHODS */
	
	@NotNull
	public LexiconBuilder<T> add(@Nullable T element) {
		lexicon.add(element);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addAll(@Nullable Collection<T> elements) {
		lexicon.addAll(elements);
		return this;
	}
	@SafeVarargs
	@NotNull
	public final LexiconBuilder<T> addAll(@Nullable T... elements) {
		lexicon.addAll(elements);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> set(int index, @Nullable T element) {
		lexicon.set(index, element);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> setOrAdd(int index, @Nullable T element) {
		lexicon.setOrAdd(index, element);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> remove(int index) {
		lexicon.remove(index);
		return this;
	}
	@NotNull
	public LexiconBuilder<T> remove(@Nullable T element) {
		lexicon.remove(element);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> removeAll(@Nullable T... elements) {
		lexicon.removeAll(elements);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> retainAll(@Nullable T... elements) {
		lexicon.retainAll(elements);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> swap(int index1, int index2) {
		lexicon.swap(index1, index2);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addAddHandlers(@NotNull AddHandler<T> addHandler) {
		lexicon.addAddHandler(addHandler);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addGetHandlers(@NotNull GetHandler<T> getHandler) {
		lexicon.addGetHandler(getHandler);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addSetHandlers(@NotNull SetHandler<T> setHandler) {
		lexicon.addSetHandler(setHandler);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addRemoveHandlers(@NotNull RemoveHandler<T> removeHandler) {
		lexicon.addRemoveHandler(removeHandler);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> setAcceptDuplicates(boolean acceptDuplicates) {
		lexicon.setAcceptDuplicates(acceptDuplicates);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> setAcceptNullValues(boolean acceptNullValues) {
		lexicon.setAcceptNullValues(acceptNullValues);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> setSynchronizedAccess(boolean synchronizedAccess) {
		lexicon.setSynchronizedAccess(synchronizedAccess);
		return this;
	}
	
	@NotNull
	public LexiconBuilder<T> addObserver(@NotNull Observer observer) {
		lexicon.addObserver(observer);
		return this;
	}
	
	/**
	 * Create the lexicon according to all settings
	 * @return A lexicon instance
	 */
	@NotNull
	public Lexicon<T> createLexicon() {
		if (lexicon == null)
			lexicon = new Lexicon<>();
		
		return new Lexicon<>(lexicon);
	}
}
