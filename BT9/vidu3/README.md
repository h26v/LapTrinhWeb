# Vi du 3 - Shop: Register/OTP/Login/ForgotPassword + CRUD User/Product + Cloudinary

Cho cac bang `Users`, `Roles` (user, admin), `OtpToken`, `Products` (quan he
1 user - n product). Upload anh len Cloudinary. Xay dung: Register + xac nhan
OTP qua mail, Login luu session, Forgot password gui OTP qua mail, CRUD User,
CRUD Product, tim kiem + phan trang cho User va Product, dem so User, dem so
Product cua tung User.

## Kien truc

```
Thymeleaf (views)
  -> AuthController / UserController / ProductController
    -> AuthService / UserService / ProductService
      -> OtpService (Generate & Verify) / EmailService (Gmail SMTP)
      -> CloudinaryService (upload/delete anh)
      -> Mapper (MapStruct: DTO <-> Entity)
        -> Repository (Spring Data JPA)
          -> Hibernate -> SQL Server
```

## Chuan bi

1. Tao database rong tren SQL Server (vi du `webst3`).
2. Copy `.env.example` -> `.env`, dien:
   - `DB_URL` / `DB_USERNAME` / `DB_PASSWORD`
   - `MAIL_USERNAME` / `MAIL_PASSWORD` (App Password cua Gmail)
   - `CLOUDINARY_CLOUD_NAME` / `CLOUDINARY_API_KEY` / `CLOUDINARY_API_SECRET`
3. `mvn spring-boot:run` — `DataInitializer` se tu tao 2 role (`ROLE_USER`,
   `ROLE_ADMIN`) va 1 tai khoan admin mau khi khoi dong lan dau (xem
   `seed-roles.sql` neu muon them role bang tay).

## Tai khoan mau

| Username | Password | Vai tro |
|----------|----------|---------|
| admin | (bien `ADMIN_PASSWORD`, mac dinh `123456`) | ROLE_ADMIN |
| user01 | 123456 | ROLE_USER |

## Luong chuc nang chinh

- **Register**: `POST /register` -> tao `User` (`enabled=false`) -> gui OTP
  qua email (`OtpServiceImpl`) -> `GET/POST /verify-otp` kich hoat tai khoan.
- **Login**: form login Spring Security (`CustomUserDetailsService` +
  `CustomUserDetails`), luu session, gioi han 1 session/tai khoan.
- **Forgot password**: `POST /forgot-password` gui OTP -> `POST /reset-password`
  xac thuc OTP roi doi mat khau (BCrypt).
- **CRUD User** (`/users/**`, chi ROLE_ADMIN): tao/sua/xoa, tim kiem theo
  username/email/ho ten, phan trang, hien thi so luong Product cua moi user.
- **CRUD Product** (`/products/**`, moi user da dang nhap): tao/sua/xoa,
  upload anh len Cloudinary (`CloudinaryServiceImpl`), tim kiem + phan trang.

## Ghi chu bao mat

- File `.env` **khong** duoc commit len Git (da khai bao trong `.gitignore`
  o thu muc goc repo) vi chua mat khau DB/SMTP/Cloudinary that.
- Mat khau luon duoc ma hoa bang `BCryptPasswordEncoder` truoc khi luu DB.
- OTP duoc bam (`passwordEncoder.encode`) truoc khi luu, khong luu OTP dang
  plain text; gioi han so lan thu va thoi gian het han (5 phut).
