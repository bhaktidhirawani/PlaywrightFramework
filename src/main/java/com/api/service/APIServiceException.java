package com.api.service;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

public class APIServiceException extends RuntimeException {

    public APIServiceException(String message) {
        super(message);
    }

    public APIServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
