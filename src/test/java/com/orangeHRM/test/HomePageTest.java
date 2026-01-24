package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username, String password) {
        //ExtentManager.startTest("verify Home test"); // moved to TestListener class
        ExtentManager.logStep("Navigating to Login Page entering valid credentials");
        loginPage.login(username, password);
        ExtentManager.logStep("Verifying successful login by checking Admin tab visibility");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");
        ExtentManager.logStep("Logged out successfully");
    }
}
