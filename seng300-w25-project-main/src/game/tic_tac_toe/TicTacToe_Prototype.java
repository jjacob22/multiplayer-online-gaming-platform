package game.tic_tac_toe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A prototype for a game of Tic-Tac-Toe. Simply create an object for the game and run it:
 * <pre>
 *     TicTacToe game = new TicTacToe_Prototype();
 *     game.run()
 * </pre>
 * This was created early on to help get an idea of how a board game would be made in Java and to help inspire the design of reusable board game components.
 * <p> </p>
 * ChatGPT WAS used to help learn how the Java Swing and Abstract Window Toolkit frameworks may be used to make the game render and take user input.
 * ChatGPT WAS NOT used to provide final implementations.
 */
public class TicTacToe_Prototype extends JFrame implements ActionListener {
    /**
     * The pieces that are allowed on a Tic Tac Toe board.
     */
    public static final char[] ALLOWED_PIECES = {'X', 'O'};
    /**
     * An empty space, representing an empty piece.
     * Or more accurately, the absence of a piece.
     * How... empty.
     */
    public static final char EMPTY_PIECE = ' ';
    /**
     * The total rows for this game's board.
     */
    public static final int TOTAL_ROWS = 3;
    /**
     * The total columns for this game's board.
     */
    public static final int TOTAL_COLUMNS = 3;
    /**
     * The length of a winning line.
     */
    public static final int WINNING_LINE_LENGTH = 3;
    public static final int MAX_TURNS = 9;
    /**
     * Primary color for the board.
     */
    public static final Color PRIMARY_COLOR = new Color(0xba55d3);
    /**
     * Secondary color for the board.
     */
    public static final Color SECONDARY_COLOR = new Color(0x7b68ee);
    /**
     * Accent color for the board.
     */
    public static final Color ACCENT_COLOR = new Color(0xc71585);
    /**
     * Color for the pieces.
     */
    public static final Color PIECE_COLOR = Color.BLACK;
    /**
     * Image for the 'X' piece.
     */
    public static final Icon X_ICON = new ImageIcon("resources/tic_tac_toe/x.png", "X");
    /**
     * Image for the 'O' piece.
     */
    public static final Icon O_ICON = new ImageIcon("resources/tic_tac_toe/o.png", "O");
    /**
     * The game is being played while this boolean is set to true.
     */
    private volatile boolean playing = true;
    /**
     * The number of turns played.
     */
    private int turns = 0;
    private String winMessage = "No winner.";
    /**
     * The backend board containing pieces.
     */
    private final char[][] backendBoard = new char[TOTAL_ROWS][TOTAL_COLUMNS];
    /**
     * The frontend board containing rendered components.
     */
    private final JButton[][] frontendBoard = new JButton[TOTAL_ROWS][TOTAL_COLUMNS];

