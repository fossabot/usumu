package io.usumu.api.steps;

import io.usumu.api.VariableStorage;
import io.usumu.api.smtp.MailStorage;
import io.usumu.api.smtp.Message;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class MailServerSteps {
    private final MailStorage mailStorage;
    private final VariableStorage variableStorage;
    private static final Pattern textLinkPattern = Pattern.compile("(http(s|)://([^\\s]*))");
    private static final Pattern htmlLinkPattern = Pattern.compile("href=\"([^\"]*)\"");

    @Autowired
    public MailServerSteps(
        MailStorage mailStorage,
        VariableStorage variableStorage
    ) {
        this.mailStorage = mailStorage;
        this.variableStorage = variableStorage;
    }

    @When("^I empty the mailbox for \"([^\"]*)\"$")
    public void emptyMailbox(String email) {
        mailStorage.empty(email);
    }

    @Given("^I receive an e-mail to \"([^\"]*)\"$")
    @Then("^I should receive an e-mail to \"([^\"]*)\"$")
    public void shouldReceiveEmail(String email) {
        assertTrue(mailStorage.read(email).size() > 0);
    }

    @Given("^I see a link in the mailbox \"([^\"]*)\" to \"([^\"]*)\"$")
    public void seeLink(String email, String patternString) {
        shouldSeeLinkAndStore(email, patternString, null, null);
    }

    @Then("^I should see a link in the mailbox \"([^\"]*)\" to \"([^\"]*)\"$")
    public void shouldSeeLink(String email, String patternString) {
        shouldSeeLinkAndStore(email, patternString, null, null);
    }

    @Given("^I see a link in the mailbox \"([^\"]*)\" to \"([^\"]*)\" and capture \"([^\"]*)\" as \"([^\"]*)\"$")
    public void seeLinkAndStore(String email, String patternString, @Nullable String captureGroup, @Nullable String captureName) {
        shouldSeeLinkAndStore(email, patternString, captureGroup, captureName);
    }

    @Then("^I should see a link in the mailbox \"([^\"]*)\" to \"([^\"]*)\" and capture \"([^\"]*)\" as \"([^\"]*)\"$")
    public void shouldSeeLinkAndStore(String email, String patternString, @Nullable String captureGroup, @Nullable String captureName) {
        Pattern pattern = Pattern.compile(patternString);
        Collection<Message> emails = mailStorage.read(email);
        Optional<Message> message = emails.stream().findAny();
        if (!message.isPresent()) {
            fail("The required message was not found.");
        } else {
            try {
                MimeMessage mimeMessage = message.get().message;
                assertTrue("The required link was not found.", findLink(pattern, mimeMessage, captureGroup, captureName));
            } catch (MessagingException|IOException e) {
                fail("Messaging exception: " + e.getMessage());
            }
        }
    }

    private boolean findLink(Pattern linkPattern, Part message, @Nullable String captureGroup, @Nullable String captureName) throws MessagingException, IOException {
        if (message.getContentType().startsWith("text/html") || message.getContentType().startsWith("text/plain")) {
            //Handle e-mail body
            String content = (String) message.getContent();
            Matcher matcher;
            if (message.getContentType().startsWith("text/html")) {
                matcher = htmlLinkPattern.matcher(content);
            } else if (message.getContentType().startsWith("text/plain")) {
                matcher = textLinkPattern.matcher(content);
            } else {
                return false;
            }

            while (matcher.find()) {
                String match = matcher.group(1);
                if (message.getContentType().startsWith("text/html")) {
                    match = StringEscapeUtils.escapeHtml4(match);
                }
                Matcher linkMatcher = linkPattern.matcher(match);
                if (linkMatcher.matches()) {
                    if (captureName != null && captureGroup != null && !captureName.isEmpty() && !captureGroup.isEmpty()) {
                        variableStorage.variables.put(captureName, linkMatcher.group(captureGroup));
                    }

                    return true;
                }
            }
        } else if (message.getContentType().startsWith("multipart/")) {
            //Handle multipart e-mail
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            for (int partIndex = 0; partIndex < multipart.getCount(); partIndex++) {
                BodyPart part = multipart.getBodyPart(partIndex);
                if (findLink(linkPattern, part, captureGroup, captureName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
