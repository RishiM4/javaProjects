import java.util.Scanner;
import java.util.Random;
public class WordleThreeLetter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_GRAY = "\u001B[90m";
    static String[] storedGuesses = {"","","","","",""};
    static int guessNumber = 0;
    
    private static String chooseWord() {
        Random random = new Random();
        String[] words = new String[] {"ADD", "AID", "AIM", "AIR", "AWE", "FIT", "FUN", "GOD", "HOT", "JOY", "NEW", "VOW", "WIN", "WON", "WOW", "YAY", "YES"};
        String result;
        result = words[random.nextInt(17)];
        //System.err.println(result);
        return result;
        
    }
    private static String prepareWord(String input) {
        String output = input;
        if(input.length() > 3) {
            output =  "" + input.charAt(0) + input.charAt(1) + input.charAt(2);
        }
        for (int k = output.length(); k < 3;) {
            output = output + "#";
            k = output.length();
        }
        output = output.toUpperCase();
        return output;

    }
    private static Boolean checkForGreen(String input, String answer, int index) {
        Boolean output = false;
        if (input.charAt(index) == answer.charAt(index)) {
            output = true;
        }
        return output;
    }
    private static Boolean checkForYellow(String input, String answer, int index) {
        String temp = "" + input.charAt(index);
        
        for (int k = 0; k<3; k++) {
            if (temp.equals("" + answer.charAt(k)) ) {
                return true;
                
            }
        }
        return false;
    }
    private static String convertToWord (String result, String input) {
        String output = "";
        for(int k = 0; k < 3; k++) {
            if (result.charAt(k) == 'G') {
                output = output + ANSI_GREEN + input.charAt(k) + ANSI_RESET;
            }
            else if (result.charAt(k) == 'Y') {
                output = output + ANSI_YELLOW + input.charAt(k) + ANSI_RESET;
            }
            else {
                output = output + ANSI_GRAY + input.charAt(k) + ANSI_RESET;
            }
        }
        return output;
    }
    private static void printGuesses(String guess) {
        storedGuesses[guessNumber] = guess;
        guessNumber++;
        for(int k = 0; k < 31; k++){
            System.err.println("");
        }
        for(int k = 0; k < guessNumber; k++) {
            System.err.println(storedGuesses[k]);
        }
    }
    private static boolean compileResults(String input, String answer) {
        StringBuffer output = new StringBuffer("XXX");
        String currentGuess = input;
        input = prepareWord(input);
        currentGuess = prepareWord(currentGuess);
        for(int p = 0; p < 3; p++) {
            if(checkForYellow(currentGuess, answer, p)) {
                output.setCharAt(p, 'Y');
            }
        }
        for(int p = 0; p < 3; p++) {
            if(checkForGreen(currentGuess, answer, p)) {
                output.setCharAt(p, 'G');
                
            }
        }
        if(output.toString().equals("GGG")) {
           System.err.println("");
           System.err.println("Congratulations!");
           System.err.println("You have won!");
           System.err.println("Thanks for playing!");
           System.err.println("");
           return true;
        }
       
        
        printGuesses(convertToWord(output.toString(), currentGuess));
        //System.err.println(convertToWord(output.toString(), currentGuess));
        return false;
    }
    public static void main(String[] args) throws Exception {
        Printer printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        String answer = chooseWord();
        
        for(int k = 0; k <= 5; k++) {
            String currentGuess = scanner.nextLine();
            if (compileResults(currentGuess, answer)) {
                scanner.close();
                return;
            }
        }
        
        scanner.close();
        printer.print("Out of Guesses :(");
        printer.print("The word was " + answer + ".");
        printer.print("Try Again Next Time!");
        printer.print("");
        
    }
}
