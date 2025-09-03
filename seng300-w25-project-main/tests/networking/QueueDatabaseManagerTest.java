package networking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

import match_making.Game;
import networking.MatchDatabaseManager;
import networking.QueueDatabaseManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import java.util.Queue;

public class QueueDatabaseManagerTest {
    private final int testPlayerId1 = 111111111;
    private final int testPlayerId2 = 222222222;
    private final int fakePlayerID = 333333333;

    QueueDatabaseManager manager = new QueueDatabaseManager();

    @BeforeEach
    public void setUpClass() throws Exception {
        manager.clearQueue(Game.Chess);
        manager.clearQueue(Game.ConnectFour);
        manager.clearQueue(Game.TicTacToe);

        manager.clearAllHostIDs(Game.Chess);
        manager.clearAllHostIDs(Game.ConnectFour);
        manager.clearAllHostIDs(Game.TicTacToe);
    }

    @AfterEach
    void setUpBeforeClass() throws Exception {
        manager.clearQueue(Game.Chess);
        manager.clearQueue(Game.ConnectFour);
        manager.clearQueue(Game.TicTacToe);

        manager.clearAllHostIDs(Game.Chess);
        manager.clearAllHostIDs(Game.ConnectFour);
        manager.clearAllHostIDs(Game.TicTacToe);
    }

    @Test
    public void getAndAddAllPlayersInQueue(){
        manager.addPlayerToQueue(testPlayerId1, Game.Chess);
        manager.addPlayerToQueue(testPlayerId2, Game.Chess);
        int[] chessQueue = manager.getAllPlayersInQueue(Game.Chess);
        assertEquals(testPlayerId1, chessQueue[0]);
        assertEquals(testPlayerId2, chessQueue[1]);

        manager.addPlayerToQueue(testPlayerId1, Game.ConnectFour);
        manager.addPlayerToQueue(testPlayerId2, Game.ConnectFour);
        int[] connectFourQueue = manager.getAllPlayersInQueue(Game.ConnectFour);
        assertEquals(testPlayerId1, connectFourQueue[0]);
        assertEquals(testPlayerId2, connectFourQueue[1]);

        manager.addPlayerToQueue(testPlayerId1, Game.TicTacToe);
        manager.addPlayerToQueue(testPlayerId2, Game.TicTacToe);
        int[] ticTacToeQueue = manager.getAllPlayersInQueue(Game.TicTacToe);
        assertEquals(testPlayerId1, ticTacToeQueue[0]);
        assertEquals(testPlayerId2, ticTacToeQueue[1]);
    }

    @Test
    public void clearQueueTest(){
        manager.addPlayerToQueue(testPlayerId1, Game.Chess);
        assertEquals(1, manager.getAllPlayersInQueue(Game.Chess).length);
        manager.clearQueue(Game.Chess);
        assertEquals(0, manager.getAllPlayersInQueue(Game.Chess).length);

        manager.addPlayerToQueue(testPlayerId1, Game.ConnectFour);
        assertEquals(1, manager.getAllPlayersInQueue(Game.ConnectFour).length);
        manager.clearQueue(Game.ConnectFour);
        assertEquals(0, manager.getAllPlayersInQueue(Game.ConnectFour).length);

        manager.addPlayerToQueue(testPlayerId1, Game.TicTacToe);
        assertEquals(1, manager.getAllPlayersInQueue(Game.TicTacToe).length);
        manager.clearQueue(Game.TicTacToe);
        assertEquals(0, manager.getAllPlayersInQueue(Game.TicTacToe).length);
    }

    @Test
    public void getAndSetPlayerMatchmakingAttempt(){
        manager.addPlayerToQueue(testPlayerId1, Game.Chess);
        manager.setPlayerMatchmakingAttempts(testPlayerId1,Game.Chess,4);
        assertEquals(4,manager.getPlayerMatchmakingAttempts(testPlayerId1,Game.Chess));
    }

    @Test
    public void addMatchmakingAttemptTest(){
        manager.addPlayerToQueue(testPlayerId1, Game.Chess);
        manager.setPlayerMatchmakingAttempts(testPlayerId1,Game.Chess,4);
        manager.addPlayerMatchmakingAttempt(testPlayerId1,Game.Chess);
        assertEquals(5,manager.getPlayerMatchmakingAttempts(testPlayerId1,Game.Chess));
    }

    @Test
    public void getAndAddHostID(){
        assertEquals(0,manager.getAllHostIDs(Game.Chess).length);
        manager.addHostID(testPlayerId1, Game.Chess);

        int[] hostIDs = manager.getAllHostIDs(Game.Chess);
        assertEquals(1,hostIDs.length);
        assertEquals(testPlayerId1,hostIDs[0]);
    }

    @Test
    public void removeHostIDTest(){
        manager.addHostID(testPlayerId1, Game.Chess);
        manager.addHostID(testPlayerId2, Game.Chess);

        assertEquals(2,manager.getAllHostIDs(Game.Chess).length);

        manager.removeHostID(testPlayerId1, Game.Chess);

        assertEquals(1,manager.getAllHostIDs(Game.Chess).length);
        assertEquals(testPlayerId2,manager.getAllHostIDs(Game.Chess)[0]);
    }

    @Test
    public void clearHostIDTest(){
        manager.addHostID(testPlayerId1, Game.Chess);
        manager.addHostID(testPlayerId2, Game.Chess);

        assertEquals(2,manager.getAllHostIDs(Game.Chess).length);

        manager.clearAllHostIDs(Game.Chess);

        assertEquals(0,manager.getAllHostIDs(Game.Chess).length);
    }




}
