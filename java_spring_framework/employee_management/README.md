# employee_management

Mini project xuyên suốt phần Spring Boot: **Employee Management System**. Project được dựng dần qua 10 module,
mỗi module thêm một mảng kiến thức vào cùng một codebase — từ REST API cơ bản tới JPA, validation, Thymeleaf,
logging/profile, actuator/cache/scheduler, Spring Security + JWT, và cuối cùng là báo cáo thống kê.

**Stack:** Spring Boot 4.1.1 · Java 21 · Spring Data JPA · Spring Security (JWT) · Thymeleaf · MySQL · Caffeine cache · Maven wrapper.

## Lộ trình 10 module

| Module | Nội dung                                                    | Lab                                                              | Trạng thái |
| ------ | ----------------------------------------------------------- | ---------------------------------------------------------------- | ---------- |
| 1      | Spring Boot, starter, auto-config, Initializr               | Tạo project, API `/api/hello`                                    | ✅         |
| 2      | Bean, IoC, `@Component`/`@Service`, DI                      | `UtilityService` (format tên, sinh mã nhân viên)                 | ✅         |
| 3      | REST cơ bản, path variable, request param, `ResponseEntity` | API danh sách + thêm nhân viên                                   | ✅         |
| 4      | Cấu hình DB, Entity, `JpaRepository`, CRUD                  | Bảng `employee` + `department`, tìm theo tên/phòng ban           | ✅         |
| 5      | Bean Validation, `@Valid`, `@RestControllerAdvice`          | Validate email/name, 404 khi không thấy nhân viên                | ✅         |
| 6      | Thymeleaf, Controller + Model + View, form binding          | Trang `/employees/list`, `/employees/add`                        | ✅         |
| 7      | Logging SLF4J, profile dev/prod                             | Log thao tác ghi, tách cấu hình DB theo profile                  | ✅         |
| 8      | Actuator, `@Scheduled`, `@Cacheable`                        | Cache tổng số nhân viên, task log mỗi 30 giây                    | ✅         |
| 9      | Spring Security, User + Role, JWT                           | Đăng ký/đăng nhập, USER chỉ xem — ADMIN được CRUD                | ✅         |
| 10     | Thống kê bằng `@Query`, REST + Thymeleaf                    | Nhân viên theo phòng ban, tổng số, trang `/employees/statistics` | ✅         |

## Chạy dự án

Cần sẵn: JDK 21, MySQL đang chạy, một database rỗng.

```bash
cp .env.example .env     # rồi điền thông tin thật
./mvnw spring-boot:run   # chạy app ở cổng 8080
./mvnw test              # chạy toàn bộ test
```

Biến môi trường trong `.env` (được nạp qua `spring.config.import` trong `application.properties`):

| Biến                          | Bắt buộc | Ghi chú                                                           |
| ----------------------------- | -------- | ----------------------------------------------------------------- |
| `DB_HOST` / `DB_PORT`         | ✔        | MySQL, ví dụ `localhost` / `3306`                                 |
| `DB_NAME`                     | ✔        | Tên database                                                      |
| `DB_USERNAME` / `DB_PASSWORD` | ✔        | Tài khoản MySQL                                                   |
| `JWT_SECRET`                  | ✔        | Chuỗi ngẫu nhiên ≥ 32 byte (HS256), thiếu thì app không khởi động |
| `JWT_EXPIRATION_MS`           |          | Hạn token, mặc định `3600000` (1 giờ)                             |
| `SPRING_PROFILES_ACTIVE`      |          | Mặc định `dev`                                                    |

Profile `dev` dùng `ddl-auto=update` (Hibernate tự tạo bảng) và chạy `data.sql` để seed sẵn tài khoản admin.
Profile `prod` dùng `ddl-auto=validate` — schema phải tồn tại trước, và **không** seed dữ liệu.

## Xác thực & phân quyền

Cơ chế: **JWT stateless**. Không có session, mỗi request tự chứng minh danh tính bằng header
`Authorization: Bearer <token>`.

