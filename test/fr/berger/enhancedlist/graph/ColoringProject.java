package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.algorithm.ColorInterface;
import fr.berger.enhancedlist.algorithm.DSATUR;
import fr.berger.enhancedlist.algorithm.Greedy;
import fr.berger.enhancedlist.algorithm.WelshPowell;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("Duplicates")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ColoringProject {
	
	// The first one must not be changed, the second must be used in test
	Graph<Object, Object> queen5, q5;
	Graph<Object, Object> queen7, q7;
	Graph<Object, Object> queen9, q9;
	Graph<Object, Object> queen11, q11;
	Graph<Object, Object> queen13, q13;
	Graph<Object, Object> queen15, q15;
	
	// List containing all graphs
	Lexicon<Graph<Object, Object>> queens;
	
	@BeforeAll
	void setupOnce() {
		queen5 = GraphIO.read("res/queen5_5.txt");
		queen7 = GraphIO.read("res/queen7_7.txt");
		queen9 = GraphIO.read("res/queen9_9.txt");
		queen11 = GraphIO.read("res/queen11_11.txt");
		queen13 = GraphIO.read("res/queen13_13.txt");
		queen15 = GraphIO.read("res/queen15_15.txt");
	}
	
	@BeforeEach
	void setup() {
		q5 = queen5;
		q7 = queen7;
		q9 = queen9;
		q11 = queen11;
		q13 = queen13;
		q15 = queen15;
		
		queens = new LexiconBuilder<Graph<Object, Object>>()
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.createLexicon();
		
		queens.addAll(q5, q7, q9, q11, q13, q15);
		
		System.out.println();
	}
	
	@Test
	void WelshPowellAscending() {
		testAlgorithm("WelshPowellAscending", new WelshPowell.WelshPowellAscending());
	}
	
	@Test
	void WelshPowellDescending() {
		testAlgorithm("WelshPowellDescending", new WelshPowell.WelshPowellDescending());
	}
	
	@Test
	void WelshPowellRandom() {
		testAlgorithm("WelshPowellRandom", new WelshPowell.WelshPowellRandom());
	}
	
	@Test
	void DSATUR() {
		testAlgorithm("DSATUR", new DSATUR());
	}
	
	@Test
	void GreedyAscending() {
		testAlgorithm("GreedyAscending", new Greedy.GreedyAscending());
	}
	
	@Test
	void GreedyDescending() {
		testAlgorithm("GreedyDescending", new Greedy.GreedyDescending());
	}
	
	@Test
	void GreedyRandom() {
		testAlgorithm("GreedyRandom", new Greedy.GreedyRandom());
	}
	
	/**
	 * Test the given algorithm with queens by printing the result using the name of the algorithm
	 * @param name The name of the algorithm
	 * @param ci The algorithm
	 * @return The list of chromatic numbers of the queens
	 */
	Lexicon<Long> testAlgorithm(@NotNull String name, @NotNull ColorInterface ci) {
		Lexicon<Long> chromatics = new LexiconBuilder<>(Long.class)
				.setAcceptNullValues(false)
				.setAcceptDuplicates(true)
				.createLexicon();
		
		for (int i = 0, maxi = queens.size(); i < maxi; i++) {
			long start = System.currentTimeMillis();
			queens.get(i).color(ci);
			long stop = System.currentTimeMillis();
			long chroma = queens.get(i).getChromaticNumber();
			chromatics.add(chroma);
			long seconds = 0;
			long elapsed = stop - start;
			if (elapsed >= 1000)
				seconds = Math.round(((double)elapsed) / 1000d);
			System.out.println("ColoringProject." + name + "> ChromaticNumber(" + (2 * i + 5) + "): " + chroma + " (time: " + elapsed + "ms" + (seconds > 0 ? " ≈ " + seconds + "s" : "") + ")");
		}
		
		return chromatics;
	}
}
