package networking;

import game.GameServerController;
import game.GameStatus;
import match_making.Game;
import networking.requestMessages.ForfeitMatch;
import networking.requestMessages.GameAction;
import networking.requestMessages.GameState;
import networking.responseMessages.ChatMessageResponse;
import networking.responseMessages.ExceptionResponse;
import networking.responseMessages.GameStateResponse;

import java.util.ArrayList;

public class Match {
    private GameServerController gameController;
    private ArrayList<ClientHandler> clients;
    private RecordEloCallback recordEloCallback;
    private Game game;

    /**
     * Match constructor
     *
     * @param gameController game server controller
     * @param clients arraylist of clients
     */
     Match(GameServerController gameController, ArrayList<ClientHandler> clients, RecordEloCallback recordEloCallback, Game game) {
        System.out.print("New match with ");
        for (var c : clients) {
            System.out.printf(c.clientID + ", ");
        }
        System.out.println("");
        this.gameController = gameController;
        this.clients = clients;
        this.game = game;
        for (var client : clients) {
            client.setMatch(this);
        }
        this.recordEloCallback = recordEloCallback;
    }

    /**
     * Make a move or place a piece in game
     *
     * @param action action of the player
     */
    protected void doAction(GameAction action) {
        try {
            var update = gameController.handleInput(action);
            for (var client : clients) {
                client.sendMessage(new GameStateResponse(update));
            }

            if (update.status() == GameStatus.WIN || update.status() == GameStatus.DRAW) {
                var matchInfo = gameController.getMatchInfo();
                // If the match is over, record stats and clean up
                recordEloCallback.matchOver(update, matchInfo);
                for (var client : clients) {
                    client.leaveMatch();
                }
                clients.clear();
            }
        } catch (Exception e) {
            for (var client : clients) {
                if (client.clientID == action.playerID()) {
                    client.sendMessage(new ExceptionResponse(e.getMessage()));
                }
            }
        }
    }

    /**
     * The method if a player forfeits a match
     * @param playerID ID of the player forfeiting
     */
    protected void forfeitMatch(int playerID) {
        doAction(new GameAction(new ForfeitMatch(), playerID));
    }

    /**
     * Send a chat message to the players in a game.
     * @param username The username of the player sending the message.
     * @param message The message being sent.
     */
    protected void sendInGameMessage(String username, String message) {
        for (var client : clients) {
            ChatMessageResponse chatMessageResponse = new ChatMessageResponse(username, message);
            client.sendMessage(chatMessageResponse);
        }
    }

    protected GameState getGameState() {
        return gameController.getGameState();
    }

    protected Game getGame() {
        return game;
    }
}
