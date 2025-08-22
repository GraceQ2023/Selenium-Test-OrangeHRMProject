package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    @Parameters("browser")
    public void setUp(String browser) throws IOException {
        logger.info("Setting up WebDriver for: " + this.getClass().getSimpleName() + " on browser --> " + browser);
        launchBrowser(browser);
        configerBrowser();
        staticWait(3);

        logger.info("WebDriver created in baseclass: " + driver + " for Thread --> " + Thread.currentThread().getId());

        // Initialize actionDriver for the current thread
        actionDriver.set(new ActionDriver(getDriver(), prop));
        logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId());
        // softAssert.set(new SoftAssert());
    }

    // Initialize the WebDriver based on browser defined in config.properties
    public void launchBrowser(String browser) throws MalformedURLException {
        // String browser = prop.getProperty("browser").toLowerCase();
        boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        String gridURL = prop.getProperty("gridURL");

        if(seleniumGrid){
            if(browser.equalsIgnoreCase("chrome")){
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless", "--disable-gpu", "--disable-notifications", "--window-size=1920,1080", "--disable-dev-shm-usage", "--no-sandbox");
                driver.set(new RemoteWebDriver(new URL(gridURL), options));
                ExtentManager.registerDriver(getDriver());
            }else if(browser.equalsIgnoreCase("firefox")){
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--headless", "--disable-notifications", "--window-size=1920,1080");
                driver.set(new RemoteWebDriver(new URL(gridURL), options));
                ExtentManager.registerDriver(getDriver());
            }else if(browser.equalsIgnoreCase("edge")){
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new","--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage", "--disable-notifications", "--window-size=1920,1080", "--disable-software-rasterizer");
                driver.set(new RemoteWebDriver(new URL(gridURL), options));
                ExtentManager.registerDriver(getDriver());
            }else{
                throw new IllegalArgumentException("Browser Not Supported: " + browser);
            }
            logger.info("RemoteWebDriver instance created for Grid in headless mode.");
        } else{
            switch (browser.toLowerCase()) {
                case "chrome":
                    // Create ChromeOptions -make browser run more efficiently, quietly, more CI/CD–friendly (i.e. Jenkins, GitHub Actions, etc.)
                    // Runs in headless mode (no browser UI), Disables GPU usage (important for headless to avoid rendering issues),
                    // Sets the browser window size (helps avoid layout issues that appear in small default headless sizes)
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless", "--disable-gpu", "--disable-notifications", "--window-size=1920,1080", "--disable-dev-shm-usage", "--no-sandbox");
                    driver.set(new ChromeDriver(chromeOptions)); // new changes as per Thread
                    ExtentManager.registerDriver(getDriver());
                    logger.info("chrome driver instance is created.");
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless", "--disable-notifications", "--window-size=1920,1080", "--disable-dev-shm-usage", "--no-sandbox");
                    driver.set(new FirefoxDriver(firefoxOptions));
                    ExtentManager.registerDriver(getDriver());
                    logger.info("firefox driver instance is created");
                    break;
                case "edge":
                    //  explicitly point to the working msedgedriver binary which have already installed.
                    //  Selenium doesn’t need to guess, download, or rely on network
                    EdgeDriverService service = new EdgeDriverService.Builder()
                            .usingDriverExecutable(new File("/opt/homebrew/bin/msedgedriver"))
                            .usingAnyFreePort()
                            .build();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless", "--disable-gpu", "--disable-notifications", "--window-size=1920,1080");
                    driver.set(new EdgeDriver(service, edgeOptions));
                    ExtentManager.registerDriver(getDriver());
                    logger.info("edge driver instance is created");
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
        }
    }

    // Configure browser setting such as implicit wait, maximize browser and navigate to URL
    private void configerBrowser(){

        // Implicit Wait - global wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        // Check if the seleniumGrid setting is provided as a system property when running the tests; if not, use the value from the config file
        boolean seleniumGrid = Boolean.parseBoolean(System.getProperty("seleniumGrid", prop.getProperty("seleniumGrid")));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // Maximize the browser
        getDriver().manage().window().setSize(new Dimension(1920, 1080));

        if(seleniumGrid){
            getDriver().get(prop.getProperty("url_grid"));
        }else {
            getDriver().get(prop.getProperty("url_local"));
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
