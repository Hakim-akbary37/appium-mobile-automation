package com.mobile.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Landing Page Object class representing the main landing screen where the app
 * shows a message containing the contact details provided in the previous screen by the user.
 * Contains elements and methods specific to the landing page
 */
public class LandingPage extends BasePage {
    
    @AndroidFindBy(id = "com.qatestapp:id/landingText")
    private WebElement textMessage;

    @AndroidFindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView")
    private WebElement pageTitle;
    
    @AndroidFindBy(id = "com.qatestapp:id/button2")
    private WebElement registerButton;
    
    private String getPageTitle() {
        logger.info("Getting page title text");
        return safeGetText(pageTitle);
    }
    
    /**
     * Click on submit button
     * @return RegistrationPage object for method chaining
     */
    public RegistrationPage clickSubmit() {
        logger.info("Checking current page and navigating to registration...");
        
        // Check the current page title to determine navigation
        try {
            String currentPageTitle = getPageTitle();
            logger.info("Current page title: {}", currentPageTitle);
            
            if ("LandingPage".equals(currentPageTitle)) {
                logger.info("On Landing Page, pressing back button to go to Registration Page");
                driver.navigate().back();
                
                // Wait a bit for the navigation
                Thread.sleep(2000);
                
                return new RegistrationPage();
            } else if ("RegistrationScreen".equals(currentPageTitle)) {
                logger.info("Already on Registration Screen, not clicking submit button");
                return new RegistrationPage();
            } else {
                logger.info("On unknown page ({}), clicking submit button", currentPageTitle);
                safeClick(registerButton);
                return new RegistrationPage();
            }
        } catch (Exception e) {
            logger.warn("Could not determine page title, attempting to click submit button", e);
            safeClick(registerButton);
            return new RegistrationPage();
        }
    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(textMessage);
        } catch (Exception e) {
            logger.error("Error checking if Landing Page is loaded", e);
            return false;
        }
    }
}
