Feature: Find Transactions functionality

  @ui @transactions @regression @positive
  Scenario Outline: Verify user can find transactions using centralized JSON test data
    Given user is logged in and is on the home page for transaction search
    When user completes a transfer using find transactions test data key "<testDataKey>"
    And user navigates to Find Transactions page
    Then user should be navigated to the Find Transactions page
    When user searches transactions by amount using the same test data key "<testDataKey>"
    Then matching transactions should be displayed
    And displayed transaction amount should match the searched amount

    Examples:
      | testDataKey             |
      | smallTransactionSearch  |
      | mediumTransactionSearch |
