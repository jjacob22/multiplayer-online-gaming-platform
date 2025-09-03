package game.chess.pieces;
import game.GameModel;
import game.chess.ChessModel;
import game.chess.ChessPiece;
public class Bishop extends ChessPiece {
    public Bishop(int x, int y, long ownerID) {
        super(x, y, (ownerID == TEAM_1) ? BISHOP_SOLID : BISHOP_HOLLOW, ownerID);
    }
    @Override
    public boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake) {
        if (!game.isValidCoordinate(startRow, startCol) || !game.isValidCoordinate(endRow, endCol)) {
            return false;
        }
        ChessPiece piece = (ChessPiece) game.getPieceAt(startRow, startCol);
        if (piece == GameModel.NO_PIECE || piece.getOwnerID() != this.getOwnerID() || !(piece instanceof Bishop)) {
            return false;
        }
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() == this.getOwnerID()) {
            return false;
        }
        boolean checkmate = false;
        if (targetPiece != GameModel.NO_PIECE || targetPiece != null || targetPiece instanceof King) {
            checkmate = true;
        }
        int deltaRow = Math.abs(endRow - startRow);
        int deltaCol = Math.abs(endCol - startCol);
        if (deltaRow != deltaCol) {
            return false;
        }
        if (super.isClearPathInPiece(startRow, startCol, endRow, endCol, game)){
            if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() == this.getOwnerID()) {
                return false;
            }else if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() != this.getOwnerID()){
                return super.capture(startRow, startCol, endRow, endCol, game, checkmate, testTake);
            }else{
                return true;
            }
        }
        return  false;
    }
}