package com.orangehrm.drivermanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

/**
 * ClassName: DriverManager
 * Package: com.orangehrm.drivermanager
 * Description:
 *
 * @Author Grace
 * @Create 6/7/2025 4:19 pm
 * Version 1.0
 */
//public class DriverManager {
//
//    protected static WebDriver driver;
//
//    public static WebDriver getDriver(){
//        if (driver==null){
//            System.out.println("Driver is not yet initialized");
//            throw new IllegalArgumentException("Driver is not yet initialized");
//        }
//        return driver;
//    }
//
//    public static void initDriver(String browser){
//
//        switch (browser) {
//            case "chrome":
//                driver = new ChromeDriver();
//                break;
//            case "firefox":
//                driver = new FirefoxDriver();
//                break;
//            case "edge":
//                driver = new EdgeDriver();
//                break;
//            default:
//                throw new IllegalArgumentException("Browser not supported: " + browser);
//        }
//    }
//
//    public static void quitDriver() {
//        if (driver != null) {
//            try {
//                driver.quit();
//                System.out.println("Application closed successfully");
//            } catch (Exception e) {
//                System.out.println("Unable to quit." + e.getMessage());
//            }
//        }
//        System.out.println("WebDriver instance is successfully closed");
//        driver=null;
//    }
//
//
//
//}
