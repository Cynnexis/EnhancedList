package fr.berger.enhancedlist.lexicon;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Couple;

import java.io.Serializable;
import java.util.*;
import java.util.function.UnaryOperator;

public class Lexicon<T> extends ArrayList<T> implements Serializable, Iterable<T>, List<T>, RandomAccess, Cloneable {
	
	@NotNull
	private ArrayList<LexiconListener<T>> lexiconListeners = new ArrayList<>(0);
	private boolean acceptCopies = true;
	private boolean acceptNullValues = true;
	
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param initialCapacity the initial capacity of the list
	 * @throws IllegalArgumentException if the specified initial capacity
	 *                                  is negative
	 */
	public Lexicon(int initialCapacity) {
		super(initialCapacity);
	}
	
	/**
	 * Constructs an empty list with an initial capacity of 0.
	 */
	public Lexicon() {
		super(0);
	}
	
	/**
	 * Constructs a list containing the elements of the specified
	 * collection, in the order they are returned by the collection's
	 * iterator.
	 *
	 * @param c the collection whose elements are to be placed into this list
	 * @throws NullPointerException if the specified collection is null
	 */
	public Lexicon(Collection<? extends T> c) {
		super(c);
	}
	
	
	/* METHODS */
	
	// Is There Null Element
	public static boolean isThereNullElement(@NotNull ArrayList<?> list) {
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
	public static ArrayList<Integer> findNullElements(@NotNull ArrayList<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		ArrayList<Integer> indexes = new ArrayList<>(0);
		
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == null)
				indexes.add(i);
		
		return indexes;
	}
	
