package fr.berger.enhancedlist;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Couple class. It contains two data {@code x} and {@code y}
 * @param <T> The type of {@code x}
 * @param <U> The type of {@code y}
 * @see Point
 * @author Valentin Berger
 */
public class Couple<T, U> extends EnhancedObservable implements Serializable, Cloneable {
	
	@Nullable
	private T x;
	@Nullable
	private U y;
	
	/**
	 * Set {@code x} and {@code y} with the given value
	 * @param x The value of x
	 * @param y The value of y
	 */
	public Couple(@Nullable T x, @Nullable U y) {
		setX(x);
		setY(y);
	}
	/**
	 * Default constructor. Set {@code x} and {@code y} with {@code null}
	 */
	public Couple() {
		this(null, null);
	}
	
	/* GETTERS & SETTERS */
	
	@Nullable
	public T getX() {
		return x;
	}
	
	public void setX(@Nullable T x) {
		this.x = x;
		snap(this.x);
	}
	
	@Nullable
	public U getY() {
		return y;
	}
	
	public void setY(@Nullable U y) {
		this.y = y;
		snap(this.y);
	}
	
	/* SERIALIZATION METHODS */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getX());
		stream.writeObject(getY());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setX((T) stream.readObject());
		setY((U) stream.readObject());
	}
	
	/* OVERRIDES */
	
	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Couple)) return false;
		Couple<?, ?> couple = (Couple<?, ?>) o;
		return Objects.equals(getX(), couple.getX()) &&
				Objects.equals(getY(), couple.getY());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}
	
	@Override
	public String toString() {
		return "Couple{" +
				"x=" + (x != null ? x.toString() : "(null)") +
				", y=" + (y != null ? y.toString() : "(null)") +
				'}';
	}
}
