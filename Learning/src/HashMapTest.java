import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
public class HashMapTest {
    public static void main(String[] args) throws Exception {
        Printer printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        HashMap<Character, Integer> characterIndex = new HashMap<Character, Integer>();
        String input = scanner.nextLine();
        scanner.close();
        
        for(int k = 0; k <= input.length()-1; k++) {
            Character currentChar = input.charAt(k);
            if (characterIndex.containsKey(currentChar)) {
                int p = characterIndex.get(currentChar);
                characterIndex.replace(currentChar, p, p+1);
            }
            else {
                characterIndex.put(currentChar, 1);
            }
        }
        String text = "Hell world!"; // Your text data
        try (PrintWriter out = new PrintWriter("test.txt")) {
            out.println(text + characterIndex.toString() + characterIndex); // Write the text to the file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        printer.print(characterIndex.toString());
        
    }
}