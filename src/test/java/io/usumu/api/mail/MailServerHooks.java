package io.usumu.api.mail;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class MailServerHooks {
    private final InterceptingMailServer interceptingMailServer;

    @Autowired
    public MailServerHooks(MailStorage mailStorage) {
        interceptingMailServer = new InterceptingMailServer(
            mailStorage
        );
    }

    @Before
    public void setUpMailserver() {
        interceptingMailServer.startServer(2525);
    }

    @After
    public void tearDownMailserver() {
        interceptingMailServer.stopServer();
    }
}
