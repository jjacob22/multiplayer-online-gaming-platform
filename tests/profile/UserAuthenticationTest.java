package profile;

import networking.AbstractDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthenticationTest {
    String email, user, password, confirmPass, salt;
    UserAuthentication userAuthentication = new UserAuthentication();

    // Mock for the sendVerificationCode method
    public static class MockUserAuthentication extends UserAuthentication {
        public void sendVerificationEmail(String email, String code) throws Exception {
            // Mock implementation: Do nothing
        }
    }

    @BeforeEach
    void setUp() {
        email = "user23@email.com";
        user = "user23";
        password = "GoodP4ssword!";
        confirmPass = "GoodP4ssword!";
        salt = PasswordManager.createSalt();
        // Reset static fields before each test
        resetStaticFields();
        if (!AbstractDatabaseManager.playerExists(AbstractDatabaseManager.getPlayerIDFromUsername(user))) {
            Profile newProfile = new Profile(user, PasswordManager.hashPassword(password, salt), salt, email, 2);
        }
    }

    @AfterEach
    void tearDown() {
        if (AbstractDatabaseManager.playerExists(AbstractDatabaseManager.getPlayerIDFromUsername(user))) {
            int id = AbstractDatabaseManager.getPlayerIDFromUsername(user);
            AbstractDatabaseManager.removePlayerProfile(id);
        }

        email = null;
        user = null;
        password = null;
        confirmPass = null;
        salt = null;
    }
    private void resetStaticFields() {
        try {
            Field loginAttempts = UserAuthentication.class.getDeclaredField("loginAttempts");
            loginAttempts.setAccessible(true);
            loginAttempts.set(null, new HashMap<String, Integer>());

            Field timeoutTracker = UserAuthentication.class.getDeclaredField("timeoutTracker");
            timeoutTracker.setAccessible(true);
            timeoutTracker.set(null, new HashMap<String, LocalDateTime>());

            Field verificationCodes = UserAuthentication.class.getDeclaredField("verificationCodes");
            verificationCodes.setAccessible(true);
            verificationCodes.set(null, new HashMap<String, String>());
        } catch (Exception e) {
            fail("Failed to reset static fields: " + e.getMessage());
        }
    }

    @Test
    void loginSuccess() throws Exception {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        String newUser = sb.toString();
        String newEmail = newUser + "@outlook.ca";

        String salt = PasswordManager.createSalt();
        Profile profile = new Profile(newUser, PasswordManager.hashPassword(password, salt), salt, newEmail,1);
        try {
            assertTrue(UserAuthentication.login(newUser, password));
        } catch (Exception e) {
            throw new Exception(e);
        }
        int id = AbstractDatabaseManager.getPlayerIDFromUsername(newUser);
        AbstractDatabaseManager.removePlayerProfile(id);
    }

    @Test
    void loginFail() {
        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.login("PeterGriffin","Wrong")
        );
        assertTrue(thrown.getMessage().contains("Username or Password is incorrect"),
                "Username or Password is incorrect");
    }
    @Test
    void loginUserNotExistTest() {
        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.login("nonexistent", password)
        );
        assertTrue(thrown.getMessage().contains("User does not exist"),
                "Login fails when user doesn't exist");
    }

    @Test
    void loginAccountLockedTest() throws Exception {
        // Simulate too many login attempts
        Method updateLoginAttempts = UserAuthentication.class.getDeclaredMethod("updateLoginAttempts", String.class);
        updateLoginAttempts.setAccessible(true);
        for (int i = 0; i < 5; i++) {
            updateLoginAttempts.invoke(null, user);
        }

        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.login(user, password)
        );
        assertTrue(thrown.getMessage().contains("Account temporarily locked due to too many failed attempts. Please try again later."),
                "Account should be locked after max attempts");
    }

    @Test
    void verify2FASuccessTest() throws Exception {
        String code = "123456";
        Field verificationCodes = UserAuthentication.class.getDeclaredField("verificationCodes");
        verificationCodes.setAccessible(true);
        HashMap<String, String> codes = (HashMap<String, String>) verificationCodes.get(null);
        codes.put(user, code);

        assertTrue(UserAuthentication.verify2FA(user, code),
                "2FA should succeed with correct code");
        assertNull(codes.get(user), "Code should be removed after successful verification");
    }

    @Test
    void verify2FAFailTest() {
        String code = "123456";
        assertFalse(UserAuthentication.verify2FA(user, code), "2FA fails as user does not have a code");
    }

    @Test
    void signupInvalidCharacterUserTest() {
        String badUser = "user~";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(email, password, badUser, confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Invalid character in use: '~'"), "Database cannot store '~'");
    }

    @Test
    void signupInvalidCharacterPassTest() {
        String badPass = "Pa4ssword!~";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(email, badPass, user, badPass)
        );
        assertTrue(thrown.getMessage().contains("Invalid character in use: '~'"), "Database cannot store '~'");
    }

    @Test
    void signupInvalidCharacterEmailTest() {
        String badEmail = "email~bad@outlook.ca";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(badEmail, password, user, confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Invalid character in use: '~'"), "Database cannot store '~'");
    }

    @Test
    void signupInvalidEmailTest() {
        String invalidEmail = "@gmail.com";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(invalidEmail, password, user, confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Invalid email address"), "Invalid email entered");
    }

    @Test
    void signupWeakPasswordTest() {
        String weakPass = "pa2s";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(email, weakPass, user, confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Password strength weak"), "Password does not meet security requirements");
    }

    @Test
    void signupDifferentPasswordsTest() {
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup(email, password, user, "differentPass1.")
        );
        assertTrue(thrown.getMessage().contains("Passwords do not match!"), "Passwords entered are different");
    }


    @Test
    void signupUserExistsTest() {
        String takenUser = "BrianGriffin";
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup("email@email.com", password, takenUser, confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Username or email already exists!"), "Username is taken");
    }


    @Test
    void signupEmailExistsTest() {
        Exception thrown = assertThrows(
                Exception.class,
                () -> userAuthentication.signup("cheesedanishboy@gmail.com", password, "different_user", confirmPass)
        );
        assertTrue(thrown.getMessage().contains("Username or email already exists!"), "Email is taken");
    }

    @Test
    void signupSuccessTest() throws Exception {
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        String newUser = sb.toString();
        String newEmail = newUser + "@outlook.ca";

        Field verificationCodesField = UserAuthentication.class.getDeclaredField("verificationCodes");
        verificationCodesField.setAccessible(true);
        HashMap<String, String> codes = (HashMap<String, String>) verificationCodesField.get(null);
        String code = "123456";
        codes.put(user, code);
        UserAuthentication mock = new MockUserAuthentication();
        assertTrue(mock.signup(newEmail, password, newUser, confirmPass), "Signup is successful");
    }

    @Test
    void verifySignupSuccessTest() throws Exception {
        String code = "123456";
        Field verificationCodes = UserAuthentication.class.getDeclaredField("verificationCodes");
        verificationCodes.setAccessible(true);
        HashMap<String, String> codes = (HashMap<String, String>) verificationCodes.get(null);
        codes.put(user, code);

        assertTrue(UserAuthentication.verifySignup(code, user, password, email),
                "Signup verification should succeed with correct code");
    }

    @Test
    void verifySignupFailTest() {
        String code = "123456";
        assertFalse(UserAuthentication.verifySignup(code, user, password, email),
                "Signup verification should fail with no code stored");
    }


    @Test
    void contains() {
        String[] list = new String[]{"in", "over", "under"};
        String in = "in";
        String out = "out";

        assertTrue(UserAuthentication.contains(list, in), "List contains String argument");
        assertFalse(UserAuthentication.contains(list, out), "List does not contain String argument");
    }

    @Test
    void forgotPasswordUserNotExistTest() {
        int invalidUserID = -1;
        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.forgotPassword(invalidUserID)
        );
        assertTrue(thrown.getMessage().contains("User does not exist"),
                "Forgot password fails for non-existent user");
    }

    @Test
    void resetPasswordSuccessTest() throws Exception {
        String resetCode = "654321";
        Field verificationCodes = UserAuthentication.class.getDeclaredField("verificationCodes");
        verificationCodes.setAccessible(true);
        HashMap<String, String> codes = (HashMap<String, String>) verificationCodes.get(null);
        codes.put(user, resetCode);

        // Would need mocking for database operations
        assertTrue(UserAuthentication.resetPassword(user, resetCode, "NewP4ssword!", "NewP4ssword!"),
                "Reset password should throw without proper mocking");
    }

    @Test
    void resetPasswordInvalidCodeTest() {
        String wrongCode = "wrong";
        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.resetPassword(user, wrongCode, "NewP4ssword!", "NewP4ssword!")
        );
        assertTrue(thrown.getMessage().contains("Invalid reset code"),
                "Reset password fails with incorrect code");
    }

    @Test
    void resetPasswordWeakPasswordTest() throws Exception {
        String resetCode = "654321";
        Field verificationCodes = UserAuthentication.class.getDeclaredField("verificationCodes");
        verificationCodes.setAccessible(true);
        HashMap<String, String> codes = (HashMap<String, String>) verificationCodes.get(null);
        codes.put(user, resetCode);

        Exception thrown = assertThrows(
                Exception.class,
                () -> UserAuthentication.resetPassword(user, resetCode, "weak", "weak")
        );
        assertTrue(thrown.getMessage().contains("Password strength weak"),
                "Reset password fails with weak password");
    }

    @Test
    void isAccountTimedOutTest() throws Exception {
        Method isAccountTimedOut = UserAuthentication.class.getDeclaredMethod("isAccountTimedOut", String.class);
        isAccountTimedOut.setAccessible(true);

        Field timeoutTracker = UserAuthentication.class.getDeclaredField("timeoutTracker");
        timeoutTracker.setAccessible(true);
        HashMap<String, LocalDateTime> tracker = (HashMap<String, LocalDateTime>) timeoutTracker.get(null);
        tracker.put(user, LocalDateTime.now().plusMinutes(5));

        assertTrue((Boolean) isAccountTimedOut.invoke(null, user),
                "Account should be timed out");

        tracker.put(user, LocalDateTime.now().minusMinutes(5));
        assertFalse((Boolean) isAccountTimedOut.invoke(null, user),
                "Timeout should be expired");
    }

    @Test
    void generateVerificationCodeTest() throws Exception {
        Method generateVerificationCode = UserAuthentication.class.getDeclaredMethod("generateVerificationCode");
        generateVerificationCode.setAccessible(true);

        String code = (String) generateVerificationCode.invoke(null);
        assertEquals(6, code.length(), "Verification code should be 6 digits");
        assertTrue(code.matches("\\d{6}"), "Code should be numeric");
    }
}