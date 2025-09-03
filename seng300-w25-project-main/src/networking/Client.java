package networking;
import leaderboard.Leaderboard;
import match_making.Game;
import match_making.MatchChallenge;
import networking.requestMessages.*;
import networking.responseMessages.*;
import networking.server_events.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static networking.PlatformServerManager.*;

public class Client {
    private Socket socket;
    private ObjectInputStream streamReader;
    private ObjectOutputStream streamWriter;
    private final BlockingQueue<Object> responseQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<BooleanResponse> boolResponseQueue = new LinkedBlockingQueue<>();
    private final ArrayList<ServerEventListener> serverEventListeners = new ArrayList<>();
    private final ArrayList<GameEventListener> gameEventListeners = new ArrayList<>();
    private int myID;

    /**
     * Constructor a client socket
     *
     * @param socket client socket
     */
    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.streamWriter = new ObjectOutputStream(socket.getOutputStream());
            this.streamReader = new ObjectInputStream(socket.getInputStream());
            listenForMessages();
        }
        catch (IOException e) {
            closeEverything(socket, streamReader, streamWriter);
            throw new RuntimeException(e);
        }
    }

    /**
     * Construct a client network communicator with custom network configurations.
     * @param serverIP the IP the server is hosted on
     * @param serverPort the port the server is hosted on
     */
    public Client(String serverIP, int serverPort) {
        try {
            this.socket = new Socket(SERVER_IP_LOCAL, SERVER_PORT_DEFAULT);
            this.streamWriter = new ObjectOutputStream(socket.getOutputStream());
            this.streamReader = new ObjectInputStream(socket.getInputStream());
            listenForMessages();
        } catch (IOException e) {
            closeEverything(socket, streamReader, streamWriter);
            throw new RuntimeException(e);
        }
    }

    /**
     * Construct a client network communicator using the default port and custom IP.
     * @param serverIP the IP the server is hosted on
     */
    public Client(String serverIP) {
        this(serverIP, SERVER_PORT_DEFAULT);
    }

    /**
     * Construct a client network communicator with default network configurations.
     */
    public Client() {
        this(SERVER_IP_LOCAL, SERVER_PORT_DEFAULT);
    }

    /**
     * Add a server event listener to the server event listener list.
     * @param listener an instance of ServerEventListener to add to the list
     */
    public void addServerEventListener(ServerEventListener listener) {
        this.serverEventListeners.add(listener);
    }

    /**
     * Remove a server event listener from the server event listener list.
     * @param listener an instance of ServerEventListener to remove from the list
     */
    public void removeServerEventListener(ServerEventListener listener) {
        this.serverEventListeners.remove(listener);
    }

    /**
     * Clear all server event listeners from the server event listener list.
     */
    public void clearServerEventListener() {
        this.serverEventListeners.clear();
    }

    /**
     * Add a game event listener to the game event listener list.
     * @param listener an instance of GameEventListener to add to the list
     */
    public void addGameEventListener(GameEventListener listener) {
        this.gameEventListeners.add(listener);
    }

    /**
     * Remove a game event listener from the game event listener list.
     * @param listener an instance of GameEventListener to remove from the list
     */
    public void removeGameEventListener(GameEventListener listener) {
        this.gameEventListeners.remove(listener);
    }

    /**
     * Clear all game event listeners from the game event listener list.
     */
    public void clearGameEventListener() {
        this.gameEventListeners.clear();
    }

    /**
     * Send object message over the socket to the server side
     *
     * @param message object to be sent over the socket
     */
    private void sendMessage(Object message) {
        try {
            if (socket.isConnected()) {
                streamWriter.writeObject(message);
                streamWriter.flush();
                System.out.println("SENT MESSAGE:\nFROM " + myID + " / " + this + " TO " + socket + ":\n" + message + "\n");
            }
        }
        catch (IOException e){
            closeEverything(socket, streamReader, streamWriter);
        }
    }

    /**
     * Listen for the responses from the sever through the socket
     */
    private void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object messageFromServer;

                while (socket.isConnected()) {
                    try{
                        try {
                            messageFromServer = streamReader.readObject();
                            System.out.println("\nRECEIVED MESSAGE:\nTO " + myID + " / " + this + " FROM " + socket + ":\n" + messageFromServer + "\n");
                            analyzeMessage(messageFromServer);
                        } catch (ClassNotFoundException e) {
                            break;
                        }
                    }
                    catch (IOException e) {
                        closeEverything(socket, streamReader, streamWriter);
                    }
                }
            }
        }).start();
    }

    /**
     * Close the client socket
     *
     * @param socket client socket
     * @param bufferedReader Object input reader
     * @param bufferedWriter Object output writer
     */
    private void closeEverything(Socket socket, ObjectInputStream bufferedReader, ObjectOutputStream bufferedWriter) {
        try {
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(socket != null){
                socket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analyze message according to the response type, followed by appropriate actions
     *
     * @param message Object received from the server as a response
     */
    private void analyzeMessage(Object message) {
        EventObject event;
        switch (message){
            case MatchFoundResponse m:
                event = new MatchFoundEvent(this, m.GameState(), m.game());
                for (var listener : serverEventListeners) {
                    listener.matchFound((MatchFoundEvent) event);
                }
                break;
            case ChatMessageResponse m:
                event = new ChatMessageEvent(this, m.from(), m.message());
                for (var listener : gameEventListeners) {
                    listener.chatMessageReceived((ChatMessageEvent) event);
                }
                break;
            case GameStateResponse m:
                event = new GameStateEvent(this, m.gameState());
                for (var listener : gameEventListeners) {
                    listener.gameStateChanged((GameStateEvent) event);
                }
                break;
            case ExceptionResponse m:
                event = new ServerExceptionEvent(this, m.errorMessage());
                for (var listener : gameEventListeners) {
                    listener.exceptionOccurred((ServerExceptionEvent) event);
                }
                break;
            case BooleanResponse m:
                boolResponseQueue.add(m);
                break;
            default:
                responseQueue.add(message);
                break;
        }
    }

    /**
     * Pull the expected response off the queue.
     * @return The server response to the previous query.
     */
    private Response getResponse() {
        Response response;
        try {
            response = (Response) responseQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * Get the boolean response from the server
     *
     * @return BooleanResponse
     */
    private BooleanResponse getBooleanResponse() {
        BooleanResponse response;
        try {
            response = boolResponseQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    // THE FOLLOWING FUNCTIONS ARE TO BE CALLED BY GUI

    /**
     * To verify sign up
     *
     * @param code Authentication Code
     * @param email Email
     * @param password Password
     * @param user User
     * @param confirmPass Confirmed Password
     * @return Boolean value
     */
    public boolean verifySignup(String code, String email, String password, String user, String confirmPass) {
        sendMessage(new VerifySignup(code, email, password, user, confirmPass));
        return getBooleanResponse().bool();
    }

    /**
     * Verify Password Change
     *
     * @param code Authentication Code
     * @param username Username
     * @param newPassword New Password
     * @param retypePassword Retyped Password
     * @return Boolean value
     */
    public boolean verifyPasswordChange(String code, String username,String newPassword, String retypePassword) {
        sendMessage(new VerifyPasswordChange(code, newPassword, retypePassword, retypePassword));
        return getBooleanResponse().bool();
    }

    /**
     * User requests to log into the game system
     *
     * @param username Username
     * @param password Password
     * @return boolean value whether the request is successful or not
     */
    public boolean userLogin(String username, String password){
        sendMessage(new LogIn(username, password));
        Integer clientID = (Integer) ((ObjectResponse) getResponse()).data();
        if (clientID != -1) {
            myID = clientID;
        }
        return true;
    }

    /**
     * User requests to log out of the game
     */
    public void LogOut() {
        LogOut logOut = new LogOut();
        sendMessage(logOut);
        myID = -1;
    }

    /**
     * User wants to create a new account in the database
     *
     * @param email Email
     * @return boolean value whether the request is successful or not
     */
    public boolean userRegister(String email, String password, String user, String confirmPass){
        sendMessage(new Signup(email, password, user, confirmPass));
        return getBooleanResponse().bool();
    }

    /**
     * Complete the signup process with the 2FA code the user entered.
     * @param code The 2FA code for completing signup
     * @param email Email
     * @param password Password
     * @param user Username
     * @param confirmPass Confirmed Password
     * @return boolean value whether the request is successful or not
     */
    public boolean completeSignup(String code, String email, String password, String user, String confirmPass) {
        sendMessage(new VerifySignup(code, email, password, user, confirmPass));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to delete the current account from the whole system
     *
     * @param username The username of the account to delete
     * @param password The password of the account to delete
     * @return boolean value whether the request is successful or not
     */
    public boolean deleteProfile(String username, String password){
        sendMessage(new DeleteProfile(username, password));
        return getBooleanResponse().bool();
    }

    /**
     * Send a friend request to the user specified by the given username.
     * @param userToFriend The username of the user to send a friend request to.
     * @return Success boolean
     */
    public boolean sendFriendRequest(String userToFriend) {
        sendMessage(new FriendRequest(userToFriend));
        return getBooleanResponse().bool();
    }

    /**
     * Accept the given friend request.
     * @param sender The friend who sent the request.
     * @return Success boolean.
     */
    public boolean acceptFriendRequest(String sender) {
        sendMessage(new AcceptFriendRequest(sender));
        return getBooleanResponse().bool();
    }

    /**
     * Get friend requests for the current player.
     * @return The list of friend request notifications.
     */
    public ArrayList<String> getFriendRequests() {
        sendMessage(new GetFriendRequests());
        return (ArrayList<String>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Get the id of the current player
     *
     * @return ID Integer
     */
    public int getMyID() {
        return myID;
    }

    /**
     * Get the username of the current player.
     * @return the username as a String.
     */
    public String getUsername() {
        sendMessage(new GetUsername());
        return (String) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Get the biography of the current player.
     * @return The biography in text form.
     */
    public String getBio() {
        sendMessage(new GetBio());
        return (String) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Get the avatar of the current player.
     * @return The name of the avatar in the standard set of avatars.
     */
    public int getAvatar() {
        sendMessage(new GetAvatar());
        return (int) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Get the list of friends of the current user.
     * @return A map of pairs of username and avatar integer ID.
     */
    public HashMap<String,Integer> getFriends() {
        sendMessage(new GetFriends());
        return (HashMap<String, Integer>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Block the given user.
     * @param toBlock The username of the user to block.
     * @return If the request was successful.
     */
    public boolean blockUser(String toBlock) {
        sendMessage(new BlockUser(toBlock));
        return getBooleanResponse().bool();
    }

    /**
     * Unblock the given user.
     * @param toUnblock The username of the user to unblock.
     * @return If the unblock was successful.
     */
    public boolean unblockUser(String toUnblock) {
        sendMessage(new UnblockUser(toUnblock));
        return getBooleanResponse().bool();
    }

    /**
     * Get the list of blocked users for the current user.
     * @return A list of usernames of blocked users.
     */
    public ArrayList<String> getBlockedUsers() {
        sendMessage(new GetBlockedUsers());
        return (ArrayList<String>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * User requests to update the bio of the profile
     *
     * @param newBio New bio to be updated
     * @return boolean value whether the request is successful or not
     */
    public boolean updateBio(String newBio) {
        sendMessage(new UpdateBio(newBio));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to update its username
     *
     * @param oldUsername The user's old username
     * @param newUsername The user's new username
     * @return boolean value whether the request is successful or not
     */
    public boolean updateUsername(String oldUsername, String newUsername) {
        sendMessage(new UpdateUsername(oldUsername, newUsername));
        return getBooleanResponse().bool();
    }

    /**
     * Start the password resetting password.
     * @return Success boolean
     */
    public boolean UpdatePassword(String currentPassword, String newPassword, String retypeNewPassword) {
        sendMessage(new UpdatePassword(currentPassword, newPassword, retypeNewPassword));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to update the password
     *
     * @param code The 2FA code to complete the password change
     * @param username Current username
     * @param currentPassword CurrentPassword
     * @param newPassword New Password
     * @param retypeNewPassword Confirmed New Password
     * @return boolean value whether the request is successful or not
     */
    public boolean CompleteUpdatePassword(String code, String username,String currentPassword, String newPassword, String retypeNewPassword){
        sendMessage(new VerifyPasswordChange(code, username,newPassword, retypeNewPassword));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to update their email
     *
     * @param newEmail New Email
     * @param verificationCodeProvided 2-Factor Authentication
     * @return boolean value whether the request is successful or not
     */
    public boolean updateEmail(String newEmail, String verificationCodeProvided){
        sendMessage(new UpdateEmail(newEmail, verificationCodeProvided));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to update its avatar
     *
     * @param newAvatar New Avatar
     * @return boolean value whether the request is successful or not
     */
    public boolean updateAvatar(int newAvatar){
        sendMessage(new UpdateAvatar(newAvatar));
        return getBooleanResponse().bool();
    }

    /**
     * User wants to find a game to play in the system
     *
     * @param game Game Name
     * @return boolean value whether the request is successful or not
     */
    public boolean findGame(Game game){
        sendMessage(new FindGame(game));
        return getBooleanResponse().bool();
    }

    /**
     * Leave the Queue in the game
     *
     * @param game Game
     * @return Boolean value
     */
    public boolean leaveQueue(Game game) {
        sendMessage(new LeaveQueue(game));
        return getBooleanResponse().bool();
    }

    /**
     * Host a match of the given game, hosted by the player associated with this object.
     *
     * @param game Game of which type the match is to be.
     */
    public boolean hostMatch(Game game){
        sendMessage(new HostMatch(game));
        return getBooleanResponse().bool();
    }

    /**
     * Unhost the match of the given game, hosted by the player associated with this object.
     *
     * @param game Game of which type the match is.
     */
    public boolean unhostMatch(Game game){
        sendMessage(new UnhostMatch(game));
        return getBooleanResponse().bool();
    }

    /**
     * Get the list of all hosts for a certain game.
     * @param game The game for which to get the hosts.
     * @return The list of usernames of hosts.
     */
    public ArrayList<String> requestHosts(Game game) {
        sendMessage(new RequestHosts(game));
        return (ArrayList<String>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Join the match, of the given game, hosted by the given player.
     *
     * @param hostUsername The username of the host of the match.
     * @param game Game of which type the match is.
     */
    public boolean joinMatch(String hostUsername, Game game){
        sendMessage(new JoinMatch(hostUsername, game));
        return getBooleanResponse().bool();
    }

    /**
     * @return The list of all online players other than the current logged-in user.
     */
    public ArrayList<String> getOnlinePLayers() {
        sendMessage(new RequestOnlinePlayers());
        return (ArrayList<String>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Request a leaderboard from the server.
     *
     * @param game Game with which the leaderboard is associated
     * @return a Leaderboard object
     */
    public Leaderboard requestLeaderboard(Game game) {
        sendMessage(new RequestLeaderboard(game));
        return (Leaderboard) ((ObjectResponse) getResponse()).data();
    }

    /**
     * User sends a chat message to other players in the same game
     *
     * @param message Chat Message Content
     */
    public void chatMessage(String message){
        sendMessage(new ChatMessage(message));
    }

    /**
     * User wants to deregister from a challenge
     *
     * @param game The game in which to register the challenge
     * @param challengedUsername The username of the player to challenge
     */
    public void registerChallenge(Game game, String challengedUsername) {
        sendMessage(new RegisterChallenge(challengedUsername, game));
    }

    /**
     * User unregisters challenge
     *
     * @param challenge The challenge to unregister.
     * @return boolean value whether the request is successful or not
     */
    public boolean unregisterChallenge(MatchChallenge challenge) {
        sendMessage(new UnregisterChallenge(challenge));
        return getBooleanResponse().bool();
    }

    /**
     * @return Get the list of challenges for the currently logged-in player
     */
    public ArrayList<MatchChallenge> getChallenges() {
        sendMessage(new RequestChallenges());
        return (ArrayList<MatchChallenge>) ((ObjectResponse) getResponse()).data();
    }

    /**
     * Accept the selected challenge.
     *
     * @param challenge The challenge being accepted
     */
    public void acceptChallenge(MatchChallenge challenge) {
        sendMessage(new AcceptChallenge(challenge));
    }

    /**
     * Forfeit the current match the user is in.
     */
    public void forfeitMatch(){
        sendMessage(new ForfeitMatch());
    }

    /**
     * Player moves a piece in the game
     *
     * @param fromRow Initial Row
     * @param fromCol Initial Column
     * @param toRow Destination Column
     * @param toCol Destination Row
     */
    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        sendMessage(new MovePiece(fromRow, fromCol, toRow, toCol));
    }

    /**
     * User wants to place a piece in the game (Tic-tac-toe)
     *
     * @param row Row
     * @param col Column
     */
    public void placePiece(int row, int col) {
        sendMessage(new PlacePiece(row, col));
    }

    public void promotePawn(PromotePawn.Promotion promotion) {
        sendMessage(new PromotePawn(promotion));
    }

    /**
     * Running main on the client directly will run a command line version of the client.
     * <p>
     *     This is primarily for testing.
     * </p>
     */
    public static void main(String[] args) throws IOException {
        /*
        The server is connected to upon client object creation.
         */
        Client client = new Client();

        Scanner scanner = new Scanner(System.in);

        boolean invalidInput = true;
        boolean login = true;
        while (invalidInput) {
            System.out.print("Type...\n1. To login\n2. To register\n: ");
            switch (scanner.nextLine()) {
                case "1" -> {
                    login = true; invalidInput = false;
                }
                case "2" -> {
                    login = false; invalidInput = false;
                }
            }
        }

        System.out.print("Enter email: ");
        var email = scanner.nextLine();

        System.out.print("Enter name: ");
        var name = scanner.nextLine();

        System.out.print("Enter password: ");
        var password = scanner.nextLine();

        if (login) {
            client.userLogin(name, password);
        }
        else {
//            client.userRegister(email, password, name, password);
        }

        invalidInput = true;
        while (invalidInput) {
            System.out.print("Type...\n1. To host\n2. To join\n: ");
            switch (scanner.nextLine()) {
                case "1" -> {
                    invalidInput = false;
                    if (client.hostMatch(Game.Chess))
                        System.out.println("Hosted");
                }
                case "2" -> {
                    invalidInput = false;
                    var hosts = client.requestHosts(Game.Chess);
                    if (client.joinMatch(hosts.getFirst(), Game.Chess))
                        System.out.println("Joined");
                }
            }
        }

        invalidInput = true;
        while (invalidInput) {
            System.out.print("Are you sure? Answer (y or n)\n: ");
            switch (scanner.nextLine()) {
                case "y" -> {
                    invalidInput = false;
                }
                case "n" -> {
                    invalidInput = false;
                    if (client.unhostMatch(Game.Chess))
                        System.out.println("Unhosted");
                }
            }
        }
        System.out.println("----- Chat ------");
        client.addGameEventListener(
                new GameEventListener() {
                    @Override
                    public void chatMessageReceived(ChatMessageEvent e) {
                        System.out.printf("%s: %s\n", e.getSender(), e.getMessage());
                    }

                    @Override
                    public void gameStateChanged(GameStateEvent e) {

                    }

                    @Override
                    public void exceptionOccurred(ServerExceptionEvent e) {
                        System.out.println(e.getExceptionMessage());
                    }
                }
        );
        while (true) {
            var msg = scanner.nextLine();
            client.sendMessage(new ChatMessage(msg));
        }
    }
}