package game;

/**
 * A class for viewing and playing a game in the terminal.
 * <p> </p>
 * Should be used as a superclass for making specific games run in terminal.
 */
@Deprecated
public abstract class TerminalView {
    public static final char HORIZONTAL = '─';
    public static final char VERTICAL = '│';
    public static final char DOWN_RIGHT = '┌';
    public static final char DOWN_LEFT = '┐';
    public static final char UP_RIGHT = '└';
    public static final char UP_LEFT = '┘';
    public static final char VERTICAL_RIGHT = '├';
    public static final char VERTICAL_LEFT = '┤';
    public static final char DOWN_HORIZONTAL = '┬';
    public static final char UP_HORIZONTAL = '┴';
    public static final char VERTICAL_HORIZONTAL = '┼';

    protected final GameServerController game;

    public TerminalView(GameServerController game) {
        this.game = game;
    }

    /**
     * Run the game.
     */
    public abstract void run();

    /**
     * Prints a two-dimensional array of pieces to the terminal.
     * @param board the two-dimensional array of pieces to print
     */
    public static void printBoard(Piece[][] board) {
        for (int row = 0; row < board.length*2 + 1; row++) {
            for (int col = 0; col < board[row].length*2 + 1; col++) {
                /*
                Code for printing the array goes here.
                 */
            }
        }
    }
}
