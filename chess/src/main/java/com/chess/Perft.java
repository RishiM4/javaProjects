package com.chess;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class Perft {

    public static long perft(Board board, int depth) {
        if (depth == 0) {
            return 1;
        }

        long nodes = 0;
        List<Move> moves = board.legalMoves();

        for (Move move : moves) {
            board.doMove(move);
            nodes += perft(board, depth - 1);
            board.undoMove();
        }

        return nodes;
    }

    public static void main(String[] args) {
        Board board = new Board();
        int depth = 5;
        long start = System.currentTimeMillis();
        long nodes = perft(board, depth);
        System.out.println("Perft(" + depth + ") = " + nodes);
        System.err.println(System.currentTimeMillis()-start);
    }
}
