package com.mobile.automation.tests;

import com.mobile.automation.utils.DriverManager;
import com.mobile.automation.utils.DeviceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base Test class that provides common setup and teardown functionality
 * All test classes should extend this class
 */
public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    
    /**
     * Setup method executed before each test method
     * Initializes the Appium driver
     */
    @BeforeMethod(alwaysRun = true)
    @Parameters({"deviceName", "platformVersion"})
    public void setUp(@Optional("iPhone") String deviceName,
                      @Optional("18.0") String platformVersion) {
        try {
            logger.info("=== Test Setup Started ===");
            logger.info("Device Name: {}", deviceName);
            logger.info("Platform Version: {}", platformVersion);
            
            // Prepare device first
            logger.info("Preparing device for testing...");
            DeviceUtils.prepareDevice();
            
            // Initialize driver
            logger.info("Initializing Appium driver...");
            DriverManager.initializeDriver();
            
            logger.info("Test setup completed successfully");
            logger.info("=== Test Setup Completed ===");
            
        } catch (Exception e) {
            logger.error("Failed to setup test environment", e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    /**
     * Teardown method executed after each test method
     * Quits the Appium driver and cleans up resources
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            logger.info("=== Test Teardown Started ===");
            
            // Quit driver
            if (DriverManager.isDriverInitialized()) {
                DriverManager.quitDriver();
                logger.info("Driver quit successfully");
            } else {
                logger.warn("Driver was not initialized, skipping quit");
            }
            
            logger.info("=== Test Teardown Completed ===");
            
        } catch (Exception e) {
            logger.error("Error during test teardown", e);
        }
    }
    
    /**
     * Helper method to log test start
     * @param testName String name of the test
     */
    protected void logTestStart(String testName) {
        logger.info("\n" + "=".repeat(80));
        logger.info("STARTING TEST: {}", testName);
        logger.info("=".repeat(80));
    }
    
    /**
     * Helper method to log test end
     * @param testName String name of the test
     * @param result String result of the test (PASSED/FAILED)
     */
    protected void logTestEnd(String testName, String result) {
        logger.info("=".repeat(80));
        logger.info("TEST {} - RESULT: {}", testName, result);
        logger.info("=".repeat(80) + "\n");
    }
    
    /**
     * Helper method to add test step logging
     * @param stepDescription String description of the test step
     */
    protected void logTestStep(String stepDescription) {
        logger.info("STEP: {}", stepDescription);
    }
    
    /**
     * Helper method to add verification logging
     * @param verification String description of what is being verified
     * @param result boolean result of the verification
     */
    protected void logVerification(String verification, boolean result) {
        String status = result ? "PASSED" : "FAILED";
        logger.info("VERIFICATION: {} - {}", verification, status);
    }
}

