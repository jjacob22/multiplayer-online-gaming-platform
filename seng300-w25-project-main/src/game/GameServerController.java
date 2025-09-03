package game;

import leaderboard.MatchInfo;
import match_making.Game;
import game.tic_tac_toe.TicTacToePiece;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class for storing methods which interpret player input and updates the game data model accordingly.
 */
public abstract class GameServerController {
    /**
     * The data model of the game being played.
     */
    protected final GameModel model;
    /**
     * The players in the game.
     */
    protected final PlayerControllerList players;
    /**
     * When the game being run by this game server controller started.
     */
    protected final long timeStart;
    protected long timeEnd;
    /**
     * Enum representing the current status of the game.
     */
    protected GameStatus status;
    /**
     * The amount of turns played in this game.
     */
    protected int turnsPlayed;

    /**
     * Main constructor.
     * @param model data model of game being played
     * @param maxPlayers max players for the game being played
     */
    public GameServerController(GameModel model, int maxPlayers) {
        this.model = model;
        this.players = new PlayerControllerList(maxPlayers);
        this.turnsPlayed = 0;
        this.timeStart = System.currentTimeMillis();
        this.status = GameStatus.PLAYING;
        this.timeEnd = 0;
    }


    /**
     * Get a DEEP COPY of the game's current board state. Model's should not use this.
     * @return A two-dimensional array of pieces representing the game's current state.
     */
    public Piece[][] getBoard() {
        Piece[][] board = new Piece[model.board.length][model.board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Piece piece = model.board[i][j];
                if (piece != null) {
                    board[i][j] = new Piece(model.board[i][j]);
                } else {
                    board[i][j] = null;
                }
            }
        }
        return board;
    }

    /**
     * Get a DEEP COPY of the game's current player list state. Model's should not use this.
     * @return An array list of the current player list state.
     */
    public PlayerControllerList getPlayers() {
        return new PlayerControllerList(players);
    }

    /**
     * Get the current state of the game.
     * @return A record object of the game's current state.
     */
    public GameState getGameState() {
        Piece[][] board = getBoard();
        PlayerControllerList pcl = getPlayers();
        return new GameState(board, pcl, status);
    }

    /**
     * Get the IDs of all players in this game.
     * @return An array list of all player IDs.
     */
    public ArrayList<Integer> getPlayerIDs() {
        var out = new ArrayList<Integer>();
        for (var player : players) {
            out.add(player.getPlayerID());
        }
        return out;
    }

    /**
     * Get the ID of the current player.
     * <p>
     *     (whom should be taking their turn)
     * </p>
     * @return The player's ID.
     */
    @Deprecated
    public Integer getCurrentPlayerID() {
        return players.current().getPlayerID();
    }

    protected Game gameType;

    public MatchInfo getMatchInfo() {
        MatchInfo m = new MatchInfo(players.get(0).getPlayerID(), players.get(1).getPlayerID(), gameType);
        if(status == GameStatus.WIN){
            m.setWinnerID( players.current().getPlayerID() );
            m.setTimePlayed(this.timeEnd - this.timeStart);
            m.setNumberMoves( this.turnsPlayed );
        }
        else if(status == GameStatus.PLAYING){
            throw new IllegalStateException("Game ongoing");
        }
        return m;
    }
    /**
     * This method is responsible for handling input from the server.
     * @param input a record object representing the input
     * @return The game state after processing the input.
     */
    public abstract GameState handleInput(GameAction input);

    /**
     * This method is responsible for adding a player to the game.
     * @param playerID the ID of the player to add to the game
     */
    public abstract void addPlayer(int playerID);

    /**
     * This method is responsible for removing a player from the game.
     * <p>
     *     This method will probably not be implemented well and is discouraged from being used.
     * </p>
     * @param playerID the ID of the player to remove from the game
     */
    public abstract void removePlayer(int playerID);
}
