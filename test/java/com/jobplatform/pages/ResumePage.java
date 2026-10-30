package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho Trang Quản lý CV của Ứng viên (ResumePage)
 * URL: http://localhost:3000/candidate/resume
 */
public class ResumePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By uploadSuccessToast = By.xpath("//*[contains(text(), 'thành công') or contains(text(), 'Tải CV lên thành công')]");
    private final By formatError = By.xpath("//*[contains(text(), 'PDF') or contains(text(), 'không hợp lệ') or contains(text(), 'Chỉ chấp nhận') or contains(@class, 'text-destructive')]");
    private final By resumeItems = By.cssSelector("[data-testid='resume-item'], .border.rounded-xl, .bg-white.rounded-xl");

    public ResumePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/candidate/resume");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void uploadFile(String absoluteFilePath) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        input.sendKeys(absoluteFilePath);
    }

    public boolean isUploadSuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(uploadSuccessToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFormatErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(formatError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResumesCount() {
        try {
            return driver.findElements(resumeItems).size();
        } catch (Exception e) {
            return 0;
        }
    }
}

