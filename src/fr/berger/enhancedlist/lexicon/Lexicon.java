package fr.berger.enhancedlist.lexicon;

import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.ListUtil;
import fr.berger.enhancedlist.exceptions.EmptyListException;
import fr.berger.enhancedlist.lexicon.eventhandlers.AddHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.GetHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.RemoveHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.SetHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

// http://server2client.com/images/collectionhierarchy.jpg
// https://docs.oracle.com/javase/tutorial/collections/custom-implementations/index.html

/**
 * Lexicon is a list manager, like List, ArrayList and Set. However, Lexicon extends from the Observable pattern and
 * also have event handlers when it is used. Those events are AddHandler, GetHandler, SetHandler and RemoveHandler.
 * Each time one of those events are called, first Lexicon notify all the observers with the element which is added/
 * gotten/set/removed and then it calls all the event handlers for more precision. You can use either the Observer
 * pattern or the handlers to manage the events, both are not required.
 * Lexicon has rules which can be set as you want. By default, Lexicon accepts duplicated and null value, and each time
 * you use the list, it is in an non-synchronized context. However, you can change those settings.
 * @param <T> The type of the object to save in the Lexicon instance
 */
@SuppressWarnings("ConstantConditions")
public class Lexicon<T> extends EnhancedObservable implements Collection<T>, Serializable, Cloneable {
	
	private static final long serialVersionUID = -8760304915380000309L;
	
	@Nullable
	private T[] array;
	
	@NotNull
	private ArrayList<AddHandler<T>> addHandlers = new ArrayList<>();
	@NotNull
	private ArrayList<GetHandler<T>> getHandlers = new ArrayList<>();
	@NotNull
	private ArrayList<SetHandler<T>> setHandlers = new ArrayList<>();
	@NotNull
	private ArrayList<RemoveHandler<T>> removeHandlers = new ArrayList<>();
	@Nullable
	private Class<T> clazz;
	private boolean acceptDuplicates = true;
	private boolean acceptNullValues = true;
	private boolean synchronizedAccess = false;
	
	private int actualSize = 0;
	
	// TODO: Implement a way to make this list persistent (aka: survive after the program exit)
	
	/* CONSTRUCTORS */
	
