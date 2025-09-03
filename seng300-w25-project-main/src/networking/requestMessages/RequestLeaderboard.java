package networking.requestMessages;

import match_making.Game;

import java.io.Serializable;

/**
 * A record object for requests to view leaderboard.
 */
public record RequestLeaderboard(Game game) implements Serializable {
}
