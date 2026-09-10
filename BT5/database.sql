-- BT5 - Spring Boot 4 + JSP/JSTL + Spring Data JPA
-- Hibernate tu tao/cap nhat bang (ddl-auto=update) va tu tao database
-- (createDatabaseIfNotExist=true). File nay tao san schema demo de cham bai.

CREATE DATABASE IF NOT EXISTS bt5crud
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bt5crud;

-- Bang users: tai khoan dang nhap. roleid = 1 la admin, roleid = 2 la nguoi dung.
CREATE TABLE IF NOT EXISTS users (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    email      NVARCHAR(255) NULL,
    username   VARCHAR(100) NOT NULL UNIQUE,
    fullname   NVARCHAR(255) NULL,
    password   VARCHAR(255) NOT NULL,
    avatar     NVARCHAR(255) NULL,
    roleid     INT NOT NULL DEFAULT 2,
    phone      VARCHAR(20) NULL,
    createddate DATE NULL,
    active     INT NOT NULL DEFAULT 1
);

-- Tai khoan admin demo. Mat khau de plain text cho de demo;
-- sau lan dang nhap thanh cong dau tien ung dung tu doi sang BCrypt hash.
INSERT INTO users (email, username, fullname, password, roleid, phone, createddate, active)
SELECT 'trung@iotstar.vn', 'trung', 'Nguyen Van Trung', '123', 1, '0900000000', CURDATE(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'trung');

-- Vai tai khoan nguoi dung thuong de thu chuc nang tim kiem.
INSERT INTO users (email, username, fullname, password, roleid, phone, createddate, active)
SELECT 'an@iotstar.vn', 'an', 'Tran Thi An', '123', 2, '0911111111', CURDATE(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'an');

INSERT INTO users (email, username, fullname, password, roleid, phone, createddate, active)
SELECT 'binh@iotstar.vn', 'binh', 'Le Van Binh', '123', 2, '0922222222', CURDATE(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'binh');

INSERT INTO users (email, username, fullname, password, roleid, phone, createddate, active)
SELECT 'chi@iotstar.vn', 'chi', 'Pham Thi Chi', '123', 2, '0933333333', CURDATE(), 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'chi');

-- Bang categories phuc vu quan he 1-n voi products.
CREATE TABLE IF NOT EXISTS categories (
    categoryId    INT AUTO_INCREMENT PRIMARY KEY,
    categoryname  NVARCHAR(255) NOT NULL,
    images        NVARCHAR(500) NULL,
    status        INT NOT NULL DEFAULT 1
);

-- Bang videos: quan he nhieu-mot voi categories (giu tu BT3/BT4).
CREATE TABLE IF NOT EXISTS videos (
    videoId     VARCHAR(255) PRIMARY KEY,
    active      INT NOT NULL DEFAULT 1,
    description NVARCHAR(500) NULL,
    poster      NVARCHAR(500) NULL,
    title       NVARCHAR(500) NULL,
    views       INT NOT NULL DEFAULT 0,
    categoryId  INT NULL,
    CONSTRAINT fk_videos_categories FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);

-- Bang products: moi san pham thuoc mot category.
CREATE TABLE IF NOT EXISTS products (
    productId    INT AUTO_INCREMENT PRIMARY KEY,
    productname  NVARCHAR(255) NOT NULL,
    description  NVARCHAR(1000) NULL,
    price        DECIMAL(12,2) NOT NULL DEFAULT 0,
    images       NVARCHAR(500) NULL,
    status       INT NOT NULL DEFAULT 1,
    createddate  DATETIME NULL,
    categoryId   INT NOT NULL,
    CONSTRAINT fk_products_categories FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);

-- Du lieu mau cho category + product (chi them khi bang dang rong).
INSERT INTO categories (categoryname, images, status)
SELECT 'Dien thoai', NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE categoryname = 'Dien thoai');

INSERT INTO categories (categoryname, images, status)
SELECT 'Laptop', NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE categoryname = 'Laptop');

INSERT INTO categories (categoryname, images, status)
SELECT 'Phu kien', NULL, 1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE categoryname = 'Phu kien');

INSERT INTO products (productname, description, price, images, status, createddate, categoryId)
SELECT 'iPhone 15', 'Dien thoai Apple', 22990000, NULL, 1, NOW(),
       (SELECT categoryId FROM categories WHERE categoryname = 'Dien thoai' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE productname = 'iPhone 15');

INSERT INTO products (productname, description, price, images, status, createddate, categoryId)
SELECT 'MacBook Air M3', 'Laptop Apple', 27990000, NULL, 1, NOW(),
       (SELECT categoryId FROM categories WHERE categoryname = 'Laptop' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE productname = 'MacBook Air M3');

INSERT INTO products (productname, description, price, images, status, createddate, categoryId)
SELECT 'Tai nghe Bluetooth', 'Phu kien am thanh', 590000, NULL, 1, NOW(),
       (SELECT categoryId FROM categories WHERE categoryname = 'Phu kien' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM products WHERE productname = 'Tai nghe Bluetooth');
