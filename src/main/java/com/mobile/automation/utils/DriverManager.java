package com.mobile.automation.utils;

import com.mobile.automation.config.ConfigManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Driver Manager to handle Appium driver initialization and management
 * Implements ThreadLocal to support parallel execution
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    
    /**
     * Initialize Android driver with capabilities
     */
    public static void initializeDriver() {
        logger.info("Initializing Android driver...");
        
        try {
            // Verify Appium server is running
            if (!isAppiumServerRunning()) {
                throw new RuntimeException("Appium server is not running at: " + ConfigManager.getAppiumServerUrl());
            }
            
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName(ConfigManager.getPlatformName());
            options.setDeviceName(ConfigManager.getDeviceName());
            
            // Configure app launch strategy
            String appPath = ConfigManager.getAppPath();
            if (appPath != null && !appPath.trim().isEmpty()) {
                File appFile = new File(appPath);
                if (appFile.exists()) {
                    logger.info("Setting app path: {}", appFile.getAbsolutePath());
                    options.setApp(appFile.getAbsolutePath());
                    
                    // Also set package and activity for better control
                    options.setAppPackage(ConfigManager.getAppPackage());
                    options.setAppActivity(ConfigManager.getAppActivity());
                } else {
                    logger.warn("App file not found at path: {}. Using package/activity only.", appPath);
                    options.setAppPackage(ConfigManager.getAppPackage());
                    options.setAppActivity(ConfigManager.getAppActivity());
                }
            } else {
                logger.info("No app path provided. Using package/activity.");
                options.setAppPackage(ConfigManager.getAppPackage());
                options.setAppActivity(ConfigManager.getAppActivity());
            }
            
            // Set automation engine
            options.setAutomationName(ConfigManager.getAutomationName());
            
            // Enhanced capabilities for better stability
            options.setNewCommandTimeout(Duration.ofSeconds(300));
            options.setFullReset(false);
            options.setNoReset(false);
            
            // Additional capabilities for Truecaller specifically
            options.setCapability("appium:autoGrantPermissions", true);
            options.setCapability("appium:ignoreUnimportantViews", false);
            options.setCapability("appium:disableWindowAnimation", true);
            options.setCapability("appium:skipDeviceInitialization", false);
            options.setCapability("appium:skipServerInstallation", true);
            options.setCapability("appium:autoLaunch", true);
            options.setCapability("appPackage", "com.truecaller");
            options.setCapability("appActivity", "com.truecaller.ui.TruecallerInit");
            
            // Timeout settings
            options.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
            options.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
            
            logger.info("Connecting to Appium server at: {}", ConfigManager.getAppiumServerUrl());
            logger.info("App Package: {}", ConfigManager.getAppPackage());
            logger.info("App Activity: {}", ConfigManager.getAppActivity());
            logger.info("Device Name: {}", ConfigManager.getDeviceName());
            
            URL serverUrl = new URL(ConfigManager.getAppiumServerUrl());
            AndroidDriver androidDriver = new AndroidDriver(serverUrl, options);
            
            // Set timeouts
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getImplicitWait()));
            
            driver.set(androidDriver);
            logger.info("Android driver initialized successfully");
            
            // Wait a bit for app to fully load
            Thread.sleep(3000);
            
        } catch (MalformedURLException e) {
            logger.error("Invalid Appium server URL: {}", ConfigManager.getAppiumServerUrl(), e);
            throw new RuntimeException("Failed to initialize driver due to invalid server URL", e);
        } catch (Exception e) {
            logger.error("Failed to initialize Android driver", e);
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }
    
    /**
     * Check if Appium server is running
     */
    private static boolean isAppiumServerRunning() {
        try {
            URL serverUrl = new URL(ConfigManager.getAppiumServerUrl() + "/status");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) serverUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            logger.warn("Appium server status check failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the current driver instance
     * @return AppiumDriver instance
     */
    public static AppiumDriver getDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver == null) {
            logger.error("Driver is not initialized. Call initializeDriver() first.");
            throw new IllegalStateException("Driver is not initialized");
        }
        return currentDriver;
    }
    
    /**
     * Quit the driver and remove from ThreadLocal
     */
    public static void quitDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                logger.info("Quitting driver...");
                currentDriver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            } finally {
                driver.remove();
            }
        }
    }
    
    /**
     * Check if driver is initialized
     * @return true if driver is initialized, false otherwise
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
}

