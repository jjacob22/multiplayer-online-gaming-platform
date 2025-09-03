package profile;

import match_making.Game;
import networking.AbstractDatabaseManager;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;

import java.util.ArrayList;

public class Profile {
    private final int userID;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String bio;
    private int avatar;
    private ArrayList<Integer> friendIDs;
    private ArrayList<Integer> blockerIDs;
    private ArrayList<Integer> blockedIDs;
    private ArrayList<Integer> requestorIDs;
    private int elo;
    private int gameWins;
    private int gamesPlayed;
    private int timePlayed;
    private ArrayList<Notification> notificationCentre = new ArrayList<>();
    private ProfileDatabaseManager profileDatabaseManager = new ProfileDatabaseManager();
    private static MatchDatabaseManager matchDatabaseManager = new MatchDatabaseManager();


    /**
     * Constructor for new users. This is for authentication. Checks the database and assigns a unique userid.
     * Likely, the avatar will be null since they might not have selected those yet.
     */
    Profile(String username, String password, String salt, String email, int avatar){

        // assigning a unique userID (not yet in data base)

        // adding values to the profile object
        this.userID = findNextID();
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.avatar = avatar;

        // storing information in the database
        AbstractDatabaseManager.createNewProfile(userID);
        profileDatabaseManager.setPlayerUsername(userID, username);
        profileDatabaseManager.setPlayerPass(userID, password);
        profileDatabaseManager.setPlayerSalt(userID, salt);
        profileDatabaseManager.setEmail(userID, email);
        profileDatabaseManager.setAvatar(userID, avatar);
    }

    Profile(int userID, String username, String bio, String password, String salt, String email, int avatar){
        this.userID = userID;
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.avatar = avatar;
    }

    Profile(String username, int userID, ArrayList<Integer> friendIDs, ArrayList<Integer> requestorIDs, ArrayList<Integer> blockedIDs, ArrayList<Integer> blockerIDs, ProfileDatabaseManager profileDatabaseManager){
        this.username = username;
        this.userID = userID;
        this.friendIDs = friendIDs;
        this.requestorIDs = requestorIDs;
        this.blockedIDs = blockedIDs;
        this.blockerIDs = blockerIDs;
        this.profileDatabaseManager = profileDatabaseManager;
    }

    /**
     * Users do not choose their own username - so we pick a unique one that is never changed
     * @return the next available UserID
     */
    private static int findNextID() {
        int[] ids = AbstractDatabaseManager.getAllPlayerIDs();
        int userID = 10000000;    // default value if there's no IDs taken yet

        if (ids.length>0){      // if there are existing IDs, then we must make sure ours isn't taken

            /*  in case there is an error in the order of the array, we will always double-check that it's unique by
                iterating through the existing IDs
            */

            boolean unique = false; // this indicates that the ID is taken, turns true once it is confirmed unique

            while(!unique){
                unique = true;
                userID= ids[ids.length-1]+1;     // the next available ID
                for(int id: ids){
                    if (id==userID){
                        unique = false;         // make sure we run through this again
                        userID++;               // iterate to the next ID
                        break;
                    }
                }
            }
        }
        return userID;
    }

    public int getID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        profileDatabaseManager.setPlayerUsername(userID, username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        profileDatabaseManager.setPlayerPass(userID, password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
        profileDatabaseManager.setBio(userID, bio);
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
        profileDatabaseManager.setPlayerSalt(userID, salt);   // salt is not yet added to database
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
        profileDatabaseManager.setAvatar(userID, avatar);
    }

    public void addFriend(int friendID){
        friendIDs.add(friendID);
    }

    public void removeFriend(int friendID){
        for (int i = 0; i<friendIDs.size(); i++){
            if (friendIDs.get(i)==friendID){
                friendIDs.remove(i);
                profileDatabaseManager.removeFriend(userID,friendID);
            }
        }
    }

    public void addBlocker(int blockerID){
        blockerIDs.add(blockerID);
    }

    public void removeBlocker(int blockerID){
        for (int i = 0; i<blockerIDs.size(); i++){
            if (blockerIDs.get(i)==blockerID){
                blockerIDs.remove(i);
                profileDatabaseManager.removeBlockedBy(userID,blockerID);
            }
        }
    }

    public void addBlocked(int blockedID){
        blockedIDs.add(blockedID);
    }

    public void removeBlocked(int blockedID){
        for (int i = 0; i<blockedIDs.size(); i++){
            if (blockedIDs.get(i)==blockedID){
                blockedIDs.remove(i);
//                profileDatabaseManager.removeHasBlocked(userID,blockedID);
            }
        }
    }

    public void addFriendRequest(int senderID){
        requestorIDs.add(senderID);
    }

    public void removeFriendRequest(int senderID){
        for (int i = 0; i<requestorIDs.size(); i++){
            if (requestorIDs.get(i)==senderID){
                requestorIDs.remove(i);
            }
        }
    }

    public int[] getBlockerIDs(){
        if (blockerIDs.isEmpty()){
            return new int[]{};
        }

        int[] result = new int[blockerIDs.size()];
        int i=0;
        for (int blockerID : blockerIDs){
            result[i]=blockerID;
            i++;
        }
        return result;
    }

    public  int[] getBlockedIDs(){
        if (blockedIDs.isEmpty()){
            return new int[]{};
        }
        int[] result = new int[blockedIDs.size()];
        int i=0;
        for (int blockedID : blockedIDs){
            result[i]=blockedID;
            i++;
        }
        return result;
    }

    public int[] getFriendIDs(){
        if (friendIDs.isEmpty()){
            return new int[]{};
        }
        int[] result = new int[friendIDs.size()];
        int i=0;
        for (int friendID : friendIDs){
            result[i]=friendID;
            i++;
        }
        return result;
    }

    public int[] getFriendRequests(){
        if (requestorIDs.isEmpty()){
            return new int[]{};
        }
        int[] result = new int[requestorIDs.size()];
        int i=0;
        for (int requestorID : requestorIDs){
            result[i]=requestorID;
            i++;
        }
        return result;
    }

}
