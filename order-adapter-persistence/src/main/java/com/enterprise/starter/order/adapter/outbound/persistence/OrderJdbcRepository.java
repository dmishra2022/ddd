package com.enterprise.starter.order.adapter.outbound.persistence;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Micronaut Data JDBC repository for {@link OrderRecord}.
 *
 * <p>Dialect is pinned to <code>POSTGRES</code>. The dev profile runs H2 in
 * PostgreSQL compatibility mode so the same generated SQL works in both
 * environments. Production profiles target real PostgreSQL.
 */
@JdbcRepository(dialect = Dialect.POSTGRES)
public interface OrderJdbcRepository extends CrudRepository<OrderRecord, UUID> {
}
