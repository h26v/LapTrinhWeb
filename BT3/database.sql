-- BT3 - JPA / Hibernate
-- Hibernate tu tao bang (hbm2ddl.auto=update) va tu tao database
-- (createDatabaseIfNotExist=true). File nay chi seed du lieu demo.

CREATE DATABASE IF NOT EXISTS bt3crud
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bt3crud;

-- Bang users: tao san de co tai khoan dang nhap truoc khi Hibernate chay.
-- Cot khop voi vn.iotstar.entity.User.
CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    email       NVARCHAR(255) NULL,
    username    VARCHAR(100) NOT NULL UNIQUE,
    fullname    NVARCHAR(255) NULL,
    password    VARCHAR(255) NOT NULL,
    avatar      NVARCHAR(255) NULL,
    roleid      INT NOT NULL DEFAULT 2,
    phone       VARCHAR(20) NULL,
    createddate DATE NULL
);

INSERT INTO users (email, username, fullname, password, roleid, phone, createddate)
SELECT 'trung@iotstar.vn', 'trung', 'Nguyen Van Trung', '123', 1, '0900000000', CURDATE()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'trung');
