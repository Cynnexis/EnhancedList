package fr.berger.enhancedlist.graph;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * In graph theory, a "simple path" is a path without any repetition of edges
 * @param <T> The generic parameter for {@code Edge}.
 * @see Path
 * @see fr.berger.enhancedlist.lexicon.Lexicon
 * @see Graph
 * @see Edge
 * @author Valentin Berger
 */
@Deprecated
public class SimplePath<T> extends Path<T> implements Serializable, Cloneable {
	
	public SimplePath(@NotNull Collection<Edge<T>> edges) {
		super(edges);
	}
	
	public SimplePath(@NotNull Edge<T>... edges) {
		super(edges);
	}
	
	public SimplePath(@NotNull SimplePath<T> path) {
		super(path);
	}
	
	public SimplePath() {
		super();
	}
	
	/* PATH OVERRIDES */
	
	/**
	 * Check if the path is valid (there is no {@code null} elements, all edges are adjacent, and there is no vertex
	 * repetitions).
	 * @return Return {@code true} if the path is correct, otherwise {@code false}.
	 */
	@SuppressWarnings("Duplicates")
	@Override
	public boolean checkPath() {
		boolean superResult = super.checkPath();
		
		if (!superResult)
			return false;
		
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
	
	@Override
	protected void configureLexicon() {
		super.configureLexicon();
		setAcceptDuplicates(false);
	}
}
