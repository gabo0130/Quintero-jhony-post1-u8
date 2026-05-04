# Quintero-jhony-post1-u8

Proyecto de la unidad 8 para implementar un módulo de gestión de pedidos con arquitectura limpia (Clean Architecture) usando Spring Boot, Spring Data JPA y H2.

## Arquitectura implementada

La solución refleja los cuatro círculos de Clean Architecture con dependencias apuntando siempre hacia adentro (hacia domain):

```text
com.example.cleanpedidos
├── domain                    # Círculo más interno: lógica pura sin dependencias
│   ├── entity
│   │   └── Pedido           # Aggregate Root inmutable
│   └── valueobject
│       ├── PedidoId         # Value Object con identidad tipada
│       ├── LineaPedido      # Value Object inmutable
│       ├── Dinero           # Value Object de dominio
│       └── EstadoPedido     # Enum de dominio
├── usecase                   # Círculo Use Cases: orquestación sin Spring
│   ├── CrearPedidoUseCase   # Interfaz del caso de uso
│   ├── ConsultarPedidoUseCase
│   ├── port
│   │   └── PedidoRepositoryPort  # Puerto de salida (abstracción)
│   └── impl
│       ├── CrearPedidoService
│       └── ConsultarPedidoService
├── adapter                   # Círculo Interface Adapters: Spring aquí
│   ├── in/web
│   │   ├── PedidoController
│   │   └── dto
│   │       ├── CrearPedidoRequest
│   │       └── PedidoResponse
│   └── out/persistence
│       ├── PedidoJpaEntity          # Entidad JPA solo aquí
│       ├── PedidoJpaRepository
│       └── PedidoRepositoryAdapter # Implementación del puerto
├── config
│   └── PedidoConfiguration  # Wiring explícito de beans
└── CleanPedidosApplication
```

### Responsabilidades por capa

- `domain/entity`: Agregado `Pedido` inmutable, sin anotaciones Spring ni JPA.
- `domain/valueobject`: Value Objects (`PedidoId`, `LineaPedido`, `Dinero`, `EstadoPedido`) sin dependencias externas.
- `usecase/`: Interfaces que definen los casos de uso sin acoplamiento a Spring.
- `usecase/impl`: Servicios que implementan la lógica de negocio orquestada.
- `usecase/port`: Abstracción del repositorio (puerto de salida).
- `adapter/in/web`: Controlador REST, DTOs y mapeo desde/hacia domain.
- `adapter/out/persistence`: Entidad JPA, repositorio Spring Data y adaptador que implementa el puerto.
- `config`: Configuración explícita de beans para inyectar dependencias.

## Requisitos

- Java 17+
- Maven 3.8+

## Cómo ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

La consola H2 queda disponible en `http://localhost:8080/h2-console` con:

- JDBC URL: `jdbc:h2:mem:pedidosdb`
- User: `sa`
- Password: vacío

## Endpoints

### Crear pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteNombre": "Ana García",
    "lineas": [
      {
        "productoNombre": "Laptop",
        "cantidad": 1,
        "precioUnitario": 1500.00
      },
      {
        "productoNombre": "Mouse inalámbrico",
        "cantidad": 2,
        "precioUnitario": 45.50
      }
    ]
  }'
```

**Respuesta (201 Created):**
```json
{
  "pedidoId": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
}
```

### Buscar pedido por id

```bash
curl -X GET http://localhost:8080/api/pedidos/f47ac10b-58cc-4372-a567-0e02b2c3d479
```

**Respuesta (200 OK):**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "clienteNombre": "Ana García",
  "estado": "CONFIRMADO",
  "total": 1591.00,
  "lineas": [
    {
      "productoNombre": "Laptop",
      "cantidad": 1,
      "precioUnitario": 1500.00,
      "subtotal": 1500.00
    },
    {
      "productoNombre": "Mouse inalámbrico",
      "cantidad": 2,
      "precioUnitario": 45.50,
      "subtotal": 91.00
    }
  ]
}
```

### Listar todos los pedidos

```bash
curl -X GET http://localhost:8080/api/pedidos
```

## Aspectos clave de Clean Architecture

### Paso 2 — Círculo Entities: Value Objects y Aggregate Root

- **`PedidoId`**: Record con UUID (identidad tipada) — no se puede construir con Long inválidos
- **`Dinero`**: Record con BigDecimal, incluye método `sumar()` y constante `CERO`
- **`LineaPedido`**: Record inmutable con `productoNombre`, `cantidad`, `precioUnitario` — validación en constructor
- **`Pedido`**: Agregado mutable que controla su propio estado
  - Inicia en estado **BORRADOR** con lista de líneas vacía
  - Método `agregarLinea()` valida estado y cantidad
  - Método `confirmar()` solo funciona con líneas presentes

### Paso 3 — Círculo Use Cases

- **`CrearPedidoUseCase`**: Interfaz que retorna `PedidoId` (no Pedido completo)
- **`CrearPedidoService`**: Orquesta la creación: genera UUID, agrega líneas, confirma, guarda
- **`ConsultarPedidoUseCase`**: Retorna `PedidoResponse` (DTO de consulta, no entidad)
- **`ConsultarPedidoService`**: Mapea Pedido → PedidoResponse

### Paso 4 — Interface Adapters

- **`PedidoController`**: Traduce HTTP → casos de uso
  - POST retorna `Map<String, String>` con pedidoId
  - GET retorna `PedidoResponse` (con estado calculado, total, líneas con subtotales)
- **`PedidoJpaEntity`**: Solo en adapter, almacena UUID como String (VARCHAR 36)
- **`PedidoRepositoryAdapter`**: Mapea bidireccional entre Pedido ↔ PedidoJpaEntity

## Verificación de arquitectura

✅ El proyecto compila con `mvn clean compile` sin errores  
✅ El paquete `domain/` no contiene imports de `org.springframework` ni `jakarta.persistence`  
✅ El paquete `usecase/` no contiene imports de `org.springframework`  
✅ Los tests pasan con `mvn test`  
✅ POST /api/pedidos retorna 201 Created con `pedidoId` en formato UUID  
✅ GET /api/pedidos/{id} retorna el pedido con estado CONFIRMADO y total calculado  
✅ POST con `clienteNombre` vacío retorna 400 con validación  
✅ POST con `cantidad` ≤ 0 retorna 400 con validación de línea  
✅ La clase `Pedido` puede instanciarse sin @SpringBootTest  
✅ Los servicios pueden testearse con un mock de `PedidoRepositoryPort`

