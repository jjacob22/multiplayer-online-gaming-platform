package networking;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import match_making.Game;
import match_making.Match;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;
import org.junit.jupiter.api.*;

import networking.AbstractDatabaseManager;
public class MatchDatabaseManagerTest {
    private final int testPlayerId1 = 111111111;
    private final int testPlayerId2 = 222222222;
    private final int fakePlayerID = 333333333;
    private final String testMatchID1 = "A1C2E3G4I5K6M";
    private final String testMatchID2 = "B1C2E3G4I5K6M";

    MatchDatabaseManager manager = new MatchDatabaseManager();

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
    public void getAndSetELO() {
        manager.setPlayerELO(testPlayerId1,Game.Chess,1222);
        manager.setPlayerELO(testPlayerId1,Game.ConnectFour,57);
        manager.setPlayerELO(testPlayerId1,Game.TicTacToe,1570);

        assertEquals(1222,manager.getPlayerELO(testPlayerId1,Game.Chess));
        assertEquals(57,manager.getPlayerELO(testPlayerId1,Game.ConnectFour));
        assertEquals(1570,manager.getPlayerELO(testPlayerId1,Game.TicTacToe));
    }
    @Test
    public void defaultELOValue(){
        assertEquals(1000, manager.getPlayerELO(testPlayerId1, Game.Chess));
        assertEquals(1000, manager.getPlayerELO(testPlayerId1, Game.ConnectFour));
        assertEquals(1000, manager.getPlayerELO(testPlayerId1, Game.TicTacToe));
    }

    @Test
    public void getAndSetELOWithNoPlayer(){
        assertEquals(-1,manager.getPlayerELO(fakePlayerID,Game.Chess));
        assertEquals(-1,manager.getPlayerELO(fakePlayerID,Game.ConnectFour));
        assertEquals(-1,manager.getPlayerELO(fakePlayerID,Game.TicTacToe));

        assertEquals(-1,manager.setPlayerELO(fakePlayerID,Game.Chess,1000));
        assertEquals(-1,manager.setPlayerELO(fakePlayerID,Game.ConnectFour,1001));
        assertEquals(-1,manager.setPlayerELO(fakePlayerID,Game.TicTacToe,1002));
    }

    @Test
    public void getAndSetGameWins() {
        manager.setGameWins(testPlayerId1,12, Game.Chess);
        manager.setGameWins(testPlayerId1,5, Game.ConnectFour);
        manager.setGameWins(testPlayerId1,15, Game.TicTacToe);

        assertEquals(12,manager.getGameWins(testPlayerId1,Game.Chess));
        assertEquals(5,manager.getGameWins(testPlayerId1,Game.ConnectFour));
        assertEquals(15,manager.getGameWins(testPlayerId1,Game.TicTacToe));
    }
    @Test
    public void defaultGameWinsValue(){
        assertEquals(0, manager.getGameWins(testPlayerId1, Game.Chess));
        assertEquals(0, manager.getGameWins(testPlayerId1, Game.ConnectFour));
        assertEquals(0, manager.getGameWins(testPlayerId1, Game.TicTacToe));
    }

    @Test
    public void getAndSetGameWinsWithNoPlayer(){
        assertEquals(-1,manager.getGameWins(fakePlayerID,Game.Chess));
        assertEquals(-1,manager.getGameWins(fakePlayerID,Game.ConnectFour));
        assertEquals(-1,manager.getGameWins(fakePlayerID,Game.TicTacToe));

        assertEquals(-1,manager.setGameWins(fakePlayerID,1,Game.Chess));
        assertEquals(-1,manager.setGameWins(fakePlayerID,2,Game.ConnectFour));
        assertEquals(-1,manager.setGameWins(fakePlayerID,3,Game.TicTacToe));
    }

    @Test
    public void addGameWinTest(){
        manager.setGameWins(testPlayerId1,12, Game.Chess);
        manager.setGameWins(testPlayerId1,5, Game.ConnectFour);
        manager.setGameWins(testPlayerId1,15, Game.TicTacToe);

        manager.addGameWin(testPlayerId1,Game.Chess);
        manager.addGameWin(testPlayerId1,Game.ConnectFour);
        manager.addGameWin(testPlayerId1,Game.TicTacToe);

        assertEquals(13,manager.getGameWins(testPlayerId1,Game.Chess));
        assertEquals(6,manager.getGameWins(testPlayerId1,Game.ConnectFour));
        assertEquals(16,manager.getGameWins(testPlayerId1,Game.TicTacToe));
    }


