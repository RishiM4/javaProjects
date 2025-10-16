package chess;

public class MiniMaxV3 {

    public static int minimax(BoardV2 board, int depth, int turn) {
        if (depth == 0) {
            return board.evaluate(); 
        }

        int bestScore;
        int[] moves = board.generateMoves(turn);

        if (turn == 1) {
            bestScore = Integer.MIN_VALUE;
            for (int move : moves) {
                if (move <= 0) continue;

                board.makeMove(move, turn);
                int score = minimax(board, depth - 1, -turn);
                board.undoMove(move, turn);

                if (score > bestScore) {
                    bestScore = score;
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int move : moves) {
                if (move <= 0) continue;

                board.makeMove(move, turn);
                int score = minimax(board, depth - 1, -turn);
                board.undoMove(move, turn);

                if (score < bestScore) {
                    bestScore = score;
                }
            }
        }

        return bestScore;
    }
    public static int findBestMove(BoardV2 board, int depth, int turn) {
        int bestScore;
        int[] moves = board.generateMoves(turn);
        int bestMove = 0;
        if (turn == 1) {
            bestScore = Integer.MIN_VALUE;
            for(int k = 0; k < moves[0]; k ++) {
                int move = moves[k];
                if (move <= 0) continue;

                board.makeMove(move, turn);
                int score = minimax(board, depth - 1, turn * -1);
                board.undoMove(move, turn);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int move : moves) {
                if (move <= 0) continue;

                board.makeMove(move, turn);
                int score = minimax(board, depth - 1, turn * -1);
                board.undoMove(move, turn);

                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;

                }
            }
        }
        return bestMove;
    }

    public static void main(String[] args) {
        BoardV2 board = new BoardV2();
        int depth = 6;

        long start = System.currentTimeMillis();
        int score = minimax(board, depth, 1);
        long end = System.currentTimeMillis();

        System.out.println("Best score at depth " + depth + ": " + score);
        System.out.println("Time: " + (end - start) + "ms");
        int move  = findBestMove(board, 3, 1);
        System.err.println(MoveV2.from(move));
        System.err.println(MoveV2.to(move));
    }
}
