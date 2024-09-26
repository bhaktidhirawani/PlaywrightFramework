package com.ui.utils;

import com.api.service.APIServiceException;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.BrowserContext;

import javax.swing.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

public class BrowserManager {

    private final Playwright playwright;
    protected Browser browser;
    public BrowserManager(String browserName){
        playwright = Playwright.create();
        browser = launchBrowser(browserName);
    }

    protected Browser launchBrowser(String browserName) {
        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    browser = playwright.chromium().launch(new BrowserType.LaunchOptions());
                    break;
                case "firefox":
                    browser = playwright.firefox().launch(new BrowserType.LaunchOptions());
                    break;
                case "webkit":
                    browser = playwright.webkit().launch(new BrowserType.LaunchOptions());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid browser name");
            }
        } catch (Exception e) {
            throw new APIServiceException("Error launching browser: " + e.getMessage(), e);
        }
        return browser;
    }

    public BrowserContext setBrowserContext(){
        return browser.newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos/")));
    }

    public String checkBrokenURLS(String urlLink){
        String message;
        try {
        URL url = new URL(urlLink);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() >= 400)
                message = urlLink + " is broken link : Error ==>" + Integer.toString(httpURLConnection.getResponseCode()) + ":" + httpURLConnection.getResponseMessage();
            else
                message = "";
        }catch (Exception e){
            throw new APIServiceException("Error encountered: " + e.getMessage(), e);
        }
        return message;
    }
    public void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
    }
    public void closePlaywright() {
        if (playwright != null) {
            playwright.close();
        }
    }
}
