package profile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class PasswordManagerTest {
    private String password, salt, hashedPassword;

    @BeforeEach
    void setUp() {
        password = "Password1.";
        salt = PasswordManager.createSalt();
        hashedPassword = PasswordManager.hashPassword(password, salt);
    }

    @AfterEach
    void tearDown() {
        password = null;
        salt = null;
        hashedPassword = null;
    }

    @Test
    void hashPasswordTest() {
        String firstHash = PasswordManager.hashPassword(password, salt);
        String secondHash = PasswordManager.hashPassword(password, salt);
        assertEquals(firstHash, secondHash, "Password hashing should return same hash with the same password and salt");
        assertNotNull(PasswordManager.hashPassword(password, salt), "Valid non-null hash should be returned");
    }

    @Test
    void hashPasswordInvalidKeyTest() {
        String badSalt = "a -n[]tA_Go#=S@lt";
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> PasswordManager.hashPassword(password, badSalt)
        );
        assertNotNull(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Invalid key specifications"));
    }

    @Test
    void createSaltTest() {
        String newSalt = PasswordManager.createSalt();
        assertNotNull(newSalt, "Valid non-null salt should be returned");

        byte[] decodedSalt = Base64.getDecoder().decode(newSalt);
        assertEquals(32, decodedSalt.length, "Salt should have fixed length of 32 bytes");
    }

    @Test
    void createSaltUniqueTest() {
        String salt1 = PasswordManager.createSalt();
        String salt2 = PasswordManager.createSalt();
        assertNotEquals(salt1, salt2, "Salts should be random and unique");
    }

    @Test
    void verifyPasswordSuccessTest() {
        assertTrue(PasswordManager.verifyPassword(hashedPassword, password, salt), "Returns true as passwords match");
    }

    @Test
    void verifyPasswordFailTest() {
        assertFalse(PasswordManager.verifyPassword(hashedPassword, "Diff3rentPass!", salt), "Returns false as passwords are different");
    }

    @Test
    void checkPasswordStrengthSuccessTest() {
        assertTrue(PasswordManager.checkPasswordStrength("PasswordIsG00d!"), "Password fulfills all requirements");
        assertTrue(PasswordManager.checkPasswordStrength(password), "Password fulfills all requirements");
    }

    @Test
    void checkPasswordStrengthFailTest() {
        assertFalse(PasswordManager.checkPasswordStrength("passwordisb4d!"), "Password does not contain an uppercase letter");
        assertFalse(PasswordManager.checkPasswordStrength("PASSISB4D!"), "Password does not contain a lowercase letter");
        assertFalse(PasswordManager.checkPasswordStrength("PasswordisBad!"), "Password does not contain a number (0-9)");
        assertFalse(PasswordManager.checkPasswordStrength("passworDIsb4d"), "Password does not contain a special character");
        assertFalse(PasswordManager.checkPasswordStrength("p4Ss1"), "Password is too short (need minimum of 8 characters)");
        assertFalse(PasswordManager.checkPasswordStrength("123456789P123456789p123456789!123"), "Password is too long (exceeds 32 characters)");
    }
}