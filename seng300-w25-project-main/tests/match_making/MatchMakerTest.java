package match_making;

import networking.*;
import org.junit.jupiter.api.*;
import profile.Profile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchMakerTest {
    private MatchDatabaseManager matchDb;
    private QueueDatabaseManager queueDb;
    private ProfileDatabaseManager db;
    private MatchMaker matchMaker;
    private int validID1, validID2, validID3, validID4;
    private int invalidID;

    Profile CoralReef1;
    Profile OceanWhisperer2;
    Profile TideSurfer3;
    Profile DeepBlueWave4;
    @BeforeEach
    void setUp() {
        //Basic constructor
        matchDb = new MatchDatabaseManager();
        queueDb = new QueueDatabaseManager();
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

        matchMaker = new MatchMaker(matchDb, queueDb, db);

        //Setting test ID for test
        validID1 = 1;
        validID2 = 2;
        validID3 = 3;
        validID4 = 4;

        //Setting invalid ID for test
        invalidID = 55723383;

        //Back up database
        AbstractDatabaseManager.backupDatabase();

        //Create test user in database
        AbstractDatabaseManager.createNewProfile(validID1);
        AbstractDatabaseManager.createNewProfile(validID2);
        AbstractDatabaseManager.createNewProfile(validID3);
        AbstractDatabaseManager.createNewProfile(validID4);

        // Set necessary user info
        db.setPlayerUsername(validID1, "1");
        db.setPlayerUsername(validID2, "2");
        db.setPlayerUsername(validID3, "3");
        db.setPlayerUsername(validID4, "4");

        //Set ELO for test in database
        matchDb.setPlayerELO(validID1, Game.Chess, 351);
        matchDb.setPlayerELO(validID1, Game.TicTacToe, 1904);
        matchDb.setPlayerELO(validID1, Game.ConnectFour, 1090);

        matchDb.setPlayerELO(validID2, Game.Chess, 1647);
        matchDb.setPlayerELO(validID2, Game.TicTacToe, 198);
        matchDb.setPlayerELO(validID2, Game.ConnectFour, 921);

        matchDb.setPlayerELO(validID3, Game.Chess, 1378);
        matchDb.setPlayerELO(validID3, Game.TicTacToe, 1262);
        matchDb.setPlayerELO(validID3, Game.ConnectFour, 1359);

        matchDb.setPlayerELO(validID4, Game.Chess, 923);
        matchDb.setPlayerELO(validID4, Game.TicTacToe, 671);
        matchDb.setPlayerELO(validID4, Game.ConnectFour, 364);

        queueDb.addPlayerToQueue(validID1,Game.Chess);
        queueDb.addPlayerToQueue(validID1,Game.TicTacToe);
        queueDb.addPlayerToQueue(validID1,Game.ConnectFour);

        queueDb.addPlayerToQueue(validID2,Game.Chess);
        queueDb.addPlayerToQueue(validID2,Game.TicTacToe);
        queueDb.addPlayerToQueue(validID2,Game.ConnectFour);

        queueDb.addPlayerToQueue(validID3,Game.Chess);
        queueDb.addPlayerToQueue(validID3,Game.TicTacToe);
        queueDb.addPlayerToQueue(validID3,Game.ConnectFour);

        queueDb.addPlayerToQueue(validID4,Game.Chess);
        queueDb.addPlayerToQueue(validID4,Game.TicTacToe);
        queueDb.addPlayerToQueue(validID4,Game.ConnectFour);
    }
    private Profile getProfileByID(int id){
        return switch (id){
            case 1 -> CoralReef1;
            case 2 -> OceanWhisperer2;
            case 3 -> TideSurfer3;
            case 4 -> DeepBlueWave4;
            default -> null;
        };
    }
    @AfterEach
    void setDown() {
        AbstractDatabaseManager.restoreDatabaseBackupAndOverrideCurrent();
        queueDb.clearQueue(Game.Chess);
        queueDb.clearQueue(Game.TicTacToe);
        queueDb.clearQueue(Game.ConnectFour);
    }

    @Test
    void findMatchTest_InvalidID_ExpectInvalidPlayerIDException(){
        Assertions.assertThrows(InvalidPlayerIDException.class , () -> {
            matchMaker.findMatch(invalidID,Game.Chess);
        }, "Expected InvalidPlayerIDException");
    }

    @Test
    void findMatchTest_EmptyQueue_ExpectNullReturns(){
        queueDb.clearQueue(Game.Chess);
        queueDb.clearQueue(Game.TicTacToe);
        queueDb.clearQueue(Game.ConnectFour);

        assertNull(matchMaker.findMatch(validID1,Game.Chess));
        assertNull(matchMaker.findMatch(validID1,Game.TicTacToe));
        assertNull(matchMaker.findMatch(validID1,Game.ConnectFour));
    }


    @Test
    void findMatchTest_NoSuitableOpponent_ExpectNullReturns(){
        queueDb = new QueueDatabaseManager(){
            @Override
            public int addPlayerToQueue(int playerID,Game game){
                return 1;
            }

            @Override
            public int[] getAllPlayersInQueue(Game game){
                return switch (game){
                    case Chess,TicTacToe,ConnectFour -> new int[]{validID1,validID2,validID3,validID4};
                };
            }

            @Override
            public int getPlayerMatchmakingAttempts(int playerID, Game game) {
                return 0;
            }

            @Override
            public void removePlayerFromQueue(int playerID, Game game){}

            @Override
            public int setPlayerMatchmakingAttempts(int playerID,Game game,int attempts){return 0;}

        };
        matchMaker = new MatchMaker(this.matchDb,queueDb,this.db);

        assertNull(matchMaker.findMatch(validID1,Game.Chess));
        assertNull(matchMaker.findMatch(validID1,Game.TicTacToe));
        assertNull(matchMaker.findMatch(validID1,Game.ConnectFour));
    }


    @Test
    void findMatchTest_SuitableOpponentAvailable_ExpectNotNullReturns(){
        matchDb.setPlayerELO(validID2, Game.Chess, 351);
        matchDb.setPlayerELO(validID2, Game.TicTacToe, 1904);
        matchDb.setPlayerELO(validID2, Game.ConnectFour, 1090);

        //Currently we cannot create GameServerController for Tictactoe and connect four
        assertNotNull(matchMaker.findMatch(validID1,Game.Chess));
        assertNotNull(matchMaker.findMatch(validID1,Game.TicTacToe));
        //assertNotNull(MATCHMAKER.findMatch(VALIDID1,Game.ConnectFour));
    }


    @Test
    void hostMatchTest_AddingAnUnknownPlayerToHost_ExpectsInvalidPlayerIDException(){
        assertThrows(InvalidPlayerIDException.class, () -> {
            matchMaker.hostMatch(invalidID,Game.Chess);
        });
        assertThrows(InvalidPlayerIDException.class, () -> {
            matchMaker.hostMatch(invalidID,Game.TicTacToe);
        });
        assertThrows(InvalidPlayerIDException.class, () -> {
            matchMaker.hostMatch(invalidID,Game.ConnectFour);
        });
    }

    @Test
    void hostMatchTest_AddingAPlayerToHost(){
        ArrayList<String> testList = new ArrayList<>();
        testList.add("CoralReef");
        matchMaker.hostMatch(validID1,Game.Chess);

        assertEquals(testList, matchMaker.getHosts(Game.Chess));
    }

    @Test
    void unhostMatchTest_RemovingAnUnknownPlayerToHost_ExpectsInvalidPlayerIDException(){
        assertThrows(InvalidPlayerIDException.class, () -> {
            matchMaker.unhostMatch(invalidID,Game.Chess);
        });
    }

    @Test
    void unhostMatchTest_RemovingAPlayerNotInHost_ExpectsUnknownMatchException(){
        assertThrows(UnknownMatchException.class, () -> {
            matchMaker.unhostMatch(validID1,Game.Chess);
        });
    }

    @Test
    void joinMatchTest_NoMatchExist_ExpectsMatchNotFoundException(){
        assertThrows(MatchNotFoundException.class, () ->{
            matchMaker.joinMatch(validID1,validID2,Game.Chess);
        });
    }

    @Test
    void joinMatchTest_MatchExist_ExpectsNotNull(){
        matchMaker.hostMatch(validID2,Game.Chess);
        assertNotNull(matchMaker.joinMatch(validID1,validID2,Game.Chess));
    }


}
