package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


/**
 * ClassName: BaseClass
 * Package: com.orangehrm.base
 * Description:
 *
 * @Author Grace
 * @Create 26/6/2025 2:50 pm
 * Version 1.0
 */
public class BaseClass {
    protected static Properties prop;  // add static to make the prop access through class, not depending on object
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    // Another way to initialized softAssert instance:
    // Whenever softAssert.get() is called for the first time on a thread,
    // withInitial() automatically create a new SoftAssert object for that thread.
    // So it removes the need for @BeforeMethod: softAssertThreadLocal.set(new SoftAssert());
    private static ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
    // private static ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>(); need to add:softAssertThreadLocal.set(new SoftAssert());

    // Ask LoggerManager to give a logger for this class
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file loading");

        // Start the Extent Report
        // ExtentManager.getReporter(); -- This has been implemented in Test Listener
    }

    @BeforeMethod
    public void setUp() throws IOException {
        logger.info("Setting up WebDriver for: " + this.getClass().getSimpleName());
//        System.out.println("Setting up WebDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configerBrowser();
        staticWait(3);

        logger.info("WebDriver created in baseclass: " + driver + " for Thread --> " + Thread.currentThread().getId());

        // Initialize actionDriver for the current thread
        actionDriver.set(new ActionDriver(getDriver(), prop));
        logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId());
        softAssert.set(new SoftAssert());
    }

    // Initialize the WebDriver based on driver defined in config.properties
    public void launchBrowser(){
        String browser = prop.getProperty("browser").toLowerCase();
            switch (browser) {
                case "chrome":

                    // Create ChromeOptions -make browser run more efficiently, quietly, more CI/CD–friendly (i.e. Jenkins, GitHub Actions, etc.)
                    ChromeOptions options = new ChromeOptions();
                    // Runs in headless mode (no browser UI), Disables GPU usage (important for headless to avoid rendering issues),
                    // Sets the browser window size (helps avoid layout issues that appear in small default headless sizes)
                    options.addArguments("--headless", "--disable-gpu", "--disable-notifications", "--window-size=1920,1080");

                    // options.addArguments("--disable-notifications"); Disable browser notification -- Stops browser notifications (like pop-ups) from interrupting tests. Disabling extras = fewer interruptions
                    //  options.addArguments("--no-sandbox");  Required for some CI environment to avoid permission errors.
                    // options.addArguments("--disable-dev-shm-usage"); Resolve memory/resource issues when running Chrome in Docker or CI/CD pipelines.

                    // driver = new ChromeDriver();
                    driver.set(new ChromeDriver(options)); // new changes as per Thread
                    ExtentManager.registerDriver(getDriver());
                    logger.info("chrome driver instance is created.");
                    break;
                case "firefox":
                    driver.set(new FirefoxDriver());
                    ExtentManager.registerDriver(getDriver());
                    logger.info("firefox driver instance is created");
                    break;
                case "edge":
                    driver.set(new EdgeDriver());
                    ExtentManager.registerDriver(getDriver());
                    logger.info("edge driver instance is created");
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
    }

    // Configure browser setting such as implicit wait, maximize browser and navigate to URL
    private void configerBrowser(){

            // Implicit Wait - global wait
            int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

            // Maximize the browser
            getDriver().manage().window().maximize();

            // Navigate to the URL
            try{
                String url = prop.getProperty("url");
                getDriver().get(url);
            }catch(Exception e){
                logger.error("Failed to navigate to URL. " + e.getMessage() );
            }
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            try {
                logger.info("WebDriver Application to be quit : " + getDriver() + "for Thread--> " + Thread.currentThread().getId());
                getDriver().quit();
            } catch (Exception e) {
                logger.error("Unable to quit." + e.getMessage());
            }
        }
        logger.info("WebDriver instance is successfully closed");
        driver.remove();
        actionDriver.remove();
        softAssert.remove();   // Clean up the ThreadLocal
        // ExtentManager.endTestLog(); -- This has been implemented in TestListener class
        logger.info("Driver : " + driver);
        logger.info("ActionDriver : " + actionDriver);
        logger.info("Final log message before teardown...");
    }

    //Prop getter method
    public static Properties getProp(){
        return prop;
    }


    // Driver getter method
    public static WebDriver getDriver(){
        if (driver.get()==null){
            logger.info("Driver is not yet initialized");
            throw new IllegalArgumentException("Driver is not yet initialized");
        }
        return driver.get();
    }

    // Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver){
        this.driver = driver;
    }

    // ActionDriver getter method
    public static ActionDriver getActionDriver(){
        if(actionDriver.get()==null){
            logger.info("ActionDriver is not yet initialized.");
        }
        return actionDriver.get();
    }

    // SoftAssert getter method
    public static SoftAssert getSoftAssert(){
        if(softAssert.get()==null){
            logger.info("SoftAssert is not yet initialized.");
        }
        return softAssert.get();
    }

    // Helper method to log test details for all tests
    public void logStep(String message) {
        LoggerManager.getLogger(getClass()).info(message);
        ExtentManager.logStep(message);
    }

    // Static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

}
