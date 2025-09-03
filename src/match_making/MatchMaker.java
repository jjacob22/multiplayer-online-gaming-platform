package match_making;

import game.GameServerController;
import game.chess.ChessServerController;
import game.connect_four.ConnectFourServerController;
import game.tic_tac_toe.TicTacToeServerController;
import leaderboard.GameStatistics;
import networking.AbstractDatabaseManager;
import networking.MatchDatabaseManager;
import networking.ProfileDatabaseManager;
import networking.QueueDatabaseManager;

import java.util.*;
import java.util.stream.Collectors;

import static networking.AbstractDatabaseManager.getAllPlayerIDs;

public class MatchMaker {
    private MatchDatabaseManager matchDB;
    private QueueDatabaseManager queueDB;
    private ProfileDatabaseManager profileDB;
    private Map<Game, ArrayList<Integer>> hosts = new HashMap<>();
    private ArrayList<MatchChallenge> challenges = new ArrayList<>();

    /**
     * Basic constructor for MatchMaker
     *
     * @param matchDB Database of match statistics.
     * @param queueDB Database representing the queue for matchmaking.
     * @param profileDB Database of player profiles.
     */
    public MatchMaker(MatchDatabaseManager matchDB, QueueDatabaseManager queueDB, ProfileDatabaseManager profileDB){
        this.matchDB = matchDB;
        this.queueDB = queueDB;
        this.profileDB = profileDB;
        for (var game : Game.values()) {
            hosts.put(game, new ArrayList<>());
        }
    }

    /**
     * Adds the requesting player to the queue and searches for a match. Returns immediately if
     * no match is found. Leaves it up to the client to wait and ask for a match again later.
     * Does not check if a player is already queued for another game.
     *
     * @param player1ID
     *      The ID of the player requesting a match.
     * @return
     *      The GameServerController with both player1 and an opponent. Null if no reasonable
     *      opponent could be found.
     */
    public GameServerController findMatch(int player1ID, Game game) {
        //Check if player1ID exist in database
        if(!AbstractDatabaseManager.playerExists(player1ID)){
            throw new InvalidPlayerIDException("Player ID " + player1ID + " does not exist in the database");
        }

        //Add player to database
        queueDB.addPlayerToQueue(player1ID, game);

        //Get match attempts
        int matchingAttempts = queueDB.getPlayerMatchmakingAttempts(player1ID, game);

        //Setting game statistics for player
        GameStatistics player1 = new GameStatistics(player1ID, game, matchDB, profileDB);

        //Getting all player in queue from database
        int[] playersIDList = queueDB.getAllPlayersInQueue(game);
        System.out.println("Match queue: " + Arrays.toString(playersIDList));

        //Check if the queue is 0 and returning null and adding 1 to match making attempts
        if (playersIDList.length == 1 && playersIDList[0] == player1ID) {
            System.out.println("Empty queue");
            queueDB.addPlayerMatchmakingAttempt(player1ID, game);
            return null;
        }

        System.out.println("Finding Match for " + player1ID);

        //Setting ELO difference dependents on match making attempts
        int ELODifference;
        if (matchingAttempts <= 5) {
            ELODifference = 50 * matchingAttempts;
            System.out.println("Elo difference of: " + ELODifference);
        }
        else if (matchingAttempts <= 10) {
            ELODifference = 100 * matchingAttempts;
            System.out.println("Elo difference of: " + ELODifference);
        }
        else {
            ELODifference = 1000 * matchingAttempts;
            System.out.println("Elo difference of: " + ELODifference);
        }

        int opponentID = getOpponent(player1, playersIDList, ELODifference, game);
        if (opponentID == -1) {
            queueDB.addPlayerMatchmakingAttempt(player1ID, game);
            return null;
        }

        queueDB.removePlayerFromQueue(player1ID, game);
        queueDB.removePlayerFromQueue(opponentID, game);
        queueDB.setPlayerMatchmakingAttempts(player1ID, game, 0);
        queueDB.setPlayerMatchmakingAttempts(opponentID, game, 0);

        return getGameController(new int[]{player1ID, opponentID}, game);
    }

    /**
     * Removes the specified player from the matchmaking queue.
     * @param playerID ID of player to remove
     * @param game Game for which the player is queued
     */
    public void leaveQueue(int playerID, Game game) {
        queueDB.removePlayerFromQueue(playerID, game);
        queueDB.setPlayerMatchmakingAttempts(playerID, game, 0);
    }

    /**
     * Get the first suitable opponent from the list of potential player
     *
     * @param player1
     *      The player class for the searching player
     * @param playersIDList
     *      The list of ID of player in the Queue
     * @param ELODifference
     *      The allowed ELO difference between the opponent and the searching player
     * @return The player who will serve as the opponent. Null if no suitable player found.
     */
    private int getOpponent(GameStatistics player1, int[] playersIDList, int ELODifference, Game game) {
        if (playersIDList.length == 1) {
            System.out.println("Empty queue");
            return -1;
        }
        else {
            int opponentID = playersIDList[0];
            boolean opponentFound = false;
            for (int i = 0; i < playersIDList.length && !opponentFound; i++) {
                if(playersIDList[i] != player1.getAccountID()){
                    int currentOpponentID = playersIDList[i];
                    opponentID = currentOpponentID;
                    opponentFound = Math.abs(matchDB.getPlayerELO(currentOpponentID, game) - player1.getELO()) <= ELODifference;
                }
            }

            if (opponentFound){
                System.out.println("Opponent found");
                return opponentID;
            }
            else {
                System.out.println("No suitable opponent found");
                return -1;
            }
        }
    }

