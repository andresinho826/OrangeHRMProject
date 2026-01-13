package com.orangeHRM.base;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {
    protected static Properties properties;
    protected static WebDriver driver;
    private static ActionDriver actionDriver;
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load the configution file
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        properties.load(fis);
        logger.info("config.properties file loaded");
    }

    private void launchBrowser() {
        // initialize the webdriver based on browser defined in config.propertiees file
        String browser = properties.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
            logger.info("chromedriver initialized ");

        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
            logger.info("firefoxdriver initialized ");

        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
            logger.info("edgedriver initialized ");

        } else {
            throw new IllegalArgumentException("Browser Not Supported:" + browser);
        }

    }

    private void configureBrowser() {
        // configure browser setting such as, implicit wait maximaze, navegato to
        // implicit wait
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // maximize the driver
        driver.manage().window().maximize();

        // navigate to url
        try {
            driver.get(properties.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to naviga to the URL: "+e.getMessage());
        }

    }

    @BeforeMethod
    public void setup() throws IOException {
        System.out.println("Setting up webdriver for: " +this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        logger.info("Webdriver initialized and browser Maximized");
        logger.trace("this is a trace message");
        logger.error("this is an error message");
        logger.debug("this is a debug message");
        logger.fatal("this is a fatal message");
        logger.warn("this is a warn message");

        // initialize the action driver only once
        if(actionDriver == null){
            actionDriver = new ActionDriver(driver);
            logger.info("ActionDriver instance is created");
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("unable to quit the driver: " +e.getMessage());
            }
        }
        logger.info("Webdriver instance is closed.");
        driver=null;
        actionDriver=null;
    }



    // getter method for prop
    public static Properties getProperties(){
        return properties;
    }
/*
    // driver getter method
    public WebDriver getDriver(){
        return driver;

    }

     */

    // getter method for webdriver
    public static WebDriver getDriver() {
        if(driver==null){
            System.out.println("webdriver is not initialized");
            throw  new IllegalStateException("Webdriver ins not initialized");
        }
        return driver;
    }

    // getter method for actiondriver
    public static ActionDriver getActionDriver() {
        if(actionDriver==null){
            System.out.println("actiondriver is not initialized");
            throw  new IllegalStateException("actiondriver ins not initialized");
        }
        return actionDriver;
    }

    // driver setter method
    public void setDriver(WebDriver driver){
        BaseClass.driver = driver;
    }

    // static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
