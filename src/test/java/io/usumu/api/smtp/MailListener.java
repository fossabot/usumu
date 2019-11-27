package io.usumu.api.smtp;

import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.smtp.helper.SimpleMessageListener;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailListener implements SimpleMessageListener {
    private final MailStorage mailStorage;

    public MailListener(MailStorage mailStorage) {
        this.mailStorage = mailStorage;
    }

    @Override
    public boolean accept(
        String from,
        String to
    ) {
        return true;
    }

    @Override
    public void deliver(
        String from,
        String to,
        InputStream data
    ) throws TooMuchDataException, IOException {
        Properties props = System.getProperties();
        Session session = Session.getInstance(props, null);
        try {
            MimeMessage message = new MimeMessage(session, data);
            mailStorage.deliver(
                new Message(
                    from,
                    to,
                    message
                )
            );
        } catch (MessagingException e) {
            throw new IOException(e);
        }
    }

}
