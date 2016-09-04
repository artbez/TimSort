package timsort;

import org.junit.Test;
import java.util.Arrays;

public class TimSortTest {

	@Test
	public void firstTest() {
		Integer[] array = {3, 4, 5, 9, 8, 7, 1, 2, 3, 4, 0, -1};
		TimSort timSorter = new TimSort();
		Arrays.stream(timSorter.apply(array)).forEach((item) -> System.out.print(item + " "));
	}
}
