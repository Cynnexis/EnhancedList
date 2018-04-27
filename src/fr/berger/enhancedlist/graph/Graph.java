package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.Couple;
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
	 * Tell if the vertices {@code v1} and {@code v2} are adjacent (there is at least one edge between them).
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 * @return Return {@code true} if there is at least one edge between them, {@code false} otherwise.
	 */
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Vertex<V> v1, @NotNull Vertex<V> v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();
		
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
			Edge edge = getEdges().get(i);
			// "edge" = (i, j)
			
			// Search for an edge "e" such that "e" = (j, i)
			boolean found = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj && !found; j++) {
				Edge e = getEdges().get(j);
				if (!Objects.equals(edge, e)) {
					found = Objects.equals(e.getX(), edge.getY()) &&
							Objects.equals(e.getY(), edge.getX());
				}
			}
			
			if (!found)
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
			Edge edge = getEdges().get(i);
			// "edge" = (i, j)
			
			// Search for an edge "e" such that "e" = (j, i)
			boolean found = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj && !found; j++) {
				Edge e = getEdges().get(j);
				if (!Objects.equals(edge, e)) {
					found = Objects.equals(e.getX(), edge.getY()) &&
							Objects.equals(e.getY(), edge.getX());
				}
			}
			
			if (found)
				antisymmetric = false;
		}
		
		return antisymmetric;
	}
	
	/**
	 * Tell if the graph is transitive.
	 * @return Return {@code true} if the graph is transitive, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public boolean isTransitive() {
		if (!isOriented())
			return true;
		
		for (int i = 0, maxi = getEdges().size(); i < maxi; i++) {
			Edge ei = getEdges().get(i); // (a, b)
			
			boolean found_ej = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj; j++) {
				if (i != j) {
					Edge ej = getEdges().get(j); // (b, c)
					
					if (Objects.equals(ei.getY(), ej.getX())) {
						found_ej = true;
						
						// Search an edge "ek" such taht "ek" = (a, b)
						boolean found_ek = false;
						for (int k = 0, maxk = getEdges().size(); k < maxk && !found_ek; k++) {
							if (i != k && j != k) {
								Edge ek = getEdges().get(k);
								
								if (Objects.equals(ei.getX(), ek.getX()) && Objects.equals(ej.getY(), ek.getY()))
									found_ek = true;
							}
						}
						
						if (!found_ek)
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
	// TODO: NOT TESTED
	public boolean isComplete() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			for (int j = 0, maxj = getVertices().size(); j < maxj; j++) {
				if (i != j) {
					Vertex<V> vi = getVertices().get(i);
					Vertex<V> vj = getVertices().get(j);
					
					// Search an edge such that ("vi" ; "vj") or ("vj" ; "vi")
					boolean found_ek = false;
					for (int k = 0, maxk = getEdges().size(); k < maxk && !found_ek; k++) {
						Edge<E> ek = getEdges().get(k);
						if ((Objects.equals(ek.getX(), vi) && Objects.equals(ek.getY(), vj)) ||
								(Objects.equals(ek.getX(), vj) && Objects.equals(ek.getY(), vi)))
							found_ek = true;
					}
					
					if (!found_ek)
						return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Tell if the graph is reflexive.
	 * @return Return {@code true} if the graph is reflexive, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public boolean isReflexive() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			Vertex<V> vi = getVertices().get(i);
			
			// Search for an edge ej such that ej = (ei, ei)
			boolean found_ej = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj && !found_ej; j++) {
				Edge<E> ej = getEdges().get(j);
				
				if (Objects.equals(ej.getX(), vi) && Objects.equals(ej.getY(), vi))
					found_ej = true;
			}
			
			if (!found_ej)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Tell if the graph is anti-reflexive.
	 * @return Return {@code true} if the graph is anti-reflexive, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public boolean isAntiReflexive() {
		for (int i = 0, maxi = getVertices().size(); i < maxi; i++) {
			Vertex<V> vi = getVertices().get(i);
			
			// Search for an edge ej such that ej = (ei, ei)
			for (int j = 0, maxj = getEdges().size(); j < maxj; j++) {
				Edge<E> ej = getEdges().get(j);
				
				if (Objects.equals(ej.getX(), vi) && Objects.equals(ej.getY(), vi))
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Compute a walk between {@code v1} and {@code v2}.
	 * @param v1 The vertex where the walk begins.
	 * @param v2 The vertex where the walk ends.
	 * @return A walk between {@code v1} and {@code v2}.
	 */
	// TODO: NOT TESTED
	public Walk<E> getWalk(@NotNull Vertex<V> v1, @NotNull Vertex<V> v2) {
		throw new NotImplementedException();
	}
	
	/**
	 * Tell if the graph is connected.
	 * @return Return {@code true} if the graph is connected, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public boolean isConnected() {
		throw new NotImplementedException();
	}
	
	/**
	 * Tell if the graph is disconnected.
	 * @return Return {@code true} if the graph is disconnected, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public boolean isDisconnected() {
		return !isConnected();
	}
	
	/**
	 * Tell if the graph is disconnected.
	 * @return Return {@code true} if the graph is disconnected, {@code false} otherwise.
	 */
	// TODO: NOT TESTED
	public Graph<V, E> getSymmetry() {
		throw new NotImplementedException();
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
	public Matrix<Integer> getMatrix() {
		throw new NotImplementedException();
	}
	
	// ALGORITHMS
	
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
			Vertex<V> x = F.first();
			
			if (x != null) {
				Lexicon<Vertex<V>> successors = getSuccessors(x);
				for (Vertex<V> y : successors) {
					if (!mark.getOrDefault(y, false)) {
						mark.put(y, true);
						F.add(y);
					}
				}
				
				route.put(x, p);
				F.remove(x);
				
				if (action != null)
					action.apply(new Couple<>(x, p));
				
				p++;
			}
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
