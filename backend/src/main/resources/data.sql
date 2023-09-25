-- roles
INSERT IGNORE INTO roles(id, name)
VALUES (1, 'USER');

INSERT IGNORE INTO roles(id, name)
VALUES (2, 'ADMIN');

-- default admin user
INSERT IGNORE INTO users(id, email, password, username)
VALUES (1, 'admin@example.com', '{noop}admin@admin', 'admin');
INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES (1, 2);
INSERT IGNORE INTO user_roles(user_id, role_id)
VALUES (1, 1);
