# Selenium WebDriver & REST Assured Automation Testing – Job Platform

## 1. Giới thiệu

Dự án xây dựng bộ kiểm thử tự động hóa toàn diện cho website tuyển dụng trực tuyến **Job Platform** bằng **Selenium WebDriver**, **REST Assured** và **Java 17**. 

Mục tiêu là tự động hóa toàn bộ **27 Test Cases** được thiết kế trong **Ma trận kiểm thử tổng thể (Master Test Matrix)**, bao phủ đầy đủ các phân hệ người dùng (Ứng viên, Nhà tuyển dụng, Quản trị viên hệ thống), phát hiện lỗi, đối soát tính toàn vẹn của chu trình vòng đời đơn ứng tuyển (*Application Lifecycle*), kiểm thử an ninh/bảo mật, và đánh giá mức độ đáp ứng của hệ thống theo Tài liệu Đặc tả Yêu cầu Phần mềm (SRS).

## 2. Công nghệ sử dụng

- **Ngôn ngữ**: Java 17 (LTS)
- **Công cụ quản lý & Build Tool**: Apache Maven 3.9+
- **Thư viện kiểm thử tự động UI**: Selenium WebDriver 4.28+
- **Thư viện kiểm thử API**: REST Assured 5.5+
- **Test Framework**: JUnit 5 (JUnit Jupiter 5.11+)
- **Quản lý trình duyệt**: Selenium Manager (Tự động tải & quản lý ChromeDriver)
- **Trình duyệt thực thi**: Google Chrome (phiên bản ổn định)
- **Kiến trúc kiểm thử**: Mô hình Page Object Model (POM) kết hợp Hybrid Testing (UI & API)

## 3. Phạm vi kiểm thử (27 Test Cases toàn diện)

Bộ kiểm thử tự động bao phủ trọn vẹn 8 phân hệ cốt lõi:

1. **Xác thực đăng nhập (Login - 5 TCs)**:
   - `TC_LOGIN_01`: Đăng nhập người dùng thành công (Candidate/Recruiter).
   - `TC_LOGIN_02`: Đăng nhập thất bại do sai mật khẩu.
   - `TC_LOGIN_03`: Đăng nhập thất bại do tài khoản bị khóa (`BANNED`).
   - `TC_LOGIN_04`: Đăng nhập Quản trị viên thành công (Admin Portal).
   - `TC_LOGIN_05`: Đăng nhập trang Quản trị thất bại do không có quyền ADMIN.
2. **Đăng ký và xác thực (Auth & OTP - 4 TCs)**:
   - `TC_AUTH_01`: Đăng ký tài khoản Ứng viên thành công với OTP.
   - `TC_AUTH_02`: Đăng ký tài khoản thất bại do Email đã tồn tại (409 Conflict).
   - `TC_AUTH_03`: Đăng ký thất bại do Mật khẩu yếu (< 8 ký tự hoặc thiếu hoa/số).
   - `TC_AUTH_04`: Xác thực OTP thất bại do mã không chính xác.
3. **Hồ sơ doanh nghiệp (Company Profile - 2 TCs)**:
   - `TC_COMP_01`: Khởi tạo hồ sơ công ty thành công (Recruiter - trạng thái `PENDING`).
   - `TC_COMP_02`: Khởi tạo công ty thất bại do thiếu Mã số thuế bắt buộc.
4. **Đăng tin và quản lý việc làm (Job Management - 4 TCs)**:
   - `TC_JOB_01`: Đăng tin tuyển dụng thành công khi công ty đã `ACTIVE`.
   - `TC_JOB_02`: Đăng tin thất bại khi công ty chưa được phê duyệt (403 Forbidden).
   - `TC_JOB_03`: Đăng tin thất bại do Hạn nộp hồ sơ (Deadline) trong quá khứ.
   - `TC_JOB_04`: Nhà tuyển dụng thay đổi trạng thái đóng mở tin (`OPEN` $\leftrightarrow$ `HIDDEN` $\leftrightarrow$ `CLOSED`).
5. **Hồ sơ và CV ứng viên (Resume - 2 TCs)**:
   - `TC_RES_01`: Tải lên CV tệp PDF thành công.
   - `TC_RES_02`: Tải lên CV thất bại do tệp không phải định dạng PDF.
6. **Vòng đời đơn ứng tuyển (Application Lifecycle - 4 TCs)**:
   - `TC_APP_01`: Ứng viên nộp hồ sơ ứng tuyển thành công (`APPLIED`).
   - `TC_APP_02`: Nộp đơn thất bại do ứng tuyển trùng lặp vào một vị trí (409 Conflict).
   - `TC_APP_03`: Ứng viên rút hồ sơ ứng tuyển thành công (`WITHDRAWN`).
   - `TC_REC_01`: Nhà tuyển dụng cập nhật trạng thái đơn (`APPLIED` $\rightarrow$ `VIEWED` $\rightarrow$ `INTERVIEW`).
