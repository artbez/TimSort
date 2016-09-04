package timsort;

import org.junit.Test;
import java.util.Arrays;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class IntSortTest {
	public static final int SEED = 1;
	public static final int ARRAY_SIZE = 1000000;
	public static final TimSort timSorter = new TimSort();

	/**
	 *
	 * @param size
	 *            array size
	 * @param seed
	 *            for the pseudo random generator
	 * @return random generated int array. It will be the same for the same seed
	 *         and size.
	 */
	int[] generateRandomIntArray(int size, long seed) {
		int array[] = new int[size];
		Random rnd = new Random(seed);
		for (int i = 0; i < array.length; i++) {
			array[i] = rnd.nextInt();
		}
		return array;
	}

	@Test
	public void testSortArray() throws Exception {
		int array[] = generateRandomIntArray(ARRAY_SIZE, SEED);

		// сортируем массив и замеряем время работы
		long startTime = System.nanoTime();
		timSorter.apply(array);
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Execution time(ms) " + (estimatedTime / 1000000));

		// проверяем правильность сортировки
		int previousValue = Integer.MIN_VALUE;
		for (int i = 0; i < array.length; i++) {
			assertTrue("Element " + array[i] + " at " + i + " position is not in the order", array[i] >= previousValue);
			previousValue = array[i];
		}
	}
}