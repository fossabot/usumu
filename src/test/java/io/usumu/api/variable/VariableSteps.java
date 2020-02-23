package io.usumu.api.variable;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Given("^I extracted the regexp \"(.*)\" group \"(.*)\" the variable \"(.*)\" into the variable \"(.*)\"(?:|,|\\.)$")
    @When("^I extract the regexp \"(.*)\" group \"(.*)\" the variable \"(.*)\" into the variable \"(.*)\"(?:|,|\\.)$")
    public void extractVariable(String expression, String group, String sourceVariable, String targetVariable) {
        assertTrue("Source variable not found: " + sourceVariable, variableStorage.has(sourceVariable));
        Pattern pattern = Pattern.compile(expression);
        //noinspection ConstantConditions
        Matcher match = pattern.matcher(variableStorage.get(sourceVariable));
        assertTrue("No match found for expression " + expression, match.matches());
        if (group.matches("^[0-9]+$")) {
            variableStorage.store(targetVariable, match.group(Integer.parseInt(group)));
        } else {
            variableStorage.store(targetVariable, match.group(group));
        }
    }
}
