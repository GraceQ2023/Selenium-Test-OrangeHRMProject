package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * ClassName: LoggerManager
 * Package: com.orangehrm.utilities
 * Description:
 *
 * @Author Grace
 * @Create 7/7/2025 12:22 pm
 * Version 1.0
 */
public class LoggerManager {
    // Method to return a Logger instance for the provided class
    public static Logger getLogger(Class<?> clazz){
        return LogManager.getLogger(clazz);
    }

}


