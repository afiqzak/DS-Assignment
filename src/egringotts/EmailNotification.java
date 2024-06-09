package egringotts;
/**
 *
 * @author wenhu
 */
//using JavaMail API
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotification {

    private final String from;
    private final String username;
    private final String appSpecificPassword;
    private final Properties props;

    public EmailNotification() {
        this.from = "egringotts50@gmail.com";            //this is a real email created
        this.username = "egringotts50@gmail.com";       //this is a real email created
        this.appSpecificPassword = "svdgororcqecftym"; //this is a real password
        this.props = new Properties();

        // Configure properties for the mail session
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    public void sendSignUpEmail(String to, String username) throws MessagingException {
        String subject = "Welcome to Gringotts Glimpse";
        String body = "Dear " + username + ",\n\nYour account has been successfully created.\n\nBest Regards,\neGringotts";
        sendEmail(to, subject, body);
    }

    public void sendSignInEmail(String to, String username) throws MessagingException {
        String subject = "Sign In Notification";
        String body = "Dear " + username + ",\n\nYou have successfully signed in to your account.\n\nBest Regards,\neGringotts";
        sendEmail(to, subject, body);
    }

    public void sendTransactionEmail(String to, String username, String transactionDetails) throws MessagingException {
        String subject = "Transaction Notification";
        String body = "Dear " + username + ",\n\nA transaction has been made on your account:\n\n" + transactionDetails + "\n\nBest Regards,\neGringotts";
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appSpecificPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new MessagingException("Failed to send email", e);
        }
    }
}