    @Test
    public void getAndSetGamesPlayed() {
        manager.setGamesPlayed(testPlayerId1,12, Game.Chess);
        manager.setGamesPlayed(testPlayerId1,5, Game.ConnectFour);
        manager.setGamesPlayed(testPlayerId1,15, Game.TicTacToe);

        assertEquals(12,manager.getGamesPlayed(testPlayerId1,Game.Chess));
        assertEquals(5,manager.getGamesPlayed(testPlayerId1,Game.ConnectFour));
        assertEquals(15,manager.getGamesPlayed(testPlayerId1,Game.TicTacToe));
    }
    @Test
    public void defaultGamesPlayedValue(){
        assertEquals(0, manager.getGamesPlayed(testPlayerId1, Game.Chess));
        assertEquals(0, manager.getGamesPlayed(testPlayerId1, Game.ConnectFour));
        assertEquals(0, manager.getGamesPlayed(testPlayerId1, Game.TicTacToe));
    }

    @Test
    public void getAndSetGamesPlayedWithNoPlayer(){
        assertEquals(-1,manager.getGamesPlayed(fakePlayerID,Game.Chess));
        assertEquals(-1,manager.getGamesPlayed(fakePlayerID,Game.ConnectFour));
        assertEquals(-1,manager.getGamesPlayed(fakePlayerID,Game.TicTacToe));

        assertEquals(-1,manager.setGamesPlayed(fakePlayerID,1,Game.Chess));
        assertEquals(-1,manager.setGamesPlayed(fakePlayerID,2,Game.ConnectFour));
        assertEquals(-1,manager.setGamesPlayed(fakePlayerID,3,Game.TicTacToe));
    }

    @Test
    public void addGamesPlayedTest(){
        manager.setGamesPlayed(testPlayerId1,12, Game.Chess);
        manager.setGamesPlayed(testPlayerId1,5, Game.ConnectFour);
        manager.setGamesPlayed(testPlayerId1,15, Game.TicTacToe);

        manager.addGamePlayed(testPlayerId1,Game.Chess);
        manager.addGamePlayed(testPlayerId1,Game.ConnectFour);
        manager.addGamePlayed(testPlayerId1,Game.TicTacToe);

        assertEquals(13,manager.getGamesPlayed(testPlayerId1,Game.Chess));
        assertEquals(6,manager.getGamesPlayed(testPlayerId1,Game.ConnectFour));
        assertEquals(16,manager.getGamesPlayed(testPlayerId1,Game.TicTacToe));
    }


    @Test
    public void getAndSetTimePlayed() {
        manager.setTimePlayed(testPlayerId1,12000, Game.Chess);
        manager.setTimePlayed(testPlayerId1,5000, Game.ConnectFour);
        manager.setTimePlayed(testPlayerId1,15000, Game.TicTacToe);

        assertEquals(12,manager.getTimePlayed(testPlayerId1,Game.Chess));
        assertEquals(5,manager.getTimePlayed(testPlayerId1,Game.ConnectFour));
        assertEquals(15,manager.getTimePlayed(testPlayerId1,Game.TicTacToe));
    }
    @Test
    public void defaultGamesTimePlayed(){
        assertEquals(0, manager.getTimePlayed(testPlayerId1, Game.Chess));
        assertEquals(0, manager.getTimePlayed(testPlayerId1, Game.ConnectFour));
        assertEquals(0, manager.getTimePlayed(testPlayerId1, Game.TicTacToe));
    }

