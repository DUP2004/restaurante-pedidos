# Manual de usuario — Casa Sabor

## 1. Objetivo

Explicar el uso del sitio público de pedidos y del panel administrativo del restaurante.

## 2. Sitio público

1. Ingresar a la dirección web proporcionada por quien administra el sistema.
2. Revisar las categorías y los platos disponibles.
3. Seleccionar los productos que se desean pedir y completar los datos solicitados por el formulario.
4. Confirmar el pedido y conservar el número o identificador mostrado por la aplicación.
5. Consultar el avance desde la opción de seguimiento, si está disponible en la versión desplegada.

El pedido se registra como `PENDIENTE`. El restaurante actualiza el estado desde el panel.

## 3. Acceso del administrador

1. Abrir `/login.html` en el mismo dominio de la aplicación.
2. Introducir el usuario y contraseña que entregó el responsable del sistema.
3. Al iniciar sesión se abre `/admin.html`.
4. Para salir, utilizar la opción de cerrar sesión.

No compartir las credenciales administrativas con clientes.

## 4. Panel administrativo

El panel dispone de secciones para:

- **Resumen:** vista general de la operación.
- **Pedidos:** revisar pedidos recibidos y actualizar su estado.
- **Productos:** registrar, modificar y eliminar productos, incluidos precio, descripción, categoría y disponibilidad.
- **Categorías:** organizar los productos del menú.
- **Clientes:** consultar y mantener los datos registrados.

Los nombres exactos de botones y campos pueden variar según la versión visual.

## 5. Gestión de pedidos

Los estados disponibles son:

1. `PENDIENTE`: el pedido fue recibido.
2. `PREPARANDO`: el restaurante está preparando el pedido.
3. `LISTO`: está preparado para recoger.
4. `ENTREGADO`: el pedido fue entregado.
5. `CANCELADO`: el pedido fue cancelado.

Actualizar el estado solo cuando corresponda al avance real del pedido.

## 6. Recomendaciones

- Verificar precio y disponibilidad antes de confirmar cambios del menú.
- Mantener actualizados teléfono, dirección y horario desde `src/main/resources/static/js/config.js`.
- No eliminar registros si se necesitan para reportes o trazabilidad.
- Cerrar sesión al terminar de utilizar el panel.
- Realizar copias de seguridad periódicas de MySQL.
- No utilizar credenciales de ejemplo en una instalación pública.

## 7. Ayuda técnica

Para problemas de instalación, variables de entorno o despliegue, consultar el README y la [guía de despliegue](GUIA_DESPLIEGUE_GITHUB.md).
