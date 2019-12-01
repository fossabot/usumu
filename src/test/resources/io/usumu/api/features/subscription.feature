Feature: Subscriber management
  Scenario: Creating a new subscription with a valid e-mail address.
    When I create a subscriber with the type "EMAIL" and the value "test1@example.com"
    Then the last call should succeed.

  Scenario: Creating a new subscription with an invalid e-mail.
    When I create a subscriber with the type "EMAIL" and the value "test2 @example.com"
    Then the last call should fail.

  Scenario: Creating a new subscriber should return the subscription object.
    When I create a subscriber with the type "EMAIL" and the value "test4@example.com"
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the type "EMAIL" and the value "test4@example.com",
    And the subscription in the last response should have the status "unconfirmed".

  Scenario: Fetching a created subscription should return a subscription object.
    Given I created a subscriber with the type "EMAIL" and the value "test3@example.com"
    When I fetch the subscription "test3@example.com",
    Then the last call should succeed,
    And the last call should return a subscription,
    And the subscription in the last response should have the status "unconfirmed".
