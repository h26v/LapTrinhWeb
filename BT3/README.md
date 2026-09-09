# BT3 - User Profile, Auth OTP và Products

BT3 xây dựng bằng **Servlet MVC + JSP/SiteMesh + JPA/Hibernate**. Project hiện có:

- Đăng nhập bằng Session.
- Đăng ký tài khoản và kích hoạt bằng OTP gửi qua email.
- Quên mật khẩu và đặt lại mật khẩu bằng OTP gửi qua email.
- Cập nhật profile User và upload ảnh đại diện bằng `multipart/form-data`.
- CRUD Category.
- CRUD Product với ảnh upload Multipart và quan hệ **Category 1 - n Product**.
- Trang chủ hiển thị 10 sản phẩm mới nhất.
- Trang `/product` phân trang 6 sản phẩm/trang và trang chi tiết sản phẩm.

## 1. Yêu cầu môi trường

- JDK 17+
- Maven 3.6+
- MySQL 8
- Tomcat 9 / Servlet 4 (`javax.servlet`)

BT3 dùng Hibernate 6 (Jakarta Persistence) ở tầng JPA nhưng giữ Servlet/JSP `javax` để tương thích với Tomcat 9.

## 2. Cấu hình database

Mở `src/main/resources/META-INF/persistence.xml` nếu cần đổi user/password MySQL. Mặc định ứng dụng dùng database `bt3crud`, user `root`, password rỗng và `createDatabaseIfNotExist=true`.

Có thể chạy `database.sql` trước để tạo schema demo và tài khoản admin:

```text
username: trung
password: 123
```

Mật khẩu seed đang là plain text để dễ demo; sau lần đăng nhập thành công đầu tiên, ứng dụng tự đổi sang dạng hash.

## 3. Cấu hình SMTP gửi OTP

Ứng dụng không hardcode mật khẩu email. Cấu hình SMTP bằng environment variables hoặc Java system properties:

```text
BT3_SMTP_HOST=smtp.gmail.com
BT3_SMTP_PORT=587
BT3_SMTP_USERNAME=your-email@gmail.com
BT3_SMTP_PASSWORD=your-app-password
BT3_SMTP_FROM=your-email@gmail.com
BT3_SMTP_STARTTLS=true
```

Hoặc dùng system properties tương ứng:

```text
-Dbt3.smtp.host=smtp.gmail.com
-Dbt3.smtp.port=587
-Dbt3.smtp.username=your-email@gmail.com
-Dbt3.smtp.password=your-app-password
-Dbt3.smtp.from=your-email@gmail.com
-Dbt3.smtp.starttls=true
```

Với Gmail, thường cần bật 2FA và tạo App Password.

## 4. Thư mục upload

Ảnh upload được lưu ngoài thư mục webapp tại:

```text
<user.home>/bt3_upload
```

Các thư mục con chính:

- `profile/`: ảnh đại diện.
- `category/`: ảnh danh mục.
- `product/`: ảnh sản phẩm.

Ảnh product/profile chỉ nhận PNG, JPEG, GIF hoặc BMP hợp lệ, kiểm tra bằng nội dung ảnh, tối đa 5 MB. Ảnh được phục vụ qua endpoint `/image?fname=...`.

## 5. Chạy project

Từ thư mục `BT3`:

```bash
mvn clean package
mvn tomcat7:run
```

Mở <http://localhost:8080/BT3/>.

Các URL chính:

- `/BT3/home`: trang chủ, 10 sản phẩm mới nhất.
- `/BT3/product`: danh sách sản phẩm, 6 sản phẩm/trang.
- `/BT3/session/login`: đăng nhập.
- `/BT3/session/register`: đăng ký và kích hoạt OTP.
- `/BT3/session/forgot-password`: quên mật khẩu bằng OTP.
- `/BT3/session/profile`: cập nhật profile.
- `/BT3/admin/categories`: CRUD Category.
- `/BT3/admin/products`: CRUD Product.

## 6. Kiến trúc

Luồng auth chính:

```text
RegisterSessionController / ForgotPasswordController / LoginSessionController
  -> IUserService / UserServiceImpl
  -> IUserDao / UserDao
  -> users + OTP hash + SMTP email
```

Luồng product:

```text
ProductAdminController / ProductWebController / HomeController
  -> IProductService / ProductServiceImpl
  -> IProductDao / ProductDao
  -> products many-to-one categories
```

`/WEB-INF/web.xml` đăng ký `ConfigurableSiteMeshFilter`; `/WEB-INF/sitemesh3.xml` áp dụng decorator chung cho các trang HTML và loại trừ endpoint `/image`. Decorator nằm ở `WEB-INF/decorators/default.jsp`.
