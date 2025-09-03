package networking.responseMessages;

import game.Piece;
import networking.requestMessages.GameState;

import java.io.Serializable;

/**
 * A record object for game state update response from the server
 *
 * @param gameState the current state of the board
 */
public record GameStateResponse(GameState gameState) implements Serializable {
}
