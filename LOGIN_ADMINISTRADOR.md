# Login del administrador — Casa Sabor

## Cómo ingresar
1. Ejecuta la aplicación Spring Boot normalmente.
2. Abre `http://localhost:8080/login.html`.
3. Ingresa estas credenciales iniciales:
   - Usuario: `admin`
   - Contraseña: `CambiarEstaClave123!`
4. Si las credenciales son correctas, se abrirá `/admin.html`. Si son incorrectas, aparecerá un mensaje de error.
5. Para cerrar sesión, abre `http://localhost:8080/logout`.

## Cambiar las credenciales
Puedes definir variables de entorno antes de iniciar Spring Boot:
- `ADMIN_USERNAME`: usuario administrador.
- `ADMIN_PASSWORD`: contraseña administrador.

Los valores por defecto son solo para probar localmente. Cámbialos antes de publicar el sistema en Internet.

## Nota de seguridad
El panel y las rutas administrativas de `/api/**` requieren una sesión autenticada. La consulta pública de productos/categorías y el envío de un pedido se mantienen permitidos para que el sitio de clientes siga funcionando. Para una publicación real, se recomienda habilitar protección CSRF y revisar los permisos públicos según el flujo definitivo de pedidos.
