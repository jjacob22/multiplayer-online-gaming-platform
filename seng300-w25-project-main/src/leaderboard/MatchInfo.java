package leaderboard;

import match_making.Game;

/**
 * Stores information to create a match and calculate statistics from, after a match is complete.
 */
public class MatchInfo {
    private final Game game;
    private final int player1ID;
    private final int player2ID;
    private int winnerID;
    private int numberMoves;
    private long timePlayed;
    private String matchID;

    /**
     * Gets the system-wide ID of a game. For example Chess would have a separate ID from Checkers.
     * @return Globally unique identifier for a game.
     */
    public Game getGame() {
        return game;
    }

    public int getPlayer1ID() {
        return player1ID;
    }

    public int getPlayer2ID() {
        return player2ID;
    }

    public int getWinnerID() {
        return winnerID;
    }

    public int getNumberMoves() {
        return numberMoves;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setWinnerID(int winnerID) {
        this.winnerID = winnerID;
    }
    public void setNumberMoves(int numberMoves) {
        this.numberMoves = numberMoves;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    /**
     * Construct the info for a match that has not been completed.
     * @param player1ID Globally unique identifier of player one. Same as account identifier.
     * @param player2ID Globally unique identifier of player two. Same as account identifier.
     * @param game Game that this info is for.
     */
    public MatchInfo(int player1ID, int player2ID, Game game) {
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.game = game;
    }

    /**
     * Unimplemented stub currently. Construct from a database using a matchID.
     * @param matchID The ID the match being retrieved from the database.
     * @param game The game which was played in the match.
     */
    public MatchInfo(int matchID, Game game){
        this.game = game;
        player1ID = 1;
        player2ID = 2;
    }
    public MatchInfo(String matchID, networking.MatchDatabaseManager db, Game game) {
        this.matchID = matchID;
        this.game = game;
        int[] playersOfMatch = db.getPlayersOfMatch(matchID);
        this.player1ID = playersOfMatch[0];
        this.player2ID = playersOfMatch[1];
        this.winnerID = db.getWinningPlayerOfMatch(matchID);
        this.numberMoves = db.getTotalMovesOfMatch(matchID);
        this.timePlayed = db.getMatchTime(matchID);
    }
}
