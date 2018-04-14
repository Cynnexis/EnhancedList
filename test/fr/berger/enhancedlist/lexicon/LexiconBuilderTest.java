package fr.berger.enhancedlist.lexicon;

import fr.berger.enhancedlist.lexicon.eventhandlers.AddHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.GetHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.RemoveHandler;
import fr.berger.enhancedlist.lexicon.eventhandlers.SetHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;

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
	
	@SuppressWarnings("unchecked")
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
				.addAll(0, 1, 2, 3, 4, 5, 6, 7)
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
		Assertions.assertFalse(setHandler);
		Assertions.assertTrue(removeHandler);
		Assertions.assertTrue(hasObserved);
		
		for (int i = 0, max = lexicon.size(); i < max; i++)
			Assertions.assertEquals(i, lexicon.get(i).intValue());
	}
}