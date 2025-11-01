package src.chessV2;

import java.util.*;

public class MiniMax {
    private final BitBoard board;
    private final int maxDepth;

    public MiniMax(BitBoard b, int depth) {
        this.board = b;
        this.maxDepth = depth;
    }

    public int[] findBestMove() {
        List<Integer> moves = board.genLegalMoves();
        if (moves.isEmpty()) return new int[]{Integer.MIN_VALUE, 0};

        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;

        for (int m : moves) {
            board.makeMove(m);
            int score = -negamax(maxDepth - 1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
            board.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = m;
            }
        }
        return new int[]{bestScore, bestMove};
    }

    private int negamax(int depth, int alpha, int beta) {
        if (depth == 0) return board.evaluate();

        List<Integer> moves = board.genLegalMoves();
        if (moves.isEmpty()) {
            if (board.inCheck(board.sideToMove)) return -100000 + (maxDepth - depth);
            return 0; // stalemate
        }

        int best = Integer.MIN_VALUE;
        for (int m : moves) {
            board.makeMove(m);
            int score = -negamax(depth - 1, -beta, -alpha);
            board.undoMove();

            if (score > best) best = score;
            if (best > alpha) alpha = best;
            if (alpha >= beta) break;
        }
        return best;
    }

    public static void main(String[] args) {
        char[][] start = {
            {'r','n','b','q','k','.','n','r'},
            {'p','p','p','p','.','p','p','p'},
            {'.','.','.','.','.','.','.','.'},
            {'.','.','b','.','p','.','.','.'},
            {'.','.','.','P','.','.','.','.'},
            {'P','.','.','.','.','.','.','.'},
            {'.','P','P','.','P','P','P','P'},
            {'R','N','B','Q','K','B','N','R'}
        };

        
        long time = System.currentTimeMillis();
        BitBoard board = new BitBoard();
        board.loadFromArray(start, BitBoard.WHITE);
        MiniMax engine = new MiniMax(board, 5);

        int[] res = engine.findBestMove();
        int bestScore = res[0], bestMove = res[1];

        if (Math.abs(bestScore) > 90000) {
            int mateIn = 100000 - Math.abs(bestScore);
            System.out.println("Mate in " + mateIn + " moves.");
        } else {
            System.out.println("Best score: " + bestScore);
        }
        System.out.println("Best move: " + BitBoard.moveToString(bestMove));
        System.err.println("TIME:" + (System.currentTimeMillis()-time));
    }
}
