package timsort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Stack;
import java.util.function.BinaryOperator;

/**
 * It's an implementation of Tim's Sorting algorithm
 * Source: https://habrahabr.ru/company/infopulse/blog/133303/
 * 
 */

// This class sorts the array of Comparable objects
public class TimSort<T extends Comparable<T>> {

	// Initial array
	private T[] array;
	// The minimum size of subarrays in the initial array 
	private int minrun = 0;
	// Stack of pairs with index of begin of subarray and its length 
	private Stack<Pair> stack;
	
	public T[] apply(T[] array) {
		this.array = array;
		stack = new Stack<Pair>();
		minrun = getMinrun();
		prepareArray();
		mergeInside();
		return array;
	}
	
	private int getMinrun() {
		int r = 0;
		int n = array.length;
	    while (n >= 64) {
	        r |= n & 1;
	        n >>= 1;
	    }
	    return n + r;
	}
	
	private void prepareArray() {
		int i = 0;
		int j = 0;
		minrun = 3;
		while (i < array.length) {
			j = i;
			while(j < array.length - 1 && array[j].compareTo(array[j + 1]) > 0) {
				j++;
			}
			reverseInitialArray(i, j);
			while(j < array.length - 1 && array[j].compareTo(array[j + 1]) <= 0) {
				j++;
			}
			int localLength = Math.min(Math.max(j - i + 1, minrun), array.length - i);
			insertSort(i, i + localLength);
			stack.push(new Pair(i, localLength));
			i += localLength;
		}
	}
	
	private void mergeInside() {
		while (!stack.isEmpty()) {
			Pair elementX = stack.pop();
			if (!stack.isEmpty()) {
				Pair elementY = stack.pop();
				if (!stack.isEmpty()) {
					Pair elementZ = stack.peek();
					if ((elementX.length > elementY.length + elementZ.length && elementY.length > elementZ.length)
							|| (elementX.length < elementZ.length)) {
						mergeStackArrays(elementX, elementY);
					} else {
						stack.pop();
						stack.push(elementX);
						mergeStackArrays(elementY, elementZ);
					}
				} else {
					mergeStackArrays(elementX, elementY);
				}
			}
		}
	}
	
	private void mergeStackArrays(Pair p1, Pair p2) {
		if (array.length == 0)
			return;
		@SuppressWarnings("unchecked")
		BinaryOperator<T[]> merge = (t,u) -> {
			T[] result = (T[]) Array.newInstance(array[0].getClass(), t.length + u.length);
            for (int i = 0, j = 0, k = 0; i < result.length; i++){
                if( j == t.length){
                    result[i] = u[k++];
                } else if (k == u.length) {
                    result[i] = t[j++];
                } else {
                    result[i] = (t[j] .compareTo(u [k]) < 0) ? t[j++] : u[k++];
                }
            }
            return result;
        };
		T[] current = merge.apply(Arrays.copyOfRange(array, p1.indexOfBegin, p1.indexOfBegin + p1.length), 
				Arrays.copyOfRange(array, p2.indexOfBegin, p2.indexOfBegin + p2.length));
		for (int i = Math.min(p1.indexOfBegin, p2.indexOfBegin); i < Math.min(p1.indexOfBegin, p2.indexOfBegin) + p1.length + p2.length; ++i) {
			array[i] = current[i - Math.min(p1.indexOfBegin, p2.indexOfBegin)];
		}
		stack.push(new Pair(Math.min(p1.indexOfBegin, p2.indexOfBegin), p1.length + p2.length));
	}
	
	private class Pair {
		int indexOfBegin;
		int length;
		
		Pair(int indexOfBegin, int length) {
			this.indexOfBegin = indexOfBegin;
			this.length = length;
		}
	}
	
	private void swap(int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private void reverseInitialArray(int start, int end) {
		for (int i = start; i < (start + end + 1) / 2; ++i) {
			swap(i, start + end - i);
		}
	}
	
	private void insertSort(int start, int end) {
		for (int i = start + 1; i < end; ++i) {
			int j = i;
			while (j > start && array[j].compareTo(array[j - 1]) < 0)
			{
				swap(j, j - 1);
				j--;
			}
		}
	}
}
