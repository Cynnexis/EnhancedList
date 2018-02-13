package fr.berger.enhancedlist.lexicon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class LexiconTest {
	
	Lexicon<Integer> ints;
	
	@BeforeEach
	void setUp() {
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
	void gettingElements() {
		for (int i = 0; i < ints.size(); i++) {
			System.out.println("gettingElements> ints[" + i + "] = " + ints.get(i).toString());
			Assertions.assertEquals(i, ints.get(i).intValue());
		}
	}
}