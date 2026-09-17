# learn_java

Quá trình tự học Java, gom về một repo để tiện theo dõi cả lộ trình: mỗi thư mục là một mảng kiến thức,
trong đó có đề bài, code thực hành và ghi chú.

```
learn_java/
├── java_core_practice/        # Java core: OOP, kế thừa, đa hình, quản lý danh sách
└── java_spring_framework/     # Spring Framework
    └── spring_data_JPA/       # Spring Boot + truy cập dữ liệu
```

## java_core_practice

Project Java thuần, chạy bằng VS Code. Source ở `src`, output biên dịch ở `bin` (đã gitignore).

### practice1 — Kế thừa và đa hình

Xây dựng lớp `Shape` với hai thuộc tính `width`, `height`; từ đó tạo `Rectangle` và `Circle`.
Mỗi lớp con tự tính và in ra thông tin của mình qua `printInfo()`.

→ [`src/practice1/`](java_core_practice/src/practice1)

### practice2 — Quản lý kho siêu thị

Quản lý ba nhóm hàng: thực phẩm, đồ gia dụng và đồ điện tử. Mỗi mặt hàng có mã, tên, số lượng tồn (>= 0)
và đơn giá; ngoài ra mỗi nhóm có thêm đặc thù riêng (hạn sử dụng và nhà cung cấp, thời hạn bảo hành và
công suất, nhà sản xuất và ngày nhập kho).

Ba yêu cầu: thiết kế hệ thống lớp và quan hệ kế thừa; viết phương thức đánh giá mức tiêu thụ của từng
nhóm hàng; xây lớp quản lý danh sách hàng hoá, thêm hàng và chặn trùng mã. Cần tính được tồn kho từng
nhóm và tiền VAT (10% cho đồ điện tử và gia dụng, 5% cho thực phẩm).

→ Đề bài đầy đủ: [`src/practice2/practice2.md`](java_core_practice/src/practice2/practice2.md)

### practice3 — Quản lý phương tiện giao thông

Ngành công an quản lý ô tô, xe máy và xe tải. Thông tin chung gồm biển số, hãng sản xuất, năm sản xuất,
màu xe và chủ xe; mỗi loại có thêm thuộc tính riêng (số chỗ ngồi và loại động cơ, dung tích xi-lanh,
trọng tải). Chủ xe quản lý theo số CMND, họ tên và email.

Chương trình cần: thêm phương tiện, tìm theo biển số, tìm xe theo CMND chủ xe, xoá toàn bộ xe của một
hãng, chỉ ra hãng có nhiều xe nhất, sắp xếp giảm dần theo số lượng và thống kê số xe từng loại.

→ Đề bài đầy đủ: [`src/practice3/practice3.md`](java_core_practice/src/practice3/practice3.md)

## java_spring_framework/spring_data_JPA

Project Spring Boot dùng để thực hành phần truy cập dữ liệu (Spring Boot 4.1.1, Java 21, MySQL).
Chi tiết cách chạy và ghi chú trong [README của module](java_spring_framework/spring_data_JPA/README.md).

## Quy ước

- Đề bài và ghi chú đặt ngay cạnh code (`practiceN.md`, hoặc `README.md` của module).
- Mỗi bài là một commit riêng để đọc được tiến trình qua lịch sử commit.
