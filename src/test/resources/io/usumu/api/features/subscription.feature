Feature: Subscriber management
  Background:
    Given I uploaded a template "verification/body.html" with the content
    """
    <a href="http://example.com/verification/{{ verificationCode }}">Verify</a>
    """
    And I uploaded a template "verification/subject.txt" with the content
    """
    Verify your subscription
    """
    And I uploaded a template "verification/fromEmail.txt" with the content
    """
    newsletter@example.com
    """
    And I uploaded a template "verification/fromName.txt" with the content
    """
    Newsletter
    """
    And I uploaded a template "verification/toEmail.txt" with the content
    """
    {{ email }}
    """


  Scenario: Creating a new subscription with a valid e-mail address.
    When I create a subscriber with the method "EMAIL" and the value "test1@example.com"
    Then the last call should succeed.

  Scenario: Creating a new subscription with an invalid e-mail.
    When I create a subscriber with the method "EMAIL" and the value "test2 @example.com"
    Then the last call should fail.

  Scenario: Creating a new subscriber should return the subscription object.
    When I create a subscriber with the method "EMAIL" and the value "test3@example.com"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should not have a value,
    And the subscription in the last response should have the status "UNCONFIRMED".

  Scenario: Fetching a created subscription should return a subscription object.
    Given I created a subscriber with the method "EMAIL" and the value "test4@example.com"
    When I fetch the subscription "test4@example.com",
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "UNCONFIRMED".

  Scenario: Creating a subscription should send a verification e-mail.
    When I create a subscriber with the method "EMAIL" and the value "test5@example.com"
    Then I should receive an e-mail to "test5@example.com",
    And the last e-mail to "test5@example.com" should contain a link to "http://example.com/verification/([^/]*)".

  Scenario: Confirming a subscription should yield in a subscription being confirmed.
    Given I created a subscriber with the method "EMAIL" and the value "test6@example.com",
    And I stored the last response field "id" in the variable "subscriptionId",
    And I extract the link to "http://example.com/verification/([^/]*)" from the last e-mail to "test5@example.com" into the variable "verificationLink",
    And I extract the regexp "http://example.com/verification/([^/]*)" group "1" the variable "verificationLink" into the variable "verificationCode",
    When I confirm the subscription "${subscriptionId}" with the code "${verificationCode}",
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "CONFIRMED".

  Scenario: Deleting a subscription should result in a deleted subscriber returned.
    Given I imported a subscriber with the method "EMAIL" and the value "test7@example.com" with status "CONFIRMED",
    And I stored the last response field "id" in the variable "subscriptionId",
    When I delete the subscription "${subscriptionId}"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "UNSUBSCRIBED",
    And the subscription in the last response should not have a value.

  Scenario: Deleting a subscription should result in the subscription actually being deleted
    Given I imported a subscriber with the method "EMAIL" and the value "test8@example.com" with status "CONFIRMED",
    And I stored the last response field "id" in the variable "subscriptionId",
    And I delete the subscription "${subscriptionId}"
    And the last call succeeded,
    When I fetch the subscription "test8@example.com"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "UNSUBSCRIBED",
    And the subscription in the last response should not have a value.
