public class TowerOfHanoi {
    static int num = 0;
    private static CustomStack<Integer> solve(int numOfDiscs) {
        CustomStack<Integer> origin = new CustomStack<>();
        CustomStack<Integer> destination = new CustomStack<>();
        for (int i = numOfDiscs; i > 0; i--) {
            origin.push(i);
        }
        solve(numOfDiscs, origin, destination, new CustomStack<Integer>());
        return destination;
    }
    private static void solve(int disc, CustomStack<Integer> origin, CustomStack<Integer> destination, CustomStack<Integer> storage) {
        if (disc == 1) {
            destination.push(origin.peek());
            System.out.println("Moved Disc" + origin.peek() + " to destination");
            num++;
            origin.pop();
            return;
        }
        else {
            solve(disc-1, origin, storage, destination);
            destination.push(origin.peek());
            System.out.println("Moved Disc" + origin.peek() + " to destination");
            num++;
            origin.pop();
            
            solve(disc-1, storage, destination, origin);
        }
    }
    public static void main(String[] args) {
        int numOfDiscs = 5;

        solve(5).printStack();
        System.out.println("\n");
        System.out.println("Optimal Solution is " + (Math.pow(2, numOfDiscs)-1)+" code took " + num + " moves to solve.");
    }
}



