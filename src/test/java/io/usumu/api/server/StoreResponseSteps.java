package io.usumu.api.server;

import io.cucumber.java.en.Then;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.VariableStorage;
import kong.unirest.JsonNode;

public class StoreResponseSteps {
    private final LastResponseStorage lastResponseStorage;
    private final VariableStorage variableStorage;

    public StoreResponseSteps(
        LastResponseStorage lastResponseStorage,
        VariableStorage variableStorage
    ) {
        this.lastResponseStorage = lastResponseStorage;
        this.variableStorage = variableStorage;
    }

    @Then("^the last call should succeed(?:|,|\\.)$")
    public void success() {
        assert lastResponseStorage.lastResponse != null;
        assert lastResponseStorage.lastResponse.getStatus() >=200;
        assert lastResponseStorage.lastResponse.getStatus() < 300;
    }

    @Then("^the last call should fail(?:|,|\\.)$")
    public void fail() {
        assert lastResponseStorage.lastResponse != null;
        assert lastResponseStorage.lastResponse.getStatus() >=400;
        assert lastResponseStorage.lastResponse.getStatus() < 500;
    }

    @Then("^I store the response field \"([^\"]*)\" in variable \"(?:[^\"]*)\"$")
    public void store(String responseField, String variable) {
        variableStorage.variables.put(variable, lastResponseStorage.lastResponse.getBody().getObject().get(responseField).toString());
    }

    @Then("^the subscription in the last response should have the status \"(?:[^\"]+)\"(|,|\\.)$")
    public void statusCheck(String status) {
        final JsonNode json = lastResponseStorage.lastResponse.getBody();
        assert json.getObject().get("@type").equals("Subscription");
        assert json.getObject().get("status").equals("UNCONFIRMED");
    }
}
