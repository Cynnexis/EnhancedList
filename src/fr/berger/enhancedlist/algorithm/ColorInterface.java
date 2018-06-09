package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.graph.Color;
import fr.berger.enhancedlist.graph.Edge;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedHashMap;

public interface ColorInterface {
	
	/**
	 * Map all vertices of {@code graph}.
	 * @param graph The graph to map
	 * @param <V> The vertex type
	 * @param <E> The edge type
	 * @return Return a LinkedHashMap where the keys are the vertices of graph, and the values the color of the given
	 * vertex.
	 */
	default <V, E> LinkedHashMap<Vertex<V>, Color> mapVertices(@NotNull Graph<V, E> graph) {
		throw new NotImplementedException();
	}
	
	/**
	 * Map all edges of {@code graph}.
	 * @param graph The graph to map
	 * @param <V> The vertex type
	 * @param <E> The edge type
	 * @return Return a LinkedHashMap where the keys are the edges of graph, and the values the color of the given
	 * edge.
	 */
	default <V, E> LinkedHashMap<Edge<E>, Color> mapEdges(@NotNull Graph<V, E> graph) {
		throw new NotImplementedException();
	}
}
