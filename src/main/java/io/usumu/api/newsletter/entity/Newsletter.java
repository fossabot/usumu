package io.usumu.api.newsletter.entity;

public class Newsletter {
    public final String id;
    public final Status status;
    public final String subject;
    public final String textTemplate;
    public final String htmlTemplate;
    public final String smsTemplate;

    public Newsletter(
            String id,
            Status status,
            String subject,
            String textTemplate,
            String htmlTemplate,
            String smsTemplate) {
        this.id = id;
        this.status = status;
        this.subject = subject;
        this.textTemplate = textTemplate;
        this.htmlTemplate = htmlTemplate;
        this.smsTemplate = smsTemplate;
    }

    public enum Status {
        NEW,
        SENDING,
        PAUSED,
        SENT
    }
}
