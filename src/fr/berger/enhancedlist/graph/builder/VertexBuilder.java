package fr.berger.enhancedlist.graph.builder;

import fr.berger.arrow.Ref;
import fr.berger.enhancedlist.graph.Vertex;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Objects;
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
	public VertexBuilder<T> setLabel(@NotNull String label) {
		vertex.setLabel(label);
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(char label) {
		vertex.setLabel(Character.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(int label) {
		vertex.setLabel(Integer.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(short label) {
		vertex.setLabel(Short.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(byte label) {
		vertex.setLabel(Byte.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(long label) {
		vertex.setLabel(Long.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(float label) {
		vertex.setLabel(Float.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(double label) {
		vertex.setLabel(Double.toString(label));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setLabel(@NotNull BigInteger label) {
		vertex.setLabel(Objects.toString(label));
		return this;
	}
	
	@NotNull
	public VertexBuilder<T> setSuccessors(@NotNull Collection<Ref<Vertex<?>>> successors) {
		vertex.setSuccessors(new Lexicon<>(successors));
		return this;
	}
	@NotNull
	public VertexBuilder<T> setSuccessors(@NotNull Ref<Vertex<?>>... successors) {
		vertex.setSuccessors(new Lexicon<>(successors));
		return this;
	}
	
	@NotNull
	public Vertex<T> createVertex() {
		return vertex;
	}
}
