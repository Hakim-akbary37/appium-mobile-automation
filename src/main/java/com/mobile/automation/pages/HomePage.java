package com.mobile.automation.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Home Page Object class representing the login screen (first screen of the app which has title as qatestapp)
 * Contains elements and methods specific to the login page
 */
public class HomePage extends BasePage {

    @AndroidFindBy(id = "com.truecaller:id/wizardLogo")
    private WebElement logo;


    @AndroidFindBy(id = "com.truecaller:id/nextButton")
    public WebElement getStartedButton;

    @AndroidFindBy(id = "com.truecaller:id/countryText")
    public WebElement countryListButton;

    @AndroidFindBy(id = "com.truecaller:id/search_button")
    public WebElement magnifier;

    @AndroidFindBy(id = "com.truecaller:id/countryText")
    public WebElement countryText;

    @AndroidFindBy (id = "com.truecaller:id/phoneNumberEditText")
    public WebElement phoneField;

    @AndroidFindBy ( id = "com.truecaller:id/nextButton")
    public WebElement verifyNumberButton;

    @AndroidFindBy(id = "com.truecaller:id/search_src_text")
    public WebElement searchFieldBar;

    @AndroidFindBy(id = "com.truecaller:id/phoneNumber")
    public WebElement phoneNumberConfirmation;

    @AndroidFindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.LinearLayout[2]/android.widget.FrameLayout/android.widget.ImageButton")
    public WebElement tickButton;

    @AndroidFindBy(id = "android:id/button2")
    public WebElement editButton;


    public void clickGetStartedButton(){
        logger.info("Clicking get started Button");
        safeClick(getStartedButton);
    }

    public void setCountryAndPhoneNumber(String country, String phoneNumber){

        countryListButton.click();
        magnifier.click();
        searchFieldBar.sendKeys(country);
        String searchBarText = searchFieldBar.getText();
        logger.info("succesfully took the text {} from search bar", searchBarText);

        countryText.click();
        safeSendKeys(phoneField, phoneNumber);

    }

    public void editPhoneNumber(String country, String phoneNumber){

        countryListButton.click();
        magnifier.click();
        searchFieldBar.sendKeys(country);
        String searchBarText = searchFieldBar.getText();
        logger.info("succesfully took the text {} from search bar", searchBarText);

        countryText.click();
        safeSendKeys(phoneField, phoneNumber);

        verifyNumberButton.click();


    }





    @Override
    public boolean isPageLoaded() {
        try {
            return isElementDisplayed(logo);
        } catch (Exception e) {
            return false;
        }
    }
}
