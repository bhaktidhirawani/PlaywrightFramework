package com.api.utils;

import com.api.entity.User;
import com.api.service.APIService;
import com.api.service.APIServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    String baseUrl = "http://100.27.30.112/api/";
    protected APIService apiService;
    protected static final Map<String, String> ENDPOINT = new HashMap<>();
    private static final String CONTENT_TYPE_HEADER = "content-type";
    private static final String APPLICATION_JSON = "application/json";

    static {
        ENDPOINT.put("user", "register/");
        ENDPOINT.put("login", "login/");
        ENDPOINT.put("case", "cases/");
    }

    @BeforeClass(groups = {"endpoint", "e2e"})
    public void setup() {
        apiService = new APIService();
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER, APPLICATION_JSON);
        headers.put("Accept", APPLICATION_JSON);
        apiService.setApiRequestContext(baseUrl, headers);
    }

    @AfterClass
    public void tearDown() {
        try {
            apiService.disposeAPIRequestContext();
            apiService.closePlaywright();
        } catch (Exception e) {
            throw new APIServiceException("Error sending request: " + e.getMessage(), e);
        }
    }

}
