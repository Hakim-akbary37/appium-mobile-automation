package com.mobile.automation.utils;

import com.mobile.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Utility class for device preparation and validation
 */
public class DeviceUtils {
    private static final Logger logger = LoggerFactory.getLogger(DeviceUtils.class);
    
    /**
     * Prepare device for testing by checking connectivity and app installation
     */
    public static void prepareDevice() {
        logger.info("Preparing device for testing...");
        
        // Check device connectivity
        if (!isDeviceConnected()) {
            throw new RuntimeException("No Android devices connected. Please connect a device or start an emulator.");
        }
        
        // Check if app is installed
        if (!isAppInstalled()) {
            logger.info("App not installed. Installing from APK path...");
            installApp();
        } else {
            logger.info("App is already installed on device");
        }
        
        // Grant permissions if needed
        grantAppPermissions();
        
        logger.info("Device preparation completed successfully");
    }
    
    /**
     * Check if Android device is connected
     */
    public static boolean isDeviceConnected() {
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line;
            boolean deviceFound = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("device") && !line.contains("List of devices")) {
                    deviceFound = true;
                    logger.info("Found connected device: {}", line.trim());
                }
            }
            
            process.waitFor();
            return deviceFound;
            
        } catch (Exception e) {
            logger.error("Error checking device connectivity", e);
            return false;
        }
    }
    
    /**
     * Check if the target app is installed on device
     */
    public static boolean isAppInstalled() {
        try {
            String command = "adb shell pm list packages | grep " + ConfigManager.getAppPackage();
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            String line = reader.readLine();
            process.waitFor();
            
            boolean isInstalled = line != null && line.contains(ConfigManager.getAppPackage());
            logger.info("App {} installation status: {}", ConfigManager.getAppPackage(), isInstalled ? "INSTALLED" : "NOT INSTALLED");
            
            return isInstalled;
            
        } catch (Exception e) {
            logger.error("Error checking app installation", e);
            return false;
        }
    }
    
    /**
     * Install app from APK path
     */
    public static void installApp() {
        try {
            String appPath = ConfigManager.getAppPath();
            if (appPath == null || appPath.trim().isEmpty()) {
                logger.warn("No app path provided. Skipping installation.");
                return;
            }
            
            logger.info("Installing app from: {}", appPath);
            String[] command = {"adb", "install", "-r", appPath};
            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0 || output.toString().contains("Success")) {
                logger.info("App installed successfully");
            } else {
                logger.error("App installation failed. Output: {}, Error: {}", output.toString(), errorOutput.toString());
                throw new RuntimeException("Failed to install app");
            }
            
        } catch (Exception e) {
            logger.error("Error installing app", e);
            throw new RuntimeException("Failed to install app", e);
        }
    }
    
    /**
     * Grant necessary permissions to the app
     */
    public static void grantAppPermissions() {
        try {
            String appPackage = ConfigManager.getAppPackage();
            
            // Common permissions needed by Truecaller
            String[] permissions = {
                "android.permission.READ_PHONE_STATE",
                "android.permission.CALL_PHONE",
                "android.permission.READ_CONTACTS",
                "android.permission.WRITE_CONTACTS",
                "android.permission.READ_CALL_LOG",
                "android.permission.WRITE_CALL_LOG",
                "android.permission.READ_SMS",
                "android.permission.RECEIVE_SMS",
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
            };
            
            for (String permission : permissions) {
                try {
                    String command = "adb shell pm grant " + appPackage + " " + permission;
                    Process process = Runtime.getRuntime().exec(command);
                    process.waitFor();
                    logger.debug("Granted permission: {}", permission);
                } catch (Exception e) {
                    logger.debug("Could not grant permission {} (might not be needed): {}", permission, e.getMessage());
                }
            }
            
            logger.info("App permissions granted");
            
        } catch (Exception e) {
            logger.warn("Error granting app permissions: {}", e.getMessage());
        }
    }
    
    /**
     * Force stop the app to ensure clean state
     */
    public static void stopApp() {
        try {
            String command = "adb shell am force-stop " + ConfigManager.getAppPackage();
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            logger.info("App stopped successfully");
        } catch (Exception e) {
            logger.warn("Error stopping app: {}", e.getMessage());
        }
    }
    
    /**
     * Clear app data (useful for clean test state)
     */
    public static void clearAppData() {
        try {
            String command = "adb shell pm clear " + ConfigManager.getAppPackage();
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            logger.info("App data cleared successfully");
        } catch (Exception e) {
            logger.warn("Error clearing app data: {}", e.getMessage());
        }
    }
    
    /**
     * Start the app manually (useful for debugging)
     */
    public static void startApp() {
        try {
            String command = "adb shell am start -n " + ConfigManager.getAppPackage() + "/" + ConfigManager.getAppActivity();
            Process process = Runtime.getRuntime().exec(command);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug("Start app output: {}", line);
            }
            
            process.waitFor();
            logger.info("App started successfully");
            
            // Wait for app to load
            Thread.sleep(3000);
            
        } catch (Exception e) {
            logger.error("Error starting app", e);
            throw new RuntimeException("Failed to start app", e);
        }
    }
}

