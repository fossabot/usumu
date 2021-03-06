package io.usumu.api.mail;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.variable.VariableStorage;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

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

    @Given("^I emptied the mailbox \"([^\"]*)\"(?:|,|\\.)$")
    @When("^I empty the mailbox \"([^\"]*)\"(?:|,|\\.)$")
    public void emptyMailbox(String email) {
        mailStorage.empty(email);
    }

    @Given("^I received an e-mail to \"([^\"]*)\"(?:|,|\\.)$")
    @When("^I receive an e-mail to \"([^\"]*)\"(?:|,|\\.)$")
    @Then("^I should receive an e-mail to \"([^\"]*)\"(?:|,|\\.)$")
    public void shouldReceiveEmail(String email) {
        assertTrue("No e-mail received to " + email, mailStorage.read(email).size() > 0);
    }

    @Given("^the last e-mail to \"([^\"]*)\" contained a link to \"([^\"]*)\"(?:|,|\\.)$")
    @When("^the last e-mail to \"([^\"]*)\" contains a link to \"([^\"]*)\"(?:|,|\\.)$")
    @Then("^the last e-mail to \"([^\"]*)\" should contain a link to \"([^\"]*)\"(?:|,|\\.)$")
    public void shouldContainLink(String email, String linkPatternString) throws Throwable {
        Pattern linkPattern = Pattern.compile(linkPatternString);
        assertTrue("No e-mail received to " + email, mailStorage.read(email).size() > 0);
        final Message lastEmail = mailStorage.read(email)
            .get(mailStorage.read(email)
                     .size() - 1);
        assertTrue(
            "No link found in the last e-mail to " + email + " matching " + linkPattern.toString(),
            findLink(linkPattern, lastEmail.message, null, null)
        );
    }

    @Given("^I extracted the link to \"([^\"]+)\" from the last e-mail to \"([^\"]+)\" into the variable \"([^\"]+)\"(?:|,|\\.)$")
    @When("^I extract the link to \"([^\"]+)\" from the last e-mail to \"([^\"]+)\" into the variable \"([^\"]+)\"(?:|,|\\.)$")
    public void extractEmailVariable(String linkPatternString, String email, String variable) throws IOException, MessagingException {
        Pattern linkPattern = Pattern.compile(linkPatternString);
        assertTrue("No e-mail received to " + email, mailStorage.read(email).size() > 0);
        final Message lastEmail = mailStorage.read(email)
                .get(mailStorage.read(email)
                        .size() - 1);
        assertTrue("No link found", findLink(linkPattern, lastEmail.message, null, variable));
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
                    if (captureName != null && !captureName.isEmpty()) {
                        if (captureGroup == null || captureGroup.isEmpty()) {
                            variableStorage.store(captureName, linkMatcher.group(0));
                        } else {
                            variableStorage.store(captureName, linkMatcher.group(captureGroup));
                        }
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
