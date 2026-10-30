package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho Trang Đăng tin và Quản lý tin tuyển dụng (JobPostPage)
 * URL: http://localhost:3000/recruiter/jobs/new và /recruiter/jobs
 */
public class JobPostPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By titleInput = By.cssSelector("input[name='title'], input#title");
    private final By deadlineInput = By.cssSelector("input[name='deadline'], input[type='date']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By deadlineError = By.xpath("//*[contains(text(), 'quá khứ') or contains(text(), 'deadline') or contains(@class, 'text-danger')]");
    private final By successToast = By.xpath("//*[contains(text(), 'Đăng tin thành công') or contains(text(), 'Tạo tin thành công')]");
    private final By statusMenuButton = By.xpath("//button[contains(@aria-haspopup, 'menu') or contains(@class, 'status-toggle')]");
    private final By hideJobOption = By.xpath("//*[contains(text(), 'Ẩn tin') or contains(text(), 'HIDDEN')]");
    private final By hiddenStatusBadge = By.xpath("//span[contains(text(), 'Tạm ẩn') or contains(text(), 'HIDDEN')]");

    public JobPostPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openNewJobPage(String baseUrl) {
        driver.get(baseUrl + "/recruiter/jobs/new");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void openJobsListPage(String baseUrl) {
        driver.get(baseUrl + "/recruiter/jobs");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void enterTitle(String title) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(titleInput));
        el.clear();
        el.sendKeys(title);
    }

    public void enterDeadline(String dateStr) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(deadlineInput));
        el.clear();
        el.sendKeys(dateStr);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean isDeadlineErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(deadlineError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isJobCreatedSuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void toggleJobStatusToHidden() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(statusMenuButton)).click();
            wait.until(ExpectedConditions.elementToBeClickable(hideJobOption)).click();
        } catch (Exception ignored) {
        }
    }

    public boolean isJobHiddenDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(hiddenStatusBadge)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

