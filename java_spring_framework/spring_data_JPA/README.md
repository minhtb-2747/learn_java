# spring_data_JPA

Project thực hành Spring Boot, tập trung vào tầng truy cập dữ liệu.

## Thông tin

- Spring Boot 4.1.1, Java 21, build bằng Maven wrapper (`./mvnw`)
- Dependency: `spring-boot-starter-webmvc`, `spring-boot-starter-jdbc`,
  `spring-boot-starter-data-jpa`, `mysql-connector-j`
- Entry point: `src/main/java/com/example/java_spring_demo/JavaSpringDemoApplication.java`

## Chạy

Cần MySQL đang chạy và một file `.env` ở thư mục gốc project:

```properties
DB_NAME=java_spring_demo
DB_USERNAME=root
DB_PASSWORD=your_password
# DB_HOST, DB_PORT optional, mặc định localhost:3306
```

Bảng không cần tạo tay: `spring.jpa.hibernate.ddl-auto=update` để Hibernate tự sinh từ entity.

```bash
./mvnw spring-boot:run     # chạy app
./mvnw test                # chạy test
```

## Cấu trúc

```
com/example/java_spring_demo/
├── JavaSpringDemoApplication.java   ← @SpringBootApplication
├── entity/       ← @Entity, map sang bảng DB
├── repository/   ← interface extends JpaRepository, truy vấn DB
├── service/      ← @Service, nghiệp vụ + transaction
└── controller/   ← @RestController, nhận HTTP request
```

Luồng một request: `Controller → Service → Repository → DB`.

## API

Base URL `http://localhost:8080`. Request có body cần header `Content-Type: application/json`.

| Method | URL                                                | Việc                                |
| ------ | -------------------------------------------------- | ----------------------------------- |
| GET    | `/api/vehicles`                                    | danh sách xe                        |
| GET    | `/api/vehicles/{id}`                               | 1 xe theo id                        |
| GET    | `/api/vehicles/search?vehicleNumber=51A01`         | tìm theo biển số                    |
| POST   | `/api/vehicles`                                    | tạo xe, trả 201 + header `Location` |
| GET    | `/api/vehicle-owners`                              | danh sách chủ xe                    |
| GET    | `/api/vehicle-owners/{id}`                         | 1 chủ xe theo id                    |
| GET    | `/api/vehicle-owners/search?idNumber=001203045678` | tìm theo số CCCD                    |
| POST   | `/api/vehicle-owners`                              | tạo chủ xe                          |

## Ghi chú học tập
