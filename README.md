# Sistema de Pedidos para Restaurante

Proyecto académico para el curso Herramientas de Desarrollo.

## Tecnologías
- Java 17, Spring Boot 3.5, Spring Data JPA
- MySQL
- Maven
- HTML, CSS y JavaScript (sin frameworks) servidos por Spring Boot
- Git/GitHub y Docker

## Qué incluye
- **Sitio público** (`/`): carta por categorías, pedido en línea para recoger, seguimiento del pedido por número.
- **Panel del restaurante** (`/admin.html`): CRUD de categorías, productos y clientes; lista de pedidos con cambio de estado.
- **API REST** (`/api/...`): la usan tanto el sitio como el panel.

## Ejecutar
1. Tener MySQL encendido en el puerto 3306 con usuario `root` y contraseña `admin`
   (o `docker compose up -d mysql`). La base de datos se crea sola.
2. Abrir esta carpeta en VS Code y ejecutar `RestaurantePedidosApplication.java` (botón ▶),
   o en la terminal: `mvn spring-boot:run`.
3. Abrir `http://localhost:8080`.

La primera vez se cargan platos de ejemplo (`src/main/resources/data.sql`) solo si las tablas están vacías.

## Personalizar el sitio
Nombre, textos, teléfono, dirección y horarios están en `src/main/resources/static/js/config.js`.

## Estructura del frontend (`src/main/resources/static`)
| Archivo | Para qué sirve |
|---|---|
| `index.html`, `css/sitio.css`, `js/sitio.js` | Sitio público |
| `admin.html`, `css/estilos.css`, `js/app.js` y demás | Panel del restaurante |
| `interfaces/*.html` | Pantallas del panel (se cargan dentro de `admin.html`) |
| `js/config.js` | Datos del restaurante |

## Estados del pedido
`PENDIENTE` → `PREPARANDO` → `LISTO` → `ENTREGADO` (o `CANCELADO`).
Se cambian desde el panel: `PUT /api/pedidos/{id}/estado?estado=PREPARANDO`.

## Docker
```bash
mvn -q package -DskipTests   # opcional: el Dockerfile ya compila solo
docker compose up --build
```

## Pruebas automatizadas

El proyecto incluye pruebas unitarias en `src/test/java` para el servicio de pedidos y la validación de cantidades:
- pedido con varios productos y cálculo del total;
- cliente o producto inexistente;
- producto agotado;
- cambio y consulta de estado;
- cantidad cero, negativa o producto sin identificador.

Ejecutar desde la carpeta que contiene `pom.xml`:
```bash
mvn test
```
Las pruebas unitarias no sustituyen pruebas de carga/concurrencia ni pruebas manuales del navegador. Consultar el informe de revisión del código para la matriz de casos recomendados.
