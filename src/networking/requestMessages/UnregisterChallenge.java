package networking.requestMessages;

import match_making.MatchChallenge;

import java.io.Serializable;

/**
 * A record object for requests to deregister a challenge.
 * @param challenge The challenge being deregistered.
 */
public record UnregisterChallenge(MatchChallenge challenge) implements Serializable {
}
