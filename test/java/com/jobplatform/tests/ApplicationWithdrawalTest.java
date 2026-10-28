package com.jobplatform.tests;

import com.jobplatform.base.BaseTest;
import com.jobplatform.pages.ApplicationPage;
import com.jobplatform.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 4.1.3 & 4.1.5: Kiểm thử tự động hóa quy trình Ứng viên rút hồ sơ ứng tuyển (WITHDRAWN)
 */
public class ApplicationWithdrawalTest extends BaseTest {

    @Test
    @DisplayName("TC_APP_WITHDRAW_01: Ứng viên rút đơn ứng tuyển khi trạng thái là APPLIED/VIEWED")
    public void testCandidateWithdrawApplication() {
        // Bước 1: Ứng viên đăng nhập
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL_CANDIDATE + "/auth/login");
        loginPage.login("ungvien@test.com", "Candidate123");

        // Bước 2: Vào trang quản lý đơn ứng tuyển
        ApplicationPage appPage = new ApplicationPage(driver);
        appPage.open(BASE_URL_CANDIDATE);

        // Bước 3: Kiểm tra nếu có nút rút đơn (đang ở APPLIED hoặc VIEWED)
        if (appPage.isWithdrawButtonVisible()) {
            appPage.clickWithdrawFirstApplication();

            // Bước 4: 4.1.6 Xác minh trạng thái đơn chuyển sang WITHDRAWN (Đã rút)
            assertTrue(appPage.isWithdrawSuccess() || appPage.getLatestApplicationStatus().contains("rút"),
                    "Đơn ứng tuyển phải được chuyển sang trạng thái Đã rút đơn (WITHDRAWN)");
        } else {
            System.out.println("[INFO] Không có đơn ứng tuyển nào ở trạng thái đủ điều kiện rút (APPLIED/VIEWED)");
        }
    }
}

