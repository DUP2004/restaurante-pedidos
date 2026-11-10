# Guía de despliegue — Casa Sabor

## 1. Arquitectura desplegada

- Aplicación: Java 17 y Spring Boot 3.5.4.
- Empaquetado: Dockerfile multi-stage.
- Hosting web: Render Web Service (Docker).
- Base de datos: TiDB Cloud Starter, compatible con el protocolo MySQL.
- Repositorio: https://github.com/DUP2004/restaurante-pedidos
- URL de demostración actualmente comunicada: https://restaurante-pedidos-o7e5.onrender.com

El servicio de demostración ya inició con Render y los logs compartidos mostraron una conexión Hikari exitosa a una base MySQL compatible. Esto no sustituye las pruebas funcionales completas ni una prueba de restauración de backups.

## 2. Variables de entorno en Render

En el servicio de Render, abrir **Environment** y configurar:

| Key | Valor |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://HOST:4000/test?sslMode=VERIFY_IDENTITY` (sustituir HOST por el host de TiDB de la instancia) |
| `SPRING_DATASOURCE_USERNAME` | Usuario mostrado por TiDB Connect |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña generada en TiDB |
| `ADMIN_USERNAME` | Usuario administrativo elegido |
| `ADMIN_PASSWORD` | Contraseña larga, única y distinta a la de TiDB |
| `DDL_AUTO` | `update` para la demostración actual |
| `SHOW_SQL` | `false` |

Render asigna la variable `PORT`; la aplicación la utiliza mediante `${PORT:8080}`. No es necesario fijar un puerto manual si Render ya lo proporciona.

No guardar contraseñas, certificados privados, tokens ni archivos `.env` en GitHub. No compartir capturas donde sean visibles. Si una contraseña se expone, rotarla inmediatamente.

### TLS / certificado CA

TiDB exige una conexión cifrada. La URL de ejemplo usa `sslMode=VERIFY_IDENTITY`. Seguir las instrucciones actuales de **Connect** de la instancia para descargar/configurar el certificado CA cuando el driver o el entorno lo requieran. No escribir literalmente `<CA_PATH>` en la URL. Si Java informa que no puede construir la cadena de confianza, instalar el CA indicado por TiDB en el truststore de Java o seguir la cadena JDBC oficial de TiDB; no degradar TLS a texto plano para resolverlo.

## 3. Crear o actualizar el servicio Render

1. Crear **New > Web Service** y conectar el repositorio GitHub.
2. Elegir runtime Docker y Dockerfile `./Dockerfile`.
3. Configurar las variables del apartado anterior.
4. Guardar cambios y desplegar la rama deseada.
5. Revisar **Logs**. Deben aparecer el inicio de Spring Boot y `HikariPool - Start completed`.
6. Abrir la URL `.onrender.com` y ejecutar las pruebas manuales de `DOCUMENTACION_PRUEBAS.md`.

El mensaje `Live` indica que Render detectó el servicio disponible; no demuestra por sí solo que todas las funciones de negocio hayan sido probadas.

## 4. Comprobaciones posteriores al despliegue

- Inicio público y carga de carta.
- Pedido nuevo sin iniciar sesión.
- Recepción del código privado de seguimiento.
- Consulta de estado con ese código.
- Inicio y cierre de sesión administrativo.
- CRUD de productos/categorías/clientes desde el panel.
- Cambio de estado de pedido.
- Reinicio del servicio y comprobación de persistencia en TiDB.
- Revisión de logs sin contraseñas ni datos personales.

## 5. Límites de los planes gratuitos

Los servicios gratuitos de Render pueden entrar en suspensión tras inactividad y tardar en reactivarse. TiDB Starter tiene cuotas y condiciones propias que pueden cambiar. Verificar límites vigentes, suspensión, almacenamiento, conexiones, backups y políticas de uso directamente en los paneles oficiales antes de depender del servicio.

Este despliegue se considera una demostración académica. No se recomienda para procesar pedidos comerciales críticos sin disponibilidad, monitorización, copias de seguridad probadas, gestión de secretos, política de privacidad y endurecimiento de seguridad.

## 6. GitHub Actions

- `.github/workflows/ci.yml`: ejecuta pruebas Maven y empaquetado en push/pull request.
- `.github/workflows/cd.yml`: prueba y publica una imagen en GHCR.
- La publicación en GHCR no despliega automáticamente Render, salvo que se configure expresamente una integración adicional.

Revisar la pestaña **Actions** después de cada cambio. No considerar una modificación lista para producción hasta que CI termine correctamente.

## 7. Seguridad y tareas pendientes antes de uso real

1. La interfaz administrativa aún requiere integrar token CSRF en sus peticiones mutables; actualmente CSRF está desactivado para compatibilidad con el frontend heredado.
2. La cuenta administrativa se almacena en memoria y se configura mediante variables de entorno; no existe administración de usuarios ni recuperación de contraseña.
3. Usar una contraseña administrativa única; no conservar el valor de desarrollo `admin/admin123`.
4. Revisar autorización por rol y límites de intentos de login.
5. Aplicar rate limiting al endpoint público de pedidos y seguimiento.
6. Establecer política de privacidad y retención para datos de clientes.
7. Implementar backups automáticos y probar restauración.
8. Migrar de `ddl-auto=update` a Flyway/Liquibase antes de cambios de esquema importantes.
9. Revisar el código de seguimiento como dato confidencial: quien lo posea puede consultar el estado y los detalles básicos de ese pedido.
10. Añadir monitoreo, alertas y un plan de recuperación.
