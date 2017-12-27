package fr.berger.enhancedlist;

import java.io.Serializable;
import java.util.Objects;

/**
 * Point class. It contains two integers
 * @author Valentin Berger
 * @see Couple
 */
public class Point extends Couple<Integer, Integer> implements Serializable {
	
	/**
	 * Default constructor. Set {@code x} and {@code y} with 0
	 */
	public Point() {
		setX(0);
		setY(0);
	}
	/**
	 * Set {@code x} and {@code y} with the given values
	 * @param x The value of x
	 * @param y The value of y
	 */
	public Point(int x, int y) {
		setX(x);
		setY(y);
	}
	
	/* OVERRIDES */
	@Override
	public String toString() {
		return "Point{" +
				"x=" + (getX() != null ? getX().toString() : "(null)") +
				", y=" + (getY() != null ? getY().toString() : "(null)") +
				'}';
	}
}
