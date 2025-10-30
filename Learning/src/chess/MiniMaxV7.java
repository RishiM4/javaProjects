package chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import chess.BoardV3.Move;
import chess.BoardV3.UndoData;

public class MiniMaxV7 {
    public static int nodes = 0;
    public static int quiescence(int turn, BoardV3 board, int depth, int alpha, int beta, long allottedTime) throws Exception{
        nodes++;
        
        List<Move> moves = new ArrayList<>();
        board.generatePseudoLegalAttacks(moves);
        Collections.sort(moves, new MoveComparator());
        int standPat = board.evaluate();
        if (standPat >= beta) {
            return beta;
        }
        if (standPat > alpha) {
            alpha = standPat;
        }
        if (moves.isEmpty()) {
            return standPat;
        }
        if ((nodes & 0x3FF) == 0) { // 0x3FF = 1023
            if (System.currentTimeMillis() > allottedTime) {
                throw new Exception("Time's up");
            }
        }
        if (depth > 5) {
            return standPat;
        }
        if (turn == 1) {
            int eval = Integer.MIN_VALUE;
            for (Move move : moves) {
                UndoData undoData = board.makeMove(move);
                eval = Math.max(eval, quiescence(turn * -1, board, depth + 1, alpha, beta, allottedTime));
                alpha = Math.max(eval, alpha);
                board.unmakeMove(move, undoData);
                if (beta <= alpha) {
                    break; // prune
                }
            }
            return eval;
        }
        else {
            int eval = Integer.MAX_VALUE;
            for (Move move : moves) {
                UndoData undoData = board.makeMove(move);
                eval = Math.min(eval, quiescence(turn * -1, board, depth + 1, alpha, beta, allottedTime));
                beta = Math.min(eval, beta);
                board.unmakeMove(move, undoData);
                if (beta <= alpha) {
                    break; // prune
                }
            }
            return eval;
        }
       
    }
    public static int miniMax(int turn, BoardV3 board, int depth, int alpha, int beta, long allottedTime) throws Exception{
        nodes++;
        if (depth == 0) {
            return quiescence(turn, board, 1, alpha, beta, allottedTime);
        }
        if ((nodes & 0x3FF) == 0) { // 0x3FF = 1023
            if (System.currentTimeMillis() > allottedTime) {
                throw new Exception("Time's up");
            }
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
    public static Move findBestMove(int turn, BoardV3 board, int depth, List<Move> moves, long allottedTime) throws Exception{
        nodes++;
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
    public static Move iterate(int turn, int time, BoardV3 board) {
        nodes++;
        long start = System.currentTimeMillis();
        Move bestMove = null;
        int depth = 1;
        List<Move> moves = board.generateLegalMoves();
        while (time > (System.currentTimeMillis() - start)) {
            try {
                Move newMove = findBestMove(turn, board, depth, moves, time + System.currentTimeMillis());
                if (newMove != bestMove) {
                    bestMove = newMove;
                    if (moves.contains(bestMove)) {
                        moves.get(moves.indexOf(bestMove)).weight = depth;
                        Collections.sort(moves, new MoveComparatorV2());
                    }
                }
            
                depth++;
            } catch (Exception e) {
                //e.printStackTrace();
                break;
            }
            
        }
        if (bestMove == null) {
            //return findBestMove(1, board, depth, moves, 1000);
        }
        System.err.println("Reached depth of: "+ depth);
        System.err.println("Traversed %s nodes".formatted(nodes));
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
            System.err.println(iterate(1, 10000, board));
            System.err.println("Found move in " + (System.currentTimeMillis() - start));
            
        }
    }
}
