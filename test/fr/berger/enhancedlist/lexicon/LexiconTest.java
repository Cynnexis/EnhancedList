package fr.berger.enhancedlist.lexicon;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.lexicon.eventhandlers.AddHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class LexiconTest {
	
	Lexicon<Integer> ints;
	
	/* For the following variables, please see test_handlers */
	boolean add = false;
	boolean get = false;
	boolean set = false;
	boolean remove = false;
	
	@BeforeEach
	void setup() {
		ints = new Lexicon<>(Integer.class, 10);
		
		ints.add(0);
		ints.add(1);
		ints.add(2);
		ints.add(3);
		ints.add(4);
		ints.add(5);
		ints.addAll(new Integer[] {6, 7, 8, 9, 10});
	}
	
	@Test
	void test_gettingElements() {
		for (int i = 0; i < ints.size(); i++) {
			System.out.println("test_gettingElements> ints[" + i + "] = " + ints.get(i).toString());
			Assertions.assertEquals(i, ints.get(i).intValue());
		}
	}
	
	@Test
	void test_autoSetClass() {
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
	}
}