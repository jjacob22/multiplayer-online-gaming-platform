package game.chess.pieces;
import game.GameModel;
import game.chess.ChessModel;
import game.chess.ChessPiece;

import java.util.Arrays;

public class Knight extends ChessPiece {
    public Knight(int x, int y, long ownerID) {
        super(x, y, (ownerID == TEAM_1) ? KNIGHT_SOLID : KNIGHT_HOLLOW, ownerID);
    }
    @Override
    public boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake) {

        if (!game.isValidCoordinate(startRow, startCol) || !game.isValidCoordinate(endRow, endCol)) {
            return false;
        }
        ChessPiece piece = (ChessPiece) game.getPieceAt(startRow, startCol);
        if (piece == GameModel.NO_PIECE || piece.getOwnerID() != this.getOwnerID() || !(piece instanceof Knight)) {
            return false;
        }
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        boolean checkmate = false;
        if (targetPiece != GameModel.NO_PIECE || targetPiece != null || targetPiece instanceof King) {
            checkmate = true;
        }
        boolean validSpace = false;
        if (Math.abs(endCol - startCol) == 2){
            if (Math.abs(endRow - startRow) == 1){
                validSpace = true;
            }
        }
        if (Math.abs(endRow - startRow) == 2) {
            if (Math.abs(endCol - startCol) == 1) {
                validSpace = true;
            }
        }


        if (validSpace){
            if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() == this.getOwnerID()) {
                return false;
            }else if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() != this.getOwnerID()){
                return super.capture(startRow, startCol, endRow, endCol, game, checkmate, testTake);
            }else{
                return true;
            }
        }
        return false;
    }
}