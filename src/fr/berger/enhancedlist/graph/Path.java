package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.lexicon.Lexicon;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * In graph theory, a "Path" is a list of adjacent edges. In this library, a path is a lexicon of generic edges.
 * @param <T> The generic parameter for {@code Edge}.
 * @see Lexicon
 * @see Graph
 * @see Edge
 * @author Valentin Berger
 */
public class Path<T> extends Lexicon<Edge<T>> implements Serializable, Cloneable {
	
	public Path(@NotNull Collection<Edge<T>> edges) {
		super(edges);
		configureLexicon();
	}
	@SuppressWarnings("unchecked")
	public Path(@NotNull Edge<T>... edges) {
		super(edges);
		configureLexicon();
	}
	public Path(@NotNull Path<T> path) {
		super(path);
		configureLexicon();
	}
	public Path() {
		super();
		configureLexicon();
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public static <V, E> Path<E> constructPathFromVertices(@NotNull Graph<V, E> graph, boolean regardingOrientation, @NotNull Lexicon<Vertex<V>> vertices) {
		if (graph == null || vertices == null)
			throw new NullPointerException();
		
		boolean canGoBackward = !regardingOrientation || (regardingOrientation && !graph.isOriented());
		Path<E> path = new Path<>();
		
		for (int i = 0, maxi = vertices.size(); i < maxi - 1; i++) {
			Vertex<V> v = vertices.get(i);
			
			// Fetch the next vertex.
			Vertex<V> next = vertices.get(i + 1);
			
			// Search the edge
			Edge<E> edge = null;
			for (int j = 0, maxj = graph.getEdges().size(); j < maxj && edge == null; j++) {
				Edge<E> currentEdge = graph.getEdges().get(j);
				if (Objects.equals(currentEdge.getX(), v) && Objects.equals(currentEdge.getY(), next) ||
						(canGoBackward && Objects.equals(currentEdge.getY(), v) && Objects.equals(currentEdge.getX(), next)))
					edge = currentEdge;
			}
			
			// If the edge has not been found, throw an exception because of the argument "vertices"
			if (edge == null)
				throw new IllegalArgumentException("No edge beginning with " + v + " and ending with " + next + " in the list of vertices given in argument: " + vertices.toString());
			
			// Add the edge to the path
			path.add(edge);
		}
		
		return path;
	}
	@NotNull
	public static <V, E> Path<E> constructPathFromVertices(@NotNull Graph<V, E> graph, @NotNull Lexicon<Vertex<V>> vertices) {
		return constructPathFromVertices(graph, true, vertices);
	}
	@SuppressWarnings("unchecked")
	public static <V, E> Path constructPathFromVertices(@NotNull Graph<V, E> graph, boolean regardingOrientation, @NotNull Vertex<V>... vertices) {
		return constructPathFromVertices(graph, regardingOrientation, new Lexicon<>(vertices));
	}
	@SuppressWarnings("unchecked")
	public static <V, E> Path constructPathFromVertices(@NotNull Graph<V, E> graph, @NotNull Vertex<V>... vertices) {
		return constructPathFromVertices(graph, true, new Lexicon<>(vertices));
	}
	
	/* PATH METHODS */
	
	/**
	 * Check if the path is valid (there is no {@code null} elements, and all edges are adjacent)
	 * @return Return {@code true} if the path is correct, otherwise {@code false}.
	 */
	public boolean checkPath() {
		// Check if one of the element is null
		for (Edge<T> e : this) {
			if (e == null)
				return false;
			
			if (e.getX() == null || e.getY() == null)
				return false;
		}
		
		// Check if all edges are adjacent
		Vertex<?> previous = first().getX();
		
		for (int i = 0, maxi = size(); i < maxi; i++) {
			Edge<T> currentEdge = get(i);
			if (!Objects.equals(previous, currentEdge.getX()))
				return false;
			
			previous = currentEdge.getY();
		}
		
		return true;
	}
	
	/**
	 * Check the path, if it is not valid, remove the element {@code element} using its index in the list {@code index}.
	 * @param index The index of the element which changed in the list.
	 * @param element The element which changed in the list.
	 * @see #checkPath()
	 */
	protected void verifyChangedElement(int index, Edge<T> element) {
		if (!checkPath())
			remove(index);
	}
	
	/**
	 * Configure the lexicon by refusing null values, and verifying all changed element with "verifyChangedElement(int,
	 * Edge&lt;T$gt;)".
	 * @see #verifyChangedElement(int, Edge)
	 */
	protected void configureLexicon() {
		this.setAcceptNullValues(false);
		this.addAddHandler(this::verifyChangedElement);
		this.addSetHandler(this::verifyChangedElement);
		this.addRemoveHandler(this::verifyChangedElement);
	}
	
	public boolean isClosed() {
		return Objects.equals(first().getX(), last().getY());
	}
	
	public boolean isCycle() {
		return isClosed();
	}
	
	public boolean isOpen() {
		return !isClosed();
	}
	
	public boolean areThereDuplicates() {
		for (int i = 0, maxi = size(); i < maxi; i++) {
			for (int j = 0, maxj = size(); j < maxj; j++) {
				if (i != j) {
					if (Objects.equals(get(i), get(j)))
						return false;
					
					try {
						if (Objects.equals(get(i).getX(), get(j).getY()))
							return false;
					} catch (NullPointerException ignored) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/* LEXICON OVERRIDE */
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(getClass().getCanonicalName())
				.append("\n");
		
		if (checkPath()) {
			for (int i = 0, maxi = size(); i < maxi; i++) {
				builder.append(get(i).getX().getLabel())
						.append(" -> ");
				
				// If last iteration, add the last one
				if (i == maxi -1)
					builder.append(get(i).getY().getLabel());
			}
		}
		else
			builder.append("Path Not Valid.");
		
		return builder.toString();
	}
}
