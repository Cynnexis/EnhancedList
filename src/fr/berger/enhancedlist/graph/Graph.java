package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.algorithm.Dijkstra;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.lexicon.LexiconBuilder;
import fr.berger.enhancedlist.matrix.Matrix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public class Graph<V, E> extends EnhancedObservable implements Serializable, Cloneable {
	
	/**
	 * Indicate if the graph is oriented
	 */
	private boolean oriented;
	
	/**
	 * The list of vertices of the graph
	 */
	@NotNull
	private Lexicon<Vertex<V>> vertices;
	
	/**
	 * The list of edges in the graph
	 */
	@NotNull
	private Lexicon<Edge<E>> edges;
	
	/* CONSTRUCTORS */
	
	public Graph(boolean oriented, @NotNull Collection<Vertex<V>> vertices, @NotNull Collection<Edge<E>> edges) {
		setOriented(oriented);
		setVertices(new Lexicon<>(vertices));
		setEdges(new Lexicon<>(edges));
	}
	public Graph(@NotNull Collection<Vertex<V>> vertices, @NotNull Collection<Edge<E>> edges) {
		initOriented();
		setVertices(new Lexicon<>(vertices));
		setEdges(new Lexicon<>(edges));
	}
	public Graph(boolean oriented) {
		setOriented(oriented);
		initVertices();
		initEdges();
	}
	@SuppressWarnings("ConstantConditions")
	public Graph(@NotNull Graph<V, E> graph) {
		if (graph == null)
			throw new NullPointerException();
		
		setOriented(graph.isOriented());
		setVertices(graph.getVertices());
		setEdges(graph.getEdges());
	}
	public Graph() {
		initOriented();
		initVertices();
		initEdges();
	}
	
	/* GRAPH METHODS */
	
	/**
	 * Build a list of every successors of {@code vertex}.
	 * @param vertex The vertex
	 * @return A lexicon containing all the successors of {@code vertex}. If the list is empty, it means that
	 * {@code vertex} is a sink.
	 * @see #getSinks()
	 */
	@SuppressWarnings({"ConstantConditions", "unchecked"})
	@NotNull
	public Lexicon<Vertex<V>> getSuccessors(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException();
		
		Lexicon<Vertex<V>> vertices = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Edge<E> edge : getEdges())
			if (Objects.equals(vertex, edge.getX()))
				vertices.add((Vertex<V>) edge.getY());
		
		return vertices;
	}
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getSuccessors(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getSuccessors(vertex.getElement());
	}
	
	/**
	 * Build a list of every predecessors of {@code vertex}.
	 * @param vertex The vertex
	 * @return A lexicon containing all the predecessors of {@code vertex}. If the list is empty, it means that
	 * {@code vertex} is a source.
	 * @see #getSources()
	 */
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getPredecessors(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException();
		
		Lexicon<Vertex<V>> vertices = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Edge<E> edge : getEdges()) {
			if (Objects.equals(vertex, edge.getY())) {
				//noinspection unchecked
				vertices.add((Vertex<V>) edge.getX());
			}
		}
		
		return vertices;
	}
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getPredecessors(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getPredecessors(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getNeighbors(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(vertex))
			throw new IllegalArgumentException();
		
		return new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.addAll(getSuccessors(vertex))
				.addAll(getPredecessors(vertex))
				.createLexicon();
	}
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getNeighbors(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getNeighbors(vertex.getElement());
	}
	
	/**
	 * Build a list of all the sources in the graph.
	 * @return A list of sources in the graph.
	 * @see #getPredecessors(Vertex)
	 */
	@NotNull
	public Lexicon<Vertex<V>> getSources() {
		Lexicon<Vertex<V>> vertices = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Vertex<V> vertex : getVertices())
			if (getPredecessors(vertex).size() == 0)
				vertices.add(vertex);
		
		return vertices;
	}
	
	/**
	 * Build a list of all the sinks in the graph.
	 * @return A list of sinks in the graph.
	 * @see #getSuccessors(Vertex)
	 */
	@NotNull
	public Lexicon<Vertex<V>> getSinks() {
		Lexicon<Vertex<V>> vertices = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Vertex<V> vertex : getVertices())
			if (getSuccessors(vertex).size() == 0)
				vertices.add(vertex);
		
		return vertices;
	}
	
	/**
	 * Get the inner degree of {@code vertex}, also called d-({@code vertex}).
	 * @param vertex The vertex
	 * @return The inner degree of {@code vertex}
	 */
	@SuppressWarnings("ConstantConditions")
	public int getInDegree(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getPredecessors(vertex).size();
	}
	@SuppressWarnings("ConstantConditions")
	public int getInDegree(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getInDegree(vertex.getElement());
	}
	
	/**
	 * Get the outer degree of {@code vertex}, also called d+({@code vertex}).
	 * @param vertex The vertex
	 * @return The outer degree of {@code vertex}
	 */
	@SuppressWarnings("ConstantConditions")
	public int getOutDegree(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getSuccessors(vertex).size();
	}
	@SuppressWarnings("ConstantConditions")
	public int getOutDegree(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getOutDegree(vertex.getElement());
	}
	
	/**
	 * Get the degree of {@code vertex}, also called d({@code vertex}) = d-({@code vertex}) + d+({@code vertex}).
	 * @param vertex The vertex
	 * @return The degree of {@code vertex}
	 * @see #getInDegree(Vertex)
	 * @see #getOutDegree(Vertex)
	 */
	@SuppressWarnings("ConstantConditions")
	public int getDegree(@NotNull Vertex<V> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getInDegree(vertex) + getOutDegree(vertex);
	}
	@SuppressWarnings("ConstantConditions")
	public int getDegree(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getDegree(vertex.getElement());
	}
	
	/**
	 * Search all edges e such that e = (source, destination) or e = (destination, source) if the graph is not oriented.
	 * @param source The first vertex.
	 * @param destination The second Vertex.
	 * @return Return a list of edges in the graph e = (source, destination) or e = (destination, source) if the graph
	 * is not oriented, or an empty list if no corresponding edge has been found in the graph.
	 */
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Edge<E>> searchAllEdges(@NotNull Vertex<?> source, @NotNull Vertex<?> destination) {
		if (source == null || destination == null)
			throw new NullPointerException();
		
		Lexicon<Edge<E>> edges = new LexiconBuilder<Edge<E>>()
				.setAcceptNullValues(false)
				.setAcceptDuplicates(false)
				.createLexicon();
		for (int i = 0, maxi = getEdges().size(); i < maxi; i++) {
			Edge<E> e = getEdges().get(i);
			
			if (Objects.equals(e.getX(), source) && Objects.equals(e.getY(), destination) || (
					!isOriented() && Objects.equals(e.getY(), source) && Objects.equals(e.getX(), destination)
			))
				edges.add(e);
		}
		
		return edges;
	}
	
	/**
	 * Search the edge e such that e = (source, destination) or e = (destination, source) if the graph is not oriented.
	 * Stop at the first corresponding edge.
	 * @param source The first vertex.
	 * @param destination The second Vertex.
	 * @return Return the edge in the graph e = (source, destination) or e = (destination, source) if the graph is not
	 * oriented, or {@code null} if no corresponding edge has been found in the graph.
	 */
	@SuppressWarnings("ConstantConditions")
	@Nullable
	public Edge<E> searchEdge(@NotNull Vertex<?> source, @NotNull Vertex<?> destination) {
		if (source == null || destination == null)
			throw new NullPointerException();
		
		Edge<E> edge = null;
		for (int i = 0, maxi = getEdges().size(); i < maxi && edge == null; i++) {
			Edge<E> e = getEdges().get(i);
			
			if (Objects.equals(e.getX(), source) && Objects.equals(e.getY(), destination) || (
					!isOriented() && Objects.equals(e.getY(), source) && Objects.equals(e.getX(), destination)
					))
				edge = e;
		}
		
		return edge;
	}
	
	/**
	 * Tell if the vertices {@code v1} and {@code v2} are adjacent (there is at least one edge between them).
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 * @return Return {@code true} if there is at least one edge between them, {@code false} otherwise.
	 */
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Vertex<V> v1, @NotNull Vertex<V> v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(v1) || !getVertices().contains(v2))
			throw new IllegalArgumentException();
		
		boolean adjacent = false;
		for (int i = 0, maxi = getEdges().size(); i < maxi && !adjacent; i++) {
			if (getEdges().get(i) != null && getEdges().get(i).getX() != null) {
				if ((Objects.equals(getEdges().get(i).getX(), v1) &&
						Objects.equals(getEdges().get(i).getY(), v2)) ||
						(Objects.equals(getEdges().get(i).getY(), v1) &&
								Objects.equals(getEdges().get(i).getX(), v2)))
					adjacent = true;
			}
		}
		
		return adjacent;
	}
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Ref<Vertex<V>> v1, @NotNull Ref<Vertex<V>> v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();
		
		return areAdjacent(v1.getElement(), v2.getElement());
	}
	/**
	 * Tell if the edges {@code e1} and {@code e2} are adjacent (they have at least one shared vertex).
	 * @param e1 The first vertex
	 * @param e2 The second vertex
	 * @return Return {@code true} if there is at least one shared vertex, {@code false} otherwise.
	 */
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Edge<E> e1, @NotNull Edge<E> e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		
		if (!getEdges().contains(e1) || !getEdges().contains(e2))
			throw new IllegalArgumentException();
		
		return Objects.equals(e1.getX(), e2.getX()) ||
				Objects.equals(e1.getX(), e2.getY()) ||
				Objects.equals(e1.getY(), e2.getX()) ||
				Objects.equals(e1.getY(), e2.getY());
	}
	
	/**
	 * Tell if the graph is symmetrical.
	 * @return Return {@code true} if the graph is symmetrical, {@code false} otherwise.
	 */
	public boolean isSymmetrical() {
		if (!isOriented())
			return true;
		
		boolean symmetrical = true;
		
		for (int i = 0, maxi = getEdges().size(); i < maxi && symmetrical; i++) {
			Edge<E> edge = getEdges().get(i);
			// "edge" = (i, j)
			
			// Search for an edge "e" such that "e" = (j, i)
			Edge<E> e = searchEdge(edge.getY(), edge.getX());
			if (e == null)
				symmetrical = false;
		}
		
		return symmetrical;
	}
	
	/**
	 * Tell if the graph is antisymmetric.
	 * @return Return {@code true} if the graph is antisymmetric, {@code false} otherwise.
	 */
	public boolean isAntisymmetric() {
		if (!isOriented())
			return true;
		
		boolean antisymmetric = true;
		
		for (int i = 0, maxi = getEdges().size(); i < maxi && antisymmetric; i++) {
			Edge<E> edge = getEdges().get(i);
			// "edge" = (i, j)
			
			// Search for an edge "e" such that "e" = (j, i)
			Edge<E> e = searchEdge(edge.getY(), edge.getX());
			if (e != null)
				antisymmetric = false;
		}
		
		return antisymmetric;
	}
	
	/**
	 * Tell if the graph is transitive.
	 * @return Return {@code true} if the graph is transitive, {@code false} otherwise.
	 */
	public boolean isTransitive() {
		if (!isOriented())
			return true;
		
		for (int i = 0, maxi = getEdges().size(); i < maxi; i++) {
			Edge<E> ei = getEdges().get(i); // (a, b)
			Vertex<?> a = ei.getX();
			Vertex<?> b = ei.getY();
			
			// Search ej such that ej = (b, c) (c ∈ X (vertices) and c ≠ b)
			boolean found_ej = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj; j++) {
				if (i != j) {
					Edge<E> ej = getEdges().get(j); // (b, c)
					Vertex<?> c = ej.getY();
					
					if (Objects.equals(ej.getX(), b) && !Objects.equals(ej.getY(), b) || (
							!isOriented() && Objects.equals(ej.getY(), b) && !Objects.equals(ej.getX(), b)
							)) {
						found_ej = true;
						
						// Search an edge "ek" such that "ek" = (a, c)
						Edge<E> ek = searchEdge(a, c);
						if (ek == null)
							return false;
					}
				}
			}
			
			if (!found_ej)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Tell if the graph is complete.
	 * @return Return {@code true} if the graph is complete, {@code false} otherwise.
	 */
	public boolean isComplete() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			for (int j = 0, maxj = getVertices().size(); j < maxj; j++) {
				if (i != j) {
					Vertex<V> vi = getVertices().get(i);
					Vertex<V> vj = getVertices().get(j);
					
					// Search an edge ek such that ek = ("vi" ; "vj") or ek = ("vj" ; "vi")
					Edge<E> ek = searchEdge(vi, vj);
					if (ek == null) {
						ek = searchEdge(vj, vi);
						
						if (ek == null)
							return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Tell if the graph is reflexive.
	 * @return Return {@code true} if the graph is reflexive, {@code false} otherwise.
	 */
	public boolean isReflexive() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			Vertex<V> vi = getVertices().get(i);
			
			// Search for an edge ej such that ej = (vi, vi)
			Edge<E> ej = searchEdge(vi, vi);
			if (ej == null)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Tell if the graph is anti-reflexive.
	 * @return Return {@code true} if the graph is anti-reflexive, {@code false} otherwise.
	 */
	public boolean isAntiReflexive() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			Vertex<V> vi = getVertices().get(i);
			
			// Search for an edge ej such that ej = (vi, vi)
			Edge<E> ej = searchEdge(vi, vi);
			if (ej != null)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Create the subgraph containing only the vertices in {@code remainingVertices} and the edges between those
	 * vertices.
	 * @param remainingVertices The remaining vertices in the subgraph.
	 * @return Return the subgraph with {@code remainingVertices} as vertices
	 */
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Graph<V, E> getSubgraphRemaining(@NotNull Lexicon<Vertex<V>> remainingVertices) {
		if (remainingVertices == null)
			throw new NullPointerException();
		
		// Check that all vertices are in the current graph
		if (!getVertices().containsAll(remainingVertices))
			throw new IllegalArgumentException();
		
		Lexicon<Edge<E>> remainingEdges = new LexiconBuilder<Edge<E>>()
				.setAcceptNullValues(false)
				.setAcceptDuplicates(true)
				.createLexicon();
		
		for (int i = 0, maxi = remainingVertices.size(); i < maxi; i++) {
			Vertex<V> vi = remainingVertices.get(i);
			
			for (int j = 0, maxj = remainingVertices.size(); j < maxj; j++) {
				if (i != j) {
					Vertex<V> vj = remainingVertices.get(j);
					remainingEdges.addAll(searchAllEdges(vi, vj));
				}
			}
		}
		
		return new Graph<>(isOriented(), remainingVertices, remainingEdges);
	}
	@SafeVarargs
	@NotNull
	public final Graph<V, E> getSubgraphRemaining(@NotNull Vertex<V>... remainingVertices) {
		return getSubgraphRemaining(new Lexicon<>(remainingVertices));
	}
	
	/**
	 * Create the subgraph containing all vertices except those in {@code exceptVertices}.
	 * @param exceptVertices The vertices to NOT include in the subgraph.
	 * @return Return the subgraph without the vertices contain in {@code remainingVertices}
	 */
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Graph<V, E> getSubgraphExcept(@NotNull Lexicon<Vertex<V>> exceptVertices) {
		if (exceptVertices == null)
			throw new NullPointerException();
		
		Lexicon<Vertex<V>> remainingVertices = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.setAcceptDuplicates(false)
				.createLexicon();
		
		for (Vertex<V> v : getVertices())
			if (!exceptVertices.contains(v))
				remainingVertices.add(v);
		
		return getSubgraphRemaining(remainingVertices);
	}
	@SafeVarargs
	@NotNull
	public final Graph<V, E> getSubgraphExcept(@NotNull Vertex<V>... exceptVertices) {
		return getSubgraphExcept(new Lexicon<>(exceptVertices));
	}
	
	/**
	 * Tell if the graph is connected.
	 * @return Return {@code true} if the graph is connected, {@code false} otherwise.
	 */
	public boolean isConnected() {
		boolean connected = true;
		
		for (int i = 0, maxi = getVertices().size(); i < maxi && connected; i++) {
			Vertex<V> vi = getVertices().get(i);
			
			for (int j = 0, maxj = getVertices().size(); j < maxj && connected; j++) {
				
				if (i != j) {
					Vertex<V> vj = getVertices().get(j);
					
					// Search a path between vi and vj
					Path<E> path = getPath(vi, vj, false);
					
					if (path == null)
						connected = false;
				}
			}
		}
		
		return connected;
	}
	
	/**
	 * Tell if the graph is disconnected.
	 * @return Return {@code true} if the graph is disconnected, {@code false} otherwise.
	 */
	public boolean isDisconnected() {
		return !isConnected();
	}
	
	/**
	 * Search every sub-graph in the current graph such that all sub-graphs are connected.
	 * @return Return the sub-graphs. If the graph is connected, return a list containing the graph.
	 */
	// TODO: NOT TESTED
	public Lexicon<Graph<V, E>> getConnectedGraphs() {
		Lexicon<Vertex<V>> remainingVertices = new Lexicon<>();
		// key: any vertex in the current graph ; value: a list of vertices that v can access (connected graph)
		LinkedHashMap<Vertex<V>, Lexicon<Vertex<V>>> connectedTo = new LinkedHashMap<>();
		Lexicon<Graph<V, E>> graphs = new Lexicon<>();
		
		// Initializing "remainingVertices"
		remainingVertices.addAll(getVertices());
		
		// Constructing "connectedTo"
		while (!remainingVertices.isEmpty()) {
			Vertex<V> x = remainingVertices.first();
			
			for (Vertex<V> y : getVertices()) {
				Path<E> path = getPath(x, y);
				if (path != null) {
					Lexicon<Vertex<V>> connections = connectedTo.getOrDefault(x, new Lexicon<>());
					connections.add(y);
					connectedTo.put(x, connections);
				}
			}
			
			remainingVertices.remove(x);
		}
		
		for (Map.Entry<Vertex<V>, Lexicon<Vertex<V>>> entry : connectedTo.entrySet()) {
			Graph<V, E> graph = new Graph<>();
			
			for (Vertex<V> vertex : entry.getValue()) {
				graph.getVertices().add(vertex);
			}
			
			graphs.add(graph);
		}
		
		return graphs;
	}
	
	/**
	 * Search every sub-graph in the current graph such that all sub-graphs are connected.
	 * @return Return the number of connected sub-graphs in the graph. If the graph is connected, return 1.
	 */
	// TODO: NOT TESTED
	// TODO: Found another solution without synchornizing the method and changing Graph instance variable
	public synchronized long getConnectivityDegree() {
		return getConnectedGraphs().size();
	}
	
	/**
	 * Construct the symmetry of the current graph.
	 * @return Return the symmetry of the current graph.
	 */
	// TODO: NOT TESTED
	public Graph<V, E> getSymmetry() {
		Graph<V, E> gSym = new Graph<>(isOriented());
		gSym.setVertices(getVertices());
		
		Lexicon<Edge<E>> edges = new LexiconBuilder<Edge<E>>()
				.setAcceptDuplicates(false)
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Edge<E> e : getEdges())
			edges.add(e.getSymmetry());
		
		gSym.setEdges(edges);
		
		return gSym;
	}
	
	/**
	 * Build a list of all articulation points in the graph.
	 * @return A list of all articulation points in the graph.
	 */
	// TODO: NOT TESTED
	public Lexicon<Vertex<V>> getArticulationPoints() {
		throw new NotImplementedException();
	}
	
	/**
	 * Build a list of all bridges in the graph.
	 * @return A list of all bridges in the graph.
	 */
	// TODO: NOT TESTED
	public Lexicon<Edge<E>> getBridges() {
		throw new NotImplementedException();
	}
	
	/**
	 * Compute a matrix which represents the graph in a mathematical viewpoint.
	 * @return The matrix.
	 */
	// TODO: NOT TESTED
	public Matrix<Integer> toMatrix() {
		Matrix<Integer> matrix = new Matrix<>(getN(), getN(), 0);
		
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			Vertex<V> vertex = getVertices().get(i);
			Lexicon<Vertex<V>> successors = getSuccessors(vertex);
			
			// TODO: Continue...
		}
		
		throw new NotImplementedException();
	}
	
	// TODO: NOT TESTED
	@SuppressWarnings("ConstantConditions")
	public static <V, E> Graph<V, E> fromMatrix(@NotNull Matrix<Integer> matrix) {
		if (matrix == null)
			throw new NullPointerException();
		
		if (matrix.getNbColumns() <= 0 || matrix.getNbColumns() != matrix.getNbRows())
			throw new IllegalArgumentException();
		
		throw new NotImplementedException();
	}
	
	// ALGORITHMS
	
	/**
	 * Compute a path between {@code source} and {@code destination}.
	 * @param source The vertex where the walk begins.
	 * @param destination The vertex where the walk ends.
	 * @param regardingOrientation If true, it will allow the algorithm to go backwards if {@code graph.isConnected()}
	 * 	                           is false. If regardingOrientation is false, the algorithm may go backward.
	 * @return A path between {@code source} and {@code destination}.
	 */
	@SuppressWarnings("ConstantConditions")
	@Nullable
	public Path<E> getPath(@NotNull Vertex<V> source, @NotNull Vertex<V> destination, boolean regardingOrientation) {
		if (source == null || destination == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(source) || !getVertices().contains(destination))
			throw new IllegalArgumentException();
		
		Lexicon<Vertex<V>> vertices = Dijkstra.getPath(this, source, destination, regardingOrientation);
		
		if (vertices == null)
			return null;
		
		return Path.constructPathFromVertices(this, regardingOrientation, vertices);
	}
	@Nullable
	public Path<E> getPath(@NotNull Vertex<V> source, @NotNull Vertex<V> destination) {
		return getPath(source, destination, true);
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public LinkedHashMap<Vertex<V>, Long> mapDistanceFrom(@NotNull Vertex<V> source, boolean regardingOrientation) {
		if (source == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(source))
			throw new IllegalArgumentException();
		
		LinkedHashMap<Vertex<V>, Boolean> mark = new LinkedHashMap<>();
		LinkedHashMap<Vertex<V>, Long> distance = new LinkedHashMap<>();
		Lexicon<Vertex<V>> F = new Lexicon<>();
		
		for (Vertex<V> x : getVertices()) {
			mark.put(x, false);
			distance.put(x, Long.MAX_VALUE);
		}
		
		mark.put(source, true);
		F.add(source);
		distance.put(source, 0L);
		
		while (!F.isEmpty()) {
			Vertex<V> x = F.first();
			
			if (x != null) {
				Lexicon<Vertex<V>> neighbor = getSuccessors(x);
				
				if (!regardingOrientation || (regardingOrientation && !isOriented()))
					neighbor.addAll(getPredecessors(x));
				
				neighbor.setAcceptNullValues(false);
				neighbor.setAcceptDuplicates(false);
				
				for (Vertex<V> y : neighbor) {
					if (!mark.getOrDefault(y, false)) {
						mark.put(y, true);
						distance.put(y, distance.getOrDefault(x, 0L) + 1);
						F.add(y);
					}
				}
				
				F.remove(x);
			}
			
		}
		
		return distance;
	}
	@NotNull
	public LinkedHashMap<Vertex<V>, Long> mapDistanceFrom(@NotNull Vertex<V> source) {
		return mapDistanceFrom(source, true);
	}
	
	@SuppressWarnings("ConstantConditions")
	public long getShortestDistanceBetween(@NotNull Vertex<V> source, @NotNull Vertex<V> destination, boolean regardingOrientation) {
		if (source == null || destination == null)
			throw new NullPointerException();
		
		if (!getVertices().contains(source) || !getVertices().contains(destination))
			throw new IllegalArgumentException();
		
		LinkedHashMap<Vertex<V>, Long> distance = mapDistanceFrom(source, regardingOrientation);
		
		return distance.getOrDefault(destination, Long.MAX_VALUE);
	}
	public long getShortestDistanceBetween(@NotNull Vertex<V> source, @NotNull Vertex<V> destination) {
		return getShortestDistanceBetween(source, destination, true);
	}
	
	// TODO: NOT TESTED
	public LinkedHashMap<Vertex<V>, Integer> breadthFirstSearch(@NotNull Vertex<V> beginning, @Nullable Function<Couple<Vertex<V>, Integer>, Void> action) {
		LinkedHashMap<Vertex<V>, Integer> route = new LinkedHashMap<>();
		LinkedHashMap<Vertex<V>, Boolean> mark = new LinkedHashMap<>();
		Lexicon<Vertex<V>> F = new LexiconBuilder<Vertex<V>>()
			.setAcceptNullValues(false)
			.setAcceptDuplicates(false)
			.createLexicon();
		int p = 1;
		
		for (Vertex<V> v : getVertices()) {
			mark.put(v, false);
			route.put(v, 0);
		}
		
		mark.put(beginning, true);
		F.add(beginning);
		
		while (!F.isEmpty()) {
			Vertex<V> x = F.last();
			
			if (x != null) {
				Lexicon<Vertex<V>> successors = getSuccessors(x);
				if (!successors.isEmpty()) {
					for (Vertex<V> y : successors) {
						if (!mark.getOrDefault(y, true)) {
							mark.put(y, true);
							F.add(y);
						}
					}
				}
				
				route.put(x, p);
				F.remove(x); // remove x (which is the last element in F)
				
				if (action != null)
					action.apply(new Couple<>(x, p));
				
				p++;
			}
			
			String output = "DEBUG> F = [";
			for (Vertex<V> vertex : F)
				output += vertex.getLabel() + ", ";
			output = output.substring(0, output.length() - 2);
			output += "]";
			System.out.println(output);
		}
		
		return route;
	}
	
	// TODO: NOT TESTED
	public LinkedHashMap<Vertex<V>, Integer> depthFirstSearch(@NotNull Vertex<V> beginning, @Nullable Function<Couple<Vertex<V>, Integer>, Void> action) {
		LinkedHashMap<Vertex<V>, Integer> route = new LinkedHashMap<>();
		LinkedHashMap<Vertex<V>, Boolean> mark = new LinkedHashMap<>();
		Lexicon<Vertex<V>> P = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.setAcceptDuplicates(false)
				.createLexicon();
		int p = 1;
		
		for (Vertex<V> v : getVertices()) {
			mark.put(v, false);
			route.put(v, 0);
		}
		
		mark.put(beginning, true);
		P.add(beginning);
		
		while (!P.isEmpty()) {
			Vertex<V> x = P.last();
			
			if (x != null) {
				Lexicon<Vertex<V>> successors = getSuccessors(x);
				for (Vertex<V> y : successors) {
					if (!mark.getOrDefault(y, false)) {
						mark.put(y, true);
						P.add(y);
					}
				}
				
				route.put(x, p);
				P.remove(x);
				
				if (action != null)
					action.apply(new Couple<>(x, p));
				
				p++;
			}
		}
		
		return route;
	}
	
	/**
	 * Detect if there is at least one cycle in the graph.
	 * @return Return {@code true} if there is at least one cycle, {@code false} if there is no cycle.
	 */
	// TODO: NOT TESTED
	public boolean detectCycle() {
		LinkedHashMap<Vertex<V>, Integer> inDegree = new LinkedHashMap<>();
		Lexicon<Vertex<V>> F = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		int p = 0;
		
		for (Vertex<V> v : getVertices()) {
			int vInDegree = getInDegree(v);
			
			inDegree.put(v, vInDegree);
			
			if (vInDegree == 0) {
				F.add(v);
				p++;
			}
		}
		
		while (!F.isEmpty()) {
			Vertex<V> x = F.first();
			
			if (x != null) {
				Lexicon<Vertex<V>> successors = getSuccessors(x);
				for (Vertex<V> y : successors) {
					int yInDegree = inDegree.getOrDefault(y, 0);
					
					inDegree.put(y, yInDegree - 1);
					
					if ((yInDegree - 1) == 0) {
						F.add(y);
						p++;
					}
				}
				
				F.remove(x);
			}
		}
		
		return p != getN();
	}
	
	/**
	 * Give to every vertices in the graph a unique topological number if the graph has no cycle.
	 * @return Return a linked HashMap where the keys are all the vertices of the graph and the values the topological
	 * number for the given vertex. If there is a cycle in the graph, return {@code null}, because it is impossible
	 * to have a topological numbering in a graph containing a cycle.
	 */
	@Nullable
	public LinkedHashMap<Vertex<V>, Integer> topologicalNumbering() {
		if (detectCycle())
			return null;
		
		LinkedHashMap<Vertex<V>, Integer> topo = new LinkedHashMap<>();
		LinkedHashMap<Vertex<V>, Integer> inDegree = new LinkedHashMap<>();
		Lexicon<Vertex<V>> F = new LexiconBuilder<Vertex<V>>()
				.setAcceptNullValues(false)
				.createLexicon();
		int p = 1;
		
		for (Vertex<V> v : getVertices()) {
			int vInDegree = getInDegree(v);
			
			inDegree.put(v, vInDegree);
			
			if (vInDegree == 0) {
				F.add(v);
				topo.put(v, p);
				p++;
			}
		}
		
		while (!F.isEmpty()) {
			Vertex<V> x = F.first();
			
			if (x != null) {
				Lexicon<Vertex<V>> successors = getSuccessors(x);
				for (Vertex<V> y : successors) {
					int yInDegree = inDegree.getOrDefault(y, 0);
					
					inDegree.put(y, yInDegree - 1);
					
					if ((yInDegree - 1) == 0) {
						F.add(y);
						topo.put(y, p);
						p++;
					}
				}
				
				F.remove(x);
			}
		}
		
		return topo;
	}
	
	/* GETTERS & SETTERS */
	
	/**
	 * Indicate if the graph is oriented.
	 * @return Return {@code true} if the graph is oriented, {@code false} otherwise.
	 */
	public boolean isOriented() {
		return oriented;
	}
	
	public void setOriented(boolean oriented) {
		this.oriented = oriented;
	}
	
	protected void initOriented() {
		setOriented(false);
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Vertex<V>> getVertices() {
		if (vertices == null)
			initVertices();
		
		return vertices;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setVertices(@NotNull Lexicon<Vertex<V>> vertices) {
		if (vertices == null)
			throw new NullPointerException();
		
		this.vertices = vertices;
		configureVertices();
	}
	
	protected void initVertices() {
		setVertices(new Lexicon<>());
	}
	
	protected void configureVertices() {
		getVertices().setAcceptNullValues(false);
		getVertices().setAcceptDuplicates(false);
		getVertices().addObserver((observable, o) -> snap(o));
		snap(getVertices());
	}
	
	public void resetVerticesID() {
		for (Vertex<V> vertex : getVertices())
			vertex.setId(UUID.randomUUID());
	}
	
	public int getN() {
		return getVertices().size();
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Lexicon<Edge<E>> getEdges() {
		if (edges == null)
			initEdges();
		
		return edges;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setEdges(@NotNull Lexicon<Edge<E>> edges) {
		if (edges == null)
			throw new NullPointerException();
		
		this.edges = edges;
		configureEdges();
	}
	
	protected void initEdges() {
		setEdges(new Lexicon<>());
	}
	
	protected void configureEdges() {
		getEdges().setAcceptNullValues(false);
		getEdges().setAcceptDuplicates(false);
		getEdges().addObserver((observable, o) -> snap(o));
		snap(getEdges());
	}
	
	public void resetEdgesID() {
		for (Edge<E> edge : getEdges())
			edge.setId(UUID.randomUUID());
	}
	
	public int getM() {
		return getEdges().size();
	}
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeBoolean(isOriented());
		stream.writeObject(getVertices());
		stream.writeObject(getEdges());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setOriented(stream.readBoolean());
		setVertices((Lexicon<Vertex<V>>) stream.readObject());
		setEdges((Lexicon<Edge<E>>) stream.readObject());
	}
	
	/* OVERRIDES */
	
	/**
	 * Check if o is equal to this instance, regardless of the identifier.
	 * @param o The object to check
	 * @return Return {@code true} if o and this instance are equivalent, {@code false} otherwise.
	 */
	public boolean equivalent(Object o) {
		if (this == o) return true;
		if (!(o instanceof Graph)) return false;
		Graph<?, ?> that = (Graph<?, ?>) o;
		
		if (this.isOriented() != that.isOriented())
			return false;
		
		if (this.getVertices().size() != that.getVertices().size() ||
				this.getEdges().size() != that.getEdges().size())
			return false;
		
		boolean found = false;
		// Vertices
		for (int i = 0, maxi = this.getVertices().size(); i < maxi; i++) {
			Vertex<V> thisV = this.getVertices().get(i);
			
			found = false;
			for (int j = 0, maxj = that.getVertices().size(); j < maxj && !found; j++) {
				Vertex<?> thatV = that.getVertices().get(j);
				if (thisV.equivalent(thatV))
					found = true;
			}
			
			if (!found)
				return false;
		}
		
		// Edges
		for (int i = 0, maxi = this.getEdges().size(); i < maxi; i++) {
			Edge<E> thisE = this.getEdges().get(i);
			
			found = false;
			for (int j = 0, maxj = that.getEdges().size(); j < maxj && !found; j++) {
				Edge<?> thatE = that.getEdges().get(j);
				if (thisE.equivalent(thatE))
					found = true;
			}
			
			if (!found)
				return false;
		}
		
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Graph)) return false;
		Graph<?, ?> graph = (Graph<?, ?>) o;
		return isOriented() == graph.isOriented() &&
				Objects.equals(getVertices(), graph.getVertices()) &&
				Objects.equals(getEdges(), graph.getEdges());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isOriented(), getVertices(), getEdges());
	}
	
	/**
	 * Parse the data of the attributes in the instance of Graph to give a readable content for humans.
	 * @return Return a representation of the graph.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Graph\n");
		
		// Vertices
		builder.append(" --- ");
		if (getVertices().size() <= 1)
			builder.append("Vertex");
		else
			builder.append("Vertices");
		builder.append(" ---\n");
		
		for (Vertex<V> vertex : getVertices()) {
			if (vertex != null) {
				builder.append("\tVertex{")
						.append("label=\"")
						.append(vertex.getLabel())
						.append("\", id=\"")
						.append(vertex.getId())
						.append("\"");
				
				if (vertex.getData() != null) {
					builder.append(", data=\"")
							.append(vertex.getData())
							.append("\"");
				}
				
				builder.append("}\n");
			}
		}
		
		// Edge
		builder.append(" --- Edge");
		if (getVertices().size() > 1)
			builder.append("s");
		builder.append(" ---\n");
		
		for (Edge<E> edge : getEdges()) {
			if (edge != null) {
				builder.append("\tEdge{");
				
				if (edge.getX() != null && edge.getY() != null) {
					builder.append(edge.getX().getLabel())
							.append(' ');
					
					if (!isOriented())
						builder.append('<');
					
					builder.append("-> ")
							.append(edge.getY().getLabel())
							.append(" (id=\"")
							.append(edge.getId());
					
					if (edge.getData() != null) {
						builder.append("\", data=\"")
								.append(edge.getData());
					}
					builder.append("\")");
				}
				
				builder.append("}\n");
			}
		}
		
		return builder.toString();
	}
}
