# Documentación de pruebas — Casa Sabor

## 1. Objetivo y alcance

Definir pruebas automatizadas y manuales para verificar validación, reglas de pedidos, autenticación, operaciones administrativas, flujo público y despliegue. No marcar como aprobada una prueba manual hasta ejecutarla y conservar evidencia.

## 2. Entorno

- Java 17, Spring Boot 3.5.4 y Maven.
- JUnit 5, Mockito y Jakarta Bean Validation mediante `spring-boot-starter-test`.
- Base de datos MySQL 8.x o TiDB compatible.
- Local: `mvn clean test`.
- CI: GitHub Actions, workflow `.github/workflows/ci.yml`.

## 3. Pruebas automatizadas existentes

### Validación de detalle de pedido

Archivo: `src/test/java/com/restaurante/pedidos/dto/DetallePedidoRequestValidationTest.java`.

| ID | Escenario | Resultado esperado |
|---|---|---|
| VAL-01 | Producto y cantidad positiva | Sin errores de validación |
| VAL-02 | Cantidad cero | Error en cantidad |
| VAL-03 | Cantidad negativa | Error en cantidad |
| VAL-04 | Producto no informado | Error en productoId |

### Servicio de pedidos

Archivo: `src/test/java/com/restaurante/pedidos/servicio/PedidoSerTest.java`.

| ID | Escenario | Resultado esperado |
|---|---|---|
| PED-01 | Pedido con varios productos | Detalles guardados y total calculado |
| PED-02 | Cliente inexistente | Se rechaza la operación |
| PED-03 | Producto inexistente | Se rechaza la operación |
| PED-04 | Producto no disponible | Se rechaza la operación |
| PED-05 | Cambio de estado | Nuevo estado persistido |
| PED-06 | Pedido inexistente | Se informa que no existe |

## 4. Pruebas manuales funcionales

Ejecutar en la URL local o de Render usando datos ficticios:

| ID | Módulo | Procedimiento | Resultado esperado |
|---|---|---|---|
| MAN-01 | Carta pública | Abrir la página sin iniciar sesión | Productos y categorías visibles |
| MAN-02 | Pedido público | Agregar productos y enviar datos válidos | Pedido creado sin login |
| MAN-03 | Validación | Enviar formulario incompleto o correo inválido | Mensaje de validación; no se crea pedido |
| MAN-04 | Disponibilidad | Intentar pedir producto agotado | Backend rechaza pedido |
| MAN-05 | Seguimiento | Consultar con código privado generado | Muestra estado, cantidades y total sin exponer datos del cliente |
| MAN-06 | Seguimiento inválido | Usar código incorrecto | Mensaje de pedido no encontrado |
| MAN-07 | Privacidad | Consultar `GET /api/clientes` sin login | HTTP 401/403 |
| MAN-08 | Privacidad | Consultar `GET /api/pedidos` sin login | HTTP 401/403 |
| MAN-09 | Login | Ingresar credenciales válidas | Acceso al panel |
| MAN-10 | Login | Ingresar contraseña incorrecta | Acceso denegado |
| MAN-11 | Sesión | Cerrar sesión y abrir `/admin.html` | Solicita autenticación |
| MAN-12 | Productos | Crear, editar y eliminar producto | Cambios persistidos |
| MAN-13 | Categorías | Crear y modificar categoría | Cambios persistidos |
| MAN-14 | Clientes | Registrar y consultar desde panel | Datos visibles solo al administrador |
| MAN-15 | Pedidos | Cambiar estado en el panel | Estado actualizado en seguimiento |
| MAN-16 | Persistencia | Reiniciar Render y volver a consultar | Datos conservados en TiDB |
| MAN-17 | Responsive | Usar panel y sitio en móvil | Sin desbordamiento horizontal |

## 5. Ejecución

Desde la carpeta que contiene `pom.xml`:

```bash
mvn clean test
```

Los reportes quedan en `target/surefire-reports/`. CI ejecuta las pruebas en cada push y pull request. La construcción Docker actual utiliza `-DskipTests`, por lo que la validación de pruebas depende del workflow CI.

## 6. Registro de resultados

| ID | Fecha | Estado (Aprobada/Fallida/Pendiente) | Evidencia |
|---|---|---|---|
| MAN-01 a MAN-17 | | Pendiente | |

## 7. Criterios de aceptación

- Todas las pruebas automatizadas terminan correctamente.
- MAN-01 a MAN-17 se ejecutan y documentan.
- Los endpoints administrativos no son accesibles sin sesión.
- El cliente puede crear y seguir su pedido usando un código aleatorio privado.
- Los datos sobreviven a un reinicio de la aplicación.
- El workflow CI finaliza en verde.
- No se declara aptitud comercial hasta resolver los pendientes de seguridad, respaldo y operación.
