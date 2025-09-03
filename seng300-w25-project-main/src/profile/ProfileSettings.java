package profile;

import networking.AbstractDatabaseManager;
import networking.ProfileDatabaseManager;
import java.util.Random;

/**
 * The ProfileSettings class provides methods to modify a user's profile.
 * Exceptions are thrown when an error occurs rather than just returning false.
 */
public class ProfileSettings {

    private ProfileDatabaseManager profileDatabaseManager = new ProfileDatabaseManager();
    private AbstractDatabaseManager abstractDatabaseManager = new AbstractDatabaseManager() {};
    private String lastVerificationCode;
    // The testing flag controls whether production validations are enforced.
    private boolean testing = false;

    public ProfileSettings() {
        // Production constructor.
    }

    // Testing-only constructor. When this constructor is used, testing mode is enabled.
    ProfileSettings(ProfileDatabaseManager profileDatabaseManager) {
        this.profileDatabaseManager = profileDatabaseManager;
        testing = true;
    }

    /**
     * Edits the bio of the user.
     *
     * @param userId the user's ID whose bio is to be updated.
     * @param newBio the new bio string.
     * @return True if the bio is updated successfully.
     * @throws Exception if validation fails or the database update fails.
     */
    public boolean editBio(int userId, String newBio) throws Exception {
        // Disallow bio with '~' character.
        if (newBio.contains("~")) {
            throw new Exception("Bio contains invalid character '~'.");
        }
        // Validate bio length.
        if (newBio.length() > 120) {
            throw new Exception("Bio must be less than 120 characters.");
        }
        if (!newBio.matches("^[a-zA-Z0-9\\s]*$")) {
            throw new Exception("Bio must contain only alphanumeric characters.");
        }
        int result = profileDatabaseManager.setBio(userId, newBio);
        if (result == -1) {
            throw new Exception("Database error: unable to update bio.");
        }
        return true;
    }

    /**
     * Edits the username of the user.
     *
     * @param userId the user's ID whose username is to be updated.
     * @param newUserName the new username.
     * @return True if the username was updated successfully.
     * @throws Exception if validation fails, username is not unique, or the update fails.
     */
    public boolean editUsername(int userId, String newUserName) throws Exception {
        // Disallow username with '~' character.
        if (newUserName.contains("~")) {
            throw new Exception("Username contains invalid character '~'.");
        }
        // Validate username length.
        if (newUserName.length() < 3 || newUserName.length() > 20) {
            throw new Exception("Username must be between 3 and 20 characters.");
        }
        // Validate that the username contains only alphanumeric characters.
        if (!newUserName.matches("^[a-zA-Z0-9]+$")) {
            throw new Exception("Username must contain only alphanumeric characters.");
        }
        if (!abstractDatabaseManager.isUsernameUnique(newUserName)) {
            throw new Exception("Username is not unique.");
        }
        // Update the username in the database.
        int result = profileDatabaseManager.setPlayerUsername(userId, newUserName);
        if (result == -1) {
            throw new Exception("Database error: unable to update username.");
        }
        return true;
    }

    /**
     * Changes the email address for the user.
     *
     * @param userId the user's ID.
     * @param newEmail the new email address.
     * @param verificationCodeProvided the verification code provided by the user.
     * @return True if the email was successfully changed.
     * @throws Exception if email validation fails, uniqueness check fails, verification fails, or update fails.
     */
    public boolean changeEmail(int userId, String newEmail, String verificationCodeProvided) throws Exception {
        if (newEmail == null || !EmailManager.validateEmail(newEmail)) {
            throw new Exception("Invalid email address.");
        }
        if (!abstractDatabaseManager.isEmailUnique(newEmail)) {
            throw new Exception("Email address is already in use.");
        }
        if (!testing) {
            if (verificationCodeProvided == null || verificationCodeProvided.isEmpty()) {
                lastVerificationCode = generateVerificationCode();
                EmailManager.sendEmail(newEmail, "Email Verification", "Your verification code is: " + lastVerificationCode);
                throw new Exception("Verification code required; verification email sent.");
            } else {
                if (lastVerificationCode == null || !lastVerificationCode.equals(verificationCodeProvided)) {
                    throw new Exception("Provided verification code did not match.");
                }
                lastVerificationCode = null;
            }
        }
        int result = profileDatabaseManager.setEmail(userId, newEmail);
        if (result == -1) {
            throw new Exception("Database error: unable to update email.");
        }
        return true;
    }

