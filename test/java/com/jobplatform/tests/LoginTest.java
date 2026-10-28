package com.jobplatform.tests;

import com.jobplatform.base.BaseTest;
import com.jobplatform.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kiểm thử quy trình đăng nhập (Login) trên cả 2 portal Người dùng và Quản trị
 */
public class LoginTest extends BaseTest {

    @Test
    @DisplayName("TC_LOGIN_01: Đăng nhập người dùng thành công với thông tin hợp lệ")
    public void testCandidateLoginSuccess() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL_CANDIDATE + "/auth/login");

        loginPage.login("ungvien@test.com", "Candidate123");

        // Xác minh chuyển hướng URL sau đăng nhập thành công
        wait.until(d -> !d.getCurrentUrl().contains("/auth/login"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/candidate") || currentUrl.contains("/recruiter") || currentUrl.equals(BASE_URL_CANDIDATE + "/"),
                "URL sau đăng nhập phải chuyển hướng khỏi trang login. Thực tế: " + currentUrl);
    }

    @Test
    @DisplayName("TC_LOGIN_02: Đăng nhập thất bại do Mật khẩu không chính xác")
    public void testLoginFailureWrongPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL_CANDIDATE + "/auth/login");

        loginPage.login("ungvien@test.com", "SaiMatKhau@2026");

        // Xác minh thông báo lỗi hiển thị và không chuyển trang
        assertTrue(loginPage.isErrorMessageDisplayed(), "Hệ thống phải hiển thị thông báo lỗi khi nhập sai mật khẩu");
        assertTrue(driver.getCurrentUrl().contains("/auth/login"), "Người dùng phải được giữ lại tại trang đăng nhập");
    }

    @Test
    @DisplayName("TC_LOGIN_04: Đăng nhập trang Quản trị viên (Admin Portal) thành công")
    public void testAdminLoginSuccess() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL_ADMIN + "/auth/login");

        loginPage.login("camly@jobplatform.app", "Admin123");

        // Xác minh chuyển hướng vào Dashboard Admin
        wait.until(d -> d.getCurrentUrl().contains("/admin"));
        assertTrue(driver.getCurrentUrl().contains("/admin"), "Quản trị viên phải được chuyển đến /admin");
    }
}

