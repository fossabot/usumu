package io.usumu.api.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

@Service
public class MailConfiguration {
    public final String smtpServer;
    public final int port;
    public final boolean auth;
    public final boolean startTls;
    public final String user;
    public final String password;
    public final String heloHostname;

    @Autowired
    public MailConfiguration(
        @Value(("${USUMU_SMTP_SERVER:localhost}"))
        final String smtpServer,
        @Value(("${USUMU_SMTP_PORT:25}"))
        final int port,
        @Value(("${USUMU_SMTP_AUTH:0}"))
        final boolean auth,
        @Value(("${USUMU_SMTP_STARTTLS:0}"))
        final boolean startTls,
        @Nullable
        @Value(("${USUMU_SMTP_USER:}"))
        final String user,
        @Nullable
        @Value(("${USUMU_SMTP_PASSWORD:}"))
        final String password,
        @Nullable
        @Value(("${USUMU_SMTP_HELO_HOSTNAME:localhost}"))
        final String heloHostname
    ) {
        this.smtpServer = smtpServer;
        this.port = port;
        this.auth = auth;
        this.startTls = startTls;
        this.user = user;
        this.password = password;
        this.heloHostname = heloHostname;
    }

    public Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTls);
        props.put("mail.smtp.localhost", heloHostname);
        return props;
    }

    public Authenticator getAuthenticator() {
        if (auth && user != null && password != null) {
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        user, password
                    );
                }
            };
        } else {
            return null;
        }
    }
}
