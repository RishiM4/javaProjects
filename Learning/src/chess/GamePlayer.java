package src.chess;

import java.util.Scanner;

import src.chess.BoardV3.Move;

public class GamePlayer {
    public static void main(String[] args) {
        

        BoardV3 board = new BoardV3();
        Scanner scanner = new Scanner(System.in);
        while(board.generateLegalMoves().size() != 0) {
            //scanner.next();
            Move bestMove = MiniMaxV5.findBestMove(1, board, 5);
            board.makeMove(bestMove);

            bestMove = MiniMaxV6.iterate(1,5000, board);
            board.makeMove(bestMove);
            System.err.println(board.evaluate());
            System.err.println(board.toStringBoard());
        }

        scanner.close();
    }
}
/*
    TO DO: 
    1. Quiescence search(search all positions until there are no checks or captures)
    2. Zobrist hashing + transposition tables (avoid repeat positions, with hashing to efficiently save the board state)
    3. Opening lookup tables
    4. Endgame move tables (Syzygy tablebase)
    5. Anti 50-move rule/ 3 fold repitition
    6. Enhanced eval function (king safety, defended pawns, doubled pawns, passed pawns, etc)
    7. Multi-threading
    8. NNUE
*/

/*
    COMPLETED:
    1. Alpha-beta pruning
    2. Move ordering for alpha beta (75% faster minimax)
    3. Iterative deepening (84% faster minimax)

*/

/*
    BENCHMARKS
    1. MiniMaxV4 - tested on chess.com, beat 1500 but lost to 2000
*/

/*
    ALGORITHM VS. ALGORITHM RESULTS

 */