import java.util.Scanner;

public class BinaryConverter {
    private static void solve(Bit[] results, int input, int depth) {
        if (input == 1) {
            results[depth] = new Bit(1);
            return;
        }
        if (((int) input / 2) == ((double) input / 2)) {
            results[depth] = new Bit(0);
            solve(results, input/2, depth+1);
        }
        else {
            results[depth] = new Bit(1);
            solve(results, input/2, depth+1);
        }
    }
    public static void main(String[] args) {
        
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please provide a number to be converted into binary");
            int input = 39;
            Bit results[] = new Bit[(int) (Math.log(input) / Math.log(2))+1];
            solve(results, input, 0);
            for(int i = results.length-1; i >= 0; i--) {
                System.out.print(results[i]);
            }
            scanner.close();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
