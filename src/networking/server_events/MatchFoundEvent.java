package networking.server_events;

import match_making.Game;
import networking.requestMessages.GameState;

import java.util.EventObject;

public class MatchFoundEvent extends EventObject {
    private GameState gameState;
    private Game game;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public MatchFoundEvent(Object source, GameState startingState, Game game) {
        super(source);
        this.gameState = startingState;
        this.game = game;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Game getGame() {
        return game;
    }
}
