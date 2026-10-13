package com.enterprise.starter.order.adapter.outbound.persistence;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

import java.math.BigDecimal;
import java.util.UUID;

@MappedEntity("order_lines")
public record OrderLineRecord(
        @Id UUID id,
        @MappedProperty("order_id") UUID orderId,
        @MappedProperty("line_number") int lineNumber,
        @MappedProperty("product_id") String productId,
        int quantity,
        @MappedProperty("unit_price") BigDecimal unitPrice) {
}
