package networking;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This is an abstract class for database including all general functions needed for reading or writing in database.
 */

public abstract class AbstractDatabaseManager extends AbstractNetworkManager{
    /**
     * ReentrantReadWriteLock to prevent the writer to rewrite the file at the same time or reading an incomplete file.
     */
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * fixed numbers to be used in different methods
     */
    public static final int TOTAL_VALUES_PROFILE = 8;
    public static final int TOTAL_VALUES_PLAYER_GAME_STATS = 13;
    public static final int TOTAL_VALUES_PLAYER_MATCH_HISTORY = 4;
    public static final int TOTAL_VALUES_MATCH_HISTORY = 8;
    public static final int MATCH_ID_LENGTH = 13; // Size TBD, we can immediately rule out any calls with an incorrect size.
    public static final int PLAYER_ID_LENGTH = 9; // Size TBD
    public static final int TOTAL_VALUES_BLOCK_HISTORY = 3;
    public static final int TOTAL_VALUES_FRIEND_REQUESTS = 2;
    public static final int TOTAL_VALUES_QUEUE = 2;
    public static final int TOTAL_VALUES_HOST_IDS = 1;
    public static final int TOTAL_VALUES_PLAYER_NOTIFICATIONS = 2;

    /**
     * database reader based on playerID
     * @param filename: the file to read from
     * @param playerID: gets player ID to read the related info
     * @param total_values_const: the number of values in a file
     * @return: String[] of the values in all lines in the file
     */
    protected static String[] readDatabaseFileForPlayerID(String filename, int playerID, int total_values_const) {
        readLock.lock();
        try {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] values = line.split("~");
                    if (values.length == total_values_const && Integer.parseInt(values[0]) == playerID) {
                        fileReader.close();
                        return values;
                    }
                }
                fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new String[0]; // FAILURE
        }finally {
            readLock.unlock(); // Always unlock after reading
        }
    }

    /**
     * This is a method to create a list of a specific value. For example: all usernames in the database
     * (to be used for checking uniqueness)
     * @param filename: the file to read from
     * @param value_index: the index the value is stored in
     * @param total_values_const: the number of values in a line of the file
     * @return: String[] of all usernames or any other specific value
     */
    protected static String[] getAllOfOneValueTypeFromFile(String filename, int value_index, int total_values_const) {
        readLock.lock();
        try {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                ArrayList<String> valuesArray = new ArrayList<>();
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] values = line.split("~");
                    if (values.length == total_values_const) {
                        valuesArray.add(values[value_index]);
                    }
                }
                fileReader.close();
                if (!valuesArray.isEmpty()) {
                    String[] allValues = new String[valuesArray.size()];
                    for (int i = 0; i < valuesArray.size(); i++) {
                        allValues[i] = valuesArray.get(i);
                    }
                    return allValues;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new String[0]; // FAILURE
        }finally {
            readLock.unlock(); // Always unlock after reading
        }
    }

    /**
     * A method to read from a file based on the matchID
     * @param filename: the file to read from
     * @param matchID: the ID of the match we want to read from
     * @param total_values_const: the number of the values in a line in the file
     * @return: String[] including all values in the lines
     */
    protected static String[] readDatabaseFileForMatchID(String filename, String matchID, int total_values_const) {
        readLock.lock();
        try {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] values = line.split("~");
                    if (values.length == total_values_const && values[0].equals(matchID)) {
                        fileReader.close();
                        return values;
                    }
                }
                fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new String[0]; // FAILURE
        }finally {
            readLock.unlock(); // Always unlock after reading
        }
    }

    /**
     * The method to write to a file based on the player ID
     * @param filename: the file to write to
     * @param playerID: the ID of the player to be edited
     * @param total_values_const: the number of values in a line
     * @param value: The value that needs to be written
     * @param index_of_value: the index that the value should be written in
     * @return: returns an int to check if it was successful or not. 1 for success, -1 for failure
     */
    protected static int writeValueToDatabaseFileForPlayerID(String filename, int playerID, int total_values_const, String value, int index_of_value) {
        writeLock.lock();
        try {
            if (index_of_value > 0) {
                File inputFile = new File(filename);
                File tempFile = new File("src/networking/database/temp.csv");
                boolean playerFound = false;
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split("~");

                        // If it's the correct player, update the field
                        if (values.length == total_values_const && Integer.parseInt(values[0]) == playerID) {
                            values[index_of_value] = value;
                            playerFound = true;
                        }
                        writer.write(String.join("~", values));
                        writer.newLine();
                    }
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!playerFound) {
                    return -1;
                }
                inputFile.delete();
                tempFile.renameTo(inputFile);
//                if (inputFile.delete() && tempFile.renameTo(inputFile)) {
//                    System.out.println("File updated successfully.");
//                } else {
//                    System.out.println("Error updating file.");
//                }
            }
            return 1;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * The method to write to a file based on the player ID
     * @param filename: the file to write to
     * @param matchID: the ID of the player to be edited
     * @param total_values_const: the number of values in a line
     * @param value: The value that needs to be written
     * @param index_of_value: the index that the value should be written in
     * @return: returns an int to check if it was successful or not. 1 for success, -1 for failure
     */
    protected static int writeValueToDatabaseFileForMatchID(String filename, String matchID, int total_values_const, String value, int index_of_value) {
        writeLock.lock();
        try {
            if (index_of_value > 0) {
                File inputFile = new File(filename);
                File tempFile = new File("src/networking/database/temp.csv");
                boolean matchFound = false;
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split("~");

                        // If it's the correct match, update the field
                        if (values.length == total_values_const && values[0].equals(matchID)) {
                            values[index_of_value] = value;
                            matchFound = true;
                        }
                        writer.write(String.join("~", values));
                        writer.newLine();
                    }
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!matchFound) {
                    return -1;
                }
                inputFile.delete();
                tempFile.renameTo(inputFile);
//                if (inputFile.delete() && tempFile.renameTo(inputFile)) {
//                    System.out.println("File updated successfully.");
//                } else {
//                    System.out.println("Error updating file.");
//                }
            }
            return 1;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * method to add a new line in a file.(to be used for in class methods)
     * @param filename: the file to add a line
     * @param newLine: the line that needs to be added
     */
    protected static void addLineToFile(String filename, String newLine) {
        writeLock.lock();
        try {
            File inputFile = new File(filename);
            File tempFile = new File("src/networking/database/temp.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.write(newLine);
                reader.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
//            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
//                System.out.println("File updated successfully.");
//            } else {
//                System.out.println("Error updating file.");
//            }
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * the method to check if a player ID already exists in the database
     * @param playerID: ID of the player to check
     * @return: true if found, false if not found
     */
    public static boolean playerExists(int playerID) {
        String fileProfile = "src/networking/database/player_profile_info.csv";
        String fileStats = "src/networking/database/player_game_stats.csv";
        String fileMatchHistory = "src/networking/database/player_match_history.csv";
        String fileFriendRequests = "src/networking/database/friend_requests.csv";
        String fileBlockHistory = "src/networking/database/block_history.csv";
        String fileNotifications = "src/networking/database/player_notifications.csv";
        if (readDatabaseFileForPlayerID(fileProfile, playerID, TOTAL_VALUES_PROFILE).length > 0 && readDatabaseFileForPlayerID(fileStats, playerID, TOTAL_VALUES_PLAYER_GAME_STATS).length > 0 && readDatabaseFileForPlayerID(fileMatchHistory, playerID, TOTAL_VALUES_PLAYER_MATCH_HISTORY).length > 0 && readDatabaseFileForPlayerID(fileFriendRequests, playerID, TOTAL_VALUES_FRIEND_REQUESTS).length > 0 && readDatabaseFileForPlayerID(fileBlockHistory, playerID, TOTAL_VALUES_BLOCK_HISTORY).length > 0 && readDatabaseFileForPlayerID(fileNotifications, playerID, TOTAL_VALUES_PLAYER_NOTIFICATIONS).length > 0) {
            return true;
        }
        return false;
    }

    /**
     * a method for creating a new profile for a player in all files related to the player
     * @param playerID: The ID of the player
     * @return: -1 for unsuccessful process, 1 for successful
     */
    public static int createNewProfile(int playerID) {
        if (playerExists(playerID)) {
            return -1;
        }
        String blockHistoryFile = "src/networking/database/block_history.csv";
        addLineToFile(blockHistoryFile, String.format("%d~null~null", playerID));

        String friendRequestsFile = "src/networking/database/friend_requests.csv";
        addLineToFile(friendRequestsFile, String.format("%d~null",playerID));

        String playerGameStatsFile = "src/networking/database/player_game_stats.csv";
        addLineToFile(playerGameStatsFile, String.format("%d~1000~0~0~0~1000~0~0~0~1000~0~0~0",playerID));

        String playerMatchHistoryFile = "src/networking/database/player_match_history.csv";
        addLineToFile(playerMatchHistoryFile, String.format("%d~null~null~null",playerID));

        String playerProfileInfoFile = "src/networking/database/player_profile_info.csv";
        addLineToFile(playerProfileInfoFile, String.format("%d~null~null~null~null~null~0~-1",playerID));

        String playerNotificationsFile = "src/networking/database/player_notifications.csv";
        addLineToFile(playerNotificationsFile,String.format("%d~null",playerID));

        return 1;
    }

    /**
     * A method to get all player IDs ( to be used for checking the uniqueness)
     * @return: int[] of all player IDs
     */
    public static int[] getAllPlayerIDs() {
        readLock.lock();
        try {
            ArrayList<Integer> playerIDsArrayList = new ArrayList<Integer>();
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/networking/database/player_profile_info.csv"))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] values = line.split("~");
                    if (values.length == TOTAL_VALUES_PROFILE) {
                        playerIDsArrayList.add(Integer.valueOf(values[0]));
                    }
                }
                fileReader.close();
                if (!playerIDsArrayList.isEmpty()) {
                    int[] playerIDs = new int[playerIDsArrayList.size()];
                    for (int i = 0; i < playerIDsArrayList.size(); i++) {
                        playerIDs[i] = playerIDsArrayList.get(i);
                    }
                    return playerIDs;
                }
                return new int[0];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }finally {
            readLock.unlock();
        }
    }

    /**
     * a method to retrieve the player from the username
     * @param username: the username that a player chooses for themselves
     * @return: -1 for unsuccessful process
     */
    public static int getPlayerIDFromUsername(String username) {
        readLock.lock();
        try {
            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/networking/database/player_profile_info.csv"))) {
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] values = line.split("~");
                    if (values.length == TOTAL_VALUES_PROFILE && values[1].equals(username)) {
                        fileReader.close();
                        return Integer.parseInt(values[0]);
                    }
                }
                fileReader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }finally {
            readLock.unlock(); // Always unlock after reading
        }
        return -1;
    }

    /**
     * a method to get all usernames in the database (to be used for uniqueness)
     * @return: String[] containing all usernames
     */
    public static String[] getAllUsernames(){
        return getAllOfOneValueTypeFromFile("src/networking/database/player_profile_info.csv",1,TOTAL_VALUES_PROFILE);
    }
    /**
     * a method to get all emails in the database
     * @return: String[] containing all emails
     */
    public static String[] getAllEmails(){
        return getAllOfOneValueTypeFromFile("src/networking/database/player_profile_info.csv",3,TOTAL_VALUES_PROFILE);
    }

    /**
     * a method to check if a username is unique or not
     * @param usernameToCheck: the username that needs to be checked
     * @return: true if it is unique, false if not
     */
    public boolean isUsernameUnique(String usernameToCheck) {
        String[] usernames = getAllUsernames();
        for (String username : usernames) {
            if (username.equals(usernameToCheck)) {
                return false;
            }
        }
        return true;
    }
    /**
     * a method to check if an email address is unique or not
     * @param emailToCheck: the email address that needs to be checked
     * @return: true if it is unique, false if not
     */

    public  boolean isEmailUnique(String emailToCheck) {
        String[] emails = getAllEmails();
        for (String email: emails) {
            if (email.equals(emailToCheck)) {
                return false;
            }
        }
        return true;
    }

