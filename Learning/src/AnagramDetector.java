import java.util.Arrays;
import java.util.Scanner;
public class AnagramDetector {

    private static StringBuilder sort(String input) {
        input = input.toLowerCase();
        input = input.replaceAll(" ", "");
        char[] temp = input.toCharArray();
        Arrays.sort(temp);
        StringBuilder sorted = new StringBuilder(new String(temp));
        return sorted;
    }

    public static void main(String[] args) throws Exception {
        test test = new test();
        test.keyPressed(null);
        try (Scanner scan = new Scanner(System.in)) {
            Printer printer = new Printer();

            printer.print("Please Enter Your First Input");
            String input1 = scan.nextLine();
            printer.print("Please Enter Your Second Input");
            String input2 = scan.nextLine();
            printer.print(" ");
            
            if (sort(input1).toString().contentEquals(sort(input2))) {
                printer.print("Match :)");
            }
            else {
                printer.print("No match :( ");
            }
            printer.print(" ");
        }
        
        
        
        
    }
}
