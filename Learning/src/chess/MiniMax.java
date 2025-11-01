package src.chess;

import java.util.List;

public class MiniMax {

    private static final int INF = 1000000;

    // Minimax with alpha-beta pruning
    public static int minimax(Board board, int depth, int alpha, int beta, int turn) {
        // Base case
        if (depth == 0) {
            return board.evaluate();
        }

        List<Move> moves = board.getLegalMoves(turn);
        if (moves.isEmpty()) {
            // no moves -> either checkmate or stalemate, let evaluation handle
            return board.evaluate();
        }

        if (turn == 1) { // White = maximizing
            int maxEval = -INF;
            for (Move move : moves) {
                board.applyMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, -turn);
                board.undoMove(move);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // beta cutoff
                }
            }
            return maxEval;
        } else { // Black = minimizing
            int minEval = INF;
            for (Move move : moves) {
                board.applyMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, -turn);
                board.undoMove(move);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // alpha cutoff
                }
            }
            return minEval;
        }
    }

    // Finds the best move at a given depth
    public static Move findBestMove(Board board, int depth, int turn) {
        List<Move> moves = board.getLegalMoves(turn);

        Move bestMove = null;
        int bestEval = (turn == 1) ? -INF : INF;

        for (Move move : moves) {
            board.applyMove(move);
            int eval = minimax(board, depth - 1, -INF, INF, -turn);
            board.undoMove(move);

            if (turn == 1 && eval > bestEval) {
                bestEval = eval;
                bestMove = move;
            } else if (turn == -1 && eval < bestEval) {
                bestEval = eval;
                bestMove = move;
            }
        }
        return bestMove;
    }

    // quick test
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Board board = new Board();
        board.print();
        Move best = MiniMax.findBestMove(board, 6, 1); // White to move
        System.err.println(best.fromRow + ", " + best.fromCol + " -> " + best.toRow + ", " + best.toCol);
        System.err.println("Found in: " + (System.currentTimeMillis()-time));
    }
}
