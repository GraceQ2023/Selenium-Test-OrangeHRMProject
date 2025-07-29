package com.orangehrm.tests;

import com.orangehrm.base.BaseClass;
import com.orangehrm.dataprovider.DataProviders;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * ClassName: HomePageTest
 * Package: com.orangehrm.tests
 * Description:
 *
 * @Author Grace
 * @Create 2/7/2025 11:06 pm
 * Version 1.0
 */
public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    public static final Logger logger = LoggerManager.getLogger(HomePageTest.class);

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getActionDriver());
        homePage = new HomePage(getActionDriver());
        logger.info("relevant pages are setup");
    }

    @Test(dataProvider= "validLoginData", dataProviderClass = DataProviders.class)
    public void verifyHRMOrangeLogo(String userName, String password){
        // ExtentManager.startTestEntry("Home Page Verify Logo Test", this.getClass().getSimpleName()); --> This has been implemented in TestListener class
        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login(userName, password);
        ExtentManager.logStep("Verifying logo is visible or not");
        Assert.assertTrue(homePage.verifyOrangeHRMlogo(), "Logo is not visible");
        ExtentManager.logStep("Validation successful.");
        homePage.logout();
        ExtentManager.logStep("Log out successfully.");
        staticWait(3);
    }


//    @Test
//    public void verifyLogout(){
//        loginPage.login("Admin", "admin123");
//        homePage.logout();
//        Assert.assertTrue(loginPage.verifyLoginPageTitle("Login"));
//        staticWait(3);
//    }

}
