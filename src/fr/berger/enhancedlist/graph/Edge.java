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

public class Edge<T> extends EnhancedObservable implements Serializable, Cloneable {
	
	@NotNull
	private UUID id;
	@Nullable
	private T data;
	@NotNull
	private Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> link;
	
	/* CONSTRUCTORS */
	
	public Edge(@NotNull UUID id, @Nullable T data, @NotNull Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> link) {
		setId(id);
		setData(data);
		setLink(link);
	}
	public Edge(@Nullable T data, @NotNull Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> link) {
		initData();
		setData(data);
		setLink(link);
	}
	public Edge(@NotNull Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> link) {
		initData();
		initData();
		setLink(link);
	}
	public Edge(@NotNull Ref<Vertex<?>> r1, Ref<Vertex<?>> r2) {
		initData();
		initData();
		setLink(new Couple<>(r1, r2));
	}
	public Edge(@NotNull Vertex<?> v1, Vertex<?> v2) {
		initData();
		initData();
		setLink(new Couple<>(new Ref<>(v1), new Ref<>(v2)));
	}
	public Edge(@Nullable T data) {
		initData();
		setData(data);
		initLink();
	}
	public Edge() {
		initId();
		initData();
		initLink();
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
	
	@SuppressWarnings("ConstantConditions")
	@NotNull
	public Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> getLink() {
		if (link == null)
			initLink();
		
		return link;
	}
	
	@SuppressWarnings("ConstantConditions")
	public void setLink(@NotNull Couple<Ref<Vertex<?>>, Ref<Vertex<?>>> link) {
		if (link == null)
			throw new NullPointerException();
		
		this.link = link;
		snap(this.link);
	}
	
	protected void initLink() {
		setLink(new Couple<>(null, null));
	}
	
	/* SERIALIZATION OVERRIDES */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getId());
		stream.writeObject(getData());
		stream.writeObject(getLink());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setId((UUID) stream.readObject());
		setData((T) stream.readObject());
		setLink((Couple<Ref<Vertex<?>>, Ref<Vertex<?>>>) stream.readObject());
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;
		Edge<?> edge = (Edge<?>) o;
		return Objects.equals(getId(), edge.getId()) &&
				Objects.equals(getData(), edge.getData()) &&
				Objects.equals(getLink(), edge.getLink());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getData(), getLink());
	}
	
	@Override
	public String toString() {
		return "Edge{" +
				"id=" + id +
				", data=" + data +
				", link=" + link +
				'}';
	}
}
