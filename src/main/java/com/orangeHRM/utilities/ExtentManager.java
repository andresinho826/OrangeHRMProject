package com.orangeHRM.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    // initialize ExtentReports instance
    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            String reporPath = System.getProperty("user.dir") + File.separator + "src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reporPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Test Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            // adding system information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    // start the test
    public synchronized static ExtentTest startTest(String testName) {
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    // end the test
    public synchronized static void endTest() {
        getReporter().flush();
    }

    // get current thread's test
    public synchronized static ExtentTest getTest() {
        return test.get();
    }

    // Method to get the name of the current test
    public static String getTestName() {
        ExtentTest currentTest = getTest();
        if (currentTest != null) {
            return currentTest.getModel().getName();
        } else {
            return "No test is currently running for this thread.";
        }
    }

    // Log a step
    public static void logStep(String LogMessage) {
        ExtentTest extentTest = getTest();
        if (extentTest != null) {
            extentTest.info(LogMessage);
        }
    }

    // log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
       getTest().pass(logMessage);
       // screenshot method
        attachScreenshot(driver, screenshotMessage);
    }

    // log a step validation for API testing where WebDriver is not available
    public static void logStepValidationForAPI( String logMessage) {
        getTest().pass(logMessage);

    }

    // log a faliure
    public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage ) {
        String colorMessage = "<span style='color:red; font-weight:bold;'>" + logMessage + "</span>";
        ExtentTest extentTest = getTest();
        if (extentTest != null) {
            extentTest.fail(colorMessage);
        }
        //screenshot method
        attachScreenshot(driver, screenshotMessage);
    }

    // log a faliure for API testing where WebDriver is not available
    public static void logFailureAPI( String logMessage ) {
        String colorMessage = "<span style='color:red; font-weight:bold;'>" + logMessage + "</span>";
        getTest().fail(colorMessage);
    }


    // log a skip
    public static void logSkip(String logMessage) {
        String colorMessage = "<span style='color:orange; font-weight:bold;'>" + logMessage + "</span>";
        ExtentTest extentTest = getTest();
        if (extentTest != null) {
            extentTest.skip(colorMessage);
        }
    }

    // Sanitize filename - remove invalid Windows characters
    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "screenshot";
        }
        // Remove invalid Windows characters: < > : " / \ | ? *
        String sanitized = fileName.replaceAll("[<>:\"/\\|?*]", "_")
                                   .replaceAll("\\s+", "_")  // Replace spaces with underscores
                                   .replaceAll("_{2,}", "_"); // Replace multiple underscores with single

        // Limit to 100 characters to avoid path length issues
        if (sanitized.length() > 100) {
            sanitized = sanitized.substring(0, 100);
        }

        return sanitized;
    }

    // take a screenshot with date and time in the file
    public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Format date and time for filename
        String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

        // Sanitize screenshot name to remove invalid Windows characters
        String sanitizedName = sanitizeFileName(screenshotName);

        // saving screenshot in the specified location
        String screenshotDir = System.getProperty("user.dir") + File.separator + "src/test/resources/screenshots";
        File screenshotFolder = new File(screenshotDir);

        // Create directory if it doesn't exist
        if (!screenshotFolder.exists()) {
            screenshotFolder.mkdirs();
        }

        String destPath = screenshotDir + File.separator + sanitizedName + "_" + timestamp + ".png";
        File finalPath = new File(destPath);

        try {
            FileUtils.copyFile(source, finalPath);
            System.out.println("Screenshot saved successfully at: " + destPath);
        } catch (IOException e) {
            System.err.println("Error saving screenshot at " + destPath + ": " + e.getMessage());
            e.printStackTrace();
        }
        // convert screenshot to base64 fir embebing in the report
        String base64Format = convertToBase64(source);
        return base64Format;
    }

    // convert screenshot to base64 format
    public static String convertToBase64(File screenshotFile) {
        String base64Format = "";
        try {
            // Read file content into byte array
            byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
            //converr to base64
            base64Format = java.util.Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Format;
    }

    // attach screenshot to the report using Base64 format
    public synchronized static void attachScreenshot(WebDriver driver, String screenshotName) {
        try {
            String base64Screenshot = takeScreenshot(driver, screenshotName);
            ExtentTest extentTest = getTest();
            if (extentTest != null && !base64Screenshot.isEmpty()) {
                extentTest.addScreenCaptureFromBase64String(base64Screenshot, screenshotName);
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to attach screenshot: " + e.getMessage());
            // Don't report as fail, just log the warning
            // This prevents false negatives when screenshot fails but test continues
        }
    }

    // register webdirver for current thead
    public static void registerDriver(WebDriver driver) {
        long threadId = Thread.currentThread().getId();
        driverMap.put(threadId, driver);
    }
}

