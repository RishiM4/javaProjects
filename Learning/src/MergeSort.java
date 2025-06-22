import java.util.ArrayList;

public class MergeSort {
    private static Integer[] merge(Integer[] arr) {
        int m = (arr.length / 2);
        
        Integer arr1[] = merge(arr, 0, m);
        Integer arr2[] = merge(arr, m + 1, arr.length-1);
        return sort(arr1, arr2);
    }
    private static Integer[] merge(Integer[] arr, int l, int h) {
        Integer arr1[];
        Integer arr2[];
        if (l == h) {
            Integer t[] = {arr[l]};
            return t;
        }
        else {
            int m = l + (h - l) / 2;
            arr1 = merge(arr, l, m);
            arr2 = merge(arr, m + 1, h);
        }
        return sort(arr1, arr2);
    }
    private static Integer[] sort(Integer[] arr1, Integer[] arr2) {
        int k = 0; 
        int j = 0;
        ArrayList<Integer>  result = new ArrayList<Integer>();
        while(k < arr1.length && j < arr2.length) {
            if (arr1[k] < arr2[j]) {
                result.add(arr1[k]);
                k++;
            }
            else {
                result.add(arr2[j]);
                j++;
            }
        }
        if (k < arr1.length) {
            for(int i = k; i < arr1.length; i++) {
                result.add(arr1[i]);
            }
        }
        else if (j < arr2.length) {
            for(int i = j; i < arr2.length; i++) {
                result.add(arr2[i]);
            }
        }
        Integer a[] = new Integer[result.size()];
        return result.toArray(a);
        
    }

    public static void main(String[] args) {
        
        Integer arr1[] = {5,6,1,6,8};
        Integer arr[] = merge(arr1);
        for(int i = 0; i < arr.length - 1; i++) {
            System.out.println(arr[i]);
        }
        
        
    }
}
