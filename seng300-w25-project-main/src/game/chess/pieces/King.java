package game.chess.pieces;
import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.GameModel;
public class King extends ChessPiece {
    public boolean hasMoved;
    public King(int x, int y, long ownerID) {
        super( x, y, (ownerID == TEAM_1) ? KING_SOLID : KING_HOLLOW, ownerID );
        this.hasMoved = false;
    }
    @Override
    public boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake) {
        if (!game.isValidCoordinate(startRow, startCol) || !game.isValidCoordinate(endRow, endCol)) {
            return false;
        }
        ChessPiece piece = (ChessPiece) game.getPieceAt(startRow, startCol);
        if (piece == GameModel.NO_PIECE || piece.getOwnerID() != this.getOwnerID() || !(piece instanceof King)) {
            return false;
        }
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        boolean checkmate = false;
        if (targetPiece != GameModel.NO_PIECE || targetPiece != null || targetPiece instanceof King) {
            checkmate = true;
        }

        if (Math.abs(endRow - startRow) == 0 && Math.abs(endCol - startCol) == 0) {
            return false;
        }
        if (Math.abs(endRow - startRow) <= 1 && Math.abs(endCol - startCol) <= 1) {
            if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() == this.getOwnerID()) {
                return false;
            } else if (targetPiece != GameModel.NO_PIECE && targetPiece.getOwnerID() != this.getOwnerID()) {
                hasMoved = true;
                return super.capture(startRow, startCol, endRow, endCol, game, checkmate, testTake);
            } else {
                hasMoved = true;
                return true;
            }
        }
        return false;
    }
}
