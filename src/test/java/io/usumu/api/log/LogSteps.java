package io.usumu.api.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.log.controller.LogEntryListResponse;
import io.usumu.api.variable.VariableStorage;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.net.URLEncoder;

import static org.junit.Assert.assertTrue;

public class LogSteps {
    private final VariableStorage variableStorage;
    private final ObjectMapper    objectMapper;

    public LogSteps(
        final VariableStorage variableStorage,
        final ObjectMapper objectMapper
    ) {
        this.variableStorage = variableStorage;
        this.objectMapper = objectMapper;
    }

    @Given("^the logs for subscription \"(.*)\" contained a log event of the type \"(.*)\"(?:|,|\\.)$")
    @When("^the logs for subscription \"(.*)\" contain a log event of the type \"(.*)\"(?:|,|\\.)$")
    @Then("^the logs for subscription \"(.*)\" should contain a log event of the type \"(.*)\"(?:|,|\\.)$")
    public void logsShouldContainAnEventType(String subscriptionId, String eventType) throws Throwable {
        final HttpResponse<String> response = Unirest
            .get("http://localhost:8080/subscriptions/" +
                 URLEncoder.encode(variableStorage.resolve(subscriptionId), "UTF-8") +
                 "/logs")
            .accept("application/json")
            .asString();

        final LogEntryListResponse responseObject = objectMapper.readValue(
            response.getBody(),
            LogEntryListResponse.class
        );

        assertTrue(responseObject.logEntries.stream().anyMatch(
            logEntry -> logEntry.entryType.toString().equalsIgnoreCase(eventType)
        ));
    }
}
