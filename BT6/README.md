# BT6 - CRUD + tìm kiếm phân trang Category bằng Spring Boot 4 + Thymeleaf

BT6 làm chức năng **CRUD** và **tìm kiếm có phân trang** cho bảng **Category**, view dùng
**Thymeleaf**, bố cục trang dùng **Thymeleaf Layout Dialect** (`layout:decorate` / `layout:fragment`).
Bố cục gồm **header** (ảnh sinh viên), **content** và **footer** (thông tin sinh viên thực hiện).

Làm theo phần "Hướng dẫn bài tập: Spring Boot với Thymeleaf, MySQL" trong slide `docs/11_Thymeleaf.pdf`,
tái sử dụng các phần giống nhau từ BT5 (Spring Boot + JSP).

## 1. Chức năng

| Chức năng | URL | Ghi chú |
|---|---|---|
| Danh sách + tìm kiếm + phân trang | `GET /admin/categories/searchpaginated?name=&page=&size=` | tìm theo tên, không phân biệt hoa thường; chọn 3/5/10/15/20 dòng/trang |
| Thêm | `GET /admin/categories/add` → `POST /admin/categories/saveOrUpdate` | upload ảnh, chọn trạng thái |
| Sửa | `GET /admin/categories/edit/{id}` → `POST /admin/categories/saveOrUpdate` | thay ảnh thì ảnh cũ bị xóa |
| Xem chi tiết | `GET /admin/categories/view/{id}` | |
| Xóa | `POST /admin/categories/delete/{id}` | hộp thoại xác nhận, quay lại đúng trang/từ khóa đang xem |
| Ảnh upload | `GET /image?fname=category/xxx.png` | |

`/BT6/` và `/BT6/admin/categories` tự chuyển tới trang danh sách.

## 2. Công nghệ

| Thành phần | Phiên bản |
|---|---|
| Spring Boot | 4.1.1 (giống BT5) |
| Java | 17+ |
| View | Thymeleaf 3.1.5 + Thymeleaf Layout Dialect 4.0.1 |
| Persistence | Spring Data JPA + Hibernate 7 |
| Database | MySQL 8 |
| Đóng gói | `jar` (Thymeleaf đọc template từ classpath, không cần `war` như JSP) |

## 3. Bố cục trang (Thymeleaf Layout Dialect)

```text
templates/admin/
├── layout-admin.html          # layout chung: header + content + footer
├── fragments/
│   ├── header.html            # th:fragment="header" – ảnh + tên + MSSV sinh viên, menu
│   ├── footer.html            # th:fragment="footer" – thông tin sinh viên thực hiện
│   └── messages.html          # th:fragment="messages" – thông báo flash
└── categories/
    ├── searchpaginated.html   # layout:decorate="~{admin/layout-admin}"
    ├── addOrEdit.html         # dùng chung cho thêm và sửa (isEdit)
    └── view.html
```

- `layout-admin.html` nhúng header/footer bằng `th:replace="~{admin/fragments/header :: header}"`
  và để chỗ trống `<section layout:fragment="content">`.
- Mỗi trang con khai báo `layout:decorate="~{admin/layout-admin}"` và chỉ viết phần
  `layout:fragment="content"`. Tiêu đề trang ghép bằng `layout:title-pattern`.
- Spring Boot 4 tự đăng ký `LayoutDialect` khi có thư viện trên classpath
  (`ThymeleafWebLayoutConfiguration`), không cần tự khai báo bean.

## 4. Thông tin sinh viên (header/footer)

Sửa file `src/main/resources/student.properties` (đọc bằng UTF-8 nên gõ tiếng Việt có dấu được):

```properties
student.full-name=Họ và tên sinh viên
student.student-id=24162046
student.class-name=Lớp
student.email=email@student.hcmute.edu.vn
student.avatar=/images/student.svg
```

