Feature: Subscriber management
  Scenario: Creating a new subscriber
    When I create a subscriber with the type "email" and the value "test1@example.com"
    Then the last call should succeed.
