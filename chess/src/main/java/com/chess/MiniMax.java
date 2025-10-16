package com.chess;

import java.util.List;

public class MiniMax {

    private int maxDepth;

    public MiniMax(int depth) {
        this.maxDepth = depth;
    }

    /**
     * Get the best move for the current position.
     * Debug prints each move's evaluation.
     */
    public Board.Move getBestMove(Board board) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int bestScore = Integer.MIN_VALUE;
        Board.Move bestMove = null;

        List<Board.Move> moves = board.generateLegalMoves();
        System.out.println("Generated " + moves.size() + " legal moves");

        for (Board.Move move : moves) {
            Board.UndoData ud = board.makeMove(move);
            int score = -minimax(board, maxDepth - 1, -beta, -alpha);
            board.unmakeMove(move, ud);

            System.out.printf("Move %s evaluated to %d%n", move, score);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                System.out.println("New best move: " + bestMove + " with score " + bestScore);
            }
        }

        System.out.println("Best move chosen: " + bestMove + " with score " + bestScore);
        return bestMove;
    }

    /**
     * Minimax with alpha-beta pruning.
     */
    private int minimax(Board board, int depth, int alpha, int beta) {
        if (depth == 0) return evaluate(board);

        List<Board.Move> moves = board.generateLegalMoves();
        if (moves.isEmpty()) {
            // Checkmate or stalemate
            if (board.isKingAttacked(board.sideToMove)) return -100000; // losing position
            return 0; // stalemate
        }

        int bestScore = Integer.MIN_VALUE;
        for (Board.Move move : moves) {
            Board.UndoData ud = board.makeMove(move);
            int score = -minimax(board, depth - 1, -beta, -alpha);
            board.unmakeMove(move, ud);

            if (score > bestScore) bestScore = score;
            if (score > alpha) alpha = score;

            if (alpha >= beta) break; // alpha-beta cutoff
        }

        return bestScore;
    }

    /**
     * Simple evaluation function: material + piece values.
     * White positive, black negative.
     */
    private int evaluate(Board board) {
        int score = 0;

        int[] pieceValues = {100, 320, 330, 500, 900, 20000}; // Pawn, Knight, Bishop, Rook, Queen, King
        for (int p = 0; p < 6; p++) {
            long wPieces = board.pieceBB[Board.WHITE*6 + p];
            long bPieces = board.pieceBB[Board.BLACK*6 + p];
            int wCount = Long.bitCount(wPieces);
            int bCount = Long.bitCount(bPieces);
            score += pieceValues[p] * (wCount - bCount);
        }

        // Check for checkmate
        if (board.generateLegalMoves().isEmpty()) {
            if (board.isKingAttacked(board.sideToMove)) {
                score -= 100000; // Losing
            } else {
                score = 0; // Stalemate
            }
        }

        return board.sideToMove == Board.WHITE ? score : -score;
    }

    /**
     * Simple test harness
     */
    public static void main(String[] args) {
        Board board = new Board();
        board.setFEN("6k1/5ppp/8/8/8/5Q2/5PPP/6K1 w - - 0 1");
        MiniMax ai = new MiniMax(7); // depth 2 for quick debug
        Board.Move best = ai.getBestMove(board);

        System.out.println("AI selected move: " + best);
    }
}
