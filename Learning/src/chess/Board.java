package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    private char[][] board;
    private HashMap<Character,Integer> values = new HashMap<Character,Integer>();
    public Board() {
        board = new char[8][8];
        setup();
    }

    private void setup() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = '.';
            }
        }

        board[0] = new char[]{'r', 'n', 'b', 'q', 'k', 'b', '.', 'r'};
        board[1] = new char[]{'.', 'p', 'p', 'p', '.', '.', 'p', 'p'};
        board[2] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.'};
        board[3] = new char[]{'p', 'P', '.', '.', 'n', 'p', '.', '.'};
        board[4] = new char[]{'P', '.', 'P', '.', 'P', '.', '.', '.'};
        board[5] = new char[]{'.', '.', '.', '.', '.', '.', '.', '.'};
        board[6] = new char[]{'.', '.', '.', 'P', '.', 'P', 'P', 'P'};
        board[7] = new char[]{'R', 'N', 'B', '.', 'K', 'B', 'N', 'R'};

        values.put('P', 100);   
        values.put('N', 320);
        values.put('B', 330);   
        values.put('R', 500);
        values.put('Q', 900);   
        values.put('K', 20000);
        values.put('p', -100);  
        values.put('n', -320);
        values.put('b', -330);  
        values.put('r', -500);
        values.put('q', -900);  
        values.put('k', -20000);

    }

    public void print() {
        for (int row = 0; row < 8; row++) {
            System.out.print(row+" ");
            for (int col = 0; col < 8; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.err.print("\n");
        }
    }

    public char getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, char piece) {
        board[row][col] = piece;
    }

    public Move applyMove(int fromRow, int fromCol, int toRow, int toCol) {
        char moved = board[fromRow][fromCol];
        char captured = board[toRow][toCol];

        board[toRow][toCol] = moved;
        board[fromRow][fromCol] = '.';

        return new Move(fromRow, fromCol, toRow, toCol, moved, captured);
    }
    public Move applyMove(Move move) {
        char moved = board[move.fromRow][move.fromCol];
        
        board[move.toRow][move.toCol] = moved;
        board[move.fromRow][move.fromCol] = '.';

        return move;
    }

    public void undoMove(Move move) {
        board[move.fromRow][move.fromCol] = move.piece;
        board[move.toRow][move.toCol] = move.captured;
    }
    public int evaluate() {
        int position = 0;
        for(int k = 0; k < 8; k++) {
            for(int j = 0; j < 8; j++) {
                position += values.getOrDefault(board[k][j], 0);
            }
        }
        return position;
    }
    public List<Move> getLegalMoves(int turn) {
        //-1 black, 1 white
        ArrayList<Move> moves = new ArrayList<>();
        for(int k = 0; k < 8; k++) {
            for(int j = 0; j < 8; j++) {
                char currentPiece = board[k][j];
                if (turn==1) {
                    if (Character.isUpperCase(currentPiece)) {
                        switch (currentPiece) {
                            case 'R':
                                getRookMoves(k, j, turn, moves);
                                break;
                            case 'N':
                                getKnightMoves(k, j, turn, moves);
                                break;
                            case 'B':
                                getBisopMoves(k, j, turn, moves);
                                break;
                            case 'K':
                                getKingMoves(k, j, turn, moves);
                                break;
                            case 'Q':
                                getQueenMoves(k, j, turn, moves);
                                break;
                            case 'P':
                                getPawnMoves(k, j, turn, moves);
                                break;
                        }
                    }
                }
                else {
                    if (Character.isLowerCase(currentPiece)) {
                        switch (currentPiece) {
                            case 'r':
                                getRookMoves(k, j, turn, moves);
                                break;
                            case 'n':
                                getKnightMoves(k, j, turn, moves);
                                break;
                            case 'b':
                                getBisopMoves(k, j, turn, moves);
                                break;
                            case 'k':
                                getKingMoves(k, j, turn, moves);
                                break;
                            case 'q':
                                getQueenMoves(k, j, turn, moves);
                                break;
                            case 'p':
                                getPawnMoves(k, j, turn, moves);
                                break;
                        }
                    }
                }
            }
        }
        return moves;
    }
    public void getPawnMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        if (turn == 1) {
            if (row==6) {
                if (board[5][collumn]=='.' && board[4][collumn]=='.') {
                    Move move = new Move(row, collumn, 4, collumn, 'P', '.');
                    moves.add(move);
                }
            }
            if (collumn==0) {
                if (inBounds(row - 1, collumn) && Character.isLowerCase(board[row-1][collumn+1])) {
                    Move move = new Move(row, collumn, row-1, collumn+1, 'P', board[row-1][collumn+1]);
                    moves.add(move);
                }
            }
            else if (collumn==7){
                if (Character.isLowerCase(board[row-1][collumn-1])) {
                    Move move = new Move(row, collumn, row-1, collumn-1, 'P', board[row-1][collumn-1]);
                    moves.add(move);
                }
            }
            else if (inBounds(row - 1, collumn) && board[row-1][collumn]=='.') {
                Move move = new Move(row, collumn, row-1, collumn, 'P', '.');
                moves.add(move);
            }
            
            else if (inBounds(row - 1, collumn) && Character.isLowerCase(board[row-1][collumn-1])) {
                Move move = new Move(row, collumn, row-1, collumn-1, 'P', board[row-1][collumn-1]);
                moves.add(move);
            }
            else if (inBounds(row - 1, collumn) && Character.isLowerCase(board[row-1][collumn+1])) {
                Move move = new Move(row, collumn, row-1, collumn+1, 'P', board[row-1][collumn+1]);
                moves.add(move);
            }
        }
        else {
            if (row==1) {
                if (board[2][collumn]=='.' && board[3][collumn]=='.') {
                    Move move = new Move(row, collumn, 3, collumn, 'p', '.');
                    moves.add(move);
                }
            }
            if (collumn==0) {
                if (Character.isLowerCase(board[row+1][collumn+1])) {
                    Move move = new Move(row, collumn, row+1, collumn+1, 'p', board[row+1][collumn+1]);
                    moves.add(move);
                }
            }
            else if (collumn==7){
                if (Character.isLowerCase(board[row+1][collumn-1])) {
                    Move move = new Move(row, collumn, row+1, collumn-1, 'p', board[row+1][collumn-1]);
                    moves.add(move);
                }
            }
            else if (inBounds(row + 1, collumn) && board[row+1][collumn]=='.') {
                Move move = new Move(row, collumn, row+1, collumn, 'p', '.');
                moves.add(move);
            }
            
            else if (inBounds(row + 1, collumn) && Character.isLowerCase(board[row+1][collumn-1])) {
                Move move = new Move(row, collumn, row+1, collumn-1, 'p', board[row+1][collumn-1]);
                moves.add(move);
            }
            else if (inBounds(row + 1, collumn) && Character.isLowerCase(board[row+1][collumn+1])) {
                Move move = new Move(row, collumn, row+1, collumn+1, 'p', board[row+1][collumn+1]);
                moves.add(move);
            }
        
        }
    }
    private static boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    public void getKnightMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        int[][] vectors = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        if (turn==1) {
            
            for(int[] vector : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                if (inBounds(newRow, newCollumn)) {
                    if (!Character.isUpperCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'N', board[newRow][newCollumn]);
                        moves.add(move);
                    }
                }
                
            }
        }
        else {
            for(int[] vector : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                if (inBounds(newRow, newCollumn)) {
                    if (!Character.isLowerCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'n', board[newRow][newCollumn]);
                        moves.add(move);
                    }
                }
                
            }
        }
    }

    public void getBisopMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        int[][] vectors = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        if (turn == 1) {
            for(int[] vector : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                while (inBounds(newRow, newCollumn) && !Character.isUpperCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'B', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isLowerCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'B', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
        else {
            for(int[] vector : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                while (inBounds(newRow, newCollumn) && !Character.isLowerCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'b', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isUpperCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'b', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
    }
    public void getRookMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        int vectors[][] = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        if (turn == 1) {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];

                while (inBounds(newRow, newCollumn) && !Character.isUpperCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'R', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isLowerCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'R', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
        else {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];

                while (inBounds(newRow, newCollumn) && !Character.isLowerCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'r', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isUpperCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'r', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
    }
    public void getQueenMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        int vectors[][] = {{1,0}, {-1,0}, {0,1}, {0,-1},{-1, -1}, {-1, +1}, {+1, -1}, {+1, +1}};
        if (turn == 1) {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];

                while (inBounds(newRow, newCollumn) && !Character.isUpperCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'Q', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isLowerCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'Q', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
        else {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];

                while (inBounds(newRow, newCollumn) && !Character.isLowerCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'q', '.');
                        moves.add(move);
                        
                    }
                    else if (Character.isUpperCase(board[newRow][newCollumn])) {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'q', board[newRow][newCollumn]);
                        moves.add(move);
                        break;
                    }
                    newRow += vector[0];
                    newCollumn += vector[1];
                }
            }
        }
    }
    public void getKingMoves(int row, int collumn, int turn, ArrayList<Move> moves) {
        int[][] vectors = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        if (turn == 1) {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                if (inBounds(newRow, newCollumn) && !Character.isUpperCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'K', '.');
                        moves.add(move);
                    }
                    else {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'K', board[newRow][newCollumn]);
                        moves.add(move);
                    }
                }
            }
        }
        else {
            for(int vector[] : vectors) {
                int newRow = row + vector[0];
                int newCollumn = collumn + vector[1];
                if (inBounds(newRow, newCollumn) && !Character.isLowerCase(board[newRow][newCollumn])) {
                    if (board[newRow][newCollumn]=='.') {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'k', '.');
                        moves.add(move);
                    }
                    else {
                        Move move = new Move(row, collumn, newRow, newCollumn, 'k', board[newRow][newCollumn]);
                        moves.add(move);
                    }
                }
            }
        }
    }
}
