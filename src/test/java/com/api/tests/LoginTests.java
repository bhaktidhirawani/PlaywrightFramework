package com.api.tests;

import com.api.data.UserBuilder;
import com.api.entity.Login;
import com.api.entity.User;
import com.api.service.APIServiceException;
import com.api.utils.BaseTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LoginTests extends BaseTest {

    @Test(testName = "Sign-in using login API", groups = {"endpoint"})
    @Feature("Login test")
    @Story("Test e2e workflow for user - register, login and create case")
    public void invalidCredentialsloginTest() {
        Login session;
        User userInput = UserBuilder.getUserData();
        // execute login API
        APIResponse response = apiService.sendRequest("POST", ENDPOINT.get("login"), RequestOptions.create().setData(userInput));
        // validate status code and status message
        assertEquals(response.status(), 400);
        System.out.println(response.statusText());
        System.out.println(response.text());
        assertEquals(response.statusText(), "Bad Request");
        try {
            //convert response text to POJO -- deserialization
            session = new ObjectMapper().readValue(response.text(), Login.class);
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
        // Validate api response body
        Assert.assertEquals(session.getDetail(), "Invalid email or password");
    }
}
