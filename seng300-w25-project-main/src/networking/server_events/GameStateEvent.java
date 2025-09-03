package networking.server_events;

import game.Piece;
import networking.requestMessages.GameState;

import java.util.EventObject;

public class GameStateEvent extends EventObject {
    private GameState gameState; // An example of how we could store board state here

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public GameStateEvent(Object source, GameState gameState) {
        super(source);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
