package com.chess;

import com.github.bhlangonijr.chesslib.Board;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Board board = new Board();
        System.err.println(board.legalMoves().size());
    }
}
