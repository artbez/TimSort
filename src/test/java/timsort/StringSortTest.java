package timsort;

import java.math.BigInteger;
import java.util.Random;
import org.junit.Test;

public class StringSortTest extends ArraySortTest<String> {
	public static final int SEED = 1;
	public static final int ARRAY_SIZE = 1000000;

	@Test
	public void testSortArray() throws Exception {
		final TimSorter<String> timSorter = new TimSorter<>();
		test(timSorter, ARRAY_SIZE, SEED);
	}

	@Override
	public String[] generateRandomArray(final int arraySize, final int seed) {
		String array[] = new String[arraySize];
		Random rnd = new Random(seed);
		for (int i = 0; i < array.length; i++) {
			array[i] = new BigInteger(500, rnd).toString(32);
		}
		return array;
	}
}
