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
public class TimSorter<T extends Comparable<T>> {

	// Initial array
	private T[] array;
	// The minimum size of subarrays in the initial array 
	private int minrun = 64;
	// Stack of pairs with index of begin of subarray and its length 
	private Stack<PairOfSubarray> stack;
	
	private int stackSize = 0;
	
	private final int CONST_FOR_GALOP = 7;

	public T[] sort(T[] array) {
		assert(array != null);
		this.array = array;
		stack = new Stack<PairOfSubarray>();
		minrun = getMinrun();
		prepareArray();
		mergeInside();
		return array;
	}

	private class PairOfSubarray {
		int indexOfBegin;
		int length;
		
		PairOfSubarray(int indexOfBegin, int length) {
			this.indexOfBegin = indexOfBegin;
			this.length = length;
		}
		
		int getEndIndex() {
			return indexOfBegin + length;
		}
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
			while(j < array.length - 1 && array[j].compareTo(array[j + 1]) > 0) {
				j++;
			}
			reverseInitialArray(i, j);
			while(j < array.length - 1 && array[j].compareTo(array[j + 1]) <= 0) {
				j++;
			}
			int localLength = Math.min(Math.max(j - i + 1, minrun), array.length - i);
			insertBinarySort(i, i + localLength);
			i += localLength;
		}
	}
	
	private void mergeInside() {
		int ostLength = array.length;
		int start = 0;
		stackSize = 0;
		do {
           
			int curRun = 1;
			while (start + curRun < array.length && array[start + curRun].compareTo(array[start + curRun - 1]) >= 0) {
				curRun++;
			}
	
            stack.push(new PairOfSubarray(start, curRun));
            stackSize++;
         
            while (stackSize > 2) {
            	PairOfSubarray elementX = stack.pop();
            	PairOfSubarray elementY = stack.pop();
            	PairOfSubarray elementZ = stack.pop();
            	stackSize -= 3;
            	if (elementX.length <= elementY.length + elementZ.length && elementY.length <= elementZ.length) {
            		if (elementX.length < elementZ.length) {
            			stack.push(elementZ);
            			stackSize++;
            			mergeStackArrays(elementX, elementY);
            		} else {
            			mergeStackArrays(elementY, elementZ);
            			stack.push(elementX);
            			stackSize++;
            		}
            	} else {
            		break;
            	}
            }
       
            start += curRun;
            ostLength -= curRun;
        } while (ostLength != 0);
		
		if (stackSize == 2) {
			mergeStackArrays(stack.pop(), stack.pop());
			stackSize -=2;
		}
	}
	
	private void mergeStackArrays(PairOfSubarray p1, PairOfSubarray p2) {
		assert(array[0] != null);
		@SuppressWarnings("unchecked")
		T[] result =  (T[]) Array.newInstance(array[0].getClass(), p1.length + p2.length);
		int count = 0;
        for (int i = 0, j = p1.indexOfBegin, k = p2.indexOfBegin; i < result.length; i++){
            if (j == p1.getEndIndex()){
                System.arraycopy(array, k, result, i, p2.getEndIndex() - k);
                break;
            }  
            if (k == p2.getEndIndex()) {
            	System.arraycopy(array, j, result, i, p1.getEndIndex() - j);
            	break;
            } 

            if (array[j].compareTo(array[k]) < 0) {
            	result[i] = array[j++];
            	count = count > 0 ? count++ : 1;
            } else {
            	result[i] = array[k++];
            	count = count < 0 ? count-- : -1;
            }
            
            if (count >= CONST_FOR_GALOP) {
            	T tmp = array[k];
             	int left = binSearch(j, p1.getEndIndex(), tmp);
             	System.arraycopy(array, j, result, i, left - j);
             	count = 0;
                i += left - j - 1;
                j = left;
            } 
                
            if (count <= -CONST_FOR_GALOP) {
            	T tmp = array[j];
                int left = binSearch(k, p2.getEndIndex(), tmp);
                System.arraycopy(array, k, result, i, left - k);
                count = 0;
                i += left - k - 1;
                k = left - p2.indexOfBegin;
            } 
        }
    
		int indexBegin = Math.min(p1.indexOfBegin, p2.indexOfBegin);
		System.arraycopy(result, 0, array, indexBegin, result.length);
		stack.push(new PairOfSubarray(indexBegin, p1.length + p2.length));
		stackSize++;
	}
	
	private int binSearch(int low, int hight, T tmp) {
		int left = low;
        int right = hight;
        while (left < right){
            int middle = (left + right) / 2;
            if (tmp.compareTo(array[middle]) >= 0)
                left=middle + 1;
            else
                 right=middle;
        }
        return left;
	}
	
	private void swap(int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private void reverseInitialArray(int low, int hight) {
		for (int i = low; i < (low + hight + 1) / 2; ++i) {
			swap(i, low + hight - i);
		}
	}
	
	private void insertBinarySort(int low, int hight) {
		for (int i = low + 1; i < hight; ++i) {
			T tmp = array[i];
			int left = binSearch(low, i, tmp);
	        System.arraycopy(array, left, array, left + 1, i - left);
	        array[left] = tmp;
		}
	}
	
}
