package io.usumu.api.server;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.variable.VariableStorage;
import kong.unirest.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StoreResponseSteps {
    private final LastResponseStorage lastResponseStorage;
    private final VariableStorage variableStorage;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public StoreResponseSteps(
        LastResponseStorage lastResponseStorage,
        VariableStorage variableStorage
    ) {
        this.lastResponseStorage = lastResponseStorage;
        this.variableStorage = variableStorage;
    }

    @Given("^the last call succeeded(?:|,|\\.)$")
    @When("^the last call succeeds(?:|,|\\.)$")
    @Then("^the last call should succeed(?:|,|\\.)$")
    public void success() {
        try {
            assert lastResponseStorage.lastResponse != null;
            assert lastResponseStorage.lastResponse.getStatus() >= 200;
            assert lastResponseStorage.lastResponse.getStatus() < 300;
        } catch (AssertionError e) {
            StringBuilder response = new StringBuilder("HTTP/1.1 " + lastResponseStorage.lastResponse.getStatus() + " " + lastResponseStorage.lastResponse.getStatusText() + "\n");
            for (Header header : lastResponseStorage.lastResponse.getHeaders().all()) {
                response.append(header.getName()).append(": ").append(header.getValue()).append("\n");
            }
            response.append("\n");
            response.append(lastResponseStorage.lastResponse.getBody().toString());
            logger.warn(response.toString());
            throw e;
        }
    }

    @Given("^the last call should failed(?:|,|\\.)$")
    @When("^the last call fails(?:|,|\\.)$")
    @Then("^the last call should fail(?:|,|\\.)$")
    public void fail() {
        assert lastResponseStorage.lastResponse != null;
        assert lastResponseStorage.lastResponse.getStatus() >=400;
        assert lastResponseStorage.lastResponse.getStatus() < 500;
    }

    @Given("^I stored the response field \"([^\"]*)\" in variable \"(?:[^\"]*)\"(?:|,|\\.)$")
    @When("^I store the response field \"([^\"]*)\" in variable \"(?:[^\"]*)\"(?:|,|\\.)$")
    public void store(String responseField, String variable) {
        variableStorage.store(
            variable,
            lastResponseStorage.lastResponse.getBody().getObject().get(responseField).toString()
        );
    }
}
