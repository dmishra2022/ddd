package com.enterprise.starter.order.domain.event;

import com.enterprise.starter.order.domain.model.OrderId;

import java.time.Instant;
import java.util.Objects;

public record OrderCancelled(OrderId orderId, String reason, Instant occurredAt)
        implements DomainEvent {

    public OrderCancelled {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(occurredAt, "occurredAt must not be null");
    }
}