7. **Quản trị hệ thống (Admin Portal - 4 TCs)**:
   - `TC_ADM_01`: Admin phê duyệt hồ sơ doanh nghiệp (`PENDING` $\rightarrow$ `ACTIVE`).
   - `TC_ADM_02`: Admin từ chối hồ sơ doanh nghiệp kèm lý do hợp lệ (`REJECTED`).
   - `TC_ADM_03`: Admin từ chối thất bại do lý do quá ngắn (< 10 ký tự).
   - `TC_ADM_04`: Admin khóa tài khoản người dùng vi phạm kèm lý do (`BANNED`).
8. **Kiểm thử hệ thống Backend & API (System & Security - 2 TCs)**:
   - `TC_API_01`: Kiểm tra API Health Check máy chủ Backend (`/health`).
   - `TC_API_02`: Kiểm tra cơ chế giới hạn tần suất gửi OTP (Rate Limiting).

## 4. Cấu trúc dự án

Bộ mã nguồn kiểm thử tự động được tổ chức theo chuẩn kiến trúc Page Object Model (POM):

```
job-platform-main/
├── pom.xml                                   # Cấu hình Maven dependencies & plugins
├── README.md                                 # Tài liệu hướng dẫn & báo cáo kiểm thử
├── test-results/
│   └── test-summary.md                       # Báo cáo tổng hợp số liệu 27 Test Cases
├── screenshots/                              # Thư mục lưu ảnh chụp màn hình & bằng chứng kiểm thử
│   ├── POM-architecture-tree.png
│   ├── TC01-login-success.png
│   ├── TC02-job-search-results.png
│   ├── TC03-application-submitted.png
│   ├── TC04-recruiter-application-status.png
│   ├── TC05-admin-portal-dashboard.png
│   ├── TC06-terminal-execution-27-tests.png
│   └── TC07-surefire-reports.png
└── test/
    └── java/
        └── com/
            └── jobplatform/
                ├── base/
                │   └── BaseTest.java         # Khởi tạo WebDriver, timeout, helper methods
                ├── pages/                    # 6 Lớp Page Object Model
                │   ├── LoginPage.java
                │   ├── RegisterPage.java
                │   ├── CompanyPage.java
                │   ├── JobPostPage.java
                │   ├── ResumePage.java
                │   ├── JobDetailPage.java
                │   ├── JobSearchPage.java
                │   ├── ApplicationPage.java
                │   ├── RecruiterApplicationsPage.java
                │   └── AdminPortalPage.java
                └── tests/                    # 8 Lớp kiểm thử JUnit 5 (27 Test Cases)
                    ├── LoginTest.java        # 5 Test Cases (TC_LOGIN_01 -> 05)
                    ├── AuthTest.java         # 4 Test Cases (TC_AUTH_01 -> 04)
                    ├── CompanyTest.java      # 2 Test Cases (TC_COMP_01 -> 02)
                    ├── JobPostingTest.java   # 4 Test Cases (TC_JOB_01 -> 04)
                    ├── ResumeTest.java       # 2 Test Cases (TC_RES_01 -> 02)
                    ├── ApplicationTest.java  # 4 Test Cases (TC_APP_01 -> 03, TC_REC_01)
                    ├── AdminTest.java        # 4 Test Cases (TC_ADM_01 -> 04)
                    └── ApiSystemTest.java    # 2 Test Cases (TC_API_01 -> 02)
```

## 5. Yêu cầu môi trường

- **JDK**: Java Development Kit 17 trở lên (`java -version`).
- **Maven**: Phiên bản 3.9+ (`mvn -version`).
- **Trình duyệt**: Google Chrome bản mới nhất.
- **Hạ tầng dịch vụ thử nghiệm**:
  - Backend API: `http://localhost:4000` (Kiểm tra: `curl http://localhost:4000/health`)
  - Frontend Web (Ứng viên / Nhà tuyển dụng): `http://localhost:3000`
  - Admin Portal: `http://localhost:3001`
  - Cơ sở dữ liệu: PostgreSQL 16 (Cổng 5433), Redis (Cổng 6379)
- **Tài khoản kiểm thử định danh**:
  - Candidate: `ungvien@test.com` / `Candidate123`
  - Recruiter: `recruiter@jobplatform.app` / `Recruiter123`
  - Admin: `camly@jobplatform.app` / `Admin123`

## 6. Cách chạy kiểm thử

Mở Terminal tại thư mục gốc dự án:

### Cách 1: Chạy toàn bộ 27 Test Cases (Full Test Suite)
```bash
mvn clean test
```
*Để chạy chế độ không giao diện (Headless mode) tối ưu tốc độ:*
```bash
mvn clean test -Dheadless=true
```

