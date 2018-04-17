package fr.berger.enhancedlist;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Point class. It contains two integers
 * @author Valentin Berger
 * @see Couple
 */
public class Point extends Couple<Integer, Integer> implements Serializable, Cloneable {
	
	/**
	 * Set {@code x} and {@code y} with the given values
	 * @param x The value of x
	 * @param y The value of y
	 */
	public Point(int x, int y) {
		setX(x);
		setY(y);
	}
	/**
	 * Default constructor. Set {@code x} and {@code y} with 0
	 */
	public Point() {
		this(0, 0);
	}
	
	/* SERIALIZATION METHODS */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeObject(getX());
		stream.writeObject(getY());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(@NotNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
		setX((Integer) stream.readObject());
		setY((Integer) stream.readObject());
	}
	
	/* OVERRIDES */
	
	@Override
	public String toString() {
		return "(" + (getX() != null ? getX().toString() : "(null)") +
				" ; " + (getY() != null ? getY().toString() : "(null)") +
				')';
	}
}
