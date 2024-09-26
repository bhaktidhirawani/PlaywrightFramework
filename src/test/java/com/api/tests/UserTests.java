package com.api.tests;

import com.api.data.UserBuilder;
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

public class UserTests extends BaseTest {
    @Test(testName = "Registers a new user using the register API.", groups = {"endpoint"})
    @Feature("User test")
    @Story("Test e2e workflow for case - register, login and create case")
    public void userTest() {
        User createdUser;

        // execute register user API
        APIResponse response = apiService.sendRequest("POST", ENDPOINT.get("user"), RequestOptions.create().setData(UserBuilder.getUserData()));
        // validate status code and status message
        assertEquals(response.status(), 200);
        assertEquals(response.statusText(), "OK");
        try {
            //convert response text to POJO -- deserialization
            createdUser = new ObjectMapper().readValue(response.text(), User.class);
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
        // Validate api response body
        Assert.assertEquals(createdUser.getMessage(), "User created successfully");
    }
}

