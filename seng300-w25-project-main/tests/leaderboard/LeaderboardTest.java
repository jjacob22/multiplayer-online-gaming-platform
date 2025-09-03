package leaderboard;

import match_making.Game;
import networking.AbstractDatabaseManager;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import profile.Profile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class LeaderboardTest {
    private MatchDatabaseManager testDb;
    private ProfileDatabaseManager db;
    private Game game;
    private final int accountID = 123;

    private GameStatistics player1;
    private GameStatistics player2;
    private GameStatistics player3;

    Profile CoralReef1;
    Profile OceanWhisperer2;
    Profile TideSurfer3;

    @BeforeEach
    void setUp() {
        testDb = new MatchDatabaseManager();
        db = new ProfileDatabaseManager() {
            @Override
            public String getPlayerUsername(int playerID){
                return switch (playerID) {
                    case 1 -> "CoralReef";
                    case 2 -> "OceanWhisperer";
                    case 3 -> "TideSurfer";
                    case 4 -> "DeepBlueWave";
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
        db = new ProfileDatabaseManager();
        game = Game.Chess;
        AbstractDatabaseManager.createNewProfile(1);
        AbstractDatabaseManager.createNewProfile(2);
        AbstractDatabaseManager.createNewProfile(3);
        AbstractDatabaseManager.createNewProfile(10);
        AbstractDatabaseManager.createNewProfile(11);

        player1 = new GameStatistics(1, game, testDb, db);
        player2 = new GameStatistics(2, game, testDb, db);
        player3 = new GameStatistics(3, game, testDb, db);

        player1.setELO(1000);
        player2.setELO(1200);
        player3.setELO(1100);
    }

    private Profile getProfileByID(int id){
        return switch (id){
            case 1 -> CoralReef1;
            case 2 -> OceanWhisperer2;
            case 3 -> TideSurfer3;
            default -> null;
        };
    }

    @AfterEach
    void tearDown() {
        CoralReef1 = null;
        OceanWhisperer2 = null;
        TideSurfer3 = null;
    }
    @Test
    void testClientConstructorStoresCorrectData() {
        ArrayList<GameStatistics> list = new ArrayList<>();
        list.add(player1);
        list.add(player2);

        Leaderboard leaderboard = new Leaderboard(accountID, list);
        ArrayList<GameStatistics> result = leaderboard.getData();

        assertEquals(2, result.size());
        assertEquals(player1, result.get(0));
        assertEquals(player2, result.get(1));
    }

    @Test
    void testGetSortedByDescendingELO() {
        ArrayList<GameStatistics> list = new ArrayList<>();
        list.add(player1);
        list.add(player2);
        list.add(player3);

        Leaderboard leaderboard = new Leaderboard(accountID, list);

        Leaderboard sorted = leaderboard.getSortedBy(false, GameStatistics::getELO);
        ArrayList<GameStatistics> result = sorted.getData();

        assertEquals(player2, result.get(0)); // 1200
        assertEquals(player3, result.get(1)); // 1100
        assertEquals(player1, result.get(2)); // 1000
    }

    @Test
    void testGetSortedByAscendingELO() {
        ArrayList<GameStatistics> list = new ArrayList<>();
        list.add(player2);
        list.add(player1);
        list.add(player3);

        Leaderboard leaderboard = new Leaderboard(accountID, list);

        Leaderboard sorted = leaderboard.getSortedBy(true, GameStatistics::getELO);
        ArrayList<GameStatistics> result = sorted.getData();

        assertEquals(player1, result.get(0)); // 1000
        assertEquals(player3, result.get(1)); // 1100
        assertEquals(player2, result.get(2)); // 1200
    }

    @Test
    void testGetFilteredByFriends() {
        // Simulate friendship behavior by overriding isFriend using an anonymous class
        GameStatistics friend = new GameStatistics(10, game, testDb, db) {
            @Override
            public boolean isFriend(int id) {
                return true;
            }
        };

        GameStatistics stranger = new GameStatistics(11, game, testDb, db) {
            @Override
            public boolean isFriend(int id) {
                return false;
            }
        };

        ArrayList<GameStatistics> list = new ArrayList<>();
        list.add(friend);
        list.add(stranger);

        Leaderboard leaderboard = new Leaderboard(accountID, list);
        Leaderboard filtered = leaderboard.getFilteredByFriends();

        ArrayList<GameStatistics> result = filtered.getData();

        assertEquals(1, result.size());
        assertEquals(friend, result.get(0));
    }

    @Test
    void testGetDateReturnsOriginalList() {
        ArrayList<GameStatistics> list = new ArrayList<>();
        list.add(player1);
        list.add(player2);

        Leaderboard leaderboard = new Leaderboard(accountID, list);
        assertEquals(list, leaderboard.getData());
    }
}
