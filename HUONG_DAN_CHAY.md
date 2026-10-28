# HƯỚNG DẪN KHỞI CHẠY DỰ ÁN JOB PLATFORM

> **Tài liệu hướng dẫn chi tiết dành cho Developer / Tester để thiết lập, cấu hình và vận hành toàn bộ hệ sinh thái Job Platform từ số 0.**

---

## MỤC LỤC
1. [Tổng Quan Kiến Trúc Hệ Thống](#1-tổng-quan-kiến-trúc-hệ-thống)
2. [Yêu Cầu Môi Trường (Prerequisites)](#2-yêu-cầu-môi-trường-prerequisites)
3. [Cấu Trúc Thư Mục Dự Án](#3-cấu-trúc-thư-mục-dự-án)
4. [Cài Đặt Dependencies](#4-cài-đặt-dependencies)
5. [Cấu Hình Biến Môi Trường (.env)](#5-cấu-hình-biến-môi-trường-env)
6. [Khởi Động Cơ Sở Dữ Liệu (Docker & Migration)](#6-khởi-động-cơ-sở-dữ-liệu-docker--migration)
7. [Khởi Chạy Ứng Dụng (Development)](#7-khởi-chạy-ứng-dụng-development)
8. [Tài Khoản Mẫu & Hướng Dẫn Kiểm Thử (Testing)](#8-tài-khoản-mẫu--hướng-dẫn-kiểm-thử-testing)
9. [Các Lệnh Tiện Ích & Script Kiểm Tra Hệ Thống](#9-các-lệnh-tiện-ích--script-kiểm-tra-hệ-thống)
10. [Khắc Phục Sự Cố Thường Gặp (Troubleshooting)](#10-khắc-phục-sự-cố-thường-gặp-troubleshooting)

---

## 1. TỔNG QUAN KIẾN TRÚC HỆ THỐNG

Dự án **Job Platform** được xây dựng theo kiến trúc **Monorepo** sử dụng **npm Workspaces**, bao gồm 3 ứng dụng chính kết nối với hệ thống cơ sở dữ liệu và các dịch vụ đám mây:

```
                            ┌────────────────────────────────────────┐
                            │               TRÌNH DUYỆT              │
                            └────┬──────────────────────────────┬────┘
                                 │ :3000                        │ :3001
                                 ▼                              ▼
                 ┌──────────────────────────────┐ ┌──────────────────────────────┐
                 │        apps/frontend         │ │     apps/frontend-admin      │
                 │   Next.js 16 (App Router)    │ │   Next.js 16 (App Router)    │
                 │   Portal Ứng viên & Tuyển dụng│ │   Dashboard Quản trị viên    │
                 └──────────────┬───────────────┘ └──────────────┬───────────────┘
                                │                                │
                                └───────────────┬────────────────┘
                                                │ REST API (:4000/api/v1)
                                                ▼
                                ┌──────────────────────────────┐
                                │         apps/backend         │
                                │   Express 5 + TypeScript     │
                                │    TypeORM 0.3 + Zod         │
                                └──────┬───────┬────────┬──────┘
                                       │       │        │
                   ┌───────────────────┘       │        └───────────────────┐
                   ▼                           ▼                            ▼
      ┌─────────────────────────┐ ┌─────────────────────────┐  ┌─────────────────────────┐
      │   PostgreSQL 17         │ │   Redis (Upstash / Local)│  │   Supabase Storage      │
      │   (Docker Port: 5433)   │ │   Rate limit, OTP, Cache │  │   Lưu CV PDF, Avatar,   │
      │   Lưu trữ dữ liệu chính │ └─────────────────────────┘  │   Logo doanh nghiệp     │
      └─────────────────────────┘                              └─────────────────────────┘
```

### Bảng phân bổ Port & URL truy cập:

| Thành phần | Cổng (Port) | URL truy cập | Vai trò |
| :--- | :---: | :--- | :--- |
| **Frontend Web** | `3000` | `http://localhost:3000` | Cổng thông tin cho Ứng viên (tìm việc, nộp CV) & Nhà tuyển dụng (đăng tin, duyệt hồ sơ) |
| **Frontend Admin** | `3001` | `http://localhost:3001` | Trang quản trị hệ thống (duyệt tin tuyển dụng, duyệt công ty, quản lý user, xem logs, thống kê) |
| **Backend API** | `4000` | `http://localhost:4000/api/v1` | REST API Server xử lý toàn bộ logic nghiệp vụ, phân quyền, cronjob |
| **PostgreSQL** | `5433` | `localhost:5433` | Cơ sở dữ liệu quan hệ (map cổng `5433:5432` từ container Docker) |
| **Redis** *(tùy chọn)* | `6379` | `localhost:6379` | Cache / Rate limit local qua Docker |

---

## 2. YÊU CẦU MÔI TRƯỜNG (PREREQUISITES)

Trước khi bắt đầu, hãy đảm bảo máy tính đã cài đặt các công cụ sau:

1. **Node.js**: Phiên bản `v20.x` trở lên (Khuyến nghị bản LTS như `v20` hoặc `v22`).
2. **npm**: Phiên bản `10.x` trở lên (đi kèm với Node.js, hỗ trợ npm workspaces).
3. **Docker & Docker Desktop**: Để chạy PostgreSQL và Redis độc lập mà không cần cài đặt trực tiếp vào hệ điều hành.
4. **Git**: Để quản lý mã nguồn.

Kiểm tra phiên bản bằng terminal:
```bash
node -v
npm -v
docker -v
docker compose version
```

---

## 3. CẤU TRÚC THƯ MỤC DỰ ÁN

```text
job-platform/
├── apps/
│   ├── backend/               # API Server (Express.js, TypeORM, PostgreSQL, Supabase, JWT)
│   │   ├── src/
│   │   │   ├── common/        # Middlewares, bảo mật (JWT, OTP), Mailer, Storage Supabase
│   │   │   ├── database/      # TypeORM Entities & Database Migrations (Seeds sẵn data)
│   │   │   ├── jobs/          # Cron job đóng tin tuyển dụng hết hạn hàng ngày
│   │   │   ├── modules/       # Các module nghiệp vụ (auth, jobs, companies, users, admin...)
│   │   │   └── server.ts      # Entrypoint khởi động API server
│   │   ├── scripts/           # Script nạp dữ liệu địa phương (Vietnamese locations)
│   │   └── .env.example       # Mẫu biến môi trường cho Backend
│   │
│   ├── frontend/              # Giao diện chính (Next.js 16, React 19, Tailwind CSS v4)
│   │   ├── src/
│   │   │   ├── app/           # App Router: Public pages, (candidate) portal, (recruiter) portal
│   │   │   ├── components/    # UI components (Shadcn UI, Rich text editor, Job filters)
│   │   │   └── services/      # HTTP Client kết nối tới Backend (:4000/api/v1)
│   │   └── .env.example       # Mẫu biến môi trường cho Frontend
│   │
│   └── frontend-admin/        # Giao diện Quản trị viên (Next.js 16, Recharts, Dashboard)
│       ├── src/
│       │   ├── app/           # Admin pages: Dashboard, Jobs, Companies, Users, Logs, Stats
│       │   └── contexts/      # Auth Context quản lý phiên đăng nhập Admin
│       └── .env.example       # Mẫu biến môi trường cho Admin
│
├── docker-compose.yml         # Cấu hình container PostgreSQL 17 và Redis 7
├── package.json               # Root package cấu hình npm Workspaces & scripts chạy nhanh
└── README.md                  # Tài liệu gốc của dự án
```

---

## 4. CÀI ĐẶT DEPENDENCIES

Tại **thư mục gốc (root)** của dự án, chạy lệnh:

```bash
npm install
```

> **Ghi chú**: Dự án sử dụng `npm workspaces`, vì vậy `npm install` ở root sẽ tự động liên kết và cài đặt toàn bộ dependencies cho cả 3 ứng dụng `backend`, `frontend`, và `frontend-admin`. Bạn **không cần** phải `cd` vào từng thư mục để install riêng lẻ.

---

## 5. CẤU HÌNH BIẾN MÔI TRƯỜNG (.ENV)

Hệ thống cần 3 file `.env` riêng biệt cho 3 ứng dụng:

```
apps/backend/.env
apps/frontend/.env
apps/frontend-admin/.env
```

### 5.1. Cấu hình Backend (`apps/backend/.env`)

Tạo file bằng cách copy từ file mẫu:
```bash
cp apps/backend/.env.example apps/backend/.env
```

Mở file `apps/backend/.env` và thiết lập các giá trị tối thiểu để chạy:

```env
PORT=4000

# ---------------------------------------------------------------
# KẾT NỐI POSTGRESQL (DOCKER)
# ---------------------------------------------------------------
DB_HOST=127.0.0.1
# LƯU Ý: docker-compose map cổng 5433 ra máy host để không trùng 5432
DB_PORT=5433
DB_USER=postgres
DB_PASSWORD=postgres
DB_NAME=job_platform
DB_SSL=false

# ---------------------------------------------------------------
# BẢO MẬT & JWT (BẮT BUỘC ĐIỀN)
# ---------------------------------------------------------------
# Chuỗi bí mật ký Access Token (tối thiểu 32 ký tự). BẮT BUỘC PHẢI CÓ nếu không login sẽ lỗi!
# Có thể sinh bằng lệnh: openssl rand -hex 32
JWT_ACCESS_SECRET=c1f4e58b88d2279bc901a18276f7fbb6b4904b684cb32d201101e40ebad82e21

COOKIE_SECURE=false

# ---------------------------------------------------------------
# GỬI MÃ OTP & EMAIL (CHẾ ĐỘ DEV)
# ---------------------------------------------------------------
# Đặt là "console": Mã OTP xác thực email khi đăng ký sẽ in thẳng ra Terminal Backend!
# Giúp bạn test luồng đăng ký nhanh mà không cần tạo tài khoản Brevo hay Gmail.
MAIL_PROVIDER=console

# ---------------------------------------------------------------
# CORS & RATE LIMIT
# ---------------------------------------------------------------
CORS_ORIGINS=http://localhost:3000,http://localhost:3001
TRUST_PROXY=false
# Đặt hệ số 100 để không bị block trong quá trình test
RATE_LIMIT_FACTOR=100

# ---------------------------------------------------------------
# GOOGLE OAUTH (TÙY CHỌN - ĐÃ CÓ KEY SẴN HOẶC DÙNG KEY MỚI)
# ---------------------------------------------------------------
GOOGLE_CLIENT_ID=163515871284-qq4b4jgbqt2nj0iltgfl8bk3apn2v48t.apps.googleusercontent.com

# ---------------------------------------------------------------
# UPSTASH REDIS / IN-MEMORY FALLBACK (TỰ ĐỘNG)
# ---------------------------------------------------------------
# Hệ thống hỗ trợ In-Memory Storage tự động khi để trống hoặc dùng giá trị mẫu.
# Bạn không cần phải có tài khoản Upstash Redis mà vẫn thực hiện được toàn bộ
# các tính năng OTP, xác thực email, và phân quyền trơn tru trên môi trường dev!
UPSTASH_REDIS_REST_URL=
UPSTASH_REDIS_REST_TOKEN=

# ---------------------------------------------------------------
# SUPABASE STORAGE (TÙY CHỌN KHI CẦN UPLOAD CV / LOGO / AVATAR)
# ---------------------------------------------------------------
SUPABASE_URL=
SUPABASE_SERVICE_ROLE_KEY=
SUPABASE_STORAGE_BUCKET=job-platform-assets
SUPABASE_STORAGE_PRIVATE_BUCKET=job-platform-private
```

> [!IMPORTANT]
> **Đặc biệt lưu ý:**
> 1. `DB_PORT=5433`: Cổng Docker PostgreSQL là **5433**, không phải 5432!
> 2. `JWT_ACCESS_SECRET`: Không được để trống. Nếu bỏ trống, bạn sẽ gặp lỗi `Missing JWT_ACCESS_SECRET` khi đăng nhập.
> 3. `MAIL_PROVIDER=console`: Khi đăng ký tài khoản mới, mã OTP 6 số sẽ xuất hiện ngay tại cửa sổ Terminal chạy Backend.

---

### 5.2. Cấu hình Frontend (`apps/frontend/.env`)

Tạo file:
```bash
cp apps/frontend/.env.example apps/frontend/.env
```

Nội dung chuẩn trong `apps/frontend/.env`:
```env
# URL API của Backend (không kèm /api/v1 ở đuôi, client tự nối)
NEXT_PUBLIC_API_BASE_URL=http://localhost:4000

# Google Client ID cho đăng nhập Google (khớp với backend)
NEXT_PUBLIC_GOOGLE_CLIENT_ID=163515871284-qq4b4jgbqt2nj0iltgfl8bk3apn2v48t.apps.googleusercontent.com
```

---

### 5.3. Cấu hình Frontend Admin (`apps/frontend-admin/.env`)

Tạo file:
```bash
cp apps/frontend-admin/.env.example apps/frontend-admin/.env
```

Nội dung chuẩn trong `apps/frontend-admin/.env`:
```env
# URL API Backend
NEXT_PUBLIC_API_BASE_URL=http://localhost:4000

# Cấu hình Supabase để hiển thị logo/ảnh (tùy chọn)
NEXT_PUBLIC_SUPABASE_URL=https://zcgrjwbmzghvjtpocxkx.supabase.co
NEXT_PUBLIC_SUPABASE_STORAGE_BUCKET=job-platform-assets
```

---

## 6. KHỞI ĐỘNG CƠ SỞ DỮ LIỆU (DOCKER & MIGRATION)

### Bước 6.1: Khởi động PostgreSQL qua Docker Compose

Đảm bảo **Docker Desktop** đang chạy trên máy của bạn. Tại thư mục root, chạy:

```bash
docker compose up -d postgres
```

Kiểm tra container đang chạy:
```bash
docker ps
```
Bạn sẽ thấy container `job-platform-postgres` có trạng thái `Up` và cổng `0.0.0.0:5433->5432/tcp`.

*(Tùy chọn)* Nếu muốn khởi động thêm Redis container:
```bash
docker compose up -d redis
```

### Bước 6.2: Chạy Database Migration

Sau khi PostgreSQL đã sẵn sàng, thực thi migration để khởi tạo các bảng và nạp dữ liệu hệ thống:

```bash
npm run migration:run -w backend
```

Nếu thành công, terminal sẽ thông báo:
```text
Migration InitialEntitiesMigration1740000000000 has been executed successfully.
Migration CreateLocations1787128685000 has been executed successfully.
...
Migration SeedRoles1787300000000 has been executed successfully.
Migration SeedPermissions1787300000001 has been executed successfully.
Migration SeedSkillCatalog1787360000000 has been executed successfully.
Migration SeedAdminTesters1787370000000 has been executed successfully.
```

> [!NOTE]
> **Dữ liệu được nạp tự động bởi migration bao gồm:**
> - Toàn bộ các bảng nghiệp vụ (`users`, `jobs`, `companies`, `applications`, `resumes`, `notifications`, `system_logs`...).
> - **Roles**: `CANDIDATE`, `RECRUITER`, `ADMIN`.
> - **Permissions**: Phân quyền chi tiết cho từng vai trò.
> - **Catalog**: Hơn 20 kỹ năng, ngoại ngữ và chứng chỉ phổ biến (JavaScript, React, Node.js, IELTS, TOEIC...).
> - **Tài khoản Admin kiểm thử**: Đã tạo sẵn 2 tài khoản Admin để đăng nhập ngay mà không cần tạo thủ công!

---

## 7. KHỞI CHẠY ỨNG DỤNG (DEVELOPMENT)

Để thuận tiện quan sát log của từng dịch vụ, bạn nên mở **3 tab Terminal riêng biệt** tại thư mục gốc:

### Terminal 1: Chạy Backend API (Port 4000)
```bash
npm run dev:backend
```
*Kết quả thành công sẽ hiển thị:*
```text
Database connected
[scheduler] Đã khởi động. Cron job đóng tin hết hạn chạy lúc 00:05 ICT mỗi ngày.
Backend listening on port 4000
```
API Health Check: `http://localhost:4000/health` (trả về `{"status":"ok","service":"backend"}`)

---

### Terminal 2: Chạy Frontend Người dùng (Port 3000)
```bash
npm run dev:frontend
```
*Truy cập:* `http://localhost:3000`  
Bao gồm: Trang chủ tìm kiếm việc làm, xem danh sách công ty, đăng ký/đăng nhập ứng viên và nhà tuyển dụng.

---

### Terminal 3: Chạy Frontend Admin (Port 3001)
```bash
npm run dev:frontend-admin
```
*Truy cập:* `http://localhost:3001`  
Bao gồm: Bảng điều khiển quản trị viên, duyệt công ty, duyệt tin tuyển dụng, phân quyền người dùng, xem log hệ thống.

---

## 8. TÀI KHOẢN MẪU & HƯỚNG DẪN KIỂM THỬ (TESTING)

### 8.1. Đăng nhập Trang Quản trị (Admin Portal)
Hệ thống migration đã tạo sẵn 2 tài khoản Admin kích hoạt sẵn:

* **URL Đăng nhập Admin**: `http://localhost:3001/auth/login`
* **Tài khoản 1**:
  * Email: `camly@jobplatform.app`
  * Mật khẩu: `Admin123`
* **Tài khoản 2**:
  * Email: `thuylinh@jobplatform.app`
  * Mật khẩu: `Admin123`

Sau khi đăng nhập thành công, bạn sẽ được chuyển đến Dashboard quản trị tại `http://localhost:3001/admin`.

---

### 8.2. Đăng ký & Trải nghiệm vai trò Ứng viên (Candidate)
1. Mở trình duyệt tới `http://localhost:3000/auth/register`.
2. Chọn vai trò **"Ứng viên tìm việc"**.
3. Điền Họ tên, Email (ví dụ: `ungvien@test.com`), Mật khẩu và bấm **Đăng ký**.
4. Trình duyệt chuyển sang trang nhập mã OTP (`/auth/verify-otp`).
5. **Xem mã OTP**: Nhìn vào **Terminal 1** (nơi đang chạy `npm run dev:backend`), bạn sẽ thấy dòng in ra:
   ```text
   [mailer:dev] to=ungvien@test.com subject="Mã xác thực đăng ký tài khoản JobPlatform"
   Mã xác thực của bạn là: 123456. Mã có hiệu lực trong 5 phút.
   ```
6. Nhập mã OTP gồm 6 chữ số này vào trang web để kích hoạt tài khoản.
7. Đăng nhập tại `http://localhost:3000/auth/login` và truy cập trang cá nhân:
   - Quản lý hồ sơ: `http://localhost:3000/candidate/profile`
   - Quản lý CV trực tuyến: `http://localhost:3000/candidate/resume`
   - Tìm kiếm và ứng tuyển việc làm: `http://localhost:3000/jobs`

---

### 8.3. Đăng ký & Trải nghiệm vai trò Nhà tuyển dụng (Recruiter)
1. Mở trình duyệt tới `http://localhost:3000/auth/register`.
2. Chọn vai trò **"Nhà tuyển dụng"**.
3. Điền thông tin và đăng ký -> Lấy mã OTP trong Terminal Backend tương tự như trên.
4. Đăng nhập vào hệ thống và truy cập giao diện Nhà tuyển dụng:
   - Tổng quan tuyển dụng: `http://localhost:3000/recruiter`
   - Cập nhật hồ sơ công ty: `http://localhost:3000/recruiter/company`
   - Đăng tin tuyển dụng mới: `http://localhost:3000/recruiter/jobs/new`
   - Quản lý danh sách ứng viên nộp CV: `http://localhost:3000/recruiter/candidates`

> [!TIP]
> **Quy trình duyệt tin tuyển dụng chuẩn:**
> Khi Nhà tuyển dụng đăng tin mới, tin có thể cần được **Admin duyệt** (tùy cấu hình trạng thái). Bạn có thể mở đồng thời tab Admin `http://localhost:3001/admin/jobs` để duyệt tin, sau đó tin sẽ xuất hiện ngay trên trang tìm kiếm công khai `http://localhost:3000/jobs` của Ứng viên.

---

## 9. CÁC LỆNH TIỆN ÍCH & SCRIPT KIỂM TRA HỆ THỐNG

### 9.1. Lệnh thao tác nhanh từ Root
| Lệnh | Ý nghĩa |
| :--- | :--- |
| `npm run dev:backend` | Chạy Backend chế độ dev (tsx watch) |
| `npm run dev:frontend` | Chạy Frontend chế độ dev |
| `npm run dev:frontend-admin` | Chạy Admin chế độ dev |
| `npm run build:backend` | Build TypeScript Backend sang thư mục `dist` |
| `npm run build:frontend` | Build Next.js Frontend |
| `npm run build:frontend-admin` | Build Next.js Frontend Admin |
| `npm run test:backend` | Chạy bộ kiểm thử tự động Vitest cho Backend |

### 9.2. Lệnh TypeORM Migration (Quản lý Database)
Chạy tại thư mục root (thông qua flag `-w backend`):
```bash
# Thực thi các migration chưa chạy
npm run migration:run -w backend

# Kiểm tra trạng thái các migration (đã chạy / chưa chạy)
npm run migration:show -w backend

# Rollback lại migration gần nhất
npm run migration:revert -w backend
```

### 9.3. Các Script kiểm tra kết nối dịch vụ
Backend tích hợp sẵn các script độc lập giúp bạn kiểm tra từng dịch vụ bên thứ ba:

* **Kiểm tra Redis**:
  ```bash
  npm run test:redis -w backend
  ```
* **Kiểm tra gửi Email**:
  ```bash
  npm run test:mail -w backend -- email_cua_ban@gmail.com
  ```
* **Kiểm tra kết nối Supabase Storage**:
  ```bash
  npm run test:storage -w backend
  ```

---

## 10. KHẮC PHỤC SỰ CỐ THƯỜNG GẶP (TROUBLESHOOTING)

### 1. Lỗi `Missing JWT_ACCESS_SECRET. Set it in apps/backend/.env`
* **Nguyên nhân**: File `apps/backend/.env` chưa cấu hình biến `JWT_ACCESS_SECRET` hoặc đang để trống.
* **Cách sửa**: Mở `apps/backend/.env`, thêm một chuỗi ngẫu nhiên dài từ 32 ký tự trở lên:
  ```env
  JWT_ACCESS_SECRET=c1f4e58b88d2279bc901a18276f7fbb6b4904b684cb32d201101e40ebad82e21
  ```
  Sau đó khởi động lại Backend.

---

### 2. Lỗi `connect ECONNREFUSED 127.0.0.1:5433`
* **Nguyên nhân**: Container PostgreSQL chưa được khởi động hoặc Docker Desktop đang tắt.
* **Cách sửa**:
  1. Bật Docker Desktop.
  2. Chạy lại lệnh: `docker compose up -d postgres`.
  3. Kiểm tra bằng `docker ps` xem container `job-platform-postgres` đã ở trạng thái `Up` chưa.

---

### 3. Lỗi `database "job_platform" does not exist`
* **Nguyên nhân**: PostgreSQL container khởi động nhưng chưa có database tên `job_platform`.
* **Cách sửa**: Tạo database bằng lệnh:
  ```bash
  docker exec -it job-platform-postgres psql -U postgres -c "CREATE DATABASE job_platform;"
  ```
  Sau đó chạy lại migration: `npm run migration:run -w backend`.

---

### 4. Đăng ký tài khoản nhưng không nhận được email chứa mã OTP
* **Nguyên nhân**: Bạn đang để `MAIL_PROVIDER=console` trong môi trường phát triển (dev).
* **Cách xem OTP**: Hệ thống **không gửi email thật ra ngoài** mà in trực tiếp mã OTP vào cửa sổ Terminal đang chạy `npm run dev:backend`. Hãy mở tab Terminal Backend và tìm dòng `[mailer:dev] to=... code=...`.

---

### 5. Lỗi `Failed to fetch Be Vietnam Pro from Google Fonts` khi build Next.js
* **Nguyên nhân**: Máy đang offline hoặc mạng bị chặn kết nối tới `fonts.googleapis.com` trong lúc Next.js tải font chữ.
* **Cách sửa**: Kết nối Internet ổn định trước khi chạy `npm run build:frontend` hoặc `npm run dev:frontend`.

---

### 6. Khi upload avatar, CV hoặc logo báo lỗi Supabase
* **Nguyên nhân**: Chưa điền `SUPABASE_URL` và `SUPABASE_SERVICE_ROLE_KEY` trong `apps/backend/.env`.
* **Cách sửa**: Đăng ký dự án miễn phí tại [Supabase](https://supabase.com), tạo 2 bucket trong Storage:
  - `job-platform-assets` (chọn Public).
  - `job-platform-private` (chọn Private).
  Sau đó copy `Project URL` và `service_role key` dán vào `apps/backend/.env`.

---

### 7. Cách dừng và dọn dẹp các container Docker
Khi không dùng nữa hoặc muốn tắt môi trường:
```bash
# Dừng container mà vẫn giữ nguyên dữ liệu đã tạo
docker compose stop

# Bật lại
docker compose start

# Hoặc tắt hoàn toàn container (dữ liệu vẫn được giữ trong Docker volume)
docker compose down
```

---

*Chúc bạn khởi chạy và phát triển dự án Job Platform thành công!*

