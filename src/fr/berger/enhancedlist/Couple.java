package fr.berger.enhancedlist;

import java.awt.datatransfer.Transferable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Couple class. It contains two data {@code x} and {@code y}
 * @author Valentin Berger
 * @param <T> The type of {@code x}
 * @param <U> The type of {@code y}
 * @see Point
 */
public class Couple<T, U> implements Serializable {
	
	private T x;
	private U y;
	
	/**
	 * Default constructor. Set {@code x} and {@code y} with {@code null}
	 */
	public Couple() {
		x = null;
		y = null;
	}
	/**
	 * Set {@code x} and {@code y} with the given value
	 * @param x The value of x
	 * @param y The value of y
	 */
	public Couple(T x, U y) {
		setX(x);
		setY(y);
	}
	
	/* GETTERS & SETTERS */
	
	public T getX() {
		return x;
	}
	
	public void setX(T x) {
		this.x = x;
	}
	
	public U getY() {
		return y;
	}
	
	public void setY(U y) {
		this.y = y;
	}
	
	/* OVERRIDES */
	
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
