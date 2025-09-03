package game.chess;

import game.GameServerController;
import game.GameStatus;
import game.Piece;
import game.PlayerController;
import game.chess.pieces.*;
import match_making.Game;
import networking.requestMessages.*;

import java.util.Arrays;

import static game.GameModel.NO_PIECE;
import static game.GameModel.moveList;
import static game.connect_four.ConnectFourPiece.TEAM_1;
import static game.tic_tac_toe.TicTacToeModel.TEAM_2;

public class ChessServerController extends GameServerController {
    public static final int MAX_PLAYERS = 2;
    public static int numOfMovesMade = 0;
    public ChessServerController() {
        super(new ChessModel(), MAX_PLAYERS);
        ChessModel chessModel = (ChessModel) this.model;
        chessModel.initializePieces();
    }
    public ChessServerController(int[] arrayOfPlayers){
        super(new ChessModel(), MAX_PLAYERS);

        gameType = Game.Chess;

        for (int arrayOfPlayer : arrayOfPlayers) {
            addPlayer( arrayOfPlayer );
        }
        ChessModel chessModel = (ChessModel) this.model;
        chessModel.initializePieces();
    }
    // Only for Testing!
    public ChessServerController(ChessModel chessModel) {
        super(chessModel, MAX_PLAYERS);
    }


    @Override
    public void addPlayer(int playerID) {
        if (players.size() > 2) {
            throw new IllegalStateException("No more players may join the game.");
        }
        int team;
        if (players.size() % 2 == 0) {
            team = TEAM_1;
        } else {
            team = TEAM_2;
        }
        PlayerController player = new PlayerController(playerID, team);
        if (players.contains(player)) {
            throw new IllegalStateException("Player already joined.");
        }
        players.add(player);
    }

    @Override
    public void removePlayer(int playerID) {
        for (int i = 0; i < players.size() ; i++) {
            if(players.get(i).getPlayerID() == playerID){
                players.remove(i);
                break;
            }
        }
    }
private int findOtherPlayerIndex(){
    int index = players.indexOf( players.current() );
    if (index == 1) {
        index = 0;
    }
    else{
        index = 1;
    }
    return index;

}
@Override
    public GameState handleInput(GameAction input) {
        ChessModel chessModel = (ChessModel) this.model;


        if(input.action() instanceof OfferDraw){
            int index = findOtherPlayerIndex();
            if(players.current().getPlayerID() == input.playerID()){
                players.current().setWantToDraw(true);
                numOfMovesMade = moveList.size();
            }
            else{
                players.get( index ).setWantToDraw(true);
                numOfMovesMade = moveList.size();
            }
            if(players.current().getWantToDraw() &&  players.get( index ).getWantToDraw()){
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.DRAW;
                return getGameState();
            }
        }
        // Handle pawn promotion scenario
        if (chessModel.isAwaitingPromotion()) { // check if this model is awaiting a pawn promotion
            if (input.action() instanceof PromotePawn) {
                Pawn pawn = (Pawn) chessModel.getPieceAt(chessModel.getPromotingPawn().getRow(), (chessModel.getPromotingPawn().getCol())); //get the pawn
                if(pawn != NO_PIECE && pawn.isAwaitingPromotion()){
                    chessModel.promotePawn(((PromotePawn) input.action()).promotion()); //perform pawn promotion
                    //if promotion was correctly done, the pawn is no longer in the list and hence the isAwaitingPromotion is reset to false automatically once it scans through the arrays in model again
                    players.next(); // Proceed to the next player after promotion
                    this.turnsPlayed ++;
                }
            }
            // Block all other move-related actions while a pawn is awaiting promotion

            return getGameState();
        }
        // Ask about the playerID TURN BASED
        if(input.playerID() == players.current().getPlayerID()){

            if(!chessModel.amIInCheckmate(players.current().getOwnerID(), (ChessModel) model)) {

                if (input.action() instanceof MovePiece) {
                    Piece targetPiece = chessModel.getPieceAt(((MovePiece) input.action()).toRow(), ((MovePiece) input.action()).toCol());
                    Piece piece = chessModel.getPieceAt( ((MovePiece) input.action()).fromRow(),((MovePiece) input.action()).fromCol() );
                    if(chessModel.movePiece(((MovePiece) input.action()).toRow(),((MovePiece) input.action()).toCol(), piece ))
                    {
                        //Only switch turns once the promotion check is done
                        if(!chessModel.isAwaitingPromotion()){

                            players.next();
                            this.turnsPlayed ++;
                        }
                        //update movelist either way because the move has been done
                        game.GameModel.updateMoveList(((MovePiece) input.action()).toRow(), ((MovePiece) input.action()).toCol(), piece, targetPiece);

                    }else{
                        System.out.println("Player " + players.current().getPlayerID() + " tried to move  and didnt");

                        return getGameState();
                    }

                }

            }else {
                players.next();
                timeEnd = System.currentTimeMillis();
                status = GameStatus.WIN;
            }
        }
        if (input.action() instanceof ForfeitMatch) {
            if(input.playerID() == players.current().getPlayerID()){
                players.next();
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.WIN;
            }else{
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.WIN;
            }
        }
        if( numOfMovesMade == moveList.size() - 2){
            players.current().setWantToDraw(false);
            int index = findOtherPlayerIndex();
            players.get( index ).setWantToDraw(false);

        }

        return getGameState();
    }
}
