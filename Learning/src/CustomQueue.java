package src;

public class CustomQueue<T>{

    int start;
    int end;
    T[] arr;

    CustomQueue(Integer e) {
        if (e < 1) {
            throw new IllegalArgumentException();
        }
        
        this.start = 0;
        this.end = 0;
    }

    public void add(T e) {
        if ((end - start) > arr.length) {
            throw new IllegalStateException("Queue full");
        }
        if (start > 0 && end > 0) {
            start--;
            arr[start] = e;
        }
    }
    public static void main(String[] args) {
       
        
    }
}
