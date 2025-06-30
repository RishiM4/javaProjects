import java.util.ArrayList;

public class DuplicateDetector {
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
        ArrayList<Integer> result = new ArrayList<Integer>();
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
    private static Integer search(Integer[] arr, int target) {
        return search(arr, target, 0, arr.length-1,1);
        
    }

    private static Integer search(Integer[] arr, int target, int lb, int ub, int depth) {
        int m = lb + (ub - lb) / 2;
        if (arr[m] == target) {
            return m;
        } 
        else if (lb == ub && arr[m] != target) {
            return -1;
        }
        else if (arr[m] > target){
            search(arr, target, lb, m, depth+1);
        }
        else {
            search(arr, target, m+1, ub, depth+1);
        }
        return -1;
    }
    public static void main(String[] args) {
        Integer[] arr1 = {1,4,9,7,8,10,15};
        Integer[] arr2= {5,20,17,8};
        arr2 =  merge(arr2);
        for(int i = 0; i < arr1.length; i++) {
            if (search(arr2, arr1[i]) != -1) {
                System.out.println(arr1[i]);
            }
        }
    }
}
