package io.usumu.api.subscription;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.subscription.controller.SubscriptionCreateRequest;
import io.usumu.api.subscription.controller.SubscriptionDeleteRequest;
import io.usumu.api.subscription.controller.SubscriptionVerifyRequest;
import io.usumu.api.subscription.entity.SubscriptionMethod;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import io.usumu.api.variable.VariableStorage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

@SpringBootTest(
    properties = {
        "USUMU_SECRET=niezaichooNoz9gae5Yei4muy3ai2boo",
        "USUMU_INIT_VECTOR=bieng4weengoh0pengeSheGaibi4ve4i",
        "USUMU_S3_ENDPOINT=http://localhost:8001",
        "USUMU_S3_REGION=us-west-2",
        "USUMU_S3_BUCKET=subscriptions",
        "USUMU_S3_ACCESS_KEY_ID=asdf",
        "USUMU_S3_SECRET_ACCESS_KEY=asdf",
        "USUMU_SMTP_PORT=2525"
    },
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class SubscriptionSteps {
    private final LastResponseStorage responseStorage;
    private final VariableStorage variableStorage;

    public SubscriptionSteps(final LastResponseStorage responseStorage, final VariableStorage variableStorage) {
        this.responseStorage = responseStorage;
        this.variableStorage = variableStorage;
    }

    @Given("^I uploaded a template \"([^\"]+)\" with the content(?:|,|\\.|:)$")
    public void createTemplate(
        String templateName,
        String body
    ) {
        final HttpResponse response = Unirest
            .put("http://127.0.0.1:8080/templates/" + templateName.replaceAll("^/", ""))
            .header("Content-Type", "text/plain")
            .accept("text/plain")
            .body(body)
            .asEmpty();

        assertEquals(200, response.getStatus());
    }

    @Given("^I created a subscriber with the method \"([^\"]*)\" and the value \"([^\"]*)\"(?:|,|\\.)$")
    @When("^I create a subscriber with the method \"([^\"]*)\" and the value \"([^\"]*)\"(?:|,|\\.)$")
    public void createSubscriber(
        String type,
        String value
    ) {
        final SubscriptionCreateRequest request = new SubscriptionCreateRequest(
            SubscriptionMethod.fromString(type),
            value,
            false,
            null,
            null
        );

        responseStorage.lastResponse = Unirest
            .post("http://127.0.0.1:8080/subscriptions")
            .header("Content-Type", "application/json")
            .accept("application/json")
            .body(request)
            .asJson();
    }


    @Given("^I imported a subscriber with the method \"([^\"]*)\" and the value \"([^\"]*)\" with status \"([^\"]*)\"(?:|,|\\.)$")
    @When("^I import a subscriber with the method \"([^\"]*)\" and the value \"([^\"]*)\" with status \"([^\"]*)\"(?:|,|\\.)$")
    public void importSubscriber(
        String type,
        String value,
        String status
    ) {
        final SubscriptionCreateRequest request = new SubscriptionCreateRequest(
            SubscriptionMethod.fromString(type),
            value,
            true,
            SubscriptionStatus.fromString(status),
            null
        );

        responseStorage.lastResponse = Unirest
            .post("http://127.0.0.1:8080/subscriptions")
            .header("Content-Type", "application/json")
            .accept("application/json")
            .body(request)
            .asJson();
    }

    @Given("^I fetched the subscription \"(.*)\"(?:|,|\\.)$")
    @When("^I fetch the subscription \"(.*)\"(?:|,|\\.)$")
    public void getSubscriber(
        String value
    ) {
        try {
            responseStorage.lastResponse = Unirest
                .get("http://127.0.0.1:8080/subscriptions/" + URLEncoder.encode(variableStorage.resolve(value), "UTF-8"))
                .accept("application/json")
                .asJson();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Given("^the last call returned a subscription(?:|,|\\.)$")
    @When("^the last call returns a subscription(?:|,|\\.)$")
    @Then("^the last call should return a subscription(?:|,|\\.)$")
    public void checkLastReturnIsSubscription() {
        assert responseStorage.lastResponse != null;
        assert responseStorage.lastResponse.getBody().getObject().get("@type").equals("Subscription");
    }

    @Given("^the subscription in the last response had the method \"(.*)\" and the value \"(.*)\",$")
    @When("^the subscription in the last response has the method \"(.*)\" and the value \"(.*)\",$")
    @Then("^the subscription in the last response should have the method \"(.*)\" and the value \"(.*)\",$")
    public void dataCheck(String type, String value) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("method").equals(variableStorage.resolve(type));
        assert json.getObject().get("value").equals(variableStorage.resolve(value));
    }

    @Then("^the subscription in the last response should not have a value.$")
    public void noValue() {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("value") == null;
    }

    @Given("^the subscription in the last response had the status \"([^\"]+)\"(?:|,|\\.)$")
    @When("^the subscription in the last response has the status \"([^\"]+)\"(?:|,|\\.)$")
    @Then("^the subscription in the last response should have the status \"([^\"]+)\"(?:|,|\\.)$")
    public void statusCheck(String status) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assertEquals("Unexpected subscription status. Wanted " + variableStorage.resolve(status) + ", found " + json.getObject().get("status"), json.getObject().get("status"), variableStorage.resolve(status));
    }

    @Given("^I confirmed the subscription \"(.*)\" with the code \"(.*)\"(?:|,|\\.)$")
    @When("^I confirm the subscription \"(.*)\" with the code \"(.*)\"(?:|,|\\.)$")
    public void confirm(String value, String code) {
        final SubscriptionVerifyRequest verifyRequest = new SubscriptionVerifyRequest(
            variableStorage.resolve(code),
            null
        );
        try {
            responseStorage.lastResponse = Unirest
                .patch("http://localhost:8080/subscriptions/" + URLEncoder.encode(variableStorage.resolve(value), "UTF-8"))
                .header("Content-Type", "application/json")
                .accept("application/json")
                .body(verifyRequest)
                .asJson();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



    @Given("^I deleted the subscription \"(.*)\"(?:|,|\\.)$")
    @When("^I delete the subscription \"(.*)\"(?:|,|\\.)$")
    public void delete(String value) {
        try {
            responseStorage.lastResponse = Unirest
                .delete("http://localhost:8080/subscriptions/" + URLEncoder.encode(variableStorage.resolve(value), "UTF-8"))
                .header("Content-Type", "application/json")
                .accept("application/json")
                .body(new SubscriptionDeleteRequest(null))
                .asJson();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Given("^I downloaded the logs for subscription \"(.*)\"(?:|,|\\.)$")
    @When("^I download the logs for subscription \"(.*)\"(?:|,|\\.)$")
    public void downloadLogs(String subscriptionId) {
        try {
            responseStorage.lastResponse = Unirest
                    .get("http://localhost:8080/subscriptions/" + URLEncoder.encode(variableStorage.resolve(subscriptionId), "UTF-8") + "/logs")
                    .accept("application/json")
                    .asJson();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
