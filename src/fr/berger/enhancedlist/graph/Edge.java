package fr.berger.enhancedlist.graph;

import fr.berger.enhancedlist.Couple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * An edge of a graph. It connected two vertices together (it can also connect one vertex to itself)
 * @param <T> The type parameter of the data that the vertex contains.
 * @see Graph
 * @see Vertex
 * @see Color
 * @author Valentin Berger
 */
public class Edge<T> extends Couple<Vertex<?>, Vertex<?>> implements Serializable, Cloneable {
	
	/**
	 * The unique identifier of this instance of edge It is mandatory because a lot of algorithm in Graph use
	 * the equal operations to compare two edges, and the data and the color does not make an edge unique in a graph.
	 * Therefore, an identifier is required.
	 * @see Graph
	 * @see UUID
	 */
	@NotNull
	private UUID id;
	
	/**
	 * The data contained by this edge (can be anything, even a null value)
	 */
	@Nullable
	private T data;
	
	/**
	 * The color of the edge if the graph is colored. If it not, the value of color is null. If the graph is coloring,
	 * the value of color is -1 until a color has been found for this edge.
	 * @see Color
	 */
	@Nullable
	private Color color;
	
	/* CONSTRUCTORS */
	
	public Edge(@NotNull UUID id, @Nullable T data, @NotNull Vertex<?> x, @NotNull Vertex<?> y, @Nullable Color color) {
		super(x, y);
		setId(id);
		setData(data);
		setColor(color);
	}
	public Edge(@NotNull UUID id, @Nullable T data, @NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		setId(id);
		setData(data);
		initColor();
	}
	public Edge(@Nullable T data, @NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		initData();
		setData(data);
		initColor();
	}
	public Edge(@NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		initData();
		initData();
		initColor();
	}
	public Edge(@Nullable T data) {
		super();
		initData();
		setData(data);
		initColor();
	}
	public Edge() {
		super();
		initId();
		initData();
		initColor();
	}
	
	/* EDGE METHODS */
	
	@NotNull
	public Edge<T> getSymmetry() {
		return new Edge<>(getId(), getData(), getY(), getX(), getColor());
	}
	
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
	
	public void initId() {
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
	
	@Nullable
	public Color getColor() {
		return color;
	}
	
	public void setColor(@Nullable Color color) {
		this.color = color;
	}
	
	protected void initColor() {
		setColor(null);
	}
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getId());
		stream.writeObject(getData());
		stream.writeObject(getColor());
		stream.writeObject(getX());
		stream.writeObject(getY());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setId((UUID) stream.readObject());
		setData((T) stream.readObject());
		setColor((Color) stream.readObject());
		setX((Vertex<?>) stream.readObject());
		setY((Vertex<?>) stream.readObject());
	}
	
	/* OVERRIDES */
	
	/**
	 * Check if o is equal to this instance, regardless of the identifier.
	 * @param o The object to check
	 * @return Return {@code true} if o and this instance are equivalent, {@code false} otherwise.
	 */
	public boolean equivalent(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;
		if (!super.equals(o)) return false;
		Edge<?> edge = (Edge<?>) o;
		return Objects.equals(getData(), edge.getData()) &&
				Objects.equals(getColor(), edge.getColor()) &&
				Objects.equals(getX(), edge.getX()) &&
				Objects.equals(getY(), edge.getY());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!equivalent(o)) return false;
		Edge<?> edge = (Edge<?>) o;
		return Objects.equals(getId(), edge.getId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getId(), getData(), getColor());
	}
	
	@Override
	public String toString() {
		return "Edge{" +
				"id=" + id +
				", x=" + getX() +
				", y=" + getY() +
				", data=" + data +
				", color=" + color +
				'}';
	}
}
