package profile;

import networking.ProfileDatabaseManager;
import networking.AbstractDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ProfileSettingsTest {

    private ProfileDatabaseManager db;
    private ProfileSettings ps;
    private AbstractDatabaseManager ab;

    Profile CoralReef1;
    Profile OceanWhisper2;
    Profile TideSurfer3;

    @BeforeEach
    void setUp() throws Exception {
        // Create a fake ProfileDatabaseManager that overrides relationship methods.
        db = new ProfileDatabaseManager() {

            @Override
            public String getPlayerUsername(int playerID) {
                return switch (playerID) {
                    case 1 -> "CoralReef1";
                    case 2 -> "OceanWhisper2";
                    case 3 -> "TideSurfer3";
                    default -> "UnknownPlayer";
                };
            }

            @Override
            public String getBio(int playerID) {
                return switch (playerID) {
                    case 1 -> "InnovativeCoder42";
                    case 2 -> "CreativeMind99";
                    case 3 -> "TechEnthusiast777";
                    default -> "UnknownBio";
                };
            }

            @Override
            public String getPlayerPass(int playerID) {
                return switch (playerID) {
                    case 1 -> "passwordCoral";
                    case 2 -> "passwordOceanWhisper";
                    case 3 -> "passwordTideSurfer";
                    default -> null;
                };
            }

            @Override
            public String getPlayerSalt(int playerID) {
                return switch (playerID) {
                    case 1 -> "saltCoral";
                    case 2 -> "saltOceanWhisper";
                    case 3 -> "saltTideSurfer";
                    default -> null;
                };
            }

            @Override
            public String getEmail(int playerID) {
                return switch (playerID) {
                    case 1 -> "emailCoral@gmail.com";
                    case 2 -> "emailOceanWhisper@gmail.com";
                    case 3 -> "emailTideSurfer@gmail.com";
                    default -> "UnknownEmail@gmail.com";
                };
            }

            @Override
            public int getAvatar(int playerID) {
                return switch (playerID) {
                    case 1 -> 1;
                    case 2 -> 2;
                    case 3 -> 3;
                    default -> 0;
                };
            }

            @Override
            public int setPlayerUsername(int userId, String newUserName) {
                return 1;
            }

            @Override
            public int setBio(int userId, String newBio) {
                return 1;
            }

            @Override
            public int setEmail(int userId, String newEmail) {
                return 1;
            }

            @Override
            public int setPlayerPass(int userId, String newPlayerPass) {
                return 1;
            }

            @Override
            public int setPlayerSalt(int userId, String newPlayerSalt) {
                return 1;
            }

            @Override
            public int setAvatar(int userId, int avatar) {
                return 1;
            }

            // Override relationship methods to return empty arrays.
            @Override
            public int[] getFriendList(int userId) {
                // Return an empty friend list.
                return new int[0];
            }

            @Override
            public int[] getBlockedBy(int userId) {
                // Return an empty blocked-by list.
                return new int[0];
            }

            @Override
            public int[] getHasBlocked(int userId) {
                // Return an empty has-blocked list.
                return new int[0];
            }

            @Override
            public int removeFriend(int userId, int friendId) {
                // Simulate a successful removal.
                return 1;
            }

            @Override
            public int removeHasBlocked(int userId, int blockedId) {
                return 1;
            }

            @Override
            public int removeBlockedBy(int userId, int blockerId) {
                return 1;
            }
        };

        // Create a custom AbstractDatabaseManager that simulates uniqueness checks.
        ab = new AbstractDatabaseManager() {
            @Override
            public boolean isEmailUnique(String email) {
                return !"sameEmail@gmail.com".equals(email);
            }
            @Override
            public boolean isUsernameUnique(String username) {
                return !"sameUsername".equals(username);
            }
        };

        // Use the testing constructor to enable testing mode.
        ps = new ProfileSettings(db);

        // Inject our custom AbstractDatabaseManager.
        Field abField = ps.getClass().getDeclaredField("abstractDatabaseManager");
        abField.setAccessible(true);
        abField.set(ps, ab);

        // Create test Profile objects.
        CoralReef1 = new Profile(1, "CoralReef1", "InnovativeCoder42", "passwordCoral", "saltCoral", "emailCoral@gmail.com", 1);
        OceanWhisper2 = new Profile(2, "OceanWhisper2", "CreativeMind99", "passwordOceanWhisper", "saltOceanWhisper", "emailOceanWhisper@gmail.com", 2);
        TideSurfer3 = new Profile(3, "TideSurfer3", "TechEnthusiast777", "passwordTideSurfer", "saltTideSurfer", "emailTideSurfer@gmail.com", 3);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Cleanup if necessary.
    }

    // ===================== Username Tests ======================

    @Test
    void editUsernameValid() throws Exception {
        String validUserName = "ValidUser123";
        assertTrue(ps.editUsername(1, validUserName));
    }

    @Test
    void editUsernameInvalidInput() throws Exception {
        String invalidUserName = "Bad~Username";
        Exception exception = assertThrows(Exception.class, () -> ps.editUsername(1, invalidUserName));
        assertEquals("Username contains invalid character '~'.", exception.getMessage());
    }

    @Test
    void eidtUsernameInvalidInputSpecialCase() throws Exception {
        String invalidUserName = "User@Name";
        Exception exception = assertThrows(Exception.class, () -> ps.editUsername(1, invalidUserName));
        assertEquals("Username must contain only alphanumeric characters.", exception.getMessage());
    }

    @Test
    void editUsernameInvalidShort() throws Exception {
        String shortUserName = "ab";
        Exception exception = assertThrows(Exception.class, () -> ps.editUsername(1, shortUserName));
        assertEquals("Username must be between 3 and 20 characters.", exception.getMessage());
    }

    @Test
    void editUsernameInvalidLong() throws Exception {
        String longUserName = "abcdefghijklmnopqrstu";
        Exception exception = assertThrows(Exception.class, () -> ps.editUsername(1, longUserName));
        assertEquals("Username must be between 3 and 20 characters.", exception.getMessage());
    }

    @Test
    void editUsernameInvalidUniquness() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> ps.editUsername(1, "sameUsername"));
        assertEquals("Username is not unique.", exception.getMessage());
    }

    // ===================== Bio Tests ======================

    @Test
    void editBioValid() throws Exception {
        String validBio = "ValidBio1234";
        assertTrue(ps.editBio(1, validBio));
    }

    @Test
    void editBioValidEmpty() throws Exception{
        assertTrue(ps.editBio(1,""));
    }

    @Test
    void editBioInvalidInput() throws Exception {
        String invalidBio = "Bad~BioString";
        Exception exception = assertThrows(Exception.class, () -> ps.editBio(1, invalidBio));
        assertEquals("Bio contains invalid character '~'.", exception.getMessage());
    }

    @Test
    void editBioInvalidInputSpecialCase() throws Exception {
        String invalidBio = "Bio@123456";
        Exception exception = assertThrows(Exception.class, () -> ps.editBio(1, invalidBio));
        assertEquals("Bio must contain only alphanumeric characters.", exception.getMessage());
    }

    @Test
    void editBioInvalidLong() throws Exception {
        String longBio = "A".repeat(121);
        Exception exception = assertThrows(Exception.class, () -> ps.editBio(1, longBio));
        assertEquals("Bio must be less than 120 characters.", exception.getMessage());
    }

    // ===================== Email Tests ======================

    @Test
    void editEmailValid() throws Exception {
        Field field = ps.getClass().getDeclaredField("lastVerificationCode");
        field.setAccessible(true);
        field.set(ps, "123456");
        assertTrue(ps.changeEmail(1, "newemail@gmail.com", "123456"));
    }

    @Test
    void editEmailInvalidInput() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                ps.changeEmail(1, "not-an-email", "anyCode")
        );
        assertEquals("Invalid email address.", exception.getMessage());
    }

    @Test
    void editEmailInvalidUniqueness() throws Exception {
        Field field = ps.getClass().getDeclaredField("lastVerificationCode");
        field.setAccessible(true);
        field.set(ps, "123456");
        Exception exception = assertThrows(Exception.class, () -> ps.changeEmail(1, "sameEmail@gmail.com", "123456"));
        assertEquals("Email address is already in use.", exception.getMessage());
    }

    // ===================== Avatar Tests ======================

    @Test
    void editAvatarValid() throws Exception {
        assertTrue(ps.editAvatar(1, 3));
    }

    @Test
    void editAvatarInvalidInput() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> ps.editAvatar(1, 0));
        assertEquals("Avatar value must be between 1 and 5.", exception.getMessage());
    }

    @Test
    void editAvatarInvalidInputOutofBound() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> ps.editAvatar(1, 6));
        assertEquals("Avatar value must be between 1 and 5.", exception.getMessage());
    }

    // ===================== Change Password Tests ======================

    @Test
    void changePasswordValid() throws Exception {
        boolean result = ps.changePassword(1, "passwordCoral", "NewStrongPassword1", "NewStrongPassword1");
        assertTrue(result);
    }

    @Test
    void changePasswordInvalidIncorrectPw() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                ps.changePassword(1, "wrongPassword", "NewStrongPassword1", "NewStrongPassword1")
        );
        assertEquals("Current password is incorrect.", exception.getMessage());
    }

    @Test
    void changePasswordInvalidNpwWeak() throws Exception {
        Exception exception = assertThrows(Exception.class, () ->
                ps.changePassword(1, "passwordCoral", "weakPassword", "weakPassword")
        );
        assertEquals("New password does not meet security requirements.", exception.getMessage());
    }

    @Test
    void changePasswordInvalidRetypePw() throws Exception {
        String pw = "Hello12@Kitty";
        String rpw = "Hello122@Kitty";
        Exception exception = assertThrows(Exception.class, () ->
                ps.changePassword(1, "passwordCoral", pw, rpw)
        );
        assertEquals("New passwords do not match.", exception.getMessage());
    }

    // ===================== Delete Player Tests ======================

    @Test
    void deletePlayerValid() throws Exception {
        // In testing mode, the existence check is bypassed.
        assertDoesNotThrow(() -> ps.deleteProfile(1, "passwordCoral"));
    }

    @Test
    void deletePlayerInvalid() throws Exception {
        Exception exception = assertThrows(Exception.class, () -> ps.deleteProfile(1, "wrongPassword"));
        assertEquals("Incorrect password.", exception.getMessage());
    }
}
