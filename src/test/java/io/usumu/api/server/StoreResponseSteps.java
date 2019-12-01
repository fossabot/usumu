package io.usumu.api.server;

import io.cucumber.java.en.Then;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.VariableStorage;

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

    @Then("^the last call should succeed.$")
    public void success() {
        assert lastResponseStorage.lastResponse != null;
        assert lastResponseStorage.lastResponse.getStatus() >=200;
        assert lastResponseStorage.lastResponse.getStatus() < 300;
    }

    @Then("^I store the response field \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void store(String responseField, String variable) {
        variableStorage.variables.put(variable, lastResponseStorage.lastResponse.getBody().getObject().get(responseField).toString());
    }
}
