package networking.requestMessages;

import java.io.Serializable;


public record Signup(String email, String password, String user, String confirmPass) implements Serializable {
}
