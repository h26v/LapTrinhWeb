# BT7 - RESTful API + AJAX

Ứng dụng quản lý `Product` và `Category` bằng Spring Boot 3, Spring Data JPA, H2 và jQuery AJAX. Giao diện quản trị được phục vụ tại `/`.

## Chạy ứng dụng

Yêu cầu Java 17 trở lên và Maven 3.9 trở lên:

```bash
mvn spring-boot:run
```

Mở [http://localhost:8080](http://localhost:8080).

Ứng dụng dùng H2 in-memory nên mỗi lần khởi động sẽ tạo dữ liệu mẫu. Console H2 nằm tại `/h2-console`, JDBC URL là `jdbc:h2:mem:bt7db`, user `sa`, password để trống.

## API

Các route số ít là route chính theo tài liệu YC4; route số nhiều cũng được hỗ trợ:

| Method | Endpoint | Mô tả |
| --- | --- | --- |
| GET | `/api/category?page=0&size=8&search=elec` | Danh sách Category có tìm kiếm và phân trang |
| GET | `/api/category/{id}` | Chi tiết Category |
| POST | `/api/category` | Thêm Category, JSON `{ "categoryName": "...", "icon": "..." }` |
| PUT | `/api/category/{id}` | Cập nhật Category |
| DELETE | `/api/category/{id}` | Xóa Category; từ chối nếu còn Product |
| GET | `/api/product?page=0&size=8&search=watch&categoryId=1` | Danh sách Product, tìm kiếm, lọc và phân trang |
| GET | `/api/product/{id}` | Chi tiết Product |
| POST | `/api/product` | Thêm Product |
| PUT | `/api/product/{id}` | Cập nhật Product |
| DELETE | `/api/product/{id}` | Xóa Product |

Mỗi response có dạng `{ success, message, data }`. Dữ liệu phân trang nằm trong `data.items`, kèm `page`, `size`, `totalItems`, `totalPages`.

Product nhận các trường `productName`, `images`, `unitPrice`, `discount`, `description`, `categoryId`, `quantity`, `status` (`1` đang bán, `0` tạm dừng). `images` và `icon` là URL hình ảnh tùy chọn, giúp API vẫn thuần JSON và dễ kiểm thử bằng Postman. Để bám sát mẫu YC4, `POST/PUT` cũng nhận `multipart/form-data`: Category dùng `categoryName` + file `icon`; Product dùng `productName`, file `imageFile`, `unitPrice`, `discount`, `description`, `categoryId`, `quantity`, `status`. File được lưu trong `uploads/` và truy cập qua `/uploads/...`.

## Cấu trúc chính

- `src/main/java/vn/iotstar/controller`: REST controllers và xử lý lỗi.
- `src/main/java/vn/iotstar/service`: nghiệp vụ CRUD, kiểm tra trùng tên và ràng buộc xóa Category.
- `src/main/java/vn/iotstar/repository`: truy vấn JPA có phân trang.
- `src/main/resources/static`: dashboard, modal CRUD, tìm kiếm debounce và phân trang AJAX.
