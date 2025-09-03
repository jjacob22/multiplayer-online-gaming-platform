package leaderboard;

import match_making.Game; // This isn't where I'd like it to be, perhaps we move this enum to networking?
import match_making.InvalidPlayerIDException;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static networking.AbstractDatabaseManager.playerExists;

public class GameStatistics implements Serializable {
    private final Integer accountID;
    private final String username;
    private final Integer wins;
    private Integer elo;
    private final Integer gamesPlayed;
    private final Integer timePlayed;
    private float winRate;
    private final ArrayList<Integer> friendIDs;
    private int rank;

    /**
     * Construct based on account ID and game. This should only be used on the server side since
     * an object constructed with these parameters requires access to the database.
     * @param accountID Globally unique identifier of a user's account.
     * @param game Identifier of a game.
     */
    public GameStatistics(Integer accountID, Game game, MatchDatabaseManager matchDB, ProfileDatabaseManager profileDB) {
        if (accountID == null) throw new IllegalArgumentException("accountID cannot be null");
        if (game == null) throw new IllegalArgumentException("game cannot be null");
        if (matchDB == null) throw new IllegalArgumentException("matchDB cannot be null");
        if (profileDB == null) throw new IllegalArgumentException("profileDB cannot be null");
        if (!playerExists(accountID))
            throw new InvalidPlayerIDException("Player ID " + accountID + " does not exist in the database");
        this.accountID = accountID;
        this.username = profileDB.getPlayerUsername(accountID);
        this.wins = matchDB.getGameWins(accountID, game);
        this.elo = matchDB.getPlayerELO(accountID, game);
        this.gamesPlayed = matchDB.getGamesPlayed(accountID, game);
        this.timePlayed = matchDB.getTimePlayed(accountID, game);
        this.friendIDs = new ArrayList<>();
        for (int ID : profileDB.getFriendList(accountID)) {
            this.friendIDs.add(ID);
        }
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public Integer getWins() {
        return wins;
    }

    /**
     * Check if the player with whom these statistics are associated is a friend of the other user.
     * @param otherUserID ID of the other user.
     * @return Whether the other user is the friend of the owner of this GameStatistics object.
     */
    public boolean isFriend(int otherUserID) {
        return friendIDs.contains(otherUserID);
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public Integer getTimePlayed() {
        return timePlayed;
    }

    public float getWinRate(){
        if (gamesPlayed > 0 ){
            winRate = ((float)wins) / ((float)gamesPlayed);
        }
        else{
            winRate = 0;
        }
        return winRate;
    }

    public float getLossRate(){
        return 1 - winRate;
    }


    public Integer getELO() {
        // get the players elo
        return elo;
    }

    /**
     *
     * @param newElo setting a new elo value for the player, throwing an exception for negative values.
     */
    public void setELO(Integer newElo) {
        // updates a players elo
        if (newElo == null || newElo < 0) {
            throw new IllegalArgumentException("ELO must be a non-negative.");
        }

        this.elo = newElo;  // Update the local field
    }

    public Integer getAccountID() {
        return accountID;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {
        return username + ", " + rank + ", " + wins + ", " + elo + ", " + gamesPlayed + ", " + timePlayed + ", " + winRate;
    }
}
