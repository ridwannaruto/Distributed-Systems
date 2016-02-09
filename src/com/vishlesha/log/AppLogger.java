package com.vishlesha.log;

import java.io.IOException;
import java.util.logging.*;


/**
 * Created by ridwan on 1/19/16.
 */
public class AppLogger {

    static private FileHandler networkLog;

    public static final String APP_LOGGER_NAME = "app_log";
    public static final String NETWORK_LOGGER_NAME = "network_log";

    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger networkLogger = Logger.getLogger(NETWORK_LOGGER_NAME);
        Logger appLogger = Logger.getLogger(APP_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");

        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        networkLogger.setLevel(Level.ALL);
        String APP_LOG_FILE_NAME = "network.log";
        networkLog = new FileHandler(APP_LOG_FILE_NAME);
        appLogger.setLevel(Level.ALL);
        String NETWORK_LOG_FILE_NAME = "app.log";
        FileHandler appLog = new FileHandler(NETWORK_LOG_FILE_NAME);

        // create a TXT formatter
        SimpleFormatter formatterTxt = new SimpleFormatter();
        networkLog.setFormatter(formatterTxt);
        appLog.setFormatter(formatterTxt);

        networkLogger.addHandler(networkLog);
        appLogger.addHandler(appLog);
    }
}