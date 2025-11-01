package src.chess;

public class MiniMaxV2 {

    private static final int INF = 1000000;

    public static int minimax(BoardV2 board, int depth, int alpha, int beta, int turn) {
        if (depth == 0) {
            return board.evaluate();
        }

        int[] moves = board.generateMoves(turn);
        if (moves[0]==1) {
            // no moves -> either checkmate or stalemate, let evaluation handle
            return board.evaluate();
        }

        if (turn == 1) { 
            int maxEval = -INF;
            for(int k = 1; k < moves[0]; k++) {
                board.makeMove(moves[k], turn);
                int eval = minimax(board, depth - 1, alpha, beta, turn * -1);
                board.undoMove(moves[k], turn);

                maxEval = Math.max((maxEval), eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else { // Black = minimizing
            int minEval = INF;
            for(int k = 1; k < moves[0]; k++) {
                board.makeMove(moves[k], turn);
                int eval = minimax(board, depth - 1, alpha, beta, turn * -1);
                board.undoMove(moves[k], turn);

                minEval = Math.max((minEval), eval);
                beta = Math.min(minEval, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            
            return minEval;
        }
    }

    public static int findBestMove(BoardV2 board, int depth, int turn) {
        int[] moves = board.generateMoves(turn);
        int bestMove = 0;
        int bestEval = (turn == 1) ? -INF : INF;
        for (int k = 1; k < moves[0]; k++) {
            board.makeMove(moves[k], turn);
            int eval = minimax(board, depth - 1, -INF, INF, -turn);
            board.undoMove(moves[k], turn);

            if (turn == 1 && eval > bestEval) {
                bestEval = eval;
                bestMove = moves[k];
            } else if (turn == -1 && eval < bestEval) {
                bestEval = eval;
                bestMove = moves[k];
            }
        }
        
        return bestMove;
    }
    public static String toChessNotation(int square) {
        int file = square % 8; 
        int rank = square / 8; 

        char charFile = (char) ('a' + file);  
        rank = rank + 1;              

        return "" + charFile + rank;
    }
    public static int toSquareNumber(String notation) {
        
        char fileChar = notation.charAt(0);
        int rank = Integer.parseInt(notation.substring(1));

        

        int fileIndex = fileChar - 'a';   
        int rankIndex = rank - 1;         

        return rankIndex * 8 + fileIndex; 
    }
    
    public static void main(String[] args) {
        BoardV2 board = new BoardV2();
        board.printBoard();
        while (true) {
            int best = findBestMove(board, 5, 1);
            System.err.println(best);
            board.makeMove(best, 1);
            board.printBoard();

        }
    }
}
