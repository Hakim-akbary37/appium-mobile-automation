package com.mobile.automation.pages;

import com.mobile.automation.config.ConfigManager;
import com.mobile.automation.utils.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base Page class that provides common functionality for all page objects
 */
public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    
    /**
     * Constructor to initialize driver and wait
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getExplicitWait()));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
    
    /**
     * Wait for element to be visible and return it
     * @param element WebElement to wait for
     * @return WebElement once visible
     */
    protected WebElement waitForElementToBeVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible: {}", element, e);
            throw new RuntimeException("Element not visible", e);
        }
    }
    
    /**
     * Wait for element to be clickable and return it
     * @param element WebElement to wait for
     * @return WebElement once clickable
     */
    protected WebElement waitForElementToBeClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable: {}", element, e);
            throw new RuntimeException("Element not clickable", e);
        }
    }
    
    /**
     * Safe click method with wait
     * @param element WebElement to click
     */
    public void safeClick(WebElement element) {
        try {
            WebElement clickableElement = waitForElementToBeClickable(element);
            clickableElement.click();
            logger.info("Successfully clicked element: {}", element);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", element, e);
            throw new RuntimeException("Failed to click element", e);
        }
    }
    
    /**
     * Safe send keys method with wait
     * @param element WebElement to send keys to
     * @param text Text to send
     */
    protected void safeSendKeys(WebElement element, String text) {
        try {
            WebElement visibleElement = waitForElementToBeVisible(element);
            visibleElement.clear();
            visibleElement.sendKeys(text);
            logger.info("Successfully entered text '{}' in element: {}", text, element);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", element, e);
            throw new RuntimeException("Failed to send keys to element", e);
        }
    }
    
    /**
     * Get text from element with wait
     * @param element WebElement to get text from
     * @return String text content
     */
    protected String safeGetText(WebElement element) {
        try {
            WebElement visibleElement = waitForElementToBeVisible(element);
            String text = visibleElement.getText();
            logger.info("Successfully retrieved text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", element, e);
            throw new RuntimeException("Failed to get text from element", e);
        }
    }
    
    /**
     * Check if element is displayed
     * @param element WebElement to check
     * @return boolean true if displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", element);
            return false;
        }
    }
    
    /**
     * Abstract method to verify page is loaded
     * Each page should implement this method to verify critical elements
     */
    public abstract boolean isPageLoaded();
}

