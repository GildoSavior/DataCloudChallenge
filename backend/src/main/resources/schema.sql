CREATE INDEX IF NOT EXISTS idx_users_phone_number ON users(phone_number);

CREATE TABLE IF NOT EXISTS authorities (
    phone_number VARCHAR(9) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (phone_number) REFERENCES users(phone_number) ON DELETE CASCADE
);


