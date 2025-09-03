package networking;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import match_making.Game;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.*;

import networking.AbstractDatabaseManager;

public class ProfileDatabaseManagerTest {
    private final int testPlayerId1 = 111111111;
    private final int testPlayerId2 = 222222222;
    private final int fakePlayerID = 333333333;

    ProfileDatabaseManager manager = new ProfileDatabaseManager();
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        AbstractDatabaseManager.backupAndClearDatabase();
    }
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        AbstractDatabaseManager.restoreDatabaseBackupAndOverrideCurrent();
    }

    @BeforeEach
    void setUp() throws Exception {
        AbstractDatabaseManager.createNewProfile(111111111);
        AbstractDatabaseManager.createNewProfile(222222222);
    }
    @AfterEach
    void tearDown() throws Exception {
        AbstractDatabaseManager.clearDatabase();
    }

    @Test
    public void getAndSetPlayerUsername() {
        manager.setPlayerUsername(testPlayerId1,"Player1");
        assertEquals("Player1", manager.getPlayerUsername(testPlayerId1));
    }
    @Test
    public void defaultPlayerUsername() {
        assertNull(manager.getPlayerUsername(testPlayerId1));
    }
    @Test
    public void setUsernameWithNoPlayer() {
        assertEquals(-1, manager.setPlayerUsername(fakePlayerID, "NoPlayer"));
    }


    @Test
    public void getAndSetPlayerPass() throws Exception {
        manager.setPlayerPass(testPlayerId1,"Player1Pass");
        assertEquals("Player1Pass", manager.getPlayerPass(testPlayerId1));
    }
    @Test
    public void defaultPlayerPass() throws Exception {
        System.out.println(manager.getPlayerPass(testPlayerId1));
        assertNull(manager.getPlayerPass(testPlayerId1));
    }
    @Test
    public void setPassWithNoPlayer() {
        assertEquals(-1, manager.setPlayerPass(fakePlayerID, "NoPass"));
    }


    @Test
    public void getAndSetEmail() throws Exception {
        manager.setEmail(testPlayerId1,"Player1@gmail.com");
        assertEquals("Player1@gmail.com", manager.getEmail(testPlayerId1));
    }
    @Test
    public void defaultEmail() throws Exception {
        assertNull(manager.getEmail(testPlayerId1));
    }
    @Test
    public void setEmailWithNoPlayer() {
        assertEquals(-1, manager.setEmail(fakePlayerID, "NoEmail@gmail.com"));
    }


    @Test
    public void getAndSetBio() throws Exception {
        manager.setBio(testPlayerId1,"This is my bio");
        assertEquals("This is my bio", manager.getBio(testPlayerId1));
    }
    @Test
    public void defaultBio() throws Exception {
        assertNull(manager.getBio(testPlayerId1));
    }
    @Test
    public void setBioWithNoPlayer() {
        assertEquals(-1, manager.setBio(fakePlayerID, "No bio"));
    }

    @Test
    public void getAndSetAvatar() throws Exception {
        manager.setAvatar(testPlayerId1,1);
        assertEquals(1, manager.getAvatar(testPlayerId1));
    }
    @Test
    public void defaultAvatar() throws Exception {
        assertEquals(-1, manager.getAvatar(testPlayerId1));;
    }
    @Test
    public void setAvatarWithNoPlayer() {
        assertEquals(-1, manager.setAvatar(fakePlayerID, 1));
    }


    @Test
    public void getAndSetFriendsList() throws Exception {
        manager.setFriendList(testPlayerId1,new int[]{122334455,988776655});
        assertEquals(122334455, manager.getFriendList(testPlayerId1)[0]);
        assertEquals(988776655, manager.getFriendList(testPlayerId1)[1]);

    }
    @Test
    public void defaultFriendsList() throws Exception {
        assertEquals(0,manager.getFriendList(testPlayerId1).length);
    }

    @Test
    public void setFriendListWithNoPlayer() {
        assertEquals(-1,manager.setFriendList(fakePlayerID, new int[]{}));
    }

    @Test
    public void addFriendToFriendsList() {
        manager.setFriendList(testPlayerId1, new int[]{122334455});
        assertEquals(1, manager.getFriendList(testPlayerId1).length);

        manager.addFriend(testPlayerId1, 988776655);
        assertEquals(2, manager.getFriendList(testPlayerId1).length);
        assertEquals(988776655, manager.getFriendList(testPlayerId1)[1]);
        assertEquals(122334455, manager.getFriendList(testPlayerId1)[0]);
    }

    @Test
    public void removeFriendFromFriendsList() {
        manager.setFriendList(testPlayerId1, new int[]{122334455,988776655});
        assertEquals(2, manager.getFriendList(testPlayerId1).length);
        manager.removeFriend(testPlayerId1, 988776655);
        assertEquals(1, manager.getFriendList(testPlayerId1).length);
        assertEquals(122334455, manager.getFriendList(testPlayerId1)[0]);
    }

    @Test
    public void getAndSetSalt() throws Exception {
        manager.setPlayerSalt(testPlayerId1,"bc12");
        assertEquals("bc12", manager.getPlayerSalt(testPlayerId1));
    }
    @Test
    public void defaultSalt() throws Exception {
        assertNull(manager.getPlayerSalt(testPlayerId1));
    }
    @Test
    public void setSaltWithNoPlayer() {
        assertEquals(-1, manager.setPlayerSalt(fakePlayerID, "ac23"));
    }

    @Test
    public void getAndSetFriendsRequests() throws Exception {
        manager.setFriendsRequests(testPlayerId1,new int[]{122334455,988776655});
        assertEquals(122334455, manager.getFriendsRequests(testPlayerId1)[0]);
        assertEquals(988776655, manager.getFriendsRequests(testPlayerId1)[1]);

    }
    @Test
    public void defaultFriendsRequests() throws Exception {
        assertEquals(0,manager.getFriendsRequests(testPlayerId1).length);
    }

    @Test
    public void setFriendsRequestsWithNoPlayer() {
        assertEquals(-1,manager.setFriendsRequests(fakePlayerID, new int[]{}));
    }

    @Test
    public void addFriendToFriendsRequests() {
        manager.setFriendsRequests(testPlayerId1, new int[]{122334455});
        assertEquals(1, manager.getFriendsRequests(testPlayerId1).length);

        manager.addRequest(testPlayerId1, 988776655);
        assertEquals(2, manager.getFriendsRequests(testPlayerId1).length);
        assertEquals(988776655, manager.getFriendsRequests(testPlayerId1)[1]);
        assertEquals(122334455, manager.getFriendsRequests(testPlayerId1)[0]);
    }

    @Test
    public void removeFriendFromFriendsRequests() {
        manager.setFriendsRequests(testPlayerId1, new int[]{122334455,988776655});
        assertEquals(2, manager.getFriendsRequests(testPlayerId1).length);
        manager.removeFriendRequest(testPlayerId1, 988776655);
        assertEquals(1, manager.getFriendsRequests(testPlayerId1).length);
        assertEquals(122334455, manager.getFriendsRequests(testPlayerId1)[0]);
    }


    @Test
    public void getAndSetHasBlocked() throws Exception {
        manager.setHasBlocked(testPlayerId1,new int[]{122334455,988776655});
        assertEquals(122334455, manager.getHasBlocked(testPlayerId1)[0]);
        assertEquals(988776655, manager.getHasBlocked(testPlayerId1)[1]);

    }
    @Test
    public void defaultHasBlocked() throws Exception {
        assertEquals(0,manager.getHasBlocked(testPlayerId1).length);
    }

    @Test
    public void setHasBlockedWithNoPlayer() {
        assertEquals(-1,manager.setHasBlocked(fakePlayerID, new int[]{}));
    }

    @Test
    public void addToHasBlocked() {
        manager.setHasBlocked(testPlayerId1, new int[]{122334455});
        assertEquals(1, manager.getHasBlocked(testPlayerId1).length);

        manager.addHasBlocked(testPlayerId1, 988776655);
        assertEquals(2, manager.getHasBlocked(testPlayerId1).length);
        assertEquals(988776655, manager.getHasBlocked(testPlayerId1)[1]);
        assertEquals(122334455, manager.getHasBlocked(testPlayerId1)[0]);
    }

    @Test
    public void removeFromHasBlocked() {
        manager.setHasBlocked(testPlayerId1, new int[]{122334455,988776655});
        assertEquals(2, manager.getHasBlocked(testPlayerId1).length);
        manager.removeHasBlocked(testPlayerId1, 988776655);
        assertEquals(1, manager.getHasBlocked(testPlayerId1).length);
        assertEquals(122334455, manager.getHasBlocked(testPlayerId1)[0]);
    }




    @Test
    public void getAndSetBlockedBy() throws Exception {
        manager.setBlockedBy(testPlayerId1,new int[]{122334455,988776655});
        assertEquals(122334455, manager.getBlockedBy(testPlayerId1)[0]);
        assertEquals(988776655, manager.getBlockedBy(testPlayerId1)[1]);

    }
    @Test
    public void defaultBlockedBy() throws Exception {
        assertEquals(0,manager.getBlockedBy(testPlayerId1).length);
    }

    @Test
    public void setBlockedByWithNoPlayer() {
        assertEquals(-1,manager.setBlockedBy(fakePlayerID, new int[]{}));
    }

    @Test
    public void addToBlockedBy() {
        manager.setBlockedBy(testPlayerId1, new int[]{122334455});
        assertEquals(1, manager.getBlockedBy(testPlayerId1).length);

        manager.addBlockedBy(testPlayerId1, 988776655);
        assertEquals(2, manager.getBlockedBy(testPlayerId1).length);
        assertEquals(988776655, manager.getBlockedBy(testPlayerId1)[1]);
        assertEquals(122334455, manager.getBlockedBy(testPlayerId1)[0]);
    }

    @Test
    public void removeFromBlockedBy() {
        manager.setBlockedBy(testPlayerId1, new int[]{122334455,988776655});
        assertEquals(2, manager.getBlockedBy(testPlayerId1).length);
        manager.removeBlockedBy(testPlayerId1, 988776655);
        assertEquals(1, manager.getBlockedBy(testPlayerId1).length);
        assertEquals(122334455, manager.getBlockedBy(testPlayerId1)[0]);
    }

    @Test
    public void getAndSetNotifications() throws Exception {
        manager.setNotifications(testPlayerId1,new String[]{"notification1","notification2"});
        assertEquals("notification1", manager.getNotifications(testPlayerId1)[0]);
        assertEquals("notification2", manager.getNotifications(testPlayerId1)[1]);

    }
    @Test
    public void defaultNotifications() throws Exception {
        assertEquals(0,manager.getNotifications(testPlayerId1).length);
    }

    @Test
    public void setNotificationsWithNoPlayer() {
        assertEquals(-1,manager.setNotifications(fakePlayerID, new String[]{}));
    }

    @Test
    public void addNotifications() {
        manager.setNotifications(testPlayerId1, new String[]{"notification1"});
        assertEquals(1, manager.getNotifications(testPlayerId1).length);

        manager.addNotification(testPlayerId1, "notification2");
        assertEquals(2, manager.getNotifications(testPlayerId1).length);
        assertEquals("notification2", manager.getNotifications(testPlayerId1)[1]);
        assertEquals("notification1", manager.getNotifications(testPlayerId1)[0]);
    }

    @Test
    public void removeNotifications() {
        manager.setNotifications(testPlayerId1, new String[]{"notification1","notification2"});
        assertEquals(2, manager.getNotifications(testPlayerId1).length);
        manager.removeNotification(testPlayerId1, "notification2");
        assertEquals(1, manager.getNotifications(testPlayerId1).length);
        assertEquals("notification1", manager.getNotifications(testPlayerId1)[0]);
    }


}

