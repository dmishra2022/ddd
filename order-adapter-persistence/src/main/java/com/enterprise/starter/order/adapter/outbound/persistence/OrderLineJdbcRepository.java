package com.enterprise.starter.order.adapter.outbound.persistence;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface OrderLineJdbcRepository extends CrudRepository<OrderLineRecord, UUID> {

    List<OrderLineRecord> findByOrderIdOrderByLineNumber(UUID orderId);

    void deleteByOrderId(UUID orderId);
}
