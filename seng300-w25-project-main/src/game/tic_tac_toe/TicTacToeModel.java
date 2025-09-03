package game.tic_tac_toe;

import game.GameModel;
import game.Piece;

/**
 * A game data model for Tic-Tac-Toe.
 */
public class TicTacToeModel extends GameModel {
    public static final int ROWS = 3;
    public static final int COLUMNS = 3;

    public static final int TEAM_1 = 1;
    public static final int TEAM_2 = 2;

    public TicTacToeModel() {
        super(new TicTacToePiece[ROWS][COLUMNS]);
    }

    /**
     * Searches for a winning line length, starting from some position.
     * @param fromX x-coordinate to start search from
     * @param fromY y-coordinate to start search from
     * @return True, iff a winning line length was found.
     */
    public boolean isWin(int fromX, int fromY) {
        Class<?> piece = getPieceAt(fromX, fromY).getClass();
        return (
                getHorizontalLineLength(fromX, fromY, piece) >= 3
                ||
                getVerticalLineLength(fromX, fromY, piece) >= 3
                ||
                getBackwardSlashLineLength(fromX, fromY, piece) >= 3
                ||
                getForwardSlashLineLength(fromX, fromY, piece) >= 3
                );
    }

    @Override
    public void placePiece(int atRow, int atCol, Piece piece) {
        if (getPieceAt(atRow, atCol) != NO_PIECE) {
            throw new IllegalArgumentException(atRow + ", " + atCol + " is already occupied by a " + getPieceAt(atRow, atCol));
        }
        super.placePiece(atRow, atCol, piece);
    }
}