    @Test
    public void getAndSetTimePlayedWithNoPlayer(){
        assertEquals(-1,manager.getTimePlayed(fakePlayerID,Game.Chess));
        assertEquals(-1,manager.getTimePlayed(fakePlayerID,Game.ConnectFour));
        assertEquals(-1,manager.getTimePlayed(fakePlayerID,Game.TicTacToe));

        assertEquals(-1,manager.setTimePlayed(fakePlayerID,1,Game.Chess));
        assertEquals(-1,manager.setTimePlayed(fakePlayerID,2,Game.ConnectFour));
        assertEquals(-1,manager.setTimePlayed(fakePlayerID,3,Game.TicTacToe));
    }

    @Test
    public void addTimePlayedTest(){
        manager.setTimePlayed(testPlayerId1,12000, Game.Chess);
        manager.setTimePlayed(testPlayerId1,5000, Game.ConnectFour);
        manager.setTimePlayed(testPlayerId1,15000, Game.TicTacToe);

        manager.addTimePlayed(testPlayerId1,Game.Chess,1000);
        manager.addTimePlayed(testPlayerId1,Game.ConnectFour,1000);
        manager.addTimePlayed(testPlayerId1,Game.TicTacToe,1000);

        assertEquals(13,manager.getTimePlayed(testPlayerId1,Game.Chess));
        assertEquals(6,manager.getTimePlayed(testPlayerId1,Game.ConnectFour));
        assertEquals(16,manager.getTimePlayed(testPlayerId1,Game.TicTacToe));
    }


