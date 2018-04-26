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
	
	private boolean oriented;
	@NotNull
	private Lexicon<Vertex<V>> vertices;
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
	
	@SuppressWarnings({"ConstantConditions", "unchecked"})
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
	public Lexicon<Vertex<V>> getSuccessors(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getSuccessors(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
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
	public Lexicon<Vertex<V>> getPredecessors(@NotNull Ref<Vertex<V>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getPredecessors(vertex.getElement());
	}
	
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
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Edge<E> e1, @NotNull Edge<E> e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		
		return Objects.equals(e1.getX(), e2.getX()) ||
				Objects.equals(e1.getX(), e2.getY()) ||
				Objects.equals(e1.getY(), e2.getX()) ||
				Objects.equals(e1.getY(), e2.getY());
	}
	
	public boolean isSymmetrical() {
		if (!isOriented())
			return true;
		
		throw new NotImplementedException();
		/*
		boolean symmetrical = true;
		
		for (int i = 0, maxi = getEdges().size(); i < maxi && symmetrical; i++) {
			Edge edge = getEdges().get(i);
			// "edge" = (i, j)
			
			// Search for an edge "e" such that "e" = (j, i)
			boolean found = false;
			for (int j = 0, maxj = getEdges().size(); j < maxj && !found; j++) {
				Edge e = getEdges().get(j);
				if (!Objects.equals(edge, e)) {
					if (e.getLink() != null)
				}
			}
			
			if (!found)
				symmetrical = false;
		}
		*/
	}
	
	public boolean isAntisymmetric() {
		if (!isOriented())
			return true;
		
		throw new NotImplementedException();
	}
	
	public boolean isTransitive() {
		throw new NotImplementedException();
	}
	
	public boolean isComplete() {
		throw new NotImplementedException();
	}
	
	public boolean isReflexive() {
		throw new NotImplementedException();
	}
	
	public boolean isAntiReflexive() {
		throw new NotImplementedException();
	}
	
	public boolean isConnected() {
		throw new NotImplementedException();
	}
	
	public boolean isDisconnected() {
		return !isConnected();
	}
	
	public Graph<V, E> getSymmetry() {
		throw new NotImplementedException();
	}
	
	public Lexicon<Vertex<V>> getArticulationPoints() {
		throw new NotImplementedException();
	}
	
	public Lexicon<Edge<E>> getBridges() {
		throw new NotImplementedException();
	}
	
	public Matrix<Integer> getMatrix() {
		throw new NotImplementedException();
	}
	
	// ALGORITHMS
	
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
	
	/* GETTERS & SETTERS */
	
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
