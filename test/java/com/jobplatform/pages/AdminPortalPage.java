package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho Trang Quản trị (AdminPortalPage)
 * URL: http://localhost:3001/admin
 */
public class AdminPortalPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators Quản lý công ty (/admin/companies)
    private final By companyRow = By.cssSelector("tr, a[href*='/admin/companies/']");
    private final By approveCompanyButton = By.xpath("//button[contains(., 'Duyệt') or contains(., 'Hoạt động')]");
    private final By rejectCompanyButton = By.xpath("//button[contains(., 'Từ chối')]");
    private final By rejectReasonTextarea = By.cssSelector("textarea[placeholder*='lý do' i], textarea#reject-reason");
    private final By confirmRejectButton = By.xpath("//button[contains(., 'Xác nhận từ chối') or contains(., 'Đồng ý')]");
    private final By reasonValidationMessage = By.xpath("//*[contains(text(), 'từ 10 đến 500 ký tự') or contains(text(), 'quá ngắn')]");

    // Locators Quản lý người dùng (/admin/users)
    private final By userRow = By.cssSelector("tr, [data-testid='user-row']");
    private final By lockUserButton = By.xpath("//button[contains(., 'Khóa') or contains(@title, 'Khóa')]");
    private final By lockReasonTextarea = By.cssSelector("textarea#lock-reason-input, textarea[placeholder*='lý do' i]");
    private final By confirmLockButton = By.xpath("//button[contains(., 'Xác nhận khóa')]");
    private final By bannedStatusBadge = By.xpath("//span[contains(text(), 'BANNED') or contains(text(), 'Đã khóa') or contains(text(), 'Bị khóa')]");

    public AdminPortalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openCompaniesPage(String adminBaseUrl) {
        driver.get(adminBaseUrl + "/admin/companies");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void openUsersPage(String adminBaseUrl) {
        driver.get(adminBaseUrl + "/admin/users");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void clickApproveCompany() {
        wait.until(ExpectedConditions.elementToBeClickable(approveCompanyButton)).click();
    }

    public void clickRejectCompany(String reason) {
        wait.until(ExpectedConditions.elementToBeClickable(rejectCompanyButton)).click();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(rejectReasonTextarea));
        input.clear();
        input.sendKeys(reason);
        wait.until(ExpectedConditions.elementToBeClickable(confirmRejectButton)).click();
    }

    public boolean isReasonValidationErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(reasonValidationMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void lockUser(String reason) {
        wait.until(ExpectedConditions.elementToBeClickable(lockUserButton)).click();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(lockReasonTextarea));
        input.clear();
        input.sendKeys(reason);
        wait.until(ExpectedConditions.elementToBeClickable(confirmLockButton)).click();
    }

    public boolean isUserBannedStatusDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(bannedStatusBadge)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

