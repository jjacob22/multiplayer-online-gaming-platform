package game.tic_tac_toe;

import game.GameServerController;
import game.GameStatus;
import game.PlayerController;
import game.tic_tac_toe.pieces.O;
import game.tic_tac_toe.pieces.X;
import match_making.Game;
import networking.requestMessages.ForfeitMatch;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.requestMessages.PlacePiece;

import static game.tic_tac_toe.TicTacToeModel.TEAM_1;
import static game.tic_tac_toe.TicTacToeModel.TEAM_2;

public class TicTacToeServerController extends GameServerController {
    public static final int MAX_PLAYERS = 2;

    public TicTacToeServerController(int[] playerIDs) {
        super(new TicTacToeModel(), MAX_PLAYERS);

        gameType = Game.TicTacToe;

        for (int ID : playerIDs) {
            addPlayer( ID );
        }
    }
    public TicTacToeServerController() {
        super(new TicTacToeModel(), MAX_PLAYERS);
    }

    /**
     * Adds a new player to the game
     * @param playerID the ID of the player to add to the game
     */

    @Override
    public void addPlayer(int playerID) {

        // make sure that the game isn't full already
        if (players.size() >= MAX_PLAYERS) {
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
        players.removeIf(player -> player.getPlayerID() == playerID);
    }

    /**
     * Handles the input based on what the caller is trying to do in the game.
     * @param input a record object representing the input
     * @return the new GameState after handling whatever the input was
     */
    @Override
    public GameState handleInput(GameAction input) {
        // make sure that the GameStatus is PLAYING, otherwise no action can be taken
        if (status != GameStatus.PLAYING) {
            throw new IllegalStateException("The game is over!");
        }
        // take a different action depending on what the caller wants
        switch (input.action()) {
            // if they're trying to place a piece, then get the necessary information to pass to placePiece
            case PlacePiece action -> {
                var player = input.playerID();
                var row = action.row();
                var col = action.col();
                placePiece(player, row, col);
            }
            // if they want to forfeit, then pass the playerID to forfeitMatch
            case ForfeitMatch action_ -> {
                var player = input.playerID();
                forfeitMatch(player);
            }
            // the above are the only valid actions. If it's something else, throw an error.
            default -> throw new IllegalArgumentException("Invalid action " + input);
        }
        // if the board is full, then the game ends in a draw
        if (turnsPlayed >= 9 && status == GameStatus.PLAYING) {
            this.timeEnd = System.currentTimeMillis();
            status = GameStatus.DRAW;
        }
        return getGameState();
    }

    /**
     * An internal helper function used in handleInput that places the piece it's given.
     * It just updates the model after moving a piece
     * @param player is the player who is placing the piece
     * @param row is the row of where they want to place the piece
     * @param col is the column of where they want to place the piece
     */

    private void placePiece(Integer player, Integer row, Integer col) {

        // make sure that they are playing on their turn
        if (!player.equals(players.current().getPlayerID())) {
            throw new IllegalStateException("It is not your turn!");
        }

        // place a different piece depending on whose turn it is
        switch(players.current().getOwnerID()) {
            case TEAM_1:
                // place a piece for team 1 by adding it to the model
                model.placePiece(row, col, new X());
                break;
            case TEAM_2:
                // place a piece for team 2 by adding it to the model
                model.placePiece(row, col, new O());
                break;
            default:
                // This case should never be reached. It is just set as an emergency check.
                this.timeEnd = System.currentTimeMillis();
                status = GameStatus.DRAW;
                throw new IllegalStateException("Anomalous owner ID: " + players.current().getOwnerID());
        }
        TicTacToeModel m = (TicTacToeModel) model;  // cast to TicTacToeModel just to be safe

        // check if this move results in a win. If so, end the game and declare the winner
        if (m.isWin(row, col)) {
            this.timeEnd = System.currentTimeMillis();
            status = GameStatus.WIN;
        }
        // otherwise, switch to the other player's turn
        else {
            players.next();
            this.turnsPlayed++;
        }
    }

    /**
     * An internal helper function for handleInput
     * When one of the players wants to forfeit, it ends the game and declares the other player as a winner
     * @param player is the player who wants to forfeit
     */

    private void forfeitMatch(Integer player) {
        if (player.equals(players.current().getPlayerID())) {
            players.next();
        }
        this.timeEnd = System.currentTimeMillis();  // stop the timer for game time
        status = GameStatus.WIN;    // set the other player as winner
    }
}
