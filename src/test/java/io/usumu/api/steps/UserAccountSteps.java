package io.usumu.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.usumu.api.LastResponseStorage;
import io.usumu.api.VariableStorage;
import io.usumu.api.token.api.AccessTokenCreateRequest;
import io.usumu.api.user.api.UserAccountCreateRequest;
import io.usumu.api.user.api.UserAccountModifyRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class UserAccountSteps extends ServerSteps {
    private final LastResponseStorage lastResponseStorage;
    private final VariableStorage     variableStorage;

    @Autowired
    public UserAccountSteps(LastResponseStorage lastResponseStorage, VariableStorage variableStorage) {
        this.lastResponseStorage = lastResponseStorage;
        this.variableStorage = variableStorage;
    }

    private ObjectWriter getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        return mapper.writer().withDefaultPrettyPrinter();
    }

    @When("^I create a user account with the e-mail \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void createUserAccount(String email, String password) throws Throwable {
        this.lastResponseStorage.lastResponse = Unirest.post("http://localhost:8080/users")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .charset(StandardCharsets.UTF_8)
            .body(getObjectMapper().writeValueAsString(
                new UserAccountCreateRequest(email, password, null)
            ))
            .asJson();
    }

    @Then("^the user creation should be successful$")
    public void userCreationShouldBeSuccessful() throws Throwable {
        assertNotNull(this.lastResponseStorage.lastResponse);
        assertEquals(200, this.lastResponseStorage.lastResponse.getStatus());
    }

    @When("^the user in the last response should have the e-mail \"([^\"]*)\"$")
    public void userShouldHaveEmail(String email) {
        assertNotNull(this.lastResponseStorage.lastResponse);
        assertTrue(this.lastResponseStorage.lastResponse.getBody().getObject().has("email"));
        assertEquals(email, this.lastResponseStorage.lastResponse.getBody().getObject().get("email").toString());
    }

    @When("^the user in the last response should be unverified$")
    public void userShouldBeUnverified() {
        assertNotNull(this.lastResponseStorage.lastResponse);
        assertTrue(this.lastResponseStorage.lastResponse.getBody().getObject().has("isVerified"));
        assertFalse((Boolean) this.lastResponseStorage.lastResponse.getBody().getObject().get("isVerified"));
    }

    @Given("^the user in the last response should be verified$")
    public void theUserShouldBeVerified() throws Throwable {
        assertNotNull(this.lastResponseStorage.lastResponse);
        assertTrue(this.lastResponseStorage.lastResponse.getBody().getObject().has("isVerified"));
        assertTrue((Boolean) this.lastResponseStorage.lastResponse.getBody().getObject().get("isVerified"));
    }

    @Then("^I verify my user with the verification code from a previous step \"([^\"]*)\"$")
    public void verifyUser(
        String verificationCodeKey
    ) throws Throwable {
        if (!variableStorage.variables.containsKey(verificationCodeKey)) {
            fail();
        }
        String verificationCode = variableStorage.variables.get(verificationCodeKey);
        this.lastResponseStorage.lastResponse = Unirest.patch("http://localhost:8080/verifications/" + URLEncoder.encode(verificationCode, StandardCharsets.UTF_8.toString()))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .charset(StandardCharsets.UTF_8)
            .body("{}")
            .asJson();
    }

    @Then("^the last call should succeed.$")
    public void lastCallShouldSucceed() {
        if (this.lastResponseStorage.lastResponse.getStatus() < 200 || this.lastResponseStorage.lastResponse.getStatus() > 299) {
            fail("The last call should have succeeded, but returned with code " + this.lastResponseStorage.lastResponse.getStatus());
        }
    }

    @Then("^the last call should fail.$")
    public void lastCallShouldFail() {
        if (this.lastResponseStorage.lastResponse.getStatus() < 400 || this.lastResponseStorage.lastResponse.getStatus() > 499) {
            fail("The last call should have failed, but returned with code " + this.lastResponseStorage.lastResponse.getStatus());
        }
    }

    @Then("^the last call should fail with code ([0-9]+).$")
    public void lastCallShouldFailWithCode(int code) {
        if (this.lastResponseStorage.lastResponse.getStatus() != code) {
            fail("The last call should have failed with code " + code + ", but returned with code " + this.lastResponseStorage.lastResponse.getStatus());
        }
    }

    @When("^I log in with the email \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void login(String email, String password) throws Throwable {
        this.lastResponseStorage.lastResponse = Unirest
            .post("http://localhost:8080/tokens")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .charset(StandardCharsets.UTF_8)
            .body(getObjectMapper().writeValueAsString(
                new AccessTokenCreateRequest(
                    email,
                    password
                )))
            .asJson();
    }

    @And("^I fetch user account with ID \"([^\"]*)\"$")
    public void fetchUserAccount(String userAccountId) {
        this.lastResponseStorage.lastResponse = Unirest
            .get("http://localhost:8080/users/" + userAccountId)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .asJson();
    }

    @And("^I fetch user account with ID variable \"([^\"]*)\"$")
    public void fetchUserAccountWithVariable(String userAccountIdVariable) {
        fetchUserAccount(variableStorage.variables.get(userAccountIdVariable));
    }

    @And("^I fetch user account with ID \"([^\"]*)\" with access token variable \"([^\"]*)\"$")
    public void fetchUserAccount(String userAccountId, String accessTokenVariable) {
        this.lastResponseStorage.lastResponse = Unirest
            .get("http://localhost:8080/users/" + userAccountId)
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + variableStorage.variables.get(accessTokenVariable))
            .asJson();
    }

    @And("^I fetch user account with ID variable \"([^\"]*)\" with access token variable \"([^\"]*)\"$")
    public void fetchUserAccountVariable(String userAccountIdVariable, String accessTokenVariable) {
        fetchUserAccount(variableStorage.variables.get(userAccountIdVariable), accessTokenVariable);
    }

    @When("^I change user account email with ID variable \"([^\"]*)\" to \"([^\"]*)\"$")
    public void changeEmail(String userAccountIdVariable, String newEmail) throws Throwable {
        this.lastResponseStorage.lastResponse = Unirest
            .patch("http://localhost:8080/users/" + variableStorage.variables.get(userAccountIdVariable))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(
                getObjectMapper().writeValueAsString(
                    new UserAccountModifyRequest(
                        newEmail,
                        null
                    )
                )
            )
            .asJson();
    }

    @When(
        "^I change user account email with ID variable \"([^\"]*)\" to \"([^\"]*)\" with access token variable \"([^\"]*)\"$")
    public void iChangeUserAccountEmailWithIDVariableToWithAccessTokenVariable(String userAccountIdVariable, String newEmail, String accessTokenIdVariable)
        throws Throwable {
        this.lastResponseStorage.lastResponse = Unirest
            .patch("http://localhost:8080/users/" + variableStorage.variables.get(userAccountIdVariable))
            .header("Authorization", "Bearer " + variableStorage.variables.get(accessTokenIdVariable))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .body(
                getObjectMapper().writeValueAsString(
                    new UserAccountModifyRequest(
                        newEmail,
                        null
                    )
                )
            )
            .asJson();
    }
}
