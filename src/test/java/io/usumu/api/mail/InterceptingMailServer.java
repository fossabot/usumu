package io.usumu.api.mail;

import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class InterceptingMailServer {
    private final SMTPServer smtpServer;

    public InterceptingMailServer(MailStorage mailStorage) {
        MailListener listener = new MailListener(mailStorage);
        smtpServer = new SMTPServer(
            new SimpleMessageListenerAdapter(listener),
            new AuthHandlerFactory()
        );
    }

    public void startServer(int port) {
        try {
            smtpServer.setBindAddress(InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        smtpServer.setPort(port);
        smtpServer.start();

        int tries = 0;
        while (!isRunning(port)) {
            tries++;
            if (tries > 30) {
                smtpServer.stop();
                throw new RuntimeException("Failed to start SMTP server in 3 seconds");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isRunning(int port) {
        InetAddress address;
        try {
            address = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try (Socket s = new Socket(address, port)) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void stopServer() {
        if (smtpServer.isRunning()) {
            smtpServer.stop();
        }
    }
}
