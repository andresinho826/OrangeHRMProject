package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyValidLoginTest() {
        //ExtentManager.startTest("verifyValidLoginTest"); // moved to TestListener class
        ExtentManager.logStep("Navigating to Login Page entering valid credentials");
        loginPage.login("admin", "admin123");
        ExtentManager.logStep("Verifying successful login by checking Admin tab visibility");
        assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after succesful login");
        homePage.logout();
        ExtentManager.logStep("Logged out successfully");
        staticWait(2);
    }

    @Test
    public void inValidLoginTest() {
        //ExtentManager.startTest("inValidLoginTest"); // moved to TestListener class
        ExtentManager.logStep("Navigating to Login Page entering invalid credentials");
        loginPage.login("adminn", "ad1234");
        ExtentManager.logStep("Verifying error message for invalid login attempt");
        String expectedErrorMessage = "Invalid credentials";
        assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),
                " Test Failed: invalid error message ");
        ExtentManager.logStep("Error message verified successfully for invalid login attempt");
        //staticWait(2);
    }
}
