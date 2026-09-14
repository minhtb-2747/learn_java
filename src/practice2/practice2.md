# Practice 2 — Supermarket Inventory Management

## Đề bài (English)

Write a program to manage the supermarket's inventory including **food**, **crockery** and **electronics**.

Each type of goods has:

- Product code
- Name
- Inventory quantity (>= 0)
- Unit price

**Food products** additionally need:

- Date of manufacture
- Expiration date (must be after or equal to the date of manufacture)
- Supplier

**Electrical goods** additionally need:

- Warranty period in months (>= 0)
- Capacity in KW (>= 0)

**Crockery** additionally needs:

- Manufacturer's information
- Date of arrival

In addition, the manager needs to know the inventory quantity of the 3 types of goods, and the VAT
amount for each type of goods:

| Type        | VAT |
| ----------- | --- |
| Electronics | 10% |
| Crockery    | 10% |
| Food        | 5%  |

### Requirement 1

Based on the above information, determine:

- Possible classes (abstract and concrete)
- Attributes and methods of each class
- Relationship design (inheritance and polymorphism if applicable)

### Requirement 2

Create a method to measure consumption:

- **Electronic goods**: if the inventory quantity < 3 → considered to be sold
- **Food goods**: if still in stock and expired → rated as hard to sell
- **Crockery**: if inventory quantity > 50 and storage time > 10 days → evaluated as slow sale
- The remaining cases are not evaluated

### Requirement 3

- Initialize DSHH management class (use an array to store the list)
- Write a method to add goods to the list
  - Added successfully if there is no duplicate product code
  - Allow users to choose the type of goods to add

## Đề bài (Tiếng Việt)

Viết một chương trình quản lý hàng tồn kho của siêu thị, bao gồm 3 loại hàng hóa: **thực phẩm**,
**đồ sành sứ** và **đồ điện tử**.

Mỗi loại hàng hóa có các thông tin:

- Mã hàng
- Tên hàng
- Số lượng tồn kho (>= 0)
- Đơn giá

Đối với **thực phẩm**, cần quản lý thêm:

- Ngày sản xuất
- Hạn sử dụng (hạn sử dụng phải sau hoặc bằng ngày sản xuất)
- Nhà cung cấp

Đối với **đồ điện tử**, cần quản lý thêm:

- Thời gian bảo hành bao nhiêu tháng (>= 0)
- Công suất bao nhiêu KW (>= 0)

Đối với **đồ sành sứ**, cần quản lý thêm:

- Thông tin nhà sản xuất
- Ngày nhập kho

Ngoài ra, người quản lý cần biết:

- Số lượng hàng tồn kho của 3 loại hàng hóa
- Số tiền VAT của từng loại hàng hóa

| Loại hàng  | VAT |
| ---------- | --- |
| Đồ điện tử | 10% |
| Đồ sành sứ | 10% |
| Thực phẩm  | 5%  |

### Requirement 1

Dựa trên các thông tin trên, xác định:

- Các class có thể có (bao gồm abstract class và concrete class)
- Attributes và methods của từng class
- Thiết kế mối quan hệ giữa các class (inheritance và polymorphism nếu có thể áp dụng)

### Requirement 2

Tạo một method để đánh giá tình trạng tiêu thụ hàng hóa:

- **Đồ điện tử**: nếu số lượng tồn kho < 3 → được đánh giá là đã bán / bán chạy
- **Thực phẩm**: nếu vẫn còn hàng trong kho và đã hết hạn → được đánh giá là khó bán
- **Đồ sành sứ**: nếu số lượng tồn kho > 50 và thời gian lưu kho > 10 ngày → được đánh giá là bán chậm
- Các trường hợp còn lại → không đánh giá

### Requirement 3

- Khởi tạo class quản lý DSHH (Danh sách hàng hóa) — sử dụng array để lưu danh sách hàng hóa
- Viết method để thêm hàng hóa vào danh sách
  - Thêm thành công nếu không bị trùng mã hàng
  - Cho phép người dùng lựa chọn loại hàng hóa muốn thêm
