# BT3 - User Profile

Bài tập xây dựng chức năng **cập nhật profile User** bằng **JPA/Hibernate**, gồm họ tên, số điện thoại và ảnh đại diện upload bằng `multipart/form-data`. Giao diện được quản lý bởi **SiteMesh 3**.

## 1. Yêu cầu môi trường

- JDK 17
- Maven 3.6+
- MySQL 8
- Tomcat 9 / Servlet 4 (`javax.servlet`)

BT3 dùng Hibernate 6 (Jakarta Persistence) ở tầng JPA nhưng giữ Servlet/JSP `javax` để tương thích với cách chạy embedded Tomcat hiện tại của repository.

## 2. Cấu hình database

Mở `src/main/resources/META-INF/persistence.xml` nếu cần đổi user/password MySQL. Mặc định ứng dụng dùng database `bt3crud`, user `root`, password rỗng và `createDatabaseIfNotExist=true`.

Có thể chạy `database.sql` trước để tạo bảng `users` và tài khoản demo:

```text
username: trung
password: 123
```

## 3. Thư mục upload

Ảnh profile được lưu ngoài thư mục webapp tại:

```text
<user.home>/bt3_upload/profile
```

Chỉ nhận PNG, JPEG, GIF hoặc BMP hợp lệ (kiểm tra nội dung ảnh), kích thước tối đa 5 MB. Tên file thật do server sinh bằng UUID.

## 4. Chạy project

Từ thư mục `BT3`:

```bash
mvn clean package
mvn tomcat7:run
```

Mở <http://localhost:8080/BT3/>. Đăng nhập Session tại `/BT3/session/login`, sau đó cập nhật profile tại `/BT3/session/profile`.

## 5. Kiến trúc và SiteMesh

Luồng chính:

```text
ProfileSessionController
  -> IUserService / UserServiceImpl
  -> IUserDao / UserDao (JPA transaction)
  -> users.avatar, users.fullname, users.phone
```

`/WEB-INF/web.xml` đăng ký `ConfigurableSiteMeshFilter`; `/WEB-INF/sitemesh3.xml` áp dụng decorator chung cho các trang HTML và loại trừ endpoint `/image`. Decorator nằm ở `WEB-INF/decorators/default.jsp`.

Profile chỉ lấy ID từ User đang đăng nhập trong Session, không tin hidden field từ form. Sau khi commit, Session được thay bằng User mới nhất để không hiển thị dữ liệu cũ.
