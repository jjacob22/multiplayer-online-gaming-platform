package networking.responseMessages;

import match_making.Game;
import networking.requestMessages.GameState;
import networking.server_events.ServerEventListener;

import java.io.Serializable;

/**
 * Response for letting a client know that a match has been found.
 * @param GameState the starting state of the game
 */
public record MatchFoundResponse(GameState GameState, Game game) implements Serializable,Response {
}
