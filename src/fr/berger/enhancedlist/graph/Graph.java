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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;

public class Graph<T, U> extends EnhancedObservable implements Serializable, Cloneable {
	
	private boolean oriented;
	@NotNull
	private Lexicon<Vertex<T>> vertices;
	@NotNull
	private Lexicon<Edge<U>> edges;
	
	/* CONSTRUCTORS */
	
	/* GRAPH METHODS */
	
	@SuppressWarnings({"ConstantConditions", "unchecked"})
	public Lexicon<Vertex<T>> getSuccessors(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		Lexicon<Vertex<T>> vertices = new LexiconBuilder<Vertex<T>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Edge<U> edge : getEdges())
			if (edge.getLink() != null && edge.getLink().getX() != null)
				if (Objects.equals(vertex, edge.getLink().getX().getElement()))
					vertices.add((Vertex<T>) edge.getLink().getY().getElement());
		
		return vertices;
	}
	@SuppressWarnings("ConstantConditions")
	public Lexicon<Vertex<T>> getSuccessors(@NotNull Ref<Vertex<T>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getSuccessors(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
	public Lexicon<Vertex<T>> getPredecessors(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		Lexicon<Vertex<T>> vertices = new LexiconBuilder<Vertex<T>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Edge<U> edge : getEdges())
			if (edge.getLink() != null && edge.getLink().getY() != null)
				if (Objects.equals(vertex, edge.getLink().getY().getElement()))
					vertices.add((Vertex<T>) edge.getLink().getX().getElement());
		
		return vertices;
	}
	@SuppressWarnings("ConstantConditions")
	public Lexicon<Vertex<T>> getPredecessors(@NotNull Ref<Vertex<T>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getPredecessors(vertex.getElement());
	}
	
	@NotNull
	public Lexicon<Vertex<T>> getSources() {
		Lexicon<Vertex<T>> vertices = new LexiconBuilder<Vertex<T>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Vertex<T> vertex : getVertices())
			if (getPredecessors(vertex).size() == 0)
				vertices.add(vertex);
		
		return vertices;
	}
	
	@NotNull
	public Lexicon<Vertex<T>> getSinks() {
		Lexicon<Vertex<T>> vertices = new LexiconBuilder<Vertex<T>>()
				.setAcceptNullValues(false)
				.createLexicon();
		
		for (Vertex<T> vertex : getVertices())
			if (getSuccessors(vertex).size() == 0)
				vertices.add(vertex);
		
		return vertices;
	}
	
	@SuppressWarnings("ConstantConditions")
	public int getInDegree(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		throw new NotImplementedException();
	}
	@SuppressWarnings("ConstantConditions")
	public int getInDegree(@NotNull Ref<Vertex<T>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getInDegree(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
	public int getOutDegree(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		throw new NotImplementedException();
	}
	@SuppressWarnings("ConstantConditions")
	public int getOutDegree(@NotNull Ref<Vertex<T>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getOutDegree(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
	public int getDegree(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getInDegree(vertex) + getOutDegree(vertex);
	}
	@SuppressWarnings("ConstantConditions")
	public int getDegree(@NotNull Ref<Vertex<T>> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		return getDegree(vertex.getElement());
	}
	
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Vertex<T> v1, @NotNull Vertex<T> v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();
		
		throw new NotImplementedException();
	}
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Ref<Vertex<T>> v1, @NotNull Ref<Vertex<T>> v2) {
		if (v1 == null || v2 == null)
			throw new NullPointerException();
		
		return areAdjacent(v1.getElement(), v2.getElement());
	}
	@SuppressWarnings("ConstantConditions")
	public boolean areAdjacent(@NotNull Edge<U> e1, @NotNull Edge<U> e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		
		throw new NotImplementedException();
	}
	
	public boolean isSymmetrical() {
		throw new NotImplementedException();
	}
	
	public boolean isAntisymmetric() {
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
	
	public Graph<T, U> getSymmetry() {
		throw new NotImplementedException();
	}
	
	public Lexicon<Vertex<T>> getArticulationPoints() {
		throw new NotImplementedException();
	}
	
	public Lexicon<Edge<U>> getBridges() {
		throw new NotImplementedException();
	}
	
	public Matrix<Integer> getMatrix() {
		throw new NotImplementedException();
	}
	
	// ALGORITHMS
	
	public LinkedHashMap<Vertex<T>, Integer> breadthFirstSearch(@NotNull Vertex<T> beginning, @Nullable Function<Couple<Vertex<T>, Integer>, Void> action) {
		LinkedHashMap<Vertex<T>, Integer> route = new LinkedHashMap<>();
		LinkedHashMap<Vertex<T>, Boolean> mark = new LinkedHashMap<>();
		Lexicon<Vertex<T>> F = new LexiconBuilder<Vertex<T>>()
			.setAcceptNullValues(false)
			.setAcceptDuplicates(false)
			.createLexicon();
		int p = 1;
		
		for (Vertex<T> v : getVertices()) {
			mark.put(v, false);
			route.put(v, 0);
		}
		
		mark.put(beginning, true);
		F.add(beginning);
		
		while (!F.isEmpty()) {
			Vertex<T> x = F.first();
			
			if (x != null) {
				Lexicon<Vertex<T>> successors = getSuccessors(x);
				for (Vertex<T> y : successors) {
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
	
	public LinkedHashMap<Vertex<T>, Integer> depthFirstSearch(@NotNull Vertex<T> beginning, @Nullable Function<Couple<Vertex<T>, Integer>, Void> action) {
		LinkedHashMap<Vertex<T>, Integer> route = new LinkedHashMap<>();
		LinkedHashMap<Vertex<T>, Boolean> mark = new LinkedHashMap<>();
		Lexicon<Vertex<T>> P = new LexiconBuilder<Vertex<T>>()
				.setAcceptNullValues(false)
				.setAcceptDuplicates(false)
				.createLexicon();
		int p = 1;
		
		for (Vertex<T> v : getVertices()) {
			mark.put(v, false);
			route.put(v, 0);
		}
		
		mark.put(beginning, true);
		P.add(beginning);
		
		while (!P.isEmpty()) {
			Vertex<T> x = P.last();
			
			if (x != null) {
				Lexicon<Vertex<T>> successors = getSuccessors(x);
				for (Vertex<T> y : successors) {
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
	
	public Lexicon<Vertex<T>> getVertices() {
		return vertices;
	}
	
	public void setVertices(Lexicon<Vertex<T>> vertices) {
		this.vertices = vertices;
	}
	
	public int getN() {
		return getVertices().size();
	}
	
	public Lexicon<Edge<U>> getEdges() {
		return edges;
	}
	
	public void setEdges(Lexicon<Edge<U>> edges) {
		this.edges = edges;
	}
	
	public int getM() {
		return getEdges().size();
	}
}
