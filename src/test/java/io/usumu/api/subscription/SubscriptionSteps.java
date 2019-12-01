package io.usumu.api.subscription;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.subscription.controller.SubscriptionCreateRequest;
import io.usumu.api.subscription.entity.Subscription;
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

    public SubscriptionSteps(final LastResponseStorage responseStorage) {
        this.responseStorage = responseStorage;
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
                .get("http://127.0.0.1:8080/subscriptions/" + URLEncoder.encode(value, "UTF-8"))
                .accept("application/json")
                .asJson();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("^the last call should return a subscription(?:|,|\\.)$")
    public void checkSubscriberTypeAndValue() {
        assert responseStorage.lastResponse != null;
        assert responseStorage.lastResponse.getBody().getObject().get("@type").equals("Subscription");
    }

    @Then("^the subscription in the last response should have the type \"(.*)\" and the value \"(.*)\",$")
    public void dataCheck(String type, String value) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("type").equals(type);
        assert json.getObject().get("value").equals(value);
    }

    @Then("^the subscription in the last response should have the status \"(?:[^\"]+)\"(|,|\\.)$")
    public void statusCheck(String status) {
        final JsonNode json = responseStorage.lastResponse.getBody();
        assert json.getObject().get("status").equals("UNCONFIRMED");
    }

}
