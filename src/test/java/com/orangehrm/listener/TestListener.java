package com.orangehrm.listener;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utils.RetryAnalyzer;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * ClassName: TestListener
 * Package: com.orangehrm.listener
 * Description:
 *
 * @Author Grace
 * @Create 15/7/2025 12:20 pm
 * Version 1.0
 */
public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    // Triggered when a test starts
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();
        // Start logging in Extent Report
        ExtentManager.startTestEntry(testName, className);
        ExtentManager.logStep("Test started --> " + testName);
    }

    // Triggered when a Test success
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logStep("Test passed: " + testName);

        if(!result.getTestClass().getName().toLowerCase().contains("api")){
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Test passed successfully.", "Test End: " + testName + " - ✓ Test Passed");
        }else{
            ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✓ Test Passed");
        }
    }

    // Triggered when a test failed
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String failureMessage = result.getThrowable().getMessage();
        ExtentManager.logStep(failureMessage);

        if(!result.getTestClass().getName().toLowerCase().contains("api")){
            ExtentManager.logFailure(BaseClass.getDriver(),"Test failed.", "Test End: " + testName + " - x Test Failed");
        }else{
            ExtentManager.logFailureAPI("Test End: " + testName + " - x Test Failed");
        }
    }

    // Trigger when a test skips
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test skipped: " + testName);
    }

    // Triggered when a suite starts
    @Override
    public void onStart(ITestContext context) {
        // Initialize the Extent Reports
        ExtentManager.getReporter();
    }

    // Triggered when the suite ends
    @Override
    public void onFinish(ITestContext context) {
        // Flush the Extent Reports
        ExtentManager.endTestLog();
    }
}
