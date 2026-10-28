# Selenium WebDriver Automation Testing – Job Platform

## 1. Giới thiệu

Dự án xây dựng bộ kiểm thử tự động cho website tuyển dụng trực tuyến **Job Platform** bằng **Selenium WebDriver** và **Java 17**. Mục tiêu là kiểm tra toàn diện các chức năng quan trọng trên giao diện người dùng (Ứng viên, Nhà tuyển dụng, Quản trị viên), phát hiện lỗi, đối soát tính toàn vẹn của chu trình vòng đời đơn ứng tuyển (*Application Lifecycle*), và đánh giá mức độ đáp ứng của hệ thống theo Tài liệu Đặc tả Yêu cầu Phần mềm (SRS).

## 2. Công nghệ sử dụng

- **Ngôn ngữ**: Java 17 (LTS)
- **Công cụ quản lý dự án & Build Tool**: Apache Maven 3.9+
- **Thư viện kiểm thử tự động UI**: Selenium WebDriver 4.28+
- **Test Framework**: JUnit 5 (JUnit Jupiter 5.11+)
- **Quản lý trình duyệt**: Selenium Manager (Tự động tải & quản lý ChromeDriver)
- **Trình duyệt thực thi**: Google Chrome (phiên bản ổn định)
- **Thiết kế kiểm thử**: Mô hình Page Object Model (POM)
- **Thư viện kiểm thử API & JSON**: REST Assured 5.5+, Jackson 2.18+

## 3. Phạm vi kiểm thử

Dự án tập trung kiểm thử tự động các luồng nghiệp vụ cốt lõi:

1. **Xác thực người dùng (Authentication)**:
   - Mở trang đăng nhập ứng viên và quản trị viên.
   - Đăng nhập thành công với tài khoản ứng viên hợp lệ.
   - Đăng nhập thất bại khi nhập sai mật khẩu (hiển thị Dialog cảnh báo).
   - Đăng nhập thành công vào Admin Portal (`/admin`).
2. **Tìm kiếm việc làm (Job Search & Filter)**:
   - Tìm kiếm công việc theo từ khóa tiêu đề/kỹ năng.
   - Lọc việc làm theo địa điểm làm việc và danh mục ngành nghề.
3. **Vòng đời đơn ứng tuyển (Application Lifecycle)**:
   - Ứng viên xem chi tiết tin tuyển dụng và nộp đơn ứng tuyển (`APPLIED`).
   - Nhà tuyển dụng (Recruiter) mở xem hồ sơ ứng viên (`VIEWED`).
   - Nhà tuyển dụng cập nhật lịch và chuyển trạng thái phỏng vấn (`INTERVIEW`).
   - Ứng viên chủ động rút đơn ứng tuyển khi còn trong trạng thái hợp lệ (`WITHDRAWN`).

## 4. Cấu trúc dự án

Bộ mã nguồn kiểm thử tự động được tổ chức theo chuẩn kiến trúc Page Object Model (POM):

```
job-platform-main/
├── pom.xml                                   # Cấu hình Maven dependencies & plugins
├── README.md                                 # Tài liệu hướng dẫn & báo cáo kiểm thử
├── test-results/
│   └── test-summary.md                       # Báo cáo tổng hợp số liệu kiểm thử
├── screenshots/                              # Thư mục lưu ảnh chụp màn hình khi có lỗi
│   └── .gitkeep
└── test/
    └── java/
        └── com/
            └── jobplatform/
                ├── base/
                │   └── BaseTest.java         # Khởi tạo WebDriver, timeout, screenshot
                ├── pages/                    # Các lớp Page Object Model
                │   ├── LoginPage.java
                │   ├── JobSearchPage.java
                │   ├── JobDetailPage.java
                │   ├── ApplicationPage.java
                │   └── RecruiterApplicationsPage.java
                └── tests/                    # Các lớp kiểm thử JUnit 5
                    ├── LoginTest.java
                    ├── JobSearchTest.java
                    ├── ApplicationSubmissionTest.java
                    ├── ApplicationStatusTest.java
                    └── ApplicationWithdrawalTest.java
```

