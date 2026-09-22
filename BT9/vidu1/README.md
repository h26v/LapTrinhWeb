# Vi du 1 - Login voi Spring Security + MapStruct

Cho bang `User`, `Role`: viet chuc nang login, thong tin cua user hien thi o
`header.html`. Dung Spring Boot + Spring Security, MapStruct, Thymeleaf
(khong dung Layout Dialect).

## Chay ung dung

1. Copy `.env.example` thanh `.env` va dien thong tin SQL Server / SMTP that.
2. `mvn spring-boot:run`
3. Mo `http://localhost:8088`

## Tai khoan mau (tao san khi ung dung khoi dong lan dau)

| Email | Password | Vai tro |
|-------|----------|---------|
| admin@hcmute.edu.vn | 123456 | ADMIN |
| user01@gmail.com | 123456 | USER |

## Cac diem chinh

- `CustomUserDetailsService` load `User` kem `Role` qua `JOIN FETCH` roi build
  `UserDetails` chuan cua Spring Security (`roles(...)`), nen `header.html`
  chi hien thi duoc `sec:authentication="name"` (email).
- `UserMapper` (MapStruct) map `User` <-> `UserDTO`, dung cho cac man CRUD user
  sau nay.
- `SecurityConfig`: form login bang email/password, phan quyen `/users/**` va
  `/dashboard` chi danh cho `ROLE_ADMIN`.
