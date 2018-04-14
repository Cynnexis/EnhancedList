package fr.berger.enhancedlist.lexicon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexiconBuilderTest {
	
	LexiconBuilder<Integer> intsBuilder;
	
	// Handlers test
	boolean addHandler;
	boolean getHandler;
	boolean setHandler;
	boolean removeHandler;
	boolean hasObserved;
	
	@BeforeEach
	void setup() {
		intsBuilder = new LexiconBuilder<>();
		addHandler = false;
		getHandler = false;
		setHandler = false;
		removeHandler = false;
		hasObserved = false;
	}
	
	//@SuppressWarnings("unchecked")
	@Test
	void test() {
		Lexicon<Integer> lexicon = intsBuilder
				.setAcceptDuplicates(false)
				.setAcceptNullValues(true)
				.setSynchronizedAccess(false)
				.addAddHandlers((index, element) -> addHandler = true)
				.addGetHandlers((index, element) -> getHandler = true)
				.addSetHandlers((index, element) -> setHandler = true)
				.addRemoveHandlers((index, element) -> removeHandler = true)
				.addObserver((observable, o) -> hasObserved = true)
				.addAll(0, 1, 2, 4, 3, 5, 6, 7)
				.swap(3, 4)
				.add(8)
				.setOrAdd(9, 9)
				.setOrAdd(-1, 10)
				.addAll(11, 12)
				.removeAll(11, 12)
				.createLexicon();
		
		System.out.println("LexiconBuilderTest.test> list: " + lexicon.toString());
		
		Assertions.assertEquals(11, lexicon.size());
		Assertions.assertFalse(lexicon.isAcceptDuplicates());
		Assertions.assertTrue(lexicon.isAcceptNullValues());
		Assertions.assertFalse(lexicon.isSynchronizedAccess());
		Assertions.assertEquals(1, lexicon.getAddHandlers().size());
		Assertions.assertEquals(1, lexicon.getGetHandlers().size());
		Assertions.assertEquals(1, lexicon.getSetHandlers().size());
		Assertions.assertEquals(1, lexicon.getRemoveHandlers().size());
		Assertions.assertTrue(addHandler);
		Assertions.assertTrue(getHandler);
		Assertions.assertTrue(setHandler);
		Assertions.assertTrue(removeHandler);
		Assertions.assertTrue(hasObserved);
		
		for (int i = 0, max = lexicon.size(); i < max; i++)
			Assertions.assertEquals(i, lexicon.get(i).intValue());
	}
}