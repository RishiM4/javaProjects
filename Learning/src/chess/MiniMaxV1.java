package chess;

import java.util.List;
import java.util.Random;

public class MiniMaxV1 {
    public static chess.BoardV3.Move findBestMove(int turn, BoardV3 board) {
        List<chess.BoardV3.Move> moves= board.generateLegalMoves();
        Random random = new Random();
        int index = random.nextInt(moves.size());
        return moves.get(0);
    }
}
