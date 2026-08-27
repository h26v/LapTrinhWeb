# BT1 - Lập Trình Web

Bài tập: **Login với Cookie**, **Login với Session**, **CRUD Category** (Servlet + JDBC + JSP, mô hình 3 tầng MVC).

## 1. Yêu cầu môi trường
- JDK 17
- Maven 3.6+
- MySQL 8
- (Không cần cài Tomcat riêng — dùng plugin `tomcat7:run`)

## 2. Cài Maven (Windows)
1. Tải Maven binary zip: https://maven.apache.org/download.cgi
2. Giải nén ra `C:\maven`
3. Thêm `C:\maven\bin` vào biến môi trường `Path`
4. Kiểm tra: mở terminal mới gõ `mvn -version`

## 3. Tạo database
Chạy script (đổi user/pass MySQL nếu cần):
```
mysql -u root -p < database.sql
```
Script tạo DB `servletcrudmvc`, bảng `category`, `users` và tài khoản demo **trung / 123**.

## 4. Cấu hình kết nối
Mở `src/main/java/vn/iotstar/connection/DBConnection.java`, chỉnh `userID` và `password` cho khớp MySQL của bạn.

Thư mục upload icon mặc định: `<thư mục home>/bt1_upload` (đổi trong `Constant.java` nếu muốn).

## 5. Chạy project
```
mvn clean package
mvn tomcat7:run
```
Mở trình duyệt: http://localhost:8080/BT1/

## 6. Các đường dẫn
| Chức năng | URL |
|---|---|
| Trang chủ | `/BT1/` |
| Login Cookie | `/BT1/cookie/login` |
| Login Session | `/BT1/session/login` |
| CRUD Category | `/BT1/admin/category/list` |

## 7. Cấu trúc (3 tầng)
```
model → dao (interface) → dao.impl → service (interface) → service.impl → controller → views (jsp)
```
- **Login Cookie**: LoginCookieController → lưu username vào Cookie 30 phút → HelloCookieController đọc cookie → LogoutCookieController xóa cookie (setMaxAge(0)).
- **Login Session**: LoginSessionController → lưu User vào HttpSession → ProfileSessionController kiểm tra session → LogoutSessionController gọi `invalidate()`.
- **CRUD Category**: List/Add/Edit/Delete + upload icon qua commons-fileupload, hiển thị ảnh qua `/image`.

## 8. Chạy bằng IDE (tùy chọn)
Nếu muốn deploy trên Tomcat 9 trong Eclipse/IntelliJ: Import Maven project → Run on Server. Lưu ý dự án dùng `javax.servlet` (Tomcat 9), **không dùng Tomcat 10** (đã đổi sang `jakarta`).
