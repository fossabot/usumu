package io.usumu.api.mail;

import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.template.TemplateEngine;
import io.usumu.api.template.TemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailSender {
    private final TemplateEngine templateEngine;
    private final MailConfiguration mailConfiguration;

    @Autowired
    public MailSender(
        final TemplateEngine templateEngine,
        final MailConfiguration mailConfiguration
    ) {
        this.templateEngine = templateEngine;
        this.mailConfiguration = mailConfiguration;
    }

    public void send(String template, Subscription subscription, Map<String, Object> data)
        throws TemplateProvider.TemplateNotFound {
        Map<String, Object> finalData = new HashMap<>(data);
        finalData.put("subscription", subscription);
        String bodyText = null;
        try {
            bodyText = templateEngine.render(template + "/body.text", finalData);
        } catch (TemplateProvider.TemplateNotFound e) {
        }
        String bodyHtml = null;
        try {
            bodyHtml = templateEngine.render(template + "/body.html", finalData);
        } catch (TemplateProvider.TemplateNotFound e) {
        }
        final String subject = templateEngine.render(template + "/subject.txt", finalData);
        String fromName = null;
        try {
            fromName = templateEngine.render(template + "/fromName.txt", finalData);
        } catch (TemplateProvider.TemplateNotFound e) {
        }
        final String fromEmail = templateEngine.render(template + "/fromEmail.txt", finalData);
        String toName = null;
        try {
            toName = templateEngine.render(template + "/toName.txt", finalData);
        } catch (TemplateProvider.TemplateNotFound e) {
        }
        final String toEmail = templateEngine.render(template + "/toEmail.txt", finalData);

        System.out.println("SimpleEmail Start");

        Session session = Session.getInstance(
            mailConfiguration.getMailProperties(),
            mailConfiguration.getAuthenticator()
        );

        try {
            Message   mimeMessage   = new MimeMessage(session);
            Multipart multiPart = new MimeMultipart("alternative");
            if (bodyHtml != null) {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlPart, "text/html; charset=utf-8");
                multiPart.addBodyPart(htmlPart);
            }
            if (bodyText != null) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(bodyText, "utf-8");
                multiPart.addBodyPart(textPart);
            }
            mimeMessage.setContent(multiPart);

            if (fromName != null) {
                mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
            } else {
                mimeMessage.setFrom(new InternetAddress(fromEmail));
            }

            if (toName != null) {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toName));
            } else {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            }
            mimeMessage.setSubject(subject);

            Transport.send(mimeMessage);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
