# Appium Mobile Automation Framework

A streamlined Appium automation framework in Java for testing native Android mobile applications. This framework implements the Page Object Model (POM) design pattern and focuses on a single, registration test case for showcasing

## Framework Features

- **Page Object Model (POM)** - Clean separation of test logic and page elements
- **TestNG Integration** - Powerful testing framework with userful features
- **ExtentReports** - HTML reporting with detailed test results
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

### Setup Steps

1. **Verify Java and Maven Installation**: `java -version`, `mvn -version`
2. **Configure Android SDK**: Ensure your `ANDROID_HOME` environment variable is set and points to your Android SDK installation. Add SDK `platform-tools` and `tools` to your system's `PATH`
3. **Verify Test Application**: Ensure the `truecaller.apk` as used in the `config.properties` example is present in the `apps/` directory.
4. **Start Appium Server**: `npx appium`. Ensure the server is running on http://127.0.0.1:4723 as configured in `config.properties`
5. **Start Android Emulator (or connect a Real Device)**: Launch your desired Android Emulator via Android Studio's AVD Manager, or connect a physical Android device and ensure it's recognized by `adb` (`adb devices`).

## Configuration

The framework's behavior can be easily configured via the `src/test/resources/config.properties` file. Update these values based on your test environment, device, and application under test.

```properties
# Platform Configuration
platform.name=Android
platform.version=11.0
device.name=192.168.xx.xxx:5555
# Alternative device configuration
#device.name=172.16.xx.xxx:5555

# Application Configuration
app.package=com.truecaller
app.activity=com.truecaller.ui.TruecallerInit
app.path=apps/truecaller.apk

# Appium Configuration
automation.name=UiAutomator2
appium.server.url=http://127.0.0.1:4723

# Timeout Configuration (in seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30
command.timeout=300

# Test Data Configuration
test.data.path=src/test/resources/testdata

# Logging Configuration
log.level=INFO
log.file.path=logs/automation.log

# Performance and Stability Settings
no.reset=true
full.reset=false
```

## Running Tests

### Quick Test Execution
For development and quick testing - fastest execution:

```bash
mvn test -Dtest='com.mobile.automation.tests.RegistrationTest#testUserRegistrationToConfirmationScreen'
```

## Reports and Output

### ExtentReports (HTML)
- **Location**: `reports/ExtentReport_YYYY-MM-DD_HH-mm-ss.html`
- **Contents**: Detailed test steps, execution time, system info, pass/fail status

### Test Execution Flow
The test will:
1. Launch the app in /apps directory
2. Handles the first install/launch screen (native app permission handled)
3. Navigate to the registration page
4. Fill out the registration form with valid data:
   - country: ConfigManager.getRegisterCountry()
   - Phone: ConfigManager.getRegisterPhone()
5. Tap verify phone number button
6. Assert phone number on the screen against phone number used in test data (Configmanager.getRegisterPhone)

## Framework Components

### ConfigManager
- Centralized configuration management
- Reads from `config.properties`
- Provides access to app config, device settings, and login credentials

### DriverManager
- Android driver initialization and cleanup
- Configures capabilities for target app (now TrueCaller)
- Handles device connection and app installation

### Page Objects

#### BasePage
- Common functionality for all page objects
- Safe element interaction methods (`safeClick`, `safeSendKeys`, `safeGetText`)
- Wait strategies and error handling
- Abstract `isPageLoaded()` method for page verification

#### HomePage
- Handles login functionality
- Methods: `clickGetStartedButton()`, `isPageLoaded()`, `setCountryAndPhoneNumber(country, phoneNumber)`
- Home page elements and locators

### BaseTest
- Common setup and teardown for all tests
- Logging helper methods (`logTestStart`, `logTestStep`, `logTestEnd`)
- Driver lifecycle management