    /**
     * When constructing a TicTacToe object, the configuration for rendering the game is set up.
     */
    public TicTacToe_Prototype() {
        super("Tic-Tac-Toe");
        setLayout(new GridLayout(TOTAL_ROWS, TOTAL_COLUMNS));
        clearBackendBoard();
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                frontendBoard[i][j] = new JButton();
                frontendBoard[i][j].addActionListener(this);
                frontendBoard[i][j].putClientProperty("x", i);
                frontendBoard[i][j].putClientProperty("y", j);
                /*
                Appearance configurations.
                 */
                frontendBoard[i][j].setFocusPainted(false);
                frontendBoard[i][j].setFont(new Font("Courier", Font.BOLD, 64));
                frontendBoard[i][j].setForeground(PIECE_COLOR); // Foreground affects text color
                /*
                The two lines of code below seem like a nice algorithm for applying a checkered pattern.
                The second if-statement could just be simplified to an else clause.
                I just wanted to make the idea explicit as of now in case others would like to see.
                 */
                if ((i+j)%2==0) {frontendBoard[i][j].setBackground(PRIMARY_COLOR);}
                if ((i+j)%2==1) {frontendBoard[i][j].setBackground(SECONDARY_COLOR);}
                frontendBoard[i][j].setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
//                frontendBoard[i][j].setBorderPainted(false); // This would make it so there is no border.
                add(frontendBoard[i][j]);
            }
        }

        setSize(TOTAL_ROWS*100, TOTAL_COLUMNS*100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setAutoRequestFocus(true);
    }

    /**
     * Run the game from a cleared board.
     */
    public void run() {
        clearBackendBoard();
        updateFrontendBoard();
        setVisible(true);
        while (playing) {
            Thread.onSpinWait();
        }
        end();
    }

    public void end() {
        System.out.println(winMessage);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int x = (int) button.getClientProperty("x");
        int y = (int) button.getClientProperty("y");
        char piece = ALLOWED_PIECES[turns % ALLOWED_PIECES.length];

        if (playing && isValidMove(x, y, piece)) {
            placePiece(x, y, piece);
            updateFrontendBoard(x, y);
            if (isWin(x, y, piece)) {
                winMessage = piece + " won!";
                playing = false;
            }
            else {turns++;}
        }
        if (turns >= MAX_TURNS) {playing = false;}
    }

    /**
     * Updates a specific cell of the rendered board to match the backend board.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void updateFrontendBoard(int x, int y) {
        switch (backendBoard[x][y]) {
            case ('X'):
                frontendBoard[x][y].setIcon(X_ICON);
                break;
            case ('O'):
                frontendBoard[x][y].setIcon(O_ICON);
                break;
            default:
                frontendBoard[x][y].setIcon(null);
                break;
        }
    }

    /**
     * Updates entire rendered board to match the backend board.
     */
    public void updateFrontendBoard() {
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                updateFrontendBoard(i, j);
            }
        }
    }

    /**
     * Clears the backend board.
     */
    public void clearBackendBoard() {
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLUMNS; j++) {
                backendBoard[i][j] = EMPTY_PIECE;
            }
        }
    }

    /**
     * Checks if given coordinates are valid coordinates on the board.
     * @param x x-coordinate on the board
     * @param y y-coordinate on the board
     * @return True, iff coordinates are valid.
     */
    public boolean isValidCoordinate(int x, int y) {
        return (0 <= x && x < TOTAL_ROWS && 0 <= y && y < TOTAL_COLUMNS);
    }

    /**
     * Checks if given piece is a valid piece.
     * @param piece piece to check
     * @return True, iff piece is valid.
     */
    public boolean isValidPiece(char piece) {
        return (piece == ALLOWED_PIECES[0] || piece == ALLOWED_PIECES[1]);
    }

    public boolean isValidMove(int x, int y, char piece) {
        return (isValidCoordinate(x, y) && backendBoard[x][y]==EMPTY_PIECE);
    }

    /**
     * Places a piece and checks for winning condition.
     * @param x x-coordinate on the board
     * @param y y-coordinate on the board
     * @param piece the piece to be place
     */
    public void placePiece(int x, int y, char piece) {
        if (isValidCoordinate(x, y) && isValidPiece(piece)) {
            backendBoard[x][y] = piece;
        }
        else {
            throw new IllegalArgumentException("Illegal coordinates or piece: (" + x + ',' + y + "), " + piece);
        }
    }

    /**
     * Searches for a winning pattern, starting from some coordinates.
     * @param x x-coordinate to originate search from
     * @param y y-coordinate to originate search from
     * @param piece piece used for winning pattern search
     * @return True, iff a winning pattern is connected to the given coordinates.
     */
    public boolean isWin(int x, int y, char piece) {
        /*
        I know this is an ugly conditional...
        This is probably one of the nicer ways I could make it look.
         */
        return (
            getHorizontalLineLength(x, y, piece) >= WINNING_LINE_LENGTH
                ||
            getVerticalLineLength(x, y, piece) >= WINNING_LINE_LENGTH
                ||
            getForwardSlashLineLength(x, y, piece) >= WINNING_LINE_LENGTH
                ||
            getBackwardSlashLineLength(x, y, piece) >= WINNING_LINE_LENGTH
        );
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '—' pattern.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @return The length of the line.
     */
    public int getHorizontalLineLength(int x, int y, char piece) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            return getHorizontalLineLength(x-1, y, piece, true) + 1 + getHorizontalLineLength(x+1, y, piece, false);
        }
        return 0;
    }

    /**
     * This a helper method for the version of the method without parameter 'left'.
     * This version of the method only recursively calls in one direction; 'left' or 'right'.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @param left recursively call left?
     * @return The length of the line, left or right from the search origin.
     */
    public int getHorizontalLineLength(int x, int y, char piece, boolean left) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            if (left) {
                return getHorizontalLineLength(x-1, y, piece, true) + 1;
            }
            else {
                return 1 + getHorizontalLineLength(x+1, y, piece, false);
            }
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '|' pattern.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @return The length of the line.
     */
    public int getVerticalLineLength(int x, int y, char piece) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            return getVerticalLineLength(x, y-1, piece, true) + 1 + getVerticalLineLength(x, y+1, piece, false);
        }
        return 0;
    }

    /**
     * This is a helper method for the version of the method without parameter 'up'.
     * This version of the method only recursively calls in one direction; 'up' or 'down'.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @param up recursively call up?
     * @return The length of the line, up or down from the search origin
     */
    public int getVerticalLineLength(int x, int y, char piece, boolean up) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            if (up) {
                return getVerticalLineLength(x, y-1, piece, true) + 1;
            }
            else {
                return 1 + getVerticalLineLength(x, y+1, piece, false);
            }
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '\' pattern.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @return The length of the line.
     */
    public int getForwardSlashLineLength(int x, int y, char piece) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            return getForwardSlashLineLength(x-1, y-1, piece, true) + 1 + getForwardSlashLineLength(x+1, y+1, piece, false);
        }
        return 0;
    }

    /**
     * This is a helper method for the version of this method without parameter 'up'.
     * This version of the method only recursively  calls in one direction; 'up' or 'down'.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @param up recursively call up?
     * @return The length of the line, up or down from the search origin.
     */
    public int getForwardSlashLineLength(int x, int y, char piece, boolean up) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            if (up) {
                return getForwardSlashLineLength(x-1, y-1, piece, true) + 1;
            }
            else {
                return 1 + getForwardSlashLineLength(x+1, y+1, piece, false);
            }
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '/' pattern.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @return The length of the line.
     */
    public int getBackwardSlashLineLength(int x, int y, char piece) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            return getBackwardSlashLineLength(x-1, y+1, piece, true) + 1 + getBackwardSlashLineLength(x+1, y-1, piece, false);
        }
        return 0;
    }

    /**
     * This is a helper method for the version of this method without parameter 'down'.
     * This version of the method only recursively calls in one direction; 'down' or 'up'.
     * @param x x-coordinate being checked
     * @param y y-coordinate being checked
     * @param piece piece that is being checked
     * @param down recursively call down?
     * @return The length of the line, down or up from the search origin.
     */
    public int getBackwardSlashLineLength(int x, int y, char piece, boolean down) {
        if (isValidCoordinate(x, y) && backendBoard[x][y] == piece) {
            if (down) {
                return getBackwardSlashLineLength(x-1, y+1, piece, true) + 1;
            }
            else {
                return 1 + getBackwardSlashLineLength(x+1, y-1, piece, false);
            }
        }
        return 0;
    }
}
