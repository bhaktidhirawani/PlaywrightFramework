package com.api.tests;

import com.api.data.UserBuilder;
import com.api.entity.Case;
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

import java.util.Base64;

import static org.testng.Assert.assertEquals;

public class WorkflowTest extends BaseTest {

    @Test(testName = "End to end case creation workflow - register new user, login new user, create case for new user", groups = {"e2e"})
    @Feature("Case test")
    @Story("Test e2e workflow for user - register, login and create case")
    public void caseTest() {
        User userInput, createdUser;
        Login session;
        Case caseInput, caseId;
        int itemId = 3;

        userInput = UserBuilder.getUserData();

        /** Registers a new user using the register API **/
        APIResponse userResponse = apiService.sendRequest("POST", ENDPOINT.get("user"), RequestOptions.create().setData(userInput));
        // validate status code and status message
        assertEquals(userResponse.status(), 200);
        assertEquals(userResponse.statusText(), "OK");
        try {
            //convert response text to POJO -- deserialization
            createdUser = new ObjectMapper().readValue(userResponse.text(), User.class);
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
        // Validate api response body
        Assert.assertEquals(createdUser.getMessage(), "User created successfully");

        /** Sign in with the newly registered user via the login API. **/
        APIResponse loginResponse = apiService.sendRequest("POST", ENDPOINT.get("login"), RequestOptions.create().setData(userInput));
        // validate status code and status message
        assertEquals(loginResponse.status(), 200);
        assertEquals(loginResponse.statusText(), "OK");
        try {
            //convert response text to POJO -- deserialization
            session = new ObjectMapper().readValue(loginResponse.text(), Login.class);
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
        // Validate api response body
        Assert.assertEquals(session.getMessage(), "Login successful");
        Assert.assertNotEquals(session.getUser_id(), 0);
        Assert.assertFalse(session.is_admin());

        /** Creates a support case using the create-case API **/

        //get user id from login session
        int userId = session.getUser_id();
        // Created case data using itemId and userId
        caseInput = Case.builder().case_name("test case creation for item id " + itemId + " for user id " + userId).user_id(userId).item_id(itemId).build();
        // Execute create case API
        APIResponse caseResponse = apiService.sendRequest("POST", ENDPOINT.get("case"), RequestOptions.create().setData(caseInput));
        // validate status code and status message
        assertEquals(caseResponse.status(), 200);
        assertEquals(caseResponse.statusText(), "OK");
        try {
            //convert response text to POJO -- deserialization
            caseId = new ObjectMapper().readValue(caseResponse.text(), Case.class);
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }

        // Validate api response body
        Assert.assertEquals(caseId.getCase_name(), caseInput.getCase_name());
        Assert.assertNotEquals(caseId.getId(), 0);
        Assert.assertEquals(caseId.getStatus(), "open");
        Assert.assertEquals(caseId.getItem_id(), caseInput.getItem_id());
        Assert.assertEquals(caseId.getUser_id(), caseInput.getUser_id());
    }
}
