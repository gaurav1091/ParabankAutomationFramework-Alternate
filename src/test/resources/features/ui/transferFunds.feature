Feature: Transfer Funds functionality

  @ui @transfer @regression @positive @stateful
  Scenario Outline: Verify successful transfer of funds using centralized JSON test data
    Given user is logged in and is on the home page
    When user navigates to Transfer Funds page
    Then user should be navigated to the Transfer Funds page
    When user transfers funds using test data key "<testDataKey>"
    Then transfer should be completed successfully
    And transferred amount should be displayed correctly

    Examples:
      | testDataKey    |
      | smallTransfer  |
      | mediumTransfer |
