Feature: Accounts Overview functionality

  @ui @accounts @smoke @regression @positive
  Scenario: Verify Accounts Overview page after successful login
    Given user is logged into the ParaBank application
    When user clicks on Accounts Overview link
    Then user should be navigated to the Accounts Overview page
    And accounts table should be visible
    And at least one account should be displayed
