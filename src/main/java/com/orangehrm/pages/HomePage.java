package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

/**
 * ClassName: HomePage
 * Package: com.orangehrm.pages
 * Description:
 *
 * @Author Grace
 * @Create 2/7/2025 2:44 pm
 * Version 1.0
 */
public class HomePage {
    private ActionDriver actionDriver;
    // Defining locators using By class
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIDBtn = By.className("oxd-userdropdown-name");
    private By logoutBtn = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");
    private By pimTab = By.xpath("//span[text()='PIM']");
    private By employSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchBtn = By.xpath("//button[@type='submit']");
    private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]/div");
    private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");
    public static final Logger logger = LoggerManager.getLogger(HomePage.class);

    // HomePage relies on ActionDriver to perform actions.
    // Whenever creates a HomePage object, make sure it has access to an ActionDriver that uses the same WebDriver and properties file
    // everything to use the same driver instance from BaseClass, not create a new one.
    // It ensures that as soon as you create a HomePage, the actionDriver is ready to use.
    public HomePage(ActionDriver actionDriver){
        this.actionDriver = actionDriver;
        logger.info("HomePage ActionDriver: " + actionDriver);
    }

    // Method to verify if Admin tab is visible
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    // Method to verify if logo is visible
    public boolean verifyOrangeHRMlogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    // Employee Search
    public void employeeSearch(String value){
        actionDriver.click(pimTab);
        actionDriver.enterText(employSearch, value);
        actionDriver.click(searchBtn);

        // Fluent Wait until the cell text matches the search value
        boolean isTextMatched = actionDriver.fluentWaitForTextToBe(emplFirstAndMiddleName, value, 10);

        if (!isTextMatched) {
            logger.warn("Search result may not be updated yet for employee: " + value);
        }
        // Now scroll to the correct element after confirming it's loaded
        actionDriver.scrollToElement(emplFirstAndMiddleName);
    }

    // Verify employee first and middle name
    public boolean verifyEmployeeFirstName(String employeeFirstNameFromDB){
        return actionDriver.compareText(emplFirstAndMiddleName, employeeFirstNameFromDB);
    }

    // Verify employee last name
    public boolean verifyEmployeeLastName(String employeeLastNameFromDB){
        return actionDriver.compareText(emplLastName, employeeLastNameFromDB);
    }


    // Method to perform logout operation
    public void logout(){
        actionDriver.click(userIDBtn);
        actionDriver.click(logoutBtn);
    }







}
