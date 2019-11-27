package io.usumu.api.steps;

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

    @Then("^I store the response field \"([^\"]*)\" in variable \"([^\"]*)\"$")
    public void store(String responseField, String variable) {
        variableStorage.variables.put(variable, lastResponseStorage.lastResponse.getBody().getObject().get(responseField).toString());
    }
}
