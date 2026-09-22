# Vi du 2 - Custom Login (username hoac email) + Thymeleaf Layout Dialect

Cho bang `User`, `Role`: viet chuc nang custom login co the dang nhap bang
username hoac email deu duoc; thong tin `fullName` va `images` cua user hien
thi o `header.html`. Dung Spring Boot + Spring Security + MapStruct +
Thymeleaf Layout Dialect (`layout:decorate`, `layout:fragment`).

## Chay ung dung

1. Copy `.env.example` thanh `.env`, dien thong tin SQL Server that.
2. `mvn spring-boot:run`
3. Mo `http://localhost:8089`

## Tai khoan mau

| Username | Password | Vai tro |
|----------|----------|---------|
| user01 | 123456 | ROLE_USER |
| admin | 123456 | ROLE_ADMIN |

## Cac diem chinh

- `UserRepository.findByUsernameOrEmail(login, login)`: cho phep dang nhap
  bang ca username lan email chi voi 1 truong input.
- `CustomUserDetails` bo sung `fullName`, `images`, `email`, `role` (khac voi
  `org.springframework.security.core.userdetails.User` mac dinh chi co
  username/password/authorities) de `header.html` co the doc truc tiep qua
  `#authentication.principal.*`.
- `layouts/layout.html` dung Thymeleaf Layout Dialect: cac trang con
  (`home.html`) chi can `layout:decorate="~{layouts/layout}"` va khai bao noi
  dung trong `layout:fragment="content"`.
