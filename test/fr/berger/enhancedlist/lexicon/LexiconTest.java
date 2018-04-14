package fr.berger.enhancedlist.lexicon;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.ListUtil;
import fr.berger.enhancedlist.exceptions.EmptyListException;
import fr.berger.enhancedlist.lexicon.eventhandlers.AddHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.GetHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.RemoveHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.SetHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.math.BigInteger;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LexiconTest implements Observer {
	
	private Lexicon<Integer> ints;
	
	/* For the following variables, please see test_handlers */
	private boolean add;
	private boolean get;
	private boolean set;
	private boolean remove;
	private int update;
	
	/* TESTS */
	
	@BeforeEach
	void setup() {
		ints = new Lexicon<>(Integer.class, 10);
		ints.add(0);
		ints.add(1);
		ints.add(2);
		ints.add(3);
		ints.add(4);
		ints.add(5);
		ints.addAll(6, 7, 8);
		
		ArrayList<Integer> arrayList = new ArrayList<>(2);
		arrayList.add(9);
		arrayList.add(10);
		
		ints.addAll(arrayList);
		
		add = false;
		get = false;
		set = false;
		remove = false;
		update = 0;
	}
	
	@AfterEach
	void tearDown() {
		System.out.println();
	}
	
	@Test
	void test_gettingElements() {
		/* Test normal gets */
		for (int i = 0; i < ints.size(); i++) {
			System.out.println("test_gettingElements> ints[" + i + "] = " + ints.get(i).toString());
			Assertions.assertEquals(i, ints.get(i).intValue());
		}
		
		Assertions.assertEquals(0, ints.first().intValue());
		Assertions.assertEquals(ints.size() - 1, ints.last().intValue());
		
		/* Test 'defaultValue' gets */
		System.out.println();
		for (int i = -5; i <= ints.size() + 10; i++) {
			Integer element = ints.get(i, null);
			
			System.out.println("test_gettingElements> ints[" + i + "] = " + (element != null ? element.toString() : "(null)"));
			if (ListUtil.checkIndex(i, ints))
				Assertions.assertEquals(i, element.intValue());
			else
				Assertions.assertNull(element);
		}
		
		Assertions.assertEquals(0, ints.first(Integer.MAX_VALUE).intValue());
		Assertions.assertEquals(ints.size() - 1, ints.last(Integer.MIN_VALUE).intValue());
		
		/* Test exceptions */
		try {
			ints.get(11);
			
			Assertions.fail("test_gettingElements> Lexicon should have throw exception.");
		} catch (IndexOutOfBoundsException ignored) {
			System.out.println("test_gettingElements> IndexOutOfBoundsException caught");
		}
		
		try {
			ints.get(-1);
			
			Assertions.fail("test_gettingElements> Lexicon should have throw exception.");
		} catch (IndexOutOfBoundsException ignored) {
			System.out.println("test_gettingElements> IndexOutOfBoundsException caught");
		}
		
		Lexicon<Object> list = new Lexicon<>(Object.class);
		String defaultValue = "This is the default value if the list is empty.";
		
		Assertions.assertEquals(defaultValue, list.first(defaultValue));
		Assertions.assertEquals(0, list.first(0));
		Assertions.assertNull(list.first(null));
		
		Assertions.assertEquals(defaultValue, list.last(defaultValue));
		Assertions.assertEquals(0, list.last(0));
		Assertions.assertNull(list.last(null));
		
		try {
			list.first();
			Assertions.fail("test_gettingElements> Lexicon should have throw exception.");
		} catch (EmptyListException ignored) {
			System.out.println("test_gettingElements> EmptyListException caught");
		}
		
		try {
			list.last();
			Assertions.fail("test_gettingElements> Lexicon should have throw exception.");
		} catch (EmptyListException ignored) {
			System.out.println("test_gettingElements> EmptyListException caught");
		}
	}
	
	@Test
	void test_autoSetClass() {
		/* Test with class */
		Lexicon<Float> floats = new Lexicon<>();
		
		System.out.println("test_autoSetClass> floats.getClazz() : " + Objects.toString(floats.getClazz()));
		
		floats.add(0.0f);
		floats.add(1.0f);
		floats.add(2.0f);
		floats.add(3.0f);
		
		System.out.println("test_autoSetClass> floats.getClazz() : " + Objects.toString(floats.getClazz()));
		
		for (int i = 0; i < floats.size(); i++) {
			System.out.println("test_autoSetClass> floats[" + i + "] = " + floats.get(i).toString());
			Assertions.assertEquals(i, (int) Math.round(floats.get(i)));
		}
		
		/* Test without class */
		Lexicon<Date> dates = new Lexicon<>();
		// Here, add a null element. 'dates' has no information about the class of the generic type 'Date'
		boolean returnedValue = dates.add(null);
		System.out.println("test_autoSetClass> returned value of dates.add(null): " + returnedValue);
		Assertions.assertTrue(returnedValue);
		Assertions.assertEquals(1, dates.size());
		Assertions.assertNull(dates.get(0));
	}
	
	@Test
	void test_nullElement() {
		// Check if objs can accept null value when its parameter is set to false
		Lexicon<Object> objs = new Lexicon<>();
		objs.setAcceptNullValues(false);
		
		objs.add(new Object());
		objs.add(null);
		objs.add(null);
		objs.add(new Object());
		objs.add(null);
		
		System.out.println("test_nullElement> objs.size() " + objs.size());
		Assertions.assertEquals(2, objs.size());
		
		// Check if objs can delete null values
		objs = new Lexicon<>();
		objs.setAcceptNullValues(true);
		
		objs.add(new Object());
		objs.add(null);
		objs.add(null);
		objs.add(new Object());
		objs.add(null);
		
		System.out.println("test_nullElement> objs : " + objs.toString());
		System.out.println("test_nullElement> objs.isThereNullElement() : " + objs.isThereNullElement());
		Assertions.assertTrue(objs.isThereNullElement());
		System.out.println("test_nullElement> objs.findNullElements().size() : " + objs.findNullElements().size());
		Assertions.assertEquals(3, objs.findNullElements().size());
		System.out.println("test_nullElement> objs.findNullElements() : " + objs.findNullElements().toString());
		Assertions.assertEquals(1, objs.findNullElements().get(0).intValue());
		Assertions.assertEquals(2, objs.findNullElements().get(1).intValue());
		Assertions.assertEquals(4, objs.findNullElements().get(2).intValue());
		objs.deleteNullElement();
		System.out.println("test_nullElement> objs.deleteNullElement() : " + objs.toString());
		
		System.out.println("test_nullElement> objs.size() " + objs.size());
		Assertions.assertEquals(2, objs.size());
	}
	
	@Test
	void test_duplicatedElement() {
		// Check if objs can accept duplicated value when its parameter is set to false
		Lexicon<Integer> ints = new Lexicon<>();
		ints.setAcceptDuplicates(false);
		
		ints.add(1);
		ints.add(null);
		ints.add(2);
		ints.add(1);
		ints.add(3);
		
		System.out.println("test_duplicatedElement> ints : " + ints);
		Assertions.assertEquals(4, ints.size());
		
		// Check if objs can accept duplicated value and duplicated null values when its parameter is set to false
		ints = new Lexicon<>();
		ints.setAcceptDuplicates(false);
		
		ints.add(1);
		ints.add(null);
		ints.add(2);
		ints.add(1);
		ints.add(null);
		ints.add(3);
		
		System.out.println("test_duplicatedElement> ints : " + ints);
		Assertions.assertEquals(4, ints.size());
		
		// Check if objs can delete duplicated values
		ints = new Lexicon<>();
		ints.setAcceptDuplicates(true);
		
		ints.add(1);
		ints.add(null);
		ints.add(2);
		ints.add(1);
		ints.add(null);
		ints.add(3);
		
		System.out.println("test_duplicatedElement> ints : " + ints.toString());
		System.out.println("test_duplicatedElement> ints.isThereNullElement() : " + ints.isThereDuplicates());
		Assertions.assertTrue(ints.isThereDuplicates());
		System.out.println("test_duplicatedElement> ints.findDuplications().size() : " + ints.findDuplications().size());
		Assertions.assertEquals(2, ints.findDuplications().size());
		System.out.println("test_duplicatedElement> ints.findDuplications() : " + ints.findDuplications().toString());
		Assertions.assertEquals(new Couple<Integer, Integer>(0, 3), ints.findDuplications().get(0));
		Assertions.assertEquals(new Couple<Integer, Integer>(1, 4), ints.findDuplications().get(1));
		ints.deleteDuplications();
		System.out.println("test_duplicatedElement> ints.deleteDuplications() : " + ints.toString());
		
		System.out.println("test_nullElement> ints.size() " + ints.size());
		Assertions.assertEquals(4, ints.size());
	}
	
	@Test
	void test_remove() {
		System.out.println("LexiconTest.test_remove> Delete by index");
		for (int i = 0; !ints.isEmpty(); i++) {
			ints.remove(0);
			
			System.out.println("LexiconTest.test_remove> ints: " + ints.toString());
			for (int j = 0; j < ints.size(); j++)
				Assertions.assertEquals(j + 1 + i, ints.get(j).intValue());
		}
		
		// Now, restart but this time, delete by object
		setup();
		System.out.println("LexiconTest.test_remove> Delete by object");
		for (int i = 0; !ints.isEmpty(); i++) {
			ints.remove((Integer) i); // cast is necessary to call remove(Object) instead of remove(int)
			
			System.out.println("LexiconTest.test_remove> ints: " + ints.toString());
			for (int j = 0; j < ints.size(); j++)
				Assertions.assertEquals(j + 1 + i, ints.get(j).intValue());
		}
	}
	
	@Test
	void test_addRecursiveType() {
		Lexicon<LexiconTest> rec1 = new Lexicon<>();
		Lexicon<LexiconTest> rec2 = new Lexicon<>(LexiconTest.class);
		
		rec1.add(new LexiconTest());
		rec1.add(new LexiconTest());
		
		rec2.add(this);
		
		Assertions.assertEquals(2, rec1.size());
		Assertions.assertEquals(1, rec2.size());
	}
	
	@Test
	void test_addLot() {
		Lexicon<Integer> counter = new Lexicon<>();
		int max = 1 << 10; // 2^10
		
		for (int i = 0; i < max; i++) {
			counter.add(i);
		}
		
		System.out.println("test_addLot()> counter.size() : " + counter.size());
		System.out.println("test_addLot()> counter.capacity() : " + counter.capacity());
		Assertions.assertEquals(max, counter.size());
	}
	
	@Test
	void test_capacity() {
		// Create a list with a capacity of 100 elements (now, all elements in that array are 'null')
		int cap = 100;
		Lexicon<Integer> list = new Lexicon<>(int.class, cap);
		
		Assertions.assertEquals(cap, list.capacity());
		
		// Add few elements in list
		int s = 3;
		for (int i = 0; i < s; i++)
			list.add(i);
		
		// Now, only 3 out of 100 cases are filled in the array, all the other values are 'null'
		Assertions.assertEquals(cap, list.capacity());
		
		for (int i = 0; i < 3; i++)
			Assertions.assertEquals(i, (int) list.get(i));
		
		// Trim the capacity
		int newCap = list.trimToSize();
		
		Assertions.assertEquals(newCap, list.capacity());
		Assertions.assertEquals(newCap, s);
	}
	
	@Test
	void test_swap() {
		ints.swap(0, 10);
		Assertions.assertEquals(10, (int) ints.get(0));
		Assertions.assertEquals(0,  (int) ints.get(10));
	}
	
	@Test
	void test_handlers() {
		ints.addAddHandler(new AddHandler<Integer>() {
			@Override
			public void onElementAdded(int index, Integer element) {
				System.out.println("LexiconTest.test_handlers> Element \"" + element.toString() + "\" added at n°" + index + '.');
				add = true;
			}
		});
		ints.addGetHandler(new GetHandler<Integer>() {
			@Override
			public void onElementGotten(int index, Integer element) {
				System.out.println("LexiconTest.test_handlers> Element \"" + element.toString() + "\" gotten at n°" + index + '.');
				get = true;
			}
		});
		ints.addSetHandler(new SetHandler<Integer>() {
			@Override
			public void onElementSet(int index, Integer element) {
				System.out.println("LexiconTest.test_handlers> Element \"" + element.toString() + "\" set at n°" + index + '.');
				set = true;
			}
		});
		ints.addRemoveHandler(new RemoveHandler<Integer>() {
			@Override
			public void onElementRemoved(int index, Integer element) {
				System.out.println("LexiconTest.test_handlers> Element \"" + element.toString() + "\" removed at n°" + index + '.');
				remove = true;
			}
		});
		
		ints.addObserver(this);
		
		ints.add(100);
		Assertions.assertTrue(add);
		
		ints.get(0);
		Assertions.assertTrue(get);
		ints.get(11);
		Assertions.assertTrue(get);
		
		ints.set(11, 11);
		Assertions.assertTrue(set);
		
		ints.remove(11);
		Assertions.assertTrue(remove);
		
		System.out.println("LexiconTest.test_handlers> number of update(s) : " + update + '.');
		Assertions.assertTrue(update > 0);
	}
	
	@Test
	void test_serialization() {
		
		String filename = "LexiconTest-test_serialization.bin";
		File file = new File(filename);
		
		// WRITE
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(ints);
			System.out.println("test_serialization> ints serialized.");
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("test_serialization> Could not write the Lexicon ints: " + ints.toString());
		}
		finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw new RuntimeException("test_serialization> Could not close oos");
				}
			}
			
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw new RuntimeException("test_serialization> Could not close fos");
				}
			}
		}
		
		// READ
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			ints = (Lexicon<Integer>) ois.readObject();
			
			System.out.println("test_serialization> De-Serialized ints: " + ints.toString());
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new RuntimeException("test_serialization> Could not read the Lexicon ints: " + ints.toString());
		}
		finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw new RuntimeException("test_serialization> Could not close ois");
				}
			}
			
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw new RuntimeException("test_serialization> Could not close fis");
				}
			}
		}
		
		// DELETE SERIALIZATION FILE
		if (!file.exists())
			throw new RuntimeException("test_serialization> The serialization file does not exist. Where did it go?");
		
		if (!file.delete())
			System.out.println("\u001B[31mtest_serialization> The serialization file could not be deleted. Please delete it manually\u001B[0m");
	}
	
	@Test
	void test_disarray() {
		//Preemptively
		Assertions.assertTimeout(Duration.ofSeconds(5), new Executable() {
			@Override
			public void execute() throws Throwable {
				Lexicon<Integer> copy = new Lexicon<>(ints);
				System.out.println("LexiconTest.test_disarray> Before disarray: " + ints.toString());
				ints.disarray();
				System.out.println("LexiconTest.test_disarray> After disarray: " + ints.toString());
				Assertions.assertEquals(copy.size(), ints.size());
				
				// STATISTICS
				int numberOfUnchangedElements = 0;
				int numberOfElements = ints.size();
				
				for (int i = 0; i < numberOfElements; i++)
					if (Objects.equals(copy.get(i), ints.get(i)))
						numberOfUnchangedElements++;
				
				double percentageOfUnchangedElements = (((double) numberOfUnchangedElements)/((double) numberOfElements)) * 100.0;
				
				System.out.println("LexiconTest.test_disarray> The percentage of unchanged element is " + percentageOfUnchangedElements + "%.");
				if (percentageOfUnchangedElements >= 25.0)
					Assertions.fail("LexiconTest.test_disarray> The percentage of unchanged elements is too big: " + percentageOfUnchangedElements + "%.");
			}
		});
	}
	
	@Test
	void test_sort() {
		// Ascending Sorting
		ints.disarray();
		ints.sort((a, b) -> a - b);
		
		System.out.println("LexiconTest.test_sort> Ascending sort: " + ints.toString());
		
		for (int i = 0; i < ints.size(); i++)
			Assertions.assertEquals(i, ints.get(i).intValue());
		
		
		// Descending Sorting
		ints.disarray();
		ints.sort((a, b) -> b - a);
		
		System.out.println("LexiconTest.test_sort> Descending sort: " + ints.toString());
		
		for (int i = 0; i < ints.size(); i++)
			Assertions.assertEquals(ints.size() - i - 1, ints.get(i).intValue());
		
		
		// Ascending Parity Sorting
		ints.disarray();
		ints.sort((a, b) -> {
			if (a % 2 == 0) {
				if (b % 2 == 0)
					return a - b;
				else
					return -1;
			}
			else {
				if (b % 2 == 0)
					return 1;
				else
					return a - b;
			}
		});
		
		System.out.println("LexiconTest.test_sort> Ascending Parity sort: " + ints.toString());
		
		int pivot = (int) Math.floor((double)ints.size() / 2.0);
		for (int i = 0; i < pivot; i++)
			Assertions.assertEquals( i * 2, ints.get(i).intValue());
		
		for (int i = pivot + 1; i < ints.size(); i++)
			Assertions.assertEquals( (i - (pivot + 1)) * 2 + 1, ints.get(i).intValue());
		
		// Test with null elements
		Lexicon<Integer> list = new Lexicon<>(Integer.class, 3);
		list.add(2);
		list.add(1);
		list.sort(Integer::compareTo);
	}
	
	/* OVERRIDE */
	
	@Override
	public void update(Observable observable, Object o) {
		System.out.println("LexiconTest.test_handlers> observable \"" + observable.getClass().getSimpleName() + "\" sent the object \"" + Objects.toString(o) + "\".");
		update++;
	}
}