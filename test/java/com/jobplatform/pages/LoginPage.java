package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 4.1.2: Page Object cho Trang Đăng nhập (LoginPage)
 * Đại diện cho giao diện đăng nhập: http://localhost:3000/auth/login và Admin http://localhost:3001/auth/login
 */
public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators phần tử giao diện
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By submitButton = By.cssSelector("button[type='submit']");
    // Hỗ trợ cả Toast alert thông thường, AlertDialog modal (@base-ui/react), và nhãn lỗi inline
    private final By errorMessage = By.cssSelector("[role='alert'], [role='alertdialog'], [role='dialog'], [data-open], .text-danger, .text-red-500, .bg-red-50, [class*='Popup']");
    private final By googleLoginButton = By.xpath("//button[contains(., 'Tiếp tục với Google')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
    }

    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        element.clear();
        element.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        element.clear();
        element.sendKeys(password);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    /**
     * Hàm tiện ích thực hiện trọn gói luồng đăng nhập
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public String getErrorMessage() {
        By errorLocator = By.xpath("//*[@role='alert' or @role='alertdialog' or contains(text(), 'Đăng nhập thất bại') or contains(text(), 'không đúng') or contains(@class, 'text-danger')]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator)).getText();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            By errorLocator = By.xpath("//*[@role='alert' or @role='alertdialog' or contains(text(), 'Đăng nhập thất bại') or contains(text(), 'không đúng') or contains(@class, 'text-danger')]");
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

