# employee_management

Mini project xuyên suốt phần Spring Boot: **Employee Management System**. Đề bài dựng dần qua 10 module,
mỗi module thêm một mảng kiến thức vào cùng một project.

Công nghệ dự kiến: Spring Boot + Spring Data JPA + REST API + Thymeleaf + MySQL.
Chức năng chính: quản lý nhân viên (CRUD, tìm kiếm), đăng ký/đăng nhập, phân quyền Admin–User, thống kê.

## Lộ trình 10 module

| Module | Nội dung                                                  | Lab                                                                  | Trạng thái |
| ------ | --------------------------------------------------------- | -------------------------------------------------------------------- | ---------- |
| 1      | Spring Boot, starter, auto-config, Initializr             | Tạo project `employee-management`, API `/hello`                      | ✅ xong     |
| 2      | Bean, IoC, `@Component`/`@Service`/`@Repository`, `@Bean`, DI | `UtilityService` (sinh mã nhân viên), một `@Bean` tự khai            |            |
| 3      | REST cơ bản, path variable, request param, `ResponseEntity` | API danh sách + thêm nhân viên (dữ liệu in-memory)                   |            |
| 4      | Cấu hình DB, Entity, `JpaRepository`, CRUD                | Bảng `employee` + `department`, quan hệ, tìm theo tên/phòng ban       |            |
| 5      | Bean Validation, `@Valid` + `BindingResult`, `@ControllerAdvice` | Validate email/name, trả 404 khi không thấy nhân viên           |            |
| 6      | Thymeleaf, Controller + Model + View, form binding        | Trang `/employees/list`, `/employees/add`, trang tìm kiếm            |            |
| 7      | Logging SLF4J + Logback, profile dev/prod                 | Log khi thêm/sửa/xoá, tách cấu hình DB theo profile                   |            |
| 8      | Actuator, `@Scheduled`, `@Cacheable`                      | API tổng số nhân viên cache 1 phút, task log mỗi 30 giây             |            |
| 9      | Spring Security, Basic Auth, User + Role, JWT cơ bản      | Entity `User`, đăng ký/đăng nhập, USER chỉ xem — ADMIN được CRUD      |            |
| 10     | Thống kê bằng `@Query`, hiển thị bằng REST + Thymeleaf    | Số nhân viên theo phòng ban, tổng số, trang `/employees/statistics`  |            |

## Thông tin

- Spring Boot 4.1.1, Java 21, build bằng Maven wrapper (`./mvnw`)
- Dependency hiện tại: `spring-boot-starter-webmvc`, `spring-boot-starter-webmvc-test` (scope `test`)
- Entry point: `src/main/java/com/employee_management/employee_management/EmployeeManagementApplication.java`

Chưa cần database ở module 1 — project chỉ có web layer, chạy trên Tomcat nhúng.

## Chạy

```bash
./mvnw spring-boot:run     # chạy app, mặc định cổng 8080
./mvnw test                # chạy test
```

## API

Base URL `http://localhost:8080`.

| Method | URL                 | Việc                            |
| ------ | ------------------- | ------------------------------- |
| GET    | `/hello`            | trả `Hello, world!`             |
| GET    | `/hello?name=Minh`  | trả `Hello, Minh!`              |

```bash
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=Minh"
```

## Cấu trúc

```
com/employee_management/employee_management/
├── EmployeeManagementApplication.java   ← @SpringBootApplication, đặt ở package GỐC
└── controller/                          ← @RestController, nhận HTTP request
    └── HelloController.java
```

Các package `service/`, `repository/`, `entity/`, `dto/`, `exception/` sẽ thêm dần từ module 2 trở đi.

## Ghi chú học tập — Module 1

### Starter

Starter là một dependency kéo theo cả bộ thư viện đã khớp phiên bản, tên theo khuôn `spring-boot-starter-*`.
Trong `pom.xml` khai starter mà **không ghi version**: `spring-boot-starter-parent` giữ một BOM
(bill of materials) khoá sẵn phiên bản cho mọi thư viện con, nên không bao giờ lệch version.

`spring-boot-starter-webmvc` (từ Spring Boot 4 tách ra từ `spring-boot-starter-web`) kéo theo Spring MVC +
Tomcat nhúng + Jackson — đủ để viết REST API.

### @SpringBootApplication

Gộp ba annotation:

- `@SpringBootConfiguration` — bản thân class này là một `@Configuration`
- `@EnableAutoConfiguration` — bật auto-config theo classpath
- `@ComponentScan` — quét bean từ package hiện tại **trở xuống**

Vì `@ComponentScan` quét từ package của class này trở xuống nên class `Application` **phải nằm ở package
gốc**. Đặt sai chỗ thì controller/service ở package khác sẽ không được quét thành bean.

### Auto-configuration chạy thế nào

`main()` chạy → quét classpath và dependency trong `pom.xml` → nạp danh sách auto-configuration từ
`spring-boot-autoconfigure.jar` (`WebMvcAutoConfiguration`, `DataSourceAutoConfiguration`…) → với mỗi lớp
kiểm tra điều kiện `@ConditionalOn...`, đủ thì gọi các method `@Bean`, không đủ thì bỏ qua →
`ApplicationContext` được lấp đầy → ứng dụng sẵn sàng.

Các điều kiện hay gặp: `@ConditionalOnClass` (có class này trên classpath không), `@ConditionalOnMissingBean`
(mình đã tự khai bean này chưa), `@ConditionalOnProperty`. Đây là lý do chỉ thêm dependency là có cấu hình,
và cũng là lý do tự khai một bean thì bản auto-config **tự nhường** — không cần tắt gì cả.

### Bốn thứ nên làm đúng từ lab đầu tiên

1. **DTO tách khỏi Entity** — đừng trả thẳng entity ra API (lộ field không cần như mật khẩu, dễ dính
   `LazyInitializationException` lúc serialize).
2. **Constructor injection** với field `final`, không dùng `@Autowired` trên field.
3. **Transaction ở tầng Service**, không phải ở Controller hay Repository.
4. **GlobalExceptionHandler** từ module 5 — làm sớm thì chín module sau đều sạch.
