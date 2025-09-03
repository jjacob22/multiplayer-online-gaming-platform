package networking;

import match_making.Game;

import java.io.*;
/**
 * This is a class for all getters and setters Matchmaking and leaderboard
 */
public class MatchDatabaseManager extends AbstractDatabaseManager {

    public MatchDatabaseManager() {}

    /**
     * getter for player's ELO
     * @param playerID: the unique ID that distinguishes the player
     * @return: the ELO or -1 if unsuccessful
     */
    public int getPlayerELO(int playerID, Game game) {
        String filename = "src/networking/database/player_game_stats.csv";
        int index = 1;
        switch(game){
            case Game.Chess: index = 1; break;
            case Game.ConnectFour: index = 5; break;
            case Game.TicTacToe: index = 9; break;
        }
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS);
        if (values.length > 0){
            return Integer.parseInt(values[index]);
        }
        return -1;
    }

    /**
     * setter for player's ELO
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that has been played
     * @param elo: the elo retrieved from that game
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerELO(int playerID, Game game, int elo){
        String filename = "src/networking/database/player_game_stats.csv";
        int index = 1;
        switch(game){
            case Game.Chess: index = 1; break;
            case Game.ConnectFour: index = 5; break;
            case Game.TicTacToe: index = 9; break;
        }
        return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS,String.valueOf(elo),index);
    }

    /**
     * getter for the wins of a player
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that has been played
     * @return: the wins or -1 if unsuccessful process
     */
    public int getGameWins(int playerID, Game game) {
        String filename = "src/networking/database/player_game_stats.csv";
        int index = 2;
        switch(game){
            case Game.Chess: index = 2; break;
            case Game.ConnectFour: index = 6; break;
            case Game.TicTacToe: index = 10; break;
        }
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS);
        if (values.length > 0){
            return Integer.parseInt(values[index]);
        }
        return -1;
    }

    /**
     * setter for the wins
     * @param playerID: the unique ID that distinguishes the player
     * @param gameWins: number of wins
     * @param game: the game that has been played
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setGameWins(int playerID, int gameWins, Game game){
        int index_of_value = 2;
        switch (game){
            case Game.Chess: index_of_value = 2; break;
            case Game.ConnectFour: index_of_value = 6; break;
            case Game.TicTacToe: index_of_value = 10; break;
        }
        String filename = "src/networking/database/player_game_stats.csv";
        return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS,String.valueOf(gameWins),index_of_value);
    }

    /**
     * the method for adding a win to the player's wins
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that the player has won
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addGameWin(int playerID, Game game){
        int game_wins = getGameWins(playerID, game);
        if (game_wins >= 0){
            game_wins += 1;
            return setGameWins(playerID, game_wins, game);
        }
        return -1;
    }

    /**
     * getter for the number of games the player has played
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that has been played
     * @return: the number of played games or -1 if unsuccessful process
     */
    public int getGamesPlayed(int playerID, Game game) {
        String filename = "src/networking/database/player_game_stats.csv";
        int index = 3;
        switch(game){
            case Game.Chess: index = 3; break;
            case Game.ConnectFour: index = 7; break;
            case Game.TicTacToe: index = 11; break;
        }
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS);
        if (values.length > 0){
            return Integer.parseInt(values[index]);
        }
        return -1;
    }
    /**
     * setter for the played games
     * @param playerID: the unique ID that distinguishes the player
     * @param gamesPlayed: number of played games
     * @param game: the game that has been played
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setGamesPlayed(int playerID, int gamesPlayed, Game game){
        int index_of_value = 3;
        switch (game){
            case Game.Chess: index_of_value = 3; break;
            case Game.ConnectFour: index_of_value = 7; break;
            case Game.TicTacToe: index_of_value = 11; break;
        }
        String filename = "src/networking/database/player_game_stats.csv";
        return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS,String.valueOf(gamesPlayed),index_of_value);
    }
    /**
     * the method for adding a played game to the player's games
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that the player has played
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addGamePlayed(int playerID, Game game){
        int games_played = getGamesPlayed(playerID, game);
        if (games_played >= 0){
            games_played += 1;
            return setGamesPlayed(playerID, games_played, game);
        }
        return -1;
    }

    /**
     * getter for the time that the player played
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that has been played
     * @return: the time or -1 if unsuccessful process
     */
    public int getTimePlayed(int playerID, Game game) {
        String filename = "src/networking/database/player_game_stats.csv";
        int index = 4;
        switch(game){
            case Game.Chess: index = 4; break;
            case Game.ConnectFour: index = 8; break;
            case Game.TicTacToe: index = 12; break;
        }
        String[] values = readDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS);
        if (values.length > 0){
            return Integer.parseInt(values[index]);
        }
        return -1;
    }

    /**
     * setter for the time
     * @param playerID: the unique ID that distinguishes the player
     * @param timePlayed: the time that the player played
     * @param game: the game that has been played
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setTimePlayed(int playerID, int timePlayed, Game game){
        int index_of_value = 4;
        switch (game){
            case Game.Chess: index_of_value = 4; break;
            case Game.ConnectFour: index_of_value = 8; break;
            case Game.TicTacToe: index_of_value = 12; break;
        }
        String filename = "src/networking/database/player_game_stats.csv";
        return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_GAME_STATS,String.valueOf(convertMilToSeconds(timePlayed)),index_of_value);
    }
    /**
     * the method for adding additional time to the player's games
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that the player has played
     * @param newTimePlayed: the additional time
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int addTimePlayed(int playerID, Game game, int newTimePlayed){
        int timePlayed = getTimePlayed(playerID, game) * 1000;
        if (timePlayed >= 0){
            timePlayed += newTimePlayed;
            return setTimePlayed(playerID, timePlayed, game);
        }
        return -1;
    }

    private int convertMilToSeconds(int mil){
        int thousand = 1000;
        return mil / thousand;
    }


    //--------------------GAME HISTORY--------------------------
    /**
     * getter for the history of matches
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that has been played
     * @return: the time or -1 if unsuccessful process
     */
    public String[] getPlayerMatchHistory(int playerID, Game game) {
        String[] values = readDatabaseFileForPlayerID("src/networking/database/player_match_history.csv", playerID, TOTAL_VALUES_PLAYER_MATCH_HISTORY);
        if (values.length > 0){
            int index = 1;
            switch(game){
                case Game.Chess: index = 1; break;
                case Game.ConnectFour: index = 2; break;
                case Game.TicTacToe: index = 3; break;
            }
            String[] matchHistory = values[index].split("-");
            if (!(matchHistory.length == 1 && matchHistory[0].equals("null"))) {
                return matchHistory;
            }
        }
        return new String[0]; // FAILURE
    }

    /**
     * setter for setting the match history
     * @param playerID: the unique ID that distinguishes the player
     * @param matchIDArray: array of match IDs
     * @param game: the game that has been played
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayerMatchHistory(int playerID, String[] matchIDArray, Game game){
        if (matchIDArray.length > 0) {
            int index_of_value = 1;
            switch (game) {
                case Game.Chess: index_of_value = 1;break;
                case Game.ConnectFour: index_of_value = 2;break;
                case Game.TicTacToe: index_of_value = 3;break;
            }
            String filename = "src/networking/database/player_match_history.csv";
            StringBuilder stringOfMatchHistory = new StringBuilder(matchIDArray[0]);
            for (int i = 1; i < matchIDArray.length; i++) {
                stringOfMatchHistory.append("-").append(matchIDArray[i]);
            }
            return writeValueToDatabaseFileForPlayerID(filename,playerID,TOTAL_VALUES_PLAYER_MATCH_HISTORY, stringOfMatchHistory.toString(),index_of_value);
        }
        return -1;
    }
    /**
     * the method for adding a match to the player's match history
     * @param playerID: the unique ID that distinguishes the player
     * @param game: the game that the player has played
     * @param matchID: the ID of the match
     */
    public void addMatchToPlayerMatchHistory(int playerID, String matchID, Game game){
        String[] currentMatchHistory = getPlayerMatchHistory(playerID, game);
        String[] newMatchHistory = new String[currentMatchHistory.length + 1];
        for (int i = 0; i < currentMatchHistory.length; i++) {
            newMatchHistory[i] = currentMatchHistory[i];
        }
        newMatchHistory[newMatchHistory.length - 1] = matchID;
        setPlayerMatchHistory(playerID, newMatchHistory, game);
    }

    /**
     * getter for type of game in a match
     * @param matchID: the ID of the match
     * @return: the game type
     */
    public Game getMatchGameType(String matchID) {
        String filename = "src/networking/database/match_history.csv";
        String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
        if (values.length > 0) {
            String typeStringValue = values[1];
            if (typeStringValue.equals("Chess")) {
                return Game.Chess;
            } else if (typeStringValue.equals("ConnectFour")) {
                return Game.ConnectFour;
            } else if (typeStringValue.equals("TicTacToe")) {
                return Game.TicTacToe;
            }
        }
        return null;
    }

    /**
     * method for setting the game type
     * @param matchID: the ID of a match
     * @param game: the type of the game
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setMatchGameType(String matchID, Game game){
        if (matchID.length() == MATCH_ID_LENGTH){
            String filename = "src/networking/database/match_history.csv";
            String gameType = "Chess";
            switch (game){
                case Chess: gameType = "Chess"; break;
                case ConnectFour: gameType = "ConnectFour"; break;
                case TicTacToe: gameType = "TicTacToe"; break;
            }
            writeValueToDatabaseFileForMatchID(filename, matchID, TOTAL_VALUES_MATCH_HISTORY,gameType,1);
        }
        return -1;
    }

    /**
     * getter for IDs of the players in a match
     * @param matchID: the ID of the match
     * @return: int[] of two players in a match
     */
    public int[] getPlayersOfMatch(String matchID){
        String filename = "src/networking/database/match_history.csv";
        String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
        if (values.length > 0) {
            return new int[]{Integer.parseInt(values[2]), Integer.parseInt(values[3])};
        }
        return new int[0];
    }

    /**
     * setter for players in a match
     * @param matchID: the ID of the match
     * @param player1ID: the player in the match
     * @param player2ID: the other player in the match
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setPlayersOfMatch(String matchID, int player1ID, int player2ID){
        if (matchID.length() == MATCH_ID_LENGTH && player1ID != player2ID){
            String filename = "src/networking/database/match_history.csv";
            writeValueToDatabaseFileForMatchID(filename, matchID, TOTAL_VALUES_MATCH_HISTORY,String.valueOf(player1ID),2);
            writeValueToDatabaseFileForMatchID(filename, matchID, TOTAL_VALUES_MATCH_HISTORY,String.valueOf(player2ID),3);
        }
        return -1;
    }

    /**
     * getter for outcome of the match (win, draw)
     * @param matchID: the ID of the match
     * @return: the result as a string
     */
    public String getOutcomeOfMatch(String matchID){
        String filename = "src/networking/database/match_history.csv";
        String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
        if (values.length > 0){
            return values[4];
        }
        return null;
    }

    /**
     * setter for the outcome of a match
     * @param matchID: the ID of the match
     * @param outcome: the outcome of the match
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setOutcomeOfMatch(String matchID, String outcome){
        if (matchID.length() == MATCH_ID_LENGTH) {
            if (!(outcome.equals("Winner") || outcome.equals("Draw"))) {
                outcome = "Undefined";
            }
            String filename = "src/networking/database/match_history.csv";
            return writeValueToDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY,outcome,4);
        }
        return -1;

    }

    /**
     * getting the winner in a match
     * @param matchID: the ID of the match
     * @return: the player's ID or -1 if unsuccessful process
     */
    public int getWinningPlayerOfMatch(String matchID){
        String filename = "src/networking/database/match_history.csv";
        if(getOutcomeOfMatch(matchID).equals("Winner")){
            String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
            if (values.length > 0){
                return Integer.parseInt(values[5]);
            }
        }
        return -1;
    }


    /**
     * method for setting the winner
     * @param matchID: the ID of the match
     * @param playerID: the ID of the winner player
     * @return: -1 for unsuccessful process and 1 for successful
     */
    // WARNING: PLAYER FIELDS AND OUTCOME FIELDS MUST BE FILLED OUT FIRST IN ORDER TO SET A WINNING PLAYER
    public int setWinningPlayerOfMatch(String matchID, int playerID){
        if (matchID.length() == MATCH_ID_LENGTH) {
            int[] players = getPlayersOfMatch(matchID);
            if (players.length == 2){
                if (players[0] == playerID || players[1] == playerID && getOutcomeOfMatch(matchID).equals("Winner")){
                    String filename = "src/networking/database/match_history.csv";
                    writeValueToDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY,String.valueOf(playerID),5);
                }
            }
        }
        return -1; //Failure
    }

    /**
     * getting the total number of moves in a match
     * @param matchID: the ID of the match
     * @return: number of moves or -1 if unsuccessful
     */
    public int getTotalMovesOfMatch(String matchID){
        String filename = "src/networking/database/match_history.csv";
        String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
        if (values.length > 0){
            return Integer.parseInt(values[6]);
        }
        return -1;
    }

    /**
     * setting the total number of moves in a game
     * @param matchID: the ID of the match
     * @param totalMoves: the number of moves
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setTotalMovesOfMatch(String matchID, int totalMoves){
        if (matchID.length() == MATCH_ID_LENGTH && totalMoves >= 0) {
            String filename = "src/networking/database/match_history.csv";
            writeValueToDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY,String.valueOf(totalMoves),6);
        }
        return -1; // FAILURE
    }

    /**
     * getter for the time of match
     * @param matchID: the ID of the match
     * @return: the time or -1 if unsuccessful
     */
    public int getMatchTime(String matchID){
        String filename = "src/networking/database/match_history.csv";
        String[] values = readDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY);
        if (values.length > 0){
            return Integer.parseInt(values[7]);
        }
        return -1;
    }

    /**
     * setter for match time
     * @param matchID: the ID of the match
     * @param matchTime: the time
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int setMatchTime(String matchID, int matchTime){
        if (matchID.length() == MATCH_ID_LENGTH && matchTime >= 0) {
            String filename = "src/networking/database/match_history.csv";
            writeValueToDatabaseFileForMatchID(filename,matchID,TOTAL_VALUES_MATCH_HISTORY,String.valueOf(convertMilToSeconds(matchTime)),7);
        }
        return -1; // FAILURE
    }

    /**
     * this method is for setting a match with full info if the outcome has a winner
     * @param matchID: the ID of the match
     * @param game: the type of the game
     * @param player1ID: first player in the match
     * @param player2ID: other player in the match
     * @param outcome: the outcome of the match
     * @param winningPlayerID: the ID of the winner
     * @param totalMoves: the total number of moves
     * @param matchTime: the time of the match
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int matchComplete(String matchID, Game game, int player1ID, int player2ID, String outcome, int winningPlayerID, int totalMoves,int matchTime){
        if (playerExists(player1ID) && playerExists(player2ID) && outcome.equals("Winner")) {
            String gameString = "Chess";
            switch (game) {
                case Chess: gameString = "Chess"; break;
                case ConnectFour: gameString = "ConnectFour"; break;
                case TicTacToe: gameString = "TicTacToe"; break;
            }
            addLineToFile("src/networking/database/match_history.csv", String.format("%s~%s~%d~%d~%s~%d~%d~%d",matchID,gameString,player1ID,player2ID,outcome,winningPlayerID,totalMoves,convertMilToSeconds(matchTime)));
            addMatchToPlayerMatchHistory(player1ID,matchID,game);
            addMatchToPlayerMatchHistory(player2ID,matchID,game);
            addGamePlayed(player1ID,game);
            addGamePlayed(player2ID,game);
            addTimePlayed(player1ID,game,matchTime);
            addTimePlayed(player2ID,game,matchTime);
            addGameWin(winningPlayerID,game);
            return 1;
        }

        return -1;
    }

    /**
     * this method is for setting a match with full info if the outcome does not have a winner
     * @param matchID: the ID of the match
     * @param game: the type of the game
     * @param player1ID: first player in the match
     * @param player2ID: other player in the match
     * @param outcome: the outcome of the match
     * @param totalMoves: the total number of moves
     * @param matchTime: the time of the match
     * @return: -1 for unsuccessful process and 1 for successful
     */
    public int matchComplete(String matchID, Game game, int player1ID, int player2ID, String outcome, int totalMoves,int matchTime){
        if (playerExists(player1ID) && playerExists(player2ID)) {
            String gameString = "Chess";
            switch (game) {
                case Chess: gameString = "Chess"; break;
                case ConnectFour: gameString = "ConnectFour"; break;
                case TicTacToe: gameString = "TicTacToe"; break;
            }
            addLineToFile("src/networking/database/match_history.csv", String.format("%s~%s~%d~%d~%s~-1~%d~%d",matchID,gameString,player1ID,player2ID,outcome,totalMoves,convertMilToSeconds(matchTime)));
            addMatchToPlayerMatchHistory(player1ID,matchID,game);
            addMatchToPlayerMatchHistory(player2ID,matchID,game);
            addGamePlayed(player1ID,game);
            addGamePlayed(player2ID,game);
            addTimePlayed(player1ID,game,matchTime);
            addTimePlayed(player2ID,game,matchTime);
            return 1;
        }

        return -1;
    }
}
