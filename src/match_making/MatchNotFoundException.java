package match_making;

public class MatchNotFoundException extends RuntimeException {
    public String matchID;
    public MatchNotFoundException(String message, String matchID) {
        super(message);
        this.matchID = matchID;
    }
}
