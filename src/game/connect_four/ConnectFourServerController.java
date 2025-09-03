package game.connect_four;

import game.GameServerController;
import game.GameStatus;
import game.Piece;
import game.PlayerController;
import game.chess.ChessModel;
import game.connect_four.pieces.BlueCircle;
import game.connect_four.pieces.RedCircle;
import leaderboard.MatchInfo;
import match_making.Game;
import networking.requestMessages.ForfeitMatch;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.PlacePiece;

import static game.connect_four.ConnectFourPiece.TEAM_1;
import static game.tic_tac_toe.TicTacToeModel.TEAM_2;


public class ConnectFourServerController extends GameServerController {
    //This is recommended might change
    public ConnectFourServerController() {
        super(new ConnectFourModel(), 2);
    }
    public ConnectFourServerController(int[] arrayOfPlayers) {
        super(new ConnectFourModel(), 2);

        gameType = Game.ConnectFour;

        for (int arrayOfPlayer : arrayOfPlayers) {
            addPlayer( arrayOfPlayer );
        }
    }
    public ConnectFourServerController(ConnectFourModel connect4Model) {
        super(connect4Model, 2);
    }

    // Maybe rethink this implementation
    //TODO NETWORKING these are what we would like to pass back to ClientServer
    //TODO Piece[][] board, long playerID, int gameOver)

    /**
     * This function calls the functions from ConnectFourGameModel that are relevant to placing a piece
     * @param col takes an int that is the col of the board
     */
    private void placePiece(int col){
        ConnectFourModel model = (ConnectFourModel) this.model;
        //Logic for placing a red piece
        if(players.current().getOwnerID() == TEAM_1){
            if(!model.draw()) {
                if(!model.checkFull(col)) {
                    //Create a new piece if the column isn't full
                    int newRow = model.dropPiece(col);
                    RedCircle piece = new RedCircle( newRow, col );
                    model.placePiece(newRow, col, piece  );
                    if( checkWin(piece)){
                        this.timeEnd = System.currentTimeMillis();
                        status = GameStatus.WIN;
                    }
                    else{
                        this.turnsPlayed ++;
                        players.next();
                    }
                }
            }
            else{
                status = GameStatus.DRAW;
                this.timeEnd = System.currentTimeMillis();
            }

        }
        //Logic for placing a blue piece
        else{
            if(!model.draw()) {
                if (!model.checkFull( col )) {
                    int newRow = model.dropPiece( col );
                    BlueCircle piece = new BlueCircle( newRow, col );
                    model.placePiece(newRow, col, piece  );
                    if (checkWin( piece )) {
                        this.timeEnd = System.currentTimeMillis();
                        status = GameStatus.WIN;
                    } else {
                        this.turnsPlayed ++;
                        players.next();
                    }
                }
            }else{
                status = GameStatus.DRAW;
                this.timeEnd = System.currentTimeMillis();
            }
        }
    }

    /**
     * This function calls all the win check functions from the ConnectFourGameModel
     * @param piece takes in the newest piece to check for a win
     * @return boolean true or false;
     */
    private boolean checkWin(Piece piece){
        ConnectFourModel model = (ConnectFourModel) this.model;
        if(model.winRow( piece )){
            //set these to whatever win is
            //getCurrentPlayer().setGameOutcome( 1 );
            return true;
        } else if (model.winColumn( piece )) {
            return true;
        } else if (model.winforwardDiagonal( piece )) {
            return true;
        } else if (model.winBackwardDiagonal( piece )) {
            return true;
        }
        return false;
    }

    /**
     * Adds a new player to the game
     * @param playerID the ID of the player to add to the game
     */
    @Override
    public void addPlayer(int playerID) {
        // make sure that the game isn't full already
        if (players.size() == 2) {
            throw new IllegalStateException("No more players may join the game.");
        }
        // the number representing whatever team they're on
        int team;

        // assigns a team. The first player is 1, the second is 2
        if (players.size() % 2 == 0) {
            team = TEAM_1;
        } else {
            team = TEAM_2;
        }

        PlayerController player = new PlayerController(playerID, team);

        // double check that the players are two different people
        if (this.getPlayerIDs().contains(playerID)) {
            throw new IllegalStateException("Player already joined.");
        }

        // if all of the above was successful, then add the player to list of players
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

    @Override
    public GameState handleInput(GameAction input) {
        if (input.action() instanceof PlacePiece) {
            if(input.playerID() == players.current().getPlayerID()){
                placePiece(((PlacePiece) input.action()).col() );
            }
        } else if (input.action() instanceof ForfeitMatch) {
            if(input.playerID() == players.current().getPlayerID()){
                players.next();
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.WIN;
            }else{
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.WIN;
            }
        }
        return getGameState();
    }
}
