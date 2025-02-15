CREATE INDEX IF NOT EXISTS idx_users_phone_number ON users(phone_number);

CREATE TABLE IF NOT EXISTS authorities (
    phone_number VARCHAR(9) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (phone_number) REFERENCES users(phone_number) ON DELETE CASCADE
);
--
--INSERT IGNORE INTO users (id, name, phone_number, email, password, access_level,  enabled)
--VALUES (1, 'Super Admin', '900000000', 'admin@example.com', '12345', 'ROLE_SUPER_ADMIN', TRUE);
--
--INSERT IGNORE INTO authorities (phone_number, authority)
--VALUES('900000000', 'ROLE_SUPER_ADMIN');

