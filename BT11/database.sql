/* =========================================================
   De thi qua trinh LTWeb - De 01 - MSSV 24162046
   Database: BookStore (SQL Server) - tat ca ID tang tu dong
   ========================================================= */
IF DB_ID(N'BookStore') IS NULL
    CREATE DATABASE BookStore;
GO
USE BookStore;
GO

IF OBJECT_ID(N'dbo.order_items', N'U') IS NOT NULL DROP TABLE dbo.order_items;
IF OBJECT_ID(N'dbo.orders', N'U') IS NOT NULL DROP TABLE dbo.orders;
IF OBJECT_ID(N'dbo.cart_items', N'U') IS NOT NULL DROP TABLE dbo.cart_items;
IF OBJECT_ID(N'dbo.rating', N'U') IS NOT NULL DROP TABLE dbo.rating;
IF OBJECT_ID(N'dbo.book_author', N'U') IS NOT NULL DROP TABLE dbo.book_author;
IF OBJECT_ID(N'dbo.users', N'U') IS NOT NULL DROP TABLE dbo.users;
IF OBJECT_ID(N'dbo.author', N'U') IS NOT NULL DROP TABLE dbo.author;
IF OBJECT_ID(N'dbo.books', N'U') IS NOT NULL DROP TABLE dbo.books;
GO

CREATE TABLE dbo.books (
    bookid       INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    isbn         INT            NULL,
    title        VARCHAR(200)   NULL,
    publisher    VARCHAR(100)   NULL,
    price        DECIMAL(6, 2)  NULL,
    description  TEXT           NULL,
    publish_date DATE           NULL,
    cover_image  VARCHAR(100)   NULL,
    quantity     INT            NULL
);

CREATE TABLE dbo.users (
    id          INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    email       VARCHAR(50)   NOT NULL,
    fullname    NVARCHAR(50)  NULL,
    phone       INT           NULL,
    passwd      VARCHAR(32)   NOT NULL,
    signup_date DATETIME      NULL,
    last_login  DATETIME      NULL,
    is_admin    BIT           NULL
);

CREATE TABLE dbo.author (
    author_id     INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    author_name   VARCHAR(100) NULL,
    date_of_birth DATE         NULL
);

CREATE TABLE dbo.book_author (
    bookid    INT NOT NULL,
    author_id INT NOT NULL,
    CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),
    CONSTRAINT FK_book_author_books  FOREIGN KEY (bookid)    REFERENCES dbo.books(bookid),
    CONSTRAINT FK_book_author_author FOREIGN KEY (author_id) REFERENCES dbo.author(author_id)
);

CREATE TABLE dbo.rating (
    userid      INT     NOT NULL,
    bookid      INT     NOT NULL,
    rating      TINYINT NULL,
    review_text TEXT    NULL,
    CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),
    CONSTRAINT FK_rating_users FOREIGN KEY (userid) REFERENCES dbo.users(id),
    CONSTRAINT FK_rating_books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid)
);

-- Gio hang: moi user 1 dong / 1 sach, so luong > 0
CREATE TABLE dbo.cart_items (
    id       INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    userid   INT      NOT NULL,
    bookid   INT      NOT NULL,
    quantity INT      NOT NULL,
    added_at DATETIME NULL,
    CONSTRAINT UQ_cart_items_user_book UNIQUE (userid, bookid),
    CONSTRAINT CK_cart_items_quantity CHECK (quantity > 0),
    CONSTRAINT FK_cart_items_users FOREIGN KEY (userid) REFERENCES dbo.users(id),
    CONSTRAINT FK_cart_items_books FOREIGN KEY (bookid) REFERENCES dbo.books(bookid)
);

