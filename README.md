# Job Platform

## 1. Giới thiệu

Job Platform là hệ thống nền tảng tuyển dụng việc làm, được xây dựng theo mô hình monorepo gồm:

* **Frontend**: Next.js
* **Frontend Admin**: Next.js
* **Backend**: Express.js + TypeORM
* **Database**: PostgreSQL
* **Cache / Rate Limit**: Redis *(tùy chọn nếu cấu hình)*
* **Package Manager**: npm Workspaces

### Cấu trúc project

```text
job-platform/
├── apps/
│   ├── frontend/          # Website dành cho người dùng
│   ├── frontend-admin/    # Trang quản trị
│   └── backend/           # API Server
│
├── docker-compose.yml     # PostgreSQL / Redis
├── package.json
└── README.md
```

---

# 2. Yêu cầu môi trường

Cần cài đặt:

* Node.js
* npm
* Docker Desktop
* Git

Kiểm tra phiên bản:

```bash
node -v
npm -v
docker -v
docker compose version
```

Khuyến nghị sử dụng Node.js phiên bản LTS.

---

# 3. Clone project

```bash
git clone https://github.com/tuanbao205/job_platform.git
cd job-platform
```

Nếu repository được clone với tên thư mục khác, di chuyển vào đúng thư mục project trước khi chạy các lệnh tiếp theo.

---

# 4. Cài đặt dependencies

Tại thư mục gốc của project:

```bash
npm install
```

Do project sử dụng npm Workspaces nên chỉ cần chạy `npm install` một lần tại thư mục root.

---

# 5. Cấu hình Environment

## 5.1. Backend

Copy file environment mẫu:

```bash
cp apps/backend/.env.example apps/backend/.env
```

Mở file:

```bash
nano apps/backend/.env
```

Hoặc có thể mở bằng VS Code:

```bash
code apps/backend/.env
```

Cấu hình tối thiểu cho PostgreSQL:

```env
PORT=4000

DB_HOST=127.0.0.1
DB_PORT=5433
DB_USER=postgres
DB_PASSWORD=postgres
DB_NAME=job_platform
DB_SSL=false

SUPABASE_URL=
SUPABASE_SERVICE_ROLE_KEY=
SUPABASE_STORAGE_BUCKET=job-platform-assets
SUPABASE_STORAGE_PRIVATE_BUCKET=job-platform-private

UPSTASH_REDIS_REST_URL=
UPSTASH_REDIS_REST_TOKEN=

JWT_ACCESS_SECRET=

COOKIE_SECURE=false

GOOGLE_CLIENT_ID=

MAIL_PROVIDER=console

CORS_ORIGINS=http://localhost:3000,http://localhost:3001

TRUST_PROXY=false
RATE_LIMIT_FACTOR=100
```

> **Lưu ý:** Không commit file `.env` lên GitHub. Chỉ commit các file `.env.example`.

---

## 5.2. Frontend

Copy environment:

```bash
cp apps/frontend/.env.example apps/frontend/.env
```

Kiểm tra và điền các biến môi trường cần thiết theo file:

```text
apps/frontend/.env.example
```

Frontend chạy mặc định tại:

```text
http://localhost:3000
```

---

## 5.3. Frontend Admin

Copy environment:

```bash
cp apps/frontend-admin/.env.example apps/frontend-admin/.env
```

Admin chạy mặc định tại:

```text
http://localhost:3001
```

---

# 6. Khởi động PostgreSQL bằng Docker

Project sử dụng PostgreSQL chạy trong Docker.

Kiểm tra Docker Desktop đã được mở, sau đó chạy:

```bash
docker compose up -d postgres
```

Kiểm tra container:

```bash
docker ps
```

Container PostgreSQL phải ở trạng thái:

```text
Up
```

Kiểm tra PostgreSQL:

```bash
docker exec -it job-platform-postgres psql -U postgres -c "\l"
```

Nếu database `job_platform` chưa tồn tại, tạo database:

```bash
docker exec -it job-platform-postgres psql -U postgres -c "CREATE DATABASE job_platform;"
```

> Nếu database đã tồn tại thì không cần tạo lại.

---

# 7. Chạy Migration

Sau khi PostgreSQL đã chạy, thực hiện migration:

```bash
npm run migration:run -w backend
```

Nếu thành công, database sẽ được tạo các bảng cần thiết cho hệ thống.

Có thể kiểm tra database:

```bash
docker exec -it job-platform-postgres psql -U postgres -d job_platform -c "\dt"
```

---

# 8. Chạy Backend

Mở terminal tại thư mục root:

```bash
npm run dev:backend
```

Nếu chạy thành công sẽ hiển thị:

```text
Database connected
Backend listening on port 4000
```

Backend API:

```text
http://localhost:4000
```

---

# 9. Chạy Frontend

Mở **terminal mới**:

