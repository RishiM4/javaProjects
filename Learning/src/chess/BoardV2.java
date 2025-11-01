package src.chess;

public class BoardV2 {
    public byte board[];
    static final long knightMasks[] = new long[64];
    static final long kingMasks[] = new long[64];
    static final long pawnAttackMasks[][] = new long[2][64];
    static final long pawnEnpassant[][] = new long[2][64];
    private boolean whiteKingsideCastlingRights = true;
    private boolean whiteQueensideCastlingRights = true;
    private boolean blackKingsideCastlingRights = true;
    private boolean blackQueensideCastlingRights = true;

    private static long whiteOccupancy = 0L;
    private static long blackOccupancy = 0L;
    private static long boardOccupancy = 0L;
    private static final long pieceOccupancy[] = new long[13];

    static final int kingVectors[] = {8, -8, 1, -1, 9, 7, -7, -9};
    static final int knightVectors[] = {17, 10, -6, -15, -17, -10, 6, 15};

    private static final int[] rookShifts = {
        52, 53, 53, 53, 53, 53, 53, 52,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        53, 54, 54, 54, 54, 54, 54, 53,
        52, 53, 53, 53, 53, 53, 53, 52
    };
    private static final int[] bishopShifts = {
        58, 59, 59, 59, 59, 59, 59, 58,
        59, 59, 59, 59, 59, 59, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 55, 55, 57, 59, 59,
        59, 59, 57, 57, 57, 57, 59, 59,
        59, 59, 59, 59, 59, 59, 59, 59,
        58, 59, 59, 59, 59, 59, 59, 58
    };  
    private static final long[] rookMagics = {
        0x8a80104000800020L, 0x140002000100040L,  0x2801880a0017001L,  0x100081001000420L,
        0x200020010080420L,  0x3001c0002010008L,  0x8480008002000100L, 0x2080088004402900L,
        0x800098204000L,     0x2024401000200040L, 0x100802000801000L,  0x120800800801000L,
        0x208808088000400L,  0x2802200800400L,    0x2200800100020080L, 0x801000060821100L,
        0x80044006422000L,   0x100808020004000L,  0x12108a0010204200L, 0x140848010000802L,
        0x481828014002800L,  0x8094004002004100L, 0x4010040010010802L, 0x20008806104L,
        0x100400080208000L,  0x2040002120081000L, 0x21200680100081L,   0x20100080080080L,
        0x2000a00200410L,    0x20080800400L,      0x80088400100102L,   0x80004600042881L,
        0x4040008040800020L, 0x440003000200801L,  0x4200011004500L,    0x188020010100100L,
        0x14800401802800L,   0x2080040080800200L, 0x124080204001001L,  0x200046502000484L,
        0x480400080088020L,  0x1000422010034000L, 0x30200100110040L,   0x100021010009L,
        0x2002080100110004L, 0x202008004008002L,  0x20020004010100L,   0x2048440040820001L,
        0x101002200408200L,  0x40802000401080L,   0x4008142004410100L, 0x2060820c0120200L,
        0x1001004080100L,    0x20c020080040080L,  0x2935610830022400L, 0x44440041009200L,
        0x280001040802101L,  0x2100190040002085L, 0x80c0084100102001L, 0x4024081001000421L,
        0x20030a0244872L,    0x12001008414402L,   0x2006104900a0804L,  0x1004081002402L
    };
    private static final long[] bishopMagics = {
        0x40040844404084L,   0x2004208a004208L,   0x10190041080202L,   0x108060845042010L,
        0x581104180800210L,  0x2112080446200010L, 0x1080820820060210L, 0x3c0808410220200L,
        0x4050404440404L,    0x21001420088L,      0x24d0080801082102L, 0x1020a0a020400L,
        0x40308200402L,      0x4011002100800L,    0x401484104104005L,  0x801010402020200L,
        0x400210c3880100L,   0x404022024108200L,  0x810018200204102L,  0x4002801a02003L,
        0x85040820080400L,   0x810102c808880400L, 0xe900410884800L,    0x8002020480840102L,
        0x220200865090201L,  0x2010100a02021202L, 0x152048408022401L,  0x20080002081110L,
        0x4001001021004000L, 0x800040400a011002L, 0xe4004081011002L,   0x1c004001012080L,
        0x8004200962a00220L, 0x8422100208500202L, 0x2000402200300c08L, 0x8646020080080080L,
        0x80020a0200100808L, 0x2010004880111000L, 0x623000a080011400L, 0x42008c0340209202L,
        0x209188240001000L,  0x400408a884001800L, 0x110400a6080400L,   0x1840060a44020800L,
        0x90080104000041L,   0x201011000808101L,  0x1a2208080504f080L, 0x8012020600211212L,
        0x500861011240000L,  0x180806108200800L,  0x4000020e01040044L, 0x300000261044000aL,
        0x802241102020002L,  0x20906061210001L,   0x5a84841004010310L, 0x4010801011c04L,
        0xa010109502200L,    0x4a02012000L,       0x500201010098b028L, 0x8040002811040900L,
        0x28000010020204L,   0x6000020202d0240L,  0x8918844842082200L, 0x4010011029020020L
    };
    
    private static final long[][] rookTable = new long[64][];
    private static final long[][] bishopTable = new long[64][];

