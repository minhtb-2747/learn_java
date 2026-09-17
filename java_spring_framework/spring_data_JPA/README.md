# spring_data_JPA

Project thực hành Spring Boot, tập trung vào tầng truy cập dữ liệu.

## Thông tin

- Spring Boot 4.1.1, Java 21, build bằng Maven wrapper (`./mvnw`)
- Dependency hiện có: `spring-boot-starter-webmvc`, `spring-boot-starter-jdbc`, `mysql-connector-j`
- Entry point: `src/main/java/com/example/java_spring_demo/JavaSpringDemoApplication.java`

> Lưu ý: hiện mới có `spring-boot-starter-jdbc`. Khi bắt đầu học JPA thì cần thêm
> `spring-boot-starter-data-jpa` vào `pom.xml`, và cấu hình datasource trong `application.properties`.

## Chạy

```bash
./mvnw spring-boot:run     # chạy app
./mvnw test                # chạy test
```

## Ghi chú học tập

<!-- Mỗi chủ đề học được thì ghi lại ở đây: khái niệm, annotation đã dùng, lỗi gặp phải và cách xử lý -->
