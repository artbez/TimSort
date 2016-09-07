package timsort;

import org.junit.Test;
import java.util.Arrays;
import java.util.Random;

/**
 * Test TimSort with Integer objects.
 * <p>
 * Also override generateRandomArray as an abstract method in the superclass.
 */
public class IntSortTest extends ArraySortTest<Integer>{
	
	/** Seed for the pseudo random generator */
	public static final int SEED = 1;
	/** Array size */
	public static final int ARRAY_SIZE = 1000000;
	
	@Test
	public void testSortArray() throws Exception {
		final TimSort<Integer> timSorter = new TimSort<>();
		test(timSorter, ARRAY_SIZE, SEED);
	}

	@Override
	public Integer[] generateRandomArray(final int arraySize, final int seed) {
		Integer array[] = new Integer[arraySize];
		Random rnd = new Random(seed);
		Arrays.setAll(array, i -> rnd.nextInt());
		return array;
	}
}
