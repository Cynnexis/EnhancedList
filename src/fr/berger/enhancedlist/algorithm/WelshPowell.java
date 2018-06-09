package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.graph.Color;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.LinkedHashMap;

public abstract class WelshPowell {
	
	public static class WelshPowellAscending implements ColorInterface {
		@SuppressWarnings("ConstantConditions")
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			if (graph == null)
				throw new NullPointerException();
			
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.sort((v1, v2) -> graph.getDegree(v1) - graph.getDegree(v2));
			
			return WelshPowell.map(graph, L);
		}
	}
	
	public static class WelshPowellDescending implements ColorInterface {
		@SuppressWarnings("ConstantConditions")
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			if (graph == null)
				throw new NullPointerException();
			
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.sort((v1, v2) -> graph.getDegree(v2) - graph.getDegree(v1));
			
			return WelshPowell.map(graph, L);
		}
	}
	
	public static class WelshPowellRandom implements ColorInterface {
		@SuppressWarnings("ConstantConditions")
		@Override
		public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
			if (graph == null)
				throw new NullPointerException();
			
			Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getVertices())
					.createLexicon();
			
			L.disarray();
			
			return WelshPowell.map(graph, L);
		}
	}
	
	private static <V, E> LinkedHashMap<Vertex<V>, Color> map(@NotNull Graph<V, E> graph, @NotNull Lexicon<Vertex<V>> L) {
		LinkedHashMap<Vertex<V>, Color> colors = new LinkedHashMap<>();
		long k = 1;
		
		while (!L.isEmpty()) {
			Vertex<V> x = L.first();
			colors.put(x, new Color(k));
			L.remove(0);
			
			for (Vertex<V> y : L) {
				// Get all 'y' neighbors
				Lexicon<Vertex<V>> neighbors = new LexiconBuilder<Vertex<V>>()
						.setAcceptNullValues(false)
						.setAcceptDuplicates(false)
						.addAll(graph.getNeighbors(y))
						.createLexicon();
				
				// Search if a neighbor of "y" (in "neighbors") has the color "k"
				boolean hasColorK = false;
				
				for (int i = 0, max = neighbors.size(); i < max && !hasColorK; i++)
					if (colors.getOrDefault(neighbors.get(i), new Color(0)).getColorNumber() == k)
						hasColorK = true;
				
				// If none of the neighbors of "y" has the color "k", then "y" get this color
				if (!hasColorK) {
					colors.put(y, new Color(k));
					L.remove(y);
				}
			}
			
			k++;
		}
		
		return colors;
	}
}
