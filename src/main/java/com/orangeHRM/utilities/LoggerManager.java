package com.orangeHRM.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {


  /**  * Get a logger instance for the specified class.
     *
     * @param clazz the class for which to get the logger
     * @return the logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger();
    }
}
