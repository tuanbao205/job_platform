package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 4.1.3: Page Object cho Trang Chi tiết việc làm và Hộp thoại Nộp đơn (JobDetailPage)
 * URL: http://localhost:3000/jobs/{id}
 */
public class JobDetailPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators trên trang chi tiết
    private final By jobTitle = By.cssSelector("h1");
    private final By applyNowButton = By.xpath("//button[contains(., 'Ứng tuyển ngay')]");
    private final By alreadyAppliedBadge = By.xpath("//*[contains(text(), 'Đã ứng tuyển') or contains(text(), 'ĐÃ ỨNG TUYỂN')]");
    
    // Locators trong Popup ApplyModal
    private final By applyModal = By.cssSelector(".fixed.inset-0, [role='dialog']");
    private final By resumeOption = By.cssSelector("input[type='radio'], [role='radio'], .cursor-pointer.border-blue-500");
    private final By confirmApplyButton = By.xpath("//button[contains(., 'Xác nhận ứng tuyển')]");
    private final By successMessage = By.xpath("//*[contains(text(), 'Ứng tuyển thành công')]");
    private final By cancelModalButton = By.xpath("//button[contains(., 'Hủy')]");

    public JobDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl, String jobId) {
        driver.get(baseUrl + "/jobs/" + jobId);
        wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitle));
    }

    public String getJobTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitle)).getText();
    }

    public void clickApplyNow() {
        wait.until(ExpectedConditions.elementToBeClickable(applyNowButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(applyModal));
    }

    public void selectResume() {
        // Tự động chọn CV đầu tiên hoặc giữ CV mặc định
        try {
            WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(resumeOption));
            if (!radio.isSelected()) {
                radio.click();
            }
        } catch (Exception ignored) {
            // Nếu đã chọn mặc định thì bỏ qua
        }
    }

    public void clickConfirmApply() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmApplyButton)).click();
    }

    public boolean isApplySuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAlreadyApplied() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(alreadyAppliedBadge)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

