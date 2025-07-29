package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utils.ApiUtility;
import com.orangehrm.utils.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ClassName: APITest
 * Package: com.orangehrm.tests
 * Description:
 *
 * @Author Grace
 * @Create 25/7/2025 10:20 pm
 * Version 1.0
 */
public class APITest {

    private static String baseURL = "https://jsonplaceholder.typicode.com";

    SoftAssert softAssert = new SoftAssert();

//    @Test (retryAnalyzer = RetryAnalyzer.class)
    @Test
    public void verifyGetUserAPI(){
        // Step1: Define API endpoint
        String endPoint = baseURL + "/users/1";
        ExtentManager.logStep("API Endpoint: " + endPoint);

        // Step2: Send GET Request
        ExtentManager.logStep("Sending GET Request to API endpoint");
        Response response = ApiUtility.sendGetRequest(endPoint);

        // Step3: validate status code
        ExtentManager.logStep("Validating API Response status code");
        boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
        softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected.");

        if(isStatusCodeValid){
            ExtentManager.logStepValidationForAPI("Status code validation pass");
        }else{
            ExtentManager.logFailureAPI("Status code validation failed");
        }

        // Step4: validate user name
        ExtentManager.logStep("Validating response body for user name");
        String userName = ApiUtility.getJsonValue(response, "username");
        boolean isUserNameValid = "Bret".equals(userName);
        softAssert.assertTrue(isUserNameValid, "User name not valid");

        if(isUserNameValid){
            ExtentManager.logStepValidationForAPI("User name validation passed.");
        }else{
            ExtentManager.logFailureAPI("User name validation failed.");
        }

        // Step5: validate email
        ExtentManager.logStep("Validating response body for email");
        String userEmail = ApiUtility.getJsonValue(response, "email");
        boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
        softAssert.assertTrue(isEmailValid, "Email not valid");

        if(isEmailValid){
            ExtentManager.logStepValidationForAPI("Email validation passed.");
        }else{
            ExtentManager.logFailureAPI("Email validation failed.");
        }

        softAssert.assertAll();  // Evaluate all assertions
    }
}
