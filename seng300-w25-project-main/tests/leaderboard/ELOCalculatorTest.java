package leaderboard;

import match_making.Game;

import networking.AbstractDatabaseManager;
import networking.Match;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import profile.Profile;

import static org.junit.jupiter.api.Assertions.*;


public class ELOCalculatorTest {
    private ELOCalculator eloCalculator;
    private GameStatistics player1;
    private GameStatistics player2;
    private MatchInfo matchInfo;
    private ProfileDatabaseManager db;
    private MatchDatabaseManager matchDB;

    Profile CoralReef1;
    Profile OceanWhisperer2;
    /**
     * Set up the test variables for ELOCalculator class
     */
    @BeforeEach
    void setUp() {
        // initialize ELOCalculator
        eloCalculator = new ELOCalculator();
        db = new ProfileDatabaseManager() {
            private Profile getProfileByID(int id){
                return switch (id){
                    case 1 -> CoralReef1;
                    case 2 -> OceanWhisperer2;
                    default -> null;
                };
            }
            @Override
            public String getPlayerUsername(int playerID){
                return switch (playerID) {
                    case 1 -> "CoralReef";
                    case 2 -> "OceanWhisperer";
                    default -> "UnknownPlayer";
                };
            }

            @Override
            public int[] getFriendList(int playerID) {
                if (getProfileByID(playerID) != null) {
                    return getProfileByID(playerID).getFriendIDs();
                }
                return new int[]{};
            }

            @Override
            public int addFriend(int playerID, int friendID) {
                getProfileByID(playerID).addFriend(friendID);
                return 1;
            }

            @Override
            public int removeFriend(int playerID, int friendID) {
                getProfileByID(playerID).removeFriend(friendID);
                return 1;
            }

            @Override
            public int getAvatar(int playerID) {
                return switch (playerID) {
                    case 1 -> 1;
                    case 2 -> 2;
                    case 3 -> 3;
                    case 4 -> 4;
                    case 5 -> 5;
                    default -> -1;
                };
            }

            @Override
            public int[] getHasBlocked(int playerID) {
                if (getProfileByID(playerID) != null) {
                    return getProfileByID(playerID).getBlockedIDs();
                }
                return new int[]{};
            }
            @Override
            public int[] getBlockedBy(int playerID){
                if (getProfileByID(playerID) != null) {
                    return getProfileByID(playerID).getBlockerIDs();
                }
                return new int[]{};
            }

            @Override
            public int addHasBlocked(int playerID, int blockedID) {
                getProfileByID(playerID).addBlocked(blockedID);
                return 1;
            }

            @Override
            public int removeHasBlocked(int playerID, int blockedID) {
                getProfileByID(playerID).removeBlocked(blockedID);
                return 1;
            }

            @Override
            public int addBlockedBy(int playerID, int blockerID){
                getProfileByID(playerID).addBlocker(blockerID);
                return 1;
            }

            @Override
            public int removeBlockedBy(int playerID, int blockerID){
                getProfileByID(playerID).removeBlocker(blockerID);
                return 1;
            }

            @Override
            public int[] getFriendsRequests(int playerID){
                if (getProfileByID(playerID) != null) {
                    return getProfileByID(playerID).getFriendRequests();
                }
                return new int[]{};
            }

            @Override
            public int addRequest(int playerID1, int playerID2) {
                getProfileByID(playerID1).addFriendRequest(playerID2);
                return 1;
            }

            @Override
            public int removeFriendRequest(int playerID1, int playerID2){
                getProfileByID(playerID1).removeFriendRequest(playerID2);
                return 1;
            }

            @Override
            public int addNotification(int playerID, String notification){
                return 1;
            }

            @Override
            public int removeNotification(int playerID, String notification){
                return 1;
            }
        };

        // initialize database managers
        matchDB = new MatchDatabaseManager(){

        };

        AbstractDatabaseManager.createNewProfile(1);
        AbstractDatabaseManager.createNewProfile(2);

        // create GameStats
        player1 = new GameStatistics(1, Game.Chess, matchDB, db);
        player2 = new GameStatistics(2, Game.Chess, matchDB, db);

        player1.setELO(1200);
        player2.setELO(1400);

        // initialize MatchInfo
        matchInfo = new MatchInfo(1001, Game.Chess);
    }



    @AfterEach
    void tearDown() {
        CoralReef1 = null;
        OceanWhisperer2 = null;
    }
    /**
     * Tests ELO changes when Player 1 wins against Player 2
     */
    @Test
    void testUpdateScores_Player1Wins() {
        matchInfo.setWinnerID(player1.getAccountID());  // Player 1 wins
        eloCalculator.updateScores(player1, player2, matchInfo);

        assertTrue(player1.getELO() > 1200, "Player 1's ELO should increase");
        assertTrue(player2.getELO() < 1400, "Player 2's ELO should decrease");
    }

    /**
     * Tests ELO changes when Player 2 wins against Player 1
     */
    @Test
    void testUpdateScores_Player2Wins() {
        matchInfo.setWinnerID(player2.getAccountID());  // Player 2 wins
        eloCalculator.updateScores(player1, player2, matchInfo);

        assertTrue(player1.getELO() < 1200, "Player 1's ELO should decrease");
        assertTrue(player2.getELO() > 1400, "Player 2's ELO should increase");
    }
    /**
     * Tests that passing null for player1 or player2 throws NullPointerException
     */
    @Test
    void testNullPlayerStats() {
        assertThrows(NullPointerException.class, () -> {
            eloCalculator.updateScores(null, player2, matchInfo);
        }, "Null player1 should throw NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            eloCalculator.updateScores(player1, null, matchInfo);
        }, "Null player2 should throw NullPointerException");
    }
    /**
     * Tests that passing null MatchInfo throws NullPointerException
     */
    @Test
    void testNullMatchInfo() {
        assertThrows(NullPointerException.class, () -> {
            eloCalculator.updateScores(player1, player2, null);
        }, "Null MatchInfo should throw NullPointerException");
    }
}
