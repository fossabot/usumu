Feature: Subscriber management
  Scenario: Creating a new subscriber with a valid e-mail address.
    When I create a subscriber with the type "email" and the value "test1@example.com"
    Then the last call should succeed.

  Scenario: Creating a new subscriber with an invalid e-mail.
    When I create a subscriber with the type "email" and the value "test2 @example.com"
    Then the last call should fail.

  Scenario: Creating a new subscriber should yield a valid subscription.
    When I create a subscriber with the type "email" and the value "test3@example.com"
    Then the subscription for "test3@example.com" should exist,
    And the subscription in the last response should have the status "unconfirmed".
