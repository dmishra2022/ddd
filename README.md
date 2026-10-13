# Order Management Enterprise Starter

A **master-service template** for building enterprise bounded contexts with:

- **Micronaut 4** on **Java 25** with **virtual threads** per request.
- **Domain-Driven Design** + **Hexagonal / Ports & Adapters** package layout, with the application layer kept 100% framework-agnostic.
- **Order Management** as the reference domain (place, fetch, cancel an order).
- **ArchUnit** rules that fail the build if anyone imports a framework type from the domain, calls a repository from a controller, or routes an inbound adapter directly into an outbound one.
- **Maven** multi-module layout. Six modules; the dependency graph itself encodes the architecture.

```
                 +------------------------+
                 |     order-bootstrap    |      <-- composition root
                 +-----------+------------+
                             |
          +------------------+------------------+
          |                                     |
 +--------v---------+                 +---------v-----------+
 | order-adapter-   |                 | order-adapter-      |
 |   rest           |                 |   persistence       |
 +--------+---------+                 +---------+-----------+
          \                                     /
           \      +---------------------+      /
            +--->| order-application   |<----+
                 +----------+----------+
                            |
                 +----------v----------+
                 |    order-domain     |
                 +---------------------+
```

| Module | Purpose |
|--------|---------|
| [`order-domain`](order-domain) | Aggregates, value objects, domain events, domain exceptions. JDK-only. |
| [`order-application`](order-application) | Use cases, inbound and outbound **ports**, commands. Depends only on `order-domain`. No DI annotations. |
| [`order-adapter-rest`](order-adapter-rest) | Inbound HTTP adapter (Micronaut HTTP + Serde + validation). |
| [`order-adapter-persistence`](order-adapter-persistence) | Outbound JDBC adapter (Micronaut Data JDBC + Flyway). |
| [`order-bootstrap`](order-bootstrap) | Runnable Micronaut app. Hosts the `@Factory` that wires the application layer and ships default `Clock` / event-publisher adapters. |
| [`architecture-tests`](architecture-tests) | ArchUnit rules enforced on `mvn verify`. |

## Quickstart

```bash
# Full build + tests + architecture rules
./mvnw verify         # or: mvn verify
```

Run the service locally with the H2 PostgreSQL-mode in-memory database:

```bash
mvn -pl order-bootstrap -am mn:run
# or build the jar and run it:
mvn -pl order-bootstrap -am package
java -jar order-bootstrap/target/order-bootstrap-*.jar
```

### Try the API

```bash
# Place an order
curl -sS -X POST http://localhost:8080/api/v1/orders \
  -H 'content-type: application/json' \
  -d '{"currencyCode":"USD","lines":[{"productId":"sku-1","quantity":2,"unitPrice":12.50}]}'

# Fetch it
curl -sS http://localhost:8080/api/v1/orders/<id>

# Cancel it
curl -sS -X POST http://localhost:8080/api/v1/orders/<id>/cancel \
  -H 'content-type: application/json' \
  -d '{"reason":"customer request"}'

# Health probe
curl -sS http://localhost:8080/health
```

## Virtual threads

Blocking request handling runs on virtual threads out of the box. Configured in
[`order-bootstrap/src/main/resources/application.yml`](order-bootstrap/src/main/resources/application.yml):

```yaml
micronaut:
  server:
    thread-selection: BLOCKING
  executors:
    io:
      virtual: true
    blocking:
      virtual: true
```

Experimental Netty **loom-carrier** mode is intentionally **not** enabled by default; teams that want it can set `micronaut.netty.event-loops.default.loom-carrier=true` after validating the known Java 25 reflection caveats.

## Architecture guard

The [`architecture-tests`](architecture-tests) module runs on every `mvn verify`:

- `HexagonalArchitectureTest` – ArchUnit `onionArchitecture()` across every module. Domain is the center, application depends only on domain, adapters never depend on each other.
- `DomainPurityTest` – domain classes may not import Micronaut, Netty, Jakarta, JDBC, Flyway, or anything from `application` / `adapter`.
- `ApplicationLayerTest` – application classes may not import `adapter.*`, must stay framework-agnostic; inbound ports named `*UseCase` must live under `application.port.in`.
- `AdapterIsolationTest` – inbound adapters never reach into outbound adapters; `@Controller` classes may only live under `adapter.inbound.rest`.
- `HexagonalArchitectureSelfTest` – meta-test that proves the rules actually fail on a deliberately-broken fixture, so a mis-configured rule set can't give a false green.

See [`docs/architecture.md`](docs/architecture.md) for the detailed dependency map and [`docs/new-bounded-context.md`](docs/new-bounded-context.md) for the step-by-step guide to fork this starter into a new service.

## Project conventions

- **Java 25** source/target. `-parameters` enabled.
- **UTF-8** everywhere.
- **BigDecimal** for money. Monetary math is isolated inside `Money`.
- Every domain exception extends `DomainException`; adapters translate them.
- Flyway migrations live under [`order-adapter-persistence/src/main/resources/db/migration`](order-adapter-persistence/src/main/resources/db/migration).
- H2 is used in **PostgreSQL compatibility mode** so the compile-time Micronaut Data `POSTGRES` dialect works identically in dev, test, and prod.

## Prod configuration

Activate the prod profile and supply secrets via environment variables:

```bash
java -Dmicronaut.environments=prod \
     -DJDBC_URL=jdbc:postgresql://db:5432/orders \
     -DJDBC_USER=orders \
     -DJDBC_PASSWORD=***** \
     -jar order-bootstrap/target/order-bootstrap-*.jar
```

See [`application-prod.yml`](order-bootstrap/src/main/resources/application-prod.yml).
