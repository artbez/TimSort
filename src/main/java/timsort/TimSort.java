package timsort;

import java.lang.reflect.Array;
import java.util.Stack;
import java.util.stream.IntStream;


/**
 * TimSorting Algorithm.
 *<p>
 * Method {@code sort} sorts an array of Comparable objects
 * Algorithm contains 3 steps: 
 * <ol>
 * <li> get the best length of arrays for sorting by insertion sort - {@code getMinrun()}</li>
 * <li> divides array to small subarrays and sorts them - {@code prepareArray()}</li>
 * <li> merges the subarrays in specific order - {@code mergeInside()} </li>
 * </ol>
 *<p>
 * Year of algorithm : 2002
 * <p>
 * Author of algorithm : Tim Peters  
 * <p>
 * Source: https://habrahabr.ru/company/infopulse/blog/133303/
 * 
 * @param <T> is a class of array elements
 */
public class TimSort<T extends Comparable<? super T>> {
	
	/** 
	 * Const for galop.
	 * <p>
	 * Used in merging two arrays.
	 * It is a number which describes how many elements from the second array in a row
	 * more then one fixed element from the first array.
	 * Negative value means that arrays change places with each other.  
	 */
	private final int CONST_FOR_GALOP = 7;
	
	/** Initial array. */
	private T[] array;
	/** The minimum size of subarrays in the initial array.  */
	private int minrun = 0;
	/** Stack of pairs with index of begin of subarray and its length. */
	private Stack<PairOfSubarray> stack;

	/**
	 * Sorts array from parameter.
	 * <p>
	 * Includes 3 steps described above class definition. 
	 * 
	 * @param array of T objects
	 * @return sorted array
	 */
	public T[] sort(T[] array) {
		assert(array != null);
		this.array = array;
		stack = new Stack<PairOfSubarray>();
		minrun = getMinrun();
		prepareArray();
		mergeInside();
		return array;
	}
	
	/** 
	 * Used for subarrays in the array.
	 * <p>
	 * Includes subarray's index of begin and length.
	 * Allows get an index of end. 
	 */
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
	
	/**
	 * Calculates better length of subarrays.
	 * 
	 * @return minrun
	 */
	private int getMinrun() {
		int r = 0;
		int n = array.length;
	    while (n >= 64) {
	        r |= n & 1;
	        n >>= 1;
	    }
	    return n + r;
	}
	
	/**
	 * Sequentially sorts small subarrays with length >= minrun by insertion binary sort.
	 * <p>
	 * Before that searches just sorted subarrays (From smallest to largest. Otherwise, reverses current subarray).
	 * If length of sorted subarray is less then minrun, then supplements this subarray by next (mintun - length) elements.
	 * Sorts them.  
	 */
	private void prepareArray() {
		int i = 0;
		int j = 0;
		while (i < array.length) {
			j = i;
			j = IntStream.range(i, array.length - 1).filter(s -> array[s].compareTo(array[s + 1]) <= 0).findFirst().orElse(array.length - 1);
			reverseInitialArray(i, j);
			j = IntStream.range(i, array.length - 1).filter(s -> array[s].compareTo(array[s + 1]) > 0).findFirst().orElse(array.length - 1);
			int localLength = Math.min(Math.max(j - i + 1, minrun), array.length - i);
			insertBinarySort(i, i + localLength);
			i += localLength;
		}
	}
	
	/**
	 * Merges subarrays in specific order.
	 * <p>
	 * For more info see source link in the javadoc description of the class.      
	 */
	private void mergeInside() {
		
		int start = 0;	
		do {
 
			int curRun = 1;
			curRun = IntStream.range(start + 1, array.length).filter(s -> array[s].compareTo(array[s - 1]) < 0).findFirst().orElse(array.length);
            stack.push(new PairOfSubarray(start, curRun - start));
            
            while (stack.size() > 1) {
            	PairOfSubarray elementX = stack.pop();
            	PairOfSubarray elementY = stack.pop();
            	if (stack.size() > 0 && stack.peek().length <= elementX.length + elementY.length) {
            		if (stack.peek().length < elementX.length) {
            			PairOfSubarray elementZ = stack.pop();
                    	mergeStackArrays(elementZ, elementY);
                    	stack.push(elementX);
            		} else {
            			mergeStackArrays(elementY, elementX);
            		}
            	} else if (elementY.length <= elementX.length){
            		mergeStackArrays(elementY, elementX);
            	} else {
            		stack.push(elementY);
            		stack.push(elementX);
            		break;
            	}
            } 
           
            start = curRun;        
        } while (array.length - start != 0);
		
		
		while (stack.size() != 1)
			mergeStackArrays(stack.pop(), stack.pop());
	}
	
	/**
	 * Merges two sorted arrays
	 * 
	 * @param p1 describes first array
	 * @param p2 describes second array
	 */
	private void mergeStackArrays(PairOfSubarray p1, PairOfSubarray p2) {	
		assert(array.getClass().getComponentType() != null);
		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), p1.length + p2.length);
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
	}
	
	private int binSearch(int low, int hight, T tmp) {
		int left = low;
        int right = hight;
        while (left < right){
            int middle = (left + right) / 2;
            if (tmp.compareTo(array[middle]) >= 0)
                left = middle + 1;
            else
                 right = middle;
        }
        return left;
	}
	
	private void swap(int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	
	private void reverseInitialArray(int low, int hight) {
		IntStream.range(low, (low + hight) / 2 + 1).parallel().forEach(i -> swap(i, hight - i + low));
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
