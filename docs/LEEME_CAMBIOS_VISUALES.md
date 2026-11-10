# Cambios visuales del panel

Se actualizó `src/main/resources/static/css/estilos.css` y se agregó un parámetro de versión al enlace CSS de `admin.html` para evitar que el navegador reutilice una hoja de estilos antigua.

Después de iniciar nuevamente Spring Boot:
1. Abre `http://localhost:8080/admin.html`.
2. Presiona `Ctrl + Shift + R` para forzar la recarga.
3. Si el diseño sigue igual, abre `http://localhost:8080/css/estilos.css?v=20261110-2`. Debe mostrarse el CSS; si aparece una página de login o un error, revisa la configuración de seguridad y confirma que estás ejecutando el proyecto correcto.
