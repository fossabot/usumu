Feature: Logging
  Background:
    Given I uploaded a template "verification/body.html" with the content
    """
    <a href="http://example.com/subscriptions/{{ value }}?verificationCode={{ verificationCode }}">Verify</a>
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
    {{ value }}
    """

  Scenario: Creating a subscription should produce a log entry
    When I create a subscriber with the method "EMAIL" and the value "test10@example.com",
    And the last call succeeds,
    And I stored the last response field "id" in the variable "subscriptionId",
    Then the logs for subscription "${subscriptionId}" should contain a log event of the type "CREATED".