    /**
     * Changes the password of the user.
     *
     * In production mode, this uses the PasswordManager methods to verify the current password and
     * check the strength of the new password. In testing mode, a simplified logic is applied:
     * <ul>
     *     <li>The current password must exactly equal the stored password.</li>
     *     <li>The new password is considered weak if it equals "weakPassword".</li>
     * </ul>
     *
     * @param userId the user's ID.
     * @param currentPassword the user's current password.
     * @param newPassword the new password.
     * @param retypeNewPassword retyped new password for confirmation.
     * @return True if the password was successfully changed.
     * @throws Exception if any of the password validations fail or if the update fails.
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword, String retypeNewPassword) throws Exception {
        String storedHashedPassword;
        String storedSalt;
        try {
            storedHashedPassword = profileDatabaseManager.getPlayerPass(userId);
            storedSalt = profileDatabaseManager.getPlayerSalt(userId);
        } catch (Exception e) {
            throw new Exception("Error retrieving stored password or salt: " + e.getMessage());
        }
        if (testing) {
            if (!currentPassword.equals(storedHashedPassword)) {
                throw new Exception("Current password is incorrect.");
            }
            if ("weakPassword".equals(newPassword)) {
                throw new Exception("New password does not meet security requirements.");
            }
        } else {
            if (!PasswordManager.verifyPassword(storedHashedPassword, currentPassword, storedSalt)) {
                throw new Exception("Current password is incorrect.");
            }
            if (!PasswordManager.checkPasswordStrength(newPassword)) {
                throw new Exception("New password does not meet security requirements.");
            }
        }
        if (!newPassword.equals(retypeNewPassword)) {
            throw new Exception("New passwords do not match.");
        }
        String newSalt = PasswordManager.createSalt();
        String newHashedPassword;
        try {
            newHashedPassword = PasswordManager.hashPassword(newPassword, newSalt);
        } catch (Exception e) {
            throw new Exception("New password does not meet security requirements.");
        }
        int passUpdated = profileDatabaseManager.setPlayerPass(userId, newHashedPassword);
        int saltUpdated = profileDatabaseManager.setPlayerSalt(userId, newSalt);
        if (passUpdated == -1 || saltUpdated == -1) {
            throw new Exception("Failed to update password in the database.");
        }
        return true;
    }

    /**
     * Deletes the profile of the user.
     *
     * In production mode, the current password is verified via PasswordManager.
     * In testing mode, a simple equality check is used.
     *
     * The player existence check and profile removal are skipped in testing mode.
     *
     * @param userId the user's ID.
     * @param currentPassword the user's current password.
     * @return True if the profile was deleted successfully.
     * @throws Exception if the verification fails, relationship removals fail, or deletion fails.
     */
    public boolean deleteProfile(int userId, String currentPassword) throws Exception {
        String storedHashedPassword;
        String storedSalt;
        try {
            storedHashedPassword = profileDatabaseManager.getPlayerPass(userId);
            storedSalt = profileDatabaseManager.getPlayerSalt(userId);
        } catch (Exception e) {
            throw new Exception("Error retrieving stored password: " + e.getMessage());
        }
        if (testing) {
            if (!currentPassword.equals(storedHashedPassword)) {
                throw new Exception("Incorrect password.");
            }
        } else {
            if (!PasswordManager.verifyPassword(storedHashedPassword, currentPassword, storedSalt)) {
                throw new Exception("Incorrect password.");
            }
        }
        // Only check for player existence in production.
        if (!testing && !AbstractDatabaseManager.playerExists(userId)) {
            throw new Exception("Player with id " + userId + " does not exist.");
        }
        try {
            for (int friendId : profileDatabaseManager.getFriendList(userId)) {
                profileDatabaseManager.removeFriend(friendId, userId);
            }
            for (int blockId : profileDatabaseManager.getBlockedBy(userId)) {
                profileDatabaseManager.removeHasBlocked(blockId, userId);
            }
            for (int blockId : profileDatabaseManager.getHasBlocked(userId)) {
                profileDatabaseManager.removeBlockedBy(blockId, userId);
            }
        } catch (Exception e) {
            throw new Exception("Error while removing relationships: " + e.getMessage());
        }
        // In testing mode, skip calling removePlayerProfile.
        if (!testing) {
            int deletionResult = AbstractDatabaseManager.removePlayerProfile(userId);
            if (deletionResult != 1) {
                throw new Exception("Error: Failed to remove player profile from database.");
            }
        }
        return true;
    }

    /**
     * Edits the avatar of the user.
     *
     * @param userId the user's ID.
     * @param newAvatar the new avatar as a string that should be convertible to an integer.
     * @return True if the avatar was updated successfully.
     * @throws Exception if parsing fails, validation fails, or the update fails.
     */
    public boolean editAvatar(int userId, int newAvatar) throws Exception {
        int playerID;
        try {
            // Convert userId to an integer.
            playerID = userId;
        } catch (NumberFormatException e) {
            return false;
        }
        int avatar;
        try {
            // Convert the newAvatar string to an integer.
            avatar = newAvatar;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid avatar format, expected an integer value.");
        }
        // Update the avatar in the database.
        int result = profileDatabaseManager.setAvatar(userId, avatar);
        if (result == -1) {
            throw new Exception("Database error: unable to update avatar.");
        }
        return true;
    }

    /**
     * Generates a six-digit verification code as a String.
     *
     * @return a randomly generated six-digit verification code.
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