    private static final int values[] = new int[13];
    private int enpassant = 0;
    private static final int EMPTY = 0;
    private static final int WHITE_PAWN = 1;
    private static final int WHITE_KNIGHT = 2;
    private static final int WHITE_BISHOP = 3;
    private static final int WHITE_ROOK = 4;
    private static final int WHITE_QUEEN = 5;
    private static final int WHITE_KING = 6;

    private static final int BLACK_PAWN = 7;
    private static final int BLACK_KNIGHT = 8;
    private static final int BLACK_BISHOP = 9;
    private static final int BLACK_ROOK = 10;
    private static final int BLACK_QUEEN = 11;
    private static final int BLACK_KING = 12;
    public BoardV2 () {
        board = new byte[] {  4,  2,  3,  5,  6,  3,  2,  4,
                              1,  1,  1,  1,  1,  1,  1,  1,
                              0,  0,  0,  0,  0,  0,  0,  0,
                              0,  0,  0,  0,  0,  0,  0,  0,
                              0,  0,  0,  0,  0,  0,  0,  0,
                              0,  0,  0,  0,  0,  0,  0,  0,
                              7,  7,  7,  7,  7,  7,  7,  7,
                             10,  8,  9, 11, 12,  9,  8, 10,};
        computeKingMoves();
        computeKnightMoves();
        computePawnAttacks();
        computeEnpassant();
        init();
        pieceOccupancy[WHITE_PAWN] = 0x000000000000FF00L;
        pieceOccupancy[WHITE_KNIGHT] = 0x0000000000000042L;
        pieceOccupancy[WHITE_BISHOP] = 0x0000000000000024L;
        pieceOccupancy[WHITE_ROOK] = 0x0000000000000081L;
        pieceOccupancy[WHITE_QUEEN] = 0x0000000000000008L;
        pieceOccupancy[WHITE_KING] = 0x0000000000000010L;
        
        pieceOccupancy[BLACK_PAWN] = 0x00FF000000000000L;
        pieceOccupancy[BLACK_KNIGHT] = 0x4200000000000000L;
        pieceOccupancy[BLACK_BISHOP] = 0x2400000000000000L;
        pieceOccupancy[BLACK_ROOK] = 0x8100000000000000L;
        pieceOccupancy[BLACK_QUEEN] = 0x0800000000000000L;
        pieceOccupancy[BLACK_KING] = 0x1000000000000000L;

        whiteOccupancy = pieceOccupancy[WHITE_PAWN] | pieceOccupancy[WHITE_KNIGHT] | pieceOccupancy[WHITE_BISHOP] | pieceOccupancy[WHITE_ROOK] | pieceOccupancy[WHITE_QUEEN] | pieceOccupancy[WHITE_KING]; 
        blackOccupancy = pieceOccupancy[BLACK_PAWN] | pieceOccupancy[BLACK_KNIGHT] | pieceOccupancy[BLACK_BISHOP] | pieceOccupancy[BLACK_ROOK] | pieceOccupancy[BLACK_QUEEN] | pieceOccupancy[BLACK_KING]; 

        boardOccupancy = whiteOccupancy | blackOccupancy;
        values[EMPTY] = 0;

        values[WHITE_PAWN] = 100;
        values[WHITE_KNIGHT] = 320;
        values[WHITE_BISHOP] = 330;
        values[WHITE_ROOK] = 500;
        values[WHITE_QUEEN] = 900;
        values[WHITE_KING] = 20000;

        values[BLACK_PAWN] = -100;
        values[BLACK_KNIGHT] = -320;
        values[BLACK_BISHOP] = -330;
        values[BLACK_ROOK] = -500;
        values[BLACK_QUEEN] = -900;
        values[BLACK_KING] = -20000;
    }
    public BoardV2(byte[] board) {
        this();

        this.board = board;
    }
    private static void init() {
        for (int sq = 0; sq < 64; sq++) {
            rookTable[sq] = new long[1 << (64 - rookShifts[sq])];
            bishopTable[sq] = new long[1 << (64 - bishopShifts[sq])];
            fillTableRook(sq, rookMagics[sq], rookShifts[sq], rookMask(sq), rookTable[sq]);
            fillTableBishop(sq, bishopMagics[sq], bishopShifts[sq], bishopMask(sq), bishopTable[sq]);
        }
    }
    private static void fillTableRook(int square, long magic, int shift, long mask, long[] table) {
        int bits = Long.bitCount(mask);
        int entries = 1 << bits;
        for (int index = 0; index < entries; index++) {
            long blockers = indexToBlockers(index, bits, mask);
            long attacks = slidingAttacksRook(square, blockers);
            int key = (int) ((blockers * magic) >>> shift);
            table[key] = attacks;
        }
    }
    private static void fillTableBishop(int square, long magic, int shift, long mask, long[] table) {
        int bits = Long.bitCount(mask);
        int entries = 1 << bits;
        for (int index = 0; index < entries; index++) {
            long blockers = indexToBlockers(index, bits, mask);
            long attacks = slidingAttacksBishop(square, blockers);
            int key = (int) ((blockers * magic) >>> shift);
            table[key] = attacks;
        }
    }
    private static long indexToBlockers(int index, int bits, long mask) {
        long blockers = 0L;
        for (int i = 0; i < bits; i++) {
            int bit = Long.numberOfTrailingZeros(mask);
            mask &= mask - 1;
            if ((index & (1 << i)) != 0) blockers |= (1L << bit);
        }
        return blockers;
    }
    private static long slidingAttacksRook(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8, file = square % 8;

        for (int r = rank + 1; r < 8; r++) {
            attacks |= 1L << (r * 8 + file);
            if (((1L << (r * 8 + file)) & blockers) != 0) break;
        }
        for (int r = rank - 1; r >= 0; r--) {
            attacks |= 1L << (r * 8 + file);
            if (((1L << (r * 8 + file)) & blockers) != 0) break;
        }
        for (int f = file + 1; f < 8; f++) {
            attacks |= 1L << (rank * 8 + f);
            if (((1L << (rank * 8 + f)) & blockers) != 0) break;
        }
        for (int f = file - 1; f >= 0; f--) {
            attacks |= 1L << (rank * 8 + f);
            if (((1L << (rank * 8 + f)) & blockers) != 0) break;
        }
        return attacks;
    }
    private static long slidingAttacksBishop(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8, file = square % 8;

        for (int r = rank + 1, f = file + 1; r < 8 && f < 8; r++, f++) {
            attacks |= 1L << (r * 8 + f);
            if (((1L << (r * 8 + f)) & blockers) != 0) break;
        }
        for (int r = rank + 1, f = file - 1; r < 8 && f >= 0; r++, f--) {
            attacks |= 1L << (r * 8 + f);
            if (((1L << (r * 8 + f)) & blockers) != 0) break;
        }
        for (int r = rank - 1, f = file + 1; r >= 0 && f < 8; r--, f++) {
            attacks |= 1L << (r * 8 + f);
            if (((1L << (r * 8 + f)) & blockers) != 0) break;
        }
        for (int r = rank - 1, f = file - 1; r >= 0 && f >= 0; r--, f--) {
            attacks |= 1L << (r * 8 + f);
            if (((1L << (r * 8 + f)) & blockers) != 0) break;
        }
        return attacks;
    }
    private static long rookMask(int square) {
        long mask = 0L;
        int rank = square / 8, file = square % 8;
        for (int r = rank + 1; r < 7; r++) mask |= 1L << (r * 8 + file);
        for (int r = rank - 1; r > 0; r--) mask |= 1L << (r * 8 + file);
        for (int f = file + 1; f < 7; f++) mask |= 1L << (rank * 8 + f);
        for (int f = file - 1; f > 0; f--) mask |= 1L << (rank * 8 + f);
        return mask;
    }
    private static long bishopMask(int square) {
        long mask = 0L;
        int rank = square / 8, file = square % 8;
        for (int r = rank + 1, f = file + 1; r < 7 && f < 7; r++, f++) mask |= 1L << (r * 8 + f);
        for (int r = rank + 1, f = file - 1; r < 7 && f > 0; r++, f--) mask |= 1L << (r * 8 + f);
        for (int r = rank - 1, f = file + 1; r > 0 && f < 7; r--, f++) mask |= 1L << (r * 8 + f);
        for (int r = rank - 1, f = file - 1; r > 0 && f > 0; r--, f--) mask |= 1L << (r * 8 + f);
        return mask;
    }
    public static long getRookMask(int square, long occupancy) {
        long blockers = occupancy & rookMask(square);
        int index = (int) ((blockers * rookMagics[square]) >>> rookShifts[square]);
        return rookTable[square][index];
    }
    public static long getBishopMask(int square, long occupancy) {
        long blockers = occupancy & bishopMask(square);
        int index = (int) ((blockers * bishopMagics[square]) >>> bishopShifts[square]);
        return bishopTable[square][index];
    }
    public static void printMask(long bb) {
        for (int rank = 7; rank >= 0; rank--) {   // print from rank 8 down to rank 1
            for (int file = 0; file < 8; file++) {
                int sq = rank * 8 + file;
                long bit = 1L << sq;
                System.out.print((bb & bit) != 0 ? "1 " : ". ");
            }
            System.out.println();
        }
    }
    public void printBoard() {
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " "); // rank label
            for (int file = 0; file < 8; file++) {
                int index = rank * 8 + file; // map rank/file to 0..63
                int piece = board[index];

                // print piece symbol, or '.' for empty
                if (piece == EMPTY) {
                    System.out.print(". ");
                } else {
                    System.out.print(pieceToChar(piece) + " ");
                }
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }
    private char pieceToChar(int piece) {
        switch (piece) {
            case WHITE_PAWN:   return 'P';
            case WHITE_KNIGHT: return 'N';
            case WHITE_BISHOP: return 'B';
            case WHITE_ROOK:   return 'R';
            case WHITE_QUEEN:  return 'Q';
            case WHITE_KING:   return 'K';
            case BLACK_PAWN:   return 'p';
            case BLACK_KNIGHT: return 'n';
            case BLACK_BISHOP: return 'b';
            case BLACK_ROOK:   return 'r';
            case BLACK_QUEEN:  return 'q';
            case BLACK_KING:   return 'k';
            case EMPTY: return '.';
            default:           return '?';
        }
    }
    public boolean inBounds(int row, int collumn) {
        return (row < 8 && row >= 0 && collumn < 8 && collumn >= 0);
    }
    public boolean inBounds(int square) {
        return (square < 64  && square >= 0);
    }
    public void computeKnightMoves() {
        for (int k = 0; k < 64; k++) {
            long mask = 0L;
                int fromCol = k % 8;
            for (int vector : knightVectors) {
                int newPosition = k + vector;
                if (inBounds(newPosition) && Math.abs(fromCol - (newPosition % 8)) <= 2) {
                    mask |= (1L << newPosition);
                }
            }

            knightMasks[k] = mask;
        }
    }
    public void computeKingMoves() {
        for (int k = 0; k < 64; k++) {
            long mask = 0L;
            int fromCol = k % 8;
            for (int vector : kingVectors) {
                int newPosition = k + vector;
                if (inBounds(newPosition) && Math.abs(fromCol - (newPosition % 8)) <= 1) {
                    mask |= (1L << newPosition);
                }
            }

            kingMasks[k] = mask;
        }
    }
    public void computeEnpassant() {
        for (int k = 0; k < 64; k++) {
            long mask = 0L;
            if (k/8 == 4) {
                int collumn = k % 8;
                if (collumn == 0) {
                    mask |= 1L << k+9;
                }
                else if (collumn == 7) {
                    mask |= 1L << k+7;
                }
                else {
                    mask |= 1L << k+9;
                    mask |= 1L << k+7;

                }
            }
            pawnEnpassant[0][k] = mask;
        }
        for (int k = 0; k < 64; k++) {
            long mask = 0L;
            if (k/8 == 3) {
                int collumn = k % 8;
                if (collumn == 0) {
                    mask |= 1L << k-7;
                }
                else if (collumn == 7) {
                    mask |= 1L << k-9;
                }
                else {
                    mask |= 1L << k-9;
                    mask |= 1L << k-7;

                }
            }
            pawnEnpassant[1][k] = mask;
        }
    }
    public void computePawnAttacks() {
        for(int k = 0; k < 64; k++) {
            int collumn = k % 8;
            if (collumn == 0) {
                pawnAttackMasks[0][k] = 1L << (k+9);
            }
            else if (collumn == 7){
                pawnAttackMasks[0][k] = 1L << (k+7);
            }
            else {
                pawnAttackMasks[0][k] = 1L << (k+9);
                pawnAttackMasks[0][k] |= 1L << (k+7);
            }
        }
        for(int k = 0; k < 64; k++) {
            int collumn = k % 8;
            if (collumn == 0) {
                pawnAttackMasks[1][k] = 1L << (k-7);
            }
            else if (collumn == 7){
                pawnAttackMasks[1][k] = 1L << (k-9);
            }
            else {
                pawnAttackMasks[1][k] = 1L << (k-9);
                pawnAttackMasks[1][k] |= 1L << (k-7);
            }
        }
    }
    public int evaluate() {
        int value = 0;
        for(byte piece : board) {
            value += values[piece];
        }
        return value;
    }
    public boolean isSquareAttacked(int square, int turn) {
        
        if (turn == 1) {
            long mask = kingMasks[square];
            if ((mask & pieceOccupancy[BLACK_KING]) != 0) {
                return true;
            }
            mask = knightMasks[square];
            if ((mask & pieceOccupancy[BLACK_KNIGHT]) != 0) {
                return true;
            }
            mask = pawnAttackMasks[1][square];
            if ((mask & pieceOccupancy[BLACK_PAWN]) != 0) {
                return true;
            }
            mask = getRookMask(square, boardOccupancy) | getBishopMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[BLACK_QUEEN]) != 0) {
                return true;
            }
            mask = getRookMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[BLACK_ROOK]) != 0) {
                return true;
            }
            mask = getBishopMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[BLACK_BISHOP]) != 0) {
                return true;
            }
        }
        else {
            long mask = kingMasks[square];
            if ((mask & pieceOccupancy[WHITE_KING]) != 0) {
                return true;
            }
            mask = knightMasks[square];
            if ((mask & pieceOccupancy[WHITE_KNIGHT]) != 0) {
                return true;
            }
            mask = pawnAttackMasks[0][square];
            if ((mask & pieceOccupancy[WHITE_PAWN]) != 0) {
                return true;
            }
            mask = getRookMask(square, boardOccupancy) | getBishopMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[WHITE_QUEEN]) != 0) {
                return true;
            }
            mask = getRookMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[WHITE_ROOK]) != 0) {
                return true;
            }
            mask = getBishopMask(square, boardOccupancy);
            if ((mask & pieceOccupancy[WHITE_BISHOP]) != 0) {
                return true;
            }
        }
        return false;
    }
    public void getKingMoves(int square, long friendlyMask, int moves[]) {
        long mask = kingMasks[square];
        mask &= ~friendlyMask;
        while(mask != 0) {
            int to = Long.numberOfTrailingZeros(mask);
            mask &= mask-1;
            
            moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
            //System.err.println(to);
        }
    }
    public void getKnightMoves(int square, long friendlyMask, int moves[]) {
        long mask = knightMasks[square];
        mask &= ~friendlyMask;
        while(mask!=0) {
            int to = Long.numberOfTrailingZeros(mask);

            mask &= mask-1;
            moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
        }
    }
    public void getBishopMoves(int square, long friendlyMask, int moves[]) {
        long mask = getBishopMask(square, boardOccupancy);
        mask &= ~friendlyMask;
        while(mask!=0) {
            int to = Long.numberOfTrailingZeros(mask);
            mask &= mask-1;
            
            moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
        }
    }
    public void getRookMoves(int square, long friendlyMask, int moves[]) {
        long mask = getRookMask(square, boardOccupancy);
        mask &= ~friendlyMask;
        while(mask!=0) {
            int to = Long.numberOfTrailingZeros(mask);
            mask &= mask-1;
            
            moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
        }
    }
    public void getQueenMoves(int square, long friendlyMask, int moves[]) {
        long mask = getBishopMask(square, boardOccupancy) | getRookMask(square, boardOccupancy);
        mask &= ~friendlyMask;
        while(mask!=0) {
            int to = Long.numberOfTrailingZeros(mask);
            mask &= mask-1;
            
            moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
        }
    }
    public void getPawnMoves(int square, int turn, long opponentMask, int moves[]) {
        //attacks
        if (turn == 1) {
            long mask = pawnAttackMasks[0][square];
            mask &= opponentMask;
            while(mask!=0) {
                int to = Long.numberOfTrailingZeros(mask);
                mask &= mask-1;
                if (to >= 56) {
                    moves[moves[0]++] = MoveV2.encode(square, to, board[to], true, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
                else {
                    moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            if (square / 8 == 1) {
                int to = square + 16;
                if (((boardOccupancy >>> (square + 8)) & 1L) == 0 && ((boardOccupancy >>> to) & 1L) == 0) {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            int to = square + 8;
            //is lsb 1? ie is the to square empty
            if (((boardOccupancy >>> to) & 1L) == 0) {
                if (to >= 56) {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, true, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
                else {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            if (square / 8 == 4) {
                mask = pawnEnpassant[0][square];
                if ((mask & (1L << enpassant)) != 0) {
                    while(mask!=0) {
                        to = Long.numberOfTrailingZeros(mask);
                        mask &= mask-1;
                        if (to == enpassant) {
                            moves[moves[0]++] = MoveV2.encode(square, to, BLACK_PAWN, false, true, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                        }
                    }
                }
            }
            
            
        }
        else {
            long mask = pawnAttackMasks[1][square];
            mask &= opponentMask;
            while(mask!=0) {
                int to = Long.numberOfTrailingZeros(mask);
                mask &= mask-1;
                if (to <= 7) {
                    moves[moves[0]++] = MoveV2.encode(square, to, board[to], true, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
                else {
                    moves[moves[0]++] = MoveV2.encode(square, to, board[to], false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            if (square / 8 == 6) {
                int to = square - 16;
                if (((boardOccupancy >>> (square - 8)) & 1L) == 0 && ((boardOccupancy >>> to) & 1L) == 0) {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            int to = square - 8;
            //is lsb 1? ie is the to square empty
            if (((boardOccupancy >>> to) & 1L) == 0) {
                if (to <= 7) {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, true, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
                else {
                    moves[moves[0]++] = MoveV2.encode(square, to, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                }
            }
            if (square / 8 == 5) {
                mask = pawnEnpassant[1][square];
                if ((mask & (1L << enpassant)) != 0) {
                    while(mask!=0) {
                        to = Long.numberOfTrailingZeros(mask);
                        mask &= mask-1;
                        if (to == enpassant) {
                            moves[moves[0]++] = MoveV2.encode(square, to, WHITE_PAWN, false, true, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, false);
                        }
                    }
                }
            }
            
        }
        
    }
    public void makeMove(int move, int turn) {
        int from = MoveV2.from(move);
        int to = MoveV2.to(move);
        byte fromPiece = board[from];
        boolean promotion = MoveV2.promotion(move);
        int captured = MoveV2.capture(move);
        boolean caslting = MoveV2.caslting(move);
        if (false) {
            if (turn == 1) {
                //enpassant is the square behind the the recent pushed pawn
                pieceOccupancy[WHITE_PAWN] &= ~(1L << from);
                pieceOccupancy[WHITE_PAWN] |= 1L << enpassant;
                pieceOccupancy[BLACK_PAWN] &= ~(1L << enpassant);

                whiteOccupancy &= ~(1L << from);
                whiteOccupancy |= 1L << enpassant;
                blackOccupancy &= ~(1L << enpassant);

                board[from] = EMPTY;
                board[enpassant] = WHITE_PAWN;
                board[enpassant-8] = EMPTY;
            }
            else {
                pieceOccupancy[BLACK_PAWN] &= ~(1L << from);
                pieceOccupancy[BLACK_PAWN] |= 1L << enpassant;
                pieceOccupancy[WHITE_PAWN] &= ~(1L << enpassant);

                blackOccupancy &= ~(1L << from);
                blackOccupancy |= 1L << enpassant;
                whiteOccupancy &= ~(1L << enpassant);

                board[from] = EMPTY;
                board[enpassant] = BLACK_PAWN;
                board[enpassant+8] = EMPTY;
            }
        }
        else if (caslting) {
            //white queenside
            if (to == 2) {
                whiteKingsideCastlingRights = false;
                whiteQueensideCastlingRights = false;
                pieceOccupancy[WHITE_KING] &= ~(1L << 4);
                pieceOccupancy[WHITE_KING] |= 1L << 2;

                pieceOccupancy[WHITE_ROOK] &= ~(1L << 0);
                pieceOccupancy[WHITE_ROOK] |= 1L << 3;

                whiteOccupancy &= ~(1L << 4); 
                whiteOccupancy &= ~(1L << 0); 

                whiteOccupancy |= (1L << 2); 
                whiteOccupancy |= (1L << 3); 

                board[4] = EMPTY;
                board[2] = WHITE_KING;
                board[0] = EMPTY;
                board[3] = WHITE_ROOK;

            }
            //white kingside
            else if (to == 6) {
                whiteKingsideCastlingRights = false;
                whiteQueensideCastlingRights = false;
                pieceOccupancy[WHITE_KING] &= ~(1L << 4);
                pieceOccupancy[WHITE_KING] |= 1L << 6;

                pieceOccupancy[WHITE_ROOK] &= ~(1L << 7);
                pieceOccupancy[WHITE_ROOK] |= 1L << 5;
                whiteOccupancy &= ~(1L << 4); 
                whiteOccupancy &= ~(1L << 7); 

                whiteOccupancy |= (1L << 6); 
                whiteOccupancy |= (1L << 5); 
                board[4] = EMPTY;
                board[6] = WHITE_KING;
                board[7] = EMPTY;
                board[5] = WHITE_ROOK;
            }
            //black queenside
            else if (to == 58) {
                blackKingsideCastlingRights = false;
                blackQueensideCastlingRights = false;
                pieceOccupancy[BLACK_KING] &= ~(1L << 60);
                pieceOccupancy[BLACK_KING] |= 1L << 58;

                pieceOccupancy[BLACK_ROOK] &= ~(1L << 56);
                pieceOccupancy[BLACK_ROOK] |= 1L << 59;
                blackOccupancy &= ~(1L << 60); 
                blackOccupancy &= ~(1L << 56); 

                blackOccupancy |= (1L << 58); 
                blackOccupancy |= (1L << 59);

                board[60] = EMPTY;
                board[58] = BLACK_KING;
                board[56] = EMPTY;
                board[59] = BLACK_ROOK;

            }
            //black kingside
            else if (to == 62) {
                blackKingsideCastlingRights = false;
                blackQueensideCastlingRights = false;
                pieceOccupancy[BLACK_KING] &= ~(1L << 60);
                pieceOccupancy[BLACK_KING] |= 1L << 62;

                pieceOccupancy[BLACK_ROOK] &= ~(1L << 63);
                pieceOccupancy[BLACK_ROOK] |= 1L << 61;
                blackOccupancy &= ~(1L << 60); 
                blackOccupancy &= ~(1L << 63); 

                blackOccupancy |= (1L << 62); 
                blackOccupancy |= (1L << 61); 

                board[60] = EMPTY;
                board[62] = BLACK_KING;
                board[63] = EMPTY;
                board[61] = BLACK_ROOK;

            }
        }
        if (Math.abs(from-to) == 16) {
            if (fromPiece == WHITE_PAWN) {
                enpassant = to - 8;
            }
            else if (fromPiece == BLACK_PAWN) {
                enpassant = to + 8;
            }
        }
        if (captured == WHITE_ROOK) {
            if (to == 0) {
                whiteQueensideCastlingRights = false;
            }
            else if (to == 7) {
                whiteKingsideCastlingRights = false;
            }
        }
        if (captured == BLACK_ROOK) {
            if (to == 56) {
                blackQueensideCastlingRights = false;
            }
            else if (to == 63) {
                blackKingsideCastlingRights = false;
            }
        }
        if (fromPiece == WHITE_KING) {
            whiteQueensideCastlingRights = false;
            whiteKingsideCastlingRights = false;
        }
        if (fromPiece == BLACK_KING) {
            blackQueensideCastlingRights = false;
            blackKingsideCastlingRights = false;
        }
        if (promotion) {
            pieceOccupancy[fromPiece] &= ~(1L << from);
            pieceOccupancy[captured] &= ~(1L << to);
            board[from] = 0;
            if (turn == 1) {
                pieceOccupancy[WHITE_QUEEN] |= 1L << to;
                board[to] = WHITE_QUEEN;
                whiteOccupancy &= ~(1L << from);
                whiteOccupancy |= 1L << to;
                blackOccupancy &= ~(1L << to);
            }
            else {
                pieceOccupancy[BLACK_QUEEN] |= 1L << to;
                board[to] = BLACK_QUEEN;
                blackOccupancy &= ~(1L << from);
                blackOccupancy |= 1L << to;
                whiteOccupancy &= ~(1L << to);

            }
            
        }
        else {
            pieceOccupancy[fromPiece] &= ~(1L << from);
            pieceOccupancy[fromPiece] |= 1L << to;
            pieceOccupancy[MoveV2.capture(move)] &= ~(1L << to);
            
            
            board[from] = 0;
            board[to] = fromPiece;
            if (turn == 1) {
                whiteOccupancy &= ~(1L << from);
                whiteOccupancy |= (1L << to);
                blackOccupancy &= ~(1L << to);
            }
            else {
                blackOccupancy &= ~(1L << from);
                blackOccupancy |= (1L << to);
                whiteOccupancy &= ~(1L << to);
            }
        }
        boardOccupancy = whiteOccupancy | blackOccupancy;
        enpassant = 0;
        
    }
    public void undoMove(int move, int turn) {
        boolean promotion = MoveV2.promotion(move);
        int from = MoveV2.from(move);
        int to = MoveV2.to(move);
        int captured = MoveV2.capture(move);
        int originalPiece = board[to];
        if (MoveV2.caslting(move)) {
            whiteKingsideCastlingRights = MoveV2.whiteKingsideCastlingRights(move);
            whiteQueensideCastlingRights = MoveV2.whiteQueensideCastlingRights(move);
            blackKingsideCastlingRights = MoveV2.blackKingsideCastlingRights(move);
            blackQueensideCastlingRights = MoveV2.blackQueensideCastlingRights(move);
            if (to == 2) {
                pieceOccupancy[WHITE_KING] &= ~(1L << 2);
                pieceOccupancy[WHITE_KING] |= 1L << 4;
                pieceOccupancy[WHITE_ROOK] &= ~(1L << 3);
                pieceOccupancy[WHITE_ROOK] |= 1L << 0;

                whiteOccupancy &= ~(1L << 2);
                whiteOccupancy |= 1L << 4;
                whiteOccupancy &= ~(1L << 3);
                whiteOccupancy |= 1L << 0;

                board[2] = EMPTY;
                board[4] = WHITE_KING;
                board[3] = EMPTY;
                board[0] = WHITE_ROOK;

            }
            else if (to == 6) {
                pieceOccupancy[WHITE_KING] &= ~(1L << 6);
                pieceOccupancy[WHITE_KING] |= 1L << 4;
                pieceOccupancy[WHITE_ROOK] &= ~(1L << 5);
                pieceOccupancy[WHITE_ROOK] |= 1L << 7;

                whiteOccupancy &= ~(1L << 6);
                whiteOccupancy |= 1L << 4;
                whiteOccupancy &= ~(1L << 5);
                whiteOccupancy |= 1L << 7;

                board[6] = EMPTY;
                board[4] = WHITE_KING;
                board[5] = EMPTY;
                board[7] = WHITE_ROOK;
            }
            else if (to == 58) {
                pieceOccupancy[BLACK_KING] &= ~(1L << 58);
                pieceOccupancy[BLACK_KING] |= 1L << 60;
                pieceOccupancy[BLACK_ROOK] &= ~(1L << 59);
                pieceOccupancy[BLACK_ROOK] |= 1L << 56;

                blackOccupancy &= ~(1L << 58);
                blackOccupancy |= 1L << 60;
                blackOccupancy &= ~(1L << 59);
                blackOccupancy |= 1L << 56;

                board[58] = EMPTY;
                board[60] = BLACK_KING;
                board[59] = EMPTY;
                board[56] = BLACK_ROOK;

            }
            else if (to == 62) {
                pieceOccupancy[BLACK_KING] &= ~(1L << 62);
                pieceOccupancy[BLACK_KING] |= 1L << 60;
                pieceOccupancy[BLACK_ROOK] &= ~(1L << 61);
                pieceOccupancy[BLACK_ROOK] |= 1L << 63;

                blackOccupancy &= ~(1L << 62);
                blackOccupancy |= 1L << 60;
                blackOccupancy &= ~(1L << 61);
                blackOccupancy |= 1L << 63;

                board[62] = EMPTY;
                board[60] = BLACK_KING;
                board[61] = EMPTY;
                board[63] = BLACK_ROOK;
            }
        }
        if (MoveV2.enpassant(move)) {
            if (turn == 1) {
                enpassant = to;
                pieceOccupancy[WHITE_PAWN] &= ~(1L << to);
                pieceOccupancy[WHITE_PAWN] |= 1L << from;
                pieceOccupancy[BLACK_PAWN] |= 1L << (enpassant - 8);

                whiteOccupancy &= ~(1L << to);
                whiteOccupancy |= 1L << from;
                blackOccupancy |= 1L << (enpassant - 8);

                board[to] = EMPTY;
                board[from] = WHITE_PAWN;
                board[enpassant - 8] = BLACK_PAWN;


            }
            else {
                enpassant = to;
                pieceOccupancy[BLACK_PAWN] &= ~(1L << to);
                pieceOccupancy[BLACK_PAWN] |= 1L << from;
                pieceOccupancy[WHITE_PAWN] |= 1L << (enpassant + 8);

                blackOccupancy &= ~(1L << to);
                blackOccupancy |= 1L << from;
                whiteOccupancy |= 1L << (enpassant + 8);

                board[to] = EMPTY;
                board[from] = BLACK_PAWN;
                board[enpassant + 8] = WHITE_PAWN;
            }
        }
        if (promotion) {
            
            pieceOccupancy[captured] |= 1L  << to;
            board[from] = board[to];
            board[to] = (byte) captured;
            if (turn == 1) {
                pieceOccupancy[WHITE_QUEEN] &= ~(1L << to);
                pieceOccupancy[WHITE_PAWN] |= 1L << from;
                whiteOccupancy &=~(1L << to);
                whiteOccupancy |= 1L << from;
                blackOccupancy |= 1L << to;
            }
            else {
                pieceOccupancy[originalPiece] &= ~(1L << to);
                pieceOccupancy[originalPiece] |= 1L << from;
                blackOccupancy &= ~(1L << to);
                blackOccupancy |= 1L << from;
                whiteOccupancy |= 1L << to;
            }
        }
        else {
            pieceOccupancy[originalPiece] &= ~(1L << to);
            pieceOccupancy[originalPiece] |= 1L << from;
            pieceOccupancy[captured] |= 1L << to;
            board[from] = board[to];
            board[to] = (byte) captured;
            if (turn == 1) {
                whiteOccupancy &= ~(1L << to);
                whiteOccupancy |= (1L << from);
                if (captured != EMPTY) {
                    blackOccupancy |= 1L << to;
                }
            }
            else {
                blackOccupancy &= ~(1L << to);
                blackOccupancy |= (1L << from);
                if (captured != EMPTY) {
                    whiteOccupancy |= 1L << to;
                }
            }
        }
        
        
        boardOccupancy = whiteOccupancy | blackOccupancy;

    }
    public int[] generateMoves(int turn) {
        int moves[] = new int[256];
        moves[0] = 1;
        if (turn == 1) {
            if (whiteQueensideCastlingRights) {
                if ((((1L << 1) | (1L << 2) | (1L << 3)) & boardOccupancy) == 0) {
                    if (!isSquareAttacked(2, 1) && !isSquareAttacked(3, 1) && !isSquareAttacked(4, 1)) {
                        moves[moves[0]++] = MoveV2.encode(4, 2, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, true);
                    }
                }
            }
            if (whiteKingsideCastlingRights) {
                if ((((1L << 5) | (1L << 6)) & boardOccupancy) == 0) {
                    if (!isSquareAttacked(4, 1) && !isSquareAttacked(5, 1) && !isSquareAttacked(6, 1)) {
                        moves[moves[0]++] = MoveV2.encode(4, 6, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, true);
                    }
                }
            }
            long currentMask = pieceOccupancy[WHITE_PAWN];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getPawnMoves(square, turn, blackOccupancy, moves);
            }

            currentMask = pieceOccupancy[WHITE_KNIGHT];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getKnightMoves(square, whiteOccupancy, moves);
            }

            currentMask = pieceOccupancy[WHITE_BISHOP];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getBishopMoves(square, whiteOccupancy, moves);
            }
            
            currentMask = pieceOccupancy[WHITE_ROOK];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getRookMoves(square, whiteOccupancy, moves);
            }

            currentMask = pieceOccupancy[WHITE_QUEEN];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getQueenMoves(square, whiteOccupancy, moves);
            }

            currentMask = pieceOccupancy[WHITE_KING];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getKingMoves(square, whiteOccupancy, moves);
            }
            
        }
        else {
            if (blackQueensideCastlingRights) {
                if ((((1L << 57) | (1L << 58) | (1L << 59)) & boardOccupancy) == 0) {
                    if (!isSquareAttacked(58, 1) && !isSquareAttacked(59, 1) && !isSquareAttacked(60, 1)) {
                        moves[moves[0]++] = MoveV2.encode(60, 58, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, true);
                    }
                }
            }
            if (blackKingsideCastlingRights) {
                if ((((1L << 61) | (1L << 62)) & boardOccupancy) == 0) {
                    if (!isSquareAttacked(60, 1) && !isSquareAttacked(61, 1) && !isSquareAttacked(62, 1)) {
                        moves[moves[0]++] = MoveV2.encode(60, 62, 0, false, false, whiteKingsideCastlingRights, whiteQueensideCastlingRights, blackKingsideCastlingRights, blackQueensideCastlingRights, true);
                    }
                }
            }
            long currentMask = pieceOccupancy[BLACK_PAWN];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getPawnMoves(square, turn, whiteOccupancy, moves);
            }
            currentMask = pieceOccupancy[BLACK_KNIGHT];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getKnightMoves(square, blackOccupancy, moves);
            }
            currentMask = pieceOccupancy[BLACK_BISHOP];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getBishopMoves(square, blackOccupancy, moves);
            }
            
            currentMask = pieceOccupancy[BLACK_ROOK];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getRookMoves(square, blackOccupancy, moves);
            }

            currentMask = pieceOccupancy[BLACK_QUEEN];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getQueenMoves(square, blackOccupancy, moves);
            }

            currentMask = pieceOccupancy[BLACK_KING];
            while(currentMask!=0) {
                int square = Long.numberOfTrailingZeros(currentMask);
                currentMask &= currentMask-1;
                getKingMoves(square, blackOccupancy, moves);
            }

        }
        for(int k = 1; k < moves[0]; k++) {
            int move = moves[k];
            
            makeMove(move, turn);
            
            if (MoveV2.enpassant(move)) {
                System.err.println(k);
            }
            if (turn == 1) {
                if (isSquareAttacked(Long.numberOfTrailingZeros(pieceOccupancy[WHITE_KING]), turn)) {
                    moves[k] = -1;;
                }
            }
            else {
                if (isSquareAttacked(Long.numberOfTrailingZeros(pieceOccupancy[BLACK_KING]), turn)) {
                    moves[k] = -1;;
                }
            }
            undoMove(move, turn);
            
        }
        return moves;
    }
    public static void main(String[] args) {
        BoardV2 t = new BoardV2();
        //printMask(pawnEnpassant[1][25]);
        int move = MoveV2.encode(48, 32, EMPTY, false, false, false, false, false, false, false);
        
        t.generateMoves(-1);
        //t.printBoard();
    }
}   
