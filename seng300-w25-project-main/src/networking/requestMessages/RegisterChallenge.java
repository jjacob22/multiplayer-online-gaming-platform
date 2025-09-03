package networking.requestMessages;

import match_making.Game;
import match_making.MatchChallenge;

import java.io.Serializable;

/**
 * A record object for requests to register a challenge.
 * @param challengedUsername The player being challenged.
 * @param game The game in which the challenger is challenging another player.
 */
public record RegisterChallenge(String challengedUsername, Game game) implements Serializable {
}
