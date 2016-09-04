package timsort;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Stack;
import java.util.function.BinaryOperator;
import static org.junit.Assert.*;

/**
 * It's an implementation of Tim's Sorting algorithm
 * Source: https://habrahabr.ru/company/infopulse/blog/133303/
 * 
 */

// This class sorts the array of Comparable objects
public class TimSort {

	// Initial array
	private int[] array;
	// The minimum size of subarrays in the initial array 
	private int minrun = 0;
	// Stack of pairs with index of begin of subarray and its length 
	private Stack<Pair> stack;
	
	public int[] apply(int[] array) {
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
		while (i < array.length) {
			j = i;
			while(j < array.length - 1 && array[j] > array[j + 1]) {
				j++;
			}
			reverseInitialArray(i, j);
			while(j < array.length - 1 && array[j] <= array[j + 1]) {
				j++;
			}
			int localLength = Math.min(Math.max(j - i + 1, minrun), array.length - i);
			insertBinarySort(i, i + localLength);
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
						mergeStackArrays(elementY, elementZ);
						stack.push(elementX);			
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
		
		int[] result = new int[p1.length + p2.length];
		int count = 0;
        for (int i = 0, j = 0, k = 0; i < result.length; i++){
            if( j == p1.length){
                System.arraycopy(array, p2.indexOfBegin + k, result, i, p2.length - k);
                break;
            } else if (k == p2.length) {
            	System.arraycopy(array, p1.indexOfBegin + j, result, i, p1.length - j);
            	break;
            } else {

                if (count >= 7) {
                	int temp = array[p2.indexOfBegin + k];
                    int left=p1.indexOfBegin + j;
                    int right=p1.indexOfBegin + p1.length;
                    while (left<right){
                        int middle=(left+right)/2;
                        if (temp>=array[middle])
                            left=middle+1;
                        else
                             right=middle;
                    }    
                    System.arraycopy(array, p1.indexOfBegin + j, result, i, left-p1.indexOfBegin-j);
                    count = 0;
                    i += left - p1.indexOfBegin - j - 1;
                    j = left - p1.indexOfBegin;
                    continue;
            	} 
                
                if (count <= -7) {
                	int temp = array[p1.indexOfBegin + j];
                    int left=p2.indexOfBegin + k;
                    int right=p2.indexOfBegin + p2.length;
                    while (left<right){
                        int middle=(left+right)/2;
                        if (temp>=array[middle])
                            left=middle+1;
                        else
                             right=middle;
                    }    
                    System.arraycopy(array, p2.indexOfBegin + k, result, i, left-p2.indexOfBegin-k);
                    count = 0;
                    i += left - p2.indexOfBegin - k - 1;
                    k = left - p2.indexOfBegin;
                    continue;
            	} 

            	if (array[p1.indexOfBegin + j] < array[p2.indexOfBegin + k]) {
            		result[i] = array[p1.indexOfBegin +j++];
            		if (count > 0)
            			count++;
            		else
            			count = 1;
            	} else {
            		result[i] = array[p2.indexOfBegin + k++];
            		if (count < 0)
            			count--;
            		else
            			count = -1;
            	}
            }
        }
    
		int ll = Math.min(p1.indexOfBegin, p2.indexOfBegin);
		System.arraycopy(result, 0, array, ll, result.length);
		stack.push(new Pair(ll, p1.length + p2.length));
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
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private void reverseInitialArray(int start, int end) {
		for (int i = start; i < (start + end + 1) / 2; ++i) {
			swap(i, start + end - i);
		}
	}
	
	private void insertBinarySort(int start, int end) {
		for (int i = start + 1; i < end; ++i) {
			int j = i;
			while (j > start && array[j]< (array[j - 1]))
			{
				swap(j, j - 1);
				j--;
			}
		}
	}
	
}
