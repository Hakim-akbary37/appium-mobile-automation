package com.mobile.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Registration Page Object class representing the user registration form
 * Contains elements and methods specific to the registration page
 */
public class RegistrationPage extends BasePage {

    private WebElement countryListButton;

    @AndroidFindBy( id = "com.truecaller:id/titleText")
    private WebElement pageTitle;

    @AndroidFindBy(id = "com.truecaller:id/phoneNumberEditText")
    private WebElement phoneField;
    
    @AndroidFindBy(id = "com.qatestapp:id/button2")
    private WebElement submitButton;
    
    @AndroidFindBy(id = "com.qatestapp:id/subscribeCheckbox")
    private WebElement termsCheckbox;
    
    /**
     * Complete registration process
  //   * @param fullName String first name
  //   * @param email String email address
  //   * @param phone String phone number
     */
//    public void completeRegistration(String fullName, String email, String phone) {
//        logger.info("Completing registration for user: {} {}", fullName);
//        fillRegistrationForm(fullName, email, phone);
//        clickSubmit();
//    }

    public void completeRegistrationForTrueCaller(String phoneNumber){
        logger.info("Putting Phone Number into the Field");
        safeSendKeys(phoneField, phoneNumber);

    }
    
//    private void fillRegistrationForm(String fullName, String email, String phone) {
//        logger.info("Filling complete registration form for user: {} {}", fullName);
//        enterFirstName(fullName);
//        enterEmail(email);
//        enterPhone(phone);
//        clickTermsCheckbox();
//    }
    
//    private void enterFirstName(String firstName) {
//        logger.info("Entering first name: {}", firstName);
//        safeSendKeys(fullNameField, firstName);
//    }
    
//    private void enterEmail(String email) {
//        logger.info("Entering email: {}", email);
//        safeSendKeys(emailField, email);
//    }
    
    private void enterPhone(String phone) {
        logger.info("Entering phone: {}", phone);
        safeSendKeys(phoneField, phone);
    }
    
    private void clickTermsCheckbox() {
        logger.info("Clicking terms and conditions checkbox");
        safeClick(termsCheckbox);
    }
    
    private void clickSubmit() {
        logger.info("Clicking submit button");
        safeClick(submitButton);
    }

//    @Override
//    public boolean isPageLoaded() {
//        return false;
//    }

    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(pageTitle);
        } catch (Exception e) {
            logger.error("Error checking if Registration Page is loaded", e);
            return false;
        }
    }
}
