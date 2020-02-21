package io.usumu.api.template;

import io.cucumber.java.en.Given;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.variable.VariableStorage;
import kong.unirest.Unirest;

public class TemplateSteps {
    private final LastResponseStorage responseStorage;
    private final VariableStorage     variableStorage;

    public TemplateSteps(final LastResponseStorage responseStorage, final VariableStorage variableStorage) {
        this.responseStorage = responseStorage;
        this.variableStorage = variableStorage;
    }

    @Given("^I uploaded a template \"([^\"]+)\" with the content(?:|,|\\.|:)$")
    public void createSubscriber(
        String templateName,
        String body
    ) {
        responseStorage.lastResponse = Unirest
            .post("http://127.0.0.1:8080/templates/" + templateName.replaceAll("^/", ""))
            .header("Content-Type", "text/plain")
            .accept("application/json")
            .body(body)
            .asJson();
    }
}
