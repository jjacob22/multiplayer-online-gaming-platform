package networking.requestMessages;

import java.io.Serializable;

public record BlockUser(String userToBlock) implements Serializable {
}
