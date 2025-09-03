package networking.requestMessages;

import java.io.Serializable;

public record UnblockUser(String userToUnblock) implements Serializable {
}
