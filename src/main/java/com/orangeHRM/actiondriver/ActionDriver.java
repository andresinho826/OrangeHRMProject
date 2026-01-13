package com.orangeHRM.actiondriver;

import com.orangeHRM.base.BaseClass;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class ActionDriver {

    private final WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProperties().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("ActionDriver isntance is created");
    }

    // method to click element
    public void click(By by) {
        String getElementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("clicked an element-->" + getElementDescription + getText(by));
        } catch (Exception e) {
            System.out.println("Unable to click element" + e.getMessage());
            logger.error("unable click an element");
        }
    }

    // method to enter text into an input field
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(value);
            logger.info("Entered text on : {}"+ getElementDescription(by), value);
        } catch (Exception e) {
            logger.error("unable to enter the value: {}", e.getMessage());
        }
    }

    //method fo get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("unable to get the text {}", e.getMessage());
            return "";
        }
    }

    // method to compare two text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)) {
                logger.info("Text are matching{} equal{}", actualText, expectedText);
                return true;
            } else {
                logger.error("Text are not matching" + actualText + " not equal" + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("unable to compare texts{}", e.getMessage());
        }
        return false;
    }

    // method to check if an element is displayed
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            boolean isDisplayed = driver.findElement(by).isDisplayed();
            if (isDisplayed) {
                logger.info("Element is visible", getElementDescription(by));
                return isDisplayed;
            } else {
                return isDisplayed;
            }
        } catch (Exception e) {
            logger.error("Element is not displayed{}", e.getMessage());
            return false;
        }

    }

    //wait for the page to load
    public void waitForPageLoad(int timeOutInSec) {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> Objects.equals(((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState"), "complete"));
            logger.info("Page loaded succesfully.");
        } catch (Exception e) {
            logger.error("Page did not load within " + timeOutInSec + "seconds. Exception: " + e.getMessage());
        }
    }

    // scroll to an element
    public void scrollToElement(By by) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true)", null);
        } catch (Exception e) {
            logger.error("unable to locate element{}", e.getMessage());
        }

    }

    // wait for element to be clickcable
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("element is not clickable{}", e.getMessage());
        }
    }

    // wait for element to be visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible{}", e.getMessage());
        }
    }


    public String getElementDescription(By locator) {
        // check for null driver or locator to avoid NullPointerException
        if (driver == null || locator == null) {
            return "Driver or locator is null";
        }
        // find element using the locator
        WebElement element = driver.findElement(locator);

        // get element attributes
        String tagName = element.getTagName();
        String id = element.getAttribute("id");
        String classes = element.getAttribute("class");
        String name = element.getAttribute("name");
        String text = element.getText();

        // return the description based on element attributes
        try {
            if (isNotEmpty(text)) {
                return String.format("Tag: %s, Text: %s", tagName, truncateString(text, 50));
            } else if (isNotEmpty(id)) {
                return String.format("Tag: %s, ID: %s", tagName, id);
            } else if (isNotEmpty(name)) {
                return String.format("Tag: %s, Name: %s", tagName, name);
            } else if (isNotEmpty(classes)) {
                return String.format("Tag: %s, Classes: %s", tagName, classes);
            }
        } catch (Exception e) {
            logger.error("Unable to get element description: {}", e.getMessage());
        }
        return "Unable to get element description";
    }

    /**
     * Utility method to check if a string is not null and not empty
     *
     * @param value
     * @return
     */
    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * utility methot to truncate long string
     */
    public String truncateString(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength) + "...";
    }
}