package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.graph.Color;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import org.jetbrains.annotations.NotNull;
import sun.awt.image.ImageWatched;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
		
		
		
		// PRINT L /DEBUG\
		/*System.out.println("DSATUR.mapVertices> L = {");
		for (Vertex<V> v : L) {
			System.out.println("\t" + v + " (degree = " + graph.getDegree(v) + ")");
		}
		System.out.println("}");*/
		
		Vertex<V> x = L.first();
		L.remove(x);
		colors.put(x, new Color(k));
		
		while (!L.isEmpty()) {
			// Choose a vertex where the saturation degree is max and the degree is max too
			x = L.first(); // As L is sorted, x has the higher degree (but maybe not the higher saturation degree)
			// Search a vertex with the same (or higher) degree, and a higher saturation degree
			for (int i = 0, maxi = L.size(); i < maxi; i++) {
				Vertex<V> vi = L.get(i);
				if (!Objects.equals(x, vi) && graph.getDegree(x) <= graph.getDegree(vi))
					if (graph.getSaturatedDegree(x) >= graph.getSaturatedDegree(vi))
						x = vi;
			}
			
			// Now, x is the best DSAT vertex, or one of the best with the greatest degree in the graph
			L.remove(x);
			colors.put(x, new Color(k));
			k++;
		}
		
		return colors;
	}
}
