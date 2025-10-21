package chess;

import java.util.*;

public final class BoardV3 {
    public static final int WHITE = 0;
    public static final int BLACK = 1;

    public static final int PAWN = 0;
    public static final int KNIGHT = 1;
    public static final int BISHOP = 2;
    public static final int ROOK = 3;
    public static final int QUEEN = 4;
    public static final int KING = 5;

    public long[] pieceBB = new long[12];
    private long[] occupancy = new long[3];
    public int sideToMove = WHITE;
    private int castlingRights = 0; 
    private int enPassantSquare = -1; 
    private int halfmoveClock = 0;
    private int currentPhase = 0;
    private static final int[] RANK = new int[64];
    private static final int[] FILE = new int[64];
    private final long[] SQUARE_BB = new long[64];
    
    private final long FILE_A = 0x0101010101010101L;
    private final long FILE_H = 0x8080808080808080L;

    private final long[] KNIGHT_ATTACKS = new long[64];
    private final long[] KING_ATTACKS = new long[64];
    private final long[] WHITE_PAWN_ATTACKS = new long[64];
    private final long[] BLACK_PAWN_ATTACKS = new long[64];

    private final long[] ROOK_MAGIC = new long[64];
    private final long[] BISHOP_MAGIC = new long[64];
    private final int[] ROOK_RELEVANT_BITS = new int[64];
    private final int[] BISHOP_RELEVANT_BITS = new int[64];

    private final long[][] ROOK_ATTACKS = new long[64][];
    private final long[][] BISHOP_ATTACKS = new long[64][];

    private final long[] ROOK_OCC_MASK = new long[64];
    private final long[] BISHOP_OCC_MASK = new long[64];

    private static final int[] PHASE_WEIGHTS = {
        0,  // Pawn
        1,  // Knight
        1,  // Bishop
        2,  // Rook
        4,  // Queen
        0   // King
    };

    private static final int[][] WHITE_PST = new int[12][64];
    private static final int[][] BLACK_PST = new int[12][64];
    
    public BoardV3() {
        for (int sq = 0; sq < 64; sq++) {
            RANK[sq] = sq >> 3;
            FILE[sq] = sq & 7;
            SQUARE_BB[sq] = 1L << sq;
        }
        setFEN("startpos");
        initKnightKingPawnAttacks();
        initMagicMasksAndNumbers();
        initSlidingAttackTables();
        initPST();
    }
    
    private void initKnightKingPawnAttacks() {
        for (int sq = 0; sq < 64; sq++) {
            long na = 0L, ka = 0L, wpa = 0L, bpa = 0L;
            int r = RANK[sq], f = FILE[sq];
            int[] KN = {1, 2, -1, -2};
            for (int dr : KN) for (int df : KN) if (Math.abs(dr) != Math.abs(df)) {
                int rr = r + dr, ff = f + df;
                if (rr >= 0 && rr < 8 && ff >= 0 && ff < 8) na |= 1L << (rr*8 + ff);
            }
            for (int dr = -1; dr <= 1; dr++) for (int df = -1; df <= 1; df++){
                if (dr == 0 && df == 0) continue;
                int rr = r + dr, ff = f + df;
                if (rr >= 0 && rr < 8 && ff >= 0 && ff < 8) ka |= 1L << (rr*8 + ff);
            }
            if (r < 7) {
                if (f > 0) wpa |= 1L << ((r+1)*8 + (f-1));
                if (f < 7) wpa |= 1L << ((r+1)*8 + (f+1));
            }
            if (r > 0) {
                if (f > 0) bpa |= 1L << ((r-1)*8 + (f-1));
                if (f < 7) bpa |= 1L << ((r-1)*8 + (f+1));
            }
            KNIGHT_ATTACKS[sq] = na;
            KING_ATTACKS[sq] = ka;
            WHITE_PAWN_ATTACKS[sq] = wpa; // attacks from a white pawn on sq
            BLACK_PAWN_ATTACKS[sq] = bpa; // attacks from a black pawn on sq
        }
    }
    private void initPST() {
        // Pawn MG
        WHITE_PST[0] = new int[] {
            0, 0, 0, 0, 0, 0, 0, 0,
            5,10,10,-20,-20,10,10,5,
            5,-5,-10,0,0,-10,-5,5,
            0,0,0,20,20,0,0,0,
            5,5,10,25,25,10,5,5,
            10,10,20,30,30,20,10,10,
            50,50,50,50,50,50,50,50,
            0,0,0,0,0,0,0,0
        };
        // Pawn EG
        WHITE_PST[1] = WHITE_PST[0];

        // Knight MG
        WHITE_PST[2] = new int[] {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,0,0,0,0,-20,-40,
            -30,0,10,15,15,10,0,-30,
            -30,5,15,20,20,15,5,-30,
            -30,0,15,20,20,15,0,-30,
            -30,5,10,15,15,10,5,-30,
            -40,-20,0,5,5,0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        };
        // Knight EG
        WHITE_PST[3] = new int[] {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,0,5,5,0,-20,-40,
            -30,0,10,15,15,10,0,-30,
            -30,5,15,20,20,15,5,-30,
            -30,0,15,20,20,15,0,-30,
            -30,5,10,15,15,10,5,-30,
            -40,-20,0,5,5,0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
        };

        // Bishop MG
        WHITE_PST[4] = new int[] {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,0,0,0,0,0,0,-10,
            -10,0,5,10,10,5,0,-10,
            -10,5,5,10,10,5,5,-10,
            -10,0,10,10,10,10,0,-10,
            -10,10,10,10,10,10,10,-10,
            -10,5,0,0,0,0,5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        };
        // Bishop EG
        WHITE_PST[5] = new int[] {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,0,5,10,10,5,0,-10,
            -10,5,10,15,15,10,5,-10,
            -10,5,10,15,15,10,5,-10,
            -10,0,10,10,10,10,0,-10,
            -10,5,10,10,10,10,5,-10,
            -10,0,0,0,0,0,0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
        };

        // Rook MG
        WHITE_PST[6] = new int[] {
            0,0,0,5,5,0,0,0,
        -5,0,0,0,0,0,0,-5,
        -5,0,0,0,0,0,0,-5,
        -5,0,0,0,0,0,0,-5,
        -5,0,0,0,0,0,0,-5,
        -5,0,0,0,0,0,0,-5,
            5,10,10,10,10,10,10,5,
            0,0,0,0,0,0,0,0
        };
        // Rook EG
        WHITE_PST[7] = WHITE_PST[6];

        // Queen MG
        WHITE_PST[8] = new int[] {
            -20,-10,-10,-5,-5,-10,-10,-20,
            -10,0,0,0,0,0,0,-10,
            -10,0,5,5,5,5,0,-10,
            -5,0,5,5,5,5,0,-5,
            0,0,5,5,5,5,0,-5,
            -10,5,5,5,5,5,0,-10,
            -10,0,5,0,0,0,0,-10,
            -20,-10,-10,-5,-5,-10,-10,-20
        };
        // Queen EG
        WHITE_PST[9] = WHITE_PST[8];

        // King MG
        WHITE_PST[10] = new int[] {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20,20,0,0,0,0,20,20,
            20,30,10,0,0,10,30,20
        };
        // King EG
        WHITE_PST[11] = new int[] {
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,0,0,-10,-20,-30,
            -30,-10,20,30,30,20,-10,-30,
            -30,-10,30,40,40,30,-10,-30,
            -30,-10,30,40,40,30,-10,-30,
            -30,-10,20,30,30,20,-10,-30,
            -30,-30,0,0,0,0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50
        };

        for (int piece = 0; piece < 12; piece++) {
            BLACK_PST[piece] = new int[64];
            for (int rank = 0; rank < 8; rank++) {
                for (int file = 0; file < 8; file++) {
                    BLACK_PST[piece][rank*8 + file] = WHITE_PST[piece][(7-rank)*8 + file];
                }
            }
        }
    }

