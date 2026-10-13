# Spinning up a new bounded context from this starter

This repository doubles as the master template for any new enterprise service.
Here is the mechanical checklist. The example below creates a hypothetical
`Billing` service.

## 1. Clone and rename

```bash
cp -R order-management-starter billing-management
cd billing-management
```

Then:

- Rename every module directory `order-*` to `billing-*`.
- In every `pom.xml`, replace `order-management-parent` and the `order-*`
  module artifact ids with `billing-*`.
- Replace the base package in Java sources: `com.enterprise.starter.order`
  → `com.enterprise.starter.billing`.
- Update `base.package` in the parent `pom.xml`.
- Update `Packages.BASE` in `architecture-tests/.../Packages.java` to the
  new base.

## 2. Model the domain

Working in `billing-domain/src/main/java/com/enterprise/starter/billing/domain/`:

- Replace `Order`, `OrderLine`, `OrderStatus`, `OrderPlaced`, `OrderCancelled`
  with your aggregate root, value objects, lifecycle enum, and domain events.
- Keep the layer JDK-only. The `DomainPurityTest` will fail the build if a
  framework slips in.

## 3. Define the ports

Working in `billing-application`:

- `application.port.in` — one interface per use case; name them `*UseCase`.
  The `ApplicationLayerTest` enforces this.
- `application.port.out` — one interface per side effect (persistence,
  messaging, time, external HTTP service).
- `application.service` — plain Java classes that implement the inbound
  ports. Do **not** add `@Singleton`; the composition root wires them.
- `application.command` — immutable records for inputs.

## 4. Implement the adapters

- `billing-adapter-rest`: controllers under `adapter.inbound.rest`.
  Only inbound ports as constructor params.
- `billing-adapter-persistence`: Micronaut Data JDBC `@MappedEntity` records
  + a `*PersistenceAdapter` that implements the outbound port using a mapper
  class. Flyway migration under `src/main/resources/db/migration`.
- Any additional outbound adapter (`messaging`, `email`, ...) sits in its
  own package `adapter.outbound.<name>` and gets its own module if the
  dependencies are heavy.

## 5. Wire it in the composition root

In `billing-bootstrap`:

- `UseCaseConfiguration` is a `@Factory` that returns each `*UseCase`
  interface. One `@Singleton` method per use case.
- Default `ClockPort` and `DomainEventPublisher` adapters live here; replace
  them with Kafka or outbox-pattern adapters when needed.
- `application.yml` is the only place that mentions Micronaut-specific
  configuration (datasource, port, thread selection, flyway).

## 6. Extend the architecture rules

In `architecture-tests`:

- If you add a new outbound adapter package (e.g.
  `adapter.outbound.messaging`), register it in `HexagonalArchitectureTest`
  via `.adapter("messaging", Packages.ADAPTER_OUTBOUND_MESSAGING + "..")`.
- Add any technology-specific forbidden packages (e.g. `org.apache.kafka..`)
  to `DomainPurityTest` and `ApplicationLayerTest`.

## 7. Verify

```bash
mvn verify
```

The build fails hard if:

- Domain depends on a framework.
- Application references an adapter.
- An inbound adapter references an outbound adapter.
- A controller lives outside `adapter.inbound.rest`.
- An inbound port is misplaced.
- The reference domain's HTTP flow is broken.

Only then push.
