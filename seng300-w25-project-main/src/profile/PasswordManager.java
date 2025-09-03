package profile;

// Imports for password hashing algorithm
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Imports to check password strength using pattern and match
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PasswordManager {
    /*
    * Class PasswordManagement has static methods to manage user passwords
    * Store hashed password + salt (need salt plaintext to verify passwords)
    */

    // PBKDF2 Password hashing algorithm using these constants
    private static final int SALT_LENGTH = 32; // 32 bytes in length
    private static final int KEY_LENGTH = 512; // 512-bit key
    private static final int ITERATIONS = 70000; // 70 000 iterations of PBKDF2 algorithm

    // Regex used to check password strength
    // Between 8-32 characters, at least 1 uppercase and lowercase, and at least 1 number and 1 special char
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?/]).{8,32}$";

    /*
     * Method uses PBKDF2 Algorithm to hash user entered password+salt
     * @param password - user entered password in plaintext
     * @param salt - randomly generated salt in plaintext
     * 
     * @return cipher - hashed user password using PBKDF2 algorithm after adding random salt
     */
    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch(NoSuchAlgorithmException n) {
            throw new RuntimeException("Invalid hashing algorithm", n);
        } catch(InvalidKeySpecException | IllegalArgumentException i) {
            throw new RuntimeException("Invalid key specifications", i);
        }
    }

    /*
     * Method uses cryptographically secure random number generator to produce 32 byte salt
     * @return salt - cryptographically secure randomly generated salt of preefined length
     */
    public static String createSalt() {
        SecureRandom next = new SecureRandom();
        byte[] process = new byte[SALT_LENGTH];
        next.nextBytes(process);
        return Base64.getEncoder().encodeToString(process);
    }

    /*
     * Method compares password from database to user entered password
     * @param databasePass - password stored in database corresponding to username/email
     * @param enteredPass - user entered password (attempt to login)
     * @param databaseSalt - unique salt used in password hashing corresponding to username/email
     * 
     * Returns: 
     * @return true if password exists in database, false otherwise
     */
    public static boolean verifyPassword(String databasePass, String enteredPass, String databaseSalt) {
        String newHashedPass = hashPassword(enteredPass, databaseSalt);
        return databasePass.equals(newHashedPass);
    }

    /*
     * Method checks password strength using defined REGEX 
     * @param password - plaintext password compared with regex to ensure
     * it follows safety/security guidelines
     * 
     * Returns:
     * @return true if password strength is strong, false otherwise
     */
    public static boolean checkPasswordStrength(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}