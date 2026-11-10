# Casa Sabor — Sistema de pedidos para restaurante

Aplicación web académica para consultar el menú, realizar pedidos para recoger y administrar la operación básica de un restaurante.

## Funcionalidades

### Sitio público
- Visualización de productos agrupados por categorías.
- Consulta de disponibilidad y precios.
- Registro de pedidos para recoger.
- Consulta del estado del pedido mediante su identificador, según las pantallas implementadas.

### Panel administrativo
- Inicio de sesión de administrador.
- Gestión de categorías, productos y clientes (crear, consultar, actualizar y eliminar).
- Consulta de pedidos y actualización de su estado.
- Panel accesible desde `/admin.html` después de iniciar sesión.

## Tecnologías

- Java 17.
- Spring Boot 3.5.4, Spring Web, Spring Data JPA, Spring Security y Bean Validation.
- MySQL 8.4 / MySQL compatible.
- Maven.
- HTML, CSS y JavaScript.
- Docker y Docker Compose.
- GitHub Actions para pruebas y publicación de imagen.

## Requisitos para ejecutar localmente

- JDK 17.
- Maven 3.9 o superior (o Maven Wrapper si se incorpora).
- MySQL 8.x, o Docker Desktop con Docker Compose.

## Ejecución con Maven

1. Clona el repositorio:
   ```bash
   git clone https://github.com/DUP2004/restaurante-pedidos.git
   cd restaurante-pedidos
   ```
2. Crea la base de datos `restaurante_pedidos` en MySQL. La cuenta local predeterminada configurada es `root` con contraseña `admin`; puedes cambiarla mediante variables de entorno.
3. Ejecuta:
   ```bash
   mvn clean spring-boot:run
   ```
4. Abre `http://localhost:8080`.

La aplicación inicializa/actualiza las tablas mediante JPA y carga datos de ejemplo desde `src/main/resources/data.sql` cuando las tablas de categorías y productos están vacías.

## Ejecución con Docker Compose

Desde la raíz del proyecto:

```bash
docker compose up --build
```

- Sitio: `http://localhost:8080`
- MySQL: puerto `3306`
- Detener: `docker compose down`
- Detener y eliminar también los datos persistidos: `docker compose down -v` (acción destructiva).

Puedes definir `MYSQL_ROOT_PASSWORD`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `ADMIN_USERNAME` y `ADMIN_PASSWORD` en un archivo `.env` local. No subas ese archivo al repositorio.

## Acceso administrativo en desarrollo

- URL: `http://localhost:8080/login.html`
- Usuario predeterminado local: `admin`
- Contraseña predeterminada local: `admin123`

Estas credenciales son exclusivamente para desarrollo. Antes de exponer la aplicación a Internet, define `ADMIN_USERNAME` y una contraseña larga y única mediante variables seguras del proveedor de alojamiento.

## Variables de entorno

| Variable | Uso | Valor local predeterminado |
|---|---|---|
| `PORT` | Puerto HTTP de la aplicación | `8080` |
| `SPRING_DATASOURCE_URL` | URL JDBC de MySQL | MySQL local / Docker |
| `SPRING_DATASOURCE_USERNAME` | Usuario de base de datos | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de base de datos | `admin` |
| `ADMIN_USERNAME` | Usuario del panel | `admin` |
| `ADMIN_PASSWORD` | Contraseña del panel | `admin123` |
| `DDL_AUTO` | Estrategia de esquema JPA | `update` |
| `SHOW_SQL` | Mostrar SQL en consola | `false` |

Los valores predeterminados son para pruebas locales. No reutilices contraseñas de ejemplo en un entorno público.

## Pruebas

```bash
mvn clean test
```

Los reportes de pruebas se generan en `target/surefire-reports/`. La integración continua de GitHub Actions ejecuta pruebas y empaquetado al hacer push o abrir un pull request hacia `main` o `master`.

## Estructura

```text
src/main/java/com/restaurante/pedidos/
  ConfiguracionSeguridad.java
  controladores/       # Controladores REST
  dto/                 # Objetos de entrada y validación
  entidades/           # Entidades JPA
  repositorio/         # Repositorios Spring Data
  servicio/            # Lógica de negocio
src/main/resources/
  application.properties
  data.sql
  static/              # Sitio público y panel administrativo
src/test/java/         # Pruebas automatizadas
docs/                  # Documentación técnica y de operación
.github/workflows/     # CI y publicación de imagen Docker
Dockerfile
docker-compose.yml
pom.xml
```

## Documentación adicional

- [Arquitectura y componentes](docs/ARQUITECTURA.md)
- [Referencia de API REST](docs/API_REST.md)
- [Manual de usuario](docs/MANUAL_USUARIO.md)
- [Documentación de pruebas](docs/DOCUMENTACION_PRUEBAS.md)
- [Guía de despliegue](docs/GUIA_DESPLIEGUE_GITHUB.md)
- [Guía de GitHub Desktop](docs/GUIA_GITHUB_DESKTOP.md)

## Consideraciones de seguridad

La configuración actual utiliza autenticación de sesión para el panel y protege las rutas administrativas de la API. La creación pública de pedidos se permite para el flujo de clientes. La protección CSRF está desactivada en la configuración actual para compatibilidad con los formularios JavaScript; antes de un uso comercial o con datos reales debe revisarse y habilitarse correctamente, además de configurar HTTPS, credenciales seguras, copias de seguridad y permisos mínimos de base de datos.

## Estado del proyecto

Proyecto académico demostrativo. La publicación de una imagen en GitHub Container Registry no equivale a tener una instancia web desplegada ni una base de datos administrada configurada. Consulta la guía de despliegue y completa las variables del proveedor antes de compartir una URL pública.
