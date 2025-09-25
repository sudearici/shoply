-- Admin kullanıcı ekle
INSERT INTO users (username, password, email, enabled) VALUES
('admin', '$2a$10$7QOQWqNwlE4P4D8mFeu8sOZVvXzG9V2x8qkJbI8QvZ2ZyHpw5sG0a', 'admin@example.com', true);

-- Admin rol atama
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username='admin' AND r.name='ROLE_ADMIN';
