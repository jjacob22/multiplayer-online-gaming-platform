package profile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailManagerTest {
    String validEmail1,validEmail2, validEmail3, validEmail4, validEmail5;
    String badEmail1, badEmail2, badEmail3, badEmail4, badEmail5, badEmail6,
            badEmail7, badEmail8, badEmail9;
            
    @BeforeEach
    void setUp() {
        validEmail1 = "valid@gmail.com";
        validEmail2 = "valid.2@gmail.good.com";
        validEmail3 = "st?ll+wor!ks@hotmail.com";
        validEmail4 = "valid@sub.domain.com";
        validEmail5 = "s@s.ca";

        badEmail1 = "..does..not..work@gmail.com";
        badEmail2 = "notvalid@gmail.toolonghere";
        badEmail3 = "invaliddomain@!?^&?.ca";
        badEmail4 = "@outlook.ca";
        badEmail5 = "<>bad<>@outlook.ca";
        badEmail6 = "noatsigninemail.ca.com";
        badEmail7 = "bad@@gma.com";
        badEmail8 = "bad@.local";
        badEmail9 = "bad@local.";
    }

    @AfterEach
    void tearDown() {
        validEmail1 = null;
        validEmail2 = null;
        validEmail3 = null;
        validEmail4 = null;
        validEmail5 = null;

        badEmail1 = null;
        badEmail2 = null;
        badEmail3 = null;
        badEmail4 = null;
        badEmail5 = null;
        badEmail6 = null;
        badEmail7 = null;
        badEmail8 = null;
        badEmail9 = null;
    }

    @Test
    void validateEmailSuccessTest() {
        assertTrue(EmailManager.validateEmail(validEmail1), "Valid structure and characters used");
        assertTrue(EmailManager.validateEmail(validEmail2), "Emails allowed groups separated by single '.'");
        assertTrue(EmailManager.validateEmail(validEmail3), "Allowed certain special characters in local part");
        assertTrue(EmailManager.validateEmail(validEmail4), "Sub domains allowed after '@'");
        assertTrue(EmailManager.validateEmail(validEmail5), "Short emails are allowed");
    }

    @Test
    void validateEmailFailTest() {
        assertFalse(EmailManager.validateEmail(badEmail1), "Emails cannot contain consecutive '.'s");
        assertFalse(EmailManager.validateEmail(badEmail2), "TLD cannot be longer than 6 characters");
        assertFalse(EmailManager.validateEmail(badEmail3), "Domain contains invalid characters");
        assertFalse(EmailManager.validateEmail(badEmail4), "Local part cannot be empty");
        assertFalse(EmailManager.validateEmail(badEmail5), "Invalid characters in local part");
        assertFalse(EmailManager.validateEmail(badEmail6), "No '@' in email");
        assertFalse(EmailManager.validateEmail(badEmail7), "More than one '@' in email");
        assertFalse(EmailManager.validateEmail(badEmail8), "Domain cannot start with '.'");
        assertFalse(EmailManager.validateEmail(badEmail9), "Domain cannot end in '.'");
    }

    @Test
    void sendEmail() {
    }
}