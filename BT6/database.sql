-- BT6 - Spring Boot 4 + Thymeleaf + Thymeleaf Layout Dialect
-- Hibernate tu tao/cap nhat bang (ddl-auto=update) va tu tao database
-- (createDatabaseIfNotExist=true). File nay tao san schema + du lieu demo de cham bai.

CREATE DATABASE IF NOT EXISTS bt6crud
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bt6crud;

-- Ten cot giong BT3/BT4/BT5 (categoryId, categoryname, images, status).
CREATE TABLE IF NOT EXISTS categories (
    categoryId    BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoryname  NVARCHAR(255) NOT NULL,
    images        NVARCHAR(500) NULL,
    status        INT NOT NULL DEFAULT 1
);

-- 12 danh muc mau de thu phan trang (5 dong/trang -> 3 trang) va tim kiem.
INSERT INTO categories (categoryname, images, status)
SELECT t.name, NULL, t.status
FROM (
    SELECT 'Điện thoại' AS name, 1 AS status UNION ALL
    SELECT 'Laptop', 1 UNION ALL
    SELECT 'Phụ kiện', 1 UNION ALL
    SELECT 'Máy tính bảng', 1 UNION ALL
    SELECT 'Tai nghe', 1 UNION ALL
    SELECT 'Đồng hồ thông minh', 1 UNION ALL
    SELECT 'Camera', 1 UNION ALL
    SELECT 'Loa', 1 UNION ALL
    SELECT 'Màn hình', 1 UNION ALL
    SELECT 'Bàn phím', 1 UNION ALL
    SELECT 'Chuột', 0 UNION ALL
    SELECT 'Sạc dự phòng', 0
) AS t
WHERE NOT EXISTS (SELECT 1 FROM categories c WHERE c.categoryname = t.name);