-- Don hang: thanh toan COD, status luu dang chuoi
CREATE TABLE dbo.orders (
    order_id       INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    userid         INT            NOT NULL,
    receiver_name  NVARCHAR(50)   NOT NULL,
    phone          VARCHAR(15)    NOT NULL,
    address        NVARCHAR(255)  NOT NULL,
    note           NVARCHAR(255)  NULL,
    payment_method VARCHAR(20)    NOT NULL DEFAULT 'COD',
    status         VARCHAR(20)    NOT NULL DEFAULT 'NEW',
    total_amount   DECIMAL(12, 2) NOT NULL,
    created_at     DATETIME       NULL,
    CONSTRAINT CK_orders_status CHECK (status IN
        ('NEW', 'CONFIRMED', 'PREPARING', 'SHIPPING', 'DELIVERING', 'DELIVERED', 'CANCELLED', 'RETURNED')),
    CONSTRAINT FK_orders_users FOREIGN KEY (userid) REFERENCES dbo.users(id)
);

-- Chi tiet don: luu ten + gia luc dat, bookid = NULL neu sach bi xoa
CREATE TABLE dbo.order_items (
    id         INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    order_id   INT           NOT NULL,
    bookid     INT           NULL,
    book_title VARCHAR(200)  NOT NULL,
    price      DECIMAL(6, 2) NOT NULL,
    quantity   INT           NOT NULL,
    CONSTRAINT CK_order_items_quantity CHECK (quantity > 0),
    CONSTRAINT FK_order_items_orders FOREIGN KEY (order_id) REFERENCES dbo.orders(order_id),
    CONSTRAINT FK_order_items_books  FOREIGN KEY (bookid)   REFERENCES dbo.books(bookid)
);
GO

/* ------------------------- Du lieu test ------------------------- */
-- Mat khau tat ca tai khoan: 123456 (MD5 = e10adc3949ba59abbe56e057f20f883e)
INSERT INTO dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin) VALUES
('admin@gmail.com', N'Quản trị viên',     901234567, 'e10adc3949ba59abbe56e057f20f883e', GETDATE(), NULL, 1),
('user@gmail.com',  N'Nguyễn Văn User',   912345678, 'e10adc3949ba59abbe56e057f20f883e', GETDATE(), NULL, 0),
('an@gmail.com',    N'Trần Thị An',       923456789, 'e10adc3949ba59abbe56e057f20f883e', GETDATE(), NULL, 0),
('binh@gmail.com',  N'Lê Văn Bình',       934567890, 'e10adc3949ba59abbe56e057f20f883e', GETDATE(), NULL, 0);

INSERT INTO dbo.author (author_name, date_of_birth) VALUES
('Robert C. Martin',  '1952-12-05'),
('Martin Fowler',     '1963-12-18'),
('Joshua Bloch',      '1961-08-28'),
('Kathy Sierra',      '1957-01-01'),
('Bert Bates',        '1955-01-01'),
('Eric Freeman',      '1965-01-01'),
('Andrew Hunt',       '1964-01-01'),
('David Thomas',      '1956-01-01');

INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity) VALUES
(132350882, 'Clean Code',                              'Prentice Hall',   32.50, 'A handbook of agile software craftsmanship.',          '2008-08-01', 'https://picsum.photos/seed/book01/300/400', 20),
(134494164, 'Clean Architecture',                      'Prentice Hall',   29.99, 'A craftsman''s guide to software structure and design.', '2017-09-10', 'https://picsum.photos/seed/book02/300/400', 15),
(137081073, 'The Clean Coder',                         'Prentice Hall',   27.00, 'A code of conduct for professional programmers.',       '2011-05-13', 'https://picsum.photos/seed/book03/300/400', 12),
(134757599, 'Refactoring',                             'Addison-Wesley',  45.00, 'Improving the design of existing code.',                '2018-11-20', 'https://picsum.photos/seed/book04/300/400', 8),
(321127420, 'Patterns of Enterprise Application Architecture', 'Addison-Wesley', 55.00, 'Enterprise patterns catalog.',                   '2002-11-15', 'https://picsum.photos/seed/book05/300/400', 5),
(321193687, 'UML Distilled',                           'Addison-Wesley',  30.00, 'A brief guide to the standard object modeling language.', '2003-09-15', 'https://picsum.photos/seed/book06/300/400', 10),
(134685997, 'Effective Java',                          'Addison-Wesley',  41.00, 'Best practices for the Java platform.',                 '2017-12-27', 'https://picsum.photos/seed/book07/300/400', 25),
(321349601, 'Java Puzzlers',                           'Addison-Wesley',  25.00, 'Traps, pitfalls, and corner cases.',                    '2005-07-04', 'https://picsum.photos/seed/book08/300/400', 6),
(596009208, 'Head First Java',                         'O''Reilly Media', 39.99, 'A brain-friendly guide to Java.',                       '2005-02-09', 'https://picsum.photos/seed/book09/300/400', 18),
(596007124, 'Head First Design Patterns',              'O''Reilly Media', 44.99, 'A brain-friendly guide to design patterns.',            '2004-10-25', 'https://picsum.photos/seed/book10/300/400', 14),
(596101015, 'Head First HTML with CSS and XHTML',      'O''Reilly Media', 35.00, 'Learn HTML and CSS the brain-friendly way.',             '2005-12-01', 'https://picsum.photos/seed/book11/300/400', 9),
(201616224, 'The Pragmatic Programmer',                'Addison-Wesley',  42.00, 'From journeyman to master.',                            '1999-10-20', 'https://picsum.photos/seed/book12/300/400', 11),
(135957059, 'The Pragmatic Programmer 20th Anniversary','Addison-Wesley', 49.99, 'Your journey to mastery.',                              '2019-09-13', 'https://picsum.photos/seed/book13/300/400', 7),
(974514055, 'Programming Ruby',                        'Pragmatic Bookshelf', 38.00, 'The pragmatic programmers'' guide.',                 '2004-10-01', 'https://picsum.photos/seed/book14/300/400', 4);

INSERT INTO dbo.book_author (bookid, author_id) VALUES
(1, 1), (2, 1), (3, 1),
(4, 2), (5, 2), (6, 2),
(7, 3), (8, 3),
(9, 4), (9, 5),
(10, 6), (10, 4), (10, 5),
(11, 6),
(12, 7), (12, 8),
(13, 7), (13, 8),
(14, 8);

INSERT INTO dbo.rating (userid, bookid, rating, review_text) VALUES
(2, 1, 5, 'Must read for every developer.'),
(3, 1, 4, 'Very practical examples.'),
(4, 1, 5, 'Changed the way I write code.'),
(2, 2, 4, 'Good overview of architecture.'),
(3, 4, 5, 'Classic refactoring catalog.'),
(2, 7, 5, 'Best Java book ever.'),
(4, 7, 4, 'Dense but very useful.'),
(3, 9, 4, 'Fun to read for beginners.'),
(2, 10, 5, 'Design patterns made easy.'),
(4, 12, 5, 'Timeless advice.');
GO

/* ------------------- Doi trang thai don hang de test ------------------
   Dat hang tren web xong, chay cac lenh duoi day roi F5 trang "Don hang"
   de thay don chuyen sang tab tuong ung.

   NEW        : Don hang moi (mac dinh khi dat)
   CONFIRMED  : Da xac nhan
   PREPARING  : Chuan bi hang
   SHIPPING   : Dang van chuyen
   DELIVERING : Dang giao hang
   DELIVERED  : Da giao
   CANCELLED  : Da huy
   RETURNED   : Hoan hang

   SELECT order_id, userid, receiver_name, status, total_amount, created_at
   FROM dbo.orders ORDER BY order_id DESC;

   UPDATE dbo.orders SET status = 'CONFIRMED'  WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'PREPARING'  WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'SHIPPING'   WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'DELIVERING' WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'DELIVERED'  WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'CANCELLED'  WHERE order_id = 1;
   UPDATE dbo.orders SET status = 'RETURNED'   WHERE order_id = 1;
   ---------------------------------------------------------------------- */
