package chess;

public class Move {
    public int fromRow, fromCol;
    public int toRow, toCol;
    public char piece;
    public char captured;

    public Move(int fromRow, int fromCol, int toRow, int toCol, char piece, char captured) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.piece = piece;
        this.captured = captured;
    }

    @Override
    public String toString() {
        return piece + ": (" + fromRow + "," + fromCol + ") -> (" + toRow + "," + toCol + ")"
               + (captured != '.' ? " x" + captured : "");
    }
}
