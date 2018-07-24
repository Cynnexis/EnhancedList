package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.graph.Color;
import fr.berger.enhancedlist.graph.Edge;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Greedy {
	
	public static class GreedyAscending implements ColorInterface {
		
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.sort((v1, v2) -> graph.getDegree(v1) - graph.getDegree(v2));
			
			return Greedy.mapVertices(graph, L);
		}
		
		@Override
		public boolean equals(Object obj) {
			return toString().equals(Objects.toString(obj));
		}
		
		@Override
		public String toString() {
			return "GreedyAscending{}";
		}
	}
	
	public static class GreedyDescending implements ColorInterface {
		
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.sort((v1, v2) -> graph.getDegree(v2) - graph.getDegree(v1));
			
			return Greedy.mapVertices(graph, L);
		}
		
		@Override
		public boolean equals(Object obj) {
			return toString().equals(Objects.toString(obj));
		}
		
		@Override
		public String toString() {
			return "GreedyDescending{}";
		}
	}
	
	public static class GreedyRandom implements ColorInterface {
		
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.disarray();
			
			return Greedy.mapVertices(graph, L);
		}
		
		@Override
		public boolean equals(Object obj) {
			return toString().equals(Objects.toString(obj));
		}
		
		@Override
		public String toString() {
			return "GreedyRandom{}";
		}
	}
	
	private static <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph, @NotNull Lexicon<Vertex<V>> L) {
		LinkedHashMap<Vertex<V>, Color> colors = new LinkedHashMap<>();
		
		int k = 1;
		
		// All color contained in this list are available
		Lexicon<Color> availableColor = new LexiconBuilder<>(Color.class)
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Vertex<V> vertex : graph.getVertices()) {
			L.add(vertex);
			colors.put(vertex, null);
		}
		
		// Take the first vertex and color it
		Vertex<V> first = L.first();
		L.remove(0);
		colors.put(first, new Color(k));
		k++;
		
		while (!L.isEmpty()) {
			Vertex<V> v = L.first();
			L.remove(0);
			
			// Reset 'availableColor': All colors from 1 to k are available
			for (int i = 1; i <= k; i++)
				availableColor.add(new Color(i));
			
			// Get all neighbors of 'v'
			Lexicon<Vertex<V>> neighbors = new LexiconBuilder<Vertex<V>>()
					.setAcceptNullValues(false)
					.setAcceptDuplicates(false)
					.addAll(graph.getNeighbors(v))
					.createLexicon();
			
			// Mark all neighbors colors as not available
			for (Vertex<V> neighbor : neighbors)
				availableColor.remove(colors.getOrDefault(neighbor, new Color(-1)));
			
			// Find the lowest color in 'availableColor'
			availableColor.sort((o1, o2) -> {
				if (o1 == null && o2 != null)
					return -1;
				else if (o1 != null && o2 == null)
					return 1;
				else if (o1 == null && o2 == null)
					return 0;
				else
					return o1.compareTo(o2);
			});
			
			// Get the first color (the lowest)
			if (availableColor.size() >= 1)
				colors.put(v, availableColor.first());
				// If there is no available color, add one
			else {
				k++;
				colors.put(v, new Color(k));
			}
		}
		
		return colors;
	}
}