```bash
npm run dev:frontend
```

Frontend:

```text
http://localhost:3000
```

---

# 10. Chạy Frontend Admin

Mở **terminal mới**:

```bash
npm run dev:frontend-admin
```

Admin:

```text
http://localhost:3001
```

---

# 11. Chạy toàn bộ hệ thống

Cần mở các terminal riêng:

### Terminal 1 – Docker

```bash
docker compose up -d postgres
```

### Terminal 2 – Backend

```bash
npm run dev:backend
```

### Terminal 3 – Frontend

```bash
npm run dev:frontend
```

### Terminal 4 – Admin

```bash
npm run dev:frontend-admin
```

Sau khi tất cả chạy thành công:

| Thành phần  | URL                   |
| ----------- | --------------------- |
| Frontend    | http://localhost:3000 |
| Admin       | http://localhost:3001 |
| Backend API | http://localhost:4000 |
| PostgreSQL  | localhost:5433        |

---

# 12. Kiểm tra trạng thái Docker

Xem các container đang chạy:

```bash
docker ps
```

Dừng các service:

```bash
docker compose stop
```

Khởi động lại:

```bash
docker compose start
```

Hoặc:

```bash
docker compose up -d
```

---

# 13. Một số lệnh thường dùng

### Cài dependencies

```bash
npm install
```

### Chạy Backend

```bash
npm run dev:backend
```

### Chạy Frontend

```bash
npm run dev:frontend
```

### Chạy Admin

```bash
npm run dev:frontend-admin
```

### Chạy migration

```bash
npm run migration:run -w backend
```

### Kiểm tra Git

```bash
git status
```

### Build project

```bash
npm run build
```

---

# 14. Troubleshooting

## 14.1. Port PostgreSQL 5432 bị sử dụng

Nếu máy đã có PostgreSQL chạy trên port `5432`, Docker PostgreSQL của project sử dụng:

```text
5433:5432
```

Do đó `.env` backend phải sử dụng:

```env
DB_HOST=127.0.0.1
DB_PORT=5433
```

Không cần tắt PostgreSQL đang chạy trên máy.

---

## 14.2. Database không tồn tại

Nếu xuất hiện lỗi:

```text
database "job_platform" does not exist
```

Tạo database:

```bash
docker exec -it job-platform-postgres psql -U postgres -c "CREATE DATABASE job_platform;"
```

Sau đó chạy lại:

```bash
npm run migration:run -w backend
```

---

## 14.3. Backend không kết nối được database

Kiểm tra PostgreSQL:

```bash
docker ps
```

Kiểm tra `.env`:

```env
DB_HOST=127.0.0.1
DB_PORT=5433
DB_USER=postgres
DB_PASSWORD=postgres
DB_NAME=job_platform
DB_SSL=false
```

Sau đó restart backend:

```bash
npm run dev:backend
```

---

## 14.4. Migration báo lỗi

Kiểm tra trạng thái migration:

```bash
npm run migration:show -w backend
```

Không nên sử dụng:

```bash
docker compose down -v
```

nếu database Docker đang chứa các database hoặc dữ liệu khác cần giữ lại.

---

## 14.5. Redis

Một số chức năng của backend có thể sử dụng Redis cho:

* Rate limiting
* OTP
* Cache

Nếu project yêu cầu Redis, có thể khởi động:

```bash
docker compose up -d redis
```

Kiểm tra:

```bash
docker ps
```

Nếu backend được cấu hình để không sử dụng Redis thì không cần khởi động service này.

---

# 15. Quy trình chạy project từ đầu

Sau khi clone project trên một máy mới:

```bash
git clone https://github.com/tuanbao205/job_platform.git
cd job-platform

npm install

cp apps/backend/.env.example apps/backend/.env
cp apps/frontend/.env.example apps/frontend/.env
cp apps/frontend-admin/.env.example apps/frontend-admin/.env

docker compose up -d postgres

npm run migration:run -w backend
```

Sau đó mở 3 terminal:

**Terminal 1:**

```bash
npm run dev:backend
```

**Terminal 2:**

```bash
npm run dev:frontend
```

**Terminal 3:**

```bash
npm run dev:frontend-admin
```

Truy cập:

```text
Frontend:
http://localhost:3000

Admin:
http://localhost:3001

Backend:
http://localhost:4000
```

---

# 16. Lưu ý khi commit lên GitHub

Không commit các file chứa thông tin bí mật:

```text
.env
.env.local
.env.production
```

Kiểm tra trước khi commit:

```bash
git status
```

Nếu `.env` đang xuất hiện trong danh sách file chuẩn bị commit, hãy kiểm tra `.gitignore` trước khi thực hiện:

```bash
git add .
```

Các file mẫu nên được commit:

```text
.env.example
```

Không đưa password database, JWT secret, API key hoặc service-role key thật lên GitHub.
