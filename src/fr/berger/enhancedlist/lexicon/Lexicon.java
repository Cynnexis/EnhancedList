package fr.berger.enhancedlist.lexicon;

import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.beyondcode.util.Irregular;
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
 * @author Valentin Berger
 */
@SuppressWarnings({"ConstantConditions", "NullableProblems"})
public class Lexicon<T> extends EnhancedObservable implements Collection<T>, Iterable<T>, Serializable, Cloneable {
	
	private static final long serialVersionUID = -8760304915380000309L;
	
	@Nullable
	private T[] array;
	
	@NotNull
	private ArrayList<AddHandler<T>> addHandlers;
	@NotNull
	private ArrayList<GetHandler<T>> getHandlers;
	@NotNull
	private ArrayList<SetHandler<T>> setHandlers;
	@NotNull
	private ArrayList<RemoveHandler<T>> removeHandlers;
	@Nullable
	private Class<T> clazz;
	private boolean acceptDuplicates;
	private boolean acceptNullValues;
	private boolean synchronizedAccess;
	
	private int actualSize;
	
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
		initialize();
		growCapacity(initialCapacity);
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public Lexicon(@NotNull Class<T> clazz) {
		setClazz(clazz);
		initialize();
	}
	@SuppressWarnings({"WeakerAccess", "unused"})
	public <U extends T> Lexicon(@Nullable U element) {
		initialize();
		add(element);
	}
	@SuppressWarnings({"WeakerAccess", "unchecked"})
	public Lexicon(@Nullable Collection<? extends T> elements) {
		initialize();
		if (elements instanceof Lexicon) {
			Lexicon<T> list = (Lexicon<T>) elements;
			setAddHandlers(list.getAddHandlers());
			setGetHandlers(list.getGetHandlers());
			setSetHandlers(list.getSetHandlers());
			setRemoveHandlers(list.getRemoveHandlers());
			setAcceptDuplicates(list.isAcceptDuplicates());
			setAcceptNullValues(list.isAcceptNullValues());
			setSynchronizedAccess(list.isSynchronizedAccess());
			setClazz(list.getClazz());
		}
		addAll(elements);
	}
	@SafeVarargs
	@SuppressWarnings({"WeakerAccess", "unused"})
	public <U extends T> Lexicon(@Nullable U... elements) {
		initialize();
		addAll(elements);
	}
	
