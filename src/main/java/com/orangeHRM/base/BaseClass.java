package com.orangeHRM.base;

import com.orangeHRM.actiondriver.ActionDriver;
import com.orangeHRM.utilities.ExtentManager;
import com.orangeHRM.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {
    protected static Properties properties;
    //protected static WebDriver driver;
    //private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    protected ThreadLocal<SoftAssert> softAssertThreadLocal = ThreadLocal.withInitial(SoftAssert::new);

    // Method to get SoftAssert instance for the current thread
    public SoftAssert getSoftAssert() {
        return softAssertThreadLocal.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load the configution file
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        properties.load(fis);
        logger.info("config.properties file loaded");

        // Start the extent report
        //ExtentManager.getReporter(); -- this line is moved to TestListener class
        //logger.info("Extent report initialized");
    }

    private synchronized void launchBrowser() {
        // initialize the webdriver based on browser defined in config.propertiees file
        String browser = properties.getProperty("browser");
        boolean headless = Boolean.parseBoolean(properties.getProperty("headlessMode", "false"));

        if (browser.equalsIgnoreCase("chrome")) {
            org.openqa.selenium.chrome.ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
            if (headless) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
            }
            options.addArguments("--start-maximized");
            options.addArguments("--disable-blink-features=AutomationControlled");
            driver.set(new ChromeDriver(options));
            logger.info("chromedriver initialized (headless: " + headless + ")");
            ExtentManager.registerDriver(getDriver());

        } else if (browser.equalsIgnoreCase("firefox")) {
            org.openqa.selenium.firefox.FirefoxOptions options = new org.openqa.selenium.firefox.FirefoxOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            driver.set(new FirefoxDriver(options));
            logger.info("firefoxdriver initialized (headless: " + headless + ")");
            ExtentManager.registerDriver(getDriver());

        } else if (browser.equalsIgnoreCase("edge")) {
            org.openqa.selenium.edge.EdgeOptions options = new org.openqa.selenium.edge.EdgeOptions();
            if (headless) {
                options.addArguments("--headless");
            }
            driver.set(new EdgeDriver(options));
            logger.info("edgedriver initialized (headless: " + headless + ")");
            ExtentManager.registerDriver(getDriver());

        } else {
            throw new IllegalArgumentException("Browser Not Supported:" + browser);
        }

    }

    private void configureBrowser() {
        // configure browser setting such as, implicit wait maximaze, navegato to
        // implicit wait
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait"));
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        logger.info("Implicit wait applied for: " + implicitWait + " seconds");

        // maximize the driver
        //driver.manage().window().maximize();
        getDriver().manage().window().maximize();
        logger.info("Browser window maximized");

        // navigate to url
        try {
            //driver.get(properties.getProperty("url"));
            getDriver().get(properties.getProperty("url"));
            logger.info("Navigated to URL: " + properties.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to naviga to the URL: "+e.getMessage());
        }

    }

    @BeforeMethod
    public synchronized void setup() throws IOException {
        System.out.println("Setting up webdriver for: " +this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();

        logger.info("Webdriver initialized and browser Maximized");
        /*
        logger.trace("this is a trace message");
        logger.error("this is an error message");
        logger.debug("this is a debug message");
        logger.fatal("this is a fatal message");
        logger.warn("this is a warn message");
        logger.info("this is an info message");

         */

        /*
        // initialize the action driver only once
        if(actionDriver == null){
            actionDriver = new ActionDriver(driver);
            logger.info("ActionDriver instance is created" + Thread.currentThread().getId());
        }

         */
        //Initialize actionDriver for each thread
        actionDriver.set(new  ActionDriver(getDriver()));
        logger.info("ActionDriver initialized is created for thread: " + Thread.currentThread().getId());
    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                //driver.quit();
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("unable to quit the driver: " +e.getMessage());
            }
        }
        logger.info("Webdriver instance is closed.");
        driver.remove();
        actionDriver.remove();
        //driver=null;
        //actionDriver=null;
        //ExtentManager.endTest(); -- this line is moved to TestListener class
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
        if(driver.get()==null){
            System.out.println("webdriver is not initialized");
            throw  new IllegalStateException("Webdriver ins not initialized");
        }
        return driver.get();
    }

    // getter method for actiondriver
    public static ActionDriver getActionDriver() {
        if(actionDriver.get()==null){
            System.out.println("actiondriver is not initialized");
            throw  new IllegalStateException("actiondriver ins not initialized");
        }
        return actionDriver.get();
    }

    // driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver){
        //BaseClass.driver = driver;
        //this.driver.get() = driver;
        BaseClass.driver = driver;
    }

    // static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
