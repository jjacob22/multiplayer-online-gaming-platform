package networking;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a class for all getters and setters related to profile and authentication
 */
public class ProfileDatabaseManager extends AbstractDatabaseManager{

    public ProfileDatabaseManager() {}
    //---------------------USERNAME------------------------
    /**
     * getter for player username
     * @param playerID: the unique ID that distinguishes the player
     * @return: the username as a string
     */
    public String getPlayerUsername(int playerID) {
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[1].equals("null")) {
            return values[1];
        }return null;
    }
    /**
     * the setter for username
     * @param playerID: the ID of the player
     * @param username: the chosen username
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerUsername(int playerID, String username) {
        String filename = "src/networking/database/player_profile_info.csv";
        return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, username, 1);
    }

    //---------------------PASSWORD------------------------
    /**
     * getter for player password (the hash)
     * @param playerID: the unique ID that distinguishes the player
     * @return: the password as a string
     */
    public String getPlayerPass(int playerID){
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[2].equals("null")) {
            return values[2];
        }return null;
    }
    /**
     * the setter for password
     * @param playerID: the ID of the player
     * @param playerPass: hash of the password
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerPass(int playerID, String playerPass){
        String filename = "src/networking/database/player_profile_info.csv";
        if (playerPass != null) {
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, playerPass, 2);
        }
        return -1;
    }
    //---------------------EMAIL------------------------
    /**
     * getter for player's email address
     * @param playerID: the unique ID that distinguishes the player
     * @return: the email address as a string
     */
    public String getEmail(int playerID) {
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[3].equals("null")) {
            return values[3];
        }return null;
    }
    /**
     * the setter for email address
     * @param playerID: the ID of the player
     * @param email: email address to be stored
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setEmail(int playerID, String email) {
        String filename = "src/networking/database/player_profile_info.csv";
        if (email != null) {
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, email, 3);
        }
        return -1;
    }
    //---------------------BIO------------------------
    /**
     * getter for player's bio
     * @param playerID: the unique ID that distinguishes the player
     * @return: the bio as a string
     */
    public String getBio(int playerID) {
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[4].equals("null")) {
            return values[4];
        }return null;
    }
    /**
     * the setter for bio
     * @param playerID: the ID of the player
     * @param bio: bio to be stored
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setBio(int playerID, String bio) {
        String filename = "src/networking/database/player_profile_info.csv";
        if (bio != null) {
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, bio, 4);
        }
        return -1;
    }

    //---------------------FRIENDS LIST------------------------
    /**
     * getter for player's friends
     * @param playerID: the unique ID that distinguishes the player
     * @return: an int[] containing all friend's IDs
     */
    public int[] getFriendList(int playerID) {
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        String[] friendsListString = values[6].split("-");
        if (friendsListString.length > 0) {
            if (!friendsListString[0].equals("0") && !friendsListString[0].equals("")) {
                int[] friendList = new int[friendsListString.length];
                for (int i = 0; i < friendsListString.length; i++){
                    friendList[i] = Integer.parseInt(friendsListString[i]);
                }return friendList;
            }
        }
        return new int[0];
    }
    /**
     * the setter for friends list
     * @param playerID: the ID of the player
     * @param friendsList: the list of friends
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setFriendList(int playerID, int[] friendsList) {
        if (friendsList.length > 0) {
            int index_of_value = 6;
            String filename = "src/networking/database/player_profile_info.csv";
            System.out.println(friendsList[0]);
            StringBuilder stringOfFriendsList = new StringBuilder(String.valueOf(friendsList[0]));
            for (int i = 1; i < friendsList.length; i++) {
                stringOfFriendsList.append("-").append(friendsList[i]);
            }
            System.out.println(stringOfFriendsList.toString());
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, stringOfFriendsList.toString(), index_of_value);
        }
        return -1;
    }
    /**
     * method for adding a friend to a list
     * @param playerID: ID of the player
     * @param friendID: ID of the friend that needs to be added
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addFriend(int playerID, int friendID) {
        int[] currentFriendsList = getFriendList(playerID);
        int[] newFriendsList = new int[currentFriendsList.length + 1];
        if (currentFriendsList.length > 0) {
            for (int i = 0; i < currentFriendsList.length; i++) {
                newFriendsList[i] = currentFriendsList[i];
            }
            newFriendsList[newFriendsList.length - 1] = friendID;
        } else {
            newFriendsList[0] = friendID;
        }
        setFriendList(playerID, newFriendsList);
        return 1;
    }
    /**
     * method for removing a friend to a list
     * @param playerID: ID of the player
     * @param friendID: ID of the friend that needs to be removed
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int removeFriend(int playerID, int friendID) {
        boolean friendFound = false;
        int[] friendsList = getFriendList(playerID);
        int[] newFriendsList = new int[friendsList.length];
        int newIndex = 0;
        if (friendsList.length > 0) {
            for (int i = 0; i < friendsList.length; i++) {
                if (friendsList[i] == friendID) {
                    friendFound = true;
                    continue;
                }
                newFriendsList[newIndex++] = friendsList[i];
            }
            if (!friendFound) {
                return -1;
            }
            newFriendsList = Arrays.copyOf(newFriendsList, newIndex);
        } else {
            return -1;
        }
        setFriendList(playerID, newFriendsList);
        return 1;
    }

    //---------------------AVATAR------------------------
    /**
     * getter for player's avatar(each avatar has a related integer)
     * @param playerID: the unique ID that distinguishes the player
     * @return: an int related to the avatar
     */
    public int getAvatar(int playerID) {
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[7].equals("null")) {
            return Integer.parseInt(values[7]);
        }return -1;
    }
    /**
     * the setter for avatar
     * @param playerID: the ID of the player
     * @param avatarID: the ID of the avatar
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setAvatar(int playerID, int avatarID) {
        String filename = "src/networking/database/player_profile_info.csv";
        return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, String.valueOf(avatarID), 7);
    }

    //---------------------SALT------------------------
    /**
     * getter for player's password salt
     * @param playerID: the unique ID that distinguishes the player
     * @return: salt as a string
     */
    public String getPlayerSalt(int playerID){
        String filename = "src/networking/database/player_profile_info.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PROFILE);
        if (!values[5].equals("null")) {
            return values[5];
        }return null;
    }
    /**
     * the setter for salt
     * @param playerID: the ID of the player
     * @param salt: the salt for the player
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerSalt(int playerID, String salt) {
        String filename = "src/networking/database/player_profile_info.csv";
        return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PROFILE, salt, 5);
    }

    //---------------------BLOCK LIST------------------------
    /**
     * getter for IDs that specific player has blocked them
     * @param playerID: the unique ID that distinguishes the player
     * @return: int[] containing the IDs that the player has blocked
     */
    public int[] getHasBlocked(int playerID) {
        String filename = "src/networking/database/block_history.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_BLOCK_HISTORY);
        String[] hasBlockedListString = values[1].split("-");
        if (hasBlockedListString.length > 0) {
            if (!hasBlockedListString[0].equals("0") && !hasBlockedListString[0].equals("null")) {
                int[] blockedList = new int[hasBlockedListString.length];
                for (int i = 0; i < hasBlockedListString.length; i++){
                    blockedList[i] = Integer.parseInt(hasBlockedListString[i]);
                }return blockedList;
            }
        }
        return new int[0];
    }
    /**
     * the setter for the list that a player has blocked
     * @param playerID: the ID of the player
     * @param blockedUsers: the IDs that a player has blocked them
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setHasBlocked(int playerID, int[] blockedUsers) {
        if (blockedUsers.length > 0) {
            String filename = "src/networking/database/block_history.csv";
            StringBuilder stringOfBlockedList = new StringBuilder(String.valueOf(blockedUsers[0]));
            for (int i = 1; i < blockedUsers.length; i++) {
                stringOfBlockedList.append("-").append(blockedUsers[i]);
            }
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_BLOCK_HISTORY, stringOfBlockedList.toString(), 1);
        }
        return -1;
    }
    /**
     * adding an ID to the IDs that a player has blocked
     * @param playerID: ID of the player
     * @param blockedID: the ID that has been blocked
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addHasBlocked(int playerID, int blockedID) {
        int[] currentBlockedList = getHasBlocked(playerID);
        int[] newBlockedList = new int[currentBlockedList.length + 1];
        if (currentBlockedList.length > 0) {
            for (int i = 0; i < currentBlockedList.length; i++) {
                newBlockedList[i] = currentBlockedList[i];
            }
            newBlockedList[newBlockedList.length - 1] = blockedID;
        } else {
            newBlockedList[0] = blockedID;
        }
        setHasBlocked(playerID, newBlockedList);
        return 1;
    }
    /**
     * removing an ID from the IDs that a player has blocked
     * @param playerID: ID of the player
     * @param blockedID: the ID that has been unblocked
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int removeHasBlocked(int playerID, int blockedID) {
        boolean userFound = false;
        int[] blockedList = getHasBlocked(playerID);
        int[] newBlockedList = new int[blockedList.length];
        int newIndex = 0;
        if (blockedList.length > 0) {
            for (int i = 0; i < blockedList.length; i++) {
                if (blockedList[i] == blockedID) {
                    userFound = true;
                    continue;
                }
                newBlockedList[newIndex++] = blockedList[i];
            }
            if (!userFound) {
                return -1;
            }
            newBlockedList = Arrays.copyOf(newBlockedList, newIndex);
        } else {
            return -1;
        }
        setHasBlocked(playerID, newBlockedList);
        return 1;
    }

    /**
     * getter for IDs that specific player has been blocked by them
     * @param playerID: the unique ID that distinguishes the player
     * @return: int[] containing the IDs that the player has been blocked by
     */
    public int[] getBlockedBy(int playerID){
        String filename = "src/networking/database/block_history.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_BLOCK_HISTORY);
        String[] blockedByListString = values[2].split("-");
        if (blockedByListString.length > 0) {
            if (!blockedByListString[0].equals("0") && !blockedByListString[0].equals("null")) {
                int[] blockedByList = new int[blockedByListString.length];
                for (int i = 0; i < blockedByListString.length; i++){
                    blockedByList[i] = Integer.parseInt(blockedByListString[i]);
                }return blockedByList;
            }
        }
        return new int[0];
    }
    /**
     * the setter for the list that a player has been blocked by
     * @param playerID: the ID of the player
     * @param blockers: the IDs that a player has been blocked by them
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setBlockedBy(int playerID, int[] blockers){
        if (blockers.length > 0) {
            String filename = "src/networking/database/block_history.csv";
            StringBuilder stringOfBlockersList = new StringBuilder(String.valueOf(blockers[0]));
            for (int i = 1; i < blockers.length; i++) {
                stringOfBlockersList.append("-").append(blockers[i]);
            }
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_BLOCK_HISTORY, stringOfBlockersList.toString(), 2);
        }
        return -1;
    }
    /**
     * adding an ID to the IDs that a player has been blocked by
     * @param playerID: ID of the player
     * @param blockerID: the ID that has blocked the player
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addBlockedBy(int playerID, int blockerID){
        int[] currentBlockersList = getBlockedBy(playerID);
        int[] newBlockersList = new int[currentBlockersList.length + 1];
        if (currentBlockersList.length > 0) {
            for (int i = 0; i < currentBlockersList.length; i++) {
                newBlockersList[i] = currentBlockersList[i];
            }
            newBlockersList[newBlockersList.length - 1] = blockerID;
        } else {
            newBlockersList[0] = blockerID;
        }
        setBlockedBy(playerID, newBlockersList);
        return 1;
    }
    /**
     * removing an ID from the IDs that a player has been blocked by
     * @param playerID: ID of the player
     * @param blockerID: the ID that has unblocked the player
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int removeBlockedBy(int playerID, int blockerID){
        boolean userFound = false;
        int[] blockersList = getBlockedBy(playerID);
        int[] newBlockersList = new int[blockersList.length];
        int newIndex = 0;
        if (blockersList.length > 0) {
            for (int i = 0; i < blockersList.length; i++) {
                if (blockersList[i] == blockerID) {
                    userFound = true;
                    continue;
                }
                newBlockersList[newIndex++] = blockersList[i];
            }
            if (!userFound) {
                return -1;
            }
            newBlockersList = Arrays.copyOf(newBlockersList, newIndex);
        } else {
            return -1;
        }
        setBlockedBy(playerID, newBlockersList);
        return 1;
    }

    //---------------------FRIENDS REQUEST------------------------
    /**
     * getter for IDs that specific player got a friends request from
     * @param playerID: the unique ID that distinguishes the player
     * @return: int[] containing the IDs that the player got a friends request from
     */
    public int[] getFriendsRequests(int playerID){
        String filename = "src/networking/database/friend_requests.csv";
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_FRIEND_REQUESTS);
        String[] requestsListString = values[1].split("-");
        if (requestsListString.length > 0) {
            if (!requestsListString[0].equals("0") && !requestsListString[0].equals("null")) {
                int[] requestList = new int[requestsListString.length];
                for (int i = 0; i < requestsListString.length; i++){
                    requestList[i] = Integer.parseInt(requestsListString[i]);
                }return requestList;
            }
        }
        return new int[0];
    }
    /**
     * the setter for the list that a player got a friends request from
     * @param playerID: the ID of the player
     * @param senderIDs: the IDs that send the friends request
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setFriendsRequests(int playerID, int[] senderIDs){
        if (senderIDs.length > 0) {
            String filename = "src/networking/database/friend_requests.csv";
            StringBuilder stringOfRequests = new StringBuilder(String.valueOf(senderIDs[0]));
            for (int i = 1; i < senderIDs.length; i++) {
                stringOfRequests.append("-").append(senderIDs[i]);
            }
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_FRIEND_REQUESTS, stringOfRequests.toString(), 1);
        }
        return -1;

    }
    /**
     * adding an ID to the IDs that a player got a friends request from
     * @param playerID1: ID of the player
     * @param playerID2: the ID that has sent the request
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addRequest(int playerID1, int playerID2) {
        int[] currentRequestsList = getFriendsRequests(playerID1);
        int[] newRequestsList = new int[currentRequestsList.length + 1];
        if (currentRequestsList.length > 0) {
            for (int i = 0; i < currentRequestsList.length; i++) {
                newRequestsList[i] = currentRequestsList[i];
            }
            newRequestsList[newRequestsList.length - 1] = playerID2;
        } else {
            newRequestsList[0] = playerID2;
        }
        setFriendsRequests(playerID1, newRequestsList);
        return 1;
    }
    /**
     * removing an ID from the IDs that a player got a friends request from (accepted or denied or deleted)
     * @param playerID1: ID of the player that accepted or denied
     * @param playerID2: the ID that has sent the request
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int removeFriendRequest(int playerID1, int playerID2){
        boolean userFound = false;
        int[] requestsList = getFriendsRequests(playerID1);
        int[] newRequestsList = new int[requestsList.length];
        int newIndex = 0;
        if (requestsList.length > 0) {
            for (int i = 0; i < requestsList.length; i++) {
                if (requestsList[i] == playerID2) {
                    userFound = true;
                    continue;
                }
                newRequestsList[newIndex++] = requestsList[i];
            }
            if (!userFound) {
                return -1;
            }
            newRequestsList = Arrays.copyOf(newRequestsList, newIndex);
        } else {
            return -1;
        }
        setFriendsRequests(playerID1, newRequestsList);
        return 1;
    }
    //---------------------NOTIFICATIONS------------------------
    /**
     * getter for player's notifications
     * @param playerID: the unique ID that distinguishes the player
     * @return: String[] containing the notifications
     */
    public String[] getNotifications(int playerID){
        String[] notifsConcat = readDatabaseFileForPlayerID("src/networking/database/player_notifications.csv", playerID, TOTAL_VALUES_PLAYER_NOTIFICATIONS);
        String[] notifications = notifsConcat[1].split("-");
        if (notifications.length > 0){
            if (!notifications[0].equals("0") && !notifications[0].equals("null")) {
                String[] notifList = new String[notifications.length];
                for (int i = 0; i < notifications.length; i++){
                    notifList[i] = notifications[i];
                }return notifList;
            }
        }
        return new String[0];
    }
    /**
     * the setter for player's notifications
     * @param playerID: the ID of the player
     * @param notifications: the notifications for a player
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setNotifications(int playerID, String[] notifications){
        if(notifications.length > 0) {
            String filename = "src/networking/database/player_notifications.csv";
            String notifsConcat = String.join("-", notifications);
            return writeValueToDatabaseFileForPlayerID(filename, playerID, TOTAL_VALUES_PLAYER_NOTIFICATIONS, notifsConcat, 1);
        }
        return -1;
    }
    /**
     * adding a notification for a player
     * @param playerID: ID of the player
     * @param notification: the new notification
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addNotification(int playerID, String notification){
        String[] notifications = getNotifications(playerID);
        String filename = "src/networking/database/player_notifications.csv";
        if (notifications.length > 0) {
            if (notifications[0] != null) {
                String[] newNotifications = new String[notifications.length+1];
                for (int i = 0; i < notifications.length; i++) {
                    newNotifications[i] = notifications[i];
                }
                newNotifications[newNotifications.length-1] = notification;
                return setNotifications(playerID, newNotifications);
            }
        }
        String[] newNotifications = new String[1];
        newNotifications[0] = notification;
        return setNotifications(playerID, newNotifications);
    }
    /**
     * removing a notification for a player
     * @param playerID: ID of the player
     * @param notification: the deleted notification
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int removeNotification(int playerID, String notification){
        String[] notifications = getNotifications(playerID);
        if (notifications.length > 0) {
            ArrayList<String> newNotifsArray = new ArrayList<>();
            for (int i = 0; i < notifications.length; i++) {
                if (!notifications[i].equals(notification)) {
                    newNotifsArray.add(notifications[i]);
                }
            }
            String[] newNotifications = newNotifsArray.toArray(new String[newNotifsArray.size()]);
            return setNotifications(playerID, newNotifications);
        }
        return -1;
    }
}
