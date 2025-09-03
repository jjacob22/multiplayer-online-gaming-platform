package game.chess;

import game.GameModel;
import game.Piece;
import game.chess.pieces.*;

import java.util.Arrays;

public abstract class ChessPiece extends Piece {
    /*
    Example of declaring static characters for piece symbols (to be used in piece constructor).
     */
    public static final char KING_HOLLOW = '♔';
    public static final char QUEEN_HOLLOW = '♕';
    public static final char ROOK_HOLLOW = '♖';
    public static final char BISHOP_HOLLOW = '♗';
    public static final char KNIGHT_HOLLOW = '♘';
    public static final char PAWN_HOLLOW = '♙';

    public static final char KING_SOLID = '♚';
    public static final char QUEEN_SOLID = '♛';
    public static final char ROOK_SOLID = '♜';
    public static final char BISHOP_SOLID = '♝';
    public static final char KNIGHT_SOLID = '♞';
    public static final char PAWN_SOLID = '♟';

    /**
     * The ID of the team that should start at the bottom of the board and go first.
     */
    public static final long TEAM_1 = 1;
    /**
     * The ID of the team that should start at the top of the board and go second.
     */
    public static final long TEAM_2 = 2;

    public ChessPiece(int x, int y, char symbol, long ownerID) {
        super(x, y, symbol, ownerID);
    }

    public abstract boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake);

    //public abstract boolean move(int startRow, int startCol, int endRow, int endCol, ChessModel game);
    public boolean capture(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean checkmate, boolean testTake) {
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        if (checkmate) {
            if (targetPiece == GameModel.NO_PIECE || targetPiece.getOwnerID() == this.getOwnerID()) {
                return false;
            }
        }else{
            if (targetPiece == GameModel.NO_PIECE || targetPiece.getOwnerID() == this.getOwnerID() || targetPiece instanceof King) {
                return false;
            }
        }
        ChessPiece[] opponentPieces;
        if (TEAM_1 == targetPiece.getOwnerID()) {
            opponentPieces =  game.whitePieceArray;
        }else {
            opponentPieces = game.blackPieceArray;
        }
        for (int i = 0; i < opponentPieces.length; i++) {
            if (opponentPieces[i] == targetPiece) {
                if (opponentPieces[i].getRow() == endRow && opponentPieces[i].getCol() == endCol){
                    if (! testTake){
                        opponentPieces[i] = (ChessPiece) GameModel.NO_PIECE;
                    }
                }
            }
        }
        return true;
    }
    public boolean isClearPathInPiece(int fromRow, int fromCol, int toRow, int toCol, ChessModel game) {
        int deltaRow = toRow - fromRow;
        int deltaCol = toCol - fromCol;

        int rowStep = Integer.compare(deltaRow, 0);
        int colStep = Integer.compare(deltaCol, 0);

        boolean isDiagonal = Math.abs(deltaRow) == Math.abs(deltaCol);
        boolean isHorizontal = deltaRow == 0 && deltaCol != 0;
        boolean isVertical = deltaCol == 0 && deltaRow != 0;
        //This causes some tests to fail
        if (game.getPieceAt(fromRow, fromCol) instanceof Pawn) {
            Pawn pawn = (Pawn) game.getPieceAt( fromRow, fromCol );
            if (!pawn.isFirstMove()) {
                int direction;
                if (pawn.getOwnerID() == TEAM_1) {
                    direction = -1;
                } else {
                    direction = 1;
                }
                if (game.isValidCoordinate( fromRow + direction, fromCol + 1 )) {
                    if (game.getPieceAt( fromRow + direction, fromCol + 1 ) instanceof King) {
                        return true;
                    }
                }
                if (game.isValidCoordinate( fromRow + direction, fromCol - 1 )) {
                    if (game.getPieceAt( fromRow + direction, fromCol - 1 ) instanceof King) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        if (!(isDiagonal || isHorizontal || isVertical)) {
            return false;
        }

        int row = fromRow + rowStep;
        int col = fromCol + colStep;

        while (row != toRow || col != toCol) {
            ChessPiece piece = (ChessPiece) game.getPieceAt(row, col);
            if (piece != GameModel.NO_PIECE) {
                return false;
            }
            row += rowStep;
            col += colStep;
        }
        return true;
    }
    //Hashcode and equals for comparison when updating array after a prommotion
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + getClass().hashCode(); // Distinguish different types of pieces
        result = 31 * result + getRow();
        result = 31 * result + getCol();
        result = (int) (31 * result + getOwnerID());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece that = (ChessPiece) obj;
        return this.hashCode() == that.hashCode(); //check if they have the same hashcode
    }


}
