package com.jobplatform.tests;

import com.jobplatform.base.BaseTest;
import com.jobplatform.pages.JobDetailPage;
import com.jobplatform.pages.JobSearchPage;
import com.jobplatform.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 4.1.3 & 4.1.6: Kiểm thử quy trình Ứng viên nộp hồ sơ ứng tuyển (Application Submission)
 */
public class ApplicationSubmissionTest extends BaseTest {

    @Test
    @DisplayName("TC_APP_01: Ứng viên đăng nhập và nộp hồ sơ ứng tuyển thành công (APPLIED)")
    public void testApplyJobSuccessfully() {
        // Bước 1: Ứng viên đăng nhập
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL_CANDIDATE + "/auth/login");
        loginPage.login("ungvien@test.com", "Candidate123");

        // Bước 2: Tìm kiếm và mở tin tuyển dụng
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL_CANDIDATE);
        
        if (searchPage.getJobCardsCount() > 0) {
            searchPage.clickFirstJobCard();

            // Bước 3: Nộp hồ sơ qua modal ứng tuyển
            JobDetailPage detailPage = new JobDetailPage(driver);
            if (!detailPage.isAlreadyApplied()) {
                detailPage.clickApplyNow();
                detailPage.selectResume();
                detailPage.clickConfirmApply();

                // Bước 4: 4.1.6 Xác minh bằng Assertion trạng thái nộp thành công
                assertTrue(detailPage.isApplySuccess() || detailPage.isAlreadyApplied(),
                        "Ứng viên phải nộp hồ sơ thành công và hiển thị thông báo hoặc huy hiệu đã ứng tuyển");
            } else {
                System.out.println("[INFO] Ứng viên đã nộp đơn vào công việc này trước đó.");
            }
        }
    }
}

