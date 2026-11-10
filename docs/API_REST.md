# Referencia de API REST

La API utiliza JSON y tiene como prefijo `/api`. Los ejemplos son orientativos y deben ejecutarse con la aplicación levantada. Las rutas administrativas requieren iniciar sesión, salvo las rutas públicas indicadas.

## Categorías — `/api/categorias`

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/categorias` | Listar categorías | Público |
| GET | `/api/categorias/{id}` | Consultar categoría | Público |
| POST | `/api/categorias` | Crear categoría | Administrador |
| PUT | `/api/categorias/{id}` | Actualizar categoría | Administrador |
| DELETE | `/api/categorias/{id}` | Eliminar categoría | Administrador |

## Productos — `/api/productos`

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/productos` | Listar productos | Público |
| GET | `/api/productos/{id}` | Consultar producto | Público |
| POST | `/api/productos` | Crear producto | Administrador |
| PUT | `/api/productos/{id}` | Actualizar producto | Administrador |
| DELETE | `/api/productos/{id}` | Eliminar producto | Administrador |

## Clientes — `/api/clientes`

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/clientes` | Listar clientes | Administrador |
| GET | `/api/clientes/{id}` | Consultar cliente | Administrador |
| POST | `/api/clientes` | Crear cliente | Administrador |
| PUT | `/api/clientes/{id}` | Actualizar cliente | Administrador |
| DELETE | `/api/clientes/{id}` | Eliminar cliente | Administrador |

## Pedidos — `/api/pedidos`

| Método | Ruta | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/pedidos` | Listar pedidos | Administrador |
| GET | `/api/pedidos/{id}` | Consultar pedido | Administrador |
| POST | `/api/pedidos` | Crear pedido con cliente existente (uso administrativo/interno) | Administrador |
| POST | `/api/pedidos/publicos` | Crear pedido desde el sitio público con datos del cliente | Público |
| GET | `/api/pedidos/seguimiento/{codigo}` | Consultar estado mediante UUID privado | Público |
| PUT | `/api/pedidos/{id}/estado?estado=LISTO` | Cambiar estado | Administrador |
| DELETE | `/api/pedidos/{id}` | Eliminar pedido | Administrador |

Los valores válidos del estado son `PENDIENTE`, `PREPARANDO`, `LISTO`, `ENTREGADO` y `CANCELADO`. El endpoint público devuelve un DTO limitado y no expone datos personales del cliente. El código UUID de seguimiento debe mantenerse privado.

## Ejemplo: crear un pedido

Solicitud:

```http
POST /api/pedidos
Content-Type: application/json
```

```json
{
  "clienteId": 1,
  "detalles": [
    { "productoId": 1, "cantidad": 2 },
    { "productoId": 4, "cantidad": 1 }
  ]
}
```

La cantidad debe ser al menos 1 y cada detalle debe incluir `productoId`. La lista de detalles no puede estar vacía. El servidor obtiene los precios desde la base de datos y calcula el total; no debe confiar en precios enviados por el navegador.

## Ejemplo: cambiar estado

```http
PUT /api/pedidos/12/estado?estado=PREPARANDO
```

## Autenticación

El inicio de sesión se realiza mediante formulario en `/login.html`. Spring Security establece una sesión. Para consumir endpoints administrativos desde herramientas externas, se debe mantener la cookie de sesión después del login.

## Respuestas y errores

Las operaciones de creación responden con HTTP 201; las eliminaciones correctas responden HTTP 204. Los errores de validación, identificadores inexistentes y restricciones de base de datos pueden producir respuestas de error HTTP. El proyecto aún no define un formato JSON uniforme para todos los errores; se recomienda incorporar un `@RestControllerAdvice` antes de integrar clientes externos.


## Ejemplo: crear pedido público

```http
POST /api/pedidos/publicos
Content-Type: application/json
```

```json
{
  "cliente": {
    "nombre": "Ana",
    "apellido": "Pérez",
    "correo": "ana@example.com",
    "telefono": "999123456"
  },
  "detalles": [
    { "productoId": 1, "cantidad": 2 }
  ]
}
```

La respuesta incluye `codigoSeguimiento`, `total`, `estado` y detalles con nombre/cantidad. Guardar el código: es la credencial necesaria para consultar el estado.

## Ejemplo: seguimiento público

```http
GET /api/pedidos/seguimiento/UUID-DEVUELTO-AL-CREAR
```

No devuelve nombre, correo ni teléfono del cliente. Las rutas administrativas `GET /api/pedidos`, `GET /api/pedidos/{id}`, y `/api/clientes/**` requieren sesión.
