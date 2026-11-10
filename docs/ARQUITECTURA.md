# Arquitectura del sistema Casa Sabor

## 1. Propósito

Casa Sabor es una aplicación web de pedidos para un restaurante. Separa la experiencia pública de clientes del panel de administración y utiliza una API REST común.

## 2. Vista general

```text
Navegador del cliente
  └── Sitio público (HTML/CSS/JavaScript)
        └── GET /api/productos y /api/categorias
        └── POST /api/pedidos

Navegador del administrador
  └── Login /login.html (Spring Security)
        └── Panel /admin.html y pantallas /interfaces
              └── API REST protegida /api/**

Aplicación Spring Boot
  ├── Controladores REST
  ├── Servicios (reglas de negocio)
  ├── Repositorios Spring Data JPA
  └── Entidades JPA
        └── MySQL
```

## 3. Tecnologías y responsabilidades

| Tecnología | Responsabilidad |
|---|---|
| Java 17 | Lenguaje de backend |
| Spring Boot 3.5.4 | Arranque y configuración de la aplicación |
| Spring Web | Endpoints HTTP y API REST |
| Spring Data JPA / Hibernate | Persistencia y mapeo objeto-relacional |
| MySQL | Almacenamiento de información |
| Spring Security | Autenticación del administrador y autorización de rutas |
| Jakarta Validation | Validación de datos recibidos |
| HTML, CSS y JavaScript | Interfaz pública y panel |
| Maven | Dependencias y compilación |
| Docker | Empaquetado y ejecución reproducible |

## 4. Organización por capas

- **controladores:** reciben solicitudes HTTP, validan entradas y delegan operaciones.
- **servicio:** implementa reglas del negocio, como verificar cliente/producto, disponibilidad y calcular el total del pedido.
- **repositorio:** accede a la base de datos por medio de Spring Data JPA.
- **entidades:** representan categorías, clientes, productos, pedidos y detalles.
- **dto:** define los datos de entrada para crear pedidos y validar cantidades.
- **static:** contiene HTML, CSS y JavaScript del sitio y del panel.

## 5. Modelo de datos conceptual

- Una categoría agrupa productos.
- Un cliente puede registrar varios pedidos.
- Un pedido pertenece a un cliente y contiene uno o más detalles.
- Cada detalle referencia un producto, cantidad y precio unitario registrado.
- El pedido conserva un total y un estado.

Estados implementados: `PENDIENTE`, `PREPARANDO`, `LISTO`, `ENTREGADO` y `CANCELADO`.

## 6. Flujo de creación de pedido

1. El cliente consulta productos y categorías disponibles.
2. El navegador envía `clienteId` y una lista de detalles a `POST /api/pedidos`.
3. El controlador valida el DTO.
4. El servicio verifica que exista el cliente, que existan los productos y que estén disponibles.
5. Se construyen los detalles, se calcula el total con `BigDecimal` y se guarda el pedido.
6. La respuesta devuelve el pedido creado.

## 7. Seguridad actual y pendientes

El panel y las rutas administrativas de `/api/**` requieren autenticación de sesión. Las consultas públicas de menú y la creación de pedidos están permitidas. La contraseña se codifica con BCrypt.

La configuración actual desactiva CSRF para que los formularios JavaScript existentes funcionen. Esto es una decisión de compatibilidad para el prototipo, no una recomendación para producción. Antes de procesar datos reales, habilitar CSRF con envío de token desde el frontend, revisar CORS (los controladores usan `@CrossOrigin`), restringir permisos y usar HTTPS.

La cuenta de administrador se mantiene en memoria; no hay gestión de usuarios administradores persistida en MySQL.

## 8. Despliegue

El proyecto contiene un Dockerfile multi-stage y Docker Compose para ejecución local con MySQL. GitHub Actions verifica el código y publica una imagen en GHCR. La publicación de la imagen no configura automáticamente un servicio web ni una base de datos externa. Ver [Guía de despliegue](GUIA_DESPLIEGUE_GITHUB.md).
