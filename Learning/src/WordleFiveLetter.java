import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
public class WordleFiveLetter {
    
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_GRAY = "\u001B[90m";
    static String[] storedGuesses = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",};
    static int guessNumber = 0;
    static int mode = 1;
    static boolean quickFix  = false;
    static Printer printer = new Printer();
    static Scanner scanner = new Scanner(System.in);
    static int temp = 0;
    static JLabel A= new JLabel("A");
    static JLabel B= new JLabel("B");
    static JLabel C= new JLabel("C");
    static JLabel D= new JLabel("D");
    static JLabel E= new JLabel("E");
    static JLabel F= new JLabel("F");
    static JLabel G= new JLabel("G");
    static JLabel H= new JLabel("H");
    static JLabel I= new JLabel("I");
    static JLabel J= new JLabel("J");
    static JLabel K= new JLabel("K");
    static JLabel L= new JLabel("L");
    static JLabel M= new JLabel("M");
    static JLabel N= new JLabel("N");
    static JLabel O= new JLabel("O");
    static JLabel P= new JLabel("P");
    static JLabel Q= new JLabel("Q");
    static JLabel R= new JLabel("R");//COOLEST LETTER IN THE WORLD
    static JLabel S= new JLabel("S");//not so cool letter, only if your name starts with it
    static JLabel T= new JLabel("T");
    static JLabel U= new JLabel("U");
    static JLabel V= new JLabel("V");
    static JLabel W= new JLabel("W");
    static JLabel X= new JLabel("X");
    static JLabel Y= new JLabel("Y");
    static JLabel Z= new JLabel("Z");
    static JFrame frame = new JFrame("Keyboard Display");
    static JPanel panel = new JPanel();
    static HashMap<Character, Integer> letterIndex = new HashMap<Character, Integer>();
    static String current  ="";
    static String currentGuess = "";  
    static String answer;
    static int numOfGuesses = numberOfGuesses();
    static int j;
    private static void setFont() {
        Font customFont = new Font("Arial",Font.BOLD, 30);
        A.setFont(customFont);
        B.setFont(customFont);
        C.setFont(customFont);
        D.setFont(customFont);
        E.setFont(customFont);
        F.setFont(customFont);
        G.setFont(customFont);
        H.setFont(customFont);
        I.setFont(customFont);
        J.setFont(customFont);
        K.setFont(customFont);
        L.setFont(customFont);
        M.setFont(customFont);
        N.setFont(customFont);
        O.setFont(customFont);
        P.setFont(customFont);
        Q.setFont(customFont);
        R.setFont(customFont);
        S.setFont(customFont);
        T.setFont(customFont);
        U.setFont(customFont);
        V.setFont(customFont);
        W.setFont(customFont);
        X.setFont(customFont);
        Y.setFont(customFont);
        Z.setFont(customFont);
    }
    private static JLabel changeColor(HashMap<Character,Integer> colored, JLabel letter, Character character) {
        
        if (colored.get(character).equals(0)) {
            letter.setForeground(Color.WHITE);
        }
        if (colored.get(character).equals(1)) {
            letter.setForeground(Color.GRAY);
        }
        if (colored.get(character).equals(2)) {
            letter.setForeground(Color.YELLOW);
        }
        if (colored.get(character).equals(3)) {
            letter.setForeground(Color.GREEN);
        }
        return letter;
        
    }
    private static void setColor() {
        A = changeColor(letterIndex, A, 'A');
        B = changeColor(letterIndex, B, 'B');
        C = changeColor(letterIndex, C, 'C');
        D = changeColor(letterIndex, D, 'D');
        E = changeColor(letterIndex, E, 'E');
        F = changeColor(letterIndex, F, 'F');
        G = changeColor(letterIndex, G, 'G');
        H = changeColor(letterIndex, H, 'H');
        I = changeColor(letterIndex, I, 'I');
        J = changeColor(letterIndex, J, 'J');
        K = changeColor(letterIndex, K, 'K');
        L = changeColor(letterIndex, L, 'L');
        M = changeColor(letterIndex, M, 'M');
        N = changeColor(letterIndex, N, 'N');
        O = changeColor(letterIndex, O, 'O');
        P = changeColor(letterIndex, P, 'P');
        Q = changeColor(letterIndex, Q, 'Q');
        R = changeColor(letterIndex, R, 'R');
        S = changeColor(letterIndex, S, 'S');
        T = changeColor(letterIndex, T, 'T');
        U = changeColor(letterIndex, U, 'U');
        V = changeColor(letterIndex, V, 'V');
        W = changeColor(letterIndex, W, 'W');
        X = changeColor(letterIndex, X, 'X');
        Y = changeColor(letterIndex, Y, 'Y');
        Z = changeColor(letterIndex, Z, 'Z');


    }
    private static void setPosition() {
        
        Q.setBounds(5,0,40,40);
        W.setBounds(34,0,40,40);
        E.setBounds(68,0,40,40);
        R.setBounds(94,0,40,40);
        T.setBounds(120,0,40,40);
        Y.setBounds(145,0,40,40);
        U.setBounds(170,0,40,40);
        I.setBounds(197,0,40,40);
        O.setBounds(210,0,40,40);
        P.setBounds(238,0,40,40);

        A.setBounds(15,30,40,40);
        S.setBounds(41,30,40,40);
        D.setBounds(66,30,40,40);
        F.setBounds(94,30,40,40);
        G.setBounds(118,30,40,40);
        H.setBounds(148,30,40,40);
        J.setBounds(177,30,40,40);
        K.setBounds(200,30,40,40);
        L.setBounds(228,30,40,40);

        Z.setBounds(25,60,40,40);
        X.setBounds(53,60,40,40);
        C.setBounds(79,60,40,40);
        V.setBounds(109,60,40,40);
        B.setBounds(136,60,40,40);
        N.setBounds(165,60,40,40);
        M.setBounds(195,60,40,40);
        
        
    }
    private static void add() {
        
        panel.setVisible(false);
        panel.setVisible(true);

        frame.add(A);
        frame.add(B);
        frame.add(C);
        frame.add(D);
        frame.add(E);
        frame.add(F);
        frame.add(G);
        frame.add(H);
        frame.add(I);
        frame.add(J);
        frame.add(K);
        frame.add(L);
        frame.add(M);
        frame.add(N);
        frame.add(O);
        frame.add(P);
        frame.add(Q);
        frame.add(R);
        frame.add(S);
        frame.add(T);
        frame.add(U);
        frame.add(V);
        frame.add(W);
        frame.add(X);
        frame.add(Y);
        frame.add(Z);
    }
    private static void condenseResults(){
        HashMap<Character, String> results = new HashMap<Character, String>();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String guess = prepareWord(currentGuess, true);
        guess = guess.toUpperCase();
        for (int k = 0; k < 5; k++) {
            String guessAccuracy = "" + current.charAt(k);
            if (guessAccuracy.equals("X")) {
                if (1 >= letterIndex.get(guess.charAt(k))) {
                    letterIndex.replace(guess.charAt(k), 1);
                }
                
            }
            if (guessAccuracy.equals("Y")) {
                if (2 >= letterIndex.get(guess.charAt(k))) {
                    letterIndex.replace(guess.charAt(k), 2);
                }
            }
            if (guessAccuracy.equals("G")) {
                if (3 >= letterIndex.get(guess.charAt(k))) {
                    letterIndex.replace(guess.charAt(k), 3);
                }
            }
            
        }
        for (int k = 0; k < 26; k++) {
            if (letterIndex.get(alphabet.charAt(k)).equals(0)) {
                results.put(alphabet.charAt(k), "" + alphabet.charAt(k));
            }
            if (letterIndex.get(alphabet.charAt(k)).equals(1)) {
                results.put(alphabet.charAt(k), ANSI_GRAY + alphabet.charAt(k)+ ANSI_RESET);
            }
            if (letterIndex.get(alphabet.charAt(k)).equals(2)) {
                results.put(alphabet.charAt(k), ANSI_YELLOW + alphabet.charAt(k)+ ANSI_RESET);
            }
            if (letterIndex.get(alphabet.charAt(k)).equals(3)) {
                results.put(alphabet.charAt(k), ANSI_GREEN + alphabet.charAt(k)+ ANSI_RESET);
            }
        }
    }
    private static void alphabet() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        letterIndex.put('#', 0);
        for (int k = 0; k < 26; k++) {
            letterIndex.put(alphabet.charAt(k), 0);
        }
        return;
    }
    private static void updateWindow() {
        panel.setBackground(Color.DARK_GRAY);
        panel.setSize(280,200);
        KeyListener listener = new KeyListener() {
            String keyboardInput = "";
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != 10) {
                    keyboardInput = keyboardInput+e.getKeyChar();
                    printGuesses(keyboardInput, false);
                }
                else {
                    
                    j++;
                    
                    compileResults(keyboardInput, j);
                    keyboardInput = "";
                    return;
                    //return guess, reduce guesses by one and call method.
                }
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        };
        frame.addKeyListener(listener);
        frame.add(panel);
        frame.setSize(280, 135);
        frame.setLayout(null);
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
                
        }
    });
    }   
    private static void updateKeyboard() {
        if (temp != 0) {
            
            condenseResults();
            setColor();
            setFont();
            setPosition();
            add();
            updateWindow(); 
        }
        else {
            temp++;
        }
        
    }
    private static void printSpaces() {
        for(int k = 0; k < 32; k++){
            System.err.println("");
        }
    }
    private static void updateHighscore(int guesses) {
        if (mode == 1) {
            try {
            
                Path filePath = Paths.get("wordleFiveLetterHighscores.txt");
                List<String> lines = Files.readAllLines(filePath);
                int highScore = 0;
                if (-1 != lines.indexOf(answer)) {
                    
                    try {
                        highScore = Integer.parseInt(lines.get(lines.indexOf(answer)+1));
                
                        if (highScore > guessNumber) {
                            System.out.println("Congratulations you beat the highscore of " + highScore + " guesses!");
                            lines.set(lines.indexOf(answer) +1, "" + (guesses));
                            Files.write(filePath, lines);
                            return;
                        }
                    } catch (Exception e) {
                        lines.remove(answer);
                        Files.write(filePath, lines);
                        return;
                    }
                }
                else {
                    System.out.println("First Beat Of Word!");
                    lines.add(lines.size(), answer);
                    lines.add(lines.size(), "" + (guessNumber-1));
                    Files.write(filePath, lines);
                    return;
                }
    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            return;
        }
        
    }
    private static int numberOfGuesses() {
        int guesses = -1;
        
        while (guesses < 0) {
            //Scanner scanner1 = new Scanner(System.in);
            try {
                guesses = scanner.nextInt();
                if (guesses < 1) {
                    return 6;
                }
                else if (guesses > 100) {
                    return 6;
                }
            } catch (Exception e) {
                
                return 6;

            }
        }
        return guesses;
            
    }   
    private static Boolean wordType() {
        
        String input = scanner.nextLine();
        input = prepareWord(input, false);
        if (input.equals("")) {
            return true;
        }
        if (input.equals("random")) {
            return true;
        }
        if (input.equals("dev")) {
            mode = 2;
            return true;
        }
        else if (input.equals("custom")) {
            printer.print("What would you like the word to be?");
            String k = "";
            while(k.length() != 5) {
                printer.print("Please enter a five letter word");
                k = scanner.nextLine();
                //k = k.replaceAll("[^a-zA-Z]", "");
                if (k.length() == 5) {
                    answer = k;
                    mode = 3;
                    return false;
                }
            }
            
            
            
            
        } else {
            printSpaces();
        
            quickFix = true;
        }
        return false;
    }
    private static void chooseWord() {
        System.out.println("Would you like to choose your word, or have a random word generated?");
        System.out.println("Please type 'Random' for a random word and 'Custom' for a custom word!");
        boolean generateAnswer = wordType();
        if (generateAnswer) {
            Random random = new Random();
            String[] words = new String[] {"about","other","which","their","there","first","would","these","click","price","state","email","world","music","after","where","books","links","years","order","items","group","under","games","could","great","hotel","store","terms","right","local","those","using","phone","forum","based","black","check","index","being","women","today","south","pages","found","house","photo","power","while","three","total","place","think","north","posts","media","since","guide","board","white","small","times","sites","level","hours","image","title","shall","class","still","money","every","visit","tools","reply","value","press","learn","print","stock","point","sales","large","table","start","model","human","movie","march","yahoo","going","study","staff","again","april","never","users","topic","below",};
            answer = words[random.nextInt(99)];
            if (mode == 2) {
                System.out.println(answer);
            }
            //System.err.println(answer);
            return;
        }
        else {
            //System.err.println(answer);
            return;
        }
        
        
    }
    private static String prepareWord(String input, boolean trimWord) {
        String output = input;
        output = output.replaceAll("[^a-zA-Z]","");
        output = output.replaceAll(" ", "");
        output = output.toLowerCase();
        if (trimWord) {
            if(input.length() > 5) {
                output =  "" + input.charAt(0) + input.charAt(1) + input.charAt(2)+ input.charAt(3) + input.charAt(4);
            }
            for (int k = output.length(); k < 5;) {
                output = output + "#";
                k = output.length();
            } 
        }
        return output;

    }
    private static Boolean checkForGreen(String input, int index) {
        try {
            if (input.charAt(index) == answer.charAt(index)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    private static Boolean checkForYellow(String input,  int index) {
        
        try {
            String temp = "" + input.charAt(index);
            for (int k = 0; k<5; k++) {
                if (temp.equals("" + answer.charAt(k)) ) {
                    return true;
                    
                }
            }
        } catch (Exception e) {
        }
        
        return false;
    }
    private static String convertToWord (String result, String input) {
        String output = "";
        for(int k = 0; k < 5; k++) {
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
    private static void printGuesses(String guess, Boolean updateGuesses) {
        if (updateGuesses) {
            storedGuesses[guessNumber] = guess;
            guessNumber++;
        
            for(int k = 0; k < 31; k++){
                System.err.println("");
            }
            for(int k = 1; k < guessNumber+1; k++) {
                System.err.println(storedGuesses[k]);
            }
        }
        else{
            for(int k = 0; k < 31; k++){
                System.err.println("");
            }
            for(int k = 1; k < guessNumber+1; k++) {
                System.err.println(storedGuesses[k]);
            }
            System.out.println(guess);
        }
        
        
    }
    private static int getChars(Character character, String input){
        int count = 0;
        for(int k = 0; k < input.length(); k++){
            if (input.charAt(k) == character) {
                count++;
            }
        }
        return count;
    }
    private static String removeChars(int count,String newCompiledAnswer,String compiledAnswer) {
        //2
        //##GYY
        //GGGYY
        
        HashMap<Integer, Boolean> index = new HashMap<Integer, Boolean>();
        StringBuffer output = new StringBuffer(compiledAnswer);
        for(int k = 0; k <5; k++) {
            index.put(k, false);
            if (newCompiledAnswer.charAt(k) != '#') {
                index.replace(k, true);
                
                
            }
        }
        //System.out.println(index);

        for(int k = 4; k >=0&&count!=0; k--) {
            if (compiledAnswer.charAt(k) == 'Y' && index.get(k)) {
                output.setCharAt(k, 'X');
                count--;

            }
        }
        //System.out.println(output);
        return output.toString();
    }
	private static String checkForDupe(String input, String answer, int index, String compiledAnswer){
        char character = answer.charAt(index-1);
        int numberOfChars = getChars(character, input);
        StringBuffer newCompiledAnswer = new StringBuffer(compiledAnswer);

        if (numberOfChars > 1) {
            //Chacacter = dupe character
            int k = -1;
            StringBuffer tempInput = new StringBuffer(input);
            
            while (!tempInput.toString().matches("["+character+"#]+")) {
                k++;
                
                if (input.charAt(k)!=character) {
                    tempInput.setCharAt(k, '#');
                    newCompiledAnswer.setCharAt(k, '#');
                    
                }
                
                
            }
            
            //System.out.println(tempInput);
            
            //System.out.println(newCompiledAnswer);
            return removeChars(numberOfChars-getChars(character, answer), newCompiledAnswer.toString(), compiledAnswer);

        }
        

        return compiledAnswer;
    }
    private static String compile(String input) {
        StringBuffer output = new StringBuffer("XXXXX");
        for(int p = 0; p < 5; p++) {
            if(checkForYellow(input, p)) {
                output.setCharAt(p, 'Y');
            }
        }
        for(int p = 0; p <5; p++) {
            if(checkForGreen(input, p)) {
                output.setCharAt(p, 'G');
                
            }
        }
        return output.toString();
    }
    private static String initDupes(String input) {
        String output=compile(input);
        for (int k = 1; k<=5; k++){
            output = checkForDupe(input, answer,k,output);
        }
        return output;
    }
    private static boolean compileResults(String input, int guesses) {
        
        String currentGuess = input;
        currentGuess = prepareWord(currentGuess, true);
        StringBuffer output = new StringBuffer(initDupes(input));
        current = output.toString();
        
        System.out.println();
        printGuesses(convertToWord(output.toString(), currentGuess), true);
        updateKeyboard();
        if(output.toString().equals("GGGGG")) {
            System.err.println("");
            updateHighscore(guesses);
            System.err.println("Congratulations!");
            System.err.println("You have won!");
            System.err.println("Thanks for playing!");
            System.err.println("");
            System.exit(0);
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Would you like to choose your word, or have a random word generated?");
        System.out.println("Please type 'Random' for a random word and 'Custom' for a custom word!");
        scanner.nextLine();
        alphabet();
        
        
        
        chooseWord();
        if (quickFix) {
            printSpaces();
            printer.print("Please Provide A Proper Value");
            printer.print("Program Terminating...");
            return;
        }
        printSpaces();
        printer.print("Please Input How Many Guesses You Would Like!");
        
        
        if (quickFix) {
            printSpaces();
            printer.print("Please Provide A Proper Value");
            printer.print("Program Terminating...");
            return;
        }
        else{
            for(j = 0; j <= numOfGuesses; j++) {
                currentGuess = scanner.nextLine();

                printer.print(currentGuess);
                
                if (compileResults(currentGuess, j)) {
                    scanner.close();
                    return;
                }
                
            }
        }
        
        
        scanner.close();
        printer.print("");
        printer.print("Out of Guesses :(");
        printer.print("The word was " + answer + ".");
        printer.print("Try Again Next Time!");
        printer.print("");
        System.exit(0);
        
    }
}

//add word library