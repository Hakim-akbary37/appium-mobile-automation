package com.mobile.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * ExtentReports TestNG Listener for generating comprehensive test reports
 * Implements both ITestListener and ISuiteListener for complete test lifecycle coverage
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {
    private static final Logger logger = LoggerFactory.getLogger(ExtentReportListener.class);
    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    
    private static final String REPORTS_DIR = "reports";
    private static final String EXTENT_REPORT_NAME = "ExtentReport";
    
    /**
     * Initialize ExtentReports at suite start
     */
    @Override
    public void onStart(ISuite suite) {
        logger.info("Starting test suite: {}", suite.getName());
        initializeExtentReports();
    }
    
    /**
     * Finalize ExtentReports at suite finish
     */
    @Override
    public void onFinish(ISuite suite) {
        logger.info("Finished test suite: {}", suite.getName());
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed successfully");
            
            // Clean up old reports
            cleanupOldReports();
        }
    }
    
    /**
     * Create ExtentTest for each test method
     */
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getMethod().getMethodName());
        
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        ExtentTest test = extentReports.createTest(testName, description != null ? description : testName);
        test.assignCategory(result.getTestClass().getName());
        
        extentTest.set(test);
        
        // Log test start
        extentTest.get().log(Status.INFO, "Test started: " + testName);
    }
    
    /**
     * Log test success
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getMethod().getMethodName());
        
        extentTest.get().log(Status.PASS, "Test passed successfully");
        extentTest.get().log(Status.INFO, "Execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        
        // Remove from ThreadLocal
        extentTest.remove();
    }
    
    /**
     * Log test failure with error details
     */
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        
        extentTest.get().log(Status.FAIL, "Test failed");
        extentTest.get().log(Status.FAIL, "Error: " + result.getThrowable().getMessage());
        extentTest.get().log(Status.INFO, "Execution time: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        
        // Log stack trace
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.FAIL, "<details><summary>Stack Trace</summary><pre>" + 
                getStackTrace(result.getThrowable()) + "</pre></details>");
        }
        
        // Remove from ThreadLocal
        extentTest.remove();
    }
    
    /**
     * Log test skip
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getMethod().getMethodName());
        
        extentTest.get().log(Status.SKIP, "Test skipped");
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, "Reason: " + result.getThrowable().getMessage());
        }
        
        // Remove from ThreadLocal
        extentTest.remove();
    }
    
    /**
     * Initialize ExtentReports with configuration
     */
    private void initializeExtentReports() {
        try {
            // Create reports directory if it doesn't exist
            File reportsDir = new File(REPORTS_DIR);
            if (!reportsDir.exists()) {
                reportsDir.mkdirs();
            }
            
            // Generate timestamp for report file
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = REPORTS_DIR + File.separator + EXTENT_REPORT_NAME + "_" + timestamp + ".html";
            
            // Initialize ExtentSparkReporter
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            
            // Configure reporter
            sparkReporter.config().setDocumentTitle("Appium Mobile Automation Test Report");
            sparkReporter.config().setReportName("Mobile Application Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
            
            // Initialize ExtentReports
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Set system information
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            extentReports.setSystemInfo("Framework", "Appium + TestNG");
            extentReports.setSystemInfo("Report Generated", timestamp);
            
            logger.info("ExtentReports initialized successfully. Report path: {}", reportPath);
            
        } catch (Exception e) {
            logger.error("Failed to initialize ExtentReports", e);
            throw new RuntimeException("Failed to initialize ExtentReports", e);
        }
    }
    
    /**
     * Get current ExtentTest instance
     * @return ExtentTest current test instance
     */
    public static ExtentTest getCurrentTest() {
        return extentTest.get();
    }
    
    /**
     * Log a step in the current test
     * @param status Status of the step
     * @param message Step message
     */
    public static void logStep(Status status, String message) {
        if (extentTest.get() != null) {
            extentTest.get().log(status, message);
        }
    }
    
    /**
     * Convert throwable to string format
     * @param throwable Throwable to convert
     * @return String formatted stack trace
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Clean up old reports keeping only the specified number of recent reports
     */
    private void cleanupOldReports() {
        try {
            // Load configuration
            Properties props = loadConfig();
            int keepCount = Integer.parseInt(props.getProperty("reports.keep.count", "3"));
            
            Path reportsPath = Paths.get(REPORTS_DIR);
            if (!Files.exists(reportsPath)) {
                return;
            }
            
            // Get all ExtentReport HTML files
            List<File> reportFiles = Files.list(reportsPath)
                    .map(Path::toFile)
                    .filter(file -> file.getName().startsWith(EXTENT_REPORT_NAME) && file.getName().endsWith(".html"))
                    .sorted(Comparator.comparingLong(File::lastModified).reversed()) // Sort by modification time, newest first
                    .collect(Collectors.toList());
            
            // Delete old reports if we have more than the keep count
            if (reportFiles.size() > keepCount) {
                List<File> filesToDelete = reportFiles.subList(keepCount, reportFiles.size());
                for (File file : filesToDelete) {
                    if (file.delete()) {
                        logger.info("Deleted old report: {}", file.getName());
                    } else {
                        logger.warn("Failed to delete old report: {}", file.getName());
                    }
                }
                logger.info("Report cleanup completed. Kept {} most recent reports, deleted {} old reports", 
                           keepCount, filesToDelete.size());
            } else {
                logger.info("No cleanup needed. Current report count: {}, keep count: {}", 
                           reportFiles.size(), keepCount);
            }
            
        } catch (Exception e) {
            logger.error("Failed to cleanup old reports", e);
        }
    }
    
    /**
     * Load configuration properties
     * @return Properties loaded configuration
     */
    private Properties loadConfig() {
        Properties props = new Properties();
        try {
            String configPath = "src/test/resources/config.properties";
            File configFile = new File(configPath);
            
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);
                }
            } else {
                // Try to load from classpath
                try (FileInputStream fis = new FileInputStream("config.properties")) {
                    props.load(fis);
                }
            }
        } catch (IOException e) {
            logger.warn("Could not load config.properties, using default values", e);
        }
        return props;
    }
}

