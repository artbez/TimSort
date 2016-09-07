package timsort;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public abstract class ArraySortTest<T extends Comparable<T>> {
	
	public abstract T[] generateRandomArray(final int arraySize, final int seed);

	public final void test(final TimSorter<T> timSorter, final int arraySize, final int seed) {
		assertTrue("Array must be not null", arraySize != 0);
		T array[] = generateRandomArray(arraySize, seed);
		T arrJava[] = array.clone();
		
		long startTime = System.nanoTime();
		timSorter.sort(array);
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("Timsort execution time(ms) " + (estimatedTime / 1000000));
		
		startTime = System.nanoTime();
		Arrays.sort(arrJava);
		estimatedTime = System.nanoTime() - startTime;
		System.out.println("Arrays.sort execution time(ms) " + (estimatedTime / 1000000));
		
		assertTrue("Array must contain at least one element", array[0] != null);
		T previousValue = array[0];
		for (int i = 1; i < array.length; i++) {
			assertTrue("Element " + array[i] + " at " + i + " position is not in the order", array[i].compareTo(previousValue) >= 0);
			previousValue = array[i];
		}
	}
}
