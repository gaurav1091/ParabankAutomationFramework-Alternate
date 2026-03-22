package com.parabank.automation.stepdefinitions.ui;

import com.parabank.automation.driver.DriverManager;
import io.cucumber.java.en.Given;
import org.testng.Assert;

public class ApplicationLaunchSteps {

    @Given("user launches the ParaBank application")
    public void userLaunchesTheParaBankApplication() {
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("parabank"), "ParaBank application did not open successfully.");
    }
}