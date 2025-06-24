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
    private final String incorrectPhoneNumber = ConfigManager.getincorrectPhoneNumber();



    @Test(description = "Verify successful user flow to Phone Number confirmation Screen")
    public void testUserRegistrationToConfirmationScreen() {
        logTestStart("True caller App Launch and Basic Navigation to Phone Number confirmation Screen");

        logTestStep("Initializing page objects");
        homePage = new HomePage();

        logger.info("Attempting to verify Home Page loaded state");
        Assert.assertTrue(homePage.isPageLoaded(), "Home page did not load successfully.");
        logger.info("Home page is loaded, continuing with the test.");


        logTestStep("Trying to safe click the get started button");
        logger.info("Landed on Home Page. Verifying {} is displayed", homePage.getStartedButton.getText());
        homePage.safeClick(homePage.getStartedButton);

        homePage.setCountryAndPhoneNumber(country, phoneNumber);

        String phoneNumberOnTheFinalScreen = homePage.phoneNumberConfirmation.getText();
        String phoneNumberOnTheFinalScreenNoSpacing = phoneNumberOnTheFinalScreen.replaceAll(" ", "").substring(3); //applying substring to remove the country code
        logTestStep("Removing the country code and spaces from the phone number on the screen");
        Assert.assertEquals(phoneNumberOnTheFinalScreenNoSpacing, phoneNumber);

    }

    @Test(description = "Verify user can edit phone number")
    public void editPhoneNumber() {

        HomePage homePage = new HomePage();

        logger.info("Attempting to verify home page is loaded");
        homePage.isPageLoaded();

        logTestStep("Tapping get started button in the edit phone number test");
        homePage.safeClick(homePage.getStartedButton);

        logger.info("Inserting country and phone number");
        homePage.editPhoneNumber(country, phoneNumber);
        Assert.assertTrue(homePage.editButton.isDisplayed(), "Edit button is not displayed");

        logger.info("Clicking edit button");
        homePage.editButton.click();
        Assert.assertTrue(homePage.phoneField.isDisplayed(), "No phone field found");

    }

    @Test(description = "Test Invalid Phone Number")
    public void invalidPhoneNumber() {

        HomePage homePage = new HomePage();
        logger.info("Verifying Home Page is loaded");
        homePage.isPageLoaded();

        logTestStep("Tapping Get Started Button");
        homePage.safeClick(homePage.getStartedButton);

        logger.info("Puttingincorrect phone number");
        homePage.setIncorrectCountryAndPhoneNumber(country, incorrectPhoneNumber);
        logger.info("incorrect phone number successfully set");
        Assert.assertTrue(homePage.incorrectErrorMessage.isDisplayed(), "No error popup displayed");



    }
}
