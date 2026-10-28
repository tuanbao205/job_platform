package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * 4.1.3: Page Object cho Trang Tìm kiếm và Danh sách việc làm (JobSearchPage)
 * URL: http://localhost:3000/jobs
 */
public class JobSearchPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators phần tử
    private final By keywordInput = By.cssSelector("input[placeholder*='công việc' i], input[placeholder*='tìm kiếm' i], input[type='search']");
    private final By searchButton = By.xpath("//button[contains(., 'Tìm kiếm') or @type='submit']");
    private final By jobCards = By.cssSelector("a[href*='/jobs/'], [data-testid='job-card']");
    private final By jobTitleInCard = By.cssSelector("h2, h3, .font-semibold");
    private final By noResultAlert = By.xpath("//*[contains(text(), 'Không tìm thấy') or contains(text(), 'Không có việc làm')]");

    public JobSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/jobs");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void searchByKeyword(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(keywordInput));
        input.clear();
        input.sendKeys(keyword);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public int getJobCardsCount() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(jobCards));
            return driver.findElements(jobCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickFirstJobCard() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(jobCards));
        List<WebElement> list = driver.findElements(jobCards);
        if (!list.isEmpty()) {
            list.get(0).click();
        } else {
            throw new RuntimeException("Không tìm thấy tin tuyển dụng nào trên danh sách");
        }
    }

    public boolean isNoResultDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noResultAlert)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

