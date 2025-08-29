package com.orangehrm.actiondriver;

import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.openqa.selenium.interactions.Actions;

/**
 * ClassName: ActionDriver
 * Package: com.orangehrm.actiondriver
 * Description:
 *
 * @Author Grace
 * @Create 1/7/2025 11:06 am
 * Version 1.0
 */
public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;
    private Properties prop;
    public static final Logger logger = LoggerManager.getLogger(ActionDriver.class);


    public ActionDriver(WebDriver driverFromBase, Properties propFromBase){ // "inject" the WebDriver into ActionDriver so it can use it internally.
        this.driver = driverFromBase; // a copy of the key given to the ActionDriver, store it into the ActionDriver's own variable, so it can open the same door (browser)
        this.prop = propFromBase;
        int explicitWait = Integer.parseInt(prop.getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    // Method to click element with scroll + wait
    public void click(By by){
        String elementDescription = getElementDescription(by);
        try {
            scrollToElement(by);  // Scrolls the page so the element is visible, Brings it into view
            applyBorder(by, "green");
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("clicked an element: " + elementDescription);  //?? same as logger.info()
            logger.info("click on an element--> " + elementDescription);
        } catch (Exception e) {
            applyBorder(by, "red");
            System.out.println("unable to click: " + e.getMessage());
            ExtentManager.logFailure(driver, "Unable to click element: ", elementDescription+"_unable to click");
            logger.error("unable to click element: " + elementDescription);
        }
    }

    // Method to enter text into input filed
    public void enterText(By by, String text){
        try {
            scrollToElement(by);  // helper to scroll to the field you want, you move your screen to bring the field into view
            applyBorder(by, "green");
            waitForElementToBeVisible(by); // helper to wait for it to appear
            WebElement element = driver.findElement(by); // actual locator call — Selenium grabs the element for interaction
            element.clear();
            element.sendKeys(text);
            logger.info("entered text on " + getElementDescription(by) + "--> " + text);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("unable to enter input: " + e.getMessage());
        }
    }

    // Method to get text from an input field
    public String getText(By by){
        try {
            scrollToElement(by);
            applyBorder(by, "green");
            waitForElementToBeVisible(by);
            logger.info("Element is displayed: " + getElementDescription(by));
            return driver.findElement(by).getText();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("unable to get the text: " + e.getMessage());
            return "";
        }
    }

    // Method to compare two text
    public boolean compareText(By by, String expectedText){
        try {
            scrollToElement(by);
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)){
                applyBorder(by, "green");
                logger.info("Test are matching: " + actualText + " equals " + expectedText);
                ExtentManager.logStepWithScreenshot(driver, "Text verification succeed", "Text verified successfully_"+ actualText + " equals " + expectedText);
                return true;
            }else{
                applyBorder(by, "red");
                logger.error("Test are not matching: " + actualText + " not equals " + expectedText);
                ExtentManager.logFailure(driver, "Text comparison failed", "Text comparison failed "+ actualText + " not equals " + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("unable to compare text");
            return false;
        }
    }

    // Method to check if element is displayed, helps check and return true/false — often used in assertions
    public boolean isDisplayed(By by){
        try {
            scrollToElement(by);
            applyBorder(by, "green");
            waitForElementToBeVisible(by);
            logger.info("Element is displayed " + getElementDescription(by));
            ExtentManager.logStepWithScreenshot(driver, "Element is displayed.", "Element is displayed: "+ getElementDescription(by));
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("element is not displayed: " + e.getMessage());
            ExtentManager.logFailure(driver, "Element is displayed.", "Elementis displayed: " + getElementDescription(by));
            return false;
        }
    }

    // Wait for the page to laod
    public void waitForPageLoad(int timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec))
                    .until(WebDriver ->(JavascriptExecutor)WebDriver) // Waits until the given condition becomes true.
                    .executeScript("return document.readyState").equals("complete");
            logger.error("page load successfully");
        } catch (Exception e) {
            logger.error("page load failed: " + e.getMessage());
        }
    }

    // Fluent Wait until the element's text matches expected value
    public boolean fluentWaitForTextToBe(By by, String expectedText, int timeoutInSeconds) {
        try {
            Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            return fluentWait.until(d -> {
                WebElement element = d.findElement(by);
                String actualText = element.getText().trim();
                return actualText.equals(expectedText);
            });
        } catch (TimeoutException e) {
            logger.error("FluentWait timeout: Text did not match '" + expectedText + "' in time.");
            return false;
        } catch (Exception e) {
            logger.error("Error in waitForTextToBe: " + e.getMessage());
            return false;
        }
    }

    // Scroll to an element
    public void scrollToElement(By by){
        try {
            WebElement element = driver.findElement(by);
            JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("arguments[0].scrollIntoView(true);", element); ????
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception e) {
            logger.error("scroll failed: " + e.getMessage());
        }

    }

    // Wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("element is not clickable: " + e.getMessage());
        }
    }

    // Wait for element to be Visible, helps wait for visibility
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("element is not visible: " + e.getMessage());
        }
    }

    // Method to get the description of an element using By locator
    public String getElementDescription(By by) {
        //check for null driver or locator to avoid NULLPointer Exception
        if (driver == null)
            return "driver is null";
        if (by == null)
            return "locator is null";

        try {
            // find the element using locator by
            WebElement element = driver.findElement(by);

            // Get element attributes
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeHolder = element.getDomAttribute("placeholder");

            // Return the description based on element attribute
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with id: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 10);
            } else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            } else if (isNotEmpty(placeHolder)) {
                return "Element with placeholder: " + placeHolder;
            }
        } catch (Exception e) {
            logger.error("Unable to describe the element" + e.getMessage());
        }
        return "Unable to describe the element";
    }

    // Utility method to check a String is not NULL or empty
    private boolean isNotEmpty(String value){
        return value != null && !value.isEmpty();
    }

    // Utility method to truncate long String
    private String truncate(String value, int maxLength){
        if(value==null || value.length()<=maxLength){
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    // Utility method to Border an element
    public void applyBorder (By by, String color){
        try {
            // Locate the element
            WebElement element = driver.findElement(by);
            // Apply border
            String script = "arguments[0].style.border='3px solid "+ color+ "'";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(script, element);
            logger.info("Applied the border with color: " + color + " to element -> " + getElementDescription(by));
        } catch (Exception e) {
            logger.warn("Failed to apply the border to element:" + getElementDescription(by), e);
        }
    }

    // Select Methods

    // Method to select a dropdown by visible text
    public void selectByVisibleText (By by, String value){
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByVisibleText(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown value: " + value, e);
        }
    }

    // Method to select a dropdown by value
    public void selectByValue (By by, String value){
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByValue(value);
            applyBorder(by, "green");
            logger.info("Selected dropdown value: " + value);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown value: " + value,e );
        }
    }

    // Method to select a dropdown by index
    public void selectByIndex (By by, int index){
        try {
            WebElement element = driver.findElement(by);
            new Select(element).selectByIndex(index);
            applyBorder(by, "green");
            logger.info("Selected dropdown index: " + index);
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to select dropdown index: " + index, e );
        }
    }

    // Method to get all options from a dropdown
    public List<String> getDropdownOptions(By by){

        List<String> optionsList = new ArrayList<>();

        try {
            WebElement dropdownElement = driver.findElement(by);
            Select select = new Select(dropdownElement);
            for(WebElement option : select.getOptions()){
                optionsList.add(option.getText());
            }
            applyBorder(by, "green");
            logger.info("Retrived dropdown options for:  " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to get dropdown options " + e.getMessage() );
        }
        return optionsList;
    }

      // JavaScript utility Methods

     // Method to click using JavaScript
    public void clickUsingJS(By by){
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
            applyBorder(by, "green");
            logger.info("Clicked element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            applyBorder(by, "red");
            logger.error("Unable to click using JavaScript", e);
        }
    }

    // Method to scroll to the bottom of the page
    public void scrollToBottom(By by){
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor)driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logger.info("Scrolled to the bottom of the page");
    }

    // Method to highlight an element using JavaScript
    public void highlightElementJS(By by){
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
            logger.info("Highlighted element using JavaScript: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to highlight element using JavaScript", e);
        }

    }

    // Window and Frame Handling
    // Method to switch between browser windows
    public void switchToWindow(String windowTitle){
        try {
            Set<String> windows = driver.getWindowHandles();
            for(String window : windows){
                driver.switchTo().window(window);
                if(driver.getTitle().equals(windowTitle)){
                    logger.info("Switched to window: " + windowTitle);
                    return;
                }
            }
            logger.warn("Window with title " + windowTitle + " not found");
        } catch (Exception e) {
            logger.error("Unable to switch window", e);
        }
    }

    // Method to switch to an iframe
    public void switchToFrame(By by) {
        try {
            driver.switchTo().frame(driver.findElement(by));
            logger.info("Switched to iframe: " + getElementDescription(by));
        } catch (Exception e) {
            logger.error("Unable to switch to iframe", e);
        }
    }


    // ===================== Alert Handling =====================

    // Method to accept an alert popup
    public void acceptAlert() {
        try {
            driver.switchTo().alert().accept();
            logger.info("Alert accepted.");
        } catch (Exception e) {
            logger.error("No alert found to accept", e);
        }
    }

    // Method to dismiss an alert popup
    public void dismissAlert() {
        try {
            driver.switchTo().alert().dismiss();
            logger.info("Alert dismissed.");
        } catch (Exception e) {
            logger.error("No alert found to dismiss", e);
        }
    }

    // Method to get alert text
    public String getAlertText() {
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("No alert text found", e);
            return "";
        }
    }


}
