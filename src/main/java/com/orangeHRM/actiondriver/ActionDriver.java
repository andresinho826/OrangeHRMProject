package com.orangeHRM.actiondriver;

import com.orangeHRM.base.BaseClass;
import com.orangeHRM.utilities.ExtentManager;
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
        try {
            applyBorder(by, "green");
            waitForElementToBeClickable(by);
            WebElement element = driver.findElement(by);
            element.click();
            logger.info("Clicked on element successfully");
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("unable to click on element: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // method to enter text into an input field
    public void enterText(By by, String value) {
        int retries = 3;
        int attempt = 0;

        while (attempt < retries) {
            try {
                waitForElementToBeVisible(by);
                applyBorder(by, "green");
                WebElement element = driver.findElement(by);
                element.clear();
                element.sendKeys(value);
                logger.info("Entered text successfully");
                return; // Éxito, salir del método
            } catch (Exception e) {
                applyBorder(by, "red");
                attempt++;
                if (attempt < retries) {
                    logger.warn("Enter text attempt {} failed, retrying...", attempt);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    logger.error("Unable to enter text after {} attempts: {}", retries, e.getMessage());
                }
            }
        }
    }

    //method fo get text from an input field
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("unable to get the text {}", e.getMessage());
            return "";
        }
    }

    // method to compare two text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            String actualText = element.getText();
            if (expectedText.equals(actualText)) {
                applyBorder(by, "green");
                logger.info("Text are matching: {} equal {}", actualText, expectedText);
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text", "Text matches: " + actualText);
                return true;
            } else {
                applyBorder(by, "red");
                logger.error("Text mismatch: " + actualText + " not equal " + expectedText);
                ExtentManager.logFailure(BaseClass.getDriver(),"Test Comparison Failed! ", "Text mismatch: " + actualText);
                return false;
            }
        } catch (Exception e) {
            logger.error("unable to compare texts: {}", e.getMessage());
        }
        return false;
    }

    // method to check if an element is displayed
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            applyBorder(by, "green");
            WebElement element = driver.findElement(by);
            boolean isDisplayed = element.isDisplayed();
            if (isDisplayed) {
                logger.info("Element is visible");
                ExtentManager.logStep("Element is visible");
                return true;
            }
            return false;
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Element is not displayed: {}", e.getMessage());
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
            applyBorder(by, "green");
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true)", null);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("unable to locate element{}", e.getMessage());
        }

    }

    // wait for element to be clickcable
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
            logger.info("Element is clickable");
        } catch (Exception e) {
            logger.warn("Timeout waiting for element to be clickable, proceeding anyway: {}", e.getMessage());
        }
    }

    // wait for element to be visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            logger.info("Element is visible");
        } catch (Exception e) {
            logger.warn("Timeout waiting for element to be visible, proceeding anyway: {}", e.getMessage());
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

    // utility method to border an element
    public void applyBorder(By by , String color) {
        try {
            // locate the element
            WebElement element = driver.findElement(by);
            // apply border using js executor
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].style.border='3px solid "+color+"'", element);
            logger.info("Border applied to element successfully" + getElementDescription(by));
        } catch (Exception e) {
            logger.warn("unable to apply border to element: {}" + getElementDescription(by), e.getMessage());
        }
    }
}