```
POST /api/auth/login  ──► AuthenticationManager ──► CustomUserDetailsService ──► bảng users
                                                          │
                                                          ▼  (đúng mật khẩu)
                                                    JwtUtil.generateToken()  ──►  token

GET /api/employees   ──► JwtAuthenticationFilter (verify chữ ký, đọc username + role từ token)
                     ──► SecurityContext  ──►  @PreAuthorize kiểm tra role
```

- **Đăng ký** (`/api/auth/register`) luôn tạo role `USER`. Không có API nào tạo `ADMIN`.
- **Tài khoản ADMIN** được seed sẵn trong `data.sql` (profile dev): `admin` / `admin123`.
- Role được lưu trong token dưới claim `role`, và được map thành authority `ROLE_ADMIN` / `ROLE_USER`.
- Phân quyền đặt ở tầng method bằng `@PreAuthorize` (không khai theo URL trong `SecurityConfig`).

## API

Base URL `http://localhost:8080`. Tất cả request/response đều là `application/json`, trừ vài endpoint trả
số hoặc chuỗi thuần (ghi rõ ở từng mục).

### Quy ước chung

Mọi endpoint ngoài `/api/auth/**` đều cần header:

```
Authorization: Bearer <token lấy từ /api/auth/login>
```

Lỗi đi qua `GlobalExceptionHandler` có chung khuôn `ErrorResponse`. Trường `errors` chỉ khác `null` khi lỗi
validation:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    { "field": "email", "message": "Email must be a valid email address" }
  ]
}
```

| Mã  | Khi nào                                    | Body                                                                    |
| --- | ------------------------------------------ | ----------------------------------------------------------------------- |
| 400 | Body sai validation, hoặc id phòng ban sai | `ErrorResponse`                                                         |
| 401 | Sai username/password khi login            | `ErrorResponse` — `"Invalid username or password"`                      |
| 403 | **Thiếu token / token sai, hết hạn**       | **rỗng** (bị chặn ở tầng filter)                                        |
| 403 | Đã đăng nhập nhưng sai role                | `ErrorResponse` — `"You do not have permission to perform this action"` |
| 404 | Không tìm thấy bản ghi                     | `ErrorResponse`, **trừ** `GET /{id}` (body rỗng)                        |

Hai loại 403 khác nhau ở chỗ bị chặn: thiếu token thì dừng ngay ở `SecurityFilterChain` (chưa vào
`DispatcherServlet` nên không có body), còn sai role thì `@PreAuthorize` chặn ở tầng method nên
`GlobalExceptionHandler` kịp gắn body.

---

### `POST /api/auth/register` — đăng ký

Công khai. Luôn tạo role `USER`, không thể tự đăng ký làm `ADMIN`.

**Request**

```json
{ "username": "minh", "password": "secret123" }
```

| Field      | Kiểu   | Ràng buộc                         |
| ---------- | ------ | --------------------------------- |
| `username` | string | bắt buộc, không rỗng, không trùng |
| `password` | string | bắt buộc, tối thiểu 6 ký tự       |

**Response `200`**

```json
{ "id": 2, "username": "minh", "role": "USER" }
```

**Lỗi** — `400` nếu validation sai, hoặc username đã tồn tại (`"Username already exists: minh"`).

---

### `POST /api/auth/login` — đăng nhập, lấy JWT

Công khai.

**Request**

```json
{ "username": "admin", "password": "admin123" }
```

**Response `200`**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTc1..."
}
```

Token ký HS256, `sub` = username, claim `role` = `ADMIN`/`USER`, hạn mặc định 1 giờ (`JWT_EXPIRATION_MS`).

**Lỗi** — `401` khi sai username **hoặc** sai password (thông báo giống nhau, cố ý không phân biệt để tránh
dò username tồn tại).

```bash
TOKEN=$(curl -s -X POST localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | jq -r .token)

curl localhost:8080/api/employees -H "Authorization: Bearer $TOKEN"
```

