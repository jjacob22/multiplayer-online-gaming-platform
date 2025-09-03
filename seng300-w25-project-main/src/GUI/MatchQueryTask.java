package GUI;

import match_making.Game;
import networking.Client;

import java.util.TimerTask;

public class MatchQueryTask extends TimerTask {
    private Client client;
    private Game game;

    public MatchQueryTask(Client client, Game game) {
        this.client = client;
        this.game = game;
    }

    @Override
    public void run() {
        if (client.findGame(game)) {
            cancel();
        }
    }
}
