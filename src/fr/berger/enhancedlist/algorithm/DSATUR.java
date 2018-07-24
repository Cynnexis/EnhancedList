package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.graph.Color;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;
import sun.awt.image.ImageWatched;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DSATUR implements ColorInterface {
	
	/*
	In case that the algorithm does not work, here some documentation:
	https://github.com/shanthu89/AdvancedAlgorithms/blob/master/Graph.java
	https://fr.wikipedia.org/wiki/DSATUR
	https://pdfs.semanticscholar.org/49f3/dee3335006aed09942ec5f472f278548b6fd.pdf
	https://imada.sdu.dk/~marco/Teaching/AY2014-2015/DM841/Slides/dm841-lec13.pdf
	 */
	
	/**
	 * Map all vertices of {@code graph}.
	 *
	 * @param graph The graph to map
	 * @return Return a LinkedHashMap where the keys are the vertices of graph, and the values the color of the given
	 * vertex.
	 */
	@Override
	public <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
		LinkedHashMap<Vertex<V>, Color> colors = new LinkedHashMap<>();
		Lexicon<Vertex<V>> L = new LexiconBuilder<Vertex<V>>()
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.addAll(graph.getVertices())
				.createLexicon();
		long k = 1;
		
		L.sort((v1, v2) -> graph.getDegree(v2) - graph.getDegree(v1));
		
		Vertex<V> x = L.first();
		L.remove(x);
		colors.put(x, new Color(k));
		
		while (!L.isEmpty()) {
			// Choose a vertex in L where the saturation degree is max. If equality, choose the one with degree is max
			x = L.first();
			for (int i = 1, maxi = L.size(); i < maxi; i++) {
				Vertex<V> vi = L.get(i);
				
				if (!Objects.equals(x, vi)) {
					// If vi has an higher saturated degree, take it
					if (graph.getSaturatedDegree(x, colors) < graph.getSaturatedDegree(vi, colors))
						x = vi;
					// If there is equality, take the one with higher degree
					if (graph.getSaturatedDegree(x, colors) == graph.getSaturatedDegree(vi, colors) && graph.getDegree(x) < graph.getDegree(vi))
						x = vi;
				}
			}
			
			// Now, x is the best DSAT vertex, or one of the best with the greatest degree in the graph
			// Color x with the smallest color according to its neighbors
			
			Lexicon<Color> neighborsColor = new LexiconBuilder<>(Color.class)
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.createLexicon();
			
			Lexicon<Vertex<V>> neighbors = new LexiconBuilder<Vertex<V>>()
					.setAcceptDuplicates(false)
					.setAcceptNullValues(false)
					.addAll(graph.getNeighbors(x))
					.createLexicon();
			
			// Fill neighborsColor
			for (Vertex<V> neighbor : neighbors) {
				if (colors.getOrDefault(neighbor, null) != null)
					neighborsColor.add(colors.get(neighbor));
			}
			
			// Sort neighborsColor
			neighborsColor.sort(new Comparator<Color>() {
				@Override
				public int compare(Color o1, Color o2) {
					return o1.compareTo(o2);
				}
			});
			
			// Search for the smallest available color
			long smallestColor = 0;
			for (long i = 1; i <= k && smallestColor == 0; i++)
				if (!neighborsColor.contains(new Color(i)))
					smallestColor = i;
			
			// If there is no smallest color, add a color
			if (smallestColor == 0) {
				k++;
				smallestColor = k;
			}
			
			// Finally, remove the vertex from the list and color it
			L.remove(x);
			colors.put(x, new Color(smallestColor));
		}
		
		return colors;
	}
	
	@Override
	public boolean equals(Object obj) {
		return toString().equals(Objects.toString(obj));
	}
	
	@Override
	public String toString() {
		return "DSATUR{}";
	}
}
