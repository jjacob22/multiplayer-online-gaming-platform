package networking;

import game.GameServerController;
import leaderboard.ELOCalculator;
import leaderboard.GameStatistics;
import leaderboard.MatchInfo;
import match_making.Game;
import match_making.MatchChallenge;
import match_making.MatchMaker;
import match_making.MatchNotFoundException;
import networking.requestMessages.GameState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static networking.AbstractDatabaseManager.*;

/**
 * PlatformServerManager manages client connections, match requests, hosting, joining,
 * and challenge operations for the game server.
 */
public class PlatformServerManager extends AbstractNetworkManager {
    /**
     * Port which the server is hosted on.
     */
    public static final int SERVER_PORT_DEFAULT = 49985;
    /**
     * IP to use if the server is being hosted on your own machine.
     * <br>
     * This can also be "localhost", it means the same thing as "127.0.0.1".
     */
    public static final String SERVER_IP_LOCAL = "127.0.0.1";

    /** Map of logged in client handlers keyed by player ID. */
    private final Map<Integer, ClientHandler> loggedInClients = new HashMap<>();
    /** The server socket used to accept client connections. */
    private final ServerSocket serverSocket;
    /** Manager for match database operations. */
    private final MatchDatabaseManager matchDb = new MatchDatabaseManager();
    /** Manager for profile database operations. */
    private final ProfileDatabaseManager profileDb = new ProfileDatabaseManager();
    /** Manager for queue database operations. */
    private final QueueDatabaseManager queueDb = new QueueDatabaseManager();
    /** The MatchMaker instance for handling match and challenge logic. */
    private final MatchMaker matchMaker = new MatchMaker(matchDb, queueDb, profileDb);
    private final ELOCalculator eloCalculator = new ELOCalculator();

    /**
     * Construct a server manager running on a particular network port.
     * @param serverPort network port to host the server on
     * @throws IOException if an I/O error occurs when opening the server socket.
     */
    public PlatformServerManager(int serverPort) throws IOException {  // Constructor
        super();
        this.serverSocket = new ServerSocket(SERVER_PORT_DEFAULT);
    }

    /**
     * Construct a server manager with default network configurations.
     * @throws IOException if an I/O error occurs when opening the server socket.
     */
    public PlatformServerManager() throws IOException {
        this(SERVER_PORT_DEFAULT);
    }

    /**
     * Main method to start the game server.
     * @param args command line arguments.
     * @throws IOException if the server socket cannot be created.
     */
    public static void main(String[] args) throws IOException {
        PlatformServerManager server;
        if (args.length == 0) {
            server = new PlatformServerManager(SERVER_PORT_DEFAULT);
        } else if (args.length == 1) {
            server = new PlatformServerManager(Integer.parseInt(args[0]));
        } else {
            throw new IllegalArgumentException(
                "Invalid arguments:\n" + Arrays.toString(args) + "\n" +
                    "usage: <this executable> [options]\n" +
                    "options:\n\t[no arguments] (server will be hosted on port " + SERVER_PORT_DEFAULT + " by default)\n" +
                    "\t<port>\n");
        }
        server.startServer();
        System.out.println("Server started");
    }

