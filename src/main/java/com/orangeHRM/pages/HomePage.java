package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private ActionDriver actionDriver;

    /*
    // Initializa the ActionDriver object by passing webdriver instance
    public HomePage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
    }

     */

    public HomePage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    // define locators using By class
    private By admingTab = By.xpath("//span[text()='Admin']");
    private  By userIDButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    private By pinTab = By.xpath("//span[text()='PIM']");
    private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton = By.xpath("//button[@type='submit']");
    private By employeeNameResult = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
    private By lastNameResult = By.xpath("//div[@class='oxd-table-card']/div/div[4]");



    // method to verify if admin tab is visible
    public boolean isAdminTabVisible(){
        return  actionDriver.isDisplayed(admingTab);
    }

    // method to verify orangeHRM Logo
    public boolean verifyOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    // Method to Navigate to PIM Tab
    public void navigateToPIMTab(){
        actionDriver.click(pinTab);
    }

    // Method to search employee by name
    public void searchEmployeeByName(String employeeName){
        actionDriver.enterText(employeeSearch, employeeName);
        actionDriver.click(searchButton);
        actionDriver.scrollToElement(employeeNameResult);
    }

    // verify employee name and last name in search result vs DB
    public boolean verifyEmployeeNameInSearchResult(String employeeNameDB){
        return actionDriver.compareText(employeeNameResult, employeeNameDB);
    }

    // verify last name in search result vs DB
    public boolean verifyLastNameInSearchResult(String lastNameDB){
        return actionDriver.compareText(lastNameResult, lastNameDB);
    }

    // method to perfom logout
    public void logout(){
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }
}
