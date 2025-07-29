package com.orangehrm.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * ClassName: RetryAnalyzer
 * Package: com.orangehrm.utils
 * Description:
 *
 * @Author Grace
 * @Create 26/7/2025 10:02 pm
 * Version 1.0
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0; // number of retreis
    private static final int maxRetry = 2; // max no. of retry


    @Override
    public boolean retry(ITestResult iTestResult) {
        if(retryCount < maxRetry){
            retryCount++;
            return true; // Retry the test
        }
        return false;
    }
}