## 5. Yêu cầu môi trường

Để chạy được bộ kiểm thử, môi trường cần chuẩn bị:

- **JDK**: Java Development Kit 17 trở lên (`java -version`).
- **Maven**: Phiên bản 3.9+ (`mvn -version`).
- **Trình duyệt**: Google Chrome bản mới nhất.
- **Dịch vụ hệ thống đang hoạt động**:
  - Backend API: `http://localhost:4000` (Kiểm tra bằng: `curl http://localhost:4000/health`)
  - Frontend Web (Ứng viên / Tuyển dụng): `http://localhost:3000`
  - Admin Portal: `http://localhost:3001`
  - Cơ sở dữ liệu: PostgreSQL (cổng 5433) và Redis (cổng 6379)
- **Tài khoản kiểm thử định danh**:
  - Candidate: `ungvien@test.com` / `Candidate123`
  - Recruiter: `recruiter@jobplatform.app` / `Recruiter123`
  - Admin: `camly@jobplatform.app` / `Admin123`

## 6. Cách chạy kiểm thử

Mở Terminal tại thư mục gốc dự án:

### Cách 1: Chạy toàn bộ Test Suite
```bash
mvn clean test
```

### Cách 2: Chạy từng lớp kiểm thử độc lập
```bash
# Kiểm thử chức năng Đăng nhập
mvn -Dtest=LoginTest test

# Kiểm thử chức năng Tìm kiếm việc làm
mvn -Dtest=JobSearchTest test

# Kiểm thử luồng Ứng viên nộp đơn
mvn -Dtest=ApplicationSubmissionTest test

# Kiểm thử Nhà tuyển dụng chuyển trạng thái đơn
mvn -Dtest=ApplicationStatusTest test

# Kiểm thử Ứng viên rút đơn ứng tuyển
mvn -Dtest=ApplicationWithdrawalTest test
```

## 7. Báo cáo kết quả

Kết quả thực thi thực tế của bộ kiểm thử tự động:

| Chỉ tiêu | Kết quả thực tế |
| :--- | :---: |
| **Tổng số Test Case** | **8** |
| **Số Test Case ĐẠT (PASS)** | **8** |
| **Số Test Case KHÔNG ĐẠT (FAIL)** | **0** |
| **Số Test Case LỖI (ERROR)** | **0** |
| **Số Test Case BỎ QUA (SKIPPED)** | **0** |
| **Tỷ lệ PASS** | **100.0%** |

Báo cáo chi tiết dạng XML và văn bản được Maven Surefire tự động xuất tại thư mục:
`target/surefire-reports/`

## 8. Kết luận

Bộ kiểm thử Selenium WebDriver đã tự động hóa thành công toàn bộ các chức năng then chốt của website tuyển dụng. Hệ thống xử lý mượt mà việc chuyển trang, hiển thị đúng các modal thông báo và tuân thủ chặt chẽ quy tắc máy trạng thái của đơn ứng tuyển. Sản phẩm đạt đầy đủ tiêu chuẩn nghiệm thu của bài tập lớn môn học.

## 9. Hạn chế

- Kịch bản kiểm thử phụ thuộc vào dữ liệu có sẵn trong cơ sở dữ liệu (cần có ít nhất 1 tin tuyển dụng đang mở).
- Một số quy trình liên quan đến cổng thanh toán bên thứ ba (VNPAY) và xác thực OAuth Google của ứng viên cần chạy trên môi trường có mạng ngoài để tích hợp đầy đủ.
- Hiện tại kiểm thử đang tập trung vào kịch bản đơn luồng (single-thread); kịch bản kiểm thử đa luồng đồng thời (concurrency) sẽ tiếp tục được mở rộng trong các phiên bản sau.
