package src.chess;

import java.util.*;

public class BitBoard {

    // Piece bitboards
    private long WP, WN, WB, WR, WQ, WK;
    private long BP, BN, BB, BR, BQ, BK;

    // Stack for undo
    private Stack<Move> history = new Stack<>();

    public BitBoard() {
        setStartingPosition();
    }

    private void setStartingPosition() {
        // clear everything
        WP = WN = WB = WR = WQ = WK = 0L;
        BP = BN = BB = BR = BQ = BK = 0L;

        char[][] board = new char[8][8];
        board[0] = new char[]{'r', 'n', 'b', '.', 'k', 'b', '.', 'r'};
        board[1] = new char[]{'.', 'p', '.', 'p', '.', 'p', '.', '.'};
        board[2] = new char[]{'p', '.', '.', '.', '.', '.', '.', 'n'};
        board[3] = new char[]{'.', '.', '.', 'q', 'p', '.', 'Q', 'p'};
        board[4] = new char[]{'P', '.', '.', '.', '.', '.', 'p', '.'};
        board[5] = new char[]{'.', '.', 'P', '.', '.', '.', '.', '.'};
        board[6] = new char[]{'.', '.', '.', '.', 'P', 'P', 'P', 'P'};
        board[7] = new char[]{'R', 'N', 'B', '.', 'K', 'B', 'N', 'P'};

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char piece = board[r][c];
                if (piece != '.') {
                    int sq = toSquare(r, c); // (0,0)=a8 ... (7,7)=h1
                    putPieceAt(piece, sq);
                }
            }
        }
    }

    // ---------- Utility ----------

    private long allWhite() {
        return WP | WN | WB | WR | WQ | WK;
    }

    private long allBlack() {
        return BP | BN | BB | BR | BQ | BK;
    }

    private long allPieces() {
        return allWhite() | allBlack();
    }

    private long squareMask(int sq) {
        return 1L << sq;
    }

    private int toSquare(int row, int col) {
        return row * 8 + col;
    }

    private int rowOf(int sq) {
        return sq / 8;
    }

    private int colOf(int sq) {
        return sq % 8;
    }

    private List<Integer> toSquares(long bb) {
        List<Integer> res = new ArrayList<>();
        while (bb != 0) {
            int sq = Long.numberOfTrailingZeros(bb);
            res.add(sq);
            bb &= bb - 1;
        }
        return res;
    }

    // ---------- Piece lookup ----------

    public char getPieceAt(int sq) {
        long mask = squareMask(sq);

        if ((WP & mask) != 0) return 'P';
        if ((WN & mask) != 0) return 'N';
        if ((WB & mask) != 0) return 'B';
        if ((WR & mask) != 0) return 'R';
        if ((WQ & mask) != 0) return 'Q';
        if ((WK & mask) != 0) return 'K';

        if ((BP & mask) != 0) return 'p';
        if ((BN & mask) != 0) return 'n';
        if ((BB & mask) != 0) return 'b';
        if ((BR & mask) != 0) return 'r';
        if ((BQ & mask) != 0) return 'q';
        if ((BK & mask) != 0) return 'k';

        return '.';
    }

    // ---------- Printing ----------

    public void printBoard() {
        for (int r = 0; r < 8; r++) {
            System.out.print((8 - r) + " ");
            for (int c = 0; c < 8; c++) {
                int sq = toSquare(r, c);
                System.out.print(getPieceAt(sq) + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // ---------- Evaluation ----------

    public int evaluate() {
        int score = 0;
        long[] white = {WP, WN, WB, WR, WQ, WK};
        long[] black = {BP, BN, BB, BR, BQ, BK};
        int[] values = {100, 320, 330, 500, 900, 20000};

        for (int i = 0; i < 6; i++) {
            score += Long.bitCount(white[i]) * values[i];
            score -= Long.bitCount(black[i]) * values[i];
        }
        return score;
    }

    // ---------- Move Generation ----------

    public List<Move> getLegalMoves(int turn) {
        List<Move> moves = new ArrayList<>();
        if (turn == 1) {
            generatePawnMoves(WP, true, moves);
            generateKnightMoves(WN, true, moves);
            generateBishopMoves(WB, true, moves);
            generateRookMoves(WR, true, moves);
            generateQueenMoves(WQ, true, moves);
            generateKingMoves(WK, true, moves);
        } else {
            generatePawnMoves(BP, false, moves);
            generateKnightMoves(BN, false, moves);
            generateBishopMoves(BB, false, moves);
            generateRookMoves(BR, false, moves);
            generateQueenMoves(BQ, false, moves);
            generateKingMoves(BK, false, moves);
        }
        return moves;
    }

    private void generatePawnMoves(long pawns, boolean white, List<Move> moves) {
        for (int sq : toSquares(pawns)) {
            int row = rowOf(sq), col = colOf(sq);

            int dir = white ? -1 : 1;
            int startRow = white ? 6 : 1;

            int fwdRow = row + dir;
            if (fwdRow >= 0 && fwdRow < 8) {
                int fwdSq = toSquare(fwdRow, col);
                if (getPieceAt(fwdSq) == '.') {
                    moves.add(new Move(row, col, fwdRow, col, getPieceAt(sq), '.'));

                    // Double push
                    if (row == startRow) {
                        int dblRow = row + 2 * dir;
                        int dblSq = toSquare(dblRow, col);
                        if (getPieceAt(dblSq) == '.') {
                            moves.add(new Move(row, col, dblRow, col, getPieceAt(sq), '.'));
                        }
                    }
                }
            }

            // Captures
            for (int dc = -1; dc <= 1; dc += 2) {
                int cc = col + dc;
                if (cc >= 0 && cc < 8 && fwdRow >= 0 && fwdRow < 8) {
                    int capSq = toSquare(fwdRow, cc);
                    char target = getPieceAt(capSq);
                    if (target != '.' && (white ? Character.isLowerCase(target) : Character.isUpperCase(target))) {
                        moves.add(new Move(row, col, fwdRow, cc, getPieceAt(sq), target));
                    }
                }
            }
        }
    }

    private void generateKnightMoves(long knights, boolean white, List<Move> moves) {
        int[] dr = {-2,-2,-1,-1,1,1,2,2};
        int[] dc = {-1,1,-2,2,-2,2,-1,1};
        for (int sq : toSquares(knights)) {
            int r = rowOf(sq), c = colOf(sq);
            for (int k = 0; k < 8; k++) {
                int rr = r + dr[k], cc = c + dc[k];
                if (rr >= 0 && rr < 8 && cc >= 0 && cc < 8) {
                    int to = toSquare(rr, cc);
                    char target = getPieceAt(to);
                    if (target == '.' || (white ? Character.isLowerCase(target) : Character.isUpperCase(target))) {
                        moves.add(new Move(r, c, rr, cc, getPieceAt(sq), target));
                    }
                }
            }
        }
    }

    private void generateSlidingMoves(long pieces, boolean white, int[][] dirs, List<Move> moves) {
        for (int sq : toSquares(pieces)) {
            int r = rowOf(sq), c = colOf(sq);
            for (int[] d : dirs) {
                int rr = r + d[0], cc = c + d[1];
                while (rr >= 0 && rr < 8 && cc >= 0 && cc < 8) {
                    int to = toSquare(rr, cc);
                    char target = getPieceAt(to);
                    if (target == '.') {
                        moves.add(new Move(r, c, rr, cc, getPieceAt(sq), '.'));
                    } else {
                        if (white ? Character.isLowerCase(target) : Character.isUpperCase(target)) {
                            moves.add(new Move(r, c, rr, cc, getPieceAt(sq), target));
                        }
                        break;
                    }
                    rr += d[0];
                    cc += d[1];
                }
            }
        }
    }

    private void generateBishopMoves(long bishops, boolean white, List<Move> moves) {
        int[][] dirs = {{-1,-1},{-1,1},{1,-1},{1,1}};
        generateSlidingMoves(bishops, white, dirs, moves);
    }

    private void generateRookMoves(long rooks, boolean white, List<Move> moves) {
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        generateSlidingMoves(rooks, white, dirs, moves);
    }

    private void generateQueenMoves(long queens, boolean white, List<Move> moves) {
        int[][] dirs = {{-1,-1},{-1,1},{1,-1},{1,1},{-1,0},{1,0},{0,-1},{0,1}};
        generateSlidingMoves(queens, white, dirs, moves);
    }

    private void generateKingMoves(long king, boolean white, List<Move> moves) {
        for (int sq : toSquares(king)) {
            int r = rowOf(sq), c = colOf(sq);
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    int rr = r + dr, cc = c + dc;
                    if (rr >= 0 && rr < 8 && cc >= 0 && cc < 8) {
                        int to = toSquare(rr, cc);
                        char target = getPieceAt(to);
                        if (target == '.' || (white ? Character.isLowerCase(target) : Character.isUpperCase(target))) {
                            moves.add(new Move(r, c, rr, cc, getPieceAt(sq), target));
                        }
                    }
                }
            }
        }
    }

    // ---------- Apply / Undo ----------

    public void applyMove(Move m) {
        int from = toSquare(m.fromRow, m.fromCol);
        int to = toSquare(m.toRow, m.toCol);
        long fromMask = squareMask(from);
        long toMask = squareMask(to);

        // Remove captured piece
        if (m.captured != '.') {
            removePieceAt(to);
        }

        // Move piece
        removePieceAt(from);
        putPieceAt(m.piece, to);

        history.push(m);
    }

    public void undoMove(Move m) {
        int from = toSquare(m.fromRow, m.fromCol);
        int to = toSquare(m.toRow, m.toCol);

        removePieceAt(to);
        putPieceAt(m.piece, from);
        if (m.captured != '.') {
            putPieceAt(m.captured, to);
        }

        history.pop();
    }

    private void removePieceAt(int sq) {
        long mask = ~squareMask(sq);
        WP &= mask; WN &= mask; WB &= mask; WR &= mask; WQ &= mask; WK &= mask;
        BP &= mask; BN &= mask; BB &= mask; BR &= mask; BQ &= mask; BK &= mask;
    }

    private void putPieceAt(char piece, int sq) {
        long mask = squareMask(sq);
        switch (piece) {
            case 'P': WP |= mask; break;
            case 'N': WN |= mask; break;
            case 'B': WB |= mask; break;
            case 'R': WR |= mask; break;
            case 'Q': WQ |= mask; break;
            case 'K': WK |= mask; break;
            case 'p': BP |= mask; break;
            case 'n': BN |= mask; break;
            case 'b': BB |= mask; break;
            case 'r': BR |= mask; break;
            case 'q': BQ |= mask; break;
            case 'k': BK |= mask; break;
        }
    }
}
