import java.util.Scanner;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WordleFiveLetter {
    static boolean usingKeyboard = false;
    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_GRAY = "\u001B[90m";
    static String[] storedGuesses = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",};
    static int guessNumber = 0;
    static int mode = 1;
    static int numOfGuesses;
    static boolean keepFirstGuess = true;
    static boolean quickFix  = false;
    static Printer printer = new Printer();
    static boolean keyboardPrinted = true;
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
    static JLabel downArrow = new JLabel("↓");
    static JLabel upArrow = new JLabel("↑");
    static JLabel enter = new JLabel("Enter");
    static ImageIcon originalBackspaceIcon = new ImageIcon("deleteIcon.png");
    static Image imageBackspaceIcon = originalBackspaceIcon.getImage();
    static ImageIcon backspaceIcon = new ImageIcon(imageBackspaceIcon.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    static JLabel backspace = new JLabel(backspaceIcon);
    static JFrame frame = new JFrame("Keyboard Display");
    static JPanel panel = new JPanel();
    static JTextField textField = new JTextField(20);
    static JPopupMenu popupMenu = new JPopupMenu();
    static JCheckBoxMenuItem menuItemHideKeyboard = new JCheckBoxMenuItem("Hide Keyboard");
    static JMenuItem menuItemGiveUp = new JMenuItem("Give Up");
    static JMenuItem menuItemRefresh = new JMenuItem("Refresh");
    static JMenuItem menuItemClose = new JMenuItem("Close");
    static JMenuItem menuItemHint = new JMenuItem("Hint");
    static JMenuBar menuBar = new JMenuBar();
    static JMenu accountMenu = new JMenu("Account");
    static JMenu editMenu = new JMenu("Edit");
    static JMenu wordHelper = new JMenu("Word Helper");
    static JMenuItem wordUnscramble = new JMenuItem("Unscramble Word");
    static JMenuItem cutItem = new JMenuItem("Cut");
    static JMenuItem copyItem = new JMenuItem("Copy");
    static JMenuItem pasteItem = new JMenuItem("Paste");
    static JMenuItem loginItem = new JMenuItem("Login");
    static JMenuItem createAccountItem = new JMenuItem("Create Account");
    static JMenuItem averageGuessesItem = new JMenuItem("Average Guesses");
    static JMenuItem logoutMenuItem = new JMenuItem("Logout");
    static UndoManager undoManager = new UndoManager();
    static Boolean keyboardDisplayed = false;
    static JButton button = new JButton("Hint");
    static HashMap<Character, Integer> letterIndex = new HashMap<Character, Integer>();
    static String current  ="";
    static String currentGuess = "";  
    static String answer;
    static boolean control = false;
    static boolean accountLoggedIn = false;
    static String currentUser = "";
    static boolean highScoreBeaten = false;
    static int j;
    static ArrayList<String> guessIndex = new ArrayList<String>();
    static ArrayList<String> answerIndex = new ArrayList<String>();
    static ArrayList<String> trueGuessIndex = new ArrayList<String>();
    static ArrayList<String> trueAnswerIndex = new ArrayList<String>();
    static int displayNumber = 1;
    static HashMap<Integer, Rectangle> boxPositions = new HashMap<Integer, Rectangle>();
    static JPanel displayPanel = new JPanel();

    private static void setFont() {
        Font customFont = new Font("Arial",Font.BOLD, 30);
        enter.setFont(customFont);
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
        downArrow.setFont(customFont);
        upArrow.setFont(customFont);
        enter.setFont(new Font("Arial", Font.PLAIN, 15));
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
        enter.setForeground(Color.WHITE);
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
        int originX = 15;
        int originY = 325;
        Q.setBounds(5+originX,10+originY,25,25);
        W.setBounds(34+originX,10+originY,29,25);
        E.setBounds(68+originX,10+originY,20,25);
        R.setBounds(94+originX,10+originY,25,25);
        T.setBounds(120+originX,10+originY,20,25);
        Y.setBounds(145+originX,10+originY,20,25);
        U.setBounds(170+originX,10+originY,25,25);
        I.setBounds(197+originX,10+originY,20,25);
        O.setBounds(210+originX,10+originY,25,25);
        P.setBounds(238+originX,10+originY,20,25);

        A.setBounds(15+originX,40+originY,25,25);
        S.setBounds(41+originX,40+originY,20,25);
        D.setBounds(66+originX,40+originY,25,25);
        F.setBounds(94+originX,40+originY,20,25);
        G.setBounds(118+originX,40+originY,25,25);
        H.setBounds(148+originX,40+originY,25,25);
        J.setBounds(177+originX,40+originY,20,25);
        K.setBounds(200+originX,40+originY,25,25);
        L.setBounds(228+originX,40+originY,20,25);

        enter.setBounds(5+originX,71+originY,39,22);
        Z.setBounds(48+originX,70+originY,20,25);
        X.setBounds(76+originX,70+originY,20,25);
        C.setBounds(102+originX,70+originY,25,25);
        V.setBounds(132+originX,70+originY,20,25);
        B.setBounds(159+originX,70+originY,25,25);
        N.setBounds(188+originX,70+originY,25,25);
        M.setBounds(218+originX,70+originY,25,25);
        backspace.setBounds(245+originX,68+originY,30,30);
        
        downArrow.setBounds(300,200,40,40);
        upArrow.setBounds(300, 150, 40, 40);
    }
    private static void add() {
        
        panel.setVisible(false);
        panel.setVisible(true);
        frame.add(enter);
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
        frame.add(backspace);
        frame.add(downArrow);
        frame.add(upArrow);
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
    private static void initLists() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        letterIndex.put('#', 0);
        for (int k = 0; k < 26; k++) {
            letterIndex.put(alphabet.charAt(k), 0);
        }
        for(int k = 0; k < 99; k++){
            boxPositions.put(k, null);

        }
        for(int k =0; k < 99; k++){
            guessIndex.add("WWWWW");
            answerIndex.add("");
            trueGuessIndex.add("WWWWW");
            trueAnswerIndex.add("");
        }
        return;
    }
    private static void generateBoxPositions(int originX, int originY){
        Path filePath = Paths.get("wordlePositionData.txt");
        for(int k =0; k < 30; k++){
            try {
                List<String> lines = Files.readAllLines(filePath);
                String originalPos = lines.get(k);
                String[] splittedPos= originalPos.split(",");
                int finalX = Integer.parseInt(splittedPos[0])+originX;
                int finalY = Integer.parseInt(splittedPos[1])+originY;
                Rectangle bounds = new Rectangle(finalX, finalY, 40, 40);
                boxPositions.replace(k, bounds);
            } catch (IOException e) {
                System.out.println("HI");
            }
        }
 
    }
    private static JPanel setBoxColor(JPanel tempPanel, int panelNumber){
        try {
            int turnNumber = 0;
            for(;panelNumber >= 5;){
                panelNumber = panelNumber -5;
                turnNumber++;
            }
            String current = guessIndex.get(turnNumber);
            if (current.charAt(panelNumber) == 'X') {
                tempPanel.setBackground(Color.LIGHT_GRAY);
            }
            if (current.charAt(panelNumber) == 'Y') {
                tempPanel.setBackground(Color.YELLOW);
            }
            if (current.charAt(panelNumber) == 'G') {
                tempPanel.setBackground(Color.GREEN);
            }
        } catch (Exception e) {
        }
        
        return tempPanel;
    }
    private static JLabel setBoxLetter(JLabel label, int panelNumber){
        
        try {
            int turnNumber = 0;
            for(;panelNumber >= 5;){
                panelNumber = panelNumber -5;
                turnNumber++;
            }
            String currentAnswer = answerIndex.get(turnNumber);
            currentAnswer = currentAnswer.toUpperCase();
            label.setText(""+currentAnswer.charAt(panelNumber)); 
        } catch (Exception e) {
        }
        
        return label;
    }
    private static void updateAnswerDisplay(){
        frame.remove(displayPanel);
        displayPanel = new JPanel();
        for(int k =0; k <30; k++){
            
            JPanel tempPanel = new JPanel();
            JLabel tempLabel = new JLabel("");
            frame.remove(tempLabel);
            frame.remove(tempPanel);
            tempLabel = setBoxLetter(tempLabel, k);
            tempPanel = setBoxColor(tempPanel, k);

            
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

            tempPanel.setBorder(border);
            generateBoxPositions(10,10);
            Rectangle bounds = boxPositions.get(k);
            tempPanel.setBounds(bounds);
            
            tempLabel.setBounds(bounds);

            Font customFont = new Font("Arial",Font.BOLD, 26);
            tempLabel.setFont(customFont);
            
            
            tempPanel.add(tempLabel);
            displayPanel.add(tempPanel);


        }
        frame.add(displayPanel);
        displayPanel.setBackground(Color.DARK_GRAY);
        displayPanel.setLayout(null);
        displayPanel.setBounds(15,10,260,310);
        displayPanel.setVisible(true);
    }
    private static void updateWindow() {
        if (!textField.hasFocus()) {
               
        }
        textField.requestFocus();
        enter.setForeground(Color.BLACK);
        accountMenu.setForeground(Color.BLACK);
        accountMenu.setBackground(Color.DARK_GRAY);
        accountMenu.add(loginItem);
        accountMenu.add(createAccountItem);
        accountMenu.add(logoutMenuItem);
        accountMenu.add(averageGuessesItem);
        wordHelper.add(wordUnscramble);
        wordHelper.setForeground(Color.BLACK);
        wordHelper.setBackground(Color.DARK_GRAY);

        editMenu.setForeground(Color.BLACK);
        editMenu.setBackground(Color.DARK_GRAY);
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        
        loginItem.setBackground(Color.DARK_GRAY);
        createAccountItem.setBackground(Color.DARK_GRAY);
        logoutMenuItem.setBackground(Color.DARK_GRAY);
        averageGuessesItem.setBackground(Color.DARK_GRAY);

        loginItem.setForeground(Color.WHITE);
        createAccountItem.setForeground(Color.WHITE);
        logoutMenuItem.setForeground(Color.WHITE);
        averageGuessesItem.setForeground(Color.WHITE);

        cutItem.setBackground(Color.DARK_GRAY);
        copyItem.setBackground(Color.DARK_GRAY);
        pasteItem.setBackground(Color.DARK_GRAY);

        cutItem.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        copyItem.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
        pasteItem.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

        menuBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        menuBar.setBackground(Color.DARK_GRAY);
        menuBar.add(editMenu);
        menuBar.add(accountMenu);
        menuBar.add(wordHelper);
        
        upArrow.setForeground(Color.WHITE);
        downArrow.setForeground(Color.WHITE);

        frame.setJMenuBar(menuBar);
        popupMenu.setBackground(Color.DARK_GRAY);
        popupMenu.setBorderPainted(false);
        popupMenu.add(menuItemGiveUp);
        popupMenu.add(menuItemRefresh);
        popupMenu.add(menuItemClose);
        popupMenu.add(menuItemHint);
        popupMenu.add(menuItemHideKeyboard);

        menuItemHint.setBackground(Color.WHITE);
        menuItemClose.setBackground(Color.WHITE);
        menuItemGiveUp.setBackground(Color.WHITE);
        menuItemHideKeyboard.setBackground(Color.WHITE);
        menuItemRefresh.setBackground(Color.WHITE);

        menuItemHint.setForeground(Color.WHITE);

        menuItemHint.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        menuItemClose.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
        menuItemGiveUp.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        menuItemHideKeyboard.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
        menuItemRefresh.setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
        
        Border whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        enter.setBorder(blackBorder);

        textField.setBounds(100,500,100,50);
        textField.setBackground(Color.DARK_GRAY);
        textField.setFont(null);
        textField.setBorder(whiteBorder);
        textField.setSelectedTextColor(Color.BLACK);
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setVisible(true);

        button.setBackground(Color.GRAY);
        button.setBorder(whiteBorder);
        button.setBounds(275, 10, 95, 30);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);

        panel.setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.DARK_GRAY);
        panel.add(textField);
        panel.setSize(400,800);
        panel.add(button);
        panel.setLayout(null);

        frame.add(panel);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setSize(400,800);
        frame.setLayout(null);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    }  
    private static void addFrameListener(){
        frame.addKeyListener(new KeyListener() {
            String output = "";
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                output = output + e.getKeyChar();
                //updateKeyboardDiplay(output);
                //updateAnswerDisplay();
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
    } 
    private static void addActionListener(){
        undoRedoActionListener();
        editMenuActionListener();
        accountMenuActionListener();
        disableAccountMenu();

        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0)) {
                    if ((e.getKeyCode() == KeyEvent.VK_W)) {
                        Random random = new Random();
                    
                        try {
                            Thread.sleep(random.nextInt(100,250));
                        } catch (InterruptedException f) {
                        }
                        System.exit(0);
                    }
                    else if((e.getKeyCode() == KeyEvent.VK_R)){
                    
                        updateWindow();
                    }
                   
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardPrinted=true;
                if (keyboardPrinted&&!textField.getText().equals("")) {
                    usingKeyboard = true;
                    Scanner scanner = new Scanner(System.in);
                    currentGuess = textField.getText();
                    keyboardPrinted = true;
                    compileResults(textField.getText(), j, scanner);
                    j++;
                    scanner.close();
                    
                }
                
                
                textField.setText("");
                return;
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String conformation = "Are you sure you would like a hint?";
                String panelName = "Hint Confirmation";
                int output  = JOptionPane.showConfirmDialog(frame, conformation, panelName,0 );
                if (output == 0) {
                    panel.remove(button);
                    frame.add(panel);
                    frame.revalidate();
                    frame.repaint();
                    createHint(output);
                
                    
                    frame.setSize(frame.getWidth()-1, frame.getHeight()-1);
                    frame.setSize(frame.getWidth()+1, frame.getHeight()+1);
                }
                
            }
        });
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    control = true;

                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    control = true;
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        menuItemRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateKeyboard();
            }
        });
        menuItemHint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String conformation = "Are you sure you would like a hint?";
                String panelName = "Hint Confirmation";
                int output  = JOptionPane.showConfirmDialog(frame, conformation, panelName,0 );
                if (output == 0) {
                    panel.remove(button);
                    frame.add(panel);
                    frame.revalidate();
                    frame.repaint();
                    createHint(output);
                
                    
                    frame.setSize(frame.getWidth()-1, frame.getHeight()-1);
                    frame.setSize(frame.getWidth()+1, frame.getHeight()+1);
                }
            }
        });
        menuItemClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuItemGiveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scanner scanner = new Scanner(System.in);
                endProgram(scanner, false);
            }
        });
        
        
        menuItemHideKeyboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                if (control) {
                    
                    if (keyboardDisplayed) {
                        A.setText("A");
                        B.setText("B");
                        C.setText("C");
                        D.setText("D");
                        E.setText("E");
                        F.setText("F");
                        G.setText("G");
                        H.setText("H");
                        I.setText("I");
                        J.setText("J");
                        K.setText("K");
                        L.setText("L");
                        M.setText("M");
                        N.setText("N");
                        O.setText("O");
                        P.setText("P");
                        Q.setText("Q");
                        R.setText("R");
                        S.setText("S");
                        T.setText("T");
                        U.setText("U");
                        V.setText("V");
                        W.setText("W");
                        X.setText("X");
                        Y.setText("Y");
                        Z.setText("Z");
                        
                        frame.getContentPane().setBackground(Color.DARK_GRAY);
                        frame.setSize(300,200);
                        frame.setLayout(null);
                        frame.revalidate();
                        frame.repaint();
                        frame.setVisible(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        keyboardDisplayed = false;
                        control = false;
                        updateKeyboard();
                    }
                    else{
                        A.setText("");
                        B.setText("");
                        C.setText("");
                        D.setText("");
                        E.setText("");
                        F.setText("");
                        G.setText("");
                        H.setText("");
                        I.setText("");
                        J.setText("");
                        K.setText("");
                        L.setText("");
                        M.setText("");
                        N.setText("");
                        O.setText("");
                        P.setText("");
                        Q.setText("");
                        R.setText("");
                        S.setText("");
                        T.setText("");
                        U.setText("");
                        V.setText("");
                        W.setText("");
                        X.setText("");
                        Y.setText("");
                        Z.setText("");
                        keyboardDisplayed = true;
                        control = false;
                    }
                    updateKeyboard();

                }
                
                

            }
        });
    }
    private static void keyBoardActionListener(){
        A.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'A');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        B.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'B');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        C.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'C');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        D.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'D');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        E.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'E');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        F.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'F');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        G.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'G');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        H.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'H');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        I.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'I');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        J.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'J');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        K.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'K');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        L.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'L');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        M.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'M');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        N.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'N');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        O.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'O');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        P.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'P');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        Q.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'Q');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        R.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'R');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        S.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'S');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        T.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'T');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        U.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'U');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        V.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'V');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        W.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'W');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        X.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'X');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        Y.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'Y');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        Z.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField.setText(textField.getText()+'Z');
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        enter.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                keyboardPrinted=true;
                if (keyboardPrinted&&!textField.getText().equals("")) {
                    
                    usingKeyboard = true;
                    Scanner scanner = new Scanner(System.in);
                    currentGuess = textField.getText();
                    compileResults(textField.getText(), j, scanner);
                    j++;
                    scanner.close();
                    
                }
                
                
                textField.setText("");
                return;
            }
            @Override
            public void mousePressed(MouseEvent e) {
                
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
            @Override
            public void mouseEntered(MouseEvent e) {
               
            }
            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        backspace.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringBuffer lastChar  = new StringBuffer(textField.getText());
                try {
                    lastChar.deleteCharAt(lastChar.length()-1);
                } catch (Exception f) {
                }
                textField.setText(lastChar.toString());
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        downArrow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollDown();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        upArrow.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                scrollUp();
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
    private static void accountMenuActionListener(){
        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog loginDialog = new JDialog(frame, "Login", true);
                loginDialog.setSize(300, 150);
                loginDialog.setLayout(new GridLayout(3, 2));

                JLabel userLabel = new JLabel("Username:");
                JTextField userField = new JTextField();
                JLabel passLabel = new JLabel("Password:");
                JPasswordField passField = new JPasswordField();
                JButton submitButton = new JButton("Submit");

                loginDialog.add(userLabel);
                loginDialog.add(userField);
                loginDialog.add(passLabel);
                loginDialog.add(passField);
                loginDialog.add(new JLabel()); // Empty cell
                loginDialog.add(submitButton);

                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = userField.getText();
                        String password = new String(passField.getPassword());
                        Path filePath = Paths.get("wordleAccountData.txt");
                        try {
                            List<String> lines = Files.readAllLines(filePath);
                            for(int k = 0; k < lines.size(); k++) {
                                lines.set(k, decryptText(lines.get(k)));
                            }
                            if (lines.contains(username)) {
                                if (lines.get(lines.indexOf(username)+1).equals(password)) {
                                    accountLoggedIn = true;
                                    currentUser = username;
                                    disableAccountMenu();
                                    System.out.println("PASSWORD CORRECT");
                                    loginDialog.dispose();
                                }
                            }
                            
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        loginDialog.dispose();
                    }
                });
                loginDialog.setVisible(true);
                
            }
        });
        createAccountItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog loginDialog = new JDialog(frame, "Account Creation", true);
                loginDialog.setSize(300, 150);
                loginDialog.setLayout(new GridLayout(3, 2));

                JLabel userLabel = new JLabel("Username:");
                JTextField userField = new JTextField();
                JLabel passLabel = new JLabel("Password:");
                JPasswordField passField = new JPasswordField();
                JButton submitButton = new JButton("Submit");

                loginDialog.add(userLabel);
                loginDialog.add(userField);
                loginDialog.add(passLabel);
                loginDialog.add(passField);
                loginDialog.add(new JLabel()); // Empty cell
                loginDialog.add(submitButton);

                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username = userField.getText();
                        String password = new String(passField.getPassword());
                        try {
                            Path filePath = Paths.get("wordleAccountData.txt");
                            List<String> lines = Files.readAllLines(filePath);
                            for(int k = 0; k < lines.size(); k++){
                                lines.set(k, decryptText(lines.get(k)));
                            }
                            if (lines.contains(username)) {
                                System.out.println("Username Taken");
                            }
                            else{
                                lines.add(username);
                                lines.add(password);
                                createUser(username);
                                SecretKey secretKey = generateKey(128);
                                Files.write(Paths.get("secretKey.txt"), Base64.getEncoder().encode(secretKey.getEncoded()));
                                for(int k = 0; k<lines.size();k++){
                                    lines.set(k, encrypt(lines.get(k), secretKey));
                                }
                                Files.write(filePath, lines);
                                loginDialog.dispose();
                            }
                        } catch (Exception e1) {
                        }
                        
                    }
             });
                loginDialog.setVisible(true);
                
            }
        });
        logoutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                System.out.println("Account Logged Out");
				currentUser="";
                accountLoggedIn = false;
                disableAccountMenu();
			}
            
        });
        averageGuessesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getAverageGuesses();
			}
            
        });

        
    }
    private static void disableAccountMenu(){
        if (accountLoggedIn) {
            loginItem.setEnabled(false);
            logoutMenuItem.setEnabled(true);
            averageGuessesItem.setEnabled(true);
        }
        else{
            logoutMenuItem.setEnabled(false);
            averageGuessesItem.setEnabled(false);
            loginItem.setEnabled(true);

        }
    }
    private static void createUser(String username){
        Path filePath = Paths.get("wordleUserData.txt");
        try {
			List<String> lines = Files.readAllLines(filePath);
            lines.add(username);
            lines.add("0");
            lines.add("");
            lines.add("0");
            lines.add("100");
            lines.add("0");
            Files.write(filePath, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return;
    }
    private static void updateStatistics(boolean win){
        if (!accountLoggedIn) {
            return;
        }
        Path filePath = Paths.get("wordleUserData.txt");
        try {
			List<String> lines = Files.readAllLines(filePath);
            lines.set(lines.indexOf(currentUser)+1, ""+(Integer.parseInt(lines.get(lines.indexOf(currentUser)+1))+1));
            if (win&&mode!=3) {
                lines.set(lines.indexOf(currentUser)+2, lines.get(lines.indexOf(currentUser)+2)+(guessNumber-1)+",");
                lines.set(lines.indexOf(currentUser)+3, ""+(Integer.parseInt(lines.get(lines.indexOf(currentUser)+3))+1));    
                if (Integer.parseInt(lines.get(lines.indexOf(currentUser)+4))>guessNumber-1) {
                    lines.set(lines.indexOf(currentUser)+4, (guessNumber-1)+"");
                } 
                if (highScoreBeaten) {
                    lines.set(lines.indexOf(currentUser)+5, ""+(Integer.parseInt(lines.get(lines.indexOf(currentUser)+5))+1));
                } 
            }
            Files.write(filePath, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    private static double getAverageGuesses() {
        Path filePath = Paths.get("wordleUserData.txt");
        try {
			List<String> lines = Files.readAllLines(filePath);
            String input = lines.get(lines.indexOf(currentUser)+2);
            if (input.equals("")) {
                System.out.println("ERROR: NO DATA FOUND");
                return 0;
            }
            String[] splitedInput = input.split(",");
            double output = 0;
            for(int k = 0; k<splitedInput.length;k++) {
                output += Double.parseDouble(splitedInput[k]);
            }
            output = output/(splitedInput.length);
            output = Math.round(output*1000);
            System.out.println("Your average time to solve is "+output/1000+" guesses.");
            return output/1000;
		} catch (IOException e) {
            
		}
        System.out.println("ERROR: NO DATA FOUND");
        return 0;
    }
    private static String encrypt(String strToEncrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
        }
        return "";
        
    }
    private static SecretKey generateKey(int n) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }
    private static String decryptText(String strToDecrypt) {
        try {
            byte[] secretKeyBytes = Base64.getDecoder().decode(Files.readAllBytes(Paths.get("secretKey.txt")));
            SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decryptedBytes);
        } catch (Exception e) {
        }
        return "";
    }
    private static void editMenuActionListener(){
        cutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.cut();
                
            }
        });

        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.copy();
            }
        });

        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.paste();
            }
        });
    }
    private static void undoRedoActionListener(){
        
        textField.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });

        Action undoAction = new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                } catch (CannotUndoException ex) {
                    ex.printStackTrace();
                }
            }
        };

        Action redoAction = new AbstractAction("Redo") {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                } catch (CannotRedoException ex) {
                    ex.printStackTrace();
                }
            }
        };

        textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        textField.getActionMap().put("Undo", undoAction);
        
        textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Redo");
        textField.getActionMap().put("Redo", redoAction);
    }
    private static void createHint(int giveHint){
        if (giveHint == 0) {
            Random random = new Random();
            int hintType = random.nextInt(3);
    
            if (hintType==0) {
                getVowels();
                return;
            }
            else if (hintType ==1) {
                //find word with similiar chars.
                similarWord();
                return;
            }
            else if(hintType ==2) {
                revealYellow();
                return;
            }
            else{

            }
            //Number of vowels
            //reveal random letter
            //word that contains similiar chars
            //guess a random word
            
            
            return;
        }
        else{
            
            return;
        }
    }  
    private static void getVowels() {
        int numOfVowels = 0;
        for (int k = 0;k<=4; k++) {
            Character currentChar = answer.charAt(k);
            if (currentChar.equals('a')) {
                numOfVowels++;
            }
            else if (currentChar.equals('e')) {
                numOfVowels++;
            }
            else if (currentChar.equals('i')) {
                numOfVowels++;
            }
            else if (currentChar.equals('o')) {
                numOfVowels++;
            }
            else if (currentChar.equals('u')) {
                numOfVowels++;
            }
            
        }
        JPanel hintPanel = new JPanel();
        JLabel label = new JLabel("The answer contains "+numOfVowels+" vowels");
        
        

        
        
        label.setBounds(50, 50, 25, 25);
        label.setForeground(Color.WHITE);
        hintPanel.setBackground(Color.DARK_GRAY);
        hintPanel.setBounds(0,100,250,250);
        hintPanel.add(label);
        System.out.println("The answer contians "+numOfVowels+" vowels!");
        return;
    } 
    private static void similarWord(){
         
        StringBuffer tempAnswer = new StringBuffer(answer);
        try {
            Path filePath = Paths.get("wordleInputData.txt");
            List<String> lines = Files.readAllLines(filePath);
            ArrayList<String> output = new ArrayList<String>();
            Random random = new Random();

            Character rhymeChar1 = tempAnswer.charAt(random.nextInt(5));
            tempAnswer.deleteCharAt(tempAnswer.indexOf(rhymeChar1+""));
            Character rhymeChar2 = tempAnswer.charAt(random.nextInt(4));
            tempAnswer.deleteCharAt(tempAnswer.indexOf(rhymeChar2+""));
            Character rhymeChar3 = tempAnswer.charAt(random.nextInt(3));

                
           
            
            for(int k =0; k < lines.size(); k++){
                String current = lines.get(k);
                if (current.contains(rhymeChar1.toString())&&current.contains(rhymeChar2.toString())&&current.contains(rhymeChar3.toString())) {
                    
                    if (!current.equals(answer)) {
                        int numberOfDupeLetters = 0;
                        for(int j = 0; j < 5; j++) {
                            if (current.contains(answer.charAt(j)+"")) {
                                numberOfDupeLetters++;
                            }
                        }
                        if (numberOfDupeLetters==3) {
                            output.add(current);
                        }
                        
                    }
                }
            }
            if (output.size()==0) {
                similarWord();
            }
            System.out.println("The word '"+output.get(random.nextInt(output.size()))+"' contains three letters in common with the answer.");
            return;
                
        } catch (Exception e) {
            
        }
        return;
    }
    private static void revealYellow() {
        ArrayList<Character> possibleAnswers = new ArrayList<Character>();
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String tempAnswer = answer;
        tempAnswer = tempAnswer.toUpperCase();
        for (int k = 0; k < 26; k++) {
            if (tempAnswer.contains(""+alphabet.charAt(k))&&letterIndex.get(alphabet.charAt(k))==0) {
                
                possibleAnswers.add(alphabet.charAt(k));
            }
        }
        Character output = possibleAnswers.get(random.nextInt(possibleAnswers.size()));
        System.out.println("The answer contains the letter '"+output+"'.");
    }
    private static void updateKeyboard() {
        if (temp == 0) {
            
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
            System.err.println();
        }
    }
    private static void updateHighscore(int guesses, boolean win) {
        if (mode == 1&&win) {
            try {
            
                Path filePath = Paths.get("wordleHighScoreData.txt");
                List<String> lines = Files.readAllLines(filePath);
                int highScore = 0;
                if (-1 != lines.indexOf(answer)) {
                    
                    try {
                        highScore = Integer.parseInt(lines.get(lines.indexOf(answer)+1));
                
                        if (highScore > guessNumber) {
                            System.out.println("Congratulations you beat the highscore of " + highScore + " guesses!");
                            highScoreBeaten = true;
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
        else if(!win){
            try {
                Path filePath = Paths.get("wordleFiveLetterHighscores.txt");
                List<String> lines = Files.readAllLines(filePath);
                int highScore = Integer.parseInt(lines.get(lines.indexOf(answer)+1));
                System.out.println("Good Try! The highscore was " + highScore + "guesses.");
            } catch (Exception e) {
                // TODO: handle exception
            }
            return;
        }
        
    }
    private static int getNumOfGuesses(Scanner scanner) {
        Object[] options = {4, 6,8,10,15,20,25};
        int choice = -1;
        while (choice == -1) {
            choice = JOptionPane.showOptionDialog(frame, "How many guesses would you like?", "Guesses",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }
        if (choice == 0) {
            return 4;
        }
        else if (choice==1){
            return 6;
        }
        else if (choice==2){
            return 8;
        }
        else if (choice==3){
            return 10;
        }
        else if (choice==4){
            return 15;
        }
        else if (choice==5){
            return 20;
        }
        else if (choice == 6){
            return 25;
        }
        return 6;
            
    }   
    private static Boolean wordType(Scanner scanner) {
        
        Object[] options = {"Random", "Custom"};
        int choice = -1;
        String word = "a";
        while (choice == -1) {
            choice = JOptionPane.showOptionDialog(frame, "Would you like a Random or Custom Word?", "Word Type",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        }
        if (choice == 0) {
            return true;
        }
        else if (choice == 1){
            word = JOptionPane.showInputDialog("What would you like the word to be?");
            while (word==null||word.length() != 5) {
                JOptionPane.showMessageDialog(frame, "Please enter a five letter word.");
                word = JOptionPane.showInputDialog("What would you like the word to be?");
            }
            answer = word;
            mode = 3;
            return false;
        }
        return true;
    }
    private static void chooseWord(Scanner scanner) {
        System.out.println("Would you like to choose your word, or have a random word generated?");
        System.out.println("Please type 'Random' for a random word and 'Custom' for a custom word!");
        boolean generateAnswer = wordType(scanner);
        if (generateAnswer) {
            Random random = new Random();
            Path filePath = Paths.get("wordleAnswerData.txt");
            try {
                List<String> lines = Files.readAllLines(filePath);
                answer = lines.get(random.nextInt(2296));
            } catch (IOException e) {
                e.printStackTrace();
                answer = "words";
            }
            
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
        //print guesses method is being called multiple times.
        
        
        if (updateGuesses) {
            
            storedGuesses[guessNumber] = guess;
            guessNumber=guessNumber+1;
            keyboardPrinted = false;
            printSpaces();

            for(int k = 1; k < guessNumber+1; k++) {
                if (!storedGuesses[k].equals("")) {
                    System.err.println(storedGuesses[k]);
                }
                
            }
            
        }
        else{
            printSpaces();
            for(int k = 1; k < guessNumber+1; k++) {
                //error with index is out of bounds.
                try {
                    if (!storedGuesses[k].equals("")) {
                        System.err.println(storedGuesses[k]);
                    }
                   

                } catch (Exception e) {
                }
            }
            System.out.println(guess);
            keyboardPrinted = true;
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
                    try {
                        tempInput.setCharAt(k, '#');
                        newCompiledAnswer.setCharAt(k, '#');
                        
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    
                }
                
                
            }
            
            
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
    private static boolean checkIfRealWord(String input){
        Path filePath = Paths.get("wordleInputData.txt");
        input = prepareWord(input, true);
        try {
            List<String> lines = Files.readAllLines(filePath);
            if (lines.contains(input)) {
                return true;
            }
            else{
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    } 
    private static void scrollDown(){
        if (displayNumber<j-4){
            displayNumber++;
            guessIndex.set(0, trueGuessIndex.get(displayNumber-1));
            guessIndex.set(1, trueGuessIndex.get(displayNumber));
            guessIndex.set(2, trueGuessIndex.get(displayNumber+1));
            guessIndex.set(3, trueGuessIndex.get(displayNumber+2));
            guessIndex.set(4, trueGuessIndex.get(displayNumber+3));
            guessIndex.set(5, trueGuessIndex.get(displayNumber+4));

            answerIndex.set(0, trueAnswerIndex.get(displayNumber-1));
            answerIndex.set(1, trueAnswerIndex.get(displayNumber));
            answerIndex.set(2, trueAnswerIndex.get(displayNumber+1));
            answerIndex.set(3, trueAnswerIndex.get(displayNumber+2));
            answerIndex.set(4, trueAnswerIndex.get(displayNumber+3));
            answerIndex.set(5, trueAnswerIndex.get(displayNumber+4));


        }
        else{
            return;
        }
        updateAnswerDisplay();
        updateKeyboard();
    }
    private static void scrollUp(){
        if (displayNumber>1){

            displayNumber = displayNumber -1;
            guessIndex.set(0, trueGuessIndex.get(displayNumber-1));
            guessIndex.set(1, trueGuessIndex.get(displayNumber));
            guessIndex.set(2, trueGuessIndex.get(displayNumber+1));
            guessIndex.set(3, trueGuessIndex.get(displayNumber+2));
            guessIndex.set(4, trueGuessIndex.get(displayNumber+3));
            guessIndex.set(5, trueGuessIndex.get(displayNumber+4));

            answerIndex.set(0, trueAnswerIndex.get(displayNumber-1));
            answerIndex.set(1, trueAnswerIndex.get(displayNumber));
            answerIndex.set(2, trueAnswerIndex.get(displayNumber+1));
            answerIndex.set(3, trueAnswerIndex.get(displayNumber+2));
            answerIndex.set(4, trueAnswerIndex.get(displayNumber+3));
            answerIndex.set(5, trueAnswerIndex.get(displayNumber+4));

        }
        else{
            return;
        }
        updateAnswerDisplay();
        updateKeyboard();
    }
    private static void updateLists(String input, String compiledResults){
        answerIndex.set(j, input);
        guessIndex.set(j, compiledResults);
        trueAnswerIndex.set(j, input);
        trueGuessIndex.set(j, compiledResults);
        scrollDown();        
    }
    private static void compileResults(String input, int guesses, Scanner scanner) {
        if (j>=numOfGuesses-1) {
            endProgram(scanner, false);
        }
        String currentGuess = input;
        currentGuess = prepareWord(currentGuess, true);
        StringBuffer output = new StringBuffer(initDupes(currentGuess));
        current = output.toString();
        
        if (!checkIfRealWord(currentGuess)) {
            j--;
            printGuesses("Please Enter A Valid Word", false);
            return;
        }

        updateLists(input, output.toString());
        updateAnswerDisplay();

        printGuesses(convertToWord(output.toString(), input), true);
        updateKeyboard();
        if (keepFirstGuess) {
            printGuesses(convertToWord(output.toString(), currentGuess), true);
            keepFirstGuess = false;
        }
        
        if(output.toString().equals("GGGGG")) {
            System.err.println("");
            updateHighscore(j, true);
            updateStatistics(true);
            endProgram(scanner, true);
                    
                    
            return;
        }
        
        
        return;
    }
    private static void endProgram(Scanner scanner, boolean win) {
        if (win) {
            System.err.println("Congratulations!");
            System.err.println("You have won!");
            System.err.println("Thanks for playing!");
            System.err.println("");
        }
        else{
            printer.print("");
            printer.print("Out of Guesses :(");
            printer.print("The word was " + answer + ".");
            updateHighscore(99, false);
            updateStatistics(false);
            printer.print("Try Again Next Time!");
            printer.print("");
        }
        

        

        scanner.close();
        System.exit(0);
        
    }
    
    public static void main(String[] args) throws Exception {
        
        initLists();
        keyBoardActionListener();
        updateAnswerDisplay();
        updateWindow();
        addActionListener();
        Scanner scanner = new Scanner(System.in);
        
        chooseWord(scanner);

        
        
        if (quickFix) {
            printSpaces();
            printer.print("Please Provide A Proper Value");
            printer.print("Program Terminating...");
            return;
        }
        
        printSpaces();
        printer.print("Please Input How Many Guesses You Would Like!");
        numOfGuesses = getNumOfGuesses(scanner);
        if (!usingKeyboard) {
            if (quickFix) {
                printSpaces();
                printer.print("Please Provide A Proper Value");
                printer.print("Program Terminating...");
                return;
            }
            else{
                addFrameListener();
                
                for(j = 0; j <= numOfGuesses; j++) {

                    try {
                        currentGuess = scanner.nextLine(); 
                    } catch (Exception e) {
                        usingKeyboard = true;
                        printGuesses("", false);
                        break;
                
                    }
                    
    
    
                    printer.print(currentGuess);
                    keyboardPrinted = true;
                    compileResults(currentGuess, j, scanner);
                    
                    
                }
            }
        }
       
        
        while(j!=numOfGuesses){
            //updateKeyboard();
            scanner.close();
        }
        endProgram(scanner, false);
        scanner.close();
       
        
    }
}

//add tab where you can use a word unscrambeler, words ending with specific letter etc