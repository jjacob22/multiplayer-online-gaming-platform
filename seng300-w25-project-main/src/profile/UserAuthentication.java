package profile;
import networking.AbstractDatabaseManager;
import networking.ProfileDatabaseManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

/*
 * UserAuthentication class allows users to login and signup
 * Includes processes like forget password and reset password
 * Contains methods needed for these processes (2FA, sending emails etc.)
 * Uses ProfileDatabaseManager to store and retrieve user information
 */
public class UserAuthentication {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int TIMEOUT_MINUTES = 15;
    private static HashMap<String, Integer> loginAttempts = new HashMap<>();
    private static HashMap<String, LocalDateTime> timeoutTracker = new HashMap<>();
    private static HashMap<String, String> verificationCodes = new HashMap<>();
    private static ProfileDatabaseManager profileDatabaseManager = new ProfileDatabaseManager();

    /*
     * Method facilitates the login process with error checking
     * @param username - user entered name
     * @param password - user entered password
     *
     * @return true if login was successful, false otherwise
     */
    public static boolean login(String username, String password) throws Exception {

        int userID = AbstractDatabaseManager.getPlayerIDFromUsername(username);

        // Check to see user exists in database
        if (!AbstractDatabaseManager.playerExists(userID)) {
            throw new Exception("User does not exist");
        }

        // Check if account is timed out
        if (isAccountTimedOut(username)) {
            throw new Exception ("Account temporarily locked due to too many failed attempts. Please try again later.");
        }
        boolean validPass;
        try {
            // Verify entered password with password/salt in database
            String databaseUserPass = profileDatabaseManager.getPlayerPass(userID);
            String databaseUserSalt = profileDatabaseManager.getPlayerSalt(userID);
            validPass = PasswordManager.verifyPassword(databaseUserPass, password, databaseUserSalt);
        } catch (Exception e) {
            throw new Exception ("Cannot read player information");
        }
        if (validPass) {
            // Reset login attempts on successful password
            loginAttempts.remove(username);
            return true;
        } else {
            updateLoginAttempts(username);
            throw new Exception ("Username or Password is incorrect");
        }
    }

