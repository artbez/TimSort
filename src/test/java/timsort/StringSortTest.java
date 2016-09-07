package timsort;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

public class StringSortTest {
	public static final int SEED = 1;
	public static final int ARRAY_SIZE = 1000000;
	public static final TimSorter<String> timSorter = new TimSorter<>();

	/**
	 *
	 * @param size
	 *            array size
	 * @param seed
	 *            for the pseudo random generator
	 * @return random generated int array. It will be the same for the same seed
	 *         and size.
	 */
	String[] generateRandomStringArray(int size, long seed) {
		String array[] = new String[size];
		Random rnd = new Random(seed);
		for (int i = 0; i < array.length; i++) {
			array[i] = new BigInteger(500, rnd).toString(32);
		}
		return array;
	}
	
	@Test
	public void testSortArray() throws Exception {
		String array[] = generateRandomStringArray(ARRAY_SIZE, SEED);
		String arrJava[] = array.clone();
		//Arrays.stream(array).forEach((a) -> System.out.print(a + " "));
		// сортируем массив и замеряем время работы
		long startTime = System.nanoTime();
		timSorter.sort(array);
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Timsort execution time(ms) " + (estimatedTime / 1000000));
		
		startTime = System.nanoTime();
		Arrays.sort(arrJava);
		estimatedTime = System.nanoTime() - startTime;
		System.out.println("Arrays.sort execution time(ms) " + (estimatedTime / 1000000));
		
		// проверяем правильность сортировки
		String previousValue = array[0];
		for (int i = 1; i < array.length; i++) {
			assertTrue("Element " + array[i] + " at " + i + " position is not in the order", array[i].compareTo(previousValue) >= 0);
			previousValue = array[i];
		}
	}
}
