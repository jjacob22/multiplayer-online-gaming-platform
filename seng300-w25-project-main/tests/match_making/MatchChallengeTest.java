package match_making;

import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import profile.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatchChallengeTest {
    private ProfileDatabaseManager db;
    private MatchChallenge challenge;

    Profile CoralReef1;
    Profile OceanWhisperer2;


    @BeforeEach
    void setUp() {
        db = new ProfileDatabaseManager() {

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

    }

    private Profile getProfileByID(int id){
        return switch (id){
            case 1 -> CoralReef1;
            case 2 -> OceanWhisperer2;
            default -> null;
        };
    }

    @AfterEach
    void tearDown() {
        CoralReef1 = null;
        OceanWhisperer2 = null;
    }

    @Test
    void testConstructorSetsCorrectFields() {
        challenge = new MatchChallenge(1, 2, db);

        assertEquals(1, challenge.getChallengerID(), "Challenger ID should be set correctly");
        assertEquals(2, challenge.getChallengedID(), "Challenged ID should be set correctly");
        assertEquals("CoralReef", challenge.getChallenger(), "Challenger name should match expected username");
        assertEquals("OceanWhisperer", challenge.getChallenged(), "Challenged name should match expected username");
        assertNotNull(challenge.getDate(), "Date should be initialized");
        assertNull(challenge.getGame(), "Game should be null unless set");
    }


    @Test
    void testEqualsReturnsFalseForDifferentChallengerID() {
        MatchChallenge challenge1 = new MatchChallenge(1, 2, db);
        MatchChallenge challenge2 = new MatchChallenge(3, 2, db);


        assertNotEquals(challenge1, challenge2, "Different challengerID should result in inequality");
    }

    @Test
    void testEqualsReturnsFalseForDifferentDate() {
        MatchChallenge challenge1 = new MatchChallenge(1, 2, db);
        MatchChallenge challenge2 = new MatchChallenge(1, 2, db);

        // Slight delay to make dates different
        try { Thread.sleep(5); } catch (InterruptedException ignored) {}
        challenge2 = new MatchChallenge(1, 2, db);

        assertNotEquals(challenge1, challenge2, "Different timestamps should result in inequality");
    }

    @Test
    void testEqualsWithNonMatchChallengeObject() {
        challenge = new MatchChallenge(1, 2, db);

        assertNotEquals(challenge, "Not a MatchChallenge", "Should return false when comparing to unrelated object type");
    }

    @Test
    void testEqualsWithNull() {
        challenge = new MatchChallenge(1, 2, db);

        assertNotEquals(challenge, null, "Should return false when compared with null");
    }

}
