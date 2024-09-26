package com.ui.test;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.ui.utils.BaseTests;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

public class LinkTest extends BaseTests {

    @Test(testName = "Test Broken Links in a site", groups = {"UI"})
    public void TestBrokenLink() {
        String regex = "((?:https?\\:\\/\\/|www\\.)(?:[-a-z0-9]+\\.)*[-a-z0-9]+.*)";
        BrowserContext context = manager.setBrowserContext();
        Page page = context.newPage();

        page.navigate("https://www.amazon.com");
        //List<Locator> amazonLinks = page.getByRole(AriaRole.LINK).all(); => alternate way to find links
        List<Locator> links = page.locator("//*[@href]").all().stream().filter(e->e.getAttribute("href").matches(regex)).collect(Collectors.toList());
        System.out.println("no. of links " +links.size());

        List<String>  urls = links.parallelStream().collect(Collectors.mapping(e -> new String(e.getAttribute("href")), Collectors.toList()));
        List<String> checkUrls = urls.parallelStream().collect(Collectors.mapping(e-> new String(manager.checkBrokenURLS(e)),Collectors.toList()));
        List<String> brokenUrls = checkUrls.parallelStream().filter(el-> el != null && !el.trim().isEmpty()).collect(Collectors.toList());

        System.out.println("no. of broken links " +brokenUrls.size());
        brokenUrls.parallelStream().forEach(e -> System.out.println(e));

        context.close();
    }
}