//    public static int[] getAllUsernames() {
//        readLock.lock();
//        try {
//            ArrayList<Integer> usernamesArrayList = new ArrayList<Integer>();
//            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/networking/database/player_profile_info.csv"))) {
//                String line;
//                while ((line = fileReader.readLine()) != null) {
//                    String[] values = line.split("~");
//                    if (values.length == TOTAL_VALUES_PROFILE) {
//                        usernamesArrayList.add(Integer.valueOf(values[1]));
//                    }
//                }
//                fileReader.close();
//                if (!usernamesArrayList.isEmpty()) {
//                    int[] usernames = new int[usernamesArrayList.size()];
//                    for (int i = 0; i < usernamesArrayList.size(); i++) {
//                        usernames[i] = usernamesArrayList.get(i);
//                    }
//                    return usernames;
//                }
//                return new int[0];
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }finally {
//            readLock.unlock();
//        }
//    }

//    public static int[] getAllEmails() {
//        readLock.lock();
//        try {
//            ArrayList<Integer> emailsArrayList = new ArrayList<Integer>();
//            try (BufferedReader fileReader = new BufferedReader(new FileReader("src/networking/database/player_profile_info.csv"))) {
//                String line;
//                while ((line = fileReader.readLine()) != null) {
//                    String[] values = line.split("~");
//                    if (values.length == TOTAL_VALUES_PROFILE) {
//                        emailsArrayList.add(Integer.valueOf(values[3]));
//                    }
//                }
//                fileReader.close();
//                if (!emailsArrayList.isEmpty()) {
//                    int[] emails = new int[emailsArrayList.size()];
//                    for (int i = 0; i < emailsArrayList.size(); i++) {
//                        emails[i] = emailsArrayList.get(i);
//                    }
//                    return emails;
//                }
//                return new int[0];
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }finally {
//            readLock.unlock();
//        }
//    }

    /**
     * method for removing a player from the database
     * @param playerID: the player that needs to be removed
     */
    public static int removePlayerProfile(int playerID) {
        try {
            removePlayerFromFile("src/networking/database/player_profile_info.csv", playerID);
            removePlayerFromFile("src/networking/database/player_game_stats.csv", playerID);
            removePlayerFromFile("src/networking/database/player_match_history.csv", playerID);
            removePlayerFromFile("src/networking/database/friend_requests.csv", playerID);
            removePlayerFromFile("src/networking/database/block_history.csv", playerID);
            removePlayerFromFile("src/networking/database/player_notifications.csv", playerID);
            return 1;
        } catch (RuntimeException e) {
            System.out.println("Error removing player profile: " + e.getMessage());
            return -1;
        }
    }
    /**
     * a method that rewrites the file for removing a player
     * @param filename: the file that the player needs to be removed from
     * @param playerID: the player that needs to be removed
     */
    protected static void removePlayerFromFile(String filename, int playerID) {
        writeLock.lock();
        try {
            File inputFile = new File(filename);
            File tempFile = new File("src/networking/database/temp.csv");
            boolean playerFound = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split("~");
                    // If it's the correct player, update the field
                    if (!(Integer.parseInt(values[0]) == playerID)) {
                        writer.write(String.join("~", line));
                        writer.newLine();
                    } else {
                        playerFound = true;
                    }
                }
                reader.close();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!playerFound) {
                throw new RuntimeException("Could not find player with id " + playerID);
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
//            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
//                System.out.println("File updated successfully.");
//            } else {
//                System.out.println("Error updating file.");
//            }
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * a method for getting a backup for all files
     */
    public static void backupDatabase(){
        backupDatabaseFile("src/networking/database/player_profile_info.csv", "src/networking/database_backup/player_profile_info_backup.csv");
        backupDatabaseFile("src/networking/database/player_match_history.csv", "src/networking/database_backup/player_match_history_backup.csv");
        backupDatabaseFile("src/networking/database/player_game_stats.csv", "src/networking/database_backup/player_game_stats_backup.csv");
        backupDatabaseFile("src/networking/database/match_history.csv", "src/networking/database_backup/match_history_backup.csv");
        backupDatabaseFile("src/networking/database/friend_requests.csv", "src/networking/database_backup/friend_requests_backup.csv");
        backupDatabaseFile("src/networking/database/block_history.csv", "src/networking/database_backup/block_history_backup.csv");
        backupDatabaseFile("src/networking/database/player_notifications.csv", "src/networking/database_backup/player_notifications_backup.csv");
    }

    /**
     * a reader and writer to back up the data
     * @param filename: the file that we need a backup from
     * @param fileBackupName: the backup file
     */
    private static void backupDatabaseFile(String filename, String fileBackupName){
        writeLock.lock();
        try {
            if (!filename.equals(fileBackupName)) {
                File fileToBackup = new File(filename);
                File backupFile = new File(fileBackupName);
                if (backupFile.exists()) {
                    backupFile.delete();
                }
                if (fileToBackup.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileToBackup));
                         BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();
                        }
                        reader.close();
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("File does not exist.");
                }
            }
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * a method for clearing the database
     */
    public static void clearDatabase(){
        clearDatabaseFile("src/networking/database/player_profile_info.csv");
        clearDatabaseFile("src/networking/database/player_game_stats.csv");
        clearDatabaseFile("src/networking/database/player_match_history.csv");
        clearDatabaseFile("src/networking/database/friend_requests.csv");
        clearDatabaseFile("src/networking/database/block_history.csv");
        clearDatabaseFile("src/networking/database/match_history.csv");
        clearDatabaseFile("src/networking/database/player_notifications.csv");
    }

    /**
     * the writer that does the clearing
     * @param filename: the file that needs to be cleared
     */
    private static void clearDatabaseFile(String filename) {
        File fileToClear = new File(filename);
        File newFile = new File("src/networking/database/temp.csv");
        try {
            newFile.createNewFile();
            fileToClear.delete();
            newFile.renameTo(fileToClear);
//            if(fileToClear.delete() && newFile.renameTo(fileToClear)) {
//                System.out.println("File cleared.");
//            }
//            else{
//                System.out.println("Error clearing file.");
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method for getting a backup and clearing the database
     */
    public static void backupAndClearDatabase() {
        backupAndClearDatabaseFile("src/networking/database/player_profile_info.csv", "src/networking/database_backup/player_profile_info_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/player_match_history.csv", "src/networking/database_backup/player_match_history_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/player_game_stats.csv", "src/networking/database_backup/player_game_stats_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/match_history.csv", "src/networking/database_backup/match_history_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/friend_requests.csv", "src/networking/database_backup/friend_requests_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/block_history.csv", "src/networking/database_backup/block_history_backup.csv");
        backupAndClearDatabaseFile("src/networking/database/player_notifications.csv", "src/networking/database_backup/player_notifications_backup.csv");

    }

    /**
     * method for getting a backup and clearing the database
     * @param filename: the file that needs the backup and clearing process
     * @param fileBackupName: the file resulting from the process
     */
    private static void backupAndClearDatabaseFile(String filename, String fileBackupName) {
        File fileToBackup = new File(filename);
        File backupFile = new File(fileBackupName);
        if (backupFile.exists()) {
            backupFile.delete();
        }
        if (fileToBackup.exists()) {
            if (fileToBackup.renameTo(new File(fileBackupName))){
                File newBlankFile = new File(filename);
                try {
                    newBlankFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else{
            throw new RuntimeException("File does not exist.");
        }
    }

    /**
     * a method for restoring the database
     */
    public static void restoreDatabaseBackupAndOverrideCurrent(){
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/player_profile_info.csv", "src/networking/database_backup/player_profile_info_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/player_match_history.csv", "src/networking/database_backup/player_match_history_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/player_game_stats.csv", "src/networking/database_backup/player_game_stats_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/match_history.csv", "src/networking/database_backup/match_history_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/friend_requests.csv", "src/networking/database_backup/friend_requests_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/block_history.csv", "src/networking/database_backup/block_history_backup.csv");
        restoreDatabaseFileBackupAndOverrideCurrent("src/networking/database/player_notifications.csv", "src/networking/database_backup/player_notifications_backup.csv");

    }

    /**
     * method for restoring the database
     * @param filename: the file that needs to be restores
     * @param backupFileName: the backup file to override
     */
    private static void restoreDatabaseFileBackupAndOverrideCurrent(String filename, String backupFileName) {
        File fileToRestore = new File(filename);
        File backupFile = new File(backupFileName);
        if (backupFile.exists()) {
            if (fileToRestore.delete() && backupFile.renameTo(fileToRestore)) {
//                System.out.println("File restored.");
            }
        }
        else{
            throw new RuntimeException("File does not exist.");
        }
    }

}