    @Test
    public void getAndSetPlayerMatchHistory() {
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"A2f9pUGm417Qt"} , Game.Chess);
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"A27G98H7Jp091","PiCnwU92N70M6"}, Game.ConnectFour);
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"EFot94820Pl0r"} , Game.TicTacToe);

        assertEquals("A2f9pUGm417Qt", manager.getPlayerMatchHistory(testPlayerId1, Game.Chess)[0]);
        assertEquals("A27G98H7Jp091", manager.getPlayerMatchHistory(testPlayerId1, Game.ConnectFour)[0]);
        assertEquals("PiCnwU92N70M6", manager.getPlayerMatchHistory(testPlayerId1, Game.ConnectFour)[1]);
        assertEquals("EFot94820Pl0r", manager.getPlayerMatchHistory(testPlayerId1, Game.TicTacToe)[0]);

    }

    @Test
    public void addToMatchHistory() {
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"A2f9pUGm417Qt"} , Game.Chess);
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"A27G98H7Jp091"}, Game.ConnectFour);
        manager.setPlayerMatchHistory(testPlayerId1,new String[]{"EFot94820Pl0r"} , Game.TicTacToe);

        manager.addMatchToPlayerMatchHistory(testPlayerId1,"QQQQQQQQQQQQQ",Game.Chess);
        manager.addMatchToPlayerMatchHistory(testPlayerId1,"PPPPPPPPPPPPP",Game.ConnectFour);
        manager.addMatchToPlayerMatchHistory(testPlayerId1,"LLLLLLLLLLLLL",Game.TicTacToe);

        assertEquals("QQQQQQQQQQQQQ", manager.getPlayerMatchHistory(testPlayerId1, Game.Chess)[1]);
        assertEquals("PPPPPPPPPPPPP", manager.getPlayerMatchHistory(testPlayerId1, Game.ConnectFour)[1]);
        assertEquals("LLLLLLLLLLLLL", manager.getPlayerMatchHistory(testPlayerId1, Game.TicTacToe)[1]);
    }

    /*

    TESTS FOR MATCH HISTORY FUNCTIONS

     */

    @Test
    public void matchCompleteTest() {
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId1,testPlayerId2,"Winner",testPlayerId1,100,150000);
        assertEquals(Game.Chess, manager.getMatchGameType(testMatchID1));
        assertEquals(testPlayerId1, manager.getPlayersOfMatch(testMatchID1)[0]);
        assertEquals(testPlayerId2, manager.getPlayersOfMatch(testMatchID1)[1]);
        assertEquals("Winner", manager.getOutcomeOfMatch(testMatchID1));
        assertEquals(testPlayerId1, manager.getWinningPlayerOfMatch(testMatchID1));
        assertEquals(100,manager.getTotalMovesOfMatch(testMatchID1));
        assertEquals(150,manager.getMatchTime(testMatchID1));
    }

    @Test
    public void matchCompleteTestDraw(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId1,testPlayerId2,"Draw",100,150000);
        assertEquals(Game.Chess, manager.getMatchGameType(testMatchID1));
        assertEquals(testPlayerId1, manager.getPlayersOfMatch(testMatchID1)[0]);
        assertEquals(testPlayerId2, manager.getPlayersOfMatch(testMatchID1)[1]);
        assertEquals("Draw", manager.getOutcomeOfMatch(testMatchID1));
        assertEquals(-1, manager.getWinningPlayerOfMatch(testMatchID1));
        assertEquals(100,manager.getTotalMovesOfMatch(testMatchID1));
        assertEquals(150,manager.getMatchTime(testMatchID1));
    }

    @Test
    public void getAndSetMatchType(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId1,testPlayerId2,"Draw",100,150000);
        manager.setMatchGameType(testMatchID1, Game.ConnectFour);
        assertEquals(Game.ConnectFour, manager.getMatchGameType(testMatchID1));
        manager.setMatchGameType(testMatchID1, Game.Chess);
        assertEquals(Game.Chess, manager.getMatchGameType(testMatchID1));
        manager.setMatchGameType(testMatchID1, Game.TicTacToe);
        assertEquals(Game.TicTacToe, manager.getMatchGameType(testMatchID1));

    }

    @Test
    public void setMatchTypeWithNoMatch() {
        assertEquals(-1,manager.setMatchGameType(testMatchID1, Game.Chess));
    }

    @Test
    public void getAndSetPlayersOfMatch(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId2,testPlayerId1,"Draw",100,150000);
        manager.setPlayersOfMatch(testMatchID1, testPlayerId1, testPlayerId2);

        assertEquals(testPlayerId1, manager.getPlayersOfMatch(testMatchID1)[0]);
        assertEquals(testPlayerId2, manager.getPlayersOfMatch(testMatchID1)[1]);

    }

    @Test
    public void setPlayersWithNoMatch() {
        assertEquals(-1,manager.setPlayersOfMatch(testMatchID1, testPlayerId1, testPlayerId2));
    }

    @Test
    public void getAndSetOutcomeOfMatch(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId2,testPlayerId1,"undefined",100,150000);
        manager.setOutcomeOfMatch(testMatchID1, "Winner");
        manager.matchComplete(testMatchID2,Game.Chess,testPlayerId2,testPlayerId1,"undefined",100,150000);
        manager.setOutcomeOfMatch(testMatchID2, "Draw");

        assertEquals("Winner", manager.getOutcomeOfMatch(testMatchID1));
        assertEquals("Draw", manager.getOutcomeOfMatch(testMatchID2));
    }

    @Test
    public void setOutcomeWithNoMatch() {
        assertEquals(-1,manager.setOutcomeOfMatch(testMatchID1, "Draw"));
    }

    @Test
    public void getAndSetWinner(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId1,testPlayerId2,"Winner",testPlayerId1,100,150000);
        manager.setWinningPlayerOfMatch(testMatchID1, testPlayerId2);

        assertEquals(testPlayerId2, manager.getWinningPlayerOfMatch(testMatchID1));
    }

    @Test
    public void setWinnerWithNoMatch() {
        assertEquals(-1,manager.setWinningPlayerOfMatch(testMatchID1, testPlayerId2));
    }

    @Test
    public void getAndSetTotalMoves(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId2,testPlayerId1,"Draw",100,150000);
        manager.setTotalMovesOfMatch(testMatchID1, 4);

        assertEquals(4, manager.getTotalMovesOfMatch(testMatchID1));
    }

    @Test
    public void setMovesWithNoMatch() {
        assertEquals(-1,manager.setTotalMovesOfMatch(testMatchID1, 4));
    }

    @Test
    public void getAndSetMatchTime(){
        manager.matchComplete(testMatchID1,Game.Chess,testPlayerId2,testPlayerId1,"Draw",100,150000);
        manager.setMatchTime(testMatchID1, 20000);

        assertEquals(20, manager.getMatchTime(testMatchID1));
    }

    @Test
    public void setTimeWithNoMatch() {
        assertEquals(-1,manager.setMatchTime(testMatchID1, 20000));
    }



}
