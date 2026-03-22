Feature: Bill Pay functionality

  @ui @billpay @regression @positive
  Scenario Outline: Verify successful bill payment with centralized JSON test data
    Given user is logged in and is on the home page for bill payment
    When user navigates to Bill Pay page
    Then user should be navigated to the Bill Pay page
    When user submits the bill payment using test data key "<testDataKey>"
    Then bill payment should be completed successfully

    Examples:
      | testDataKey        |
      | electricityPayment |
      | waterBillPayment   |