	// Count Null Elements
	public static int countNullElements(@NotNull ArrayList<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		int counter = 0;
		
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				counter++;
			}
		}
		
		return counter;
	}
	public int countNullElements() {
		return countNullElements(this);
	}
	
	// Delete Null Elements
	public static void deleteNullElement(@NotNull ArrayList<?> list) {
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
	
	public static @NotNull ArrayList<Couple<Integer, Integer>> findDuplications(@NotNull ArrayList<?> list) {
		ArrayList<Couple<Integer, Integer>> counter = new ArrayList<>(0);
		
		for (int i = 0; i < list.size(); i++)
			for (int j = 0; j < list.size(); j++)
				if (Objects.equals(list.get(i), list.get(j)))
					counter.add(new Couple<>(i, j));
		
		return counter;
	}
	public @NotNull ArrayList<Couple<Integer, Integer>> findDuplications() {
		return findDuplications(this);
	}
	
	public static void deleteDuplications(ArrayList<?> list) {
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
	
	
	/* GETTERS & SETTERS */
	
	public @NotNull ArrayList<LexiconListener<T>> getLexiconListeners() {
		// If the attribute is null, create a new instance
		if (this.lexiconListeners == null)
			this.lexiconListeners = new ArrayList<>(0);
		
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
		deleteNullElement(lexiconListeners);
		
		// Remove duplicates elements
		deleteDuplications(lexiconListeners);
		
		this.lexiconListeners = lexiconListeners;
	}
	
	public boolean isAcceptCopies() {
		return acceptCopies;
	}
	
	public void setAcceptCopies(boolean acceptCopies) {
		this.acceptCopies = acceptCopies;
	}
	
	public boolean isAcceptNullValues() {
		return acceptNullValues;
	}
	
	public void setAcceptNullValues(boolean acceptNullValues) {
		this.acceptNullValues = acceptNullValues;
	}
	
	
	/* OVERRIDES */
	
	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		T element = super.get(index);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementGotten(element);
		
		return element;
	}
	
	/**
	 * Replaces the element at the specified position in this list with
	 * the specified element.
	 *
	 * @param index   index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public T set(int index, @NotNull T element) {
		T result = super.set(index, element);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementSet(index, result);
		
		return result;
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param t element to be appended to this list
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	@Override
	public boolean add(T t) {
		boolean result = super.add(t);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementAdded(t);
		
		return result;
	}
	
	/**
	 * Inserts the specified element at the specified position in this
	 * list. Shifts the element currently at that position (if any) and
	 * any subsequent elements to the right (adds one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public void add(int index, T element) {
		super.add(index, element);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementAdded(index, element);
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 * Shifts any subsequent elements to the left (subtracts one from their
	 * indices).
	 *
	 * @param index the index of the element to be removed
	 * @return the element that was removed from the list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public T remove(int index) {
		T result = super.remove(index);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementRemoved(index, result);
		
		return result;
	}
	
	/**
	 * Removes the first occurrence of the specified element from this list,
	 * if it is present.  If the list does not contain the element, it is
	 * unchanged.  More formally, removes the element with the lowest index
	 * <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
	 * (if such an element exists).  Returns <tt>true</tt> if this list
	 * contained the specified element (or equivalently, if this list
	 * changed as a result of the call).
	 *
	 * @param o element to be removed from this list, if present
	 * @return <tt>true</tt> if this list contained the specified element
	 */
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onElementRemoved(o);
		
		return result;
	}
	
	/**
	 * Removes all of the elements from this list.  The list will
	 * be empty after this call returns.
	 */
	@Override
	public void clear() {
		super.clear();
		
		for (LexiconListener<T> lexiconListener : lexiconListeners)
			lexiconListener.onLexiconCleared(this);
	}
	
	/**
	 * Appends all of the elements in the specified collection to the end of
	 * this list, in the order that they are returned by the
	 * specified collection's Iterator.  The behavior of this operation is
	 * undefined if the specified collection is modified while the operation
	 * is in progress.  (This implies that the behavior of this call is
	 * undefined if the specified collection is this list, and this
	 * list is nonempty.)
	 *
	 * @param c collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws NullPointerException if the specified collection is null
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return super.addAll(c);
	}
	
	/**
	 * Inserts all of the elements in the specified collection into this
	 * list, starting at the specified position.  Shifts the element
	 * currently at that position (if any) and any subsequent elements to
	 * the right (increases their indices).  The new elements will appear
	 * in the list in the order that they are returned by the
	 * specified collection's iterator.
	 *
	 * @param index index at which to insert the first element from the
	 *              specified collection
	 * @param c     collection containing elements to be added to this list
	 * @return <tt>true</tt> if this list changed as a result of the call
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws NullPointerException      if the specified collection is null
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return super.addAll(index, c);
	}
	
	/**
	 * Removes from this list all of the elements whose index is between
	 * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
	 * Shifts any succeeding elements to the left (reduces their index).
	 * This call shortens the list by {@code (toIndex - fromIndex)} elements.
	 * (If {@code toIndex==fromIndex}, this operation has no effect.)
	 *
	 * @param fromIndex
	 * @param toIndex
	 * @throws IndexOutOfBoundsException if {@code fromIndex} or
	 *                                   {@code toIndex} is out of range
	 *                                   ({@code fromIndex < 0 ||
	 *                                   fromIndex >= size() ||
	 *                                   toIndex > size() ||
	 *                                   toIndex < fromIndex})
	 */
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}
	
	/**
	 * Removes from this list all of its elements that are contained in the
	 * specified collection.
	 *
	 * @param c collection containing elements to be removed from this list
	 * @return {@code true} if this list changed as a result of the call
	 * @throws ClassCastException   if the class of an element of this list
	 *                              is incompatible with the specified collection
	 *                              (<a href="Collection.html#optional-restrictions">optional</a>)
	 * @throws NullPointerException if this list contains a null element and the
	 *                              specified collection does not permit null elements
	 *                              (<a href="Collection.html#optional-restrictions">optional</a>),
	 *                              or if the specified collection is null
	 * @see Collection#contains(Object)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return super.removeAll(c);
	}
	
	@Override
	public void replaceAll(UnaryOperator<T> operator) {
		super.replaceAll(operator);
	}
	
	@Override
	public void sort(Comparator<? super T> c) {
		super.sort(c);
	}
}
