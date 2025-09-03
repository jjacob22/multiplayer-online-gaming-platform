package match_making;

import networking.ProfileDatabaseManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MatchChallenge implements Serializable {
    private int challengerID;
    private String challenger;
    private int challengedID;
    private String challenged;
    private Game game;
    private LocalDateTime date;

    protected int getChallengerID() {
        return challengerID;
    }

    public String getChallenger() {
        return challenger;
    }

    protected int getChallengedID() {
        return challengedID;
    }

    public String getChallenged() {
        return challenged;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Game getGame() {
        return game;
    }

    public MatchChallenge(int challengerID, int challengedID, ProfileDatabaseManager profileDb, Game game) {
        this.challengerID = challengerID;
        this.challengedID = challengedID;
        this.challenger = profileDb.getPlayerUsername(challengerID);
        this.challenged = profileDb.getPlayerUsername(challengedID);
        this.date = LocalDateTime.now();
        this.game = game;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MatchChallenge m &&
                m.getChallengerID() == getChallengerID() &&
                m.getChallengedID() == getChallengedID() &&
                m.date.equals(date) &&
                m.game.equals(game) ||
                super.equals(obj));
    }
}
