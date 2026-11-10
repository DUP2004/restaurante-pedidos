# Casa Sabor

Sistema web académico para la gestión de pedidos de un restaurante. Permite a los clientes consultar el menú y realizar pedidos para recoger, mientras que el administrador gestiona productos, categorías, clientes y pedidos.

## Funcionalidades principales

- Consulta pública del menú, categorías, precios y disponibilidad.
- Registro y seguimiento de pedidos.
- Inicio de sesión para el administrador.
- Gestión de productos, categorías y clientes.
- Consulta y actualización del estado de los pedidos.

## Tecnologías

- Java 17 y Spring Boot 3.
- Spring Data JPA y Spring Security.
- MySQL.
- HTML, CSS y JavaScript.
- Maven y Docker.

## Requisitos

- JDK 17.
- Maven 3.9 o superior.
- MySQL 8.x, o Docker Desktop con Docker Compose.

## Ejecución local

1. Clona el repositorio:
   ```bash
   git clone https://github.com/DUP2004/restaurante-pedidos.git
   cd restaurante-pedidos
   ```
2. Configura la conexión a MySQL y las credenciales del administrador mediante variables de entorno.
3. Inicia la aplicación:
   ```bash
   mvn spring-boot:run
   ```
4. Abre http://localhost:8080.

También puedes iniciar el proyecto con Docker Compose:

```bash
docker compose up --build
```

No publiques contraseñas ni archivos `.env` en el repositorio.

## Pruebas

```bash
mvn clean test
```

## Documentación

La documentación técnica y las guías del proyecto se encuentran en la carpeta [docs](docs/):

- [Arquitectura](docs/ARQUITECTURA.md)
- [API REST](docs/API_REST.md)
- [Manual de usuario](docs/MANUAL_USUARIO.md)
- [Documentación de pruebas](docs/DOCUMENTACION_PRUEBAS.md)
- [Flujo público de pedidos y privacidad](docs/FLUJO_PUBLICO_PEDIDOS.md)
- [Guía de despliegue](docs/GUIA_DESPLIEGUE_GITHUB.md)

## Demostración

https://restaurante-pedidos-o7e5.onrender.com

> Proyecto académico demostrativo. Antes de utilizarlo en un entorno real, se deben completar las revisiones de seguridad, configuración y pruebas correspondientes.
