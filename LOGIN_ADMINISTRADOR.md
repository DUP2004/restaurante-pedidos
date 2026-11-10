# Acceso al panel administrador — Casa Sabor

## Iniciar sesión en desarrollo local

1. Inicia MySQL y ejecuta la aplicación Spring Boot.
2. Abre `http://localhost:8080/login.html`.
3. Usa las credenciales predeterminadas de desarrollo:
   - Usuario: `admin`
   - Contraseña: `admin123`
4. Al autenticarte correctamente, la aplicación redirige a `/admin.html`.
5. Para cerrar sesión, utiliza la opción de cierre de sesión del panel o visita `/logout`.

## Cambiar las credenciales

Define estas variables de entorno antes de iniciar la aplicación:

- `ADMIN_USERNAME`: nombre de usuario administrador.
- `ADMIN_PASSWORD`: contraseña del administrador.

Ejemplo en PowerShell:

```powershell
$env:ADMIN_USERNAME="administrador"
$env:ADMIN_PASSWORD="Escribe-una-clave-larga-y-unica"
mvn spring-boot:run
```

En Docker Compose, puedes establecerlas en un archivo `.env` local que no debe subirse a GitHub. En un proveedor cloud, configúralas en la sección de variables/secretos del servicio.

## Seguridad

Los valores predeterminados solo sirven para desarrollo local. No publiques la aplicación con `admin/admin123`. Utiliza HTTPS y una contraseña fuerte, y revisa la protección CSRF antes de utilizar el sistema con información real.
