package profile;

import networking.ProfileDatabaseManager;

public class Notification {
    private int userID;     // the ID of the profile it's associated with
    private int senderID;   // the ID of the person who sent the request
    private final ProfileDatabaseManager profileDatabaseManager;


    Notification(int userID, int senderID){
        this.userID = userID;
        this.senderID = senderID;
        this.profileDatabaseManager = new ProfileDatabaseManager();
    }
    Notification(int userID, int senderID, ProfileDatabaseManager profileDatabaseManager){
        this.userID = userID;
        this.senderID = senderID;
        this.profileDatabaseManager = profileDatabaseManager;
    }

    /**
     * @return the notification as a String
     */

    @Override
    public String toString() {
        return profileDatabaseManager.getPlayerUsername(senderID) + " wants to be your friend!";
    }
}

