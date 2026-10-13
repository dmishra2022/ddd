package com.enterprise.starter.order.domain.model;

import java.util.Objects;

/**
 * Value object identifying a product within an order line.
 *
 * <p>The product catalog lives in a separate bounded context; here we only need
 * an opaque identifier so this value object stays a plain string wrapper.
 */
public record ProductId(String value) {

    public ProductId {
        Objects.requireNonNull(value, "ProductId value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ProductId value must not be blank");
        }
    }
}
