Feature: Login functionality

  @ui @login @smoke @regression @positive
  Scenario Outline: Successful login with valid centralized JSON test data
    Given user is on the ParaBank login page
    When user logs in with valid test data key "<testDataKey>"
    Then user should be navigated to the home page
    And logout link should be visible

    Examples:
      | testDataKey |
      | validUser   |

  @ui @login @negative @quarantined
  Scenario Outline: Login with invalid centralized JSON test data
    Given user is on the ParaBank login page
    When user attempts login failure with test data key "<testDataKey>"
    Then user should see login error message

    Examples:
      | testDataKey            |
      | invalidUser            |
      | validUserWrongPassword |
      | wrongUserValidPassword |
