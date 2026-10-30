package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho Trang Thông tin Doanh nghiệp của Nhà tuyển dụng (CompanyPage)
 * URL: http://localhost:3000/recruiter/company
 */
public class CompanyPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By nameInput = By.cssSelector("input[name='name'], input#name");
    private final By taxCodeInput = By.cssSelector("input[name='taxCode'], input#taxCode, input[placeholder*='thuế' i]");
    private final By phoneInput = By.cssSelector("input[name='phone'], input#phone");
    private final By emailInput = By.cssSelector("input[name='email'], input#email");
    private final By addressInput = By.cssSelector("input[name='address'], input#address");
    private final By submitButton = By.xpath("//button[@type='submit' or contains(., 'Lưu') or contains(., 'Tạo hồ sơ')]");
    private final By taxCodeError = By.xpath("//*[contains(text(), 'Mã số thuế') or contains(text(), 'bắt buộc') or contains(@class, 'text-danger')]");
    private final By successToast = By.xpath("//*[contains(text(), 'thành công') or contains(text(), 'Đã lưu')]");
    private final By statusBadge = By.xpath("//span[contains(@class, 'rounded-full') and (contains(text(), 'Chờ duyệt') or contains(text(), 'PENDING') or contains(text(), 'Hoạt động'))]");

    public CompanyPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/recruiter/company");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void enterName(String name) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        el.clear();
        el.sendKeys(name);
    }

    public void enterTaxCode(String taxCode) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(taxCodeInput));
        el.clear();
        el.sendKeys(taxCode);
    }

    public void enterPhone(String phone) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        el.clear();
        el.sendKeys(phone);
    }

    public void enterEmail(String email) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        el.clear();
        el.sendKeys(email);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean isTaxCodeErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(taxCodeError)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSaveSuccess() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

