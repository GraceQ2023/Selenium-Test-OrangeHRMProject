package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.dataprovider.DataProviders;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * ClassName: LoginPageTest
 * Package: com.orangehrm.tests
 * Description:
 *
 * @Author Grace
 * @Create 2/7/2025 3:43 pm
 * Version 1.0
 */
public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;
    public static final Logger logger = LogManager.getLogger(LoginPageTest.class);

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getActionDriver());
        homePage = new HomePage(getActionDriver());
        logger.info("pages objects are set up");
    }

    @Test(dataProvider= "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String userName, String password){
        // ExtentManager.startTestEntry("Valid Login Test", this.getClass().getSimpleName()); --> This has been implemented in TestListener class
        System.out.println("Running testMethod1-valid login on thread: " + Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(userName, password);
        ExtentManager.logStep("Verifying Admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisible(), "Admin tab should be visible after successfully login");
        ExtentManager.logStep("Validation successful.");
        homePage.logout();
        ExtentManager.logStep("logged out successfully.");
        staticWait(2);
    }

    @Test(dataProvider= "invalidLoginData", dataProviderClass = DataProviders.class)
    public void verifyInvalidLoginTest(String userName, String password){
        // ExtentManager.startTestEntry("In-valid Login Test", this.getClass().getSimpleName()); --> This has been implemented in TestListener class
        System.out.println("Running testMethod2-invalid login on thread: " + Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(userName, password);
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage), "Test failed: invalid error message");
        ExtentManager.logStep("Validation successful.");
    }




}
