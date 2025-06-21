# Appium Mobile Automation Framework

A streamlined Appium automation framework in Java for testing native Android mobile applications. This framework implements the Page Object Model (POM) design pattern and focuses on a single, robust registration test case.

## Framework Features

- **Page Object Model (POM)** - Clean separation of test logic and page elements
- **TestNG Integration** - Powerful testing framework with advanced features
- **ExtentReports** - Comprehensive HTML reporting with detailed test results
- **Error Handling** - Comprehensive exception handling and logging
- **Flexible Configuration** - Easy configuration management via properties files
- **Maven Build Tool** - Dependency management and build automation


## Project Structure

```
appium-mobile-automation/
├── src/
│   ├── main/java/com/mobile/automation/
│   │   ├── config/
│   │   │   └── ConfigManager.java          # Configuration management
│   │   ├── pages/
│   │   │   ├── BasePage.java               # Base page with common methods
│   │   │   ├── HomePage.java               # Login page object
│   │   │   ├── LandingPage.java            # Landing page object
│   │   │   └── RegistrationPage.java       # Registration page object
│   │   └── utils/
│   │       └── DriverManager.java          # Appium driver management
│   └── test/
│       ├── java/com/mobile/automation/
│       │   ├── listeners/
│       │   │   └── ExtentReportListener.java # ExtentReports listener
│       │   └── tests/
│       │       ├── BaseTest.java           # Base test class
│       │       └── RegistrationTest.java   # Main registration test
│       └── resources/
│           ├── config.properties           # Framework configuration
│           ├── logback.xml                 # Logging configuration
│           └── testng.xml                  # TestNG suite configuration
├── apps/
│   └── QATestApp-1.1.apk                   # Test application
├── reports/                                # Test reports directory
├── logs/                                   # Log files directory
├── pom.xml                                 # Maven configuration
├── .gitignore                              # Git ignore file
└── README.md                               # This file
```

## Prerequisites

Before setting up the framework, ensure you have the following installed:

### Required Software
1. **Java Development Kit (JDK) 11 or higher**
2. **Apache Maven 3.6.0 or higher**
3. **Android SDK**
4. **Appium Server**
5. **Android Emulator or Real Device**



### 3. Verify Test Application
Ensure the QATestApp-1.1.apk is present in the `apps/` directory. 

### 4. Start Appium Server


### 5. Start Android Emulator


## Running Tests

### Quick Test Execution (No HTML Reports)
For development and quick testing - fastest execution:

mvn test -Dtest='com.mobile.automation.tests.RegistrationTest#testSuccessfulRegistrationWithValidData'


### 📊 Reports and Output

#### ExtentReports (HTML)
- **Location**: `reports/ExtentReport_YYYY-MM-DD_HH-mm-ss.html`
- **Contents**: Detailed test steps, execution time, system info, pass/fail status


### Test Execution Flow
The test will:
1. Launch the QATestApp
2. Perform login if required (using configured credentials)
3. Navigate to the registration page
4. Fill out the registration form with valid data:
   - Name: Hakim
   - Email: hakim@akbary.com
   - Phone: 123456
5. Subscribe to newsletter
6. Submit the registration


## 🔍 Framework Components

### ConfigManager
- Centralized configuration management
- Reads from `config.properties`
- Provides access to app config, device settings, and login credentials

### DriverManager
- Android driver initialization and cleanup
- Configures capabilities for QATestApp
- Handles device connection and app installation

### Page Objects

#### BasePage
- Common functionality for all page objects
- Safe element interaction methods (`safeClick`, `safeSendKeys`, `safeGetText`)
- Wait strategies and error handling
- Abstract `isPageLoaded()` method for page verification

#### HomePage
- Handles login functionality
- Methods: `login()`, `isPageLoaded()`
- Element interactions for username/password fields and login button

#### LandingPage
- Manages navigation from landing page to registration
- Methods: `clickRegister()`, `isPageLoaded()`
- Handles page title verification and navigation logic

#### RegistrationPage
- Complete registration form handling
- Methods: `completeRegistration()`, `isPageLoaded()`
- Form field interactions: name, email, phone, terms checkbox, submit

### BaseTest
- Common setup and teardown for all tests
- Logging helper methods (`logTestStart`, `logTestStep`, `logTestEnd`)
- Driver lifecycle management
