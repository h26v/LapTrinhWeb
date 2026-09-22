# Web09 - Spring Security (Login, Custom Login, Shop Auth + CRUD)

Bài tập buổi Lập trình Web 09: 3 ví dụ về Spring Security 6/7 + Spring Boot +
MapStruct + Thymeleaf, theo hướng dẫn trên UTEXLMS.

| # | Thư mục | Nội dung |
|---|---------|----------|
| 1 | [`vidu1`](vidu1) | Login với Spring Security + MapStruct, hiển thị thông tin user ở `header.html` (không dùng Layout Dialect) |
| 2 | [`vidu2`](vidu2) | Custom Login: đăng nhập bằng username hoặc email, hiển thị fullname/avatar ở header (dùng Thymeleaf Layout Dialect) |
| 3 | [`vidu3`](vidu3) | Ứng dụng Shop đầy đủ: Register + xác nhận OTP qua email, Login lưu session, Forgot password (OTP), CRUD User, CRUD Product (upload ảnh Cloudinary), tìm kiếm + phân trang |

## Công nghệ dùng chung

- Spring Boot, Spring Security, Spring Data JPA
- MapStruct (map DTO <-> Entity)
- Thymeleaf (+ thymeleaf-extras-springsecurity6, layout-dialect ở ví dụ 2 và 3)
- SQL Server / H2, Lombok
- Maven

## Chạy từng ví dụ

Mỗi thư mục con là một project Maven độc lập:

```bash
cd vidu1   # hoặc vidu2, vidu3
mvn spring-boot:run
```

Cấu hình kết nối database/mail/cloudinary qua file `.env` ở thư mục gốc của
từng project (xem `.env.example` trong `vidu3`), theo hướng dẫn trong file PDF
gốc của môn học.
