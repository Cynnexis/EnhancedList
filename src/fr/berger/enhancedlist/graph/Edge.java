package fr.berger.enhancedlist.graph;

import fr.berger.arrow.Ref;
import fr.berger.beyondcode.util.EnhancedObservable;
import fr.berger.enhancedlist.Couple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Edge<T> extends Couple<Vertex<?>, Vertex<?>> implements Serializable, Cloneable {
	
	@NotNull
	private UUID id;
	@Nullable
	private T data;
	
	/* CONSTRUCTORS */
	
	public Edge(@NotNull UUID id, @Nullable T data, @NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		setId(id);
		setData(data);
	}
	public Edge(@Nullable T data, @NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		initData();
		setData(data);
	}
	public Edge(@NotNull Vertex<?> x, @NotNull Vertex<?> y) {
		super(x, y);
		initData();
		initData();
	}
	public Edge(@Nullable T data) {
		super();
		initData();
		setData(data);
	}
	public Edge() {
		super();
		initId();
		initData();
	}
	
	/* EDGE METHODS */
	
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
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getId());
		stream.writeObject(getData());
		stream.writeObject(getX());
		stream.writeObject(getY());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setId((UUID) stream.readObject());
		setData((T) stream.readObject());
		setX((Vertex<?>) stream.readObject());
		setY((Vertex<?>) stream.readObject());
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;
		if (!super.equals(o)) return false;
		Edge<?> edge = (Edge<?>) o;
		return Objects.equals(getId(), edge.getId()) &&
				Objects.equals(getData(), edge.getData()) &&
				Objects.equals(getX(), edge.getX()) &&
				Objects.equals(getY(), edge.getY());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getId(), getData());
	}
	
	@Override
	public String toString() {
		return "Edge{" +
				"id=" + id +
				", x=" + getX() +
				", y=" + getY() +
				", data=" + data +
				'}';
	}
}
