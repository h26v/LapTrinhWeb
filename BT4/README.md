# BT4 - SiteMesh Decorator 3, Bootstrap, Validation và Profile

BT4 kế thừa bài tập 03 và bổ sung đúng trọng tâm bài tập 04:

- Cấu hình **SiteMesh Decorator 3** với một template **Bootstrap 5** dùng chung.
- Tích hợp template vào toàn bộ giao diện của bài tập 03.
- Validation server-side cho các chức năng có form.
- Cập nhật profile User gồm `fullname`, `phone`, `images` bằng **JPA** và upload file **Multipart**.
- Giữ lại các chức năng BT3: đăng nhập Session, đăng ký/kích hoạt OTP email, quên mật khẩu OTP email, CRUD Category, CRUD Product, trang chủ và trang `/product`.

## 1. Yêu cầu môi trường

- JDK 17+
- Maven 3.6+
- MySQL 8
- Tomcat 9 / Servlet 4 (`javax.servlet`)

BT4 dùng Hibernate 6 ở tầng JPA (`jakarta.persistence`) nhưng giữ Servlet/JSP `javax.servlet` để tương thích với Tomcat 9.

## 2. Cấu hình database

Mặc định ứng dụng dùng database `bt4crud`, user `root`, password rỗng. Có thể chỉnh trong:

```text
src/main/resources/META-INF/persistence.xml
```

Có thể chạy `database.sql` trước để tạo schema demo và tài khoản admin:

```text
username: trung
password: 123
```

Mật khẩu seed để plain text cho dễ demo; sau lần đăng nhập thành công đầu tiên, ứng dụng tự đổi sang BCrypt hash.

## 3. SiteMesh Decorator 3 + Bootstrap

Các file cấu hình chính:

```text
src/main/webapp/WEB-INF/web.xml
src/main/webapp/WEB-INF/sitemesh3.xml
src/main/webapp/WEB-INF/decorators/default.jsp
```

`web.xml` đăng ký `org.sitemesh.config.ConfigurableSiteMeshFilter`. `sitemesh3.xml` áp dụng decorator chung cho HTML pages và exclude các endpoint/static path như `/image`, `/assets`, `/css`, `/js`.

`default.jsp` là template duy nhất, chứa:

- Bootstrap 5.3 CDN.
- Navbar chung.
- Main container.
- Footer.
- Các style nhỏ dùng chung cho product card/avatar.

Các JSP trong `views/` chỉ là fragment nội dung trang; SiteMesh quản lý layout tổng thể.

## 4. Validation form

BT4 bổ sung `ValidationUtil` tại:

```text
src/main/java/vn/iotstar/util/ValidationUtil.java
```

Các nhóm form đã có validation server-side:

- Login Session/Cookie: username/password bắt buộc, username tối đa 100 ký tự.
- Register: username, email, fullname, phone, password, confirm password.
- Verify OTP và reset OTP: OTP phải đúng 6 chữ số.
- Forgot password: email bắt buộc và đúng định dạng.
- Profile: fullname bắt buộc 1–255 ký tự, phone đúng định dạng nếu nhập, image hợp lệ.
- Category: tên danh mục bắt buộc 1–255 ký tự, status hợp lệ, id hợp lệ, keyword search tối đa 100 ký tự.
- Product: tên sản phẩm bắt buộc, giá hợp lệ `DECIMAL(12,2)`, category tồn tại, mô tả tối đa 1000 ký tự, status/id hợp lệ.

Validation phía HTML vẫn được giữ để hỗ trợ trải nghiệm người dùng, nhưng server-side validation là lớp kiểm tra chính.

## 5. Profile User bằng JPA + Multipart

URL chính:

```text
/session/profile
/session/profile/update
```

Profile cho phép cập nhật:

- `fullname`
- `phone`
- `images` file input, lưu vào field `avatar` của bảng `users`

Luồng code:

```text
ProfileSessionController
  -> IUserService / UserServiceImpl
  -> IUserDao / UserDao
  -> users.fullname, users.phone, users.avatar
```

Controller dùng `@MultipartConfig` và `ImageUploadUtil.saveImage(...)`. DAO chỉ update các field profile được phép, không merge toàn bộ User từ form nên không làm thay đổi username/email/password/role/active.

## 6. Upload file Multipart

Ảnh upload được lưu ngoài thư mục webapp tại:

```text
<user.home>/bt4_upload
```

Các thư mục con:

- `profile/`: ảnh đại diện.
- `category/`: ảnh danh mục.
- `product/`: ảnh sản phẩm.

Upload image dùng chung `ImageUploadUtil`:

- Không dùng filename client gửi lên.
- Đổi tên file bằng UUID.
- Kiểm tra nội dung ảnh bằng `ImageIO`.
- Chỉ nhận PNG, JPEG, GIF hoặc BMP.
- Giới hạn 5 MB.
- Chặn path traversal khi xóa/serve file.

Ảnh được phục vụ qua endpoint:

```text
/image?fname=profile/...
/image?fname=category/...
/image?fname=product/...
```

## 7. Cấu hình SMTP gửi OTP

Ứng dụng không hardcode mật khẩu email. Cấu hình SMTP bằng environment variables:

```text
BT4_SMTP_HOST=smtp.gmail.com
BT4_SMTP_PORT=587
BT4_SMTP_USERNAME=your-email@gmail.com
BT4_SMTP_PASSWORD=your-app-password
BT4_SMTP_FROM=your-email@gmail.com
BT4_SMTP_STARTTLS=true
```

Hoặc dùng Java system properties:

```text
-Dbt4.smtp.host=smtp.gmail.com
-Dbt4.smtp.port=587
-Dbt4.smtp.username=your-email@gmail.com
-Dbt4.smtp.password=your-app-password
-Dbt4.smtp.from=your-email@gmail.com
-Dbt4.smtp.starttls=true
```

Với Gmail, thường cần bật 2FA và tạo App Password.

## 8. Chạy project

Từ thư mục `BT4`:

```bash
mvn clean package
mvn tomcat7:run
```

Mở:

```text
http://localhost:8080/BT4/
```

Các URL chính:

- `/BT4/home`: trang chủ, 10 sản phẩm mới nhất.
- `/BT4/product`: danh sách sản phẩm, 6 sản phẩm/trang.
- `/BT4/session/login`: đăng nhập.
- `/BT4/session/register`: đăng ký và kích hoạt OTP.
- `/BT4/session/forgot-password`: quên mật khẩu bằng OTP.
- `/BT4/session/profile`: cập nhật profile.
- `/BT4/admin/categories`: CRUD Category.
- `/BT4/admin/products`: CRUD Product.

## 9. Kiểm tra nhanh

Sau khi chạy app, nên test:

1. Login bằng `trung / 123`.
2. Vào `/BT4/session/profile`, update fullname/phone/avatar.
3. Thử fullname rỗng, phone sai, file không phải ảnh để kiểm tra validation.
4. CRUD Category với ảnh upload.
5. CRUD Product với category, giá, ảnh upload.
6. Trang `/BT4/home`, `/BT4/product`, `/BT4/product/detail?id=...`.
7. Truy cập `/BT4/image?fname=../../secret.txt` phải bị chặn.
