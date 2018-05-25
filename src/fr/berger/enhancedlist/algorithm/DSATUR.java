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

public class DSATUR implements ColorInterface {
	
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
		Lexicon<Vertex<V>> toColor = new LexiconBuilder<Vertex<V>>()
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.addAll(graph.getVertices())
				.createLexicon();
		long k = 1;
		
		toColor.sort((v1, v2) -> graph.getDegree(v2) - graph.getDegree(v1));
		
		Vertex<V> x = toColor.first();
		toColor.remove(x);
		colors.put(x, new Color(k));
		
		while (!toColor.isEmpty()) {
			// Choose a vertex where the saturation degree is max
			x = toColor.first();
			for (int i = 0, maxi = graph.getVertices().size(); i < maxi; i++) {
				Vertex<V> vi = graph.getVertices().get(i);
				if (graph.getSaturatedDegree(x) == graph.getSaturatedDegree(vi)) {
					if (graph.getDegree(x) > graph.getDegree(vi))
						x = vi;
				}
				else if (graph.getSaturatedDegree(x) > graph.getSaturatedDegree(vi))
					x = vi;
			}
			
			// Now, x is the best DSAT vertex, or one of the best with the greatest degree in the graph
			toColor.remove(x);
			colors.put(x, new Color(k));
			k++;
		}
		
		return colors;
	}
}
