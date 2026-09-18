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

### Entity

- `@Entity` — đánh dấu class là entity được JPA quản lý
- `@Id` — khóa chính
- `@GeneratedValue` — tự động sinh ID

Entity thường map với một table trong DB.

```java
@Entity
public class Vehicle {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}
```

### Repository

Repository chịu trách nhiệm truy cập DB, thường `extends JpaRepository<Entity, ID>`.

```java
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
```

Chỉ khai báo vậy là có sẵn các method CRUD, không phải viết thân hàm:

| Method | Việc |
| --- | --- |
| `save()` | thêm mới hoặc cập nhật |
| `findById()` | lấy 1 bản ghi theo id |
| `findAll()` | lấy tất cả |
| `deleteById()` | xoá theo id |
| `count()` | đếm số bản ghi |
| `existsById()` | kiểm tra tồn tại |

### Derived query

Spring Data JPA tự tạo query dựa trên tên method:

```java
findByVehicleNumber(String vehicleNumber);
findByColor(String color);
findByYearOfManufactureGreaterThan(int year);
```

Quy tắc đặt tên: `find` / `exists` / `count` / `delete` + `By` + **tên field trong entity**
(viết hoa chữ đầu), nối nhau bằng `And` / `Or`.

→ Không cần tự viết SQL cho các query đơn giản.

### Entity ↔ Table

```text
Vehicle.java        →  vehicle table

id                  →  id
vehicleNumber       →  vehicle_number
manufacturer        →  manufacturer
```

JPA/Hibernate thực hiện mapping giữa object và table.

### Transaction

`@Transactional` dùng để đảm bảo một nhóm thao tác DB chạy trong cùng transaction.
Thường đặt ở Service:

```java
@Transactional
public void updateVehicle(...) {
  ...
}
```

### Dependency Injection

Repository được Spring quản lý và inject vào Service:

```java
@Service
public class VehicleService {

  private final VehicleRepository vehicleRepository;

  public VehicleService(VehicleRepository vehicleRepository) {
    this.vehicleRepository = vehicleRepository;
  }
}
```

Ưu tiên constructor injection.