### Cách 2: Chạy theo từng lớp kiểm thử độc lập
```bash
# 1. Kiểm thử phân hệ Đăng nhập (5 TCs)
mvn -Dtest=LoginTest test

# 2. Kiểm thử phân hệ Đăng ký & OTP (4 TCs)
mvn -Dtest=AuthTest test

# 3. Kiểm thử phân hệ Doanh nghiệp (2 TCs)
mvn -Dtest=CompanyTest test

# 4. Kiểm thử phân hệ Đăng tin tuyển dụng (4 TCs)
mvn -Dtest=JobPostingTest test

# 5. Kiểm thử phân hệ Tải lên CV (2 TCs)
mvn -Dtest=ResumeTest test

# 6. Kiểm thử phân hệ Vòng đời đơn ứng tuyển (4 TCs)
mvn -Dtest=ApplicationTest test

# 7. Kiểm thử phân hệ Quản trị viên (4 TCs)
mvn -Dtest=AdminTest test

# 8. Kiểm thử API Backend & Rate Limiting (2 TCs)
mvn -Dtest=ApiSystemTest test
```

## 7. Báo cáo kết quả kiểm thử thực tế

Kết quả thực thi nghiệm thu toàn bộ bộ kiểm thử tự động:

| Chỉ tiêu đo lường | Số lượng | Tỷ lệ (%) |
| :--- | :---: | :---: |
| **Tổng số Test Case** | **27** | 100% |
| **Số Test Case ĐẠT (PASS)** | **27** | **100.0%** |
| **Số Test Case KHÔNG ĐẠT (FAIL)** | **0** | **0.0%** |
| **Số Test Case LỖI (ERROR)** | **0** | **0.0%** |
| **Số Test Case BỎ QUA (SKIPPED)** | **0** | **0.0%** |
| **Tỷ lệ PASS** | **100.0%** | -- |
| **Tổng thời gian thực thi (Surefire)** | **20.97 giây** | -- |

$$\text{Tỷ lệ PASS} = \frac{27}{27} \times 100\% = 100.0\%$$

Toàn bộ báo cáo XML và nhật ký thực thi chi tiết được lưu trữ tại:
`target/surefire-reports/`

### 7.2. Danh mục bằng chứng & ảnh chụp màn hình kiểm thử (Screenshots)
Toàn bộ ảnh chụp màn hình minh chứng và sơ đồ kiến trúc đã được chụp và lưu trữ trong thư mục `screenshots/` cũng như nhúng trực tiếp vào tài liệu báo cáo `BC KT.docx`:
- `screenshots/POM-architecture-tree.png`: Sơ đồ kiến trúc Page Object Model (POM) và phân lớp kiểm thử trong dự án (Hình 4.1 trong BC KT.docx).
- `screenshots/TC01-login-success.png`: Minh họa đăng nhập ứng viên thành công và xác thực phiên làm việc JWT (Hình 5.2 trong BC KT.docx).
- `screenshots/TC02-job-search-results.png`: Minh họa giao diện tìm kiếm việc làm và bộ lọc dữ liệu đa chiều (Hình 6.1 trong BC KT.docx).
- `screenshots/TC03-application-submitted.png`: Minh họa nộp hồ sơ ứng tuyển trực tuyến và xác nhận trạng thái APPLIED (Hình 6.2 trong BC KT.docx).
- `screenshots/TC04-recruiter-application-status.png`: Minh họa quy trình xử lý đơn ứng tuyển và cập nhật trạng thái phỏng vấn INTERVIEW của Nhà tuyển dụng (Hình 6.3 trong BC KT.docx).
- `screenshots/TC05-admin-portal-dashboard.png`: Minh họa trang quản trị Admin phê duyệt doanh nghiệp PENDING sang ACTIVE (Hình 6.4 trong BC KT.docx).
- `screenshots/TC06-terminal-execution-27-tests.png`: Minh họa thực thi kiểm thử tự động toàn bộ 27 Test Cases đạt 100% PASS trên Terminal bằng Maven Surefire (Hình 5.1 trong BC KT.docx).
- `screenshots/TC07-surefire-reports.png`: Minh họa cấu trúc thư mục báo cáo kiểm thử Surefire Reports và biên bản tổng kết test-summary.md (Hình 7.1 trong BC KT.docx).

## 8. Kết luận

Dự án đã tự động hóa thành công **100% (27/27) các kịch bản kiểm thử** được thiết kế trong tài liệu Báo cáo Kiểm thử phần mềm. Hệ thống kiểm thử hoạt động ổn định, kiến trúc phân lớp Page Object Model rõ ràng, đáp ứng toàn diện mọi yêu cầu nghiệm thu bài tập lớn giữa kỳ của môn học.
