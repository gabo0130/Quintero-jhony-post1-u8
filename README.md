# Quintero-jhony-post2-u7

Proyecto de la unidad 7 para implementar un modulo de gestion de productos con arquitectura hexagonal (Ports & Adapters) usando Spring Boot, Spring Data JPA y H2.

## Arquitectura implementada

La solucion separa el dominio de la infraestructura:

```text
com.example.hexagonal
├── domain
│   ├── exception
│   ├── model
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
├── adapter
│   ├── in
│   │   └── web
│   └── out
│       └── persistence
├── config
└── HexagonalApplication
```

### Responsabilidades por capa

- `domain/model`: entidad de dominio `Producto` sin anotaciones Spring ni JPA.
- `domain/port/in`: puertos de entrada que representan los casos de uso.
- `domain/port/out`: puerto de salida para persistencia.
- `domain/service`: implementacion pura de los casos de uso.
- `adapter/in/web`: controlador REST y manejo de errores HTTP.
- `adapter/out/persistence`: entidad JPA, repositorio Spring Data y adaptador de salida.
- `config`: configuracion explicita de beans para conectar dominio e infraestructura.

## Requisitos

- Java 17+
- Maven 3.8+

## Como ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

La consola H2 queda disponible en `http://localhost:8080/h2-console` con:

- JDBC URL: `jdbc:h2:mem:productosdb`
- User: `sa`
- Password: vacio

## Endpoints

### Listar productos

```bash
curl -X GET http://localhost:8080/api/productos
```

### Buscar producto por id

```bash
curl -X GET http://localhost:8080/api/productos/1
```

### Crear producto

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Mouse\",\"descripcion\":\"Mouse inalambrico\",\"precio\":85.50,\"stock\":15}"
```

### Reducir stock

```bash
curl -X PATCH "http://localhost:8080/api/productos/1/stock?cantidad=3"
```

