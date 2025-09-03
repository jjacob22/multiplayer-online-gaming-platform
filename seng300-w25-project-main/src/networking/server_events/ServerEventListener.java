package networking.server_events;

import java.util.EventListener;
import java.util.EventObject;

public interface ServerEventListener extends EventListener {
    public void matchFound(MatchFoundEvent e);
}
