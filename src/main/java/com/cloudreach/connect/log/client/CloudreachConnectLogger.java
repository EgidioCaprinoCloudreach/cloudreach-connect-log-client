package com.cloudreach.connect.log.client;

import com.cloudreach.connect.api.LogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.logging.Level;

public class CloudreachConnectLogger implements LogService {

    public static final String SYSTEM_PROPERTY_URL = CloudreachConnectLogger.class.getName() + ".url";

    private final String appKey;

    public CloudreachConnectLogger(String appKey) {
        if (appKey == null) {
            throw new NullPointerException("Application key cannot be null");
        }
        this.appKey = appKey;
    }

    public void info(String message) {
        log(message, Level.INFO.intValue());
    }

    public void error(String message, Exception exception) {
        StringBuilder builder = new StringBuilder();
        if (message != null) {
            builder.append(message);
        }
        if (exception != null) {
            if (builder.length() > 0) {
                builder.append('\n');
            }
            builder.append(ExceptionUtils.getStackTrace(exception));
        }
        error(builder.toString());
    }

    public void error(String message) {
        log(message, Level.SEVERE.intValue());
    }

    private void log(String message, long level) {
        message = StringUtils.trimToNull(message);
        if (message != null) {
            Thread thread = new Thread(new LoggerRunnable(appKey, level, message));
            thread.start();
        }
    }

}
