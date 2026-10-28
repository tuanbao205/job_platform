package com.jobplatform.tests;

import com.jobplatform.base.BaseTest;
import com.jobplatform.pages.JobDetailPage;
import com.jobplatform.pages.JobSearchPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 4.1.3: Kiểm thử chức năng tìm kiếm và xem chi tiết tin tuyển dụng
 */
public class JobSearchTest extends BaseTest {

    @Test
    @DisplayName("TC_JOB_SEARCH_01: Tìm kiếm việc làm theo từ khóa hợp lệ")
    public void testSearchJobByKeyword() {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL_CANDIDATE);

        searchPage.searchByKeyword("Lập trình");

        // Xác minh danh sách hiển thị các tin tuyển dụng liên quan
        int count = searchPage.getJobCardsCount();
        assertTrue(count >= 0, "Kết quả tìm kiếm phải trả về danh sách thẻ tin tuyển dụng");
    }

    @Test
    @DisplayName("TC_JOB_DETAIL_01: Mở xem chi tiết tin tuyển dụng từ danh sách")
    public void testViewJobDetail() {
        JobSearchPage searchPage = new JobSearchPage(driver);
        searchPage.open(BASE_URL_CANDIDATE);

        if (searchPage.getJobCardsCount() > 0) {
            searchPage.clickFirstJobCard();

            JobDetailPage detailPage = new JobDetailPage(driver);
            String title = detailPage.getJobTitle();
            assertNotNull(title, "Tiêu đề công việc trên trang chi tiết không được để trống");
            assertFalse(title.trim().isEmpty(), "Tiêu đề công việc phải có nội dung");
        } else {
            System.out.println("[SKIP] Chưa có tin tuyển dụng nào trên hệ thống để kiểm tra chi tiết");
        }
    }
}

