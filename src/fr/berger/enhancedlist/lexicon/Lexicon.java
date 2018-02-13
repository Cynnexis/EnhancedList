package fr.berger.enhancedlist.lexicon;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Couple;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

// http://server2client.com/images/collectionhierarchy.jpg
// https://docs.oracle.com/javase/tutorial/collections/custom-implementations/index.html

public class Lexicon<T> extends AbstractCollection<T> implements Serializable {
	
	@NotNull
	private T[] array;
	
	@NotNull
	private transient ArrayList<LexiconListener<T>> lexiconListeners = new ArrayList<>();
	@NotNull
	private transient Class<T> clazz;
	private boolean acceptDuplicates = true;
	private boolean acceptNullValues = true;
	private boolean automaticSort = false;
	private boolean synchronizedAccess = false;
	
	private int actualSize = 0;
	
	// TODO: Implement a way to make this list persistent (aka: survive after the program exit)
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @throws IllegalArgumentException if the specified initial capacity
	 *                                  is negative
	 */
	public Lexicon() {
	}
	public Lexicon(@NotNull Class<T> clazz) {
		setClazz(clazz);
	}
	public Lexicon(@NotNull Class<T> clazz, int initialCapacity) {
		setClazz(clazz);
		growCapacity(initialCapacity);
	}
	public <U extends T> Lexicon(@Nullable U element) {
		add(element);
	}
	public Lexicon(@Nullable Collection<? extends T> elements) {
		addAll(elements);
	}
	
	
	/* METHODS */
	
	// Is There Null Element
	public static boolean isThereNullElement(@NotNull Lexicon<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		boolean nullFound = false;
		
		for (int i = 0; i < list.size() && !nullFound; i++)
			if (list.get(i) == null)
				nullFound = true;
		
		return nullFound;
	}
	public boolean isThereNullElement() {
		return isThereNullElement(this);
	}
	
	// Find Null Elements
	public static ArrayList<Integer> findNullElements(@NotNull Lexicon<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		ArrayList<Integer> indexes = new ArrayList<>(0);
		
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == null)
				indexes.add(i);
		
