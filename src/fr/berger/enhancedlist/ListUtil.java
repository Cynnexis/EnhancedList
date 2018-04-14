package fr.berger.enhancedlist;

import fr.berger.beyondcode.util.Irregular;
import fr.berger.enhancedlist.lexicon.Lexicon;
import fr.berger.enhancedlist.matrix.Matrix;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.ArrayList;
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
	
	@SuppressWarnings("Duplicates")
	public static <T> void swap(@NotNull List<T> list, int index1, int index2) throws IndexOutOfBoundsException {
		checkIndexException(index1, list);
		checkIndexException(index2, list);
		
		T temp = list.get(index1);
		list.set(index1, list.get(index2));
		list.set(index2, temp);
	}
	@SuppressWarnings("Duplicates")
	public static <T> void swap(@NotNull Lexicon<T> lexicon, int index1, int index2) {
		checkIndexException(index1, lexicon);
		checkIndexException(index2, lexicon);
		
		// Stop the duplicates-rule just for the swap
		boolean acceptDuplicates = lexicon.isAcceptDuplicates();
		if (!acceptDuplicates)
			lexicon.setAcceptDuplicates(true);
		
		T temp = lexicon.get(index1);
		lexicon.set(index1, lexicon.get(index2));
		lexicon.set(index2, temp);
		
		// Re-activate the duplicate-rule after the swap
		if (!acceptDuplicates)
			lexicon.setAcceptDuplicates(false);
	}
	
	public static <T> List<T> disarray(@NotNull List<T> list) {
		if (list == null)
			throw new NullPointerException();
		
		/**
		 * Save all the indexes changed (x = the old index ; y = the new index)
		 */
		ArrayList<Couple<Integer, Integer>> indexes = new ArrayList<>(list.size());
		
		/**
		 * Contain all the available indexes left. At the beginning, it is full of number from 0 to `list.size() - 1`,
		 * then, iteration after iteration, it will delete one by one the indexes until it is empty.
		 */
		ArrayList<Integer> availableIndexes = new ArrayList<>(list.size());
		
		// Fill availableIndexes list
		for (int i = 0, max = list.size(); i < max; i++)
			availableIndexes.add(i);
		
		// Assign for every element in list a new unique integer
		for (int i = 0, max = list.size(); i < max && !availableIndexes.isEmpty(); i++) {
			// Generate a new index according to `availableIndexes` list
			int randomIndex;
			int newIndex;
			
			if (i + 1 >= max)
				randomIndex = 0;
			else
				randomIndex = Irregular.rangeInt(0, true, availableIndexes.size(), false);
			newIndex = availableIndexes.get(randomIndex);
			
			// Delete the index in `availableIndexes` list
			availableIndexes.remove(randomIndex);
			
			// Update `indexes` list
			indexes.add(new Couple<>(i, newIndex));
		}
		
		// Now, swap all the elements according to `indexes`
		for (Couple<Integer, Integer> ind : indexes)
			swap(list, ind.getX(), ind.getY());
		
		return list;
	}
	public static <T> Lexicon<T> disarray(@NotNull Lexicon<T> lexicon) {
		if (lexicon == null)
			throw new NullPointerException();
		
		List<T> newList = disarray(lexicon.toList());
		lexicon.fromList(newList);
		
		return lexicon;
	}
}
