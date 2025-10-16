package chessV2;

import java.util.*;

public class BitBoard {
    public static final int WHITE = 0, BLACK = 1;
    public static final int PAWN = 0, KNIGHT = 1, BISHOP = 2, ROOK = 3, QUEEN = 4, KING = 5;

    public long[][] pieces = new long[2][6];
    public long[] sideOcc = new long[2];
    public long occ = 0L;
    public int sideToMove = WHITE;

    // Move history for undo
    private final Deque<MoveRecord> history = new ArrayDeque<>();

    private static final long[] KNIGHT_ATTACKS = new long[64];
    private static final long[] KING_ATTACKS = new long[64];
    private static final long[] PAWN_ATTACKS_WHITE = new long[64];
    private static final long[] PAWN_ATTACKS_BLACK = new long[64];

    private static final int[] PIECE_VALUE = {100, 320, 330, 500, 900, 20000};

    static {
        for (int sq = 0; sq < 64; sq++) {
            KNIGHT_ATTACKS[sq] = genKnight(sq);
            KING_ATTACKS[sq] = genKing(sq);
            PAWN_ATTACKS_WHITE[sq] = genPawn(sq, WHITE);
            PAWN_ATTACKS_BLACK[sq] = genPawn(sq, BLACK);
        }
    }

    public BitBoard() {}

