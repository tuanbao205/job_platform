package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 4.1.3 & 4.1.5: Page Object cho Trang Danh sách đơn ứng tuyển của Ứng viên (ApplicationPage)
 * URL: http://localhost:3000/candidate/applications
 */
public class ApplicationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By applicationCards = By.cssSelector("[data-testid='application-card'], .border.rounded-2xl, .bg-white.p-5");
    private final By withdrawButton = By.xpath("//button[contains(., 'Rút hồ sơ') or contains(., 'Rút đơn')]");
    private final By confirmWithdrawButton = By.xpath("//button[contains(., 'Đồng ý rút đơn') or contains(., 'Xác nhận rút')]");
    private final By statusBadge = By.xpath("//span[contains(@class, 'rounded-full') or contains(@class, 'font-medium')]");
    private final By withdrawSuccessToast = By.xpath("//*[contains(text(), 'Rút đơn thành công') or contains(text(), 'Đã rút đơn')]");

    public ApplicationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/candidate/applications");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void clickWithdrawFirstApplication() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(withdrawButton));
        btn.click();
        // Nhấn xác nhận trong modal cảnh báo rút đơn
        wait.until(ExpectedConditions.elementToBeClickable(confirmWithdrawButton)).click();
    }

    public String getLatestApplicationStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(statusBadge)).getText();
    }

    public boolean isWithdrawButtonVisible() {
        try {
            return driver.findElement(withdrawButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWithdrawSuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(withdrawSuccessToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

