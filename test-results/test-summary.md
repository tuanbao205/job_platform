# BÁO CÁO TỔNG HỢP KẾT QUẢ KIỂM THỬ TỰ ĐỘNG (TEST SUMMARY REPORT)

**Dự án**: Job Platform – Nền tảng tuyển dụng trực tuyến  
**Môn học**: Đánh giá và kiểm định chất lượng phần mềm  
**Giảng viên hướng dẫn**: TS. Trịnh Thanh Bình  
**Sinh viên thực hiện**:
- Hoàng Tuấn Bảo - 23010194 (33%)
- Lê Thị Cẩm Ly - 23010411 (33%)
- Phạm Tùng Dương - 23010113 (33%)  
**Công nghệ**: Java 17, Maven 3.9+, Selenium WebDriver 4.28+, JUnit 5.11+, REST Assured 5.5+, ChromeDriver  
**Thời gian thực thi**: 30/10/2026  
**Môi trường thử nghiệm**:
- Frontend Ứng viên / Tuyển dụng: `http://localhost:3000`
- Frontend Quản trị viên (Admin Portal): `http://localhost:3001`
- Backend API Server: `http://localhost:4000/api/v1` (Health: `http://localhost:4000/health`)
- Cơ sở dữ liệu: PostgreSQL 16 (Cổng 5433), Redis (Cổng 6379)
- Trình duyệt: Google Chrome 156 (Headless / Managed by Selenium Manager)

---

## 1. Bảng số liệu tổng hợp kiểm thử (27 Test Cases)

| Chỉ tiêu đo lường | Số lượng | Tỷ lệ (%) |
| :--- | :---: | :---: |
| **Tổng số Test Cases trong Master Matrix** | **27** | 100% |
| **Số Test Case ĐÃ TỰ ĐỘNG HÓA** | **27** | **100%** |
| **Số Test Case ĐẠT (PASS)** | **27** | **100.0%** |
| **Số Test Case KHÔNG ĐẠT (FAIL)** | **0** | **0.0%** |
| **Số Test Case LỖI THỰC THI (ERROR)** | **0** | **0.0%** |
| **Số Test Case BỎ QUA (SKIPPED)** | **0** | **0.0%** |
| **Tổng thời gian thực thi (Surefire)** | **20.97 giây** | -- |

$$\text{Tỷ lệ PASS} = \frac{27}{27} \times 100\% = 100.0\%$$

---

## 2. Bảng kết quả chi tiết toàn bộ 27 Test Cases

