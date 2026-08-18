# Sistema de Pedidos para Restaurante

Proyecto académico para el curso Herramientas de Desarrollo.

## Tecnologías
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Git/GitHub
- Docker

## Funcionalidades
- CRUD de categorías
- CRUD de productos
- CRUD de clientes
- Creación y consulta de pedidos
- Cambio de estado del pedido
- Cálculo automático del total

## Base de datos
Crear:

```sql
CREATE DATABASE restaurante_pedidos;
```

Luego configurar la contraseña de MySQL en `application.properties`.

## Ejecutar
```bash
mvn spring-boot:run
```

La API estará disponible en:
`http://localhost:8080`

## Crear pedido de ejemplo

```json
{
  "clienteId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    },
    {
      "productoId": 2,
      "cantidad": 1
    }
  ]
}
```

## Cambiar estado
```text
PUT /api/pedidos/1/estado?estado=PREPARANDO
```
