package io.usumu.api.subscription;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.subscription.controller.SubscriptionCreateRequest;
import io.usumu.api.subscription.controller.SubscriptionVerifyRequest;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.variable.VariableStorage;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SpringBootTest(
    properties = {
        "USUMU_SECRET=niezaichooNoz9gae5Yei4muy3ai2boo",
        "USUMU_INIT_VECTOR=bieng4weengoh0pengeSheGaibi4ve4i",
        "USUMU_S3_ENDPOINT=http://localhost:8001",
        "USUMU_S3_REGION=us-west-2",
        "USUMU_S3_BUCKET=subscriptions"
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

    @Given("^I created a subscriber with the type \"([^\"]*)\" and the value \"([^\"]*)\"$")
    @When("^I create a subscriber with the type \"([^\"]*)\" and the value \"([^\"]*)\"$")
    public void createSubscriber(
        String type,
        String value
    ) {
        final SubscriptionCreateRequest request = new SubscriptionCreateRequest(
            Subscription.Type.fromString(type),
            value
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
    public void checkSubscriberTypeAndValue() {
        assert responseStorage.lastResponse != null;
        assert responseStorage.lastResponse.getBody().getObject().get("@type").equals("Subscription");
    }

    @Given("^the subscription in the last response had the type \"(.*)\" and the value \"(.*)\",$")
    @When("^the subscription in the last response has the type \"(.*)\" and the value \"(.*)\",$")
    @Then("^the subscription in the last response should have the type \"(.*)\" and the value \"(.*)\",$")
    public void dataCheck(String type, String value) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("type").equals(variableStorage.resolve(type));
        assert json.getObject().get("value").equals(variableStorage.resolve(value));
    }

    @Given("^the subscription in the last response had the status \"([^\"]+)\"(?:|,|\\.)$")
    @When("^the subscription in the last response has the status \"([^\"]+)\"(?:|,|\\.)$")
    @Then("^the subscription in the last response should have the status \"([^\"]+)\"(?:|,|\\.)$")
    public void statusCheck(String status) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("status").equals(variableStorage.resolve(status));
    }

    @Given("^I confirmed the subscription \"(.*)\" with the code \"(.*)\"(?:|,|\\.)$")
    @When("^I confirm the subscription \"(.*)\" with the code \"(.*)\"(?:|,|\\.)$")
    public void confirm(String value, String code) {
        final SubscriptionVerifyRequest verifyRequest = new SubscriptionVerifyRequest(
            variableStorage.resolve(code)
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
}
