Feature: Open New Account functionality

  @ui @accounts @regression @positive @stateful
  Scenario Outline: Verify user can create a new bank account with centralized JSON test data
    Given user is logged in and is on the home page for account creation
    When user navigates to Open New Account page
    Then user should be navigated to the Open New Account page
    When user creates a new account using test data key "<testDataKey>"
    Then new account should be created successfully
    And new account number should be displayed

    Examples:
      | testDataKey     |
      | savingsAccount  |
      | checkingAccount |
