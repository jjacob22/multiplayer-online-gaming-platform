package leaderboard;

import match_making.Game;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.function.ToIntFunction;

import static networking.AbstractDatabaseManager.getAllPlayerIDs;

public class Leaderboard implements Serializable {
    private final int accountID;
    private final ArrayList<GameStatistics> playerStats;

    /**
     * A constructor for the server side. Gets all necessary GameStatistics objects using the ID and Game provided.
     * @param accountID Globally unique identifier of a user account.
     * @param game Identifier for a game.
     * @param matchDB Statistics database to get data from.
     * @param profileDB Profile database to get data from.
     */
    public Leaderboard(int accountID, Game game, MatchDatabaseManager matchDB, ProfileDatabaseManager profileDB) {
        this.accountID = accountID;
        int[] players = getAllPlayerIDs();
        playerStats = new ArrayList<>();
        for (int player : players) {
            playerStats.add(new GameStatistics(player, game, matchDB, profileDB));
        }
        playerStats.sort(Comparator.comparingInt(GameStatistics::getELO).reversed());
        for (int i = 0; i < playerStats.size(); i++) {
            playerStats.get(i).setRank(i+1);
        }
    }

    /**
     * A constructor for the client side. Should be used when reassembling the class after sending over the network.
     * @param accountID Globally unique identifier of a user account.
     * @param list The list of GameStatistics objects that make up the leaderboard.
     */
    public Leaderboard(int accountID, ArrayList<GameStatistics> list) {
        this.accountID = accountID;
        playerStats = list;
    }

    // display related methods: sorting, filtering

    /**
     * Gets underlying list, sorted by wins.
     * @return A leaderboard to allow chaining.
     */
    public Leaderboard getSortedBy(boolean greatestToLeast, ToIntFunction<GameStatistics> sortBy) {
        if (greatestToLeast) {
            playerStats.sort(Comparator.comparingInt(sortBy).reversed());
        } else {
            playerStats.sort(Comparator.comparingInt(sortBy));
        }
        return new Leaderboard(accountID, playerStats);
    }

    /**
     * Gets a copy of the leaderboard to filter so it only include friends of the user.
     * @return A leaderboard so you can chain like,
     *
     * "l.getSortedBy().getSortedByFriends().getData()"
     * to just return an ArrayList of the players that fit the sort and filter.
     */
    public Leaderboard getFilteredByFriends() {
        ArrayList<GameStatistics> output = new ArrayList<>(playerStats);
        output.removeIf((stats) -> !stats.isFriend(accountID));
        return new Leaderboard(accountID, output);
    }

    /**
     *
     * @return A copy of the ArrayList of players that fit the sort and filters applied.
     */
    public ArrayList<GameStatistics> getData(){
        return playerStats;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (var stat : playerStats) {
            out.append(stat.toString()).append("\n");
        }
        return out.toString();
    }
}