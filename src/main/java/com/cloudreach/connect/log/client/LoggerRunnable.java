package com.cloudreach.connect.log.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@RequiredArgsConstructor
class LoggerRunnable implements Runnable {

    private final String serviceUrl;
    private final String appKey;
    private final long level;
    private final String message;

    public void run() {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(serviceUrl);
            connection = openConnectionTo(url);
            MessageRequest messageRequest = new MessageRequest(level, message);
            populateRequest(connection, messageRequest);
            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                handleError(connection, null);
            }
        } catch (IOException e) {
            handleError(connection, e);
        }
    }

    private HttpsURLConnection openConnectionTo(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("AuthKey", appKey);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    private void populateRequest(HttpsURLConnection connection, Object data) throws IOException {
        try (OutputStream requestBody = connection.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(requestBody, data);
        }
    }

    private void handleError(HttpsURLConnection connection, Throwable cause) {
        StringBuilder message = new StringBuilder("An error occurred while sending log request");
        Integer statusCode = null;
        String statusMessage = null;
        String body = null;
        String errorBody = null;
        try {
            statusCode = connection.getResponseCode();
        } catch (IOException e) {
        }
        try {
            statusMessage = connection.getResponseMessage();
        } catch (IOException e) {
        }
        try (InputStream is = connection.getInputStream()) {
            body = StringUtils.trimToNull(IOUtils.toString(is));
        } catch (IOException e) {
        }
        try (InputStream is = connection.getErrorStream()) {
            errorBody = StringUtils.trimToNull(IOUtils.toString(is));
        } catch (IOException e) {
        }
        message.append("\nStatus code: ").append(String.valueOf(statusCode));
        message.append("\nStatus message: ").append(String.valueOf(statusMessage));
        message.append("\nBody: ").append(body);
        message.append("\nError body: ").append(errorBody);
        if (cause != null) {
            throw new RuntimeException(message.toString(), cause);
        } else {
            throw new RuntimeException(message.toString());
        }
    }

}
