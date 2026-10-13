package com.enterprise.starter.order.adapter.outbound.persistence;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Persistence record for the <code>orders</code> table.
 *
 * <p>Lives in the persistence adapter so the domain aggregate stays free of
 * ORM annotations. Mapped to/from {@link
 * com.enterprise.starter.order.domain.model.Order} by {@link OrderEntityMapper}.
 */
@MappedEntity("orders")
public record OrderRecord(
        @Id UUID id,
        @MappedProperty("status") String status,
        @MappedProperty("currency_code") String currencyCode,
        @MappedProperty("total_amount") BigDecimal totalAmount,
        @MappedProperty("created_at") Instant createdAt) {
}
