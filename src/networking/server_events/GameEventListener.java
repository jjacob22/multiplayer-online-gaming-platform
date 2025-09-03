package networking.server_events;

import networking.requestMessages.PromotePawn;

import java.util.EventListener;

public interface GameEventListener extends EventListener {
    public void chatMessageReceived(ChatMessageEvent e);

    public void gameStateChanged(GameStateEvent e);

    public void exceptionOccurred(ServerExceptionEvent e);
}
