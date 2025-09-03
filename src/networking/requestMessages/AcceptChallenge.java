package networking.requestMessages;

import match_making.Game;
import match_making.MatchChallenge;

import java.io.Serializable;

/**
 * A record object for accepting a challenge
 * @param challenge The challenge being accepted.
 */
public record AcceptChallenge(MatchChallenge challenge) implements Serializable {
}
