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
    public final boolean ssl;
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
        @Value(("${USUMU_SMTP_SSL:0}"))
        final boolean ssl,
        @Nullable
        @Value(("${USUMU_SMTP_USER:}"))
        final String user,
        @Nullable
        @Value(("${USUMU_SMTP_PASSWORD:}"))
        final String password,
        @Nullable
        @Value(("${USUMU_SMTP_HELO_HOSTNAME:}"))
        final String heloHostname
    ) {
        this.smtpServer = smtpServer;
        this.port = port;
        this.auth = auth;
        this.ssl = ssl;
        this.user = user;
        this.password = password;
        this.heloHostname = heloHostname;
    }

    public Properties getMailProperties() {
        Properties props = new Properties();
        if (smtpServer != null && !smtpServer.isEmpty()) {
            props.put("mail.smtp.host", smtpServer);
        }
        if (port != 25) {
            props.put("mail.smtp.port", port);
        }
        if (auth) {
            props.put("mail.smtp.auth", "true");
        }
        props.put("mail.smtp.starttls.enable", "true");
        if (ssl) {
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        if (heloHostname != null && !heloHostname.isEmpty()) {
            props.put("mail.smtp.localhost", heloHostname);
        }
        return props;
    }

    public Authenticator getAuthenticator() {
        if (auth && user != null && password != null && !user.isEmpty() && !password.isEmpty()) {
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
