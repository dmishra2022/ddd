package com.enterprise.starter.order.domain.model;

import java.util.Objects;

/**
 * Value object representing a single line item on an order.
 *
 * <p>An order line is immutable; changing quantity or price produces a new
 * line. The aggregate root ({@link Order}) is the only place that holds a
 * collection of lines and recomputes totals.
 */
public record OrderLine(ProductId productId, int quantity, Money unitPrice) {

    public OrderLine {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive, was " + quantity);
        }
        if (unitPrice.isNegative()) {
            throw new IllegalArgumentException("unitPrice must not be negative");
        }
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}
