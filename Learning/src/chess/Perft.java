package chess;

public class Perft {

    public static long perft(BoardV2 board, int depth, int turn) {
        if (depth == 0) {
            return 1; 
        }

        long nodes = 0;
        int[] moves = board.generateMoves(turn);
        for(int k = 1; k < moves[0]; k++) {
            int move = moves[k];
            if (move > 0) {
                board.makeMove(move, turn);
                nodes += perft(board, depth - 1, turn * -1);
                board.undoMove(move, turn);
            }
            
        }
        
        return nodes;
    }

    public static void main(String[] args) {
        
        BoardV2 board = new BoardV2(); 
        perft(board, 3, 1);
        perft(board, 3, 1);
        perft(board, 3, 1);
        perft(board, 3, 1);
        perft(board, 4, 1);
        perft(board, 4, 1);
        perft(board, 4, 1);
        long start = System.currentTimeMillis();
        int depth = 5; 
        long nodes = perft(board, depth, -1);

        System.out.println("Perft(" + depth + ") = " + nodes);
        System.err.println(System.currentTimeMillis()-start);
    }
}
