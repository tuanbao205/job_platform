# BÁO CÁO TỔNG HỢP KẾT QUẢ KIỂM THỬ TỰ ĐỘNG (TEST SUMMARY REPORT)

**Dự án**: Job Platform – Nền tảng tuyển dụng trực tuyến  
**Môn học**: Đánh giá và kiểm định chất lượng phần mềm  
**Công nghệ**: Java 17, Maven 3.9+, Selenium WebDriver 4.28+, JUnit 5, ChromeDriver  
**Thời gian thực thi**: 28/10/2026  
**Môi trường thử nghiệm**:
- Frontend Ứng viên / Tuyển dụng: `http://localhost:3000`
- Frontend Quản trị viên (Admin Portal): `http://localhost:3001`
- Backend API Server: `http://localhost:4000/api/v1`
- Cơ sở dữ liệu: PostgreSQL 16 (Cổng 5433), Redis (Cổng 6379)
- Trình duyệt: Google Chrome 156 (Headless / Managed by Selenium Manager)

---

## 1. Bảng số liệu tổng hợp kiểm thử

| Chỉ tiêu | Kết quả đợt chạy đầu (Chưa fix locator) | Kết quả nghiệm thu chính thức (Đã tối ưu) |
| :--- | :---: | :---: |
| **Tổng số Test Cases** | 8 | 8 |
| **Số Test Case ĐẠT (PASS)** | 6 | **8** |
| **Số Test Case KHÔNG ĐẠT (FAIL)** | 1 | **0** |
| **Số Test Case LỖI (ERROR)** | 1 | **0** |
| **Số Test Case BỎ QUA (SKIPPED)** | 0 | **0** |
| **Tỷ lệ PASS (%)** | 75.0% | **100.0%** |
| **Tổng thời gian thực thi** | 92.61 giây | **84.35 giây** |

$$\text{Tỷ lệ PASS} = \frac{8}{8} \times 100\% = 100\%$$

---

## 2. Bảng kết quả chi tiết theo Test Case

| STT | Mã Test Case | Lớp kiểm thử (Test Class) | Tên kịch bản | Kết quả mong đợi | Kết quả thực tế | Trạng thái |
| :-: | :--- | :--- | :--- | :--- | :--- | :---: |
| 1 | `TC_LOGIN_01` | `LoginTest` | Ứng viên đăng nhập hợp lệ | Đăng nhập thành công, chuyển hướng vào `/candidate/profile` | Chuyển hướng chính xác vào trang cá nhân | **PASS** |
| 2 | `TC_LOGIN_02` | `LoginTest` | Đăng nhập sai mật khẩu | Hiển thị Dialog lỗi 'Đăng nhập thất bại', giữ trang login | Dialog lỗi xuất hiện với role `alertdialog` | **PASS** |
| 3 | `TC_LOGIN_04` | `LoginTest` | Admin đăng nhập Portal | Đăng nhập thành công, chuyển hướng vào `/admin` | Chuyển hướng chính xác vào Admin Dashboard | **PASS** |
| 4 | `TC_JOB_01` | `JobSearchTest` | Tìm kiếm theo từ khóa | Hiển thị danh sách việc làm phù hợp | Tìm kiếm thành công danh sách thẻ việc làm | **PASS** |
| 5 | `TC_JOB_03` | `JobSearchTest` | Lọc địa điểm & danh mục | Lọc chính xác danh sách tin tuyển dụng | Danh sách tin lọc chính xác theo tiêu chí | **PASS** |
| 6 | `TC_APP_01` | `ApplicationSubmissionTest` | Nộp đơn ứng tuyển | Tạo đơn thành công, snapshot CV, trạng thái `APPLIED` | Hiển thị thông báo nộp thành công, trạng thái `APPLIED` | **PASS** |
| 7 | `TC_APP_04` | `ApplicationStatusTest` | Recruiter chuyển trạng thái | Chuyển đơn từ `VIEWED` sang `INTERVIEW` kèm ghi chú | Cập nhật thành công, badge trạng thái `INTERVIEW` | **PASS** |
| 8 | `TC_APP_06` | `ApplicationWithdrawalTest` | Ứng viên rút đơn ứng tuyển | Đơn được rút an toàn, chuyển trạng thái `WITHDRAWN` | Nút rút đơn kích hoạt thành công, trạng thái `WITHDRAWN` | **PASS** |

---

## 3. Ghi nhận lỗi và giải pháp khắc phục (Defect Resolution Log)

Trong quá trình thực thi kiểm thử ở Bước 5, hệ thống phát hiện 2 điểm bất thường trong `LoginTest`:

1. **Lỗi `TimeoutException` trong `testCandidateLoginSuccess`**:
   - *Nguyên nhân*: Sử dụng nhầm tài khoản Quản trị viên (`camly@jobplatform.app`) trên portal Ứng viên, dẫn đến việc Next.js điều hướng về `/` thay vì `/candidate/profile`.
   - *Khắc phục*: Thay thế bằng tài khoản Ứng viên chuẩn `ungvien@test.com` / `Candidate123`.
2. **Lỗi `AssertionFailedError` trong `testLoginFailureWrongPassword`**:
   - *Nguyên nhân*: Frontend hiển thị thông báo lỗi bằng component `AppAlertDialog` có thuộc tính `role="alertdialog"`. Locator cũ chỉ tìm `[role='alert']` nên không bắt được phần tử.
   - *Khắc phục*: Bổ sung locator đa hình trong `LoginPage.java` hỗ trợ `role="alertdialog"`, `role="alert"`, và đối soát văn bản *"Đăng nhập thất bại"*.

Sau khi áp dụng giải pháp, toàn bộ 8 bài test đều đạt trạng thái **PASS (100%)**.

---

## 4. Kết luận nghiệm thu

- Bộ kiểm thử tự động Selenium WebDriver hoạt động ổn định, điều khiển trình duyệt mượt mà và mô phỏng chính xác hành vi người dùng thật.
- Các quy trình nghiệp vụ trọng tâm của hệ thống tuyển dụng (Đăng nhập, Tìm kiếm tin, Nộp đơn, Phê duyệt trạng thái đơn và Rút đơn) đều đáp ứng đúng tiêu chuẩn kỹ thuật và đặc tả yêu cầu phần mềm (SRS).
- Sản phẩm đủ điều kiện nghiệm thu cho bài báo cáo học phần *Đánh giá và kiểm định chất lượng phần mềm*.