    private GameServerController getGameController(int[] playerIDs, Game game) {
        GameServerController controller;
        switch (game) {
            case Chess -> controller = new ChessServerController(playerIDs);
            case ConnectFour -> controller = new ConnectFourServerController(playerIDs);
            case TicTacToe -> controller = new TicTacToeServerController(playerIDs);
            default -> {
                return null;
            }
        }
        return controller;
    }

    public void hostMatch(int playerID, Game game) {
        //Check if player1ID exist in database
        if(!AbstractDatabaseManager.playerExists(playerID)){
            throw new InvalidPlayerIDException("Player ID " + playerID + " does not exist in the database");
        }
        hosts.get(game).add(playerID);
    }

    public void unhostMatch(int playerID, Game game) {
        //Check if player1ID exist in database
        if(!AbstractDatabaseManager.playerExists(playerID)){
            throw new InvalidPlayerIDException("Player ID " + playerID + " does not exist in the database");
        }
        try{
            hosts.get(game).remove(playerID);
        }catch (RuntimeException e){
            throw new UnknownMatchException("Player ID " + playerID + " is not hosting a match");
        }

    }

    /**
     * Gets the list of unique usernames which represent the list of match hosts for the given game.
     * @param game The game in which the matches are hosted.
     * @return The list of usernames of hosts.
     */
    public ArrayList<String> getHosts(Game game) {
        var hostIDs = hosts.get(game);
        var out = new ArrayList<String>(hostIDs.size());
        for (var hostID : hostIDs) {
            out.add(profileDB.getPlayerUsername(hostID));
        }
        return out;
    }

    /**
     * Join a match that was already made. When we refer to "hosting" a match this does not mean
     * it is running on the client's device. Rather, it means the client initiated the match.
     * This method does not ask the host whether they want the other player to join.
     *
     * @param playerID
     *      The ID of the player that is joining the match
     * @param hostID
     *      The ID of the player hosting the match.
     * @return status
     *      return a boolean statement to networking
     */
    public GameServerController joinMatch(int playerID, int hostID, Game game){
        if (!hosts.get(game).contains(hostID)) {
            System.out.println("Join match failed: The host ID " + hostID + " could not be found in the list of lobbies.");
            throw new MatchNotFoundException("Failed to find match hosted by " + playerID, "");
            //TODO: Generate match IDs properly instead of just giving this an empty string

        }

        GameServerController controller = getGameController(new int[]{hostID, playerID}, game);
        if (controller == null) {
            return null; //TODO: Possibly also throw an exception here?
        }
        hosts.get(game).remove((Integer) hostID);
        return controller;
    }

    /**
     * Registers that one player wants to challenge another. Does not restrict how many
     * challenges a user can make or how many times a user can be challenged.
     * @param challengerID ID of the challenging player.
     * @param challengedID ID of the player being challenged.
     */
    public void registerChallenge(int challengerID, int challengedID, Game game) {
        var players = getAllPlayerIDs();
        boolean containsChallenger = Arrays.stream(players).anyMatch(x -> x == challengerID);
        boolean containsChallenged = Arrays.stream(players).anyMatch(x -> x == challengedID);
        if (!containsChallenger) {
            throw new IllegalArgumentException("ID "+ challengerID + " of challenger not in database.");
        }
        if (!containsChallenged) {
            throw new IllegalArgumentException("ID "+ challengerID + " of challenger not in database.");
        }

        // Block check
        int[] challengerBlocked = profileDB.getHasBlocked(challengerID);
        int[] challengedBlocked = profileDB.getHasBlocked(challengedID);

        boolean blockedEitherWay =
                Arrays.stream(challengerBlocked).anyMatch(id -> id == challengedID) ||
                        Arrays.stream(challengedBlocked).anyMatch(id -> id == challengerID);

        if (blockedEitherWay) {
            System.out.println("Challenge blocked: one of the players has blocked the other.");
            return; // or throw new IllegalStateException("Challenge not allowed due to blocking.");
        }

        challenges.add(new MatchChallenge(challengerID, challengedID, profileDB, game));
    }

    public void unregisterChallenge(MatchChallenge challenge) {
        challenges.remove(challenge);
    }

    public ArrayList<MatchChallenge> getChallengesFor(int challengedID) {
        return (ArrayList<MatchChallenge>) challenges.stream()
                .filter(c -> c.getChallengedID() == challengedID)
                .collect(Collectors.toList());
    }

    public ArrayList<MatchChallenge> getMatchChallengesFrom(int challengerID, Game game) {
        return (ArrayList<MatchChallenge>) challenges.stream()
                .filter(c -> c.getChallengerID() == challengerID)
                .collect(Collectors.toList());
    }

    /**
     * Creates the GameServerController to host the match between two players.
     * @param challenge The challenge being accepted.
     * @return A GameServerController that hosts the match for the two players.
     */
    public GameServerController acceptChallenge(MatchChallenge challenge) {

        for (MatchChallenge i : challenges) {
            System.out.println("Challenger: " + i.getChallenger() + " Challenged: " + i.getChallenged());
        }
        System.out.println("AAAAAAAA Challenger: " + challenge.getChallenger() + " Challenged: " + challenge.getChallenged());

        if (!challenges.contains(challenge))
            throw new MatchNotFoundException("Failed to find challenge from player ID "+ challenge.getChallengerID() + " to " + challenge.getChallengedID(), "");
        //TODO: Generate match IDs properly instead of just giving this an empty string

        GameServerController controller = getGameController(new int[]{challenge.getChallengerID(), challenge.getChallengedID()}, challenge.getGame());
        if (controller == null) {
            return null;
        }

        challenges.remove(challenge);
        return controller;
    }
}
