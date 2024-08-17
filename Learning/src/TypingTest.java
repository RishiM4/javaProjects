import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import javax.swing.JFrame;


public class TypingTest {
    static Character keyChar;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m"; 
    public static final String ANSI_HIGHLIGHT_RED = "\u001B[41m";
    public static final String ANSI_HIGHLIGHT_GREEN = "\u001B[42m";
    static Timer timer = new Timer();
    static String answer = "";
    static int sentencesCompleted =0;
    static String stringOutput = "";
    static Random random  = new Random();
    static StringBuffer remaining = new StringBuffer(answer);
    private static int randomInt(int max){
        

        
        return random.nextInt(max);
        
    }
    
    private static String getPassword() {
        try {
            System.out.println(timer);
            timer.cancel();
            Path filePath = Paths.get("password.txt");
            List<String> lines = Files.readAllLines(filePath);
            sentencesCompleted=randomInt(lines.size());
            
            stringOutput = "";
            
            try {
                answer = lines.get(sentencesCompleted);
                remaining.replace(0, remaining.length(), answer);

            } catch (Exception e) {
                sentencesCompleted=0;
                answer =  lines.get(sentencesCompleted);
                remaining.replace(0, remaining.length(), answer);

            }
            

        } catch (Exception e) {
            
            
        }
        return "";
            
    }
    private static String convertToWord (String result, String input, int number) {
        if (result.charAt(number) == 'G') {
            stringOutput = stringOutput + ANSI_GREEN + input.charAt(number) + ANSI_RESET;
        }
        else {
            stringOutput = stringOutput + ANSI_RED + input.charAt(number) + ANSI_RESET;
        }
        for(int k = 0; k < 32; k++){
            System.err.println("");
        }
        remaining.deleteCharAt(0);
        System.out.println(stringOutput+remaining);
        return stringOutput;
    }
    private static StringBuffer compareChar(String input, int charNumber, Character current, StringBuffer result) {
        StringBuffer output = new StringBuffer(result);
        try {
            if (current.equals(input.charAt(charNumber))) {
                output.append("G");
            }
            else {
                output.append("R");
            }
            
        } 
        catch (Exception e) {
            test();
            return output; 
        }
        
        convertToWord(output.toString(), answer,charNumber);
        return output;
    }
    private static void test() {
        getPassword();
        JFrame frame = new JFrame("KeyListener Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true); // Request focus for key events
        for(int k = 0; k < 32; k++){
            System.err.println("");
        }
        System.out.println(answer);
        
       
        
        frame.addKeyListener(new KeyListener() {
            int digit = 0;
            StringBuffer output = new StringBuffer();
            public void keyPressed(KeyEvent e) {}

            
            public void keyReleased(KeyEvent e) {
                
                keyChar = e.getKeyChar();
                if (keyChar.equals(' ')) {
                    keyChar = '_';
                }
                String temp = ""+keyChar;
                
                if (temp.matches("^[A-Za-z0-9_-]+$")) {
                    output = compareChar(answer,digit, keyChar, output); 
                    keyChar = temp.charAt(0);       
                    digit++; 
                    
                    
                }
                else if (temp.matches(" ")){
                    temp.replaceAll(" ","_");
                    keyChar = temp.charAt(0);       
                    output = compareChar(answer,digit, keyChar, output); 
                    
                    digit++; 
                }
                else if (temp.matches(".")){
                    keyChar = temp.charAt(0);       
                    output = compareChar(answer,digit, keyChar, output); 
                    
                    digit++; 
                }
                else if (temp.matches("'")){
                    keyChar = temp.charAt(0);       
                    output = compareChar(answer,digit, keyChar, output); 
                    
                    digit++; 
                }
                else if (temp.matches(",")){
                    keyChar = temp.charAt(0);       
                    output = compareChar(answer,digit, keyChar, output); 
                    
                    digit++; 
                }
                
                
            }

            public void keyTyped(KeyEvent e) {}
            });
        
        
        
        
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        test();
    }
}
