package io.usumu.api.variable;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VariableSteps {
    private final LastResponseStorage responseStorage;
    private final VariableStorage variableStorage;

    @Autowired
    public VariableSteps(final LastResponseStorage responseStorage, final VariableStorage variableStorage) {
        this.responseStorage = responseStorage;
        this.variableStorage = variableStorage;
    }

    @Given("^I stored the last response field \"(.*)\" in the variable \"(.*)\"(?:|,|\\.)$")
    @When("^I store the last response field \"(.*)\" in the variable \"(.*)\"(?:|,|\\.)$")
    public void storeVariable(String field, String variable) {
        assertNotNull("No previous response.", responseStorage.lastResponse);
        assertNotNull("Response body is null.", responseStorage.lastResponse.getBody());
        assertNotNull("Response body is null.", responseStorage.lastResponse.getBody().getObject());
        assertTrue("Response does not have a field " + field, responseStorage.lastResponse.getBody().getObject().has(field));

        variableStorage.store(variable, (String)responseStorage.lastResponse.getBody().getObject().get(field));
    }
}
