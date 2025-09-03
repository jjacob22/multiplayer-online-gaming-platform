package leaderboard;

import match_making.Game;

public class ELOCalculator {
    /**
     * K-factors are obtained for each player based on their ELO and the game.
     * @param ELO The ELO score of a player who's K-factor is to be obtained.
     * @return The K-factor of the player with the given ELO.
     */
    private int getKFactor(int ELO, Game game) {
        // TODO: These are just sample values, find reasonable ones
        int K;
        switch (game) {
            case Chess:
                if (ELO < 2100) K = 32;
                else if (ELO < 2400) K = 24;
                else K = 16;
                break;

            case TicTacToe:
                K = 10;
                break;
            case ConnectFour:
                K = 20;
                break;
            default:
                K = 32;
        }
        return K;
    }

    /**
     * Updates the ELO scores of the given players.
     * @param player1Stats The player statistics of player 1.
     * @param player2Stats The player statistics of player 2.
     * @param results The results of the match which should cause a change in ELO.
     */
    public void updateScores(GameStatistics player1Stats, GameStatistics player2Stats, MatchInfo results) {
        int K1 = getKFactor(player1Stats.getELO(), results.getGame()); // K-factor for player 1
        int K2 = getKFactor(player2Stats.getELO(), results.getGame()); // K-factor for player 2

        double expectedScore1 = 1 / (1 + Math.pow(10, (player2Stats.getELO() - player1Stats.getELO()) / 400.0));
        double expectedScore2 = 1 / (1 + Math.pow(10, (player1Stats.getELO() - player2Stats.getELO()) / 400.0));

        double score1, score2;

        if (results.getWinnerID() == player1Stats.getAccountID()) { // P1 wins
            score1 = 1.0;
            score2 = 0.0;
        } else if (results.getWinnerID() == player2Stats.getAccountID()) { // P2 wins
            score1 = 0.0;
            score2 = 1.0;
        } else { // Draw
            score1 = 0.5;
            score2 = 0.5;
        }

        Integer newRatingPlayer1 = (int) (player1Stats.getELO() + K1 * (score1 - expectedScore1));
        Integer newRatingPlayer2 = (int) (player2Stats.getELO() + K2 * (score2 - expectedScore2));

        player1Stats.setELO(newRatingPlayer1);
        player2Stats.setELO(newRatingPlayer2);
    }

}