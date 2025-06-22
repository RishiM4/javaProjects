import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
// 0 1 2
// 3 4 5
// 6 7 8
public class TicTacToeTest {
    static int initialBoard[] = {0,0,0,0,0,0,0,0,0};
    static int turn = 1;
    static JFrame frame = new JFrame();
    static JButton button1 = new JButton("");
    static JButton button2 = new JButton("");
        
    static JButton button3 = new JButton("");
    static JButton button4 = new JButton("");
    static JButton button5 = new JButton("");
    static JButton button6 = new JButton("");
    static JButton button7 = new JButton("");
    static JButton button8 = new JButton("");
    static JButton button9 = new JButton("");
    static JButton button10 = new JButton("O COMPUTER");
    static JButton button11 = new JButton("Reset Board");
    static Boolean oComputer = true;
    private static int getZeros(int[] board){
        int output = 0;
        for(int k = 0; k < 9; k++){
            if (board[k]==0) {
                output++;
            }
        }
        return output;
    }
    private static boolean checkForPossibleWin(int[] board,int player){
        //down 1 
        if ((board[0]+board[3]+board[6])==player) {
            return true;
        }
        //down 2
        if ((board[1]+board[4]+board[7])==player) {
            return true;

        }
        //down 3
        if ((board[2]+board[5]+board[8])==player) {
            return true;

        }
        //across 1 
        if ((board[0]+board[1]+board[2])==player) {
            return true;
        }
        //across 2
        if ((board[3]+board[4]+board[5])==player) {
            return true;

        }
        //across 3
        if ((board[6]+board[7]+board[8])==player) {
            return true;

        }
        //diagonal 1
        if ((board[0]+board[4]+board[8])==player) {
            return true;

        }
        //diagonal 2
        if ((board[6]+board[4]+board[2])==player) {
            return true;

        }
        return false;
    }
    private static int checkForWin(int[] board){
        //3 down, 3 across, 2 diagonal
        //down 1
        if (board[0]==board[3]&&board[0]==board[6]) {
            if (board[0]==1) {
                return 1;
            }
            if (board[0]==-1) {
                return -1;
            }
        }
        //down 2
        if (board[1]==board[4]&&board[1]==board[7]) {
            if (board[1]==1) {
                return 1;

            }
            if (board[1]==-1) {
                return -1;
            }
        }
        //down 3
        if (board[2]==board[5]&&board[2]==board[8]) {
            if (board[2]==1) {
                return 1;

            }
            if (board[2]==-1) {
                return -1;
            }
        }
        //across 1
        if (board[0]==board[1]&&board[0]==board[2]) {
            if (board[0]==1) {
                return 1;

            }
            if (board[0]==-1) {
                return -1;
            }
        }
        //across 2
        if (board[3]==board[4]&&board[3]==board[5]) {
            if (board[4]==1) {
                return 1;

            }
            if (board[4]==-1) {
                return -1;
            }
        }
        //across 3
        if (board[6]==board[7]&&board[6]==board[8]) {
            if (board[6]==1) {
                return 1;

            }
            if (board[6]==-1) {
                return -1;
            }
        }
        //diagonal 1
        if (board[0]==board[4]&&board[0]==board[8]) {
            if (board[0]==1) {
                return 1;

            }
            if (board[0]==-1) {
                return -1;
            }
        }
        //diagonal 2
        if (board[2]==board[4]&&board[2]==board[6]) {
            if (board[2]==1) {
                return 1;
            }
            if (board[2]==-1) {
                return -1;
            }
        }       
        return 0;
    }
    private static int[] evaluateBoard(int[] board, int[] results, int depth) {
       
        if (checkForWin(board)==1) {
            results[0] += 1;
            results[1] += 1;
            return results;
        }
        else if (checkForWin(board)==-1) {
            results[0] += 1;
            results[1] += -1;
            return results;
        }
        if (getZeros(board)==0) {
            results[0] += 1;
            return results;
        }
        
        if (depth == 0 || depth == 2 || depth == 4 || depth == 6 || depth == 8) {
           
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    int[] tempBoard = board;
                    board[k] = 1;
                    evaluateBoard(tempBoard, results, depth+1);
                    board[k] = 0;
                }
                
            }
        }
        else {
            
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    int[] tempBoard = board;
                    board[k] = -1;
                    evaluateBoard(tempBoard, results, depth+1);
                    board[k] = 0;
                }
            }
        }
        return results;
    }
    private static void printBoard(int[] board){
        String output[] = {"","","","","","","","",""};
        for(int k = 0; k < 9; k++) {
            if (board[k]==1) {
                output[k] = "X";
            }
            else if (board[k]==-1) {
                output[k] = "O";
            
            }
            else {
                output[k] = " ";
            }
        }
        System.err.println(output[0]+" "+output[1]+" "+output[2]);
        System.err.println(output[3]+" "+output[4]+" "+output[5]);
        System.err.println(output[6]+" "+output[7]+" "+output[8]);
    }
    private static int findOptimalMoveX(int[] board) {
        ArrayList<Double> result = new ArrayList<Double>();
       
        if (checkForPossibleWin(board.clone(), 2)) {
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    board[k] = 1;
                    if (checkForWin(board)==1) {
                        return k;
                    }
                    board[k] = 0;
                }
            
            }
        }
        if (checkForPossibleWin(board.clone(), -2)) {
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    board[k] = -1;
                    if (checkForWin(board)==-1) {
                        return k;
                    }
                    board[k] = 0;
                }
            
            }
        }
        for(int k = 0; k < 9; k++) {
            
            //System.out.println(board[k]);
            if (board[k]==0) {
                //System.out.println("BYE");
                int results[] = {0,0};
                
                board[k] = 1;

                int[] temp = evaluateBoard(board, results, 0);
                result.add((double)temp[1]/temp[0]);
                board[k] = 0;
                
            }
            else {
                result.add(-9.0);
            }

        }
        
        int highestResult = 0;
        for (int k = 1; k < 9; k++) {
            if(result.get(k)>result.get(highestResult)) {
                highestResult = k;
            }
            
        }
       
        return highestResult;
    }
    
    private static int findOptimalMoveO(int[] board) {
        ArrayList<Double> result = new ArrayList<Double>();
       
        if (checkForPossibleWin(board.clone(), -2)) {
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    board[k] = -1;
                    if (checkForWin(board)==-1) {
                        return k;
                    }
                    board[k] = 0;
                }
            
            }
        }
        if (checkForPossibleWin(board.clone(), 2)) {
            for(int k = 0; k < 9; k++) {
                if (board[k]==0) {
                    board[k] = 1;
                    if (checkForWin(board)==1) {
                        return k;
                    }
                    board[k] = 0;
                }
            
            }
        }
        for(int k = 0; k < 9; k++) {
            
            //System.out.println(board[k]);
            if (initialBoard[k]==0) {
                //System.out.println("BYE");
                int results[] = {0,0};
                
                board[k] = -1;
                int[] temp = evaluateBoard(board, results, 1);
                result.add((double)temp[1]/temp[0]);
                board[k] = 0;
                
            }
            else {
                result.add(9.0);
            }

        }
        
        int highestResult = -1;
        for (int k = 0; k < 9; k++) {
            if (result.get(k) != 9.0) {
                highestResult = k;

                break;
            }
        }
        for (int k = 1; k < 9; k++) {
            if(result.get(k)<result.get(highestResult)) {
                highestResult = k;
            }
            
        }
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return highestResult;
    }
    private static void makeMoveO(int k) {
        if (getZeros(initialBoard)==0) {
            if (checkForWin(initialBoard) == 1 && oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
                return;
            }
            else if (checkForWin(initialBoard) == -1 && oComputer) {
                
                turn = 0;
                JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
                return;
            }
            else if (checkForWin(initialBoard) == 1 && !oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
                return;
            }
            else if (checkForWin(initialBoard) == 1 && !oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
                return;
            }
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You were able to draw!", ":D", -1);
            resetBoard();
            return;
        }
        
        if (k == 0) {
            button1.setText("O");
        }
        else if (k == 1) {
            button2.setText("O");
        }
        else if (k == 2) {
            button3.setText("O");
        }
        else if (k == 3) {
            button4.setText("O");
        }
        else if (k == 4) {
            button5.setText("O");
        }
        else if (k == 5) {
            button6.setText("O");
        }
        else if (k == 6) {
            button7.setText("O");
        }
        else if (k == 7) {
            button8.setText("O");
        }
        else if (k == 8) {
            button9.setText("O");
        }
        
        if (checkForWin(initialBoard) == 1 && oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
        }
        else if (checkForWin(initialBoard) == -1 && oComputer) {
            System.err.println("HIIII");
            turn = 0;
            JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
        }
        else if (checkForWin(initialBoard) == 1 && !oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
            
        }
        else if (checkForWin(initialBoard) == 1 && !oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
        }
        else {
            turn = 1;
        }
    }
    private static void makeMoveX(int k) {
        if (getZeros(initialBoard)==0) {
            if (checkForWin(initialBoard) == 1 && oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
                return;
            }
            else if (checkForWin(initialBoard) == -1 && oComputer) {
                
                turn = 0;
                JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
                return;
            }
            else if (checkForWin(initialBoard) == 1 && !oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
                return;
            }
            else if (checkForWin(initialBoard) == 1 && !oComputer) {
                turn = 0;
                JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
                return;
            }
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You were able to draw!", ":D", -1);
            resetBoard();
            return;
        }
        
        if (k == 0) {
            button1.setText("X");
        }
        else if (k == 1) {
            button2.setText("X");
        }
        else if (k == 2) {
            button3.setText("X");
        }
        else if (k == 3) {
            button4.setText("X");
        }
        else if (k == 4) {
            button5.setText("X");
        }
        else if (k == 5) {
            button6.setText("X");
        }
        else if (k == 6) {
            button7.setText("X");
        }
        else if (k == 7) {
            button8.setText("X");
        }
        else if (k == 8) {
            button9.setText("X");
        }
        
       
        if (checkForWin(initialBoard) == 1 && oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
        }
        else if (checkForWin(initialBoard) == -1 && oComputer) {
            
            turn = 0;
            JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
        }
        else if (checkForWin(initialBoard) == 1 && !oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "You have lost! \n Try again next time!", ":(", -1);
            
        }
        else if (checkForWin(initialBoard) == 1 && !oComputer) {
            turn = 0;
            JOptionPane.showMessageDialog(frame, "Congratulations! \n You have won!", ":D", -1);
        }
        else {
            turn = 1;
        }
    }
    private static void resetBoard() {
        initialBoard[0] = 0;
        initialBoard[1] = 0;
        initialBoard[2]= 0;
        initialBoard[3] = 0;
        initialBoard[4] = 0;
        initialBoard[5] = 0;
        initialBoard[6] = 0;
        initialBoard[7] = 0;
        initialBoard[8] = 0;
        button1.setText("");
        button2.setText("");
        button3.setText("");
        button4.setText("");
        button5.setText("");
        button6.setText("");
        button7.setText("");
        button8.setText("");
        button9.setText("");
        if (!oComputer&&getZeros(initialBoard)==9) {
            double j = Math.random();
            if (j<0.25) {
                initialBoard[0] = 1;
                makeMoveX(0);
                        
            }
            else if (j < 0.50){
                initialBoard[2] = 1;
                makeMoveX(2);
            }
            else if (j < 0.75) {
                initialBoard[6] = 1;
                makeMoveX(6);
            }
            else {
                initialBoard[8] = 1;
                makeMoveX(8);
            }
        }
        turn = 1;
    }

    public static void main(String[] args) {
        
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        
        button1.setBounds(10,10,50,50);
        button2.setBounds(70,10,50,50);
        button3.setBounds(130,10,50,50);
        button4.setBounds(10,70,50,50);
        button5.setBounds(70,70,50,50);
        button6.setBounds(130,70,50,50);
        button7.setBounds(10,130,50,50);
        button8.setBounds(70,130,50,50);
        button9.setBounds(130,130,50,50);
        button10.setBounds(200,10,200,50);
        button11.setBounds(200,70,200,50);
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);
        frame.add(button5);
        frame.add(button6);
        frame.add(button7);
        frame.add(button8);
        frame.add(button9);
        frame.add(button10);
        frame.add(button11);
        button11.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
            
        });
        button10.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (button10.getText()=="O COMPUTER") {
                    button10.setText("X COMPUTER");
                    oComputer = false;
                    resetBoard();
                    double j = Math.random();
                    

                }
                else {
                    button10.setText("O COMPUTER");
                    oComputer = true;
                    resetBoard();
                }
            }
            
        });
        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (turn == 1 && initialBoard[0] == 0 &&oComputer) {
                    button1.setText("X");
                    initialBoard[0] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[0] == 0 && !oComputer) {
                    button1.setText("O");
                    initialBoard[0] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[1] == 0 &&oComputer) {
                    button2.setText("X");
                    initialBoard[1] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    
                    
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[1] == 0 && !oComputer) {
                    button2.setText("O");
                    initialBoard[1] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[2] == 0 &&oComputer) {
                    button3.setText("X");
                    initialBoard[2] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);    
                }
                else if (turn == 1 && initialBoard[2] == 0 && !oComputer) {
                    button3.setText("O");
                    initialBoard[2] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[3] == 0 &&oComputer) {
                    button4.setText("X");
                    initialBoard[3] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[3] == 0 && !oComputer) {
                    button4.setText("O");
                    initialBoard[3] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[4] == 0 &&oComputer) {
                    button5.setText("X");
                    initialBoard[4] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);                
                }
                else if (turn == 1 && initialBoard[4] == 0 && !oComputer) {
                    button5.setText("O");
                    initialBoard[4] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[5] == 0 &&oComputer) {
                    button6.setText("X");
                    initialBoard[5] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[5] == 0 && !oComputer) {
                    button6.setText("O");
                    initialBoard[5] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button7.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[6] == 0 &&oComputer) {
                    button7.setText("X");
                    initialBoard[6] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[6] == 0 && !oComputer) {
                    button7.setText("O");
                    initialBoard[6] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button8.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (turn == 1 && initialBoard[7] == 0 &&oComputer) {
                    button8.setText("X");
                    initialBoard[7] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[7] == 0 && !oComputer) {
                    button8.setText("O");
                    initialBoard[7] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        button9.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (turn == 1 && initialBoard[8] == 0 &&oComputer) {
                    button9.setText("X");
                    initialBoard[8] = 1;
                    int k = findOptimalMoveO(initialBoard);
                    initialBoard[k] = -1;
                    turn = -1;
                    
                    makeMoveO(k);
                }
                else if (turn == 1 && initialBoard[8] == 0 && !oComputer) {
                    button9.setText("O");
                    initialBoard[8] = -1;
                    int k = findOptimalMoveX(initialBoard);
                    initialBoard[k] = 1;
                    turn = -1;
                    makeMoveX(k);
                }
            }
            
        });
        
        
    }

}