---

### `GET /api/employees` — danh sách nhân viên

Quyền: `USER`, `ADMIN`.

| Query param | Bắt buộc | Ý nghĩa                                                 |
| ----------- | -------- | ------------------------------------------------------- |
| `name`      | không    | lọc theo tên, khớp một phần, không phân biệt hoa thường |

**Response `200`** — trả entity trực tiếp, `department` lồng bên trong. Chưa có phân trang.

```json
[
  {
    "id": 1,
    "name": "NGUYEN VAN A",
    "email": "a@example.com",
    "department": { "id": 1, "name": "IT" }
  }
]
```

---

### `GET /api/employees/{id}` — chi tiết nhân viên

Quyền: `USER`, `ADMIN`.

**Response `200`** — một object `Employee` như trên.
**`404`** — **body rỗng** (controller trả `ResponseEntity.notFound()`, không đi qua `GlobalExceptionHandler`).

---

### `GET /api/employees/count` — tổng số nhân viên

Quyền: `USER`, `ADMIN`. Kết quả được cache 1 phút (Caffeine), tự xoá cache khi thêm/xoá nhân viên.

**Response `200`** — số nguyên thuần, không bọc object:

```
12
```

---

### `POST /api/employees` — thêm nhân viên

Quyền: **`ADMIN`**.

**Request**

```json
{ "name": "Nguyen Van A", "email": "a@example.com", "departmentId": 1 }
```

| Field          | Kiểu   | Ràng buộc                       |
| -------------- | ------ | ------------------------------- |
| `name`         | string | bắt buộc, không rỗng            |
| `email`        | string | bắt buộc, đúng định dạng email  |
| `departmentId` | number | bắt buộc, phải tồn tại trong DB |

**Response `200`** (không phải `201`) — nhân viên vừa tạo. Lưu ý `name` được chuẩn hoá **viết hoa toàn bộ**
(`UtilityService.formatName`):

```json
{
  "id": 5,
  "name": "NGUYEN VAN A",
  "email": "a@example.com",
  "department": { "id": 1, "name": "IT" }
}
```

**Lỗi** — `400` validation; `400` `"Department not found with id: 99"`; `403` nếu role là `USER`.

---

### `PUT /api/employees/{id}` — sửa nhân viên

Quyền: **`ADMIN`**. Body giống `POST` (ghi đè toàn bộ 3 trường).

**Response `200`** — nhân viên sau khi sửa.
**Lỗi** — `404` `"Employee not found with id: 99"` (có body); `400` nếu `departmentId` không tồn tại.

---

### `DELETE /api/employees/{id}` — xoá nhân viên

Quyền: **`ADMIN`**.

**Response `204`** — không có body.
**Lỗi** — `404` `"Employee not found with id: 99"` (có body); `403` nếu role là `USER`.

---

### `GET /api/employees/statistics/by-department` — thống kê theo phòng ban

Quyền: `USER`, `ADMIN`. Dùng `@Query` gom nhóm trực tiếp ở DB.

**Response `200`**

```json
[
  { "departmentName": "IT", "employeeCount": 3 },
  { "departmentName": "Sales", "employeeCount": 1 }
]
```

Vì câu query gom nhóm từ bảng `employee`, **phòng ban chưa có nhân viên nào sẽ không xuất hiện** trong kết quả.

---

### `GET /api/employees/statistics/total` — tổng số nhân viên

Quyền: `USER`, `ADMIN`. Dùng lại đúng giá trị đã cache của `/api/employees/count`.

**Response `200`** — số nguyên thuần: `12`

---

### `/api/departments` — phòng ban

Phân quyền giống `/api/employees`: **đọc** thì `USER` và `ADMIN` đều được, **ghi** thì chỉ `ADMIN`.

