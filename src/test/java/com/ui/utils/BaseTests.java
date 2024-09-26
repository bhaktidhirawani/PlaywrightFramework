package com.ui.utils;

import com.api.service.APIServiceException;
import com.microsoft.playwright.Browser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTests {
    public BrowserManager manager;
    public Browser browser;

    @BeforeClass
    @Parameters({"browserName"})
    public void setup(@Optional("CHROME") String browserName) {
        manager = new BrowserManager(browserName);
    }

    @AfterClass
    public void tearDown() {
        try {
            manager.closeBrowser();
            manager.closePlaywright();
        } catch (Exception e) {
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
    }
}
