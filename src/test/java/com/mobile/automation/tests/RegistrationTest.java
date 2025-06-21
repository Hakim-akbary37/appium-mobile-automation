package com.mobile.automation.tests;

import com.mobile.automation.config.ConfigManager;
import com.mobile.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for Registration Page functionality
 * Contains the testSuccessfulRegistrationWithValidData test
 */
public class RegistrationTest extends BaseTest {
    
    private HomePage homePage;
    private final String phoneNumber = ConfigManager.getRegisterPhone();
    private final String country = ConfigManager.getRegisterCountry();



    @Test(description = "Verify successful user flow to Phone Number confirmation Screen")
    public void testUserRegistrationToConfirmationScreen() throws InterruptedException {
        logTestStart("True caller App Launch and Basic Navigation to Phone Number confirmation Screen");

        logTestStep("Initializing page objects");
        homePage = new HomePage();


        logger.info("Attempting to verify Home Page loaded state");
        Assert.assertTrue(homePage.isPageLoaded(), "Home page did not load successfully.");
        logger.info("Home page is loaded, continuing with the test.");


        logTestStep("Trying to safe click the get started button");
        logger.info("Landed on Home Page. Verifying {} is displayed", homePage.getStartedButton.getText());
        homePage.safeClick(homePage.getStartedButton);

        homePage.countrySelection(country, phoneNumber);

        homePage.verifyNumberButton.click();


        String phoneNumberOnTheFinalScreen = homePage.phoneNumberConfirmation.getText();
        String phoneNumberOnTheFinalScreenNoSpacing = phoneNumberOnTheFinalScreen.replaceAll(" ", "").substring(3);
        logTestStep("Removing the country code and spaces from the phone number on the screen");
        Assert.assertEquals(phoneNumberOnTheFinalScreenNoSpacing, phoneNumber);




    }
}
