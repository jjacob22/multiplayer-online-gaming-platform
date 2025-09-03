package networking;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import leaderboard.GameStatistics;
import leaderboard.Leaderboard;
import networking.requestMessages.*;
import networking.responseMessages.*;
import profile.ProfileCommunityTab;
import profile.ProfileSettings;
import profile.UserAuthentication;

import static networking.AbstractDatabaseManager.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream bufferedReader;
    private ObjectOutputStream bufferedWriter;
    private ProfileDatabaseManager profileDb;
    private MatchDatabaseManager matchDb;
    private QueueDatabaseManager queueDb;
    private PlatformServerManager manager;
    private Match match;
    private boolean loggedIn;
    protected int clientID;
    private ProfileSettings profileSettings = new ProfileSettings();
    private ProfileCommunityTab communityTab = new ProfileCommunityTab();

    // TODO: Remove this when integrating actual authentication
    static int ID = 0;

    /**
     * ClientHandler constructor
     *
     * @param socket client socket
     * @param manager platform server manager
     * @param matchDb match database manager
     * @param profileDb profile database manager
     * @param queueDb queue database manager
     */
    public ClientHandler(Socket socket, PlatformServerManager manager, MatchDatabaseManager matchDb, ProfileDatabaseManager profileDb, QueueDatabaseManager queueDb) {
        try {
            this.socket = socket;
            this.bufferedWriter = new ObjectOutputStream(socket.getOutputStream());
            this.bufferedReader = new ObjectInputStream(socket.getInputStream());
            this.matchDb = matchDb;
            this.profileDb = profileDb;
            this.queueDb = queueDb;
            this.manager = manager;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Concurrent Socket Listener
     */
    @Override
    public void run() {
        Object messageFromClient;

        while (socket.isConnected()) {
            try {
                try {
                    messageFromClient = bufferedReader.readObject();
                    System.out.println(this + " Received: " + messageFromClient);
                    handleRequest(messageFromClient);
                } catch (ClassNotFoundException e) {
                    sendMessage(new ExceptionResponse("Invalid request. Request type was not serializable."));
                    break;
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    /**
     * Handle the requests from the client side and call the corresponding helper method to process the request
     *
     * @param message Incoming Request Object
     */
    private void handleRequest(Object message) {
        try {
            if (loggedIn) {
                switch (message) {
                    case FriendRequest m:
                        friendRequest(m);
                        break;
                    case GetFriendRequests m:
                        getFriendRequest(m);
                        break;
                    case AcceptFriendRequest m:
                        acceptFriendRequest(m);
                        break;
                    case DenyFriendRequest m:
                        denyFriendRequest(m);
                        break;
                    case BlockUser m:
                        blockUser(m);
                        break;
                    case UnblockUser m:
                        unblockUser(m);
                        break;
                    case GetBlockedUsers m:
                        getBlockedUsers(m);
                        break;
                    case GetUsername m:
                        getPlayerUsername(m);
                        break;
                    case GetBio m:
                        getBio(m);
                        break;
                    case GetAvatar m:
                        getAvatar(m);
                        break;
                    case GetGameStats m:
                        getGameStat(m);
                        break;
                    case UpdateBio m:
                        updateBio(m);
                        break;
                    case UpdateEmail m:
                        updateEmail(m);
                        break;
                    case VerifyPasswordChange m:
                        verifyPasswordChange(m);
                        break;
                    case UpdatePassword m:
                        updatePassword(m);
                        break;
                    case DeleteProfile m:
                        deleteProfile(m);
                        break;
                    case UpdateUsername m:
                        updateUsername(m);
                        break;
                    case UpdateAvatar m:
                        updateAvatar(m);
                        break;
                    case RequestLeaderboard m:
                        requestLeaderboard(m);
                        break;
                    case FindGame m:
                        findGame(m);
                        break;
                    case LeaveQueue m:
                        leaveQueue(m);
                        break;
                    case HostMatch m:
                        hostMatch(m);
                        break;
                    case UnhostMatch m:
                        unhostMatch(m);
                        break;
                    case RequestHosts m:
                        requestHosts(m);
                        break;
                    case RequestOnlinePlayers m:
                        requestOnlinePlayers(m);
                        break;
                    case JoinMatch m:
                        joinMatch(m);
                        break;
                    case RegisterChallenge m:
                        registerChallenge(m);
                        break;
                    case UnregisterChallenge m:
                        unregisterChallenge(m);
                        break;
                    case RequestChallenges m:
                        requestChallenges(m);
                        break;
                    case AcceptChallenge m:
                        acceptChallenge(m);
                        break;
                    case MovePiece m:
                        movePiece(m);
                        break;
                    case PlacePiece m:
                        placePiece(m);
                        break;
                    case PromotePawn m:
                        match.doAction(new GameAction(m, clientID));
                        break;
                    case ForfeitMatch m:
                        forfeitMatch(m);
                        break;
                    case ChatMessage m:
                        chatMessage(m);
                        break;
                    case LogOut m:
                        logOut(m);
                        break;
                    default:
                        sendMessage(new ExceptionResponse("Invalid request: " + message));
                        break;
                }
            }
            else {
                switch (message) {
                    case LogIn m:
                        userLogin(m);
                        break;
                    case Signup m:
                        userSignup(m);
                        break;
                    case VerifySignup m:
                        verifySignup(m);
                        break;
                    default:
                        sendMessage(new ExceptionResponse("Not logged in, Please log in first."));
                        break;
                }
            }
        } catch (Exception e) {
            sendMessage(new ExceptionResponse("Failed to handle client request: " + e.getMessage()));
        }
    }

    /**
     * Add players to a match
     *
     * @param match match
     */
    public void setMatch(Match match) {
        this.match = match;
        sendMessage(new MatchFoundResponse(match.getGameState(), match.getGame()));
    }

    /**
     * Leave the match
     */
    public void leaveMatch() {
        this.match = null;
    }

    /**
     * Requesting to become a friend
     *
     * @param m FriendRequest Record Object
     */
    private void friendRequest(FriendRequest m){
        if (m.userToFriend() != null) {
            int userToFriend = getPlayerIDFromUsername(m.userToFriend());
            boolean success = communityTab.sendFriendRequest(clientID, userToFriend);
            sendMessage(new BooleanResponse(success));
        }
        else {
            sendMessage(new ExceptionResponse("Invalid friend request: userToFriend cannot be null."));
        }
    }

    /**
     * Get friend request list
     *
     * @param m getFriendRequest Record Object
     */
    private void getFriendRequest(GetFriendRequests m){
        var result = new ArrayList<String>();
        for (int ID : profileDb.getFriendsRequests(clientID)) {
            result.add(profileDb.getPlayerUsername(ID));
        }
        sendMessage(new ObjectResponse(result));
    }

    /**
     * Accept friend request in game
     *
     * @param m AcceptFriendRequest Record Object
     */
    private void acceptFriendRequest(AcceptFriendRequest m){
        if (m.sender() != null) {
            int friendID = getPlayerIDFromUsername(m.sender());
            try{
                sendMessage(new BooleanResponse(communityTab.acceptFriendRequest(clientID, friendID)));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid friend request: sender cannot be null."));
        }
    }

    /**
     * Deny Friend Request
     *
     * @param m DenyFriendRequest Record Object
     */
    private void denyFriendRequest(DenyFriendRequest m){
        if (m.sender() != null) {
            int toDecline = getPlayerIDFromUsername(m.sender());
            try{
                sendMessage(new BooleanResponse(communityTab.declineFriendRequest(clientID, toDecline)));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else{
            sendMessage(new ExceptionResponse("Invalid friend request: sender cannot be null."));
        }
    }

    /**
     * To block a user
     *
     * @param m BlockUser Record Object
     */
    private void blockUser(BlockUser m){
        if (m.userToBlock() != null) {
            int toBlock = getPlayerIDFromUsername(m.userToBlock());
            try {
                sendMessage(new BooleanResponse(communityTab.blockUser(clientID, toBlock)));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else{
            sendMessage(new ExceptionResponse("Invalid block request: userToBlock cannot be null."));
        }
    }

    /**
     * To unblock a user
     *
     * @param m UnblockUser Record Object
     */
    private void unblockUser(UnblockUser m){
        if (m.userToUnblock() != null) {
            int toUnblock = getPlayerIDFromUsername(m.userToUnblock());
            try {
                sendMessage(new BooleanResponse(communityTab.unBlockUser(clientID, toUnblock)));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else{
            sendMessage(new ExceptionResponse("Invalid block request: userToUnblock cannot be null."));
        }
    }

    /**
     * Get a list of blocked users
     *
     * @param m GetBlockedUsers Record Object
     */
    private void getBlockedUsers(GetBlockedUsers m){
        try{
            sendMessage(new ObjectResponse(communityTab.viewBlockedList(clientID)));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Get the player username
     *
     * @param m GetUsername Record Object
     */
    private void getPlayerUsername(GetUsername m){
        try{
            sendMessage(new ObjectResponse(profileDb.getPlayerUsername(clientID)));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Get the bio of a player
     *
     * @param m GetBio Record Object
     */
    private void getBio(GetBio m){
        try{
            sendMessage(new ObjectResponse(profileDb.getBio(clientID)));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Get the avatar of the player
     *
     * @param m GetAvatar Record Object
     */
    private void getAvatar(GetAvatar m){
        try{
            sendMessage(new ObjectResponse(profileDb.getAvatar(clientID)));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Get the game statistics
     *
     * @param m GetGameStats Record Object
     */
    private void getGameStat(GetGameStats m){
        try{
            sendMessage(new GameStatistics(clientID, m.game(), matchDb, profileDb));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Leave the queue
     *
     * @param m LeaveQueue Record Object
     */
    private void leaveQueue(LeaveQueue m){
        try {
            sendMessage(new BooleanResponse(manager.leaveQueue(clientID, m.game())));
        }catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Join a match
     *
     * @param m JoinMatch Record Object
     */
    private void joinMatch(JoinMatch m){
        int hostID = getPlayerIDFromUsername(m.hostUsername());
        if (m.game() != null) {
            try{
                manager.joinMatch(m.game(), clientID, hostID);
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid join match request. Missing required fields."));
        }
    }

    /**
     * Register a challenge match
     *
     * @param m RegisterChallenge Record object
     */
    private void registerChallenge(RegisterChallenge m){
        String challengedUsername = m.challengedUsername();
        int challenged = getPlayerIDFromUsername(challengedUsername);
        if (challenged != -1 && m.game() != null) {
            try{
                manager.registerChallenge(m.game(), clientID, challenged);
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid register challenge request. Missing required fields."));
        }
    }

    /**
     * Unregister a challenge
     *
     * @param m UnregisterChallenge Record Object
     */
    private void unregisterChallenge(UnregisterChallenge m){
        if (m.challenge() != null) {
            try{
                manager.deregisterChallenge(m.challenge());
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid deregister challenge request. Missing required fields."));
        }
    }

    /**
     * Accept a challenge match
     *
     * @param m AcceptChallenge Record Object
     */
    private void acceptChallenge(AcceptChallenge m){
        if (m.challenge() != null) {
            try{
                manager.acceptChallenge(m.challenge());
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid accept challenge request. Missing required fields."));
        }
    }

    /**
     * Host a match
     *
     * @param m HostMatch Record Object
     */
    private void hostMatch(HostMatch m){
        if (m.game() != null) {
            try {
                manager.hostMatch(m.game(), clientID);
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid host match request. Missing required fields."));
        }
    }

    /**
     * Unhost a match
     *
     * @param m UnhostMatch Record Object
     */
    private void unhostMatch(UnhostMatch m){
        if (m.game() != null) {
            try {
                manager.unhostMatch(m.game(), clientID);
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid host match request. Missing required fields."));
        }
    }

    /**
     * Move a piece
     *
     * @param m MovePiece Record Object
     */
    private void movePiece(MovePiece m){
        try{
            match.doAction(new GameAction(m, clientID));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Place a piece in tic-tac-toe
     *
     * @param m PlacePiece Record
     */
    private void placePiece(PlacePiece m){
        try{
            match.doAction(new GameAction(m, clientID));
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Forfeit a match
     *
     * @param m ForfeitMatch Record Object
     */
    private void forfeitMatch(ForfeitMatch m){
        try{
            match.forfeitMatch(clientID);
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Send a chat message
     *
     * @param m ChatMessage Record Object
     */
    private void chatMessage(ChatMessage m){
        try{
            match.sendInGameMessage(profileDb.getPlayerUsername(clientID), m.message());
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Log out of the account
     *
     * @param m LogOut Record Object
     */
    private void logOut(LogOut m){
        try{
            manager.removeClientHandler(clientID);
            clientID = -1;
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     ** Authenticates the user based on username and password.
     * This method calls the authentication methods from UserAuthentication class.
     *
     * Sends client ID to client to confirm success.
     *
     * @param m LogIn Record Object
     */
    private void userLogin(LogIn m){
        if (m.username() != null && m.password() != null) {
            try{
                boolean result = UserAuthentication.login(m.username(), m.password());
                if (result) {
                    this.clientID = ProfileDatabaseManager.getPlayerIDFromUsername(m.username());
                    manager.registerLoggedInUser(clientID, this);
                    loggedIn = true;
                    sendMessage(new ObjectResponse(clientID));
                }
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid login request. Username or password cannot be null."));
        }
    }

    /**
     * Start the user registration process. Sends 2FA code to the provided email address.
     *
     * @param m Signup Record Object
     */
    public void userSignup(Signup m) {
        if (m.user()!=null || m.confirmPass()!=null || m.password()!=null || m.email()!=null) {
            try {
                sendMessage(new BooleanResponse(UserAuthentication.signup(m.email(), m.password(), m.user(), m.confirmPass())));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        }
        else {
            sendMessage(new ExceptionResponse("Invalid signup request. Username or password cannot be null."));
        }
    }

    /**
     * Registers a new user
     * Calls the registration method to register the new user. Does not log the user in.
     * @param m VerifySignUp Record Object
     */
    private void verifySignup(VerifySignup m) {
        if (m.email() != null && m.password() != null && m.user() != null && m.confirmPass() != null) {
            try{
                sendMessage(new BooleanResponse(UserAuthentication.verifySignup(m.code(),  m.user(), m.password(), m.email())));
            }
            catch(Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid registration request. All fields must be provided."));
        }
    }

    /**
     * Updated password of the user
     * @param m VerifyPasswordChange Record Object
     */
    private void verifyPasswordChange(VerifyPasswordChange m) {
        String newPassword = m.newPassword();
        String retypeNewPassword = m.retypePassword();
        if (newPassword != null && retypeNewPassword != null) {
            try {
                String currentPassword = profileDb.getPlayerPass(clientID);
                sendMessage(new BooleanResponse(profileSettings.changePassword(clientID, currentPassword, newPassword, retypeNewPassword)));
            } catch (Exception e) {
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid password update request. All fields are required."));
        }
    }

    /**
     * Delete the profile.
     * @param m DeleteProfile Record Object
     */
    // Not sure if we need username or password
    private void deleteProfile(DeleteProfile m) {
        int userID = getPlayerIDFromUsername(m.username());
        try {
            sendMessage(new BooleanResponse(profileSettings.deleteProfile(userID, m.currentPassword())));
            logOut(new LogOut());
        }
        catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Update the user profile bio
     * @param m UpdateBio Record Object
     */
    private void updateBio(UpdateBio m) {
        if (m.bio() != null) {
            try{
                sendMessage(new BooleanResponse(profileSettings.editBio(clientID, m.bio())));
            }
            catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid update request: bio must not be null."));
        }
    }

    /**
     * Updates the email of the user profile
     * @param m UpdateEmail Record Object
     */
    private void updateEmail(UpdateEmail m){
        String newEmail = m.newEmail();
        String verificationCode = m.verificationCodeProvided();
        if (newEmail != null && verificationCode != null) {
            try {
                sendMessage(new BooleanResponse(profileSettings.changeEmail(clientID, newEmail, verificationCode)));
            } catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid update email request. All fields (user ID, new email, and verification code) are required."));
        }

    }

    /**
     * Update the user password
     * @param m UpdatePassword Record Object
     */
    // TODO: Is it supposed to call forgotPassword?
    private void updatePassword(UpdatePassword m){
        try{
            sendMessage(new BooleanResponse(UserAuthentication.forgotPassword(clientID)));
        } catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Updates the user's username
     * @param m UpdateUsername Record Object
     */
    private void updateUsername(UpdateUsername m) {
        String oldUsername = m.oldUsername();
        String newUsername = m.newUsername();
        if (oldUsername != null && newUsername != null && !newUsername.trim().isEmpty()) {
            try{
                sendMessage(new BooleanResponse(profileSettings.editUsername(clientID,newUsername)));
            } catch (Exception e){
                sendMessage(new ExceptionResponse(e.getMessage()));
            }
        } else {
            sendMessage(new ExceptionResponse("Invalid username update request. User ID and new username are required."));
        }
    }

    /**
     * Updating the user's Avatar
     * @param m UpdateAvatar Record Object
     */
    private void updateAvatar(UpdateAvatar m) {
        int newAvatar = m.newAvatar();
        try{
            sendMessage(new BooleanResponse(profileSettings.editAvatar(clientID, newAvatar)));
        } catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Requesting leaderboard
     * @param m RequestLeaderBoard Record object
     */
    private void requestLeaderboard(RequestLeaderboard m){
        if (m.game() != null) {
            Leaderboard leaderboard = new Leaderboard(clientID, m.game(), matchDb, profileDb);
            sendMessage(new ObjectResponse(leaderboard));
        } else {
            sendMessage(new ExceptionResponse("Invalid request: Game is null."));
        }
    }

    /**
     * Finding the games
     * @param m FindGame Record Object
     */
    private void findGame(FindGame m){
        if (m.game() != null) {
            boolean success = manager.requestMatch(clientID, m.game());
            sendMessage(new BooleanResponse(success));
        } else {
            sendMessage(new ExceptionResponse("Invalid request: Player ID or game is null."));
        }
    }

    /**
     * Request the hosts
     *
     * @param m RequestHosts Record Object
     */
    private void requestHosts(RequestHosts m){
        try {
            sendMessage(new ObjectResponse(manager.getHosts(m.game())));
        }catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Request Online Active Players
     *
     * @param m RequestOnlinePlayers Record Object
     */
    private void requestOnlinePlayers(RequestOnlinePlayers m){
        try{
            sendMessage(new ObjectResponse(manager.getOnlineUsers(clientID)));
        }catch(Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Request challenges
     *
     * @param m RequestChallenges Record Object
     */
    private void requestChallenges(RequestChallenges m){
        try{
            sendMessage(new ObjectResponse(manager.getChallenges(clientID)));
        }catch (Exception e){
            sendMessage(new ExceptionResponse(e.getMessage()));
        }
    }

    /**
     * Sending requests and responses over the socket
     *
     * @param object request/ response objects
     */
    protected void sendMessage(Object object) {
        try {
            bufferedWriter.writeObject(object);
            bufferedWriter.flush();
            System.out.println(this + " Sent: " + object);
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    /**
     * Close the client socket
     *
     * @param socket client socket
     * @param bufferedReader Object Stream Reader
     * @param bufferedWriter Object Stream Writer
     */
    private void closeEverything(Socket socket, ObjectInputStream bufferedReader, ObjectOutputStream bufferedWriter) {
        manager.removeClientHandler(clientID);
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
