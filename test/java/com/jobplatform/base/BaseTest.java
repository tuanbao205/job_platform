package com.jobplatform.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 4.1.1 & 4.1.2: Lớp cơ sở (BaseTest)
 * Khởi tạo môi trường kiểm thử Selenium WebDriver, quản lý vòng đời trình duyệt Chrome
 * và cung cấp các hàm tiện ích thao tác, chờ đợi (Explicit Waits) và chụp ảnh màn hình.
 */
public abstract class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Cấu hình URL mặc định của hệ thống Job Platform
    protected final String BASE_URL_CANDIDATE = "http://localhost:3000";
    protected final String BASE_URL_ADMIN = "http://localhost:3001";
    protected final Duration TIMEOUT = Duration.ofSeconds(10);

    @BeforeEach
    public void setUp() {
        // Cấu hình ChromeOptions tối ưu cho kiểm thử tự động
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

        // Bật cờ headless nếu cần chạy kiểm thử không giao diện (trên CI/CD)
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // Selenium 4 tự động quản lý ChromeDriver thông qua Selenium Manager
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ==========================================
    // CÁC HÀM TIỆN ÍCH HỖ TRỢ KIỂM THỬ (HELPER METHODS)
    // ==========================================

    /**
     * Chờ phần tử hiển thị trên DOM và màn hình
     */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Chờ phần tử sẵn sàng để tương tác click
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Nhập dữ liệu an toàn vào trường input (tự động xóa dữ liệu cũ)
     */
    protected void sendKeys(By locator, String text) {
        WebElement element = waitForClickable(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Nhấn nút với cơ chế chờ sẵn sàng và hỗ trợ fallback JavaScript click
     */
    protected void click(By locator) {
        try {
            waitForClickable(locator).click();
        } catch (ElementClickInterceptedException | JavascriptException e) {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Cuộn trang tới phần tử cần tương tác
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * 4.1.7: Lưu ảnh màn hình (Screenshot) phục vụ báo cáo khi kiểm thử thất bại
     */
    public void takeScreenshot(String testName) {
        if (driver instanceof TakesScreenshot takesScreenshot) {
            File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path targetDir = Paths.get("target", "screenshots");
            try {
                Files.createDirectories(targetDir);
                Path targetPath = targetDir.resolve(testName + "_" + timestamp + ".png");
                Files.copy(source.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[INFO] Đã lưu ảnh chụp màn hình kiểm thử tại: " + targetPath.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("[ERROR] Không thể lưu ảnh chụp màn hình: " + e.getMessage());
            }
        }
    }
}

