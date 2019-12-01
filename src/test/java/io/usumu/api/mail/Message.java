package io.usumu.api.mail;

import javax.mail.internet.MimeMessage;
import java.util.UUID;

public class Message {
    public final String id;
    public final String from;
    public final String to;
    public final MimeMessage message;

    public Message(
        String from,
        String to,
        MimeMessage message
    ) {
        this.id = UUID.randomUUID().toString();
        this.from = from;
        this.to = to;
        this.message = message;
    }
}
