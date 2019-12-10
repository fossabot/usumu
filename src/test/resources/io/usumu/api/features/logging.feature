Feature: Logging
  Scenario: Creating a subscription should produce a log entry
    When I create a subscriber with the method "EMAIL" and the value "test10@example.com"
    And the last call succeeds,
    And I stored the last response field "id" in the variable "subscriptionId"
    Then the subscription "${subscriptionId}" should have a log entry type "CREATED".