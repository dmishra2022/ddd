package com.enterprise.starter.order.domain.event;

import com.enterprise.starter.order.domain.model.Money;
import com.enterprise.starter.order.domain.model.OrderId;

import java.time.Instant;
import java.util.Objects;

public record OrderPlaced(OrderId orderId, Money totalAmount, Instant occurredAt)
        implements DomainEvent {

    public OrderPlaced {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(totalAmount, "totalAmount must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
