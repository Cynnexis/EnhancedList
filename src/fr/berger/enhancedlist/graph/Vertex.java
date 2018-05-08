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
 * @param <T> The type parameter of the data that the vertex contains.
 * @see Graph
 * @see Edge
 * @see Color
 * @see fr.berger.enhancedlist.graph.builder.VertexBuilder
 * @author Valentin Berger
 */
public class Vertex<T> extends EnhancedObservable implements Serializable, Cloneable {
	
	/**
	 * The unique identifier of this instance of vertex. It is mandatory because a lot of algorithm in Graph use
	 * the equal operations to compare two vertices, and the data, the string and the color does not make a vertex
	 * unique in a graph. Therefore, an identifier is required.
	 * @see Graph
	 * @see UUID
	 */
	@NotNull
	private UUID id;
	
	/**
	 * The data contained by this vertex (can be anything, even a null value)
	 */
	@Nullable
	private T data;
	
	/**
	 * The display label of the vertex. It is not necessary unique, but it is recommended to be so.
	 */
	@NotNull
	private String label;
	
	/**
	 * The color of the vertex if the graph is colored. If it not, the value of color is null. If the graph is coloring,
	 * the value of color is -1 until a color has been found for this vertex.
	 * @see Color
	 */
	@Nullable
	private Color color;
	
	/* CONSTRUCTORS */
	
	public Vertex(@NotNull UUID id, @Nullable T data, @NotNull String label, @Nullable Color color) {
		setId(id);
		setData(data);
		setLabel(label);
		setColor(color);
	}
	public Vertex(@NotNull UUID id, @Nullable T data, @NotNull String label) {
		setId(id);
		setData(data);
		setLabel(label);
		initColor();
	}
	public Vertex(@Nullable T data, @NotNull String label) {
		initId();
		setData(data);
		setLabel(label);
		initColor();
	}
	public Vertex(@NotNull String label) {
		initId();
		initData();
		setLabel(label);
		initColor();
	}
	public Vertex(char label) {
		initId();
		initData();
		setLabel(Character.toString(label));
		initColor();
	}
	public Vertex(int label) {
		initId();
		initData();
		setLabel(Integer.toString(label));
		initColor();
	}
	public Vertex(short label) {
		initId();
		initData();
		setLabel(Short.toString(label));
		initColor();
	}
	public Vertex(byte label) {
		initId();
		initData();
		setLabel(Byte.toString(label));
		initColor();
	}
	public Vertex(long label) {
		initId();
		initData();
		setLabel(Long.toString(label));
		initColor();
	}
	public Vertex(float label) {
		initId();
		initData();
		setLabel(Float.toString(label));
		initColor();
	}
	public Vertex(double label) {
		initId();
		initData();
		setLabel(Double.toString(label));
		initColor();
	}
	@SuppressWarnings("ConstantConditions")
	public Vertex(@NotNull Vertex<T> vertex) {
		if (vertex == null)
			throw new NullPointerException();
		
		setId(vertex.getId());
		setData(vertex.getData());
		setLabel(vertex.getLabel());
		setColor(vertex.color);
	}
	public Vertex() {
		initId();
		initData();
		initLabel();
		initColor();
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
	
	@Nullable
	public Color getColor() {
		return color;
	}
	
	public void setColor(@Nullable Color color) {
		this.color = color;
		snap(this.color);
	}
	
	protected void initColor() {
		setColor(null);
	}
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getId());
		stream.writeObject(getData());
		stream.writeBytes(getLabel());
		stream.writeObject(getColor());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setId((UUID) stream.readObject());
		setData((T) stream.readObject());
		setLabel(stream.readUTF());
		setColor((Color) stream.readObject());
	}
	
	/* OVERRIDES */
	
	/**
	 * Check if o is equal to this instance, regardless of the identifier.
	 * @param o The object to check
	 * @return Return {@code true} if o and this instance are equivalent, {@code false} otherwise.
	 */
	public boolean equivalent(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vertex)) return false;
		Vertex<?> vertex = (Vertex<?>) o;
		return Objects.equals(getData(), vertex.getData()) &&
				Objects.equals(getLabel(), vertex.getLabel()) &&
				Objects.equals(getColor(), vertex.getColor());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!equivalent(o))
			return false;
		Vertex<?> vertex = (Vertex<?>) o;
		return Objects.equals(getId(), vertex.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getData(), getLabel(), getColor());
	}
	
	@Override
	public String toString() {
		return "Vertex{" +
				"id=" + id +
				", data=" + data +
				", label='" + label + '\'' +
				", color=" + color +
				'}';
	}
}
