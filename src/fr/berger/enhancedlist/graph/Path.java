package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;

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
public abstract class Path<T> extends Lexicon<Edge<T>> implements Serializable, Cloneable {
	
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
