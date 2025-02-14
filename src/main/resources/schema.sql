DELETE FROM users WHERE email = 'admin@example.com';

INSERT INTO users (id, name, phone_number, email, password, access_level, data, last_login)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), 'Super Admin', '900000000', 'admin@example.com', '12345', 'SUPER_ADMIN', NOW(), NOW());
