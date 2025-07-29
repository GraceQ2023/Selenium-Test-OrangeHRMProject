package com.orangehrm.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * ClassName: ApiUtility
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 25/7/2025 5:57 pm
 * Version 1.0
 */
public class ApiUtility {

    // Method to send the GET Request
    public static Response sendGetRequest(String endPoint){
        return RestAssured.get(endPoint);
    }

    // Method to send the POST Request
    public static Response sendPostRequest(String endPoint, String payLoad){
        return RestAssured.given().header("Content-Type", "application/json")
                           .body(payLoad)
                           .post(endPoint);
    }

    // Method to validate the response status
    public static boolean validateStatusCode(Response response, int statusCode){ // expected status code
        return response.getStatusCode() == statusCode;
    }

    // Method to extra value from JSON response
    public static String getJsonValue(Response response, String value ){ // expected status code
        return response.jsonPath().getString(value);
    }

}
