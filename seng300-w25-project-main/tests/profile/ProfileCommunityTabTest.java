package profile;

import game.tic_tac_toe.pieces.O;
import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProfileCommunityTabTest {

    private ProfileDatabaseManager db;
    private ProfileCommunityTab profileCommunityTab;

    Profile CoralReef1;
    Profile OceanWhisperer2;
    Profile TideSurfer3;
    Profile DeepBlueWave4;
    Profile SeaweedBrain5;
    Profile PenguinDude6;

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
        profileCommunityTab = new ProfileCommunityTab(db);
        CoralReef1 = new Profile("CoralReef",1, new ArrayList<Integer>(Arrays.asList(2,3)), new ArrayList<Integer>(), new ArrayList<Integer>(List.of(4)), new ArrayList<Integer>(), db);
        OceanWhisperer2 = new Profile("OceanWhisperer",2,new ArrayList<Integer>(List.of(1)),new ArrayList<Integer>(List.of(5)), new ArrayList<Integer>(List.of(3)),  new ArrayList<Integer>(List.of(4)),db);
        TideSurfer3 = new Profile("TideSurfer",3,new ArrayList<Integer>(Arrays.asList(1,4,5)),new ArrayList<Integer>(), new ArrayList<Integer>(),  new ArrayList<Integer>(List.of(2)),db);
        DeepBlueWave4 = new Profile("DeepBlueWave",4,new ArrayList<Integer>(List.of(3)),new ArrayList<Integer>(), new ArrayList<Integer>(List.of(2)),  new ArrayList<Integer>(List.of(1)),db);
        SeaweedBrain5 = new Profile("SeaweedBrain",5,new ArrayList<Integer>(List.of(3)),new ArrayList<Integer>(Arrays.asList(1,4)), new ArrayList<Integer>(), new ArrayList<Integer>(),db);
        PenguinDude6 = new Profile("PenguinDude",6,new ArrayList<Integer>(),new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>(),db);
    }

    private Profile getProfileByID(int id){
        return switch (id){
            case 1 -> CoralReef1;
            case 2 -> OceanWhisperer2;
            case 3 -> TideSurfer3;
            case 4 -> DeepBlueWave4;
            case 5 -> SeaweedBrain5;
            case 6 -> PenguinDude6;
            default -> null;
        };
    }

    @AfterEach
    void tearDown() {
        CoralReef1 = null;
        OceanWhisperer2 = null;
        TideSurfer3 = null;
        DeepBlueWave4 = null;
        SeaweedBrain5 = null;
        PenguinDude6 = null;
    }


    /**
     * This tests the case where a friend request is sent successfully.
     */
    @Test
    void sendFriendRequest1() {
        assertTrue(profileCommunityTab.sendFriendRequest(6,1)); // make sure that the method returns true
        int[] expected = new int[]{6};    // there were no requests to begin with, so now there should just be the one from 5
        assertArrayEquals(db.getFriendsRequests(1),expected);   // make sure they're equal
    }

    /**
     * This tests the case where a friend request is sent unsuccessfully because they are already friends
     */
    @Test
    void sendFriendRequest2() {
        assertFalse(profileCommunityTab.sendFriendRequest(2,1));    // make sure method returns false if unsuccessful
        int[] expected = new int[]{};    // player 1 has no requests yet
        assertArrayEquals(db.getFriendsRequests(1),expected);   // the list of requests should be empty still
    }
    /**
     * This tests the case where a friend request is sent unsuccessfully because one of the users blocked the other
     */
    @Test
    void sendFriendRequest3() {
        assertFalse(profileCommunityTab.sendFriendRequest(4,1));    // make sure method returns false if unsuccessful
        int[] expected = new int[]{};    // player 1 should not have any new requests
        assertArrayEquals(db.getFriendsRequests(1),expected);   // the list of requests should be unchanged
    }
    /**
     * This tests the case where a friend request is sent unsuccessfully because it has already been requests
     */
    @Test
    void sendFriendRequest4() {
        assertFalse(profileCommunityTab.sendFriendRequest(5,2));    // make sure method returns false if unsuccessful
        int[] expected = new int[]{5};    // player 5 has already sent a request
        assertArrayEquals(db.getFriendsRequests(2),expected);   // the list of requests should be unchanged
    }

    /**
     * This tests the case where a friend request is unsuccessful because someone tries to friend themself
     */
    @Test
    void sendFriendRequest5() {
        assertFalse(profileCommunityTab.sendFriendRequest(1,1)); // make sure method returns false if unsuccessful
    }


    /**
     * Tests that a friend request can be accepted successfully
     * Makes sure that the method returns true, the request is dismissed, and each player is added to the other's friends
     */
    @Test
    void acceptFriendRequest1() {
        assertTrue(profileCommunityTab.acceptFriendRequest(2,5));

        int[] expected = new int[]{};   // there should now be no friend requests
        assertArrayEquals(expected, db.getFriendsRequests(2));

        expected = new int[]{1,5};  // Player 2's friends
        assertArrayEquals(expected,db.getFriendList(2));

        expected = new int[]{3,2};  // Player 5's friends
        assertArrayEquals(expected,db.getFriendList(5));
    }

    /**
     * Tests that a friend request cannot be accepted if there is not already a request existing
     */
    @Test
    void acceptFriendRequest2() {
        assertFalse(profileCommunityTab.acceptFriendRequest(5,2));

        int[] expected = new int[]{1};  // Player 2's friends (no player 5)
        assertArrayEquals(expected,db.getFriendList(2));

        expected = new int[]{3};  // Player 5's friends (no player 2)
        assertArrayEquals(expected,db.getFriendList(5));
    }

    /**
     * Tests a successful declination of a friend request
     * Ensures that the method returns true, the request is dismissed, and the two players have not been added as friends
     */
    @Test
    void declineFriendRequest1() {
        assertTrue(profileCommunityTab.declineFriendRequest(2,5));

        int[] expected = new int[]{};   // there should now be no friend requests
        assertArrayEquals(expected, db.getFriendsRequests(2));

        expected = new int[]{1};  // Player 2's friends should be unchanged
        assertArrayEquals(expected,db.getFriendList(2));

        expected = new int[]{3};  // Player 5's friends should be unchanged
        assertArrayEquals(expected,db.getFriendList(5));
    }

    /**
     * Tests an unsuccessful declination of a friend request because there was no request to be declined
     */
    @Test
    void declineFriendRequest2() {
        assertFalse(profileCommunityTab.declineFriendRequest(5, 2));
    }

    /**
     * This tests the case when the user tries to remove themselves as a friend
     */
    @Test
    void removeFriend1() {
        assertFalse(profileCommunityTab.removeFriend(1, 1));
    }

    /**
     * This tests the case when the user tries to remove users that aren't their friends from their friends list
     */
    @Test
    void removeFriend2() {
        assertFalse(profileCommunityTab.removeFriend(1, 5));
    }

    /**
     * This tests the case when the user tries to remove actual friends
     */
    @Test
    void removeFriend3() {
        // make sure the method returns true when successful
        assertTrue(profileCommunityTab.removeFriend(1, 2));

        // check that player 2 is removed from 1's friend list
        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("TideSurfer", 3);
        assertEquals(expected, profileCommunityTab.viewFriendsList(1));

        // now check that player 1 is also removed from 2's friend list
        expected = new HashMap<>();
        assertEquals(expected, profileCommunityTab.viewFriendsList(2));
    }



    /**
     * This tests the case when the user tries to remove a friend with an invalid id
     */
    @Test
    void removeFriend4(){
        assertFalse(profileCommunityTab.removeFriend(1,1234));
    }

    /**
     * This tests the case when the user has several friends and wants to see their friend list
     */
    @Test
    void viewFriendsList1() {
        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("CoralReef", 1);
        expected.put("DeepBlueWave", 4);
        expected.put("SeaweedBrain",5);
        assertEquals(expected, profileCommunityTab.viewFriendsList(3));
    }

    /**
     * This tests the case when the user has no friends and wants to see their friend list
     */
    @Test
    void viewFriendsList2() {
        HashMap<String, Integer> actual = profileCommunityTab.viewFriendsList(6);
        assertTrue(actual.isEmpty());
    }

    /**
     * This tests the case when we are trying to check Friends List for an invalid user
     */
    @Test
    void viewFriendsList3() {
        profileCommunityTab = new ProfileCommunityTab();    // we don't want a testing ProfileCommunityTab
        HashMap<String, Integer> actual = profileCommunityTab.viewFriendsList(1234);
        assertEquals(new HashMap<String, Integer>(), actual);
    }

    /**
     * This tests the case when the user is trying to block themselves
     */
    @Test
    void blockUser1() {
        assertFalse(profileCommunityTab.blockUser(1,1));
    }

    /**
     * This tests the case when the user is trying to block someone who is already blocked
     */
    @Test
    void blockUser2() {
        assertFalse(profileCommunityTab.blockUser(2, 3));
    }

    /**
     * This tests the case when the user is trying to block a user that isn't blocked yet
     */
    @Test
    void blockUser3() {
        assertTrue(profileCommunityTab.blockUser(5, 1));
    }


    /**
     * This tests the case when the user is trying to unblock someone who was not blocked
     */
    @Test
    void unBlockUser1() {
        assertFalse(profileCommunityTab.unBlockUser(1,2));
    }

    /**
     * This tests the case when the user is trying to unblock themselves
     */
    @Test
    void unBlockUser2() {
        assertFalse(profileCommunityTab.unBlockUser(1,1));
    }

    /**
     * This tests the case when the user is trying to unblock someone who was blocked
     */
    @Test
    void unBlockUser3() {
        assertTrue(profileCommunityTab.unBlockUser(2,3));
    }

    /**
     * This tests the case when the user (the blocker) does not exist
     */
    @Test
    void unBlockUser4() {
        assertFalse(profileCommunityTab.unBlockUser(1234, 2));
    }

    /**
     * This tests the case when the user (the one to be unblocked) does not exist
     */
    @Test
    void unBlockUser5() {
        assertFalse(profileCommunityTab.unBlockUser(2, 1234));
    }
    /**
     * This tests the case when the user only has 1 person on their blocked list
     */
    @Test
    void viewBlockedList1() {
        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("TideSurfer", 3);
        assertEquals(expected, profileCommunityTab.viewBlockedList(2));
    }

    /**
     * This tests the case when the user has no one on their blocked list
     */
    @Test
    void viewBlockedList2(){
        HashMap<String, Integer> expected = new HashMap<>();
        assertEquals(expected, profileCommunityTab.viewBlockedList(3));
    }

    /**
     * This tests the case when an invalid user tries to check the blocked list
     */
    @Test
    void viewBlockedList3(){
        profileCommunityTab = new ProfileCommunityTab();    // we don't want a testing ProfileCommunityTab
        HashMap<String, Integer> expected = new HashMap<>();
        assertEquals(expected, profileCommunityTab.viewBlockedList(1234));
    }

    /**
     * This tests the case where you try to view the blocked list of someone who doesn't have any blockees
     */
    @Test
    void viewBlockedList4(){
        HashMap<String, Integer> expected = new HashMap<>();
        assertEquals(expected, profileCommunityTab.viewBlockedList(3));
    }
}