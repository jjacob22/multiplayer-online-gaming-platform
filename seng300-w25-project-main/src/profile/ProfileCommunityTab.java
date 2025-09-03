package profile;

import networking.AbstractDatabaseManager;
import networking.ProfileDatabaseManager;

import java.util.Arrays;
import java.util.HashMap;

/**
 * This class represents the Community Tab in the Online Multiplayer
 * board Game (OMG) platform. It allows users to manage their social
 * interactions, including viewing and managing friends, handling friend
 * requests, blocking users, and viewing the leaderboard.
 */
public class ProfileCommunityTab {

    private ProfileDatabaseManager profileDatabaseManager = new ProfileDatabaseManager();
    private boolean testing = false; // this will only be used in testing. when true, it bypasses certain checks so we can test every scenario


    public ProfileCommunityTab(){
    }


    ProfileCommunityTab(ProfileDatabaseManager profileDatabaseManager){
        this.profileDatabaseManager = profileDatabaseManager;
        testing = true;
    }

    /**
     * Sends a friend request from sender to receiver
     * After ensuring that neither player is blocked by the other, it adds to the receiver's incoming friend requests,
     * and adds a notification to their centre.
     *
     * @param senderID is the id of the user who is sending a friend request
     * @param receiverID is the id of the user who they want to be friends with
     * @return whether it was successful
     */
    public boolean sendFriendRequest(int senderID, int receiverID){
        if (usersValid(senderID,receiverID)){
            boolean receiverBlocked = isOnList(profileDatabaseManager.getHasBlocked(receiverID),senderID);
            boolean senderBlocker = isOnList(profileDatabaseManager.getHasBlocked(senderID),receiverID);
            boolean alreadyRequested = isOnList(profileDatabaseManager.getFriendsRequests(receiverID),senderID);
            boolean alreadyFriends = isOnList(profileDatabaseManager.getFriendList(senderID),receiverID);

            if(!(receiverBlocked||senderBlocker||alreadyRequested||alreadyFriends)){
                profileDatabaseManager.addRequest(receiverID,senderID);
                Notification notification = new Notification(receiverID,senderID,profileDatabaseManager);
                profileDatabaseManager.addNotification(receiverID,notification.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Accepts a friend request that had been received, deleting the notification.
     * @param userID is the id of the user who wants to accept the request
     * @param friendID is the id of the user who sent the friend request
     * @return whether it was successful
     */

    public boolean acceptFriendRequest(int userID, int friendID){
        if (usersValid(userID,friendID) && isOnList(profileDatabaseManager.getFriendsRequests(userID),friendID)){
            profileDatabaseManager.removeFriendRequest(userID, friendID);
            profileDatabaseManager.addFriend(userID,friendID);
            profileDatabaseManager.addFriend(friendID,userID);
            Notification notification = new Notification(userID,friendID,profileDatabaseManager);
            profileDatabaseManager.removeNotification(userID,notification.toString());
            return true;
        }
        return false;
    }

    /**
     * Declines a friend request that had been received, deleting the notification.
     * @param userID is the id of the user who wants to decline a request
     * @param senderID is the id of the user who sent the request
     * @return whether it was successful
     */
    public boolean declineFriendRequest(int userID, int senderID){
        if (usersValid(userID,senderID) && isOnList(profileDatabaseManager.getFriendsRequests(userID),senderID)){
            int success1 = profileDatabaseManager.removeFriendRequest(userID, senderID);
            Notification notification = new Notification(userID,senderID,profileDatabaseManager);
            int success2 = profileDatabaseManager.removeNotification(userID,notification.toString());
            return (success1+success2)==2;
        }
        return false;
    }

    /**
     * Removes two users from each others friend lists.
     * @param userID is the id of the user who wants to unfriend someone
     * @param friendID is the id of the user they want to unfriend
     * @return whether it was successful
     */

    public boolean removeFriend(int userID, int friendID){
        if (usersValid(userID,friendID) && isOnList(profileDatabaseManager.getFriendList(userID),friendID)){
            int success1 = profileDatabaseManager.removeFriend(userID, friendID);
            int success2 = profileDatabaseManager.removeFriend(friendID,userID);
            return (success1+success2)==2;
        }
        return false;
    }

    /**
     * Gets a convenient list for viewing friends in this format <Username, Avatar>.
     * @param userID is the player whose friends we want displayed
     * @return a HashMap. The key for each pair is a String and the value is the avatar's corresponding Integer
     */
    public HashMap<String,Integer> viewFriendsList(int userID){
        if (AbstractDatabaseManager.playerExists(userID) || testing){
            HashMap<String, Integer> friendList = new HashMap<String,Integer>();
            int[] friendIDs = profileDatabaseManager.getFriendList(userID);
            for (int friendID: friendIDs){
                String username = profileDatabaseManager.getPlayerUsername(friendID);
                int avatar = profileDatabaseManager.getAvatar(friendID);
                friendList.put(username, avatar);
            }
            return friendList;
        }
        return new HashMap<String,Integer>();
    }

    /**
     * Allows a user to block another. If they were friends, this is undone.
     * @param playerID is the id of the user who wants to block someone
     * @param blockedID is the id of the user who they want to block
     * @return whether it was successful
     */

    public boolean blockUser(int playerID, int blockedID){
        if (usersValid(playerID,blockedID) && !isOnList(profileDatabaseManager.getHasBlocked(playerID),blockedID)){
            removeFriend(playerID, blockedID);
            declineFriendRequest(playerID, blockedID);
            declineFriendRequest(blockedID, playerID);
            profileDatabaseManager.addBlockedBy(blockedID, playerID);
            profileDatabaseManager.addHasBlocked(playerID, blockedID);
            return true;
        }
        return false;

    }

    /**
     * Unblocks a user.
     * @param userID is the id of the user who wants to unblock someone
     * @param blockedID is the id of the user they want unblocked
     * @return whether it was successful
     */

    public boolean unBlockUser(int userID, int blockedID){
        if (usersValid(userID,blockedID) && isOnList(profileDatabaseManager.getHasBlocked(userID),blockedID)){
            int hasBlockedSuccess = profileDatabaseManager.removeHasBlocked(userID,blockedID);
            int blockedBySuccess = profileDatabaseManager.removeBlockedBy(blockedID,userID);
            return (hasBlockedSuccess+blockedBySuccess)==2;
        }
        return false;
    }
    /**
     * Gets a convenient list for viewing blocked users
     * @param userID is the player whose blockees we want displayed
     * @return a HashMap. The key for each pair is a String and the value is the avatar's corresponding Integer
     */
    public HashMap<String,Integer> viewBlockedList(int userID){
        if (AbstractDatabaseManager.playerExists(userID) || testing){
            HashMap<String, Integer> blockedList = new HashMap<String,Integer>();
            int[] blockedIDs = profileDatabaseManager.getHasBlocked(userID);
            for (int blockedID: blockedIDs){
                String username = profileDatabaseManager.getPlayerUsername(blockedID);
                int avatar = profileDatabaseManager.getAvatar(blockedID);
                blockedList.put(username, avatar);
            }
            return blockedList;
        }
        return new HashMap<String,Integer>();
    }
    /**
     * Checks if a user is on a specific list of user ids
     * @param list is the list to check
     * @param id is the id to look for in the list
     * @return true if the id is found on the list, false otherwise
     */
    private boolean isOnList(int[] list, int id){
        for(int i=0;i<list.length;i++){
            if (list[i]==id){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if two specific users exist
     * @param id1 is the first user id to check
     * @param id2 is the second user id to check
     * @return whether both users exist
     */

    private boolean usersValid(int id1, int id2){
        return id1 !=id2 && ((AbstractDatabaseManager.playerExists(id1) && AbstractDatabaseManager.playerExists(id2))  || testing);
    }
}