	/**
	 * Constructs an empty list with the specified initial capacity.
	 * @throws IllegalArgumentException if the specified initial capacity
	 *                                  is negative
	 */
	@SuppressWarnings({"WeakerAccess", "unused"})
	public Lexicon() {
		initialize();
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public Lexicon(@NotNull Class<T> clazz, int initialCapacity) {
		setClazz(clazz);
		growCapacity(initialCapacity);
		initialize();
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public Lexicon(@NotNull Class<T> clazz) {
		setClazz(clazz);
		initialize();
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public <U extends T> Lexicon(@Nullable U element) {
		add(element);
		initialize();
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public Lexicon(@Nullable Collection<? extends T> elements) {
		addAll(elements);
		initialize();
	}
	@SafeVarargs
	@SuppressWarnings({"WeakerAccess", "unused"})
	public <U extends T> Lexicon(@Nullable U... elements) {
		addAll(elements);
		initialize();
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void initialize() {
		setAddHandlers(new ArrayList<>());
		setGetHandlers(new ArrayList<>());
		setSetHandlers(new ArrayList<>());
		setRemoveHandlers(new ArrayList<>());
		setAcceptDuplicates(true);
		setAcceptNullValues(true);
		setSynchronizedAccess(false);
	}
	
	/* METHODS */
	
	@SuppressWarnings("unchecked")
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(array);
		stream.writeObject(addHandlers);
		stream.writeObject(getHandlers);
		stream.writeObject(setHandlers);
		stream.writeObject(removeHandlers);
		stream.writeBoolean(acceptDuplicates);
		stream.writeBoolean(acceptNullValues);
		stream.writeBoolean(synchronizedAccess);
		stream.writeObject(clazz);
		stream.writeInt(actualSize);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		array = (T[]) stream.readObject();
		setAddHandlers((ArrayList<AddHandler<T>>) stream.readObject());
		setGetHandlers((ArrayList<GetHandler<T>>) stream.readObject());
		setSetHandlers((ArrayList<SetHandler<T>>) stream.readObject());
		setRemoveHandlers((ArrayList<RemoveHandler<T>>) stream.readObject());
		setAcceptDuplicates(stream.readBoolean());
		setAcceptNullValues(stream.readBoolean());
		setSynchronizedAccess(stream.readBoolean());
		setClazz((Class<T>) stream.readObject());
		actualSize = stream.readInt();
	}
	
	@SuppressWarnings("WeakerAccess")
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
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public static ArrayList<Integer> findNullElements(@NotNull Lexicon<?> list) {
		if (list == null)
			throw new NullPointerException();
		
		ArrayList<Integer> indexes = new ArrayList<>(0);
		
		for (int i = 0; i < list.size(); i++)
			if (list.get(i) == null)
				indexes.add(i);
		
		return indexes;
	}
	@NotNull
	public ArrayList<Integer> findNullElements() {
		return findNullElements(this);
	}
	
	// Delete Null Elements
	@SuppressWarnings("WeakerAccess")
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
	
	@SuppressWarnings("WeakerAccess")
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
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public static ArrayList<Couple<Integer, Integer>> findDuplications(@NotNull Lexicon<?> list) {
		ArrayList<Couple<Integer, Integer>> counter = new ArrayList<>(0);
		
		for (int i = 0; i < list.size()-1; i++)
			for (int j = i+1; j < list.size(); j++)
				if (Objects.equals(list.get(i), list.get(j)))
					counter.add(new Couple<>(i, j));
		
		return counter;
	}
	@NotNull
	public ArrayList<Couple<Integer, Integer>> findDuplications() {
		return findDuplications(this);
	}
	
	@SuppressWarnings("WeakerAccess")
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
	
	/* CAPACITY METHODS */
	
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	protected int checkCapacity(int newCapacity) {
		if (newCapacity > capacity())
			return growCapacity(newCapacity);
		
		return capacity();
	}
	
	public synchronized int trimToSize() {
		if (size() < capacity()) {
			array = Arrays.copyOf(array, size());
		}
		
		return capacity();
	}
	
	@SuppressWarnings({"WeakerAccess", "unchecked"})
	protected synchronized int growCapacity(int newCapacity) {
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
	@SuppressWarnings("unused")
	protected int growCapacity() {
		return growCapacity(size() + 1);
	}
	
	public int capacity() {
		if (!checkArrayNullity())
			return 0;
		
		return array.length;
	}
	
	/**
	 * Tru to instantiate the array.
	 * As Lexicon has an array of T[], the size of the generic type must be known. To do so, each time the user try
	 * to add a new element, Lexicon fetch the class (through element.getClass()). However, sometimes there is no class
	 * of the element...
	 * @return Return <c>true</c> if the array is not null or has been instantiated, <c>false</c> otherwise.
	 */
	@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unchecked"})
	private synchronized boolean checkArrayNullity() {
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
	
	/* BASIC LIST METHODS */
	
	@SuppressWarnings("WeakerAccess")
	public void checkIndex(int index) {
		if (!(0 <= index && index < size()))
			throw new ArrayIndexOutOfBoundsException();
	}
	
	
	@Nullable
	public T get(int index, @Nullable T defaultValue) {
		if (isSynchronizedAccess()) {
			try {
				synchronized (this) {
					return get_content(index, defaultValue);
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
				return defaultValue;
			}
		}
		else
			return get_content(index, defaultValue);
	}
	@Nullable
	public T get(int index) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return get_content(index);
			}
		}
		else
			return get_content(index);
	}
	@Nullable
	private T get_content(int index, @Nullable T defaultValue) {
		if (!checkArrayNullity())
			return defaultValue;
		
		if (!ListUtil.checkIndex(index, this))
			return defaultValue;
		
		T element;
		try {
			element = array[index];
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return defaultValue;
		}
		
		triggerGetHandlers(index, element);
		return element;
	}
	@Nullable
	private T get_content(int index) {
		if (!checkArrayNullity())
			throw new InstantiationError();
		
		ListUtil.checkIndexException(index, this);
		
		T element = array[index];
		
		triggerGetHandlers(index, element);
		return element;
	}
	
	@Nullable
	public T first(@Nullable T defaultValue) {
		if (size() < 1)
			return defaultValue;
		
		return get(0);
	}
	@Nullable
	public T first() {
		if (size() < 1)
			throw new EmptyListException();
		
		return get(0);
	}
	
	public T last(@Nullable T defaultValue) {
		if (size() < 1)
			return defaultValue;
		
		return get(size() - 1);
	}
	public T last() {
		if (size() < 1)
			throw new EmptyListException();
		
		return get(size() - 1);
	}
	
	@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
	public T set(int index, @Nullable T element) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return set_content(index, element);
			}
		}
		else
			return set_content(index, element);
	}
	@Nullable
	@SuppressWarnings("unchecked")
	private T set_content(int index, @Nullable T element) {
		ListUtil.checkIndexException(index, this);
		
		if (!isAcceptNullValues() && element == null)
			return null;
		
		if (!isAcceptDuplicates() && contains(element))
			return null;
		
		if (getClazz() == null)
			setClazz((Class<T>) element.getClass());
		
		T oldValue = get(index);
		array[index] = element;
		
		snap(element);
		triggerSetHandlers(index, element);
		
		return oldValue;
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
	public boolean add(@Nullable T element) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return add_content(element);
			}
		}
		else
			return add_content(element);
	}
	@SuppressWarnings("unchecked")
	private boolean add_content(@Nullable T element) {
		if (!isAcceptNullValues() && element == null)
			return false;
		
		if (!isAcceptDuplicates() && contains(element))
			return false;
		
		if (getClazz() == null && element != null)
			setClazz((Class<T>) element.getClass());
		
		checkCapacity(size() + 1);
		
		if (array == null)
			return false;
		
		array[actualSize++] = element;
		
		snap(element);
		triggerAddHandlers(size() - 1, element);
		
		return true;
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
	public boolean addAll(@Nullable Collection<? extends T> c) {
		if (c == null)
			return false;
		
		if (c.size() == 0)
			return true;
		
		boolean problem = false;
		
		Object[] objects = c.toArray();
		
		for (Object object : objects) {
			T t = (T) object;
			
			if (!add(t))
				problem = true;
		}
		
		return !problem;
	}
	@SuppressWarnings({"unchecked", "UnusedReturnValue"})
	public boolean addAll(@Nullable T... list) {
		if (list == null)
			return false;
		
		boolean problem = false;
		
		for (T t : list) {
			if (!add(t))
				problem = true;
		}
		
		return !problem;
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
	@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
	public T remove(int index) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return remove_content(index);
			}
		}
		else
			return remove_content(index);
	}
	private T remove_content(int index) {
		ListUtil.checkIndexException(index, this);
		
		T oldValue = get(index);
		
		if (index < size() - 1)
			System.arraycopy(array, index + 1, array, index, size() - 1 - index);
		
		array[--actualSize] = null;
		
		snap(oldValue);
		triggerRemoveHandlers(index, oldValue);
		
		return oldValue;
	}
	
	@SuppressWarnings("unused")
	public void swap(int index1, int index2) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				swap_content(index1, index2);
			}
		}
		else
			swap_content(index1, index2);
	}
	private void swap_content(int index1, int index2) {
		ListUtil.swap(this, index1, index2);
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
	
	public void disarray() {
		ListUtil.disarray(this);
	}
	
	public synchronized void sort(@NotNull Comparator<T> comparator) {
		if (array != null && size() > 0)
			Arrays.sort(array, 0, size(), comparator);
	}
	
	/* OVERRIDES */
	
	/**
	 * Returns an iterator over the elements contained in this collection.
	 *
	 * @return an iterator over the elements contained in this collection
	 */
	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			private int index = 0;
			
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
	 * Search the element in the list, and return the list of indexes where the element has been found in the list.
	 * @param element The element to search
	 * @return The array of indexes where element has been found in the list. If the list is empty, the element is not
	 * in the list.
	 */
	@NotNull
	public ArrayList<Integer> search(@Nullable T element) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return search_content(element);
			}
		}
		else
			return search_content(element);
	}
	/**
	 * Search the element in the list, and return an HashMap where the keys are the elements and the value a list of
	 * indexes where the given element has been found in the list.
	 * @param elements The elements to search
	 * @return The HashMap of the elements and their list of indexes. If the HashMap is empty, then the elements were
	 * not in the list.
	 */
	@NotNull
	public HashMap<T, ArrayList<Integer>> search(@NotNull List<T> elements) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				return search_content(elements);
			}
		}
		else
			return search_content(elements);
	}
	/**
	 * Search the element in the list, and return an HashMap where the keys are the elements and the value a list of
	 * indexes where the given element has been found in the list.
	 * @param elements The elements to search
	 * @return The HashMap of the elements and their list of indexes. If the HashMap is empty, then the elements were
	 * not in the list.
	 */
	@NotNull HashMap<T, ArrayList<Integer>> search(@NotNull T... elements) {
		return search(Arrays.asList(elements));
	}
	
	@NotNull
	private ArrayList<Integer> search_content(@Nullable T element) {
		ArrayList<Integer> indexes = new ArrayList<>();
		
		for (int i = 0; i < size(); i++)
			if (Objects.equals(get(i), element))
				indexes.add(i);
		
		return indexes;
	}
	@NotNull
	private HashMap<T, ArrayList<Integer>> search_content(@NotNull List<T> elements) {
		HashMap<T, ArrayList<Integer>> indexes = new HashMap<>();
		
		for (T element : elements)
			indexes.put(element, search(element));
		
		return indexes;
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
	@SuppressWarnings("NullableProblems")
	@Nullable
	@Override
	public T[] toArray() {
		if (array == null)
			return null;
		
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
	@Nullable
	@SuppressWarnings({"NullableProblems", "unchecked", "SuspiciousSystemArraycopy"})
	@Override
	public <U> U[] toArray(U[] a) {
		if (a.length < size())
			return (U[]) Arrays.copyOf(array, size(), a.getClass());
		
		System.arraycopy(array, 0, a, 0, size());
		if (a.length > size())
			a[size()] = null;
		
		return a;
	}
	
	@NotNull
	public List<T> toList() {
		List<T> list = new ArrayList<>(size());
		list.addAll(this);
		
		return list;
	}
	
	public void fromList(@NotNull List<T> list) {
		clear();
		
		for (int i = 0, max = list.size(); i < max; i++)
			add(list.get(i));
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
	public boolean containsAll(@NotNull Collection<?> c) {
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
	public boolean removeAll(@NotNull Collection<?> c) {
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
	public boolean retainAll(@NotNull Collection<?> c) {
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
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		
		if (this.size() > 0) {
			for (int i = 0; i < this.size(); i++) {
				builder.append(Objects.toString(get(i)));
				
				if (i != this.size() - 1)
					builder.append(", ");
			}
		}
		builder.append(']');
		
		return builder.toString();
	}
	
	/* GETTERS & SETTERS */
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public ArrayList<AddHandler<T>> getAddHandlers() {
		if (addHandlers == null)
			addHandlers = new ArrayList<>();
		
		return addHandlers;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setAddHandlers(@NotNull ArrayList<AddHandler<T>> addHandlers) {
		if (addHandlers == null)
			throw new NullPointerException();
		
		this.addHandlers = addHandlers;
	}
	
	@SuppressWarnings("unused")
	public boolean addAddHandler(@NotNull AddHandler<T> addHandler) {
		if (addHandler == null)
			throw new NullPointerException();
		
		return getAddHandlers().add(addHandler);
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void triggerAddHandlers(int index, T element) {
		for (int i = 0; i < getAddHandlers().size(); i++) {
			if (getAddHandlers().get(i) == null) {
				getAddHandlers().remove(i);
				i--;
			}
			else
				getAddHandlers().get(i).onElementAdded(index, element);
		}
	}
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public ArrayList<GetHandler<T>> getGetHandlers() {
		if (getHandlers == null)
			getHandlers = new ArrayList<>();
		
		return getHandlers;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setGetHandlers(@NotNull ArrayList<GetHandler<T>> getHandlers) {
		if (getHandlers == null)
			throw new NullPointerException();
		
		this.getHandlers = getHandlers;
	}
	
	@SuppressWarnings("unused")
	public boolean addGetHandler(@NotNull GetHandler<T> getHandler) {
		if (getHandler == null)
			throw new NullPointerException();
		
		return getGetHandlers().add(getHandler);
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void triggerGetHandlers(int index, T element) {
		for (int i = 0; i < getGetHandlers().size(); i++) {
			if (getGetHandlers().get(i) == null) {
				getGetHandlers().remove(i);
				i--;
			}
			else
				getGetHandlers().get(i).onElementGotten(index, element);
		}
	}
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public ArrayList<SetHandler<T>> getSetHandlers() {
		if (setHandlers == null)
			setHandlers = new ArrayList<>();
		
		return setHandlers;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setSetHandlers(@NotNull ArrayList<SetHandler<T>> setHandlers) {
		if (setHandlers == null)
			throw new NullPointerException();
		
		this.setHandlers = setHandlers;
	}
	
	@SuppressWarnings("unused")
	public boolean addSetHandler(@NotNull SetHandler<T> setHandler) {
		if (setHandler == null)
			throw new NullPointerException();
		
		return getSetHandlers().add(setHandler);
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void triggerSetHandlers(int index, T element) {
		for (int i = 0; i < getSetHandlers().size(); i++) {
			if (getSetHandlers().get(i) == null) {
				getSetHandlers().remove(i);
				i--;
			}
			else
				getSetHandlers().get(i).onElementSet(index, element);
		}
	}
	
	@NotNull
	@SuppressWarnings("WeakerAccess")
	public ArrayList<RemoveHandler<T>> getRemoveHandlers() {
		if (removeHandlers == null)
			removeHandlers = new ArrayList<>();
		
		return removeHandlers;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setRemoveHandlers(@NotNull ArrayList<RemoveHandler<T>> removeHandlers) {
		if (removeHandlers == null)
			throw new NullPointerException();
		
		this.removeHandlers = removeHandlers;
	}
	
	@SuppressWarnings("unused")
	public boolean addRemoveHandler(@NotNull RemoveHandler<T> removeHandler) {
		if (removeHandler == null)
			throw new NullPointerException();
		
		return getRemoveHandlers().add(removeHandler);
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void triggerRemoveHandlers(int index, T element) {
		for (int i = 0; i < getRemoveHandlers().size(); i++) {
			if (getRemoveHandlers().get(i) == null) {
				getRemoveHandlers().remove(i);
				i--;
			}
			else
				getRemoveHandlers().get(i).onElementRemoved(index, element);
		}
	}
	
	@Nullable
	public Class<T> getClazz() {
		return clazz;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setClazz(@NotNull Class<T> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		
		this.clazz = clazz;
	}
	
	@SuppressWarnings({"WeakerAccess", "BooleanMethodIsAlwaysInverted"})
	public boolean isAcceptDuplicates() {
		return acceptDuplicates;
	}
	
	public void setAcceptDuplicates(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
		
		if (!this.acceptDuplicates)
			deleteDuplications();
	}
	
	@SuppressWarnings({"WeakerAccess", "BooleanMethodIsAlwaysInverted"})
	public boolean isAcceptNullValues() {
		return acceptNullValues;
	}
	
	public void setAcceptNullValues(boolean acceptNullValues) {
		this.acceptNullValues = acceptNullValues;
		
		if (!this.acceptNullValues)
			deleteNullElement();
	}
	
	@SuppressWarnings("WeakerAccess")
	public boolean isSynchronizedAccess() {
		return synchronizedAccess;
	}
	
	@SuppressWarnings("WeakerAccess")
	public void setSynchronizedAccess(boolean synchronizedAccess) {
		this.synchronizedAccess = synchronizedAccess;
	}
}
