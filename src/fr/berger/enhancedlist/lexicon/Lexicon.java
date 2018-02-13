package fr.berger.enhancedlist.lexicon;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Couple;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

// http://server2client.com/images/collectionhierarchy.jpg
// https://docs.oracle.com/javase/tutorial/collections/custom-implementations/index.html

public class Lexicon<T> extends AbstractList<T> implements Serializable, Iterable<T>, List<T>, RandomAccess, Cloneable {
	
	@NotNull
	private T[] array;
	
	@NotNull
	private ArrayList<LexiconListener<T>> lexiconListeners = new ArrayList<>(0);
	private boolean acceptDuplicates = true;
	private boolean acceptNullValues = true;
	private boolean automaticSort = false;
	private boolean synchronizedAccess = false;
	
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 *
	 * @param initialCapacity the initial capacity of the list
	 * @throws IllegalArgumentException if the specified initial capacity
	 *                                  is negative
	 */
	public Lexicon(int initialCapacity) {
		super();
	}
	
	/**
	 * Constructs an empty list with an initial capacity of 0.
	 */
	public Lexicon() {
		super(0);
	}
	
	@Override
	public int size() {
		return 0;
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
	public static boolean isThereNullElement(@NotNull AbstractList<?> list) {
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
	public static ArrayList<Integer> findNullElements(@NotNull AbstractList<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		ArrayList<Integer> indexes = new ArrayList<>(0);
		
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == null)
				indexes.add(i);
		
		return indexes;
	}
	
	// Count Null Elements
	public static int countNullElements(@NotNull AbstractList<?> list) {
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
	public static void deleteNullElement(@NotNull AbstractList<?> list) {
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
	
	public static @NotNull ArrayList<Couple<Integer, Integer>> findDuplications(@NotNull AbstractList<?> list) {
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
	
	public static void deleteDuplications(@NotNull AbstractList<?> list) {
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
		return false;
	}
	
	/**
	 * Replaces each element of this list with the result of applying the
	 * operator to that element.  Errors or runtime exceptions thrown by
	 * the operator are relayed to the caller.
	 *
	 * @param operator the operator to apply to each element
	 * @throws UnsupportedOperationException if this list is unmodifiable.
	 *                                       Implementations may throw this exception if an element
	 *                                       cannot be replaced or if, in general, modification is not
	 *                                       supported
	 * @throws NullPointerException          if the specified operator is null or
	 *                                       if the operator result is a null value and this list does
	 *                                       not permit null elements
	 *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
	 * @implSpec The default implementation is equivalent to, for this {@code list}:
	 * <pre>{@code
	 *     final ListIterator<E> li = list.listIterator();
	 *     while (li.hasNext()) {
	 *         li.set(operator.apply(li.next()));
	 *     }
	 * }</pre>
	 * <p>
	 * If the list's list-iterator does not support the {@code set} operation
	 * then an {@code UnsupportedOperationException} will be thrown when
	 * replacing the first element.
	 * @since 1.8
	 */
	@Override
	public void replaceAll(UnaryOperator<T> operator) {
	
	}
	
	/**
	 * Sorts this list according to the order induced by the specified
	 * {@link Comparator}.
	 * <p>
	 * <p>All elements in this list must be <i>mutually comparable</i> using the
	 * specified comparator (that is, {@code c.compare(e1, e2)} must not throw
	 * a {@code ClassCastException} for any elements {@code e1} and {@code e2}
	 * in the list).
	 * <p>
	 * <p>If the specified comparator is {@code null} then all elements in this
	 * list must implement the {@link Comparable} interface and the elements'
	 * {@linkplain Comparable natural ordering} should be used.
	 * <p>
	 * <p>This list must be modifiable, but need not be resizable.
	 *
	 * @param c the {@code Comparator} used to compare list elements.
	 *          A {@code null} value indicates that the elements'
	 *          {@linkplain Comparable natural ordering} should be used
	 * @throws ClassCastException            if the list contains elements that are not
	 *                                       <i>mutually comparable</i> using the specified comparator
	 * @throws UnsupportedOperationException if the list's list-iterator does
	 *                                       not support the {@code set} operation
	 * @throws IllegalArgumentException      (<a href="Collection.html#optional-restrictions">optional</a>)
	 *                                       if the comparator is found to violate the {@link Comparator}
	 *                                       contract
	 * @implSpec The default implementation obtains an array containing all elements in
	 * this list, sorts the array, and iterates over this list resetting each
	 * element from the corresponding position in the array. (This avoids the
	 * n<sup>2</sup> log(n) performance that would result from attempting
	 * to sort a linked list in place.)
	 * @implNote This implementation is a stable, adaptive, iterative mergesort that
	 * requires far fewer than n lg(n) comparisons when the input array is
	 * partially sorted, while offering the performance of a traditional
	 * mergesort when the input array is randomly ordered.  If the input array
	 * is nearly sorted, the implementation requires approximately n
	 * comparisons.  Temporary storage requirements vary from a small constant
	 * for nearly sorted input arrays to n/2 object references for randomly
	 * ordered input arrays.
	 * <p>
	 * <p>The implementation takes equal advantage of ascending and
	 * descending order in its input array, and can take advantage of
	 * ascending and descending order in different parts of the same
	 * input array.  It is well-suited to merging two or more sorted arrays:
	 * simply concatenate the arrays and sort the resulting array.
	 * <p>
	 * <p>The implementation was adapted from Tim Peters's list sort for Python
	 * (<a href="http://svn.python.org/projects/python/trunk/Objects/listsort.txt">
	 * TimSort</a>).  It uses techniques from Peter McIlroy's "Optimistic
	 * Sorting and Information Theoretic Complexity", in Proceedings of the
	 * Fourth Annual ACM-SIAM Symposium on Discrete Algorithms, pp 467-474,
	 * January 1993.
	 * @since 1.8
	 */
	@Override
	public void sort(Comparator<? super T> c) {
	
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @param index
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		if (!(0 <= index && index < size()))
			throw new ArrayIndexOutOfBoundsException();
		
		return array[index];
	}
	
	public T set(int index, T element) {
		if (!(0 <= index && index < size()))
			throw new ArrayIndexOutOfBoundsException();
		
		T oldValue = get(index);
		array[index] = element;
		return oldValue;
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
	
	}
	
	/**
	 * Creates a {@link Spliterator} over the elements in this list.
	 * <p>
	 * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
	 * {@link Spliterator#ORDERED}.  Implementations should document the
	 * reporting of additional characteristic values.
	 *
	 * @return a {@code Spliterator} over the elements in this list
	 * @implSpec The default implementation creates a
	 * <em><a href="Spliterator.html#binding">late-binding</a></em> spliterator
	 * from the list's {@code Iterator}.  The spliterator inherits the
	 * <em>fail-fast</em> properties of the list's iterator.
	 * @implNote The created {@code Spliterator} additionally reports
	 * {@link Spliterator#SUBSIZED}.
	 * @since 1.8
	 */
	@Override
	public Spliterator<T> spliterator() {
		return null;
	}
	
	/**
	 * Returns a sequential {@code Stream} with this collection as its source.
	 * <p>
	 * <p>This method should be overridden when the {@link #spliterator()}
	 * method cannot return a spliterator that is {@code IMMUTABLE},
	 * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
	 * for details.)
	 *
	 * @return a sequential {@code Stream} over the elements in this collection
	 * @implSpec The default implementation creates a sequential {@code Stream} from the
	 * collection's {@code Spliterator}.
	 * @since 1.8
	 */
	@Override
	public Stream<T> stream() {
		return null;
	}
	
	/**
	 * Returns a possibly parallel {@code Stream} with this collection as its
	 * source.  It is allowable for this method to return a sequential stream.
	 * <p>
	 * <p>This method should be overridden when the {@link #spliterator()}
	 * method cannot return a spliterator that is {@code IMMUTABLE},
	 * {@code CONCURRENT}, or <em>late-binding</em>. (See {@link #spliterator()}
	 * for details.)
	 *
	 * @return a possibly parallel {@code Stream} over the elements in this
	 * collection
	 * @implSpec The default implementation creates a parallel {@code Stream} from the
	 * collection's {@code Spliterator}.
	 * @since 1.8
	 */
	@Override
	public Stream<T> parallelStream() {
		return null;
	}
	
	/**
	 * Appends the specified element to the end of this list (optional
	 * operation).
	 * <p>
	 * <p>Lists that support this operation may place limitations on what
	 * elements may be added to this list.  In particular, some
	 * lists will refuse to add null elements, and others will impose
	 * restrictions on the type of elements that may be added.  List
	 * classes should clearly specify in their documentation any restrictions
	 * on what elements may be added.
	 * <p>
	 * <p>This implementation calls {@code add(size(), e)}.
	 * <p>
	 * <p>Note that this implementation throws an
	 * {@code UnsupportedOperationException} unless
	 * {@link #add(int, Object) add(int, E)} is overridden.
	 *
	 * @param t element to be appended to this list
	 * @return {@code true} (as specified by {@link Collection#add})
	 * @throws UnsupportedOperationException if the {@code add} operation
	 *                                       is not supported by this list
	 * @throws ClassCastException            if the class of the specified element
	 *                                       prevents it from being added to this list
	 * @throws NullPointerException          if the specified element is null and this
	 *                                       list does not permit null elements
	 * @throws IllegalArgumentException      if some property of this element
	 *                                       prevents it from being added to this list
	 */
	@Override
	public boolean add(T t) {
		return super.add(t);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation always throws an
	 * {@code UnsupportedOperationException}.
	 *
	 * @param index
	 * @param element
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @throws IllegalArgumentException      {@inheritDoc}
	 * @throws IndexOutOfBoundsException     {@inheritDoc}
	 */
	@Override
	public void add(int index, T element) {
		super.add(index, element);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation always throws an
	 * {@code UnsupportedOperationException}.
	 *
	 * @param index
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws IndexOutOfBoundsException     {@inheritDoc}
	 */
	@Override
	public T remove(int index) {
		return super.remove(index);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation first gets a list iterator (with
	 * {@code listIterator()}).  Then, it iterates over the list until the
	 * specified element is found or the end of the list is reached.
	 *
	 * @param o
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public int indexOf(Object o) {
		return super.indexOf(o);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation first gets a list iterator that points to the end
	 * of the list (with {@code listIterator(size())}).  Then, it iterates
	 * backwards over the list until the specified element is found, or the
	 * beginning of the list is reached.
	 *
	 * @param o
	 * @throws ClassCastException   {@inheritDoc}
	 * @throws NullPointerException {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object o) {
		return super.lastIndexOf(o);
	}
	
	/**
	 * Removes all of the elements from this list (optional operation).
	 * The list will be empty after this call returns.
	 * <p>
	 * <p>This implementation calls {@code removeRange(0, size())}.
	 * <p>
	 * <p>Note that this implementation throws an
	 * {@code UnsupportedOperationException} unless {@code remove(int
	 * index)} or {@code removeRange(int fromIndex, int toIndex)} is
	 * overridden.
	 *
	 * @throws UnsupportedOperationException if the {@code clear} operation
	 *                                       is not supported by this list
	 */
	@Override
	public void clear() {
		super.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation gets an iterator over the specified collection
	 * and iterates over it, inserting the elements obtained from the
	 * iterator into this list at the appropriate position, one at a time,
	 * using {@code add(int, E)}.
	 * Many implementations will override this method for efficiency.
	 * <p>
	 * <p>Note that this implementation throws an
	 * {@code UnsupportedOperationException} unless
	 * {@link #add(int, Object) add(int, E)} is overridden.
	 *
	 * @param index
	 * @param c
	 * @throws UnsupportedOperationException {@inheritDoc}
	 * @throws ClassCastException            {@inheritDoc}
	 * @throws NullPointerException          {@inheritDoc}
	 * @throws IllegalArgumentException      {@inheritDoc}
	 * @throws IndexOutOfBoundsException     {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return super.addAll(index, c);
	}
	
	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 * <p>
	 * <p>This implementation returns a straightforward implementation of the
	 * iterator interface, relying on the backing list's {@code size()},
	 * {@code get(int)}, and {@code remove(int)} methods.
	 * <p>
	 * <p>Note that the iterator returned by this method will throw an
	 * {@link UnsupportedOperationException} in response to its
	 * {@code remove} method unless the list's {@code remove(int)} method is
	 * overridden.
	 * <p>
	 * <p>This implementation can be made to throw runtime exceptions in the
	 * face of concurrent modification, as described in the specification
	 * for the (protected) {@link #modCount} field.
	 *
	 * @return an iterator over the elements in this list in proper sequence
	 */
	@Override
	public Iterator<T> iterator() {
		return super.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns {@code listIterator(0)}.
	 *
	 * @see #listIterator(int)
	 */
	@Override
	public ListIterator<T> listIterator() {
		return super.listIterator();
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns a straightforward implementation of the
	 * {@code ListIterator} interface that extends the implementation of the
	 * {@code Iterator} interface returned by the {@code iterator()} method.
	 * The {@code ListIterator} implementation relies on the backing list's
	 * {@code get(int)}, {@code set(int, E)}, {@code add(int, E)}
	 * and {@code remove(int)} methods.
	 * <p>
	 * <p>Note that the list iterator returned by this implementation will
	 * throw an {@link UnsupportedOperationException} in response to its
	 * {@code remove}, {@code set} and {@code add} methods unless the
	 * list's {@code remove(int)}, {@code set(int, E)}, and
	 * {@code add(int, E)} methods are overridden.
	 * <p>
	 * <p>This implementation can be made to throw runtime exceptions in the
	 * face of concurrent modification, as described in the specification for
	 * the (protected) {@link #modCount} field.
	 *
	 * @param index
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	@Override
	public ListIterator<T> listIterator(int index) {
		return super.listIterator(index);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns a list that subclasses
	 * {@code AbstractList}.  The subclass stores, in private fields, the
	 * offset of the subList within the backing list, the size of the subList
	 * (which can change over its lifetime), and the expected
	 * {@code modCount} value of the backing list.  There are two variants
	 * of the subclass, one of which implements {@code RandomAccess}.
	 * If this list implements {@code RandomAccess} the returned list will
	 * be an instance of the subclass that implements {@code RandomAccess}.
	 * <p>
	 * <p>The subclass's {@code set(int, E)}, {@code get(int)},
	 * {@code add(int, E)}, {@code remove(int)}, {@code addAll(int,
	 * Collection)} and {@code removeRange(int, int)} methods all
	 * delegate to the corresponding methods on the backing abstract list,
	 * after bounds-checking the index and adjusting for the offset.  The
	 * {@code addAll(Collection c)} method merely returns {@code addAll(size,
	 * c)}.
	 * <p>
	 * <p>The {@code listIterator(int)} method returns a "wrapper object"
	 * over a list iterator on the backing list, which is created with the
	 * corresponding method on the backing list.  The {@code iterator} method
	 * merely returns {@code listIterator()}, and the {@code size} method
	 * merely returns the subclass's {@code size} field.
	 * <p>
	 * <p>All methods first check to see if the actual {@code modCount} of
	 * the backing list is equal to its expected value, and throw a
	 * {@code ConcurrentModificationException} if it is not.
	 *
	 * @param fromIndex
	 * @param toIndex
	 * @throws IndexOutOfBoundsException if an endpoint index value is out of range
	 *                                   {@code (fromIndex < 0 || toIndex > size)}
	 * @throws IllegalArgumentException  if the endpoint indices are out of order
	 *                                   {@code (fromIndex > toIndex)}
	 */
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return super.subList(fromIndex, toIndex);
	}
	
	/**
	 * Compares the specified object with this list for equality.  Returns
	 * {@code true} if and only if the specified object is also a list, both
	 * lists have the same size, and all corresponding pairs of elements in
	 * the two lists are <i>equal</i>.  (Two elements {@code e1} and
	 * {@code e2} are <i>equal</i> if {@code (e1==null ? e2==null :
	 * e1.equals(e2))}.)  In other words, two lists are defined to be
	 * equal if they contain the same elements in the same order.<p>
	 * <p>
	 * This implementation first checks if the specified object is this
	 * list. If so, it returns {@code true}; if not, it checks if the
	 * specified object is a list. If not, it returns {@code false}; if so,
	 * it iterates over both lists, comparing corresponding pairs of elements.
	 * If any comparison returns {@code false}, this method returns
	 * {@code false}.  If either iterator runs out of elements before the
	 * other it returns {@code false} (as the lists are of unequal length);
	 * otherwise it returns {@code true} when the iterations complete.
	 *
	 * @param o the object to be compared for equality with this list
	 * @return {@code true} if the specified object is equal to this list
	 */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
	
	/**
	 * Returns the hash code value for this list.
	 * <p>
	 * <p>This implementation uses exactly the code that is used to define the
	 * list hash function in the documentation for the {@link List#hashCode}
	 * method.
	 *
	 * @return the hash code value for this list
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	/**
	 * Removes from this list all of the elements whose index is between
	 * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
	 * Shifts any succeeding elements to the left (reduces their index).
	 * This call shortens the list by {@code (toIndex - fromIndex)} elements.
	 * (If {@code toIndex==fromIndex}, this operation has no effect.)
	 * <p>
	 * <p>This method is called by the {@code clear} operation on this list
	 * and its subLists.  Overriding this method to take advantage of
	 * the internals of the list implementation can <i>substantially</i>
	 * improve the performance of the {@code clear} operation on this list
	 * and its subLists.
	 * <p>
	 * <p>This implementation gets a list iterator positioned before
	 * {@code fromIndex}, and repeatedly calls {@code ListIterator.next}
	 * followed by {@code ListIterator.remove} until the entire range has
	 * been removed.  <b>Note: if {@code ListIterator.remove} requires linear
	 * time, this implementation requires quadratic time.</b>
	 *
	 * @param fromIndex index of first element to be removed
	 * @param toIndex   index after last element to be removed
	 */
	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * <p>This implementation returns <tt>size() == 0</tt>.
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty();
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
	public boolean contains(Object o) {
		return super.contains(o);
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
	public Object[] toArray() {
		return super.toArray();
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
	public <T1> T1[] toArray(T1[] a) {
		return super.toArray(a);
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
		return super.remove(o);
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
		return super.containsAll(c);
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
		return super.addAll(c);
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
		return super.removeAll(c);
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
		return super.retainAll(c);
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
		return super.toString();
	}
}
