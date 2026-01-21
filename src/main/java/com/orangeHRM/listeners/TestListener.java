package com.orangeHRM.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    // triggered when a suite starts
    @Override
    public void onStart(ITestContext context) {
        // Initialize ExtentReports instance
        ExtentManager.getReporter();
    }

    // triggered when a suite ends
    @Override
    public void onFinish(ITestContext context) {
        // flush the extent reports
        ExtentManager.endTest();
    }

    // triggered when a test fails
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage =  result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage);
        ExtentManager.logFailure(BaseClass.getDriver(),"Test failed","Test " + testName + " failed.");
    }

    // triggered when a test is skipped
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped" + testName);
    }

    // triggered when a test starts
    @Override
    public void onTestStart(ITestResult result) {
       String testName = result.getMethod().getMethodName();
       // start logging in extent report
       ExtentManager.startTest(testName);
       ExtentManager.logStep("Test " + testName + " started.");
    }


    // Triggered when a test success
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test passed Succesfully","Test " + testName + " passed successfully.");
    }
}
