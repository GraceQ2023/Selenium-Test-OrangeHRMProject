package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

/**
 * ClassName: LoginPage
 * Package: com.orangehrm.pages
 * Description:
 *
 * @Author Grace
 * @Create 1/7/2025 5:57 pm
 * Version 1.0
 */
public class LoginPage {
    private ActionDriver actionDriver; // → declares a variable, but it’s null, must assign it before using → usually done in the constructor

    // Define locators using By class
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginBtn = By.xpath("//button[text()=' Login ']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
    private By loginPageTitle = By.xpath("//h5[text()='Login']");

    public static final Logger logger = LoggerManager.getLogger(LoginPage.class);


    // Whenever creates a LoginPage object, make sure it has access to an ActionDriver that uses the same WebDriver and properties file
    // LoginPage relies on ActionDriver to perform actions.
    // ActionDriver needs the driver and prop to function
    // everything to use the same driver instance, not create a new one.
    // It ensures that as soon as you create a LoginPage, the actionDriver is ready to use.
    public LoginPage(ActionDriver actionDriver){
        this.actionDriver = actionDriver;
        logger.info("LoginPage ActionDriver: " + actionDriver);
    }


    // Method to perform login
    public void login(String userName, String password){
        actionDriver.enterText(userNameField, userName);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginBtn);
    }

    // Method to check if error message is displayed
    public boolean isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);
    }

    // Method to get the text from error message
    public String getErrorMessageText(){
        return actionDriver.getText(errorMessage);
    }

    // Verify if error message is correct or not
    public boolean verifyErrorMessage(String expectedErrorMessage){
        return actionDriver.compareText(errorMessage, expectedErrorMessage);
    }

    public boolean verifyLoginPageTitle(String expectedTitle){
        return actionDriver.compareText(loginPageTitle, expectedTitle);
    }
}
