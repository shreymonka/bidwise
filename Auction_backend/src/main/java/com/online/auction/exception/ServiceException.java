package com.online.auction.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends Exception {
    private final int statusCode;
    private final String errorMessage;

    public ServiceException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode.value();
        this.errorMessage = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
