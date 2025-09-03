package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

/**
 * A record object for requests to "unhost" a match.
 * @param game The game for which the match should be unhosted.
 */
public record UnhostMatch(Game game) implements Serializable {
}
