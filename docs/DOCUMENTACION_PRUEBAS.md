# Documentación de pruebas – Casa Sabor

## 1. Objetivo
Documentar las pruebas automatizadas existentes y las pruebas manuales propuestas para verificar las funciones principales del sistema de pedidos.

## 2. Entorno
- Aplicación: Spring Boot 3.5.4.
- Lenguaje: Java 17.
- Persistencia: MySQL.
- Pruebas: JUnit 5, Mockito y Jakarta Bean Validation mediante `spring-boot-starter-test`.
- Ejecución: `mvn clean test`.

## 3. Pruebas automatizadas presentes en el proyecto

### 3.1 Validación de DetallePedidoRequest
Archivo: `src/test/java/com/restaurante/pedidos/dto/DetallePedidoRequestValidationTest.java`.

| ID | Escenario | Resultado esperado |
|---|---|---|
| VAL-01 | Cantidad positiva y producto informado | El DTO no presenta errores de validación. |
| VAL-02 | Cantidad igual a cero | Se detecta error en el campo cantidad. |
| VAL-03 | Cantidad negativa | Se detecta error en el campo cantidad. |
| VAL-04 | Producto sin informar | Se detecta error en productoId. |

### 3.2 Servicio de pedidos
Archivo: `src/test/java/com/restaurante/pedidos/servicio/PedidoSerTest.java`.

| ID | Escenario | Resultado esperado |
|---|---|---|
| PED-01 | Crear pedido con dos productos y cantidades | Se guardan los detalles y se calcula total 89.50 para los datos de prueba. |
| PED-02 | Crear pedido con cliente inexistente | Se lanza `Cliente no encontrado` y no se guarda. |
| PED-03 | Crear pedido con producto inexistente | Se informa que el producto no existe y no se guarda. |
| PED-04 | Crear pedido con producto no disponible | Se rechaza la operación y no se guarda. |
| PED-05 | Cambiar estado de pedido existente | Se persiste el estado solicitado (LISTO en el caso de prueba). |
| PED-06 | Consultar pedido inexistente | Se informa `Pedido no encontrado`. |

## 4. Pruebas manuales funcionales propuestas
Ejecutar con la aplicación levantada y MySQL disponible. Registrar el resultado real y adjuntar capturas en la entrega.

| ID | Módulo | Pasos | Resultado esperado |
|---|---|---|---|
| MAN-01 | Login | Abrir `/login.html` e ingresar credenciales válidas | Acceso al panel administrativo. |
| MAN-02 | Login | Ingresar contraseña incorrecta | No se concede acceso y se muestra error. |
| MAN-03 | Sesión | Cerrar sesión y volver a `/admin.html` | El panel queda protegido y solicita autenticación. |
| MAN-04 | Productos | Crear, editar y eliminar un producto | Los cambios se reflejan en el listado y base de datos. |
| MAN-05 | Categorías | Registrar y modificar una categoría | Los datos se muestran correctamente. |
| MAN-06 | Clientes | Registrar un cliente y consultar el listado | El cliente aparece con sus datos. |
| MAN-07 | Pedidos | Registrar pedido con cliente y productos válidos | El pedido se crea con total calculado. |
| MAN-08 | Pedidos | Cambiar estado de un pedido | El nuevo estado se visualiza al actualizar. |
| MAN-09 | Responsive | Abrir el panel en ancho móvil | Navegación y formularios se adaptan sin desbordamiento horizontal. |
| MAN-10 | Navegación | Recorrer Resumen, Pedidos, Productos, Categorías y Clientes | Cada sección se carga y conserva sus acciones. |

## 5. Cómo ejecutar las pruebas automatizadas
Desde la carpeta que contiene `pom.xml`:

```bash
mvn clean test
```

En Windows PowerShell también se puede ejecutar el mismo comando desde la terminal de VS Code. Si el proyecto incorpora Maven Wrapper, usar `./mvnw clean test` o `mvnw.cmd clean test`.

Los reportes de JUnit quedan en `target/surefire-reports/`. Revisar los archivos `.txt` y `.xml` para conocer pruebas ejecutadas, fallidas y errores.

## 6. Integración continua
El workflow `.github/workflows/ci.yml` se ejecuta en cada push y pull request hacia `main` o `master`. Configura Java 17, ejecuta `mvn clean test` y construye el JAR. Si una prueba falla, el job queda fallido y no debe integrarse ese cambio.

## 7. Plantilla de evidencia y resultados
Completar esta tabla después de ejecutar los casos manuales; no registrar como aprobada una prueba que no se haya ejecutado.

| ID | Fecha | Resultado (Aprobada/Fallida/Pendiente) | Evidencia / observación |
|---|---|---|---|
| MAN-01 | | Pendiente | |
| MAN-02 | | Pendiente | |
| MAN-03 | | Pendiente | |
| MAN-04 | | Pendiente | |
| MAN-05 | | Pendiente | |
| MAN-06 | | Pendiente | |
| MAN-07 | | Pendiente | |
| MAN-08 | | Pendiente | |
| MAN-09 | | Pendiente | |
| MAN-10 | | Pendiente | |

## 8. Criterio de aceptación
- Todas las pruebas automatizadas deben finalizar correctamente.
- Las pruebas manuales críticas de autenticación, sesión, CRUD y pedidos deben aprobarse.
- No debe publicarse una versión si el workflow de CI falla.
