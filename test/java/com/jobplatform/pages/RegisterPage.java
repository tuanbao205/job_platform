package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object cho Trang Đăng ký tài khoản (RegisterPage)
 * URL: http://localhost:3000/auth/register
 */
public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By fullNameInput = By.cssSelector("input[name='fullName'], input#fullName, input[placeholder*='họ tên' i]");
    private final By emailInput = By.cssSelector("input[name='email'], input#email, input[type='email']");
    private final By passwordInput = By.cssSelector("input[name='password'], input#password");
    private final By confirmPasswordInput = By.cssSelector("input[name='confirmPassword'], input#confirmPassword");
    private final By termsCheckbox = By.cssSelector("input[name='terms'], input[type='checkbox']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By candidateRoleRadio = By.cssSelector("input[value='CANDIDATE']");
    private final By recruiterRoleRadio = By.cssSelector("input[value='RECRUITER']");
    private final By errorMessage = By.xpath("//*[@role='alert' or @role='alertdialog' or contains(@class, 'text-danger') or contains(text(), 'Đăng ký thất bại') or contains(text(), 'đã được sử dụng')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
    }

    public void enterFullName(String fullName) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
        element.clear();
        element.sendKeys(fullName);
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

    public void enterConfirmPassword(String confirmPassword) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput));
        element.clear();
        element.sendKeys(confirmPassword);
    }

    public void checkTerms() {
        WebElement checkbox = driver.findElement(termsCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void selectCandidateRole() {
        try {
            driver.findElement(candidateRoleRadio).click();
        } catch (Exception ignored) {
        }
    }

    public void selectRecruiterRole() {
        try {
            driver.findElement(recruiterRoleRadio).click();
        } catch (Exception ignored) {
        }
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void register(String fullName, String email, String password, String role) {
        enterFullName(fullName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        if ("RECRUITER".equalsIgnoreCase(role)) {
            selectRecruiterRole();
        } else {
            selectCandidateRole();
        }
        checkTerms();
        clickSubmit();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }
}

