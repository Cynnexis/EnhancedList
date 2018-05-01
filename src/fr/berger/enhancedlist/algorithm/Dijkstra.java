package fr.berger.enhancedlist.algorithm;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.exceptions.InfiniteLoopException;
import fr.berger.enhancedlist.graph.Graph;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Dijkstra {
	
	/**
	 * Map all the graph using the Dijkstra's algorithm by starting from {@code source}.
	 * @param graph The graph.
	 * @param source The source (vertex) in the graph.
	 * @param <V> Vertex type.
	 * @param <E> Edge type.
	 * @return Return a couple where the x part represent a LinkedHashMap where the keys are all the vertices in
	 * {@code graph} and all the values the distance of the key from {@code source} ; the y part represents a
	 * LinkedHashMap where the keys are all the vertices in {@code graph} and all the values the previous node from
	 * {@code source}
	 * @see Graph
	 * @see Vertex
	 * @see LinkedHashMap
	 * @see Couple
	 * @see #getPath(Graph, Vertex, Vertex)
	 */
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public static <V, E> Couple<LinkedHashMap<Vertex<V>, Long>, LinkedHashMap<Vertex<V>, Vertex<V>>> map(@NotNull Graph<V, E> graph, @NotNull Vertex<V> source) {
		if (graph == null || source == null)
			throw new NullPointerException();
		
		if (!graph.getVertices().contains(source))
			throw new IllegalArgumentException();
		
		long limit = (long) (graph.getM() + Math.pow(graph.getN(), 2) + 100);
		long iteration = 0;
		LinkedHashMap<Vertex<V>, Long> dist = new LinkedHashMap<>();
		LinkedHashMap<Vertex<V>, Vertex<V>> prev = new LinkedHashMap<>();
		Lexicon<Vertex<V>> Q = new Lexicon<>();
		
		for (Vertex<V> vertex : graph.getVertices()) {
			dist.put(vertex, Long.MAX_VALUE);
			prev.put(vertex, null);
			Q.add(vertex);
		}
		
		dist.put(source, 0L);
		
		while (!Q.isEmpty()) {
			Vertex<V> u = Q.first();
			long dist_u = Long.MAX_VALUE;
			
			// Search for u such that min(dist(u)) in Q
			for (Vertex<V> potentialU : Q) {
				long currentDistance;
				if ((currentDistance = dist.getOrDefault(potentialU, Long.MAX_VALUE)) < dist_u) {
					u = potentialU;
					dist_u = currentDistance;
				}
			}
			
			Q.remove(u);
			
			// Building neighbors of "u"
			Lexicon<Vertex<V>> neighbor = new Lexicon<>();
			// Adding all successors
			neighbor.addAll(graph.getSuccessors(u));
			// Adding all predecessors if the graph is not oriented
			if (!graph.isOriented())
				neighbor.addAll(graph.getPredecessors(u));
			
			for (Vertex<V> v : neighbor) {
				long alt = dist.get(u) + graph.getShortestDistanceBetween(u, v);
				
				// If the value of alt overflowed, put it back to Long.MAX_VALUE (equivalent to +oo)
				if (alt < 0)
					alt = Long.MAX_VALUE;
				
				if (alt < dist.get(v)) {
					dist.put(v, alt);
					prev.put(v, u);
				}
			}
			
			iteration++;
			// If the number of iteration is too big, break the loop by clearing Q.
			if (iteration >= limit)
				Q.clear();
		}
		
		return new Couple<>(dist, prev);
	}
	
	/**
	 * Give a path from {@code source} to {@code destination} in {@code graph}.
	 * @param graph The graph.
	 * @param source The vertex "source" in {@code graph}.
	 * @param destination The vertex "destination" in {@code graph}.
	 * @param <V> Vertex type.
	 * @param <E> Edge type.
	 * @return Return a list of vertex from {@code source} to {@code destination}.
	 */
	@SuppressWarnings("ConstantConditions")
	@Nullable
	public static <V, E> Lexicon<Vertex<V>> getPath(@NotNull Graph<V, E> graph, @NotNull Vertex<V> source, @NotNull Vertex<V> destination) {
		if (graph == null || source == null || destination == null)
			throw new NullPointerException();
		
		if (!graph.getVertices().contains(source) || !graph.getVertices().contains(destination))
			throw new IllegalArgumentException();
		
		long limit = (long) (graph.getM() + Math.pow(graph.getN(), 2) + 100);
		long iteration = 0;
		Lexicon<Vertex<V>> vertices = new Lexicon<>();
		LinkedHashMap<Vertex<V>, Vertex<V>> prev = map(graph, source).getY();
		
		// Add all vertices from destination to the source using "prev"
		Vertex<V> x = prev.get(destination);
		while (!Objects.equals(x, source)) {
			vertices.add(x);
			x = prev.get(x);
			
			iteration++;
			// If the number of iteration is too big, break the loop by returning "null"
			if (iteration >= limit)
				return null;
		}
		// Adding the source
		vertices.add(x);
		
		// Reverse the list "vertices" in "path"
		Lexicon<Vertex<V>> path = new Lexicon<>();
		
		/*
		for (int i = 0; i < vertices.size(); i++)
			path.add(null);
		
		// path.size() == vertices.size()
		*/
		
		for (int i = 0; i < vertices.size(); i++)
			path.add(vertices.get(vertices.size() - i - 1));
		
		path.add(destination);
		
		//path.deleteNullElement();
		
		return path;
	}
}
