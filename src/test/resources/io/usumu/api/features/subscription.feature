Feature: Subscriber management
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
    And the subscription in the last response should have the method "EMAIL" and the value "test3@example.com",
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
    Given I created a subscriber with the method "EMAIL" and the value "test7@example.com",
    And I stored the last response field "verificationCode" in the variable "verificationCode",
    And I stored the last response field "id" in the variable "subscriptionId",
    And I confirm the subscription "${subscriptionId}" with the code "${verificationCode}"
    When I delete the subscription "${subscriptionId}"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "UNSUBSCRIBED",
    And the subscription in the last response should not have a value.

  Scenario: Deleting a subscription should result in the subscription actually being deleted
    Given I created a subscriber with the method "EMAIL" and the value "test8@example.com",
    And I stored the last response field "verificationCode" in the variable "verificationCode",
    And I stored the last response field "id" in the variable "subscriptionId",
    And I confirm the subscription "${subscriptionId}" with the code "${verificationCode}"
    And I delete the subscription "${subscriptionId}"
    And the last call succeeded,
    When I fetch the subscription "test7@example.com"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "UNSUBSCRIBED",
    And the subscription in the last response should not have a value.
