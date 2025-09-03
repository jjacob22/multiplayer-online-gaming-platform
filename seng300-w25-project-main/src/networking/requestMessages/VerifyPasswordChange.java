package networking.requestMessages;

import java.io.Serializable;

public record VerifyPasswordChange(String code, String username,String newPassword, String retypePassword) implements Serializable {
}
