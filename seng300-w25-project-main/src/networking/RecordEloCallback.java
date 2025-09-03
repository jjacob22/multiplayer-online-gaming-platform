package networking;

import leaderboard.MatchInfo;
import networking.requestMessages.GameState;

interface RecordEloCallback {
    void matchOver(GameState state, MatchInfo matchInfo);
}
