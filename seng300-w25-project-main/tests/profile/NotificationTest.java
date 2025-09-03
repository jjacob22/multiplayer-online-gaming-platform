package profile;

import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private ProfileDatabaseManager db;

    Profile CoralReef1;
    Profile OceanWhisperer2;
    Profile TideSurfer3;

    Notification notification = new Notification(0,0);
    @BeforeEach
    void setUp() {
        db = new ProfileDatabaseManager() {

            @Override
            public String getPlayerUsername(int playerID){
                return switch (playerID) {
                    case 1 -> "CoralReef";
                    case 2 -> "OceanWhisperer";
                    case 3 -> "TideSurfer";
                    case 4 -> "DeepBlueWave";
                    case 5 -> "SeaweedBrain";
                    case 6 -> "PenguinDude";
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
        CoralReef1 = new Profile("CoralReef",1, new ArrayList<Integer>(Arrays.asList(2,3)), new ArrayList<Integer>(), new ArrayList<Integer>(List.of(4)), new ArrayList<Integer>(), db);
        OceanWhisperer2 = new Profile("OceanWhisperer",2,new ArrayList<Integer>(List.of(1)),new ArrayList<Integer>(List.of(5)), new ArrayList<Integer>(List.of(3)),  new ArrayList<Integer>(List.of(4)),db);
        TideSurfer3 = new Profile("TideSurfer",3,new ArrayList<Integer>(Arrays.asList(1,4,5)),new ArrayList<Integer>(), new ArrayList<Integer>(),  new ArrayList<Integer>(List.of(2)),db);


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
    void testToString() {
        Notification notification = new Notification(3,2, db);
        String expected = "OceanWhisperer wants to be your friend!";
        assertEquals(notification.toString(), expected);
    }
}