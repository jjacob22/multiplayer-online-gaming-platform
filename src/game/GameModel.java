package game;

import game.chess.ChessPiece;
import game.chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class for modelling game data. Stores game data and methods for interacting with that data.
 */
public abstract class GameModel {
    /**
     * A variable representing what no piece in the board is defined as.
     */
    public static final Piece NO_PIECE = null;
    /**
     * A two-dimensional array of pieces which represents the game board
     */
    protected final Piece[][] board;
    /**
     * An array list of all the moves in that game
     */
    public static ArrayList<String> moveList = new ArrayList<String>();

    /**
     * Abstract game model constructor.
     * @param board the 2D array of pieces that this game model should work with.
     */
    public GameModel(Piece[][] board) {
//        System.out.println("gamemodel");
//        System.out.println(Arrays.deepToString(board));
//        System.out.println("game");
        this.board = board;
    }

    /**
     * Get the current state of the board from the model.
     * @return a <i>copy</i> of the board.
     */
    public Piece[][] getBoard() {
        return board.clone();
    }

    /**
     * Validates whether a pair of integers represent a valid position on the game board.
     * @param row row position
     * @param col column position
     * @return True, iff the coordinates are valid.
     */
    public boolean isValidCoordinate(int row, int col) {
        return (0 <= row && row < this.board.length && 0 <= col && col < this.board[row].length);
    }

    /**
     * Gets the piece located at a position on the board.
     * @param atRow row position
     * @param atCol column position
     * @return The piece located at (x, y).
     */
    public Piece getPieceAt(int atRow, int atCol) {
        if (!isValidCoordinate(atRow, atCol)) {
            throw new IllegalArgumentException("Invalid coordinates: " + atRow + ", " + atCol);
        }
        return board[atRow][atCol];
    }

    /**
     * Sets a piece to some location on the board.
     * @param atRow row position
     * @param atCol column position
     * @param piece the piece to set at the given location.
     */
    public void setPieceAt(int atRow, int atCol, Piece piece) {
//        System.out.println("insetpiece");
        if (!isValidCoordinate(atRow, atCol)) {
            throw new IllegalArgumentException("Invalid position: " + atRow + ", " + atCol);
        }
        board[atRow][atCol] = piece;
        if(piece!=null){ //I added null condition check to avoid system attempting to setRow for a null pointer
//            System.out.println("ooooooooooooo");
            piece.setRow(atRow);
            piece.setCol(atCol);
//            System.out.println(getPieceAt(piece.getRow(), piece.getCol()));
        }
//        System.out.println("setp");
//        System.out.println(Arrays.deepToString(board));
//        System.out.println("set p");


    }

    /**
     * Places a piece at some position on the board.
     * <p></p>
     * This is just a wrapper for the setPieceAt() method.
     * @param atRow row position
     * @param atCol column position
     * @param piece the piece to set at the given location.
     */
    public void placePiece(int atRow, int atCol, Piece piece) {
        setPieceAt(atRow, atCol, piece);
    }

