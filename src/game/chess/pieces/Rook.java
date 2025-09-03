package game.chess.pieces;
import game.GameModel;
import game.chess.ChessModel;
import game.chess.ChessPiece;
public class Rook extends ChessPiece {
    public boolean hasMoved;
    public Rook(int x, int y, long ownerID) {
        super(x, y, (ownerID == TEAM_1) ? ROOK_SOLID : ROOK_HOLLOW, ownerID);
        this.hasMoved = false;
    }
    @Override
    public boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake) {
        if (!game.isValidCoordinate(startRow, startCol) || !game.isValidCoordinate(endRow, endCol)) {
            return false;
        }
        ChessPiece piece = (ChessPiece) game.getPieceAt(startRow, startCol);
        if (piece == GameModel.NO_PIECE || piece.getOwnerID() != this.getOwnerID() || !(piece instanceof Rook)) {
            return false;
        }
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        boolean checkmate = false;
        if (targetPiece != GameModel.NO_PIECE || targetPiece != null || targetPiece instanceof King) {
            checkmate = true;
        }
        int deltaRow = Math.abs(endRow - startRow);
        int deltaCol = Math.abs(endCol - startCol);
        if ((deltaCol== 0 && deltaRow != 0) || (deltaCol!= 0 && deltaRow == 0)){
            //if ((deltaCol== 0 && deltaRow == 0) ){
            if (super.isClearPathInPiece(startRow, startCol, endRow, endCol, game)){
                if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() == this.getOwnerID()) {
                    return false;
                }else if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() != this.getOwnerID()) {
                    hasMoved = true;
                    return super.capture(startRow, startCol, endRow, endCol, game, checkmate, testTake);
                }else {
                    hasMoved = true;
                    return true;
                }
            }
        }
        return false;
    }

}