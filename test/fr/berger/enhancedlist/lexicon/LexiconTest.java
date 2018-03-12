package fr.berger.enhancedlist.lexicon;

import fr.berger.enhancedlist.Couple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class LexiconTest {
	
	Lexicon<Integer> ints;
	
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
}