package com.jobplatform.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 4.1.4 & 4.1.5: Page Object cho Trang Quản lý ứng viên của Nhà tuyển dụng (RecruiterApplicationsPage)
 * URL: http://localhost:3000/recruiter/candidates
 */
public class RecruiterApplicationsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators danh sách ứng viên
    private final By candidateCards = By.cssSelector("a[href*='/recruiter/candidates/'], tr.candidate-row, .cursor-pointer");
    
    // Locators trên trang chi tiết ứng viên (/recruiter/candidates/{id})
    private final By statusSelectDropdown = By.xpath("//button[contains(@aria-haspopup, 'listbox')] | //select");
    private final By statusOptionInterview = By.xpath("//*[contains(text(), 'Mời phỏng vấn') or @data-value='INTERVIEW']");
    private final By statusOptionAccepted = By.xpath("//*[contains(text(), 'Tiếp nhận') or contains(text(), 'Trúng tuyển') or @data-value='ACCEPTED']");
    private final By statusOptionRejected = By.xpath("//*[contains(text(), 'Từ chối') or @data-value='REJECTED']");
    private final By currentStatusBadge = By.xpath("//span[contains(@class, 'rounded-full') and (contains(text(), 'Đã nộp') or contains(text(), 'Đã xem') or contains(text(), 'Mời phỏng vấn') or contains(text(), 'Đã tiếp nhận'))]");
    private final By recruiterNoteTextarea = By.cssSelector("textarea[placeholder*='ghi chú' i], textarea");
    private final By saveNoteButton = By.xpath("//button[contains(., 'Lưu ghi chú') or contains(., 'Cập nhật')]");

    public RecruiterApplicationsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/recruiter/candidates");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void clickFirstCandidate() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(candidateCards));
        driver.findElements(candidateCards).get(0).click();
    }

    public void updateStatusToInterview() {
        wait.until(ExpectedConditions.elementToBeClickable(statusSelectDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(statusOptionInterview)).click();
    }

    public void updateStatusToAccepted() {
        wait.until(ExpectedConditions.elementToBeClickable(statusSelectDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(statusOptionAccepted)).click();
    }

    public String getCurrentStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(currentStatusBadge)).getText();
    }

    public void addInternalNote(String note) {
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(recruiterNoteTextarea));
        textarea.clear();
        textarea.sendKeys(note);
        wait.until(ExpectedConditions.elementToBeClickable(saveNoteButton)).click();
    }
}

