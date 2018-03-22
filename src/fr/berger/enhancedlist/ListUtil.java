package fr.berger.enhancedlist;

import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.matrix.Matrix;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.List;

public class ListUtil {
	
	public static boolean checkIndex(int index, int size) {
		return 0 <= index && index < size;
	}
	public static boolean checkIndex(int index, @NotNull List<?> list) {
		return checkIndex(index, list.size());
	}
	public static boolean checkIndex(int index, @NotNull AbstractCollection<?> list) {
		return checkIndex(index, list.size());
	}
	public static boolean checkIndex(int index, @NotNull Lexicon<?> list) {
		return checkIndex(index, list.size());
	}
	public static boolean checkIndex(int x, int y, int width, int height) {
		return 0 <= x && x < width && 0 <= y && y < height;
	}
	public static boolean checkIndex(int x, int y, @NotNull Matrix<?> matrix) {
		return checkIndex(x, y, matrix.getNbColumns(), matrix.getNbRows());
	}
	
	public static void checkIndexException(int index, int size) {
		if (!checkIndex(index, size))
			throw new IndexOutOfBoundsException("Index \"" + index + "\" is out of bound (size = \"" + size + "\")");
	}
	public static void checkIndexException(int index, @NotNull List<?> list) {
		checkIndexException(index, list.size());
	}
	public static void checkIndexException(int index, @NotNull AbstractCollection<?> list) {
		checkIndexException(index, list.size());
	}
	public static void checkIndexException(int index, @NotNull Lexicon<?> list) {
		checkIndexException(index, list.size());
	}
	public static void checkIndexException(int x, int y, int width, int height) {
		if (!checkIndex(x, y, width, height))
			throw new IndexOutOfBoundsException("Indexes (" + x + " ; " + y + ") are out of bounds (size = (" + width + " ; " + height + "))");
	}
	public static void checkIndexException(int x, int y, @NotNull Matrix<?> matrix) {
		checkIndexException(x, y, matrix.getNbColumns(), matrix.getNbRows());
	}
}