    /**
     * Starts the game server, accepting client connections in a loop.
     */
    public void startServer() {
        try {
            while(!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client has connected");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this, matchDb, profileDb, queueDb);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch(IOException e){
            stopServer();
        }
    }

    /**
     * Stops the game server by closing the server socket.
     */
    public void stopServer() {
        try{
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Registers a client as logged in.
     * @param playerID the player's ID.
     */
    protected void registerLoggedInUser(int playerID, ClientHandler clientHandler) {
        loggedInClients.put(playerID, clientHandler);
        if (loggedInClients.containsKey(playerID)) {
            System.out.println("Client " + playerID + " logged in");
            System.out.println("Clients: " + loggedInClients.values());
        }
    }

    /**
     * @return Get the list of logged-in user's usernames.
     */
    protected ArrayList<String> getOnlineUsers(int userID) {
        var result = new ArrayList<String>();
        for (int otherUserID : loggedInClients.keySet()) {
            if (userID == otherUserID) continue;
            result.add(profileDb.getPlayerUsername(otherUserID));
        }
        return result;
    }

    /**
     * Removes a client handler from the logged in clients list.
     * @param clientID the client's ID.
     */
    protected void removeClientHandler(int clientID) {
        loggedInClients.remove(clientID);
    }

    private void recordElo(GameState status, MatchInfo matchInfo) {
        var player1Stats = new GameStatistics(matchInfo.getPlayer1ID(), matchInfo.getGame(), matchDb, profileDb);
        var player2Stats = new GameStatistics(matchInfo.getPlayer2ID(), matchInfo.getGame(), matchDb, profileDb);
        eloCalculator.updateScores(player1Stats, player2Stats, matchInfo);
        int player1ID = matchInfo.getPlayer1ID();
        int player2ID = matchInfo.getPlayer2ID();
        // Set player stats
        matchDb.setPlayerELO(matchInfo.getPlayer1ID(), matchInfo.getGame(), player1Stats.getELO());
        matchDb.setPlayerELO(matchInfo.getPlayer2ID(), matchInfo.getGame(), player2Stats.getELO());
        matchDb.setGamesPlayed(player1ID, matchDb.getGamesPlayed(player1ID, matchInfo.getGame()) + 1, matchInfo.getGame());
        matchDb.setGamesPlayed(player2ID, matchDb.getGamesPlayed(player2ID, matchInfo.getGame()) + 1, matchInfo.getGame());
        matchDb.setGameWins(matchInfo.getWinnerID(), matchDb.getGameWins(matchInfo.getWinnerID(), matchInfo.getGame()) + 1, matchInfo.getGame());
        matchDb.setTimePlayed(player1ID, matchDb.getTimePlayed(player1ID, matchInfo.getGame()) + (int)matchInfo.getTimePlayed(), matchInfo.getGame());
        matchDb.setTimePlayed(player2ID, matchDb.getTimePlayed(player2ID, matchInfo.getGame()) + (int)matchInfo.getTimePlayed(), matchInfo.getGame());
    }

    /**
     * Requests a match for a player in a specific game.
     * @param playerID the player's ID.
     * @param game the game for which the match is requested.
     * @return true if a match was found and started, false otherwise.
     */
    protected boolean requestMatch(int playerID, Game game) {
        var controller = matchMaker.findMatch(playerID, game);
        if (controller == null) {
            return false;
        }
        var clients = new ArrayList<ClientHandler>();
        for (var ID : controller.getPlayerIDs()) {
            var client = loggedInClients.get(ID);
            clients.add(client);
        }
        new Match(controller, clients, this::recordElo, game);
        return true;
    }

    protected boolean leaveQueue(int playerID, Game game) {
        matchMaker.leaveQueue(playerID, game);
        return true;
    }

    /**
     * Hosts a match for a player in a specific game.
     * @param game the game to be hosted.
     * @param playerID the host player's ID.
     */
    protected void hostMatch(Game game, int playerID) {
        matchMaker.hostMatch(playerID, game);
    }

    /**
     * Unhosts a match for a player in a specific game.
     * @param game the game for which the match is to be unhosted.
     * @param playerID the player's ID.
     */
    protected void unhostMatch(Game game, int playerID) {
        matchMaker.unhostMatch(playerID, game);
    }

    /**
     * Retrieves a list of hosts for a specific game.
     * @param game the game for which to retrieve hosts.
     * @return an ArrayList of host identifiers.
     */
    protected ArrayList<String> getHosts(Game game) {
        return matchMaker.getHosts(game);
    }

    /**
     * Joins an existing match for a player.
     * @param game the game in which to join the match.
     * @param playerID the ID of the joining player.
     * @param hostID the host player's ID.
     */
    protected void joinMatch(Game game, int playerID, int hostID) {
        var controller = matchMaker.joinMatch(playerID, hostID, game);
        if (controller == null) {
            throw new MatchNotFoundException("Join match failed for player " + playerID + " with host " + hostID, "");
            //TODO: Generate match IDs properly instead of just giving this an empty string
        }
        ArrayList<ClientHandler> players = new ArrayList<>();
        ClientHandler hostHandler = loggedInClients.get(hostID);
        ClientHandler joiningHandler = loggedInClients.get(playerID);
        if (hostHandler != null) {
            players.add(hostHandler);
        }
        if (joiningHandler != null) {
            players.add(joiningHandler);
        }
        new Match(controller, players, this::recordElo, game);
    }

    /**
     * Registers a challenge from one player to another for a specific game.
     * @param game the game in which the challenge is issued.
     * @param challengerID the ID of the challenging player.
     * @param challengedID the ID of the player being challenged.
     */
    // WE MIGHT DELETE THE GAME PARAMETER
    protected void registerChallenge(Game game, int challengerID, int challengedID) {
        try {
            matchMaker.registerChallenge(challengerID, challengedID, game);
        } catch (IllegalArgumentException e) {
            ClientHandler handler = loggedInClients.get(challengerID);
            if (handler != null) {
                handler.sendMessage("Challenge Registration failed: " + e.getMessage());
            }
        }
    }

    /**
     * Deregisters a challenge.
     * @param m the MatchChallenge object representing the challenge.
     */
    protected void deregisterChallenge(MatchChallenge m) {
        matchMaker.unregisterChallenge(m);
    }

    /**
     * Accepts a challenge and starts a match between the challenger and the challenged.
     * @param challenge the MatchChallenge to be accepted.
     * @return true if the challenge was accepted and the match started successfully, false otherwise.
     * @throws MatchNotFoundException if the match cannot be created.
     * @throws IllegalStateException if one or both players are not online.
     */
    protected boolean acceptChallenge(MatchChallenge challenge) {
        GameServerController controller = matchMaker.acceptChallenge(challenge);
        if (controller == null) {
            throw new MatchNotFoundException("MatchMaker failed to create a game session for challenge from player "
                    + challenge.getChallenger() + " to " + challenge.getChallenged(),
                    challenge.getChallenger());
            // TODO: Generate match IDs properly
        }
        int challengerID = getPlayerIDFromUsername(challenge.getChallenger());
        int challengedID = getPlayerIDFromUsername(challenge.getChallenged());
        ClientHandler challengerHandler = loggedInClients.get(challengerID);
        ClientHandler challengedHandler = loggedInClients.get(challengedID);
        if (challengerHandler == null || challengedHandler == null) {
            throw new IllegalStateException("One or both players are not online for challenge from player "
                    + challenge.getChallenger() + " to " + challenge.getChallenged());
        }
        ArrayList<ClientHandler> players = new ArrayList<>();
        players.add(challengerHandler);
        players.add(challengedHandler);
        new Match(controller, players, this::recordElo, challenge.getGame());
        return true;
    }

    /**
     * Get the list of challenges for the given player
     * @param clientID The ID of the player for whom to get challenges
     * @return The list of challenges for the given player.
     */
    protected ArrayList<MatchChallenge> getChallenges(int clientID) {
        return matchMaker.getChallengesFor(clientID);
    }
}
