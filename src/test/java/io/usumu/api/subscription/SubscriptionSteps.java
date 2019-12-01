package io.usumu.api.subscription;

import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.subscription.controller.SubscriptionCreateRequest;
import io.usumu.api.subscription.entity.Subscription;
import kong.unirest.Unirest;
import org.springframework.boot.test.context.SpringBootTest;

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
}
