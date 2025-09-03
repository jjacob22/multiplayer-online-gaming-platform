package leaderboard;


import match_making.Game;
import networking.*;
import networking.MatchDatabaseManager;
import networking.MatchDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import profile.Profile;

import static org.junit.jupiter.api.Assertions.*;

public class GameStatisticsTest {
    private MatchDatabaseManager testDb;
    private ProfileDatabaseManager db;
    private GameStatistics stats;
    private final Integer accountID = 123;
    private final Game game = Game.Chess;

    Profile CoralReef1;

    @BeforeEach
    void setUp() {
        AbstractDatabaseManager.createNewProfile(123);
        db = new ProfileDatabaseManager() {
            private Profile getProfileByID(int id){
                return switch (id){
                    case 123 -> CoralReef1;
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

        testDb = new MatchDatabaseManager();
        stats = new GameStatistics(accountID, game, testDb, db);


    }

    @AfterEach
    void tearDown() {
        CoralReef1 = null;
    }

    @Test
    void testSetELOValid() {
        stats.setELO(1300);
        assertEquals(1300, stats.getELO());
    }

    @Test
    void testSetELONullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> stats.setELO(null));
    }

    @Test
    void testSetELONegativeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> stats.setELO(-1));
    }

    @Test
    void testSetELOWithoutMatchDbThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameStatistics(accountID, game, null, db)
        );
    }

    @Test
    void testSetELOWithoutProfDbThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new GameStatistics(accountID, game, testDb, null)
        );
    }

}
