-- BT4 - JPA / Hibernate / SiteMesh Bootstrap
-- Hibernate tu tao/cap nhat bang (hbm2ddl.auto=update) va tu tao database
-- (createDatabaseIfNotExist=true). File nay tao schema demo ro rang de cham bai.

CREATE DATABASE IF NOT EXISTS bt4crud
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bt4crud;

-- Bang users: co tai khoan dang nhap va cac cot OTP kich hoat/reset mat khau.
CREATE TABLE IF NOT EXISTS users (
    id                         INT AUTO_INCREMENT PRIMARY KEY,
    email                      NVARCHAR(255) NULL,
    username                   VARCHAR(100) NOT NULL UNIQUE,
    fullname                   NVARCHAR(255) NULL,
    password                   VARCHAR(255) NOT NULL,
    avatar                     NVARCHAR(255) NULL,
    roleid                     INT NOT NULL DEFAULT 2,
    phone                      VARCHAR(20) NULL,
    createddate                DATE NULL,
    active                     INT NOT NULL DEFAULT 1,
    activation_otp_hash        VARCHAR(255) NULL,
    activation_otp_expires_at  DATETIME NULL,
    reset_otp_hash             VARCHAR(255) NULL,
    reset_otp_expires_at       DATETIME NULL
);

-- Neu database cu da ton tai, Hibernate se bo sung cac cot moi nho hbm2ddl.auto=update.

INSERT INTO users (email, username, fullname, password, roleid, phone, createddate, active)
SELECT 'trung@iotstar.vn', 'trung', 'Nguyen Van Trung', '123', 1, '0900000000', CURDATE(), 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'trung');

-- Bang categories phuc vu quan he 1-n voi products.
CREATE TABLE IF NOT EXISTS categories (
    categoryId    INT AUTO_INCREMENT PRIMARY KEY,
    categoryname  NVARCHAR(255) NOT NULL,
    images        NVARCHAR(500) NULL,
    status        INT NOT NULL DEFAULT 1
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
