package com.orangeHRM.test;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.pages.HomePage;
import com.orangeHRM.pages.LoginPage;
import com.orangeHRM.utilities.DBConnection;
import com.orangeHRM.utilities.DataProviders;
import com.orangeHRM.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class DBVerificationTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
    public void verifyEmployeeNameVerificationFromDB(String emplID, String emplName) {

        SoftAssert softAssert = getSoftAssert();

        ExtentManager.logStep("Logging with admin user to verify employee from DB");
        loginPage.login(properties.getProperty("username_local"), properties.getProperty("password_local"));

        ExtentManager.logStep("Click on PIM tab to search employee from DB");
        homePage.navigateToPIMTab();

        ExtentManager.logStep("Search employee by name and verify with DB record");
        homePage.searchEmployeeByName(emplName);

        ExtentManager.logStep("Get the employee name from DB to verify with search result");
        // fetch employee details from DB
        Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(emplID);

        if (!employeeDetails.containsKey("firstName") || !employeeDetails.containsKey("lastName")) {
            softAssert.fail("Employee details fetched from DB are incomplete or null");
        }

        String emplNameDB = employeeDetails.get("firstName").trim().toLowerCase();
        String emplLastnameDB = employeeDetails.get("lastName").trim().toLowerCase();
        String emplNameUI = emplName.trim().toLowerCase();

        ExtentManager.logStep("Verify the employee name and lastname");
        ExtentManager.logStep("Employee name from DB: " + emplNameDB + ", Last name from DB: " + emplLastnameDB);
        ExtentManager.logStep("Employee name from UI: " + emplNameUI);

        softAssert.assertTrue(homePage.verifyEmployeeNameInSearchResult(emplNameDB),
                "Employee first name does not match with DB record. Expected: " + emplNameDB + ", Found: " + emplNameUI);
        softAssert.assertTrue(homePage.verifyLastNameInSearchResult(emplLastnameDB),
                "Employee last name does not match with DB record");

        ExtentManager.logStep("Employee name and last name verified successfully with DB record");
        softAssert.assertAll();
    }
}
