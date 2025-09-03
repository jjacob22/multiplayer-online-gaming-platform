package networking.requestMessages;

import java.io.Serializable;

/**
 * A record object for sending requests to promote a pawn.
 * @param promotion the piece class which the user would like the pawn to be promoted to
 */
public record PromotePawn(Promotion promotion) implements Serializable {
    public enum Promotion {
        Queen, Bishop, Rook, Knight
    }
}
