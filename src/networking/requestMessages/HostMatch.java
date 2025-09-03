package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

/**
 * A record object for requests to host a custom match.
 * @param game The type of game that the player would like to host
 */
public record HostMatch(Game game) implements Serializable {
}
