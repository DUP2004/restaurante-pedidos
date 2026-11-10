-- Datos de ejemplo. Solo se insertan si las tablas están vacías,
-- así que no pisan lo que cargues desde el panel de administración.

INSERT INTO categorias (nombre, descripcion)
SELECT * FROM (
    SELECT 'Entradas' AS nombre, 'Para empezar y compartir' AS descripcion
    UNION ALL SELECT 'Platos de fondo', 'Cocina del día, servida con guarnición'
    UNION ALL SELECT 'Postres', 'Hechos en casa'
    UNION ALL SELECT 'Bebidas', 'Frías y naturales'
) AS nuevas
WHERE NOT EXISTS (SELECT 1 FROM categorias);

INSERT INTO productos (nombre, descripcion, precio, disponible, categoria_id)
SELECT n.nombre, n.descripcion, n.precio, 1,
       (SELECT c.id FROM categorias c WHERE c.nombre = n.categoria)
FROM (
    SELECT 'Papa a la huancaína' AS nombre, 'Papa sancochada con crema de ají amarillo, huevo y aceituna' AS descripcion, 14.00 AS precio, 'Entradas' AS categoria
    UNION ALL SELECT 'Anticuchos de corazón', 'Brochetas a la parrilla con papa y choclo', 22.00, 'Entradas'
    UNION ALL SELECT 'Tequeños de queso', 'Seis unidades con salsa de guacamole', 16.00, 'Entradas'
    UNION ALL SELECT 'Lomo saltado', 'Lomo fino salteado al wok con cebolla, tomate y papas fritas', 32.00, 'Platos de fondo'
    UNION ALL SELECT 'Ají de gallina', 'Pollo deshilachado en crema de ají amarillo, con arroz y papa', 28.00, 'Platos de fondo'
    UNION ALL SELECT 'Arroz con pollo', 'Arroz al cilantro con presa de pollo y salsa criolla', 26.00, 'Platos de fondo'
    UNION ALL SELECT 'Hamburguesa doble', 'Dos carnes, queso, cebolla caramelizada y papas al hilo', 24.00, 'Platos de fondo'
    UNION ALL SELECT 'Suspiro limeño', 'Manjar blanco con merengue de oporto', 12.00, 'Postres'
    UNION ALL SELECT 'Tres leches', 'Bizcocho húmedo con crema chantilly', 13.00, 'Postres'
    UNION ALL SELECT 'Chicha morada', 'Jarra de un litro', 14.00, 'Bebidas'
    UNION ALL SELECT 'Limonada', 'Vaso grande, con hielo', 8.00, 'Bebidas'
    UNION ALL SELECT 'Maracuyá frozen', 'Pulpa de maracuyá licuada con hielo', 9.00, 'Bebidas'
) AS n
WHERE NOT EXISTS (SELECT 1 FROM productos);
