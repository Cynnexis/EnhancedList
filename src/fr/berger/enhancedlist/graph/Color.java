package fr.berger.enhancedlist.graph;

import fr.berger.beyondcode.util.EnhancedObservable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * Color is the representation of the color in the graph theory. In this project, it is represented by a positive
 * number.
 * @author Valentin Berger
 */
public class Color extends EnhancedObservable implements Serializable, Cloneable, Comparable<Color> {
	
	/**
	 * The color of the vertex/edge. If it is equal to -1, it means that the vertex/edge has not been colored yet.
	 * This number must be greater than zero.
	 */
	private long colorNumber;
	
	public Color(long colorNumber) {
		setColorNumber(colorNumber);
	}
	public Color() {
		initColorNumber();
	}
	
	/* COLOR METHOD */
	
	/* GETTER & SETTER */
	
	public long getColorNumber() {
		return colorNumber;
	}
	
	public void setColorNumber(long colorNumber) {
		if (colorNumber < -1)
			throw new IllegalArgumentException();
		
		this.colorNumber = colorNumber;
	}
	
	public void initColorNumber() {
		setColorNumber(-1);
	}
	
	/* SERIALIZATION METHODS */
	
	private void writeObject(@NotNull ObjectOutputStream stream) throws IOException {
		stream.writeLong(getColorNumber());
	}
	
	private void readObject(@NotNull ObjectInputStream stream) throws IOException {
		setColorNumber(stream.readLong());
	}
	
	/* COMPARABLE METHOD */
	
	@Override
	public int compareTo(@NotNull Color o) {
		return Long.compare(getColorNumber(), o.getColorNumber());
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Color)) return false;
		Color color = (Color) o;
		return getColorNumber() == color.getColorNumber();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getColorNumber());
	}
	
	@Override
	public String toString() {
		return Long.toString(colorNumber);
	}
}
