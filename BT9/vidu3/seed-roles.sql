-- Chay 1 lan sau khi tao database, truoc khi start app lan dau (neu chua co DataInitializer
-- hoac muon them thu cong). DataInitializer.java trong project da tu tao 2 role nay roi.

INSERT INTO roles (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');
