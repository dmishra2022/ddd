package com.enterprise.starter.order.application.command;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Transport-agnostic representation of a line item in an incoming command.
 *
 * <p>Lives in the application layer because it is consumed by use cases but
 * stays free of transport concerns (no serialization annotations, no HTTP
 * validation constraints).
 */
public record OrderLineCommand(String productId, int quantity, BigDecimal unitPrice) {

    public OrderLineCommand {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }
}
