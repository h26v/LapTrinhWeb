-- =========================================================
-- BT1 - LTWeb : MySQL schema + seed
-- Chay: mysql -u root -p < database.sql
-- =========================================================
DROP DATABASE IF EXISTS servletcrudmvc;
CREATE DATABASE servletcrudmvc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE servletcrudmvc;

-- Bang Category (CRUD)
CREATE TABLE category (
    cate_id   INT AUTO_INCREMENT PRIMARY KEY,
    cate_name VARCHAR(255) NOT NULL,
    icons     VARCHAR(255) NULL
);

INSERT INTO category (cate_name, icons) VALUES
('Dien thoai', NULL),
('Laptop', NULL),
('Phu kien', NULL);

-- Bang User (Login Cookie / Login Session)
CREATE TABLE users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255) NULL,
    username    VARCHAR(100) NOT NULL UNIQUE,
    fullname    VARCHAR(255) NULL,
    password    VARCHAR(255) NOT NULL,
    avatar      VARCHAR(255) NULL,
    roleid      INT NOT NULL DEFAULT 5,
    phone       VARCHAR(20)  NULL,
    createddate DATE NULL
);

-- Tai khoan demo: trung / 123
INSERT INTO users (email, username, fullname, password, avatar, roleid, phone, createddate)
VALUES ('trung@iotstar.vn', 'trung', 'Nguyen Huu Trung', '123', NULL, 1, '0908617108', CURDATE());
