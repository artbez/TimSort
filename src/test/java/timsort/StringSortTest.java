package timsort;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;

/**
 * Test TimSort with String objects.
 * <p>
 * Also override generateRandomArray as an abstract method in the superclass.
 */
public class StringSortTest extends ArraySortTest<String> {
	
	/** Seed for the pseudo random generator */
	public static final int SEED = 1;
	/** Array size */
	public static final int ARRAY_SIZE = 1000000;

	@Test
	public void testSortArray() throws Exception {
		final TimSort<String> timSorter = new TimSort<>();
		test(timSorter, ARRAY_SIZE, SEED);
	}

	@Override
	public String[] generateRandomArray(final int arraySize, final int seed) {
		String array[] = new String[arraySize];
		Random rnd = new Random(seed);
		Arrays.setAll(array, i -> new BigInteger(500, rnd).toString(32));
		return array;
	}
}