    // Load position from 8x8 char array
    public void loadFromArray(char[][] arr, int stm) {
        pieces = new long[2][6];
        sideOcc = new long[2];
        occ = 0;
        sideToMove = stm;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char ch = arr[r][c];
                if (ch == '.') continue;

                int sq = (7 - r) * 8 + c; // rank 0 = bottom
                int color = Character.isUpperCase(ch) ? WHITE : BLACK;
                int piece = -1;

                switch (Character.toLowerCase(ch)) {
                    case 'p': piece = PAWN; break;
                    case 'n': piece = KNIGHT; break;
                    case 'b': piece = BISHOP; break;
                    case 'r': piece = ROOK; break;
                    case 'q': piece = QUEEN; break;
                    case 'k': piece = KING; break;
                }

                pieces[color][piece] |= (1L << sq);
            }
        }
        updateOcc();
    }

    private void updateOcc() {
        sideOcc[WHITE] = sideOcc[BLACK] = 0;
        for (int p = 0; p < 6; p++) {
            sideOcc[WHITE] |= pieces[WHITE][p];
            sideOcc[BLACK] |= pieces[BLACK][p];
        }
        occ = sideOcc[WHITE] | sideOcc[BLACK];
    }

    // Move encoding: from<<12 | to<<6 | promo
    // promo = 0 pawn, 1 knight, 2 bishop, 3 rook, 4 queen
    public static int encodeMove(int from, int to, int promo) {
        return (from << 12) | (to << 6) | promo;
    }
    public static int fromSq(int move) { return (move >> 12) & 63; }
    public static int toSq(int move) { return (move >> 6) & 63; }
    public static int promoPiece(int move) { return move & 7; }

    public List<Integer> genLegalMoves() {
        List<Integer> moves = new ArrayList<>();
        int side = sideToMove;
        int enemy = side ^ 1;

        for (int piece = 0; piece < 6; piece++) {
            long bb = pieces[side][piece];
            while (bb != 0) {
                int from = bitScanForward(bb);
                bb &= bb - 1;

                if (piece == PAWN) {
                    int dir = (side == WHITE) ? 8 : -8;
                    int to = from + dir;
                    if (to >= 0 && to < 64) {
                        long mask = 1L << to;
                        if ((occ & mask) == 0) {
                            if ((to / 8 == 7 && side == WHITE) || (to / 8 == 0 && side == BLACK)) {
                                for (int promo : new int[]{QUEEN, ROOK, BISHOP, KNIGHT})
                                    moves.add(encodeMove(from, to, promo));
                            } else {
                                moves.add(encodeMove(from, to, PAWN));
                                // double push
                                if ((side == WHITE && from / 8 == 1) || (side == BLACK && from / 8 == 6)) {
                                    int to2 = from + 2 * dir;
                                    long mask2 = 1L << to2;
                                    if ((occ & mask2) == 0) {
                                        moves.add(encodeMove(from, to2, PAWN));
                                    }
                                }
                            }
                        }
                    }

                    long attacks = (side == WHITE ? PAWN_ATTACKS_WHITE[from] : PAWN_ATTACKS_BLACK[from]) & sideOcc[enemy];
                    while (attacks != 0) {
                        int toSq = bitScanForward(attacks);
                        attacks &= attacks - 1;
                        if ((toSq / 8 == 7 && side == WHITE) || (toSq / 8 == 0 && side == BLACK)) {
                            for (int promo : new int[]{QUEEN, ROOK, BISHOP, KNIGHT})
                                moves.add(encodeMove(from, toSq, promo));
                        } else {
                            moves.add(encodeMove(from, toSq, PAWN));
                        }
                    }
                } else if (piece == KNIGHT) {
                    long attacks = KNIGHT_ATTACKS[from] & ~sideOcc[side];
                    while (attacks != 0) {
                        int to = bitScanForward(attacks);
                        attacks &= attacks - 1;
                        moves.add(encodeMove(from, to, piece));
                    }
                } else if (piece == KING) {
                    long attacks = KING_ATTACKS[from] & ~sideOcc[side];
                    while (attacks != 0) {
                        int to = bitScanForward(attacks);
                        attacks &= attacks - 1;
                        moves.add(encodeMove(from, to, piece));
                    }
                } else { // sliding pieces
                    int[] dirs;
                    if (piece == BISHOP) dirs = new int[]{9,7,-9,-7};
                    else if (piece == ROOK) dirs = new int[]{8,-8,1,-1};
                    else dirs = new int[]{9,7,-9,-7,8,-8,1,-1};

                    for (int d : dirs) {
                        int sq = from;
                        while (true) {
                            int nsq = sq + d;
                            if (nsq < 0 || nsq >= 64) break;
                            if (Math.abs((nsq % 8) - (sq % 8)) > 2 && (d == 1 || d == -1)) break;

                            long mask = 1L << nsq;
                            if ((sideOcc[side] & mask) != 0) break;
                            moves.add(encodeMove(from, nsq, piece));
                            if ((sideOcc[enemy] & mask) != 0) break;
                            sq = nsq;
                        }
                    }
                }
            }
        }

        // filter illegal (king in check)
        List<Integer> legal = new ArrayList<>();
        for (int m : moves) {
            makeMove(m);
            if (!inCheck(side)) legal.add(m);
            undoMove();
        }
        return legal;
    }

    public void makeMove(int move) {
        int from = fromSq(move);
        int to = toSq(move);
        int promo = promoPiece(move);

        int side = sideToMove;
        int enemy = side ^ 1;
        int movedPiece = getPieceAt(from, side);

        long fromMask = 1L << from;
        long toMask = 1L << to;

        int capturedPiece = getPieceAt(to, enemy);

        // Save record
        history.push(new MoveRecord(move, movedPiece, capturedPiece));

        // Remove from origin
        pieces[side][movedPiece] &= ~fromMask;
        // Remove captured
        if (capturedPiece != -1) pieces[enemy][capturedPiece] &= ~toMask;
        // Place new piece
        if (movedPiece == PAWN && promo != PAWN) {
            pieces[side][promo] |= toMask;
        } else {
            pieces[side][movedPiece] |= toMask;
        }

        updateOcc();
        sideToMove ^= 1;
    }

    public void undoMove() {
        MoveRecord rec = history.pop();
        int move = rec.move;
        int from = fromSq(move);
        int to = toSq(move);
        int promo = promoPiece(move);

        sideToMove ^= 1;
        int side = sideToMove;
        int enemy = side ^ 1;

        long fromMask = 1L << from;
        long toMask = 1L << to;

        // Remove piece from to
        for (int p = 0; p < 6; p++) pieces[side][p] &= ~toMask;

        // Restore moved piece
        if (rec.movedPiece == PAWN && promo != PAWN)
            pieces[side][PAWN] |= fromMask;
        else
            pieces[side][rec.movedPiece] |= fromMask;

        // Restore captured
        if (rec.capturedPiece != -1) pieces[enemy][rec.capturedPiece] |= toMask;

        updateOcc();
    }

    private int getPieceAt(int sq, int color) {
        long mask = 1L << sq;
        for (int p = 0; p < 6; p++) {
            if ((pieces[color][p] & mask) != 0) return p;
        }
        return -1;
    }

    public boolean inCheck(int color) {
        int kingSq = bitScanForward(pieces[color][KING]);
        return isAttacked(kingSq, color ^ 1);
    }

    private boolean isAttacked(int sq, int by) {
        if (((by==WHITE ? PAWN_ATTACKS_BLACK[sq] : PAWN_ATTACKS_WHITE[sq]) & pieces[by][PAWN]) != 0)
            return true;
        if ((KNIGHT_ATTACKS[sq] & pieces[by][KNIGHT]) != 0) return true;
        if ((KING_ATTACKS[sq] & pieces[by][KING]) != 0) return true;

        for (int d : new int[]{9,7,-9,-7}) {
            int nsq = sq + d;
            while (nsq >= 0 && nsq < 64) {
                long bb = 1L << nsq;
                if ((occ & bb) != 0) {
                    if (((pieces[by][BISHOP] | pieces[by][QUEEN]) & bb) != 0) return true;
                    break;
                }
                nsq += d;
            }
        }
        for (int d : new int[]{8,-8,1,-1}) {
            int nsq = sq + d;
            while (nsq >= 0 && nsq < 64) {
                long bb = 1L << nsq;
                if ((occ & bb) != 0) {
                    if (((pieces[by][ROOK] | pieces[by][QUEEN]) & bb) != 0) return true;
                    break;
                }
                nsq += d;
            }
        }
        return false;
    }

    public int evaluate() {
        int score = 0;
        for (int c = 0; c < 2; c++) {
            int sgn = (c == WHITE ? 1 : -1);
            for (int p = 0; p < 6; p++) {
                score += sgn * PIECE_VALUE[p] * Long.bitCount(pieces[c][p]);
            }
        }
        return (sideToMove == WHITE) ? score : -score;
    }

    public static int bitScanForward(long bb) {
        return bb==0 ? -1 : Long.numberOfTrailingZeros(bb);
    }

    private static long genKnight(int sq) {
        int r = sq / 8, c = sq % 8;
        int[] dr = {-2,-2,-1,-1,1,1,2,2};
        int[] dc = {-1,1,-2,2,-2,2,-1,1};
        long bb = 0;
        for (int i = 0; i < 8; i++) {
            int rr = r + dr[i], cc = c + dc[i];
            if (rr>=0 && rr<8 && cc>=0 && cc<8)
                bb |= 1L << (rr*8+cc);
        }
        return bb;
    }

    private static long genKing(int sq) {
        int r = sq / 8, c = sq % 8;
        long bb = 0;
        for (int dr=-1; dr<=1; dr++)
            for (int dc=-1; dc<=1; dc++) {
                if (dr==0 && dc==0) continue;
                int rr = r+dr, cc = c+dc;
                if (rr>=0 && rr<8 && cc>=0 && cc<8)
                    bb |= 1L << (rr*8+cc);
            }
        return bb;
    }

    private static long genPawn(int sq, int side) {
        int r = sq/8, c = sq%8;
        long bb=0;
        int dir = (side==WHITE)?1:-1;
        if (r+dir>=0 && r+dir<8) {
            if (c-1>=0) bb |= 1L << ((r+dir)*8+(c-1));
            if (c+1<8) bb |= 1L << ((r+dir)*8+(c+1));
        }
        return bb;
    }

    public static String moveToString(int move) {
        int from = fromSq(move);
        int to = toSq(move);
        return ""+(char)('a'+from%8)+(char)('1'+from/8)+
                 (char)('a'+to%8)+(char)('1'+to/8);
    }

    private static class MoveRecord {
        int move;
        int movedPiece, capturedPiece;
        MoveRecord(int m, int mp, int cp) {
            move=m; movedPiece=mp; capturedPiece=cp;
        }
    }
}
