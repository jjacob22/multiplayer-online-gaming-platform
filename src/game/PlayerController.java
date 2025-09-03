package game;

import java.io.Serializable;

public class PlayerController implements Serializable {
    private final int playerID;
    private final int ownerID;
    private boolean wantToDraw;

    public PlayerController(int playerID, int ownerID){
        this.playerID = playerID;
        this.ownerID = ownerID;
        this.wantToDraw = false;
    }
    public PlayerController(int playerID, int ownerID, boolean wantToDraw){
        this.playerID = playerID;
        this.ownerID = ownerID;
        this.wantToDraw = wantToDraw;
    }
    public PlayerController(PlayerController player) {
        this.playerID = player.getPlayerID();
        this.ownerID = player.getOwnerID();
        this.wantToDraw = player.getWantToDraw();
    }
    public int getPlayerID() {
        return playerID;
    }
    public int getOwnerID() {
        return ownerID;
    }
    public boolean getWantToDraw() {
        return wantToDraw;
    }
    public void setWantToDraw(boolean wantToDraw) {
        this.wantToDraw = wantToDraw;
    }
}
