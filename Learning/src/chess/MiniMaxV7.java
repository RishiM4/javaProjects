package chess;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import chess.BoardV3.Move;
import chess.BoardV3.UndoData;

public class MiniMaxV7 {
    public static int quiescence(int turn, BoardV3 board, int depth, int alpha, int beta, int allotedTime) {
        List<Move> moves = board.generateLegalMoves();
        boolean hasCapture = false;
        for (Move move : moves) {
            if (move.isCapture) {
                hasCapture = true;
            }
        }
        if (!hasCapture) {
            return board.evaluate();
        }
        return 0;
    }
    public static int miniMax(int turn, BoardV3 board, int depth, int alpha, int beta, long allottedTime) {
        if (depth == 0) {
            return board.evaluate();
        }
        if (allottedTime < System.currentTimeMillis()) {
            throw new RuntimeException();
        }
        List<Move> moves = board.generateLegalMoves();
        Collections.sort(moves, new MoveComparator());

        if (moves.isEmpty()) {
            if (board.isMate(turn)) {
                return turn == 1 ? Integer.MIN_VALUE + depth: Integer.MAX_VALUE - depth;
            }
            else {
                return 0;
            }
        }
        if (turn == 1) {
            int eval = Integer.MIN_VALUE;
            
            for(Move move : moves) {
                UndoData undo = board.makeMove(move);
                eval = Math.max(eval, miniMax(turn * -1, board, depth - 1, alpha, beta, allottedTime));
                alpha = Math.max(alpha, eval);
                
                board.unmakeMove(move, undo);
                if (beta <= alpha) {
                    break; // prune
                }
            }
            return eval;
        }
        else {
            int eval = Integer.MAX_VALUE;
            for(Move move : moves) {
                UndoData undo = board.makeMove(move);
                eval = Math.min(eval, miniMax(turn * -1, board, depth - 1, alpha, beta, allottedTime));
                beta = Math.min(beta, eval);
                
                board.unmakeMove(move, undo);
                if (beta <= alpha) {
                    break; // prune
                }
            }
            return eval;
        }


    }
    public static Move findBestMove(int turn, BoardV3 board, int depth, List<Move> moves, long allottedTime) {
        Move bestMove = null;

        if (turn == 1) {
            int eval = Integer.MIN_VALUE;
            for (Move move : moves) {
                UndoData undo = board.makeMove(move);
                if (board.generateLegalMoves().isEmpty() && board.isMate(turn * -1)) {
                    return move;
                }
                int currentEval = miniMax(-turn, board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, allottedTime);
                board.unmakeMove(move, undo);

                if (currentEval > eval) {
                    eval = currentEval;
                    bestMove = move;
                }
            }
        } else {
            int eval = Integer.MAX_VALUE;
            for (Move move : moves) {
                UndoData undo = board.makeMove(move);
                int currentEval = miniMax(-turn, board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, allottedTime);
                board.unmakeMove(move, undo);
                if (board.generateLegalMoves().isEmpty() && board.isMate(turn * -1)) {
                    return move;
                }
                if (currentEval < eval) {
                    eval = currentEval;
                    bestMove = move;
                    
                }
            }
        }
        return bestMove;
    }
    public static Move iterate(int time, BoardV3 board) {
        long start = System.currentTimeMillis();
        Move bestMove = null;
        int depth = 1;
        List<Move> moves = board.generateLegalMoves();
        while (time > (System.currentTimeMillis() - start)) {
            try {
                Move newMove = findBestMove(1, board, depth, moves, time + System.currentTimeMillis());
                if (newMove != bestMove) {
                    bestMove = newMove;
                    if (moves.contains(bestMove)) {
                        moves.get(moves.indexOf(bestMove)).weight = depth;
                        Collections.sort(moves, new MoveComparatorV2());
                    }
                }
            
                depth++;
            } catch (Exception e) {
                break;
            }
            
        }
        if (bestMove == null) {
            return findBestMove(1, board, depth, moves, 1000);
        }
        System.err.println("Reached depth of: "+ depth);
        return bestMove;
    }

    public static void main(String[] args) {
        BoardV3 board = new BoardV3();
        board.setFEN("1n2r3/5pk1/1Rp3p1/p1Np1P2/8/P1P4p/1P6/1K2r3 b - - 0 44");
        
        Scanner scanner = new Scanner(System.in);
        while(true) {  
            String t = scanner.nextLine();
            board.setFEN(t);
            //board.setFEN("1n2r3/5pk1/1Rp3p1/p1Np1P2/8/P1P4p/1P6/1K2r3 b - - 0 44"); 
            long start = System.currentTimeMillis();
            System.err.println(iterate(10000, board));
            System.err.println("Found move in " + (System.currentTimeMillis() - start));
            
        }
    }
}
