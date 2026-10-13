package com.enterprise.starter.order.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object identifying an {@link Order}.
 *
 * <p>Wrapping the identifier makes aggregate boundaries explicit, prevents
 * accidentally passing a product id where an order id is required, and keeps
 * the domain layer free from framework assumptions about how ids are generated.
 */
public record OrderId(UUID value) {

    public OrderId {
        Objects.requireNonNull(value, "OrderId value must not be null");
    }

    public static OrderId newId() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(String raw) {
        Objects.requireNonNull(raw, "raw order id must not be null");
        return new OrderId(UUID.fromString(raw));
    }

    public String asString() {
        return value.toString();
    }
}
