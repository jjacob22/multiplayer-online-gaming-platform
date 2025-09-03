package profile;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailManager {
    /*
     * EmailValildator class validates that emails follow RFC5322 Standards using a Regex
     */
    // Simple REGEX used for email validation - catches most common cases
    private static final String EMAIL_REGEX =
        "^(?!.*\\.\\.)([a-zA-Z0-9_!#$%&’*+/=?`{|}^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*)"
        + "@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})$";
    
    /*
     * Arguments: 
     * String email - user entered email address
     * 
     * Returns:
     * boolean - true if email entered is a valid email, false otherwise
     */
    public static boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Email configuration (replace with our app credentials - could be set as environment variable instead of hardcode for professionalism :})
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587"; // TLS port
    private static final String SMTP_USER = "seng300.w25.project@gmail.com"; // App Gmail address
    private static final String SMTP_PASSWORD = "djxxmmizwtebbmtb"; //App password

    public static void sendEmail(String toEmail, String subject, String body) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            //System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            throw new Exception("Failed to send email: " + e.getMessage());
        }
    }
}