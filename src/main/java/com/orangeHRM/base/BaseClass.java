package com.orangeHRM.base;

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

    @BeforeSuite
    public void loadConfig() throws IOException {
        // Load the configution file
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        properties.load(fis);
    }

    private void launchBrowser() {
        // initialize the webdriver based on browser defined in config.propertiees file
        String browser = properties.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();

        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();

        } else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();

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
    }

    // getter method for prop
    public static Properties getProperties(){
        return properties;
    }

    // driver getter method
    public WebDriver getDriver(){
        return driver;

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
