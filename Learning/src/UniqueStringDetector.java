package src;
import java.util.HashMap;
import java.util.Scanner;
public class UniqueStringDetector {
    public static void main(String[] args) throws Exception {
        Printer printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        HashMap<Character, Integer> characterIndex = new HashMap<Character, Integer>();
        printer.print("Enter a word and it will tell you if it only has unique characters or not!");
        String input = scanner.nextLine();
        scanner.close();
        for(int k = 0; k <= input.length()-1; k++) {
            Character currentChar = input.charAt(k);
            if (characterIndex.containsKey(currentChar)) {
                printer.print("FAILED!!!");
                return;
            }
            else {
                characterIndex.put(currentChar, 1);
            }
        }
        printer.print(characterIndex.toString());
        
    }
}
