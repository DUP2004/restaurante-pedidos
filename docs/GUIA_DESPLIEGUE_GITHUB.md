# Guía de publicación y despliegue

## 1. Qué está automatizado actualmente

El repositorio utiliza dos workflows:

- `.github/workflows/ci.yml`: ejecuta `mvn clean test` y empaqueta la aplicación en cada push o pull request hacia `main` o `master`.
- `.github/workflows/cd.yml`: vuelve a ejecutar las pruebas y publica una imagen Docker en GitHub Container Registry (GHCR) al hacer push a `main` o publicar una etiqueta `v*`.

La publicación de la imagen en GHCR no significa que exista una página web desplegada. El workflow CD actual no crea un servicio en Render ni configura una base de datos externa.

## 2. Preparar variables de producción

La aplicación lee estas variables:

| Variable | Descripción |
|---|---|
| `PORT` | Puerto HTTP que asigna el proveedor |
| `SPRING_DATASOURCE_URL` | URL JDBC de la base MySQL compatible |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos |
| `ADMIN_USERNAME` | Usuario del panel |
| `ADMIN_PASSWORD` | Contraseña fuerte del panel |
| `DDL_AUTO` | Estrategia JPA; revisar antes de producción |
| `SHOW_SQL` | Activar/desactivar SQL en logs |

No guardes secretos en GitHub, Dockerfile, README ni archivos versionados. En Render, configura estos valores en la sección Environment del servicio.

## 3. Despliegue de demostración con Render

La aplicación incluye Dockerfile, por lo que se puede crear un **Web Service** desde el repositorio:

1. Crear una cuenta en Render y autorizar el acceso al repositorio GitHub.
2. Seleccionar **New > Web Service** y conectar `DUP2004/restaurante-pedidos`.
3. Elegir despliegue desde Dockerfile (ruta `./Dockerfile`).
4. Configurar las variables anteriores con credenciales reales y una URL JDBC válida de un proveedor MySQL compatible.
5. Iniciar el despliegue y revisar los logs hasta que Spring Boot indique que inició correctamente.
6. Probar la URL pública, `/login.html`, la consulta de menú y un pedido de prueba.

**Base de datos:** Render ofrece PostgreSQL administrado, no un servicio MySQL administrado equivalente. Como este proyecto usa el driver MySQL, para mantenerlo sin migración se necesita un proveedor externo compatible con MySQL y conectividad desde Render. Comprueba sus límites, TLS, lista de IP permitidas y política de suspensión antes de elegirlo. Alternativamente, migra el proyecto a PostgreSQL y adapta el driver/configuración.

**Límite del plan gratuito de Render:** los Web Services gratuitos se suspenden después de 15 minutos sin tráfico entrante y pueden tardar alrededor de un minuto en reactivarse. El sistema de archivos local es efímero. Por ello, este plan sirve para una demostración académica, no para operación continua ni para pedidos reales con garantía de disponibilidad. Consulta [documentación oficial de Render](https://render.com/docs/free).

## 4. Publicar imagen Docker en GHCR

El workflow CD utiliza `GITHUB_TOKEN`, sin necesidad de crear un token personal:

1. Hacer push a `main` o publicar una etiqueta `v*`.
2. Abrir la pestaña **Actions** y revisar el workflow **Entrega continua - imagen Docker**.
3. Si termina correctamente, consultar el paquete en GitHub Packages del propietario.
4. La imagen se etiqueta como `latest` y con el SHA del commit.

Puede ser necesario ajustar la visibilidad del paquete según quién vaya a descargarlo.

## 5. Despliegue en VPS por SSH (no configurado todavía)

Para un servidor propio se requiere una VPS Linux con Docker y Docker Compose, acceso SSH y un compose de producción. Se podrían configurar estos secretos en **Settings > Secrets and variables > Actions**:

- `DEPLOY_HOST`: IP o dominio del servidor.
- `DEPLOY_USER`: usuario SSH.
- `DEPLOY_SSH_KEY`: clave privada SSH.
- `DEPLOY_PATH`: directorio absoluto de despliegue.

La clave pública debe estar autorizada en el servidor. El workflow actual **no consume estos secretos ni realiza despliegue SSH**; solo publica la imagen. No agregues secretos hasta que exista un servidor y se implemente ese paso.

## 6. Lista de verificación antes de compartir una demo

- [ ] Base de datos externa creada y accesible.
- [ ] Variables JDBC configuradas correctamente.
- [ ] Contraseña administrativa única y segura.
- [ ] Servicio desplegado y logs sin errores.
- [ ] Inicio de sesión y cierre de sesión comprobados.
- [ ] Menú público y creación de pedido probados.
- [ ] Panel administrativo protegido.
- [ ] Copia de seguridad y restauración de base de datos consideradas.
- [ ] CSRF, CORS y HTTPS revisados antes de usar datos reales.

## 7. Seguridad

La configuración actual desactiva CSRF para compatibilidad con los formularios JavaScript del prototipo. No debe considerarse una configuración final para producción. Antes de procesar información real, habilita CSRF e integra el token en las solicitudes, restringe CORS, revisa autorización y usa un usuario MySQL con privilegios mínimos. Para un despliegue serio, reemplaza `ddl-auto=update` por migraciones versionadas y una estrategia controlada de cambios de esquema.
