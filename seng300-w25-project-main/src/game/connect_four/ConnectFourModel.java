package game.connect_four;

import game.GameModel;
import game.Piece;

public class ConnectFourModel extends GameModel {
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;

    /**
     * The constructor for a new model of Connect4
     */
    public ConnectFourModel() {
        super(new ConnectFourPiece[ROWS][COLUMNS]);
    }
    /**
     * This function checks if there is a win horizontally
     * @param piece requires the current piece to check and see if there is a win
     * @return a boolean value, true if there was a win and false otherwise.
     */
    protected boolean winRow(Piece piece) {
        //use to get if there is a win in this row, needs x and y coords
        if (getHorizontalLineLength( piece.getRow(), piece.getCol(), piece.getClass())==4){
            return true;
        }
        return false;
    }

    /**
     * This function checks for wins in a backslash diagonal pattern \.
     * @param piece requires a piece object to check for
     * @return a boolean value, true if there was a win and false otherwise.
     */
    protected boolean winBackwardDiagonal(Piece piece) {
        if(getBackwardSlashLineLength(piece.getRow(), piece.getCol(), piece.getClass())==4){
            return true;
        }
        return false;
    }

    /**
     * Check forward slash / win. Use getforwardLineLength method
     * @param piece
     * @return
     */
    protected boolean winforwardDiagonal(Piece piece) {
        if (getForwardSlashLineLength(piece.getRow(), piece.getCol(), piece.getClass())==4){
            return true;
        }
        return false;
    }
    //use getverticalLineLength from the gameModel to check for a win in the current column
    protected boolean winColumn(Piece piece) {
        if (getVerticalLineLength(piece.getRow(), piece.getCol(), piece.getClass())==4){
            return true;
        }
        return false;
    }
    // check the top row of that specific column. as long as it is null, the board is not full yet
    protected boolean checkFull( int column) {
        if(board[0][column]==null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * This function is used to determine if the board is completely full
     * @return boolean true if the board is full false otherwise.
     */
    protected boolean draw() {
        /*
        TODO: implement this method
         */
        for (int i = 0; i < COLUMNS; i++) {
            if (!checkFull(i)){    // if any column is not full, then that means we do not have a draw yet
                return false;
            }
        }
        return true;  // if we get here, means all columns were full and now in was detected and hence we have a
    }

    /**
     * This function the logic of placing a piece. Start at the bottom of the board and keep checking for a null spot.
     * once a place on that column is null, that means we can place a piece there.
     * Otherwise, we decrement to move up on the board. If we reach row 0 and nothing is null, we return -1 which means that this column is full
     * @param col takes in a column for the piece to go
     * @return int value, row that piece fell to
     */
    protected int dropPiece(int col){
        //The logic of playing a piece
        for(int i = ROWS-1; i >= 0; i--) { //rows-1 because we start from index 0
            if (board[i][col]==null){
                return i;       // piece was placed at this row i
            }
        }
        return -1; // return -1 if all rows for this column were not null(i.e. were full)
    }

    /**
     * This function is used for getting a view of the board, used in debugging.
     * @return String returns a string representation of the board
     */

}
