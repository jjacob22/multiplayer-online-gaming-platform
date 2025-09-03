package game;

import javax.sql.rowset.serial.SerialException;
import java.io.Serializable;

/**
 * An abstract class for board game pieces.
 */
public class Piece implements Serializable {
    /**
     * The current row position of the piece.
     * <p></p>
     * -1 may be used to indicate that the piece is not currently on the board but still in the game somehow.
     */
    private int row;
    /**
     * The current column position of the piece.
     * <p></p>
     * -1 may be used to indicate that the piece is not currently on the board but still in the game somehow.
     */
    private int col;
    /**
     * The piece's character symbol.
     */
    private final char symbol;
    /**
     * An ID to represent who this piece belongs to.
     */
    private final long ownerID;

    public Piece(int row, int col, char symbol, long ownerID) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
        this.ownerID = ownerID;
    }

    public Piece(Piece piece) {
        this.row = piece.row;
        this.col = piece.col;
        this.symbol = piece.symbol;
        this.ownerID = piece.ownerID;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public char getSymbol() {
        return symbol;
    }

    public long getOwnerID() {
        return ownerID;
    }

    @Override
    public String toString() {
        return String.valueOf(this.symbol);
    }
}
