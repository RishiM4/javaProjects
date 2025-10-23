package chess;

public class GamePlayer {
    public static void main(String[] args) {
        

        BoardV3 board = new BoardV3();
        MiniMaxV4.findBestMove(1, board, 5);
        
    }
}
/*
    TO DO: 
    1. Move ordering for alpha beta
    2. Quiescence search(search all positions until there are no checks or captures)
    3. Zobrist hashing + transposition tables (avoid repeat positions, with hashing to efficiently save the board state)
    4. Iterative deepening 
    5. Early-game lookup tables
    6. Endgame move tables
    7. Anti 50-move rule
*/

/*
    COMPLETED:
    1. Alpha-beta pruning
*/

/*
    BENCHMARKS
    1. MiniMaxV3 - tested on chess.com, beat 1500 but lost to 2000
*/

/*
    ALGORITHM VS. ALGORITHM RESULTS

 */