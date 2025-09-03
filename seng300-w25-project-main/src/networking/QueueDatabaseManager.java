package networking;

import match_making.Game;

import java.io.*;
import java.util.ArrayList;

/**
 * This is a class for all getters and setters for players that want to start or participate in a match
 */
public class QueueDatabaseManager extends AbstractDatabaseManager{

    public QueueDatabaseManager() {
    }

    /**
     * a method to get all players in the queue for match
     * @param game: the type of the game
     * @return: int[] of all player IDs
     */
    public int[] getAllPlayersInQueue(Game game){
        String[] playerStrings = getAllOfOneValueTypeFromFile(queueFileRequired(game),0, TOTAL_VALUES_QUEUE);
        if (playerStrings.length > 0) {
            int[] playerIDs = new int[playerStrings.length];
            for (int i = 0; i < playerStrings.length; i++) {
                playerIDs[i] = Integer.parseInt(playerStrings[i]);
            }
            return playerIDs;
        }
        return new int[0];
    }

    /**
     * a method to add a player to the queue
     * @param playerID: the player that wants to be added
     * @param game: the type of game
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addPlayerToQueue(int playerID, Game game){
        String filename = queueFileRequired(game);
        int[] currentQueue = getAllPlayersInQueue(game);
        for (int i = 0; i < currentQueue.length; i++) {
            if (currentQueue[i] == playerID) {
                return -1;
            }
        }
        addLineToFile(filename, String.format("%d~0",playerID));
        return 1;
    }

    /**
     * a method for removing a player from the queue
     * @param playerID: the player to remove
     * @param game: the type of the game
     */
    public void removePlayerFromQueue(int playerID, Game game){
        String filename = queueFileRequired(game);
        removePlayerFromFile(filename, playerID);
    }

    /**
     * a method for clearing the queue
     * @param game: the type of the game
     */
    public void clearQueue(Game game){
        String filename = queueFileRequired(game);
        File fileToClear = new File(filename);
        File newFile = new File("src/networking/database/game_queues/temp.csv");
        try {
            newFile.createNewFile();
            if(fileToClear.delete() && newFile.renameTo(fileToClear)) {
                System.out.println("File cleared.");
            }
            else{
                System.out.println("Error clearing file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * a method for getting the attempts for matchmaking
     * @param playerID: the player to attempt the matchmaking
     * @param game: the type of the game
     * @return: the matchmaking attempts
     */
    public int getPlayerMatchmakingAttempts(int playerID, Game game){
        String filename = queueFileRequired(game);
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_QUEUE);
        try{
            return Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * a method for setting the number of attempts for matchmaking
     * @param playerID: the ID of the player
     * @param game: the type of the game
     * @param attempts: the number of attempts
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerMatchmakingAttempts(int playerID, Game game, int attempts){
        String filename = queueFileRequired(game);
        return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_QUEUE,String.valueOf(attempts),1);
    }

    /**
     * a method for adding to the number of attempts
     * @param playerID: the ID of the player
     * @param game: the type of the game
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addPlayerMatchmakingAttempt(int playerID, Game game){
        String filename = queueFileRequired(game);
        int attempts = getPlayerMatchmakingAttempts(playerID, game);
        attempts++;
        return setPlayerMatchmakingAttempts(playerID, game, attempts);
    }


    /**
     * a method for getting all IDs of hosts
     * @param game: the type of the game
     * @return: int[] containing all IDs of hosts
     */
    public int[] getAllHostIDs(Game game){
        ArrayList<String> matchIDsArrayList = new ArrayList<String>();
        String filename = hostIDFileRequired(game);
        String[] hostIDStrings = getAllOfOneValueTypeFromFile(filename,0,TOTAL_VALUES_HOST_IDS);
        if (hostIDStrings.length > 0) {
            int[] hostIDs = new int[hostIDStrings.length];
            for (int i = 0; i < hostIDStrings.length; i++) {
                hostIDs[i] = Integer.parseInt(hostIDStrings[i]);
            }
            return hostIDs;
        }
        return new int[0];
    }

    /**
     * method for adding an ID to the hosts
     * @param hostID: the ID of the host
     * @param game: the type of the game
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addHostID(int hostID, Game game){
        String filename = hostIDFileRequired(game);
        int[] currentQueue = getAllHostIDs(game);
        for (int i = 0; i < currentQueue.length; i++) {
            if (currentQueue[i] == hostID) {
                return -1;
            }
        }
        addLineToFile(filename, String.valueOf(hostID));
        return 1;
    }

    /**
     * method for removing an ID from the hosts
     * @param hostID: the ID to be removed
     * @param game: the type of the game
     */
    public void removeHostID(int hostID, Game game){
        String filename = hostIDFileRequired(game);
        removePlayerFromFile(filename,hostID);
    }

    /**
     * method for clearing all hosts
     * @param game: the type of the game
     */
    public void clearAllHostIDs(Game game){
        String filename = hostIDFileRequired(game);
        File fileToClear = new File(filename);
        File newFile = new File("src/networking/database/host_ids/temp.csv");
        try {
            newFile.createNewFile();
            if(fileToClear.delete() && newFile.renameTo(fileToClear)) {
                System.out.println("File cleared.");
            }
            else{
                System.out.println("Error clearing file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * a method for getting the file of the queue
     * @param game: the type of the game
     * @return: String including the filename
     */
    public String queueFileRequired(Game game){
        String filename = "src/networking/database/game_queues/chess_queue.csv";
        switch (game) {
            case Chess: filename = "src/networking/database/game_queues/chess_queue.csv"; break;
            case ConnectFour: filename = "src/networking/database/game_queues/connect_four_queue.csv"; break;
            case TicTacToe: filename = "src/networking/database/game_queues/tic_tac_toe_queue.csv"; break;
        }
        return filename;
    }

    /**
     * a method for getting the file of host IDs
     * @param game: the type of the game
     * @return: String including the filename
     */
    public String hostIDFileRequired(Game game){
        String filename = "src/networking/database/lobby_matches/chess_host_ids.csv";
        switch (game) {
            case Chess: filename = "src/networking/database/host_ids/chess_host_ids.csv"; break;
            case ConnectFour: filename = "src/networking/database/host_ids/connect_four_host_ids.csv"; break;
            case TicTacToe: filename = "src/networking/database/host_ids/tic_tac_toe_host_ids.csv"; break;
        }
        return filename;
    }

}
