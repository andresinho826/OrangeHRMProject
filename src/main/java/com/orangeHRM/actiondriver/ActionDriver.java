package com.orangeHRM.actiondriver;

import com.orangeHRM.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProperties().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    // method to click element
    public void click(By by) {
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
        } catch (Exception e) {
            System.out.println("Unable to click element" + e.getMessage());
        }
    }

    // method to enter text into an input field
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(value);
        } catch (Exception e) {
            System.out.println("unable to enter the value: " + e.getMessage());
        }
    }

    //method fo get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            System.out.println("unable to get the text " + e.getMessage());
            return "";
        }
    }

    // method to compare two text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                System.out.println("Text are matching" + actualText + " equal" + expectedText);
                return true;
            } else {
                System.out.println("Text are not matching" + actualText + " not equal" + expectedText);
                return false;
            }
        } catch (Exception e) {
            System.out.println("unable to compare texts" + e.getMessage());
        }
        return false;
    }

    // method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            boolean isDisplayed = driver.findElement(by).isDisplayed();
            if (isDisplayed){
                System.out.println("Element is visible");
                return isDisplayed;
            }
            else {
                return isDisplayed;
            }
        } catch (Exception e) {
            System.out.println("Element is not displayed"+e.getMessage());
            return false;
        }

    }

    //wait for the page to load
    public void waitForPageLoad(int timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> Objects.equals(((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState"), "complete"));
            System.out.println("Page loaded succesfully.");
        } catch (Exception e) {
            System.out.println("Page did not load within " + timeOutInSec + "seconds. Exception: " + e.getMessage());
        }
    }

    // scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true)", null);
        } catch (Exception e) {
            System.out.println("unable to locate element"+e.getMessage());
        }

    }

    // wait for element to be clickcable
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            System.out.println("element is not clickable" + e.getMessage());
        }
    }

    // wait for element to be visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("Element is not visible" + e.getMessage());
        }
    }
}
