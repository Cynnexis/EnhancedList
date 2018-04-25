package fr.berger.enhancedlist.graph.builder;

import fr.berger.arrow.Ref;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class VertexBuilder<T> {
	
	@NotNull
	private Vertex<T> vertex;
	
	@SuppressWarnings("ConstantConditions")
	public VertexBuilder(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		this.vertex = vertex;
	}
	public VertexBuilder() {
		this(new Vertex<>());
	}
	
	@NotNull
	public VertexBuilder<T> setId(@NotNull UUID id) {
		vertex.setId(id);
		return this;
	}
	
	@NotNull
	public VertexBuilder<T> setData(@Nullable T data) {
		vertex.setData(data);
		return this;
	}
	
	@NotNull
	public VertexBuilder setLabel(@NotNull String label) {
		vertex.setLabel(label);
		return this;
	}
	@NotNull
	public VertexBuilder setLabel(char label) {
		vertex.setLabel(Character.toString(label));
		return this;
	}
	
	@NotNull
	public VertexBuilder setSuccessors(@NotNull Collection<Ref<Vertex<?>>> successors) {
		vertex.setSuccessors(new Lexicon<>(successors));
		return this;
	}
	@NotNull
	public VertexBuilder setSuccessors(@NotNull Ref<Vertex<?>>... successors) {
		vertex.setSuccessors(new Lexicon<>(successors));
		return this;
	}
}
