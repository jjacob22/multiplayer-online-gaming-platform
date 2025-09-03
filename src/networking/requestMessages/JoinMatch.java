package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

/**
 * A record object for requests to join a match.
 * @param hostUsername Username of the player who is hosting the match.
 * @param game The game in which the match to join is.
 */
public record JoinMatch(String hostUsername, Game game) implements Serializable {

}
