package networking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import networking.AbstractDatabaseManager;

public class AbstractDatabaseManagerTest {
    private final int testPlayerId1 = 111111111;
    private final int testPlayerId2 = 222222222;

    @BeforeAll
    public static void setUpBeforeAllTest() throws Exception {
        AbstractDatabaseManager.backupAndClearDatabase();
    }

    @AfterAll
    public static void tearDownAfterAllTest() throws Exception {
        AbstractDatabaseManager.restoreDatabaseBackupAndOverrideCurrent();
    }

    @BeforeEach
    public void setUpBeforeEachTest() throws Exception {
        AbstractDatabaseManager.clearDatabase();
    }

    @Test
    public void playerExistsWithNoPlayers(){
        assertFalse(AbstractDatabaseManager.playerExists(testPlayerId1));
        assertFalse(AbstractDatabaseManager.playerExists(testPlayerId2));
    }

    @Test
    public void createNewProfile() {
        AbstractDatabaseManager.createNewProfile(testPlayerId1);
        assertTrue(AbstractDatabaseManager.playerExists(testPlayerId1));
    }

    @Test
    public void playerExistsWithWrongID() {
        AbstractDatabaseManager.createNewProfile(testPlayerId2);
        assertFalse(AbstractDatabaseManager.playerExists(testPlayerId1));
        assertTrue(AbstractDatabaseManager.playerExists(testPlayerId2));
    }

    @Test
    public void getAllPlayersWithNoPlayers() {
        assertEquals(0, AbstractDatabaseManager.getAllPlayerIDs().length);
    }

    @Test
    public void getAllPlayers() {
        AbstractDatabaseManager.createNewProfile(testPlayerId1);
        AbstractDatabaseManager.createNewProfile(testPlayerId2);
        int[] playerIDs = AbstractDatabaseManager.getAllPlayerIDs();
        assertEquals(2, playerIDs.length);
        assertEquals(testPlayerId1, playerIDs[0]);
        assertEquals(testPlayerId2, playerIDs[1]);
    }

    @Test
    public void removePlayerWithNoPlayers() {
        int result = AbstractDatabaseManager.removePlayerProfile(testPlayerId1);
        assertEquals(-1, result);
    }

    @Test
    public void removePlayer() {
        AbstractDatabaseManager.createNewProfile(testPlayerId1);
        AbstractDatabaseManager.createNewProfile(testPlayerId2);
        assertTrue(AbstractDatabaseManager.playerExists(testPlayerId1));
        assertTrue(AbstractDatabaseManager.playerExists(testPlayerId2));
        assertEquals(2, AbstractDatabaseManager.getAllPlayerIDs().length);

        AbstractDatabaseManager.removePlayerProfile(testPlayerId1);

        assertFalse(AbstractDatabaseManager.playerExists(testPlayerId1));
        assertTrue(AbstractDatabaseManager.playerExists(testPlayerId2));
        assertEquals(1, AbstractDatabaseManager.getAllPlayerIDs().length);
    }

    @Test
    public void clearDatabase() {
        AbstractDatabaseManager.createNewProfile(testPlayerId1);
        AbstractDatabaseManager.clearDatabase();
        assertFalse(AbstractDatabaseManager.playerExists(testPlayerId1));
    }




}
