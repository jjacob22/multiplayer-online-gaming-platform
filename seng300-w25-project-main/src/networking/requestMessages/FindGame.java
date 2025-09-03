package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

/**
 * A record object for requests to find a game via matchmaking.
 * @param game The game type that the player would like to play
 */
public record FindGame(Game game) implements Serializable {
}
