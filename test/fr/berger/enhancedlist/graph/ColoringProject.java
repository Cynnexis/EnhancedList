package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.algorithm.ColorInterface;
import fr.berger.enhancedlist.algorithm.DSATUR;
import fr.berger.enhancedlist.algorithm.Greedy;
import fr.berger.enhancedlist.algorithm.WelshPowell;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;

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
		
		/*queens = new LexiconBuilder<Graph<Object, Object>>()
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.createLexicon();
		
		queens.addAll(queen5, queen7, queen9, queen11, queen13, queen15);*/
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
	
	@Contract(pure = true)
	int getQueenNumber(int indexInQueensList) {
		return 2 * indexInQueensList + 5;
	}
	
	@Test
	void WelshPowellAscending() {
		testAlgorithm(new WelshPowell.WelshPowellAscending(), "WelshPowellAscending");
	}
	
	@Test
	void WelshPowellDescending() {
		testAlgorithm(new WelshPowell.WelshPowellDescending(), "WelshPowellDescending");
	}
	
	@Test
	void WelshPowellRandom() {
		testAlgorithm(new WelshPowell.WelshPowellRandom(), "WelshPowellRandom");
	}
	
	@Test
	void DSATUR() {
		testAlgorithm(new DSATUR(), "DSATUR");
	}
	
	@Test
	void GreedyAscending() {
		testAlgorithm(new Greedy.GreedyAscending(), "GreedyAscending");
	}
	
	@Test
	void GreedyDescending() {
		testAlgorithm(new Greedy.GreedyDescending(), "GreedyDescending");
	}
	
	@Test
	void GreedyRandom() {
		testAlgorithm(new Greedy.GreedyRandom(), "GreedyRandom");
	}
	
	/**
	 * Test the given algorithm with queens by printing the result using the name of the algorithm
	 * @param name The name of the algorithm
	 * @param ci The algorithm
	 * @return A LinkedHashMap which map for each graph (queens, with their number) their chromatic number as X and their time in
	 * milliseconds as Y
	 */
	LinkedHashMap<Integer, Couple<Long, Long>> testAlgorithm(@NotNull ColorInterface ci, @Nullable String name) {
		LinkedHashMap<Integer, Couple<Long, Long>> result = new LinkedHashMap<>();
		
		for (int i = 0, maxi = queens.size(); i < maxi; i++) {
			long start = System.currentTimeMillis();
			queens.get(i).color(ci);
			long stop = System.currentTimeMillis();
			long elapsed = stop - start;
			long chroma = queens.get(i).getChromaticNumber();
			result.put(getQueenNumber(i), new Couple<>(chroma, elapsed));
			
			// Print
			if (name != null) {
				long seconds = 0;
				if (elapsed >= 1000)
					seconds = Math.round(((double)elapsed) / 1000d);
				System.out.println("ColoringProject." + name + "> ChromaticNumber(" + (2 * i + 5) + "): " + chroma + " (time: " + elapsed + "ms" + (seconds > 0 ? " ≈ " + seconds + "s" : "") + ")");
			}
		}
		
		return result;
	}
	LinkedHashMap<Integer, Couple<Long, Long>> testAlgorithm(@NotNull ColorInterface ci) {
		return testAlgorithm(ci, null);
	}
	
	/**
	 * Write the results of the algorithm in 2 files.
	 * @param filename The path and filename fo the files. Do not add the extension
	 * @throws Exception It's a test!
	 */
	void writeCSV(@NotNull String filename) throws Exception {
		long start, stop, elapsed;
		
		start = System.currentTimeMillis();
		
		/* OPEN FILES */
		
		String filename1 = filename + "_ChromaticNumber.csv";
		String filename2 = filename + "_Time.csv";
		
		File f1 = new File(filename1);
		File f2 = new File(filename2);
		
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(f1));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(f2));
		
		/* COMPUTE DATA */
		Lexicon<ColorInterface> algorithms = new LexiconBuilder<>(ColorInterface.class)
				.setAcceptNullValues(false)
				.setAcceptDuplicates(false)
				.addAll(new WelshPowell.WelshPowellAscending(), new WelshPowell.WelshPowellDescending(), new WelshPowell.WelshPowellRandom())
				.addAll(new Greedy.GreedyAscending(), new Greedy.GreedyDescending(), new Greedy.GreedyRandom())
				.addAll(new DSATUR())
				.createLexicon();
		
		/**
		 * data map an algorithm to another LinkedHashMap which map for a given graph the chromatic number and the time
		 *
		 * data[algorithm] -> result[graph] -> (chromatic_number ; time_ms)
		 */
		LinkedHashMap<ColorInterface, LinkedHashMap<Integer, Couple<Long, Long>>> data = new LinkedHashMap<>();
		
		/* REAL */
		for (ColorInterface algorithm : algorithms) {
			System.out.println("Computing " + algorithm.toString() + "...");
			data.put(algorithm, testAlgorithm(algorithm));
			System.out.println("Done.");
		}
		/* MOCK *
		for (ColorInterface algorithm : algorithms) {
			LinkedHashMap<Integer, Couple<Long, Long>> result = new LinkedHashMap<>();
			
			for (int i = 0, maxi = queens.size(); i < maxi; i++)
				result.put(getQueenNumber(i), new Couple<>(-1L, -1L));
			
			data.put(algorithm, result);
		}*/
		
		/* CHROMATIC NUMBER */
		
		StringBuilder build1 = new StringBuilder();
		
		// Write header
		build1.append("Chromatic Number,WelshPowell,,,Greddy,,,DSATUR\n")
				.append(",Ascending,Descending,Random,Ascending,Descending,Random\n");
		
		// Write data
		for (int i = 0, maxi = queens.size(); i < maxi; i++) {
			Graph<Object, Object> queen = queens.get(i);
			
			// Compute the number of the queen
			int number = 2 * i + 5;
			
			// Write the name of the graph
			build1.append("\"queen").append(number).append("_").append(number).append(".txt\",");
			
			// Write the results of the algorithm
			for (ColorInterface algorithm : algorithms) {
				try {
					build1.append(
							data
									.get(algorithm)
									.get(getQueenNumber(i))
									.getX())
							.append(',');
				} catch (NullPointerException ex) {
					ex.printStackTrace();
					build1.append("(null),");
				}
			}
			
			build1.append('\n');
		}
		
		/* TIME */
		
		StringBuilder build2 = new StringBuilder();
		
		// Write header
		build2.append("Time (ms),WelshPowell,,,Greddy,,,DSATUR\n")
				.append(",Ascending,Descending,Random,Ascending,Descending,Random\n");
		
		// Write data
		for (int i = 0, maxi = queens.size(); i < maxi; i++) {
			Graph<Object, Object> queen = queens.get(i);
			
			// Compute the number of the queen
			int number = 2 * i + 5;
			
			// Write the name of the graph
			build2.append("\"queen").append(number).append("_").append(number).append(".txt\",");
			
			// Write the results of the algorithm
			for (ColorInterface algorithm : algorithms) {
				try {
					build2.append(
							data
									.get(algorithm)
									.get(getQueenNumber(i))
									.getY())
							.append(',');
				} catch (NullPointerException ex) {
					ex.printStackTrace();
					build1.append("(null),");
				}
			}
			
			build2.append('\n');
		}
		
		/* WRITE TO FILE */
		bw1.write(build1.toString());
		bw2.write(build2.toString());
		
		/* CLOSE STREAMS */
		bw1.close();
		bw2.close();
		
		/* CONCLUDE */
		
		stop = System.currentTimeMillis();
		
		elapsed = stop - start;
		long seconds = 0;
		
		if (elapsed >= 1000)
			seconds = Math.round((double)elapsed / 1000d);
		
		System.out.println("ColoringProject.writeCSV> The computing and writing process took " + elapsed + "ms" + (seconds > 0 ? " ≈ " + seconds + "s" : ""));
	}
	
	@Test
	void writeCSV() throws Exception {
		writeCSV("out/output");
	}
}
