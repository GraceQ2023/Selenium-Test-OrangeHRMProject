package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

/**
 * ClassName: DBVerificationTest
 * Package: com.orangehrm.tests
 * Description:
 *
 * @Author Grace
 * @Create 21/7/2025 10:13 pm
 * Version 1.0
 */
public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;
    public static final Logger logger = LoggerManager.getLogger(DBVerificationTest.class);

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getActionDriver());
        homePage = new HomePage(getActionDriver());
        logger.info("relevant pages are setup");
    }

    @Test
    public void verifyEmployeeNameFromDB(){

        SoftAssert softAssert = new SoftAssert();
        logStep("Logging with Admin Credentials.");
        loginPage.login(prop.getProperty("username"),prop.getProperty("password") );
        logStep("Click on PIM tab and search employee");
        homePage.employeeSearch("Macy");
        logStep("Get the Employee name from DB");
        String employeeId = "2";
        // Fetch the employee data into a map
        Map<String, String> employeeDetailsFromDB = DBConnection.getEmployeeDetails(employeeId);

        String employeeFirstNameFromDB = employeeDetailsFromDB.get("firstName");
        String employeeLastNameFromDB = employeeDetailsFromDB.get("lastName");
//        String employeeFirstAndMiddleNameFromDB = (employeeFirstNameFromDB + " " + employeeMiddleNameFromDB).trim();

        // Validation for first name
        logStep("Verify employee's first name.");
        getSoftAssert().assertTrue(homePage.verifyEmployeeFirstName(employeeFirstNameFromDB), "First name mismatch. Expected: " + employeeFirstNameFromDB);

        // Validation for last name
        logStep("Verify employee's last name.");
        getSoftAssert().assertTrue(homePage.verifyEmployeeLastName(employeeLastNameFromDB), "Last name mismatch. Expected: " + employeeLastNameFromDB);

        logStep("Database validation completed.");

        getSoftAssert().assertAll();  // Evaluate all assertions
    }

}
