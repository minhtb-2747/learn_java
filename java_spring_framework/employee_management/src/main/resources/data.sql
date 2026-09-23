-- Seed 1 tài khoản ADMIN để test phân quyền (register qua API chỉ tạo được USER)
-- username: admin / password: admin123
INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', '$2a$10$xp5qOu2CKwW8y7/0Gwg46epzJR4LC8mO/MBZr0CKBd0JEf43zadHO', 'ADMIN');