	@SuppressWarnings("WeakerAccess")
	protected void initialize() {
		this.actualSize = 0;
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
	
	@SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
	private synchronized void changeArrayType(@Nullable T element) {
		// Get all superclasses of the current class, then of 'element', and search a mutual one
		if (element != null) {
			Class<?> superclass;
			
			// Superclasses of the current class (getClazz())
			ArrayList<Class<?>> superclassesU = new ArrayList<>();
			
			superclass = getClazz().getSuperclass();
			if (superclass != null)
				superclassesU.add(superclass);
			
			while (superclass != Object.class && superclass != null) {
				superclass = superclass.getSuperclass();
				superclassesU.add(superclass);
			}
			
			// Superclasses of 'element'
			ArrayList<Class<?>> superclassesS = new ArrayList<>();
			
			superclass = element.getClass().getSuperclass();
			if (superclass != null)
				superclassesS.add(superclass);
			
			while (superclass != Object.class && superclass != null) {
				superclass = superclass.getSuperclass();
				superclassesS.add(superclass);
			}
			
			// Now, search for a mutual class from 0 to n
			Class<?> commonClass = null;
			for (int u = 0, maxu = superclassesU.size(); u < maxu && commonClass == null; u++)
				for (int s = 0, maxs = superclassesS.size(); s < maxs && commonClass == null; s++)
					if (Objects.equals(superclassesU.get(u), superclassesS.get(s)))
						commonClass = superclassesU.get(u);
			
			//System.out.println("DEBUG: common class:" + commonClass);
			
			// Set the class:
			try {
				setClazz((Class<T>) commonClass);
			} catch (ClassCastException ignored) {
				setClazz((Class<T>) Object.class);
			}
		}
		else
			setClazz((Class<T>) Object.class);
		
		// Now, change the format of the array
		Object[] copy = new Object[size()];
		System.arraycopy(array, 0, copy, 0, size());
		array = (T[]) Array.newInstance(getClazz(), capacity());
		System.arraycopy(copy, 0, array, 0, size());
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
	
	/**
	 * Return a random element in the array from the indexes {@code minIndex} and {@code maxIndex} (both included)
	 * @param minIndex The lower bound of the range of indexes (included)
	 * @param maxIndex The greater bound of the range of indexes (included)
	 * @return A random element in the array from the indexes {@code minIndex} and {@code maxIndex} (both included)
	 */
	@Nullable
	public T getRandom(int minIndex, int maxIndex) {
		ListUtil.checkIndexException(minIndex, this);
		ListUtil.checkIndexException(maxIndex, this);
		
		return get(Irregular.rangeInt(minIndex, true, maxIndex, true));
	}
	/**
	 * Return a random element in the array.
	 * @return A random element in the array.
	 */
	@Nullable
	public T getRandom() {
		return getRandom(0, size() - 1);
	}
	/**
	 * Return a random element in the array from the index {@code minIndex} (included)
	 * @param minIndex The greater bound of the range of indexes (included)
	 * @return A random element in the array from the index minIndex (included)
	 */
	@Nullable
	public T getRandomFrom(int minIndex) {
		return getRandom(minIndex, size() - 1);
	}
	/**
	 * Return a random element in the array from the indexes 0 to {@code maxIndex} (both included)
	 * @param maxIndex The lower bound of the range of indexes (included)
	 * @return A random element in the array from the indexes 0 to {@code maxIndex} (both included)
	 */
	@Nullable
	public T getRandomTo(int maxIndex) {
		return getRandom(0, maxIndex);
	}
	
	/**
	 * Return the first element in the list if it exists, otherwise return {@code defaultValue}.
	 * @param defaultValue The default value to return if the list is empty.
	 * @return The first element in the list if the list is not empty, otherwise return {@code defaultValue}.
	 */
	@Nullable
	public T first(@Nullable T defaultValue) {
		if (size() < 1)
			return defaultValue;
		
		return get(0);
	}
	/**
	 * Return the first element in the list if it exists, otherwise throw EmptyListException.
	 * @return The first element in the list if the list is not empty.
	 * @throws EmptyListException Thrown if the list is empty
	 * @see EmptyListException
	 */
	@Nullable
	public T first() {
		if (size() < 1)
			throw new EmptyListException();
		
		return get(0);
	}
	
	/**
	 * Return the last element in the list if it exists, otherwise return {@code defaultValue}.
	 * @param defaultValue The default value to return if the list is empty.
	 * @return The last element in the list if the list is not empty, otherwise return {@code defaultValue}.
	 */
	public T last(@Nullable T defaultValue) {
		if (size() < 1)
			return defaultValue;
		
		return get(size() - 1);
	}
	/**
	 * Return the last element in the list if it exists, otherwise throw EmptyListException.
	 * @return The last element in the list if the list is not empty.
	 * @throws EmptyListException Thrown if the list is empty
	 * @see EmptyListException
	 */
	public T last() {
		if (size() < 1)
			throw new EmptyListException();
		
		return get(size() - 1);
	}
	
	/**
	 * Set the element {@code element} at {@code index}.
	 * @param index The index where to put {@code element}.
	 * @param element The element to set in the list.
	 * @return The element at {@code index} before {@code element} is set.
	 */
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
		
		if (getClazz() == null && element != null)
			setClazz((Class<T>) element.getClass());
		
		T oldValue = get(index);
		
		try {
			array[index] = element;
		} catch (ArrayStoreException ignored) {
			changeArrayType(element);
		}
		
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
			throw new InstantiationError();
		
		/*
		Imagine we have T as generic parameter, and the class of T has not been given by the user. Therefore, when
		'add' is called, it will get automatically the class of the given element. But let us say that the given element
		is not an instance of T but U, with U a class that inherited from T. Then, 'add' will get U.class. HOWEVER, now
		the user add an instance of S, with S inherited from T. Here, an ArrayStoreException will occur. If that happen,
		the method must change the class by searching a mutual superclass (T in our case)
		 */
		try {
			array[actualSize++] = element;
		} catch (ArrayStoreException ignored) {
			changeArrayType(element);
		}
		
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
	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(@Nullable Collection<? extends T> c) {
		if (c == null)
			return false;
		
		if (c.size() == 0)
			return true;
		
		boolean problem = false;
		
		for (Object object : c) {
			try {
				T t = (T) object;
				
				if (!add(t))
					problem = true;
			} catch (ClassCastException ex) {
				ex.printStackTrace();
				problem = true;
			}
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
	 * If <c>index</c> is in the range of the array, the method set at <c>index</c> the element <c>element</c>.
	 * If <c>index</c> is not in the range, the method add <c>element</c> at the end of the list (and its index will
	 * be <c>size() - 1</c>
	 * @param index The index where to set the element. It can be any value (out of bound, and even negative)
	 * @param element The element to set or add in the list
	 * @return Return the index where the element is placed in the list
	 */
	public int setOrAdd(int index, @Nullable T element) {
		if (!ListUtil.checkIndex(index, this)) {
			add(element);
			return size() - 1;
		}
		else {
			set(index, element);
			return index;
		}
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
	
	public synchronized void shift(int startIndex, int shift, @Nullable T elementToPlaceInGap) {
		ListUtil.checkIndexException(startIndex, this);
		
		if (shift < 1)
			throw new IllegalArgumentException("shift must be greater or equal to 1.");
		
		// Check capacity()
		if (checkCapacity(size() + shift) < size() + shift)
			throw new InstantiationError("Cannot grow the array.");
		
		if (array == null)
			throw new InstantiationError();
		
		System.arraycopy(array, startIndex, array, startIndex + shift, size() - (startIndex));
		this.actualSize += shift;
		
		for (int i = startIndex; i < startIndex + shift; i++)
			set(i, elementToPlaceInGap);
	}
	
	/**
	 * <p>
	 * Insert {@code elements} in the array at index {@code i}.
	 * </p>
	 * <p>
	 * Example:
	 * {@code
	 * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	 * insert(4, -1, -2, -3)
	 * [0, 1, 2, 3, -1, -2, -3, 4, 5, 6, 7, 8, 9, 10]
	 * }
	 * </p>
	 * @param i The index where the insertion will be.
	 * @param elements The elements to insert.
	 */
	public void insertAll(int i, @NotNull Lexicon<T> elements) {
		if (isSynchronizedAccess()) {
			synchronized (this) {
				insertAll_content(i, elements);
			}
		}
		else
			insertAll_content(i, elements);
	}
	/**
	 * <p>
	 * Insert {@code elements} in the array at index {@code i}.
	 * </p>
	 * <p>
	 * Example:
	 * {@code
	 * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	 * insert(4, -1, -2, -3)
	 * [0, 1, 2, 3, -1, -2, -3, 4, 5, 6, 7, 8, 9, 10]
	 * }
	 * </p>
	 * @param i The index where the insertion will be.
	 * @param elements The elements to insert.
	 */
	public void insertAll(int i, @NotNull Collection<T> elements) {
		insertAll(i, new Lexicon<>(elements));
	}
	/**
	 * <p>
	 * Insert {@code elements} in the array at index {@code i}.
	 * </p>
	 * <p>
	 * Example:
	 * {@code
	 * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	 * insert(4, -1, -2, -3)
	 * [0, 1, 2, 3, -1, -2, -3, 4, 5, 6, 7, 8, 9, 10]
	 * }
	 * </p>
	 * @param i The index where the insertion will be.
	 * @param elements The elements to insert.
	 */
	@SuppressWarnings("unchecked")
	public void insertAll(int i, @Nullable T... elements) {
		insertAll(i, new Lexicon<>(elements));
	}
	
	private void insertAll_content(int i, @Nullable Lexicon<T> elements) {
		ListUtil.checkIndexException(i, this);
		
		if (elements == null)
			throw new NullPointerException();
		
		if (elements.isEmpty())
			return;
		
		boolean aNull = isAcceptNullValues();
		boolean aDuplicates = isAcceptDuplicates();
		
		/* The following code is in synchronized because it will change the parameters of the current instance of
		Lexicon, such as acceptNullValues and acceptDuplicates. */
		synchronized (this) {
			setAcceptNullValues(true);
			setAcceptDuplicates(true);
			
			shift(i, elements.size(), null);
			
			// Fill the gap with "elements" values
			for (int k = i; k < i + elements.size(); k++)
				set(k, elements.get(k - i));
			
			setAcceptNullValues(aNull);
			setAcceptDuplicates(aDuplicates);
		}
	}
	
	/**
	 * <p>
	 * Insert {@code element} in the array at index {@code i}.
	 * </p>
	 * <p>
	 * Example:
	 * {@code
	 * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	 * insert(4, -1)
	 * [0, 1, 2, 3, -1, 4, 5, 6, 7, 8, 9, 10]
	 * }
	 * </p>
	 * @param i The index where the insertion will be.
	 * @param element The element to insert.
	 */
	@SuppressWarnings("unchecked")
	public void insert(int i, @Nullable T element) {
		if (element == null)
			throw new NullPointerException();
		
		insertAll(i, element);
	}
	
	/**
	 * The current length of the array.
	 * Warning: the size represents the number of useful elements in the array. The total length of the array is the
	 * capacity (size <= capacity)
	 * @return The current size of the array
	 * @see #capacity()
	 */
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
	 * Switch the elements in the array.
	 */
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings({"NullableProblems", "unchecked"})
	@Nullable
	@Override
	public T[] toArray() {
		if (array == null)
			return null;
		
		if (getClazz() == null)
			throw new ClassFormatError();
		
		T[] arr = (T[]) Array.newInstance(getClazz(), size());
		System.arraycopy(array,0, arr, 0, size());
		
		return arr;
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
		addAll(list);
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
		
		boolean objectNotInListFound = false;
		
		Iterator<?> iterator = c.iterator();
		while (iterator.hasNext() && !objectNotInListFound) {
			Object o = iterator.next();
			
			if(!contains(o))
				objectNotInListFound = true;
		}
		
		return !objectNotInListFound;
	}
	@SafeVarargs
	public final boolean containsAll(@Nullable T... elements) {
		if (elements == null)
			throw new NullPointerException();
		
		return containsAll(Arrays.asList(elements));
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
		
		for (Object o : c) {
			if (!remove(o))
				problem = true;
		}
		
		return !problem;
	}
	@SuppressWarnings({"unchecked", "UnusedReturnValue"})
	public boolean removeAll(@Nullable T... elements) {
		if (elements == null)
			throw new NullPointerException();
		
		return removeAll(Arrays.asList(elements));
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
		
		for (int i = 0; i < size(); i++) {
			if (!c.contains(get(i))) {
				remove(get(i));
				i--;
			}
		}
		
		return true;
	}
	@SuppressWarnings({"unchecked", "UnusedReturnValue"})
	public boolean retainAll(@Nullable T... elements) {
		if (elements == null)
			throw new NullPointerException();
		
		return retainAll(Arrays.asList(elements));
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
	
	@SuppressWarnings({"unused", "UnusedReturnValue"})
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
	
	@SuppressWarnings({"unused", "UnusedReturnValue"})
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
	
	@SuppressWarnings({"unused", "UnusedReturnValue"})
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
	
	@SuppressWarnings({"unused", "UnusedReturnValue"})
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