		return indexes;
	}
	
	// Delete Null Elements
	public static void deleteNullElement(@NotNull Lexicon<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				list.remove(i);
				i--;
			}
		}
	}
	public void deleteNullElement() {
		deleteNullElement(this);
	}
	
	public static boolean isThereDuplicates(@NotNull Lexicon<?> list) {
		boolean duplicateFound = false;
		
		for (int i = 0; i < list.size()-1 && !duplicateFound; i++)
			for (int j = i+1; j < list.size() && !duplicateFound; j++)
				if (Objects.equals(list.get(i), list.get(j)))
					duplicateFound = true;
		
		return duplicateFound;
	}
	public boolean isThereDuplicates() {
		return isThereDuplicates(this);
	}
	
	public static @NotNull ArrayList<Couple<Integer, Integer>> findDuplications(@NotNull Lexicon<?> list) {
		ArrayList<Couple<Integer, Integer>> counter = new ArrayList<>(0);
		
		for (int i = 0; i < list.size()-1; i++)
			for (int j = i+1; j < list.size(); j++)
				if (Objects.equals(list.get(i), list.get(j)))
					counter.add(new Couple<>(i, j));
		
		return counter;
	}
	public @NotNull ArrayList<Couple<Integer, Integer>> findDuplications() {
		return findDuplications(this);
	}
	
	public static void deleteDuplications(@NotNull Lexicon<?> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = i+1; j < list.size(); j++) {
				if (Objects.equals(list.get(i), list.get(j))) {
					list.remove(j);
					j--;
				}
			}
		}
	}
	public void deleteDuplications() {
		deleteDuplications(this);
	}
	
	public int checkCapacity(int newCapacity) {
		if (newCapacity > capacity())
			return growCapacity(newCapacity);
		
		return capacity();
	}
	
	public int trimToSize() {
		if (size() < capacity()) {
			array = Arrays.copyOf(array, size());
		}
		
		return capacity();
	}
	
	public int growCapacity(int newCapacity) {
		if (!checkArrayNullity()) {
			try {
				array = (T[]) new Object[0];
			} catch (ClassCastException ex) {
				ex.printStackTrace();
			}
		}
		
		if (newCapacity > capacity()) {
			array = Arrays.copyOf(array, newCapacity);
		}
		
		return capacity();
	}
	public int growCapacity() {
		return growCapacity(size() + 1);
	}
	
	public int capacity() {
		if (!checkArrayNullity())
			return 0;
		
		return array.length;
	}
	
	private boolean checkArrayNullity() {
		if (array == null) {
			if (getClazz() != null) {
				try {
					array = (T[]) Array.newInstance(getClazz(), 0);
					return true;
				} catch (ClassCastException ex) {
					return false;
				}
			}
			else
				return false;
		}
		else
			return true;
	}
	
	public T get(int index) {
		if (!checkArrayNullity())
			return null;
		
		checkIndex(index);
		
		return array[index];
	}
	
	public T set(int index, @Nullable T element) {
		checkIndex(index);
		
		if (!isAcceptNullValues() && element == null)
			return null;
		
		if (!isAcceptDuplicates() && contains(element))
			return null;
		
		if (getClazz() == null)
			setClazz((Class<T>) element.getClass());
		
		T oldValue = get(index);
		array[index] = element;
		
		return oldValue;
	}
	
	public void checkIndex(int index) {
		if (!(0 <= index && index < size()))
			throw new ArrayIndexOutOfBoundsException();
	}
	
	public boolean addAll(T[] list) {
		if (list == null)
			return true;
		
		boolean problem = false;
		
		for (T t : list) {
			if (!add(t))
				problem = true;
		}
		
		return !problem;
	}
	
	
	/* GETTERS & SETTERS */
	
	public @NotNull ArrayList<LexiconListener<T>> getLexiconListeners() {
		// If the attribute is null, create a new instance
		if (this.lexiconListeners == null)
			this.lexiconListeners = new ArrayList<>();
		
		// Remove 'null' instances
		deleteNullElement();
		
		// Remove duplicates elements
		deleteDuplications();
		
		return lexiconListeners;
	}
	
	public void setLexiconListeners(@NotNull ArrayList<LexiconListener<T>> lexiconListeners) {
		if (lexiconListeners == null)
			throw new NullPointerException();
		
		// Remove 'null' instances
		//deleteNullElement(lexiconListeners);
		
		// Remove duplicates elements
		//deleteDuplications(lexiconListeners);
		
		this.lexiconListeners = lexiconListeners;
	}
	
	public @NotNull Class<T> getClazz() {
		return clazz;
	}
	
	public void setClazz(@NotNull Class<T> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		
		this.clazz = clazz;
	}
	
	public boolean isAcceptDuplicates() {
		return acceptDuplicates;
	}
	
	public void setAcceptDuplicates(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}
	
	public boolean isAcceptNullValues() {
		return acceptNullValues;
	}
	
	public void setAcceptNullValues(boolean acceptNullValues) {
		this.acceptNullValues = acceptNullValues;
	}
	
	public boolean isAutomaticSort() {
		return automaticSort;
	}
	
	public void setAutomaticSort(boolean automaticSort) {
		this.automaticSort = automaticSort;
	}
	
	public boolean isSynchronizedAccess() {
		return synchronizedAccess;
	}
	
	public void setSynchronizedAccess(boolean synchronizedAccess) {
		this.synchronizedAccess = synchronizedAccess;
	}
	
	/* OVERRIDES */
	
	/**
	 * Returns an iterator over the elements contained in this collection.
	 *
	 * @return an iterator over the elements contained in this collection
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			public int index = 0;
			
			@Override
			public boolean hasNext() {
				return size() > index;
			}
			
			@Override
			public T next() {
				return get(index++);
			}
		};
	}
	
	/**
	 * Performs the given action for each element of the {@code Iterable}
	 * until all elements have been processed or the action throws an
	 * exception.  Unless otherwise specified by the implementing class,
	 * actions are performed in the order of iteration (if an iteration order
	 * is specified).  Exceptions thrown by the action are relayed to the
	 * caller.
	 *
	 * @param action The action to be performed for each element
	 * @throws NullPointerException if the specified action is null
	 * @implSpec <p>The default implementation behaves as if:
	 * <pre>{@code
	 *     for (T t : this)
	 *         action.accept(t);
	 * }</pre>
	 * @since 1.8
	 */
	@Override
	public void forEach(Consumer<? super T> action) {
		for (T e : this)
			action.accept(e);
	}
	
	/**
	 * Removes all of the elements of this collection that satisfy the given
	 * predicate.  Errors or runtime exceptions thrown during iteration or by
	 * the predicate are relayed to the caller.
	 *
	 * @param filter a predicate which returns {@code true} for elements to be
	 *               removed
	 * @return {@code true} if any elements were removed
	 * @throws NullPointerException          if the specified filter is null
	 * @throws UnsupportedOperationException if elements cannot be removed
	 *                                       from this collection.  Implementations may throw this exception if a
	 *                                       matching element cannot be removed or if, in general, removal is not
	 *                                       supported.
	 * @implSpec The default implementation traverses all elements of the collection using
	 * its {@link #iterator}.  Each matching element is removed using
	 * {@link Iterator#remove()}.  If the collection's iterator does not
	 * support removal then an {@code UnsupportedOperationException} will be
	 * thrown on the first matching element.
	 * @since 1.8
	 */
	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		boolean atLeastOneRemoved = false;
		
		for (T e : this) {
			if (filter.test(e)) {
				remove(e);
				atLeastOneRemoved = true;
			}
		}
		
		return atLeastOneRemoved;
	}
	
	@Override
	public int size() {
		if (actualSize < 0)
			actualSize = 0;
		
		return actualSize;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns <tt>size() == 0</tt>.
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over the elements in the collection,
	 * checking each element in turn for equality with the specified element.
	 *
	 * @param o
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public boolean contains(@Nullable Object o) {
		boolean result = false;
		
		for (int i = 0; i < size() && !result; i++)
			if (Objects.equals(o, get(i)))
				result = true;
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns an array containing all the elements
	 * returned by this collection's iterator, in the same order, stored in
	 * consecutive elements of the array, starting with index {@code 0}.
	 * The length of the returned array is equal to the number of elements
	 * returned by the iterator, even if the size of this collection changes
	 * during iteration, as might happen if the collection permits
	 * concurrent modification during iteration.  The {@code size} method is
	 * called only as an optimization hint; the correct result is returned
	 * even if the iterator returns a different number of elements.
	 * <p>
	 * <p>This method is equivalent to:
	 * <p>
	 * <pre> {@code
	 * List<E> list = new ArrayList<E>(size());
	 * for (E e : this)
	 *     list.add(e);
	 * return list.toArray();
	 * }</pre>
	 */
	@Override
	public T[] toArray() {
		return array.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns an array containing all the elements
	 * returned by this collection's iterator in the same order, stored in
	 * consecutive elements of the array, starting with index {@code 0}.
	 * If the number of elements returned by the iterator is too large to
	 * fit into the specified array, then the elements are returned in a
	 * newly allocated array with length equal to the number of elements
	 * returned by the iterator, even if the size of this collection
	 * changes during iteration, as might happen if the collection permits
	 * concurrent modification during iteration.  The {@code size} method is
	 * called only as an optimization hint; the correct result is returned
	 * even if the iterator returns a different number of elements.
	 * <p>
	 * <p>This method is equivalent to:
	 * <p>
	 * <pre> {@code
	 * List<E> list = new ArrayList<E>(size());
	 * for (E e : this)
	 *     list.add(e);
	 * return list.toArray(a);
	 * }</pre>
	 *
	 * @param a
	 * @throws ArrayStoreException  {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public <U> U[] toArray(U[] a) {
		if (a.length < size())
			return (U[]) Arrays.copyOf(array, size(), a.getClass());
		
		System.arraycopy(array, 0, a, 0, size());
		if (a.length > size())
			a[size()] = null;
		
		return a;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation always throws an
	 * <tt>UnsupportedOperationException</tt>.
	 *
	 * @param element The new element to add to the list
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @throws IllegalArgumentException      {@inheritDoc}
	 * @throws IllegalStateException         {@inheritDoc}
	 */
	@Override
	public boolean add(T element) {
		if (!isAcceptNullValues() && element == null)
			return false;
		
		if (!isAcceptDuplicates() && contains(element))
			return false;
		
		checkCapacity(size() + 1);
		
		if (getClazz() == null)
			setClazz((Class<T>) element.getClass());
		
		array[actualSize++] = element;
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over the collection looking for the
	 * specified element.  If it finds the element, it removes the element
	 * from the collection using the iterator's remove method.
	 * <p>
	 * <p>Note that this implementation throws an
	 * <tt>UnsupportedOperationException</tt> if the iterator returned by this
	 * collection's iterator method does not implement the <tt>remove</tt>
	 * method and this collection contains the specified object.
	 *
	 * @param o
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		int toRemove = -1;
		
		for (int i = 0; i < size() && toRemove == -1; i++)
			if (Objects.equals(get(i), o))
				toRemove = i;
		
		if (toRemove != -1) {
			remove(toRemove);
			return true;
		}
		else
			return false;
	}
	
	public T remove(int index) {
		checkIndex(index);
		
		T oldValue = get(index);
		
		if (index < size() - 1)
			System.arraycopy(array, index + 1, array, index, size() - 1 - index);
		
		array[--actualSize] = null;
		
		return oldValue;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over the specified collection,
	 * checking each element returned by the iterator in turn to see
	 * if it's contained in this collection.  If all elements are so
	 * contained <tt>true</tt> is returned, otherwise <tt>false</tt>.
	 *
	 * @param c
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 * @see #contains(Object)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();
		
		if (c.size() == 0)
			return true;
		
		boolean objectNotInListFound = true;
		
		while (c.iterator().hasNext() && objectNotInListFound) {
			Object o = c.iterator().next();
			
			if(!contains(o))
				objectNotInListFound = false;
		}
		
		return objectNotInListFound;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over the specified collection, and adds
	 * each object returned by the iterator to this collection, in turn.
	 * <p>
	 * <p>Note that this implementation will throw an
	 * <tt>UnsupportedOperationException</tt> unless <tt>add</tt> is
	 * overridden (assuming the specified collection is non-empty).
	 *
	 * @param c
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @throws IllegalArgumentException      {@inheritDoc}
	 * @throws IllegalStateException         {@inheritDoc}
	 * @see #add(Object)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (c == null)
			throw new NullPointerException();
		
		if (c.size() == 0)
			return true;
		
		boolean problem = false;
		
		while (c.iterator().hasNext()) {
			T t = c.iterator().next();
			
			if (!add(t))
				problem = true;
		}
		
		return !problem;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over this collection, checking each
	 * element returned by the iterator in turn to see if it's contained
	 * in the specified collection.  If it's so contained, it's removed from
	 * this collection with the iterator's <tt>remove</tt> method.
	 * <p>
	 * <p>Note that this implementation will throw an
	 * <tt>UnsupportedOperationException</tt> if the iterator returned by the
	 * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
	 * and this collection contains one or more elements in common with the
	 * specified collection.
	 *
	 * @param c
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();
		
		if (c.size() == 0)
			return true;
		
		boolean problem = false;
		
		while (c.iterator().hasNext()) {
			Object o = c.iterator().next();
			
			if (!remove(o))
				problem = true;
		}
		
		return !problem;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over this collection, checking each
	 * element returned by the iterator in turn to see if it's contained
	 * in the specified collection.  If it's not so contained, it's removed
	 * from this collection with the iterator's <tt>remove</tt> method.
	 * <p>
	 * <p>Note that this implementation will throw an
	 * <tt>UnsupportedOperationException</tt> if the iterator returned by the
	 * <tt>iterator</tt> method does not implement the <tt>remove</tt> method
	 * and this collection contains one or more elements not present in the
	 * specified collection.
	 *
	 * @param c
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		if (c == null)
			throw new NullPointerException();
		
		if (c.size() == 0)
			return true;
		
		while (c.iterator().hasNext()) {
			Object o = c.iterator().next();
			
			for (int i = 0; i < size(); i++) {
				if (!Objects.equals(o, get(i)))
					remove(i);
			}
		}
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation iterates over this collection, removing each
	 * element using the <tt>Iterator.remove</tt> operation.  Most
	 * implementations will probably choose to override this method for
	 * efficiency.
	 * <p>
	 * <p>Note that this implementation will throw an
	 * <tt>UnsupportedOperationException</tt> if the iterator returned by this
	 * collection's <tt>iterator</tt> method does not implement the
	 * <tt>remove</tt> method and this collection is non-empty.
	 *
	 * @throws UnsupportedOperationException {@inheritDoc}
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size(); i++)
			set(i, null);
		
		actualSize = 0;
	}
	
	/**
	 * Returns a string representation of this collection.  The string
	 * representation consists of a list of the collection's elements in the
	 * order they are returned by its iterator, enclosed in square brackets
	 * (<tt>"[]"</tt>).  Adjacent elements are separated by the characters
	 * <tt>", "</tt> (comma and space).  Elements are converted to strings as
	 * by {@link String#valueOf(Object)}.
	 *
	 * @return a string representation of this collection
	 */
	@Override
	public String toString() {
		return "Lexicon{" +
				"array=" + Arrays.toString(array) +
				", acceptDuplicates=" + acceptDuplicates +
				", acceptNullValues=" + acceptNullValues +
				", automaticSort=" + automaticSort +
				", synchronizedAccess=" + synchronizedAccess +
				", actualSize=" + actualSize +
				'}';
	}
}