    /*
     * Compares code stored to user enter code
     * @param username - user entered username
     * @param code - user entered code
     *
     * @return true if 2FA was verified, false otherwise
     */
    public static boolean verify2FA(String username, String code) {
        String storedCode = verificationCodes.get(username);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(username);
            return true;
        }
        return false;
    }

    /*
     * Method is used to facilitate the signup process with error checking
     * @param email - user entered email
     * @param password - user entered password
     * @param user - use entered username
     * @param confirmPass - user enters password again to confirm
     *
     * @return true if signup was successful, false otherwise
     */
    public static boolean signup(String email, String password, String user, String confirmPass) throws Exception {
        // Check if any args contains '~'; cannot be stored in database
        char invalidChar = '~';
        if ((email.indexOf(invalidChar) != -1) || (user.indexOf(invalidChar) != -1) || (password.indexOf(invalidChar) != -1)) {
            throw new Exception("Invalid character in use: '~'\n");
        }

        // Check to see user email is a valid email address
        if (!EmailManager.validateEmail(email)) {
            throw new Exception("Invalid email address\n");
        }

        // Check to see user entered password follows security guidelines
        if (!PasswordManager.checkPasswordStrength(password)) {
            throw new Exception("""
                    Password strength weak: Must contain at least:
                    - 8+ characters
                    - Include 1 uppercase letter
                    - Include 1 lowercase letter
                    - Include 1 number (0-9)
                    - Include 1 special character (!@#$%^&*...)
                    """);
        }

        // Checks to see if both passwords entered by user matches
        if (!password.equals(confirmPass)) {
            throw new Exception("Passwords do not match!\n");
        }

        // Check to see if email or username is already taken in database
        String[] duplicateUsername = AbstractDatabaseManager.getAllUsernames();
        String[] duplicateEmail = AbstractDatabaseManager.getAllEmails();
        if ( contains(duplicateUsername, user) || contains(duplicateEmail, email)) {
            throw new Exception("Username or email already exists!\n");
        }

        // Send welcome email with verification
        String code = generateVerificationCode();
        sendVerificationEmail(email, code);
        verificationCodes.put(user, code);
        return true;
    }

    /**
     * Create new profile after verify 2fa
     * @param code - verification code to verify successful signup
     * @param username - username of user attempting to signup
     * @param password - password of user attempting to signup
     * @param email - email of user
     * @return true if code match, false otherwise
     */
    public static boolean verifySignup (String code, String username, String password, String email){
        if (verify2FA(username, code)){
            String salt = PasswordManager.createSalt();
            String hashedPassword = PasswordManager.hashPassword(password, salt);

            // Creating a new profile
            // Registration process
            int avatar = 1; // default avatar (values from 1-5)

            Profile newUser = new Profile(username, hashedPassword, salt, email, avatar);
            return true;
        }
        return false;
    }

    /*
     * Method finds a target value within an array
     * @param list - array of Strings
     * @param target - String that we are trying to find in list
     *
     * @return true if the array contains target, false otherwise
     */
    public static boolean contains(String[] list, String target) {
        for (String element : list) {
            if (element.equalsIgnoreCase(target)) return true;
        }
        return false;
    }

    /*
     * Method sends verification code to email to allow user to reset password
     * @param username - user profile's username
     *
     * @return true if code successfully sent to email, false otherwise
     */
    public static boolean forgotPassword(int userID) throws Exception{
        if (!AbstractDatabaseManager.playerExists(userID)) {
            throw new Exception("User does not exist");
        }

        String resetCode = generateVerificationCode();
        verificationCodes.put(profileDatabaseManager.getPlayerUsername(userID), resetCode);
        sendResetPasswordEmail(profileDatabaseManager.getEmail(userID), resetCode);
        return true;
    }

    /*
     * Method resets user password
     * @param username - username of profile requesting reset
     * @param resetCode - code that is sent to user email
     * @param newPassword - user enters new password to replace previous
     *
     * @return true if password was successfully reset, false otherwise
     */
    public static boolean resetPassword(String username, String resetCode, String newPassword, String retypePassword) throws Exception {
        int userID = AbstractDatabaseManager.getPlayerIDFromUsername(username);
        // Check to see user and code exists
        // Check to see code in database matches user entered one
        if ( !AbstractDatabaseManager.playerExists(userID) || !verificationCodes.containsKey(profileDatabaseManager.getPlayerUsername(userID)) ||
                !verificationCodes.get(profileDatabaseManager.getPlayerUsername(userID)).equals(resetCode)) {
            throw new Exception("Invalid reset code or email");
        }

        // Check new password strength
        if (!PasswordManager.checkPasswordStrength(newPassword)) {
            throw new Exception("""
                    Password strength weak: Must contain at least:
                    - 8+ characters
                    - Include 1 uppercase letter
                    - Include 1 lowercase letter
                    - Include 1 number (0-9)
                    - Include 1 special character (!@#$%^&*...)
                    """);
        }
        // Checks to see if both passwords entered by user matches
        if (!newPassword.equals(retypePassword)) {
            throw new Exception("Passwords do not match!\n");
        }

        String newSalt = PasswordManager.createSalt();
        String newHashedPassword = PasswordManager.hashPassword(newPassword, newSalt);
        profileDatabaseManager.setPlayerPass(userID, newHashedPassword);
        profileDatabaseManager.setPlayerSalt(userID, newSalt);
        verificationCodes.remove(profileDatabaseManager.getPlayerUsername(userID));
        return true;
    }

    /*
     * Method updates the number of login attempts
     * @param username - user that is attempting to login
     */
    private static void updateLoginAttempts(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);

        // Account is locked if user exceeds the maximum attempt
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            timeoutTracker.put(username, LocalDateTime.now().plusMinutes(TIMEOUT_MINUTES));
            loginAttempts.remove(username);
        }
    }

    /*
     * Method checks to see if account is locked/timed out
     * @param username - user profile
     *
     * @return true if account is locked/timed out, false otherwise
     */
    private static boolean isAccountTimedOut(String username) {
        LocalDateTime timeout = timeoutTracker.get(username);
        if (timeout == null) return false;

        if (LocalDateTime.now().isBefore(timeout)) {
            return true;
        } else {
            timeoutTracker.remove(username);
            return false;
        }
    }

    /*
     * Method generates a random 6 digit verification code
     *
     * @return 000000 - 999999 String erification code
     */
    private static String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    /*
     * Method sends verification code to email
     * @param email - email of the user
     * @param code - randomly generated 2FA code
     */
    private static void sendVerificationEmail(String email, String code) throws Exception{
        try {
            EmailManager.sendEmail(email, "Verification Code",
                    "Please use the following code to verify your login: " + code);
        } catch(Exception e) {
            throw new Exception("Unable to send email" + e.getMessage());
        }
    }

    /*
     * Method to send verification code to email regarding password resets
     * @param email - email of the user
     * @param code - randomly generated 2FA code
     */
    private static void sendResetPasswordEmail(String email, String code) throws Exception{
        try {
            EmailManager.sendEmail(email, "Password Reset Code",
                    "Use this code to reset your password: " + code + "\nIf you didn’t request this, ignore this email.");
        } catch(Exception e) {
            throw new Exception("Unable to send email" + e.getMessage());
        }
    }

}