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


    // method to verify if admin tab is visible
    public boolean isAdminTabVisible(){
        return  actionDriver.isDisplayed(admingTab);
    }

    // method to verify orangeHRM Logo
    public boolean verifyOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    // method to perfom logout
    public void logout(){
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }
}
