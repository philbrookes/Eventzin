/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.logger;

import org.apache.log4j.Priority;

/**
 *
 * @author phil
 */
public class Logger {
    public static final int INFO = Priority.INFO_INT;
    public static final int DEBUG = Priority.DEBUG_INT;
    public static final int WARN = Priority.WARN_INT;
    public static final int ERROR = Priority.ERROR_INT;
    public static final int FATAL = Priority.FATAL_INT;

    public static void debug(String Category, String Message){
        log(Category, Message, DEBUG);
    }

    public static void info(String Category, String Message){
        log(Category, Message, INFO);
    }

    public static void warn(String Category, String Message){
        log(Category, Message, WARN);
    }

    public static void error(String Category, String Message){
        log(Category, Message, ERROR);
    }

    public static void fatal(String Category, String Message){
        log(Category, Message, FATAL);
    }

    private static void log(String Category, String Message, int Level){
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Category);
        if(logger.isEnabledFor(Priority.toPriority(Level))){
            switch(Level){
                case INFO:
                    logger.info(Message);
                    break;
                case DEBUG:
                    logger.debug(Message);
                    break;
                case WARN:
                    logger.warn(Message);
                    break;
                case ERROR:
                    logger.error(Message);
                    break;
                case FATAL:
                    logger.fatal(Message);
                    break;
            }
        }
    }
}
