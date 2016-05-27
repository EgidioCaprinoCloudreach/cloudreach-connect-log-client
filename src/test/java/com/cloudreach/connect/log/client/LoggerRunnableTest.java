package com.cloudreach.connect.log.client;

import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

public class LoggerRunnableTest {

    private Runnable loggerRunnable;
    private String appKey;
    private long level;
    private String message;

    @Before
    public void givenLoggerRunnableAppKeyLevelMessage() {
        appKey = "4d8921e5-7431-463f-87bd-d29565eed90a";
        level = Level.SEVERE.intValue();
        message = getClass().getName();
        loggerRunnable = new LoggerRunnable(appKey, level, message);
    }

    @Test
    public void whenRanThenNoExceptionIsThrown() {
        System.setProperty(CloudreachConnectLogger.SYSTEM_PROPERTY_URL, "https://cloudreach-connect-demo.herokuapp.com/ccsvc/54/v1.0/log-entry");
        loggerRunnable.run();
    }

}