| Method | URL                     | Quyền   | Request               | Response                             |
| ------ | ----------------------- | ------- | --------------------- | ------------------------------------ |
| GET    | `/api/departments`      | USER, ADMIN | —                 | `200` `[{ "id": 1, "name": "IT" }]`  |
| GET    | `/api/departments/{id}` | USER, ADMIN | —                 | `200` object, hoặc `404` body rỗng   |
| POST   | `/api/departments`      | **ADMIN**   | `{ "name": "Sales" }` | `200` `{ "id": 3, "name": "Sales" }` |
| PUT    | `/api/departments/{id}` | **ADMIN**   | `{ "name": "Sales" }` | `200` object đã sửa                  |
| DELETE | `/api/departments/{id}` | **ADMIN**   | —                 | `204`                                |

`USER` gọi `POST`/`PUT`/`DELETE` nhận `403` kèm `ErrorResponse`.
`PUT`/`DELETE` với id không tồn tại trả `400` `"Department not found with id: 99"` (không phải 404).
Body của `POST`/`PUT` **vẫn chưa được validate** (thiếu `@Valid`), nên `name` rỗng sẽ đi thẳng xuống DB và vi phạm
ràng buộc `NOT NULL` thay vì được chặn ở tầng API.

---

### Endpoint khác

| Method | URL                                     | Quyền                 | Response                      |
| ------ | --------------------------------------- | --------------------- | ----------------------------- |
| GET    | `/api/hello`                            | mọi user đã đăng nhập | text thuần: `Hello world !`   |
| GET    | `/actuator/health`, `/actuator/metrics` | mọi user đã đăng nhập | JSON của Spring Boot Actuator |

## Trang web (Thymeleaf)

Các trang render phía server, gọi thẳng service (không qua REST API) nên **không cần token** — vì trình duyệt
không tự gắn header `Authorization` khi mở URL trực tiếp.

| URL                     | Nội dung                                    |
| ----------------------- | ------------------------------------------- |
| `/employees/list`       | Danh sách + tìm kiếm theo tên               |
| `/employees/add`        | Form thêm nhân viên (có validate + báo lỗi) |
| `/employees/statistics` | Tổng số nhân viên + số lượng theo phòng ban |

## Cấu trúc

```
com/employee_management/employee_management/
├── Entity/          Employee, Department, User, Role
├── config/          AppConfig (PasswordEncoder, AuthenticationManager), SecurityConfig,
│                    CacheConfig (Caffeine), SchedulingConfig
├── controller/      REST: Auth, Employee, Department, Statistics, Hello
│                    View: EmployeeViewController (Thymeleaf)
├── dto/             Request/Response records + DepartmentStatsResponse (interface projection)
├── exception/       GlobalExceptionHandler, ErrorResponse, EmployeeNotFoundException
├── repository/      Employee, Department, User (Spring Data JPA)
├── scheduler/       SystemStatusScheduler (@Scheduled 30s)
├── security/        JwtUtil, JwtAuthenticationFilter, CustomUserDetailsService
└── service/         Employee, Department, Auth, Statistics, Utility
```

## Test

```bash
./mvnw test                          # toàn bộ
./mvnw test -Dtest=EmployeeServiceTest
```

Hai nhóm test:

- **Unit test thuần (Mockito)** — `service/` và `security/`: business logic, sinh & verify JWT, map
  `User` → `UserDetails`, filter đọc token.
- **Slice test (`@WebMvcTest` + MockMvc)** — `controller/`: mã HTTP, JSON trả về, và đặc biệt là
  **rule phân quyền** (USER gọi API ghi phải nhận 403, ADMIN thì được).

Lưu ý khi viết thêm test controller: filter chain đang `STATELESS`, nên `@WithMockUser` **không có tác dụng**
(context bị `NullSecurityContextRepository` ghi đè). Dùng request post-processor thay thế. Hai danh tính dùng
chung nằm ở `support/TestUsers` — đừng khai lại trong từng file test, để khi cách gán quyền đổi thì chỉ sửa một chỗ:

```java
import static com.employee_management.employee_management.support.TestUsers.AS_ADMIN;

mockMvc.perform(get("/api/employees").with(AS_ADMIN))
```
