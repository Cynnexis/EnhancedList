package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.beyondcode.util.Irregular;
import fr.berger.enhancedlist.lexicon.Lexicon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Vertex of a graph.
 * @param <T> The type paramter of the data that the vertex contains.
 * @see Graph
 * @see Edge
 * @author Valentin Berger
 */
public class Vertex<T> extends EnhancedObservable implements Serializable, Cloneable {
	
	@NotNull
	private UUID id;
	@Nullable
	private T data;
	@NotNull
	private String label;
	@Deprecated
	@NotNull
	private Lexicon<Ref<Vertex<?>>> successors;
	
	/* CONSTRUCTORS */
	
	public Vertex(@NotNull UUID id, @Nullable T data, @NotNull String label, @NotNull Lexicon<Ref<Vertex<?>>> successors) {
		setId(id);
		setData(data);
		setLabel(label);
		setSuccessors(successors);
	}
	public Vertex(@Nullable T data, @NotNull String label, @NotNull Lexicon<Ref<Vertex<?>>> successors) {
		initId();
		setData(data);
		setLabel(label);
		setSuccessors(successors);
	}
	public Vertex(@NotNull String label, @NotNull Lexicon<Ref<Vertex<?>>> successors) {
		initId();
		initData();
		setLabel(label);
		setSuccessors(successors);
	}
	public Vertex(@NotNull Lexicon<Ref<Vertex<?>>> successors) {
		initId();
		initData();
		initLabel();
		setSuccessors(successors);
	}
	public Vertex(@NotNull String label) {
		initId();
		initData();
		setLabel(label);
		initSuccessors();
	}
	@SuppressWarnings("ConstantConditions")
	public Vertex(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		setId(vertex.getId());
		setData(vertex.getData());
		setLabel(vertex.getLabel());
		setSuccessors(vertex.getSuccessors());
	}
	public Vertex() {
		initId();
		initData();
		initLabel();
		initSuccessors();
	}
	
	/* VERTEX METHODS */
	
	/* GETTERS & SETTERS */
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public UUID getId() {
		if (id == null)
			initId();
		
		return id;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setId(@NotNull UUID id) {
		if (id == null)
			throw new NullPointerException();
		
		this.id = id;
		snap(this.id);
	}
	
	protected void initId() {
		setId(UUID.randomUUID());
	}
	
	@Nullable
	public T getData() {
		return data;
	}
	
	public void setData(@Nullable T data) {
		this.data = data;
		snap(this.data);
	}
	
	protected void initData() {
		setData(null);
	}
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public String getLabel() {
		if (label == null)
			initLabel();
		
		return label;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setLabel(@NotNull String label) {
		if (label == null)
			throw new NullPointerException();
		
		this.label = label;
		snap(this.label);
	}
	
	protected void initLabel() {
		setLabel(Integer.toString(Irregular.rangeInt(0, true, 100, true)));
	}
	
	@SuppressWarnings("ConstantConditions")
	public Lexicon<Ref<Vertex<?>>> getSuccessors() {
		if (successors == null)
			initSuccessors();
		
		return successors;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setSuccessors(@NotNull Lexicon<Ref<Vertex<?>>> successors) {
		if (successors == null)
			throw new NullPointerException();
		
		this.successors = successors;
		configureSuccessors();
	}
	
	protected void initSuccessors() {
		setSuccessors(new Lexicon<>());
	}
	
	protected void configureSuccessors() {
		getSuccessors().setAcceptNullValues(false);
		snap(getSuccessors());
	}
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getId());
		stream.writeObject(getData());
		stream.writeBytes(getLabel());
		stream.writeObject(getSuccessors());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setId((UUID) stream.readObject());
		setData((T) stream.readObject());
		setLabel(stream.readUTF());
		setSuccessors((Lexicon<Ref<Vertex<?>>>) stream.readObject());
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vertex)) return false;
		Vertex<?> vertex = (Vertex<?>) o;
		return Objects.equals(getId(), vertex.getId()) &&
				Objects.equals(getData(), vertex.getData()) &&
				Objects.equals(getLabel(), vertex.getLabel()) &&
				Objects.equals(getSuccessors(), vertex.getSuccessors());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getData(), getLabel(), getSuccessors());
	}
	
	@Override
	public String toString() {
		return "Vertex{" +
				"id=" + id +
				", data=" + data +
				", label='" + label + '\'' +
				'}';
	}
}