    /**
     * Moves a piece from one location on the board to another.
     * @post The piece's previous location will be set to empty.
     * @param toRow row position
     * @param toCol column position
     * @param piece the piece to move
     */
    public boolean movePiece(int toRow, int toCol, Piece piece) {
        int oldRow = piece.getRow();
        int oldCol = piece.getCol();
        if (isValidCoordinate(toRow, toCol)) {
//            System.out.println("works");
            setPieceAt(toRow, toCol, piece);
            board[oldRow][oldCol] = NO_PIECE;
            return true;
        }
        return false;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '—' pattern.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @return The length of the line.
     */
    public int getHorizontalLineLength(int row, int col, Class<?> piece) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                return getHorizontalLineLength(row, col-1, piece, true) + 1 + getHorizontalLineLength(row, col+1, piece, false);
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * This a helper method for the version of the method without parameter 'left'.
     * This version of the method only recursively calls in one direction; 'left' or 'right'.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @param left recursively call left?
     * @return The length of the line, left or right from the search origin.
     */
    public int getHorizontalLineLength(int row, int col, Class<?> piece, boolean left) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                if (left) {
                    return getHorizontalLineLength(row, col-1, piece, true) + 1;
                }
                else {
                    return 1 + getHorizontalLineLength(row, col+1, piece, false);
                }
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '|' pattern.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @return The length of the line.
     */
    public int getVerticalLineLength(int row, int col, Class<?> piece) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                return getVerticalLineLength(row-1, col, piece, true) + 1 + getVerticalLineLength(row+1, col, piece, false);
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * This is a helper method for the version of the method without parameter 'up'.
     * This version of the method only recursively calls in one direction; 'up' or 'down'.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @param up recursively call up?
     * @return The length of the line, up or down from the search origin
     */
    public int getVerticalLineLength(int row, int col, Class<?> piece, boolean up) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                if (up) {
                    return getVerticalLineLength(row-1, col, piece, true) + 1;
                }
                else {
                    return 1 + getVerticalLineLength(row+1, col, piece, false);
                }
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '/' pattern.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @return The length of the line.
     */
    public int getForwardSlashLineLength(int row, int col, Class<?> piece) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                return getForwardSlashLineLength(row-1, col+1, piece, true) + 1 + getForwardSlashLineLength(row+1, col-1, piece, false);
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * This is a helper method for the version of this method without parameter 'up'.
     * This version of the method only recursively  calls in one direction; 'up' or 'down'.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @param up recursively call up?
     * @return The length of the line, up or down from the search origin.
     */
    public int getForwardSlashLineLength(int row, int col, Class<?> piece, boolean up) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                if (up) {
                    return getForwardSlashLineLength(row-1, col+1, piece, true) + 1;
                }
                else {
                    return 1 + getForwardSlashLineLength(row+1, col-1, piece, false);
                }
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * Calculates the amount of consecutive pieces in a line, following the '\' pattern.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @return The length of the line.
     */
    public int getBackwardSlashLineLength(int row, int col, Class<?> piece) {
        try {
            if ( (getPieceAt(row, col)) !=null && getPieceAt(row, col).getClass() == piece) {
                return getBackwardSlashLineLength(row+1, col+1, piece, true) + 1 + getBackwardSlashLineLength(row-1, col-1, piece, false);
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }

    /**
     * This is a helper method for the version of this method without parameter 'down'.
     * This version of the method only recursively calls in one direction; 'down' or 'up'.
     * @param row row being checked
     * @param col column being checked
     * @param piece piece type that is being checked
     * @param down recursively call down?
     * @return The length of the line, down or up from the search origin.
     */
    public int getBackwardSlashLineLength(int row, int col, Class<?> piece, boolean down) {
        try {
            if ( (getPieceAt(row, col)) !=null &&getPieceAt(row, col).getClass() == piece) {
                if (down) {
                    return getBackwardSlashLineLength(row+1, col+1, piece, true) + 1;
                }
                else {
                    return 1 + getBackwardSlashLineLength(row-1, col-1, piece, false);
                }
            }
        } catch (IllegalArgumentException e) {
            return 0;
        }
        return 0;
    }
    public static ArrayList<String> getMoveList(){
        return moveList;
    }
    public static void updateMoveList(int toRow, int toCol, Piece piece, Piece targetPiece){
        String move = getMove(toRow, toCol, piece, targetPiece);
        moveList.add(move);
    }

    public static String getMove(int toRow, int toCol, Piece piece, Piece targetPiece){
        //format (piece)(where piece came from)(if piece killed)(where piece is going to)
        String output = null;
        int startRow = piece.getRow();
        int startCol = piece.getCol();
        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<String> columns = new ArrayList<String>();
        int castle = Math.abs(startCol - toCol);
        for (int i = 1; i < 9; i++) {
            rows.add(i);
        }
        columns.add(String.valueOf('a'));
        columns.add(String.valueOf('b'));
        columns.add(String.valueOf('c'));
        columns.add(String.valueOf('d'));
        columns.add(String.valueOf('e'));
        columns.add(String.valueOf('f'));
        columns.add(String.valueOf('g'));
        columns.add(String.valueOf('h'));
        if ((piece instanceof King) && castle == 2){
            if (rows.get(toCol) == 'c') {
                output = "O-O-O_";
            }else{
                output = "O-O___";
            }
            return output;
        }
        if (piece instanceof Bishop){
            output = output + "B";
        } else if (piece instanceof King) {
            output = output + "K";
        } else if (piece instanceof Knight) {
            output = output + "N";
        } else if (piece instanceof Pawn){
            output = output + "P";
        } else if (piece instanceof Queen) {
            output = output + "Q";
        } else if (piece instanceof Rook) {
            output = output + "R";
        }
        output = output + rows.get(startRow) + rows.get(startCol);
        if (targetPiece != NO_PIECE && targetPiece.getOwnerID() != piece.getOwnerID()){
            output = output + "x";
        }else {
            output = output + "o";
        }
        output = output + rows.get(toRow) + rows.get(toCol);
        return output;
    }
}
