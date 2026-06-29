package src.chessV2;

public class Board {
    static long bitboards[] = new long[11];
    /*
    0 - white pawn
    1 - white knight
    2 - white bishop
    3 - white rook
    4 - white queen
    5 - white king
    6 - black pawn
    7 - black knight
    8 - black bishop
    9 - black rook
    10 - black queen
    11 - black king
     */
    long occupancy[] = new long[3];
    public static void printBB(long bb) {
        for(int k = 0; k < Long.numberOfLeadingZeros(bb); k++){
            System.err.print(0);
        }
        System.err.print(Long.toBinaryString(bb));
    }
    public static void loadFEN(String FEN) {

    }
    public static void main(String[] args) {
        
        
    }
}