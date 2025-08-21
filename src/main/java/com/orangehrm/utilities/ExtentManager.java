package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ExtentManager
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 10/7/2025 2:33 pm
 * Version 1.0
 */
public class ExtentManager {

    private static ExtentReports extent;

    // Each test running in parallel (in its own thread) has its own ExtentTest object (report section).
    // Without ThreadLocal, tests might overwrite each other’s logs.
    private static ThreadLocal<ExtentTest> testLog = new ThreadLocal<>();

    // The map stores WebDrivers for each Thread ID
    private static Map<Long, WebDriver> driverMap= new HashMap<>();

    // Initialized Extent Report
    public static ExtentReports getReporter(){

        // Create a report file, set title, theme, etc.
        // Ensures that the ExtentReports object is created only once, and used across all tests.
        if(extent==null){
            String reportPath = System.getProperty("user.dir") +"/src/test/resources/extentreport/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);  // Generate content of extent report
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("OrangeHRM Report");
            spark.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(spark); // Link ExtentReports to the Spark (HTML) reporter so logs can be saved to the file

            // Adding system information
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java version", System.getProperty("java.version"));
            extent.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extent;
    }


    // Create a new test entry/section in the report
    public static ExtentTest startTestEntry(String testName, String className){
        // Creates a new ExtentTest (test log) with the test name, e.g.  "Valid Login Test"
        ExtentTest newTest = getReporter().createTest(testName)
                .assignCategory(Thread.currentThread().getName())  // Assign the thread id as a category for better grouping
                .assignCategory(className);
        testLog.set(newTest); // Store this test log in the ThreadLocal for the current thread
        return newTest;
    }

    // End a test
    public static void endTestLog(){
        // Appends the HTML file with all the ended tests, i.e. after tests finish, this writes all collected logs into the .html file.
        getReporter().flush();  // Finalizes the report and writes everything to disk
    }

    // Get the test log for the current thread
    // It returns the ExtentTest object (test log) set via startTestEntry(...) earlier for that thread, i.e., the "Valid Login Test" section of the report.
    public static ExtentTest getTestLog(){
        return testLog.get();
    }

    // Method to get the name of current test
    // Return the name you passed to createTest("..."), e.g., "Valid Login Test"
    public static String getTestName(){
        ExtentTest currentTestLog = getTestLog();
        if(currentTestLog != null){
            return currentTestLog.getModel().getName();
        } else{
            return "No test is currently active";
        }
    }

    //Log a step
    public static void logStep(String logMessage){
        getTestLog().info(logMessage);
    }

    //Log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage){
        getTestLog().pass(logMessage);
        attachScreenshot(driver, screenShotMessage);
    }

    //Log a step validation for API
    public static void logStepValidationForAPI(String logMessage){
        getTestLog().pass(logMessage);
    }

    // Log a Failure
    public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage){
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTestLog().fail(colorMessage);
        attachScreenshot(driver, screenShotMessage);
    }

    // Log a Failure for API
    public static void logFailureAPI(String logMessage){
        String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
        getTestLog().fail(colorMessage);
    }


    // Log a skip
    public static void logSkip(String logMessage){
        String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
        getTestLog().skip(colorMessage);
    }

    // Take a screenshot with date and time in the file
    public static String takeScreenShot(WebDriver driver, String screenShotName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //Format data and time for file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        //Saving the screenshot to a file
        String desPath = System.getProperty("user.dir") +"/src/test/resources/screenshots/" + screenShotName + "_" + timeStamp + ".png";

        File finalPath = new File(desPath);
        FileUtils.copyFile(src, finalPath);  // difference between "throws IOException at method" and try-catch

        // Convert screenshot to Base64 String for embedding in the report
        String base64Format = convertToBase64(finalPath); //???? why he put src
        return base64Format; ///??????
    }

    // Convert screenshot to Base64 format
    public static String convertToBase64(File screenShotFile){
        String base64Format="";
        try {
            //Read the file content into a byte array
            byte[] fileContent = FileUtils.readFileToByteArray(screenShotFile);
            //Convert the byte array to Base64 String
            base64Format = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return base64Format;
    }

    // Attach screenshot to report using Base64
    public static void attachScreenshot(WebDriver driver, String message){
        try {
            String screenShotBase64 = takeScreenShot(driver, getTestName());
            getTestLog().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
        } catch (IOException e) {
            getTestLog().fail("Failed"); //????? why not use logFailure()?
            e.printStackTrace();
        }
    }

    // Register WebDriver for current Thread
    // So, if you want to take a screenshot, you know which WebDriver belongs to which thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(), driver);
    }


}
