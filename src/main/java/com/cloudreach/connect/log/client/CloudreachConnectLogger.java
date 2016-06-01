package com.cloudreach.connect.log.client;

import com.cloudreach.connect.api.LogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.logging.Level;

public class CloudreachConnectLogger implements LogService {

    private final String serviceUrl;
    private final String appKey;

    public CloudreachConnectLogger(String serviceUrl, String appKey) {
        if (serviceUrl == null) {
            throw new NullPointerException("Service URL cannot be null");
        }
        if (appKey == null) {
            throw new NullPointerException("Application key cannot be null");
        }
        this.serviceUrl = serviceUrl;
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
            Thread thread = new Thread(new LoggerRunnable(serviceUrl, appKey, level, message));
            thread.start();
        }
    }

}