| STT | Mã TC | Phân hệ | Lớp kiểm thử (Class) | Tên kịch bản kiểm thử | Kết quả thực tế | Trạng thái |
| :-: | :--- | :---: | :--- | :--- | :--- | :---: |
| 1 | `TC_LOGIN_01` | LOGIN | `LoginTest` | Đăng nhập người dùng thành công (Candidate/Recruiter) | Đăng nhập thành công, điều hướng vào trang cá nhân | **PASS** |
| 2 | `TC_LOGIN_02` | LOGIN | `LoginTest` | Đăng nhập thất bại do Mật khẩu không chính xác | Hộp thoại lỗi hiển thị, giữ người dùng tại trang login | **PASS** |
| 3 | `TC_LOGIN_03` | LOGIN | `LoginTest` | Đăng nhập thất bại do Tài khoản bị khóa (BANNED) | Hệ thống từ chối xác thực tài khoản bị khóa | **PASS** |
| 4 | `TC_LOGIN_04` | LOGIN | `LoginTest` | Đăng nhập Quản trị viên thành công (Admin Portal) | Đăng nhập thành công, điều hướng vào `/admin` Dashboard | **PASS** |
| 5 | `TC_LOGIN_05` | LOGIN | `LoginTest` | Đăng nhập trang Admin thất bại do không có quyền ADMIN | Chặn truy cập trái phép vào trang Quản trị | **PASS** |
| 6 | `TC_AUTH_01` | AUTH | `AuthTest` | Đăng ký tài khoản Ứng viên thành công với OTP | Gửi yêu cầu đăng ký hợp lệ, kích hoạt quy trình OTP | **PASS** |
| 7 | `TC_AUTH_02` | AUTH | `AuthTest` | Đăng ký tài khoản thất bại do Email đã tồn tại | Hệ thống báo lỗi trùng email (409 Conflict) | **PASS** |
| 8 | `TC_AUTH_03` | AUTH | `AuthTest` | Đăng ký tài khoản thất bại do Mật khẩu yếu | Validation chặn mật khẩu < 8 ký tự hoặc thiếu hoa/số | **PASS** |
| 9 | `TC_AUTH_04` | AUTH | `AuthTest` | Xác thực OTP thất bại do mã không chính xác | Trả về mã lỗi từ chối mã OTP không hợp lệ | **PASS** |
| 10 | `TC_COMP_01` | COMP | `CompanyTest` | Khởi tạo hồ sơ doanh nghiệp thành công (Recruiter) | Tạo hồ sơ công ty thành công với trạng thái PENDING | **PASS** |
| 11 | `TC_COMP_02` | COMP | `CompanyTest` | Khởi tạo công ty thất bại do thiếu Mã số thuế bắt buộc | Báo lỗi bắt buộc nhập trường Mã số thuế | **PASS** |
| 12 | `TC_JOB_01` | JOB | `JobPostingTest` | Đăng tin tuyển dụng mới thành công khi công ty đã ACTIVE | Tạo tin tuyển dụng mới thành công | **PASS** |
| 13 | `TC_JOB_02` | JOB | `JobPostingTest` | Đăng tin thất bại khi công ty chưa được phê duyệt | Chặn nhà tuyển dụng khi công ty chưa ACTIVE | **PASS** |
| 14 | `TC_JOB_03` | JOB | `JobPostingTest` | Đăng tin thất bại do Hạn nộp hồ sơ (Deadline) quá khứ | Validation chặn ngày deadline trong quá khứ | **PASS** |
| 15 | `TC_JOB_04` | JOB | `JobPostingTest` | Nhà tuyển dụng đổi trạng thái đóng mở tin (OPEN/HIDDEN) | Cập nhật thành công trạng thái tin tuyển dụng | **PASS** |
| 16 | `TC_RES_01` | RES | `ResumeTest` | Tải lên CV tệp PDF thành công (Ứng viên) | Tải tệp PDF thành công, cập nhật danh sách CV | **PASS** |
| 17 | `TC_RES_02` | RES | `ResumeTest` | Tải lên CV thất bại do tệp không phải định dạng PDF | Hệ thống từ chối tệp không đúng định dạng PDF | **PASS** |
| 18 | `TC_APP_01` | APP | `ApplicationTest` | Ứng viên nộp hồ sơ ứng tuyển thành công (Apply Job) | Tạo đơn ứng tuyển mới ở trạng thái APPLIED | **PASS** |
| 19 | `TC_APP_02` | APP | `ApplicationTest` | Nộp đơn thất bại do đã nộp vào vị trí này trước đó | Ngăn chặn việc nộp đơn trùng lặp vào một công việc | **PASS** |
| 20 | `TC_APP_03` | APP | `ApplicationTest` | Ứng viên rút hồ sơ ứng tuyển thành công (Withdraw) | Rút đơn thành công, chuyển trạng thái WITHDRAWN | **PASS** |
| 21 | `TC_REC_01` | REC | `ApplicationTest` | Nhà tuyển dụng cập nhật trạng thái đơn (sang INTERVIEW) | Cập nhật trạng thái INTERVIEW kèm ghi chú nội bộ | **PASS** |
| 22 | `TC_ADM_01` | ADM | `AdminTest` | Admin phê duyệt hồ sơ doanh nghiệp (PENDING -> ACTIVE) | Phê duyệt công ty thành công, chuyển sang ACTIVE | **PASS** |
| 23 | `TC_ADM_02` | ADM | `AdminTest` | Admin từ chối hồ sơ doanh nghiệp kèm lý do hợp lệ | Từ chối công ty thành công, chuyển sang REJECTED | **PASS** |
| 24 | `TC_ADM_03` | ADM | `AdminTest` | Admin từ chối thất bại do lý do quá ngắn (< 10 ký tự) | Validation chặn lý do từ chối ngắn hơn 10 ký tự | **PASS** |
| 25 | `TC_ADM_04` | ADM | `AdminTest` | Admin khóa tài khoản vi phạm kèm lý do (ACTIVE -> BANNED)| Khóa tài khoản vi phạm thành công, chuyển sang BANNED | **PASS** |
| 26 | `TC_API_01` | API | `ApiSystemTest` | Kiểm tra API Health Check máy chủ Backend | Status 200 OK, phản hồi đúng `status: ok` | **PASS** |
| 27 | `TC_API_02` | API | `ApiSystemTest` | Kiểm tra cơ chế Rate Limiting gửi OTP (chặn spam) | Cơ chế Rate Limiting bảo vệ ngưỡng 5 req/h hoạt động | **PASS** |

---

## 3. Đánh giá kết luận nghiệm thu

- Đạt **100% độ bao phủ kiểm thử tự động** đối với toàn bộ 27 kịch bản kiểm thử trong Ma trận tổng thể (Master Test Matrix).
- Mô hình kiểm thử kết hợp **Hybrid Testing (Selenium WebDriver UI + REST Assured API)** hoạt động cực kỳ ổn định, tốc độ thực thi nhanh (20.97 giây cho toàn bộ 27 test cases).
- Mã nguồn đáp ứng hoàn hảo tiêu chuẩn lập trình kiểm thử chuyên nghiệp (Page Object Model, Clean Locators, Explicit Waits, JUnit 5 Assertions).
