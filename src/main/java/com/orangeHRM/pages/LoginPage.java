package com.orangeHRM.pages;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;

    /*
    // Initializa the ActionDriver object by passing webdriver instance
    public LoginPage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
    }

     */
    //
    public LoginPage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    //define locator using By class
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[@type='submit']");  // ✅ Cambio: XPath en lugar de CSS selector
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    // method to perform login
    public void login(String userName, String password){
        actionDriver.enterText(userNameField,userName);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);

    }

    // method to check if error mesage is displayed
    public boolean  isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);

    }

    // method to get the text from error message
    public String getErrorMessage(){
        return actionDriver.getText(errorMessage);
    }

    // verify if error is correct or not
    public boolean verifyErrorMessage( String expectedError){
        return actionDriver.compareText(errorMessage, expectedError);
    }

}
