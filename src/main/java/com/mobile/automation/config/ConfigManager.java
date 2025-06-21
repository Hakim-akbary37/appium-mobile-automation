package com.mobile.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads from config.properties file and provides easy access to configuration values
 */
public class ConfigManager {
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                System.err.println("Warning: " + CONFIG_FILE + " not found. Using default values.");
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("platform.name", "Android");
        properties.setProperty("device.name", "Android Emulator");
        properties.setProperty("app.package", "com.example.app");
        properties.setProperty("app.activity", "com.example.app.MainActivity");
        properties.setProperty("automation.name", "UiAutomator2");
        properties.setProperty("appium.server.url", "http://127.0.0.1:4723");
        properties.setProperty("app.path", "apps/app.apk");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "20");
    }
    
    public static String getPlatformName() {
        return properties.getProperty("platform.name");
    }
    
    public static String getDeviceName() {
        return properties.getProperty("device.name");
    }
    
    public static String getAppPackage() {
        return properties.getProperty("app.package");
    }
    
    public static String getAppActivity() {
        return properties.getProperty("app.activity");
    }
    
    public static String getAutomationName() {
        return properties.getProperty("automation.name");
    }
    
    public static String getAppiumServerUrl() {
        return properties.getProperty("appium.server.url");
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait"));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait"));
    }
    
    public static String getAppPath() {
        return properties.getProperty("app.path");
    }

    public static String getRegisterCountry(){ return properties.getProperty("register.country");}
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get login username from configuration
     * @return login username
     */
    public static String getLoginUsername() {
        return properties.getProperty("login.username");
    }
    
    /**
     * Get login password from configuration
     * @return login password
     */
    public static String getLoginPassword() {
        return properties.getProperty("login.password");
    }

    /**
     * Get registration full name from configuration
     * @return registration full name
     */
    public static String getRegisterFullName() {
        return properties.getProperty("register.fullName");
    }

    /**
     * Get registration email from configuration
     * @return registration email
     */
    public static String getRegisterEmail() {
        return properties.getProperty("register.email");
    }

    /**
     * Get registration phone from configuration
     * @return registration phone
     */
    public static String getRegisterPhone() {
        return properties.getProperty("register.phone");
    }
}

