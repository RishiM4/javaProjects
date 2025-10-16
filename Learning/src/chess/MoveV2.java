package chess;

public class MoveV2 {
    public static int encode(int from, int to, int capture, boolean promotion, boolean enpassant, boolean whiteKingsideCastlingRights, boolean whiteQueensideCastlingRights, boolean blackKingsideCastlingRights, boolean blackQueensideCastlingRights, boolean castling) {
        int move = 0;
        move |= from;
        move |= to << 6;
        move |= capture << 12;
        if (promotion) {
            move |= 1 << 17;
        }
        if (enpassant) {
            move |= 1 << 18;
        }
        if (whiteKingsideCastlingRights) {
            move |= 1 << 19;
        }
        if (whiteQueensideCastlingRights) {
            move |= 1 << 20;
        }
        if (blackKingsideCastlingRights) {
            move |= 1 << 21;
        }
        if (blackQueensideCastlingRights) {
            move |= 1 << 22;
        }
        if (castling) {
            move |= 1 << 23;
        }
        return move;
    }
    public static int from(int move) {
        return move & 0b111111; 

    }
    public static int to(int move) {
        return move >> 6 & 0b111111;
    }
    public static int capture(int move) {
        return move >> 12 & 0b11111;
    }
    public static boolean promotion(int move) {
        return (move >> 17 & 0b1) == 1;
    }
    public static boolean enpassant(int move) {
        return (move >> 18 & 0b1) == 1;
    }
    public static boolean whiteKingsideCastlingRights(int move) {
        return (move >> 19 & 0b1) == 1;
    }
    public static boolean whiteQueensideCastlingRights(int move) {
        return (move >> 20 & 0b1) == 1;
    }
    public static boolean blackKingsideCastlingRights(int move) {
        return (move >> 21 & 0b1) == 1;
    }
    public static boolean blackQueensideCastlingRights(int move) {
        return (move >> 22 & 0b1) == 1;
    }
    public static boolean caslting(int move) {
        return (move >> 23) == 1;
    }
    public static void main(String[] args) {
    }
}

