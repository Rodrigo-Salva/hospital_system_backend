    -- ============================================================
-- Datos iniciales: usuario admin por defecto
-- Contrasena: admin123  (BCrypt hash)
-- ============================================================

-- Asegurar que la columna activo tenga valor por defecto antes de insertar
UPDATE usuarios SET activo = true WHERE activo IS NULL;

INSERT INTO usuarios (username, password, activo)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', true)
ON CONFLICT (username) DO NOTHING;

-- Asignar rol ROLE_ADMIN al usuario admin
INSERT INTO usuario_roles (usuario_id, rol)
SELECT id, 'ROLE_ADMIN'
FROM usuarios
WHERE username = 'admin'
ON CONFLICT DO NOTHING;
