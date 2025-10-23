package chess;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import chess.BoardV3.Move;
import chess.BoardV3.UndoData;

public class MiniMaxV4 {
    public static int miniMax(int turn, BoardV3 board, int depth, int alpha, int beta) {
        if (depth == 0) {
            return board.evaluate();
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
                eval = Math.max(eval, miniMax(turn * -1, board, depth - 1, alpha, beta));
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
                eval = Math.min(eval, miniMax(turn * -1, board, depth - 1, alpha, beta));
                beta = Math.min(beta, eval);
                
                board.unmakeMove(move, undo);
                if (beta <= alpha) {
                    break; // prune
                }
            }
            return eval;
        }


    }
    public static Move findBestMove(int turn, BoardV3 board, int depth) {
        List<Move> moves = board.generateLegalMoves();
        Collections.sort(moves, new MoveComparator());
        Move bestMove = null;

        if (turn == 1) {
            int eval = Integer.MIN_VALUE;
            for (Move move : moves) {
                UndoData undo = board.makeMove(move);
                if (board.generateLegalMoves().isEmpty() && board.isMate(turn * -1)) {
                    return move;
                }
                int currentEval = miniMax(-turn, board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
                int currentEval = miniMax(-turn, board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
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

    public static void main(String[] args) {
        BoardV3 board = new BoardV3();
        board.setFEN("startpos");
        Scanner scanner = new Scanner(System.in);
        while(true) {  
            String t = scanner.nextLine();
            board.setFEN(t); 
            long start = System.currentTimeMillis();
            //System.err.println(findBestMove(1, board, 7));
            System.err.println(board.generateLegalMoves().size());
            System.err.println("Found move in " + (System.currentTimeMillis() - start));
            //4179
            //r1b3k1/pp3pp1/4p2p/3r4/3B1Q2/2Pq1P2/PP4PP/R3K2R w KQ - 3 20
            //51073
        }
    }
}
