package fr.berger.enhancedlist.graph;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * In graph theory, a walk is a path.
 * @param <T> The generic parameter for {@code Edge}.
 * @see Path
 * @see fr.berger.enhancedlist.lexicon.Lexicon
 * @see Graph
 * @see Edge
 * @author Valentin Berger
 */
public class Walk<T> extends Path<T> implements Serializable, Cloneable {
	
	public Walk(@NotNull Collection<Edge<T>> collection) {
		super(collection);
	}
	@SuppressWarnings("unchecked")
	public Walk(@NotNull Edge<T>... edges) {
		super(edges);
	}
	public Walk(@NotNull Walk<T> walk) {
		super(walk);
	}
	public Walk() {
		super();
	}
	
	/* WALK METHODS */
	
	public boolean isClosed() {
		return Objects.equals(first().getX(), last().getY());
	}
	
	public boolean isCycle() {
		return isClosed();
	}
	
	public boolean isOpen() {
		return !isClosed();
	}
}
