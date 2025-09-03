package game.chess.pieces;

import game.GameModel;
import game.Piece;
import game.chess.ChessModel;
import game.chess.ChessPiece;
import game.chess.ChessServerController;
import networking.requestMessages.PromotePawn;

import java.util.ArrayList;
import java.util.function.DoubleToIntFunction;

public class Pawn extends ChessPiece {
    private boolean awaitingPromotion = false;   //awaiting promotion boolean to help determine when the game needs to wait for it to occur before proceeding

    public Pawn(int x, int y, long ownerID) {
        super(x, y, (ownerID == TEAM_1) ? PAWN_SOLID : PAWN_HOLLOW, ownerID);
    }

    public boolean isFirstMove() {
        return switch ((int) getOwnerID()) {
            //TEAM 1 is white
            //TEAM 2 is black
            case (int) TEAM_1 -> getRow() == 6;
            case (int) TEAM_2 -> getRow() == 1;
            default -> false;
        };
    }

    @Override
    public boolean isValid(int startRow, int startCol, int endRow, int endCol, ChessModel game, boolean testTake) {
        if (!game.isValidCoordinate(startRow, startCol) || !game.isValidCoordinate(endRow, endCol)) {
            return false;
        }
        ChessPiece targetPiece = (ChessPiece) game.getPieceAt(endRow, endCol);
        ChessPiece piece = (ChessPiece) game.getPieceAt(startRow, startCol);
        //Talk about this
        if (piece == null || piece.getOwnerID() != this.getOwnerID() || !(piece instanceof Pawn)) {
            return false;
        }
        boolean checkmate = false;
        if ((targetPiece != GameModel.NO_PIECE || targetPiece != null) && targetPiece instanceof King) {
            checkmate = true;
        }

        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;

        if (deltaCol != 0) {
            return isValidAttackPattern( startRow, startCol, endRow, endCol, targetPiece, game, checkmate, testTake);
        } else {
            if(game.getPieceAt((piece.getRow()+getRankAdvancingDirection()), startCol)== GameModel.NO_PIECE){
                int rankAdvancingDirection = getRankAdvancingDirection();
                int rankAdvancingDisplacement = deltaRow * rankAdvancingDirection;

                if (isFirstMove()) {
                    if(rankAdvancingDisplacement ==1){
                        return true;
                    }else if(targetPiece == GameModel.NO_PIECE){
                        return (rankAdvancingDisplacement == 2);
                    }else{
                        return false;

                    }                }
                return (rankAdvancingDisplacement == 1);
            } else{
                return false;
            }
        }
    }

    public boolean isValidAttackPattern(int startRow, int startCol, int endRow, int endCol, Piece targetPiece, ChessModel game, boolean checkmate, boolean testTake) {
        int deltaRow = endRow - startRow;
        int deltaCol = endCol - startCol;
        int rankAdvancingDirection = getRankAdvancingDirection();
        int rankAdvancingDisplacement = deltaRow * rankAdvancingDirection;
        int colChangingDistance = Math.abs(deltaCol);
        if (rankAdvancingDisplacement == 1 && colChangingDistance == 1) {
            if (targetPiece == GameModel.NO_PIECE || targetPiece ==null) {
                ChessPiece rightPiece = (ChessPiece) GameModel.NO_PIECE;
                ChessPiece leftPiece = (ChessPiece) GameModel.NO_PIECE;
                if(game.isValidCoordinate( startRow, startCol+1 )){
                    rightPiece = (ChessPiece) game.getPieceAt(startRow, startCol+1);

                }
               if(game.isValidCoordinate( startRow, startCol-1 )){
                   leftPiece = (ChessPiece) game.getPieceAt(startRow, startCol-1);

               }
                //get the square
                boolean isLeft = false;
                ChessPiece sidePiece;
                if(leftPiece != GameModel.NO_PIECE){
                    sidePiece = leftPiece;

                } else if (rightPiece != GameModel.NO_PIECE) {
                    sidePiece = rightPiece;

                }
                else{
                    return false;
                }
                if(sidePiece.getOwnerID() != this.getOwnerID() && sidePiece instanceof Pawn){
                    ArrayList<String> moveList = GameModel.getMoveList();
                   // if (moveList.size() > 3){
                        String mostRecentMove = moveList.getLast();
                        if (mostRecentMove.indexOf("P") == 0 && mostRecentMove.indexOf("o") == 3){
                            ChessPiece[] opponentPieces;
                            if (TEAM_1 == sidePiece.getOwnerID()) {
                                opponentPieces =  game.whitePieceArray;
                            }else {
                                opponentPieces = game.blackPieceArray;
                            }
                            for (int i = 0; i < opponentPieces.length; i++) {
                                if (opponentPieces[i] == targetPiece) {
                                    if (opponentPieces[i].getRow() == endRow && opponentPieces[i].getCol() == endCol){
                                        opponentPieces[i] = (ChessPiece) GameModel.NO_PIECE;
                                    }
                                }
                            }
                            return true;
                        }else {

                            return false;
                        }
                }
            }
            else{
                if(targetPiece.getOwnerID() != this.getOwnerID()) {
                    return super.capture( startRow,startCol,endRow,endCol, game, checkmate, testTake);
                }
            }
        }
        return false;
    }

    private int getRankAdvancingDirection() {
        return switch ((int) getOwnerID()) {
            case (int) ChessPiece.TEAM_1 -> -1;
            case (int) ChessPiece.TEAM_2 -> 1;
            default -> 0;
        };
    }

    //choose which piece they want
    private ChessPiece choosePromotionPiece(PromotePawn.Promotion promotion){
        //promotion is now handled according to which piece the gui gives
        //choose a piece
        return switch (promotion) {
            case PromotePawn.Promotion.Queen -> new Queen(this.getRow(), this.getCol(), this.getOwnerID());
            case PromotePawn.Promotion.Bishop -> new Bishop(this.getRow(), this.getCol(), this.getOwnerID());
            case PromotePawn.Promotion.Rook -> new Rook(this.getRow(), this.getCol(), this.getOwnerID());
            case PromotePawn.Promotion.Knight-> new Knight(this.getRow(), this.getCol(), this.getOwnerID());
            default -> throw new IllegalArgumentException("Invalid piece type for promotion");
        };
    }
    public void promote(ChessModel model,int row,int col, PromotePawn.Promotion promotion) {
        //depending on what they want to promote to, call the helper method to choose a piece
        try{
            ChessPiece promotionPiece = choosePromotionPiece(promotion);
            model.setPieceAt(row,col, promotionPiece); //replace the piece
        }catch (Exception e){
            System.out.println("Could not update piece"+e.getMessage());
        }

    }

    // Helper method to determine if the pawn is on its promotion row
    public boolean isPromotionRow(int row){
        //Team1 promotes if they reach the top row (0) and row 7 for team 2
        return(getOwnerID()==TEAM_1&& row==0)||(getOwnerID()==TEAM_2 && row==7);
    }
    // These are for the promotion flag
    public void setAwaitingPromotion(boolean awaiting) { //will set the flag in model to indicate this pawn is awaiting promotion which will be passed over to the controller so that no other moves can be done until promotion is complete
        this.awaitingPromotion = awaiting;
    }

    public boolean isAwaitingPromotion() {
        return this.awaitingPromotion; //the actual flag
    }


}
