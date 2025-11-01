package src;

public class BinarySearchInteger {
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
        Integer arr[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        search(arr,16);
        /*
         * 1 - 4
         * 2 - 3
         * 3 - 4
         * 4 - 2
         * 5 - 4
         * 6 - 3
         * 7 - 4
         * 8 - 1
         * 9 - 4
         * 10 - 3
         * 11 - 4
         * 12 - 2
         * 13 - 4
         * 14 - 3
         * 15 - 4
         * 16 - 5
         */
    }
}