Đổi ảnh: chép ảnh của bạn vào `src/main/resources/static/images/` (ví dụ `student.jpg`)
rồi sửa `student.avatar=/images/student.jpg`.

> Không để các thông tin này trong `application.properties` vì Spring Boot đọc file đó theo
> ISO-8859-1, tiếng Việt có dấu sẽ bị lỗi.

## 5. Cấu hình database & chạy

Mặc định database `bt6crud`, user `root`, mật khẩu rỗng (giống BT5), chỉnh trong
`src/main/resources/application.properties`. Database tự được tạo khi chạy lần đầu;
có thể chạy `database.sql` để có sẵn 12 danh mục mẫu.

Từ thư mục `BT6`:

```bash
mvn spring-boot:run
```

Mở <http://localhost:8088/BT6/>

Hoặc build rồi chạy jar:

```bash
mvn clean package
java -jar target/BT6.jar
```

## 6. Cấu trúc code

```text
vn.iotstar
├── BT6Application.java
├── config/
│   ├── StudentInfo.java              # @ConfigurationProperties("student")
│   └── StudentPropertiesConfig.java  # nạp student.properties bằng UTF-8
├── entity/Category.java
├── model/CategoryModel.java          # DTO cho form (validate + isEdit)
├── repository/CategoryRepository.java
├── service/ICategoryService.java (+ impl/CategoryServiceImpl.java)
├── controller/
│   ├── admin/CategoryController.java
│   ├── HomeController.java
│   ├── ImageController.java          # tái sử dụng từ BT5
│   ├── GlobalModelAttributes.java    # đưa "student" vào model mọi trang
│   └── GlobalExceptionHandler.java   # file > 5 MB -> thông báo thân thiện
└── util/ImageUploadUtil.java         # tái sử dụng từ BT5
```

## 7. Tái sử dụng từ BT5

- Entity `Category` cùng tên bảng/cột (`categories`, `categoryId`, `categoryname`, `images`, `status`)
  và `PhysicalNamingStrategyStandardImpl` để Spring Boot không đổi tên cột sang snake_case.
- `ImageUploadUtil` + `ImageController`: đổi tên file bằng UUID, kiểm tra nội dung ảnh thật bằng
  `ImageIO`, giới hạn 5 MB, chặn path traversal.
- Cấu hình multipart 5 MB + `GlobalExceptionHandler`.
- Fragment header/footer/messages thay cho `<jsp:include>` của BT5.

## 8. Điểm kỹ thuật

- **PRG**: sau thêm/sửa/xóa dùng `redirect:` + flash attribute, F5 không gửi lại form.
- **Form dùng DTO** (`CategoryModel`), không bind thẳng vào entity; khi sửa, đường dẫn ảnh luôn lấy
  từ database chứ không tin hidden field của form.
- **Validate**: tên bắt buộc, tối đa 255 ký tự, không trùng (không phân biệt hoa thường), trạng thái 0/1.
- **Xóa bằng POST** (không phải GET) qua hộp thoại xác nhận Bootstrap 5.
- **Phân trang**: trang bắt đầu từ 1, trang vượt quá tự lùi về trang cuối, `size` ngoài danh sách cho phép
  quay về 5, hiển thị tối đa 5 số trang quanh trang hiện tại; liên kết phân trang giữ từ khóa tìm kiếm.

## 9. Kiểm thử

Đã `mvn clean package` thành công (Spring Boot 4.1.1, JDK 20) và chạy thử end-to-end trên H2
(chỉ thêm H2 vào classpath lúc chạy, không có trong `pom.xml`): 39 kịch bản gồm thêm/sửa/xóa/xem,
tìm kiếm tiếng Việt không phân biệt hoa thường, phân trang + đổi số dòng, validate, upload/thay/xóa ảnh,
ảnh giả mạo, file > 5 MB, path traversal, XSS. Chưa chạy thật với MySQL 8 vì máy không có MySQL.
