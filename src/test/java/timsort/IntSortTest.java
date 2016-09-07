package timsort;

import org.junit.Test;
import java.util.Random;

public class IntSortTest extends ArraySortTest<Integer>{
	public static final int SEED = 1;
	public static final int ARRAY_SIZE = 1000000;
	
	@Test
	public void testSortArray() throws Exception {
		final TimSorter<Integer> timSorter = new TimSorter<>();
		test(timSorter, ARRAY_SIZE, SEED);
	}

	@Override
	public Integer[] generateRandomArray(final int arraySize, final int seed) {
		Integer array[] = new Integer[arraySize];
		Random rnd = new Random(seed);
		for (int i = 0; i < array.length; i++) {
			array[i] = rnd.nextInt();
		}
		return array;
	}
}
