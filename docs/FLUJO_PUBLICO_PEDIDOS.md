# Flujo público de pedidos y privacidad

## 1. Objetivo

Permitir que un visitante cree un pedido sin iniciar sesión, sin exponer el listado ni los datos personales de los clientes y sin utilizar el identificador secuencial de la base de datos como clave pública de seguimiento.

## 2. Creación del pedido

El navegador envía a `POST /api/pedidos/publicos` un JSON con datos del cliente y una lista de detalles. El servidor:

1. Valida los datos del cliente y las cantidades.
2. Busca al cliente por correo electrónico (sin distinguir mayúsculas/minúsculas); lo crea si no existe o actualiza sus datos básicos.
3. Verifica cada producto y su disponibilidad.
4. Calcula el total en el servidor usando el precio guardado en la base de datos y `BigDecimal`.
5. Genera un UUID aleatorio para seguimiento.
6. Persiste pedido y detalles dentro de una transacción.
7. Devuelve una respuesta pública limitada: identificador interno no expuesto, código de seguimiento, total, estado y detalles con nombre/cantidad.

El cliente nunca envía el total ni el precio unitario como fuente de verdad.

## 3. Seguimiento

El endpoint `GET /api/pedidos/seguimiento/{codigo}` busca por UUID y devuelve solamente estado, total y detalles básicos. No devuelve nombre, correo ni teléfono del cliente. El código debe tratarse como secreto: cualquier persona que lo tenga puede consultar la información básica del pedido.

La interfaz conserva el último código en el almacenamiento local del navegador y lo muestra al confirmar el pedido. El cliente debe guardarlo para consultar desde otro dispositivo.

## 4. Endpoints administrativos

Las rutas generales `/api/clientes`, `/api/pedidos` y las operaciones de escritura de productos/categorías siguen protegidas por autenticación. Se eliminó CORS global en los controladores porque el frontend se sirve desde el mismo origen.

## 5. Compatibilidad de datos existentes

La columna `codigo_seguimiento` permite valores nulos para que registros históricos no impidan la actualización automática del esquema. Los pedidos nuevos generan código UUID. Los pedidos anteriores no tendrán código público de seguimiento automáticamente.

## 6. Limitaciones conocidas

- El código de seguimiento funciona como un secreto portador; no es una cuenta de usuario ni verifica identidad.
- Falta limitación de frecuencia/rate limiting y monitoreo antiabuso.
- La interfaz administrativa todavía requiere integración CSRF antes de habilitar esa protección.
- Para uso real se requiere política de privacidad, retención y controles de acceso adicionales.