    private void initMagicMasksAndNumbers() {
        int[] rookBits = {
            12,11,11,11,11,11,11,12,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            11,10,10,10,10,10,10,11,
            12,11,11,11,11,11,11,12
        };
        int[] bishopBits = {
            6,5,5,5,5,5,5,6,
            5,5,5,5,5,5,5,5,
            5,5,7,7,7,7,5,5,
            5,5,7,9,9,7,5,5,
            5,5,7,9,9,7,5,5,
            5,5,7,7,7,7,5,5,
            5,5,5,5,5,5,5,5,
            6,5,5,5,5,5,5,6
        };
        System.arraycopy(rookBits, 0, ROOK_RELEVANT_BITS, 0, 64);
        System.arraycopy(bishopBits, 0, BISHOP_RELEVANT_BITS, 0, 64);

        long[] rookMagics = new long[] {
            0x8a80104000800020L,0x140002000100040L,0x2801880a0017001L,0x100081001000420L,
            0x200020010080420L,0x3001c0002010008L,0x8480008002000100L,0x2080088004402900L,
            0x800098204000L,0x2024401000200040L,0x100802000801000L,0x120800800801000L,
            0x208808088000400L,0x2802200800400L,0x2200800100020080L,0x801000060821100L,
            0x80044006422000L,0x100808020004000L,0x12108a0010204200L,0x140848010000802L,
            0x481828014002800L,0x8094004002004100L,0x4010040010010802L,0x20008806104L,
            0x100400080208000L,0x2040002120081000L,0x21200680100081L,0x20100080080080L,
            0x2000a00200410L,0x20080800400L,0x80088400100102L,0x80004600042881L,
            0x4040008040800020L,0x440003000200801L,0x4200011004500L,0x188020010100100L,
            0x14800401802800L,0x2080040080800200L,0x124080204001001L,0x200046502000484L,
            0x480400080088020L,0x1000422010034000L,0x30200100110040L,0x100021010009L,
            0x2002080100110004L,0x202008004008002L,0x20020004010100L,0x2048440040820001L,
            0x101002200408200L,0x40802000401080L,0x4008142004410100L,0x2060820c0120200L,
            0x1001004080100L,0x20c020080040080L,0x2935610830022400L,0x44440041009200L,
            0x280001040802101L,0x2100190040002085L,0x80c0084100102001L,0x4024081001000421L,
            0x20030a0244872L,0x12001008414402L,0x2006104900a0804L,0x1004081002402L
        };

        long[] bishopMagics = new long[] {
            0x40040844404084L,0x2004208a004208L,0x10190041080202L,0x108060845042010L,
            0x581104180800210L,0x2112080446200010L,0x1080820820060210L,0x3c0808410220200L,
            0x4050404440404L,0x21001420088L,0x24d0080801082102L,0x1020a0a020400L,
            0x40308200402L,0x4011002100800L,0x401484104104005L,0x801010402020200L,
            0x400210c3880100L,0x404022024108200L,0x810018200204102L,0x4002801a02003L,
            0x85040820080400L,0x810102c808880400L,0xe900410884800L,0x8002020480840102L,
            0x220200865090201L,0x2010100a02021202L,0x152048408022401L,0x20080002081110L,
            0x4001001021004000L,0x800040400a011002L,0xe4004081011002L,0x1c004001012080L,
            0x8004200962a00220L,0x8422100208500202L,0x2000402200300c08L,0x8646020080080080L,
            0x80020a0200100808L,0x2010004880111000L,0x623000a080011400L,0x42008c0340209202L,
            0x209188240001000L,0x400408a884001800L,0x110400a6080400L,0x1840060a44020800L,
            0x90080104000041L,0x201011000808101L,0x1a2208080504f080L,0x8012020600211212L,
            0x500861011240000L,0x180806108200800L,0x4000020e01040044L,0x300000261044000aL,
            0x802241102020002L,0x20906061210001L,0x5a84841004010310L,0x4010801011c04L,
            0xa010109502200L,0x4a02012000L,0x500201010098b028L,0x8040002811040900L,
            0x28000010020204L,0x6000020202d0240L,0x8918844842082200L,0x4010011029020020L
        };

        System.arraycopy(rookMagics, 0, ROOK_MAGIC, 0, 64);
        System.arraycopy(bishopMagics, 0, BISHOP_MAGIC, 0, 64);

        for (int sq = 0; sq < 64; sq++) {
            ROOK_OCC_MASK[sq] = rookOccupancyMask(sq);
            BISHOP_OCC_MASK[sq] = bishopOccupancyMask(sq);
        }
    }
    private long bishopOccupancyMask(int sq) {
        long mask = 0L;
        int r = RANK[sq], f = FILE[sq];
        int rr, ff;
        rr = r + 1; ff = f + 1;
        while (rr < 7 && ff < 7) { mask |= 1L << (rr*8 + ff); rr++; ff++; }
        rr = r + 1; ff = f - 1;
        while (rr < 7 && ff > 0) { mask |= 1L << (rr*8 + ff); rr++; ff--; }
        rr = r - 1; ff = f + 1;
        while (rr > 0 && ff < 7) { mask |= 1L << (rr*8 + ff); rr--; ff++; }
        rr = r - 1; ff = f - 1;
        while (rr > 0 && ff > 0) { mask |= 1L << (rr*8 + ff); rr--; ff--; }
        return mask;
    }
    private long rookOccupancyMask(int sq) {
        long mask = 0L;
        int r = RANK[sq], f = FILE[sq];
        int rr, ff;
        rr = r + 1; ff = f;
        while (rr < 7) { mask |= 1L << (rr*8 + ff); rr++; }
        rr = r - 1; ff = f;
        while (rr > 0) { mask |= 1L << (rr*8 + ff); rr--; }
        rr = r; ff = f + 1;
        while (ff < 7) { mask |= 1L << (rr*8 + ff); ff++; }
        rr = r; ff = f - 1;
        while (ff > 0) { mask |= 1L << (rr*8 + ff); ff--; }
        return mask;
    }
    private long slidingAttacksOnTheFly(int sq, long block, boolean isRook) {
        long attacks = 0L;
        int r = RANK[sq], f = FILE[sq];
        int rr, ff;
        if (isRook) {
            rr = r + 1; ff = f;
            while (rr < 8) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr++; }
            rr = r - 1; ff = f;
            while (rr >= 0) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr--; }
            rr = r; ff = f + 1;
            while (ff < 8) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; ff++; }
            rr = r; ff = f - 1;
            while (ff >= 0) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; ff--; }
        } else {
            rr = r + 1; ff = f + 1;
            while (rr < 8 && ff < 8) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr++; ff++; }
            rr = r + 1; ff = f - 1;
            while (rr < 8 && ff >= 0) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr++; ff--; }
            rr = r - 1; ff = f + 1;
            while (rr >= 0 && ff < 8) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr--; ff++; }
            rr = r - 1; ff = f - 1;
            while (rr >= 0 && ff >= 0) { int s = rr*8 + ff; attacks |= 1L<<s; if ((block & (1L<<s))!=0) break; rr--; ff--; }
        }
        return attacks;
    }
    private void initSlidingAttackTables() {
        for (int sq = 0; sq < 64; sq++) {
            int rBits = ROOK_RELEVANT_BITS[sq];
            int bBits = BISHOP_RELEVANT_BITS[sq];
            int rSize = 1 << rBits;
            int bSize = 1 << bBits;
            ROOK_ATTACKS[sq] = new long[rSize];
            BISHOP_ATTACKS[sq] = new long[bSize];
            long occMask = ROOK_OCC_MASK[sq];
            for (int i = 0; i < rSize; i++) {
                long occ = indexToOccupancy(i, rBits, occMask);
                long key = (occ * ROOK_MAGIC[sq]) >>> (64 - rBits);
                ROOK_ATTACKS[sq][(int)key] = slidingAttacksOnTheFly(sq, occ, true);
            }
            occMask = BISHOP_OCC_MASK[sq];
            for (int i = 0; i < bSize; i++) {
                long occ = indexToOccupancy(i, bBits, occMask);
                long key = (occ * BISHOP_MAGIC[sq]) >>> (64 - bBits);
                BISHOP_ATTACKS[sq][(int)key] = slidingAttacksOnTheFly(sq, occ, false);
            }
        }
    }
    private long indexToOccupancy(int index, int bits, long mask) {
        long occ = 0L;
        long bb = mask;
        for (int i = 0; i < bits; i++) {
            int bit = Long.numberOfTrailingZeros(bb);
            bb &= bb - 1;
            if ((index & (1<<i)) != 0) occ |= 1L << bit;
        }
        return occ;
    }
    private long rookAttacks(int sq, long occ) {
        occ &= ROOK_OCC_MASK[sq];
        int key = (int)((occ * ROOK_MAGIC[sq]) >>> (64 - ROOK_RELEVANT_BITS[sq]));
        return ROOK_ATTACKS[sq][key];
    }
    private long bishopAttacks(int sq, long occ) {
        occ &= BISHOP_OCC_MASK[sq];
        int key = (int)((occ * BISHOP_MAGIC[sq]) >>> (64 - BISHOP_RELEVANT_BITS[sq]));
        return BISHOP_ATTACKS[sq][key];
    }
    private long queenAttacks(int sq, long occ) {
        return rookAttacks(sq, occ) | bishopAttacks(sq, occ);
    }
    
    public void setFEN(String fen) {
        clear();
        if (fen.equals("startpos")) fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/");
        int rank = 7;
        for (String row : rows) {
            int file = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) file += c - '0';
                else {
                    int sq = rank*8 + file;
                    placePieceFromChar(c, sq);
                    file++;
                }
            }
            rank--;
        }
        sideToMove = parts[1].equals("w") ? WHITE : BLACK;
        castlingRights = 0;
        if (parts.length > 2) {
            String cr = parts[2];
            if (cr.contains("K")) castlingRights |= 1;
            if (cr.contains("Q")) castlingRights |= 2;
            if (cr.contains("k")) castlingRights |= 4;
            if (cr.contains("q")) castlingRights |= 8;
        }
        enPassantSquare = -1;
        if (parts.length > 3 && !parts[3].equals("-")) {
            enPassantSquare = algebraicToSquare(parts[3]);
        }
        if (parts.length > 4) halfmoveClock = Integer.parseInt(parts[4]);
        recomputeOccupancy();
    }
    private void clear() {
        Arrays.fill(pieceBB, 0L);
        Arrays.fill(occupancy, 0L);
        sideToMove = WHITE;
        castlingRights = 0;
        enPassantSquare = -1;
        halfmoveClock = 0;
    }
    private void placePieceFromChar(char c, int sq) {
        int color = Character.isUpperCase(c) ? WHITE : BLACK;
        char lc = Character.toLowerCase(c);
        int type;
        switch (lc) {
            case 'p': type = PAWN; break;
            case 'n': type = KNIGHT; break;
            case 'b': type = BISHOP; break;
            case 'r': type = ROOK; break;
            case 'q': type = QUEEN; break;
            case 'k': type = KING; break;
            default: return;
        }
        pieceBB[color*6 + type] |= 1L << sq;
    }
    private void recomputeOccupancy() {
        long w = 0L, b = 0L;
        for (int p = 0; p < 6; p++) {
            w |= pieceBB[WHITE*6 + p];
            b |= pieceBB[BLACK*6 + p];
        }
        occupancy[WHITE] = w;
        occupancy[BLACK] = b;
        occupancy[2] = w | b;
    }
    private int algebraicToSquare(String sq) {
        int f = sq.charAt(0) - 'a';
        int r = sq.charAt(1) - '1';
        return r*8 + f;
    }
    private static String squareToAlgebraic(int sq) {
        int f = FILE[sq]; int r = RANK[sq];
        return "" + (char)('a' + f) + (char)('1' + r);
    }
    public static final class Move {
        public final int from, to;
        public final int piece;
        public final int promotion;
        public final boolean isCapture;
        public final boolean isEnPassant;
        public final boolean isCastle;

        public Move(int from, int to, int piece, int promotion, boolean isCapture, boolean isEnPassant, boolean isCastle) {
            this.from = from; this.to = to; this.piece = piece; this.promotion = promotion; this.isCapture = isCapture; this.isEnPassant = isEnPassant; this.isCastle = isCastle;
        }
        public String toString() {
            String prom = promotion >= 0 ? "=" + pieceChar(promotion) : "";
            return squareToAlgebraic(from) + (isCapture ? "x" : "-") + squareToAlgebraic(to) + prom + (isCastle?" c":"");
        }
    }
    private static char pieceChar(int p) {
        switch (p) { case PAWN: return 'P'; case KNIGHT: return 'N'; case BISHOP: return 'B'; case ROOK: return 'R'; case QUEEN: return 'Q'; case KING: return 'K'; }
        return '?';
    }
    public List<Move> generateLegalMoves() {
        List<Move> moves = new ArrayList<>();
        generatePseudoLegalMoves(moves);
        Iterator<Move> it = moves.iterator();
        while (it.hasNext()) {
            Move m = it.next();
            int mover = sideToMove;                     // save mover color
            UndoData ud = makeMove(m);
            boolean ok = !isKingAttacked(mover);       // test if mover's king is attacked after move
            unmakeMove(m, ud);
            if (!ok) it.remove();
        }
        return moves;
    }
    private void generatePseudoLegalMoves(List<Move> out) {
        int us = sideToMove;
        int them = us ^ 1;
        long occ = occupancy[2];
        long pawns = pieceBB[us*6 + PAWN];
        while (pawns != 0) {
            int from = Long.numberOfTrailingZeros(pawns);
            pawns &= pawns - 1;
            generatePawnMoves(out, from, us, occ);
        }
        long knights = pieceBB[us*6 + KNIGHT];
        while (knights != 0) {
            int from = Long.numberOfTrailingZeros(knights);
            knights &= knights - 1;
            long targets = KNIGHT_ATTACKS[from] & ~occupancy[us];
            while (targets != 0) {
                int to = Long.numberOfTrailingZeros(targets); targets &= targets-1;
                boolean cap = ((occupancy[them] & (1L<<to))!=0);
                out.add(new Move(from,to,KNIGHT,-1,cap,false,false));
            }
        }
        long bishops = pieceBB[us*6 + BISHOP];
        while (bishops != 0) {
            int from = Long.numberOfTrailingZeros(bishops); bishops &= bishops-1;
            long attacks = bishopAttacks(from, occ) & ~occupancy[us];
            addSlidingMoves(out, from, attacks, BISHOP);
        }
        long rooks = pieceBB[us*6 + ROOK];
        while (rooks != 0) {
            int from = Long.numberOfTrailingZeros(rooks); rooks &= rooks-1;
            long attacks = rookAttacks(from, occ) & ~occupancy[us];
            addSlidingMoves(out, from, attacks, ROOK);
        }
        long queens = pieceBB[us*6 + QUEEN];
        while (queens != 0) {
            int from = Long.numberOfTrailingZeros(queens); queens &= queens-1;
            long attacks = queenAttacks(from, occ) & ~occupancy[us];
            addSlidingMoves(out, from, attacks, QUEEN);
        }
        long kings = pieceBB[us*6 + KING];
        if (kings != 0) {
            int from = Long.numberOfTrailingZeros(kings);
            long targets = KING_ATTACKS[from] & ~occupancy[us];
            while (targets != 0) {
                int to = Long.numberOfTrailingZeros(targets); targets &= targets-1;
                boolean cap = ((occupancy[them] & (1L<<to))!=0);
                out.add(new Move(from,to,KING,-1,cap,false,false));
            }
            // Castling with attack verification
            if (us == WHITE) {
                if ((castlingRights & 1) != 0) {
                    if (((occupancy[2] & (SQUARE_BB[5]|SQUARE_BB[6]))==0)) {
                        if (!isSquareAttacked(4, BLACK) && !isSquareAttacked(5, BLACK) && !isSquareAttacked(6, BLACK))
                            out.add(new Move(from,6,KING,-1,false,false,true));
                    }
                }
                if ((castlingRights & 2) != 0) {
                    if (((occupancy[2] & (SQUARE_BB[1]|SQUARE_BB[2]|SQUARE_BB[3]))==0)) {
                        if (!isSquareAttacked(4, BLACK) && !isSquareAttacked(3, BLACK) && !isSquareAttacked(2, BLACK))
                            out.add(new Move(from,2,KING,-1,false,false,true));
                    }
                }
            } else {
                if ((castlingRights & 4) != 0) {
                    if (((occupancy[2] & (SQUARE_BB[61]|SQUARE_BB[62]))==0)) {
                        if (!isSquareAttacked(60, WHITE) && !isSquareAttacked(61, WHITE) && !isSquareAttacked(62, WHITE))
                            out.add(new Move(from,62,KING,-1,false,false,true));
                    }
                }
                if ((castlingRights & 8) != 0) {
                    if (((occupancy[2] & (SQUARE_BB[57]|SQUARE_BB[58]|SQUARE_BB[59]))==0)) {
                        if (!isSquareAttacked(60, WHITE) && !isSquareAttacked(59, WHITE) && !isSquareAttacked(58, WHITE))
                            out.add(new Move(from,58,KING,-1,false,false,true));
                    }
                }
            }
        }
    }
    private void addSlidingMoves(List<Move> out, int from, long attacks, int piece) {
        long t = attacks;
        int them = sideToMove ^ 1;
        while (t != 0) {
            int to = Long.numberOfTrailingZeros(t); t &= t-1;
            boolean cap = ((occupancy[them] & (1L<<to))!=0);
            out.add(new Move(from,to,piece,-1,cap,false,false));
        }
    }
    private void generatePawnMoves(List<Move> out, int from, int us, long occ) {
        int them = us ^ 1;
        if (us == WHITE) {
            int one = from + 8;
            if ((occ & (1L<<one))==0) {
                if (RANK[one] == 7) {
                    out.add(new Move(from,one,PAWN,QUEEN,false,false,false));
                    out.add(new Move(from,one,PAWN,ROOK,false,false,false));
                    out.add(new Move(from,one,PAWN,BISHOP,false,false,false));
                    out.add(new Move(from,one,PAWN,KNIGHT,false,false,false));
                } else out.add(new Move(from,one,PAWN,-1,false,false,false));
                if (RANK[from]==1) {
                    int two = from + 16;
                    if ((occ & (1L<<two))==0) out.add(new Move(from,two,PAWN,-1,false,false,false));
                }
            }
            long attacks = WHITE_PAWN_ATTACKS[from] & occupancy[them];
            long t = attacks;
            while (t != 0) {
                int to = Long.numberOfTrailingZeros(t); t &= t-1;
                if (RANK[to] == 7) {
                    out.add(new Move(from,to,PAWN,QUEEN,true,false,false));
                    out.add(new Move(from,to,PAWN,ROOK,true,false,false));
                    out.add(new Move(from,to,PAWN,BISHOP,true,false,false));
                    out.add(new Move(from,to,PAWN,KNIGHT,true,false,false));
                } else out.add(new Move(from,to,PAWN,-1,true,false,false));
            }
            if (enPassantSquare >= 0) {
                long ep = 1L<<enPassantSquare;
                long epAtt = WHITE_PAWN_ATTACKS[from] & ep;
                if (epAtt != 0) out.add(new Move(from,enPassantSquare,PAWN,-1,true,true,false));
            }
        } else {
            int one = from - 8;
            if ((occ & (1L<<one))==0) {
                if (RANK[one] == 0) {
                    out.add(new Move(from,one,PAWN,QUEEN,false,false,false));
                    out.add(new Move(from,one,PAWN,ROOK,false,false,false));
                    out.add(new Move(from,one,PAWN,BISHOP,false,false,false));
                    out.add(new Move(from,one,PAWN,KNIGHT,false,false,false));
                } else out.add(new Move(from,one,PAWN,-1,false,false,false));
                if (RANK[from]==6) {
                    int two = from - 16;
                    if ((occ & (1L<<two))==0) out.add(new Move(from,two,PAWN,-1,false,false,false));
                }
            }
            long attacks = BLACK_PAWN_ATTACKS[from] & occupancy[them];
            long t = attacks;
            while (t != 0) {
                int to = Long.numberOfTrailingZeros(t); t &= t-1;
                if (RANK[to] == 0) {
                    out.add(new Move(from,to,PAWN,QUEEN,true,false,false));
                    out.add(new Move(from,to,PAWN,ROOK,true,false,false));
                    out.add(new Move(from,to,PAWN,BISHOP,true,false,false));
                    out.add(new Move(from,to,PAWN,KNIGHT,true,false,false));
                } else out.add(new Move(from,to,PAWN,-1,true,false,false));
            }
            if (enPassantSquare >= 0) {
                long ep = 1L<<enPassantSquare;
                long epAtt = BLACK_PAWN_ATTACKS[from] & ep;
                if (epAtt != 0) out.add(new Move(from,enPassantSquare,PAWN,-1,true,true,false));
            }
        }
    }
    static final class UndoData {
        long[] pieceBBcopy = new long[12];
        long[] occCopy = new long[3];
        int oldCastling;
        int oldEP;
        int oldHalfmove;
    }
    public UndoData makeMove(Move m) {

        updatePhase();
        UndoData ud = new UndoData();
        // save full board arrays and state
        System.arraycopy(pieceBB, 0, ud.pieceBBcopy, 0, 12);
        System.arraycopy(occupancy, 0, ud.occCopy, 0, 3);
        ud.oldCastling = castlingRights;
        ud.oldEP = enPassantSquare;
        ud.oldHalfmove = halfmoveClock;

        int us = sideToMove;
        int them = us ^ 1;
        long fromBB = 1L << m.from;
        long toBB = 1L << m.to;

        // Reset halfmove or increment
        if (m.piece == PAWN) halfmoveClock = 0;
        else halfmoveClock++;

        // Clear captured piece (normal capture or en-passant)
        if (m.isEnPassant) {
            // en-passant capture: captured pawn is behind the to-square
            int capSq = (us == WHITE) ? m.to - 8 : m.to + 8;
            long capBB = 1L << capSq;
            // remove captured pawn
            pieceBB[them*6 + PAWN] &= ~capBB;
        } else {
            // normal capture: remove any piece that sits on to-square
            for (int p = 0; p < 6; p++) {
                if ((pieceBB[them*6 + p] & toBB) != 0) {
                    pieceBB[them*6 + p] &= ~toBB;
                    break;
                }
            }
        }

        // move the piece (remove from from, place on to; handle promotion)
        pieceBB[us*6 + m.piece] &= ~fromBB;
        if (m.promotion >= 0) {
            // place promoted piece
            pieceBB[us*6 + m.promotion] |= toBB;
        } else {
            pieceBB[us*6 + m.piece] |= toBB;
        }

        // handle castling rook move
        if (m.isCastle) {
            if (us == WHITE) {
                if (m.to == 6) { // white king-side
                    pieceBB[WHITE*6 + ROOK] &= ~(1L<<7);
                    pieceBB[WHITE*6 + ROOK] |= (1L<<5);
                } else if (m.to == 2) { // white queen-side
                    pieceBB[WHITE*6 + ROOK] &= ~(1L<<0);
                    pieceBB[WHITE*6 + ROOK] |= (1L<<3);
                }
            } else {
                if (m.to == 62) { // black king-side
                    pieceBB[BLACK*6 + ROOK] &= ~(1L<<63);
                    pieceBB[BLACK*6 + ROOK] |= (1L<<61);
                } else if (m.to == 58) { // black queen-side
                    pieceBB[BLACK*6 + ROOK] &= ~(1L<<56);
                    pieceBB[BLACK*6 + ROOK] |= (1L<<59);
                }
            }
        }

        // update castling rights if king or rook moved or rook captured
        if (m.piece == KING) {
            if (us == WHITE) castlingRights &= ~(1|2);
            else castlingRights &= ~(4|8);
        }
        if (m.piece == ROOK) {
            if (us == WHITE) {
                if (m.from == 0) castlingRights &= ~2;
                if (m.from == 7) castlingRights &= ~1;
            } else {
                if (m.from == 56) castlingRights &= ~8;
                if (m.from == 63) castlingRights &= ~4;
            }
        }
        // if a rook was captured, clear opponent castling rights accordingly
        // For en-passant, captured piece is a pawn, so no castling rights change
        if (!m.isEnPassant) {
            if (m.isCapture) {
                int to = m.to;
                if (them == WHITE) {
                    if (to == 0) castlingRights &= ~2;
                    if (to == 7) castlingRights &= ~1;
                } else {
                    if (to == 56) castlingRights &= ~8;
                    if (to == 63) castlingRights &= ~4;
                }
            }
        }

        // set en-passant square after a double pawn push
        enPassantSquare = -1;
        if (m.piece == PAWN && Math.abs(m.to - m.from) == 16) {
            enPassantSquare = (m.from + m.to) / 2;
        }

        recomputeOccupancy();
        sideToMove = them;
        return ud;
    }
    public void unmakeMove(Move m, UndoData ud) {
        System.arraycopy(ud.pieceBBcopy, 0, pieceBB, 0, 12);
        System.arraycopy(ud.occCopy, 0, occupancy, 0, 3);
        castlingRights = ud.oldCastling;
        enPassantSquare = ud.oldEP;
        halfmoveClock = ud.oldHalfmove;
        sideToMove ^= 1;
    }
    public boolean isSquareAttacked(int sq, int byColor) {
        long occ = occupancy[2];
        if (byColor == WHITE) {
            long whitePawns = occupancy[WHITE] & pieceBB[WHITE*6 + PAWN];
            // squares attacked by white pawns:
            long pawnAttacks = ((whitePawns & ~FILE_A) << 7) | ((whitePawns & ~FILE_H) << 9);
            if ((pawnAttacks & (1L<<sq)) != 0) return true;
        } else {
            long blackPawns = occupancy[BLACK] & pieceBB[BLACK*6 + PAWN];
            long pawnAttacks = ((blackPawns & ~FILE_H) >>> 7) | ((blackPawns & ~FILE_A) >>> 9);
            if ((pawnAttacks & (1L<<sq)) != 0) return true;
        }
        // knights
        if ((KNIGHT_ATTACKS[sq] & pieceBB[byColor*6 + KNIGHT]) != 0) return true;
        // bishops / queens
        if ((bishopAttacks(sq, occ) & (pieceBB[byColor*6 + BISHOP] | pieceBB[byColor*6 + QUEEN])) != 0) return true;
        // rooks / queens
        if ((rookAttacks(sq, occ) & (pieceBB[byColor*6 + ROOK] | pieceBB[byColor*6 + QUEEN])) != 0) return true;
        // king
        if ((KING_ATTACKS[sq] & pieceBB[byColor*6 + KING]) != 0) return true;
        return false;
    }
    public boolean isKingAttacked(int color) {
        long kingBB = pieceBB[color*6 + KING];
        if (kingBB == 0) return false;
        int ksq = Long.numberOfTrailingZeros(kingBB);
        return isSquareAttacked(ksq, color^1);
    }
    public long perft(int depth) {
        if (depth == 0) return 1;
        List<Move> moves = generateLegalMoves();
        if (depth == 1) return moves.size();
        long nodes = 0L;
        for (Move m : moves) {
            UndoData ud = makeMove(m);
            nodes += perft(depth - 1);
            unmakeMove(m, ud);
        }
        return nodes;
    }
    public String toStringBoard() {
        StringBuilder sb = new StringBuilder();
        for (int r = 7; r >= 0; r--) {
            sb.append(r+1).append(" ");
            for (int f = 0; f < 8; f++) {
                int sq = r*8 + f; char ch = '.';
                for (int c = 0; c < 2; c++) for (int p = 0; p < 6; p++) {
                    if ((pieceBB[c*6 + p] & (1L<<sq)) != 0) {
                        ch = pieceChar(p);
                        if (c == BLACK) ch = Character.toLowerCase(ch);
                    }
                }
                sb.append(ch).append(' ');
            }
            sb.append('\n');
        }
        sb.append("  a b c d e f g h\n");
        sb.append("Side: ").append(sideToMove==WHITE?"w":"b").append(" Castling: ");
        sb.append(((castlingRights&1)!=0)?"K":"");
        sb.append(((castlingRights&2)!=0)?"Q":"");
        sb.append(((castlingRights&4)!=0)?"k":"");
        sb.append(((castlingRights&8)!=0)?"q":"");
        sb.append(" EP: ").append(enPassantSquare>=0?squareToAlgebraic(enPassantSquare):"-");
        sb.append("\n");
        return sb.toString();
    }
    public void prettyPrintBitboard(long bb) {
        for (int r = 7; r >= 0; r--) {
            for (int f = 0; f < 8; f++) {
                int sq = r*8 + f;
                System.out.print(((bb>>>sq)&1) == 1 ? '1' : '.');
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private void updatePhase() {
        int phase = 0;

        for (int piece = 0; piece < 6; piece++) {
            phase += Long.bitCount(pieceBB[piece]) * PHASE_WEIGHTS[piece];
        }

        for (int piece = 0; piece < 6; piece++) {
            phase += Long.bitCount(pieceBB[6 + piece]) * PHASE_WEIGHTS[piece];
        }

        if (phase > 24) currentPhase = 24;
        currentPhase = (24 - phase) / 24;
        
    }
    public int evaluate() {
        int position = 0;
        long mask = pieceBB[(WHITE*6) + PAWN];
        while (mask != 0) {

            int square = Long.numberOfTrailingZeros(mask);
            
            int pieceValue = 100 + ((1-currentPhase) * (WHITE_PST[PAWN * 2][square]) + currentPhase * (WHITE_PST[(PAWN * 2) + 1][square]));
            position += pieceValue;
            if (square == 28) {
                System.err.println("HIIIII" + pieceValue);
            }
            mask &= ~(1L << square);
        }

        mask = pieceBB[(WHITE*6) + KNIGHT];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = 320 + ((1-currentPhase) * (WHITE_PST[KNIGHT * 2][square]) + currentPhase * (WHITE_PST[(KNIGHT * 2) + 1][square]));
            position += pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(WHITE*6) + BISHOP];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = 330 + ((1-currentPhase) * (WHITE_PST[BISHOP * 2][square]) + currentPhase * (WHITE_PST[(BISHOP * 2) + 1][square]));
            position += pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(WHITE*6) + ROOK];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = 500 + ((1-currentPhase) * (WHITE_PST[ROOK * 2][square]) + currentPhase * (WHITE_PST[(ROOK * 2) + 1][square]));
            position += pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(WHITE*6) + QUEEN];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = 900 + ((1-currentPhase) * (WHITE_PST[QUEEN * 2][square]) + currentPhase * (WHITE_PST[(QUEEN * 2) + 1][square]));
            position += pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(WHITE*6) + KING];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = ((1-currentPhase) * (WHITE_PST[KING * 2][square]) + currentPhase * (WHITE_PST[(KING * 2) + 1][square]));
            position += pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(BLACK*6) + PAWN];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue =  100 + (((1-currentPhase) * (BLACK_PST[PAWN * 2][square]) + currentPhase * (BLACK_PST[(PAWN * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(BLACK*6) + KNIGHT];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue =  320 + (((1-currentPhase) * (BLACK_PST[KNIGHT * 2][square]) + currentPhase * (BLACK_PST[(KNIGHT * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        
        }
        mask = pieceBB[(BLACK*6) + BISHOP];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue =  330 + (((1-currentPhase) * (BLACK_PST[BISHOP * 2][square]) + currentPhase * (BLACK_PST[(BISHOP * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(BLACK*6) + ROOK];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue =  500 + (((1-currentPhase) * (BLACK_PST[ROOK * 2][square]) + currentPhase * (BLACK_PST[(ROOK * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(BLACK*6) + QUEEN];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue =  900 + (((1-currentPhase) * (BLACK_PST[QUEEN * 2][square]) + currentPhase * (BLACK_PST[(QUEEN * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        }
        mask = pieceBB[(BLACK*6) + KING];
        while (mask != 0) {
            int square = Long.numberOfTrailingZeros(mask);
            int pieceValue = (((1-currentPhase) * (BLACK_PST[KING * 2][square]) + currentPhase * (BLACK_PST[(KING * 2) + 1][square])));
            position -= pieceValue;
            mask &= ~(1L << square);
        }
        return position;
    }
    //bit 1 is a1 and array[0]...
    public static void main(String[] args) {
        BoardV3 cb = new BoardV3();
        cb.setFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        System.err.println(cb.evaluate());
        System.out.println("Default starting position:");
        System.out.println(cb.toStringBoard());
        int[] depths = {1,2,3,4};
        for (int d : depths) {
            long start = System.nanoTime();
            long nodes = cb.perft(d);
            long end = System.nanoTime();
            double sec = (end - start) / 1e9;
            System.out.printf("perft(%d) = %d   time=%.10fs\n", d, nodes, sec);
        }
        System.err.println(cb.evaluate());
        
    }
}
