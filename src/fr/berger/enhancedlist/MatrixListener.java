package fr.berger.enhancedlist;

import java.io.Serializable;

/**
 * Listener Interface for the Matrix class
 * @author Valentin Berger
 * @param <T> The type of data the class Matrix uses
 * @see Matrix
 */
public interface MatrixListener<T> extends Serializable {
	
	/**
	 * Method called when a cell in the matrix is changed
	 * @param x The column index of the cell
	 * @param y The row index of the cell
	 * @param value The new value of the cell
	 */
	void OnCellChanged(int x, int y, T value);
	
	/**
	 * Method called when a cell in the matrix is read
	 * @param x The column index of the cell
	 * @param y The row index of the cell
	 * @param value The value of the cell
	 */
	void OnCellRead(int x, int y, T value);
}
