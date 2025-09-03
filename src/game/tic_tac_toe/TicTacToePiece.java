package game.tic_tac_toe;

import game.Piece;

public abstract class TicTacToePiece extends Piece {
    public static final char SYMBOL_O = 'O';
    public static final char SYMBOL_X = 'X';

    public TicTacToePiece(int x, int y, char symbol, long ownerID) {
        super(x, y, symbol, ownerID);
    }
}
