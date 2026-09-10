# BT5 - CRUD admin Category & User bằng Spring Boot 4 + JSP/JSTL

BT5 chuyển các bài tập trước (vốn dùng Servlet/JSP thuần) sang **Spring Boot 4** và hoàn thiện
chức năng CRUD trong role admin cho hai bảng **Category** và **User**, kèm **tìm kiếm**.

## 1. Nội dung chính

- **CRUD Category**: thêm, sửa, xóa, danh sách, upload ảnh, tìm theo tên.
- **CRUD User**: thêm, sửa, xóa, danh sách, upload ảnh đại diện, đặt lại mật khẩu,
  gán vai trò (admin/người dùng), khóa/mở tài khoản.
- **Tìm kiếm**:
  - Category: theo `categoryname`, không phân biệt hoa thường, có phân trang.
  - User: theo `username`, `fullname`, `email` hoặc `phone`, có phân trang.
- **Phân quyền admin**: chỉ tài khoản `roleid = 1` mới vào được `/admin/**`.
- Kèm theo: đăng nhập/đăng xuất bằng session, CRUD Product (port từ BT4), trang chủ và `/product`.

## 2. Công nghệ

| Thành phần | Phiên bản |
|---|---|
| Spring Boot | 4.1.1 |
| Java | 17+ |
| View | JSP + JSTL 3.0 (`jakarta.tags.*`) |
| Persistence | Spring Data JPA + Hibernate 7 |
| Database | MySQL 8 |
| Đóng gói | `war` (JSP không chạy được trong jar) |

Package giữ nguyên `vn.iotstar` như các bài trước.

## 3. Yêu cầu môi trường

- JDK 17 trở lên
- Maven 3.6.3+
- MySQL 8

> **Ghi chú kiểm thử:** project đã được `mvn clean package` thành công với Spring Boot 4.1.1
> (JDK 20) và chạy thử end-to-end trên H2 (thêm dependency H2 chỉ trong môi trường test,
> không có trong `pom.xml` của bài nộp): đăng nhập, phân quyền admin, CRUD + tìm kiếm cho cả
> Category và User, upload ảnh, cùng các chốt chặn bảo mật đều hoạt động.
> Việc chạy thật với MySQL 8 vẫn cần được thực hiện trên máy có MySQL.

## 4. Cấu hình database

Mặc định dùng database `bt5crud`, user `root`, mật khẩu rỗng. Chỉnh trong:

```text
src/main/resources/application.properties
```

Có thể chạy `database.sql` trước để có sẵn schema và dữ liệu demo:

```text
username: trung   password: 123   (admin)
username: an      password: 123   (người dùng thường)
```

Mật khẩu seed để plain text cho dễ demo; sau lần đăng nhập thành công đầu tiên,
ứng dụng tự động đổi sang BCrypt hash.

## 5. Chạy project

Từ thư mục `BT5`:

```bash
mvn clean package
mvn spring-boot:run
```

Mở: <http://localhost:8080/BT5/>

Hoặc deploy file `target/BT5.war` lên Tomcat 11 bên ngoài
(`spring-boot-starter-tomcat` để `provided` nên war không kèm Tomcat).

### Các URL chính

| URL | Mô tả |
|---|---|
| `/BT5/home` | Trang chủ, 9 sản phẩm mới nhất |
| `/BT5/product` | Danh sách sản phẩm, 6 sản phẩm/trang |
| `/BT5/product/detail?id=...` | Chi tiết sản phẩm |
| `/BT5/login` | Đăng nhập |
| `/BT5/logout` | Đăng xuất |
| `/BT5/admin/categories` | CRUD Category + tìm kiếm |
| `/BT5/admin/users` | CRUD User + tìm kiếm |
| `/BT5/admin/products` | CRUD Product + tìm kiếm |
| `/BT5/image?fname=...` | Phục vụ ảnh upload |

## 6. Cấu trúc code

```text
vn.iotstar
├── BT5Application.java          # @SpringBootApplication + SpringBootServletInitializer
├── config/
│   ├── AdminInterceptor.java    # chặn /admin/** nếu không phải admin
│   ├── WebMvcConfig.java        # đăng ký interceptor
│   ├── WebConfig.java           # multipart, locale, static resource
│   └── SessionKeys.java         # tên thuộc tính session
├── entity/                      # Category, User, Product, Video
├── repository/                  # Spring Data JPA repositories
├── service/ (+ impl/)           # nghiệp vụ + validation
├── controller/
│   ├── AuthController.java      # login/logout/access-denied
│   ├── DownloadImageController.java
│   ├── admin/                   # CategoryAdminController, UserAdminController, ProductAdminController
│   └── web/                     # HomeController, ProductWebController
└── util/                        # ValidationUtil, PasswordUtil, ImageUploadUtil
```

View JSP nằm trong `src/main/webapp/WEB-INF/views/`:

- `layout/` – header, footer, messages, pagination, admin-tabs dùng chung (`<jsp:include>`).
- `admin/` – các trang CRUD.
- `auth/` – login, access-denied.
- `web/` – trang chủ, danh sách và chi tiết sản phẩm.

## 7. Điểm kỹ thuật đáng chú ý

- **JSTL 3.0 dùng URI mới** `jakarta.tags.core` / `jakarta.tags.fmt` / `jakarta.tags.functions`,
  không còn `http://java.sun.com/jsp/jstl/core` như BT1–BT4.
- **Layout bằng `<jsp:include>`** thay cho SiteMesh của BT4 để giảm phụ thuộc.
- **Không merge entity từ form**: service nạp entity đang được quản lý rồi mới cập nhật từng trường,
  nên mật khẩu/ngày tạo không bị ghi đè bằng giá trị rỗng.
- **Chống mất quyền admin**: không cho xóa tài khoản admin cuối cùng.
- **Chặn xóa danh mục còn sản phẩm** để tránh lỗi khóa ngoại.
- **Upload an toàn** (giữ từ BT4): đổi tên file bằng UUID, kiểm tra nội dung thật bằng `ImageIO`,
  giới hạn 5 MB, chặn path traversal khi đọc/xóa ảnh.
- **Chống session fixation**: hủy session cũ và tạo session mới sau khi đăng nhập.
- **Naming strategy**: Spring Boot mặc định đổi tên cột camelCase sang snake_case
  (`categoryId` → `category_id`), làm lệch so với schema BT3/BT4 và `database.sql`.
  BT5 đặt `PhysicalNamingStrategyStandardImpl` để tên cột đúng y nguyên như trong `@Column`.
- **Giới hạn multipart** được nâng lên 5 MB cho khớp với thông báo trên form
  (mặc định Spring Boot chỉ 1 MB), kèm `GlobalExceptionHandler` hiển thị thông báo thân thiện
  thay vì trang lỗi 413.

## 8. Kiểm tra nhanh

1. Chạy app, mở `/BT5/home`.
2. Truy cập `/BT5/admin/categories` khi chưa đăng nhập → bị chuyển về `/BT5/login`.
3. Đăng nhập `trung / 123` → vào được trang admin.
4. Category: thêm có ảnh, sửa, tìm theo tên, xóa.
5. User: thêm tài khoản mới, sửa (đổi vai trò/khóa), tìm theo username/họ tên/email/điện thoại.
6. Thử xóa chính tài khoản admin duy nhất → phải bị chặn kèm thông báo.
7. Tạo một tài khoản `roleid = 2`, đăng nhập tài khoản đó → `/admin/**` phải bị từ chối.
8. Truy cập `/BT5/image?fname=../../application.properties` → phải bị chặn.
