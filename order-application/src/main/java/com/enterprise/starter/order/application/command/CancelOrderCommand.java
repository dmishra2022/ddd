package com.enterprise.starter.order.application.command;

import com.enterprise.starter.order.domain.model.OrderId;

import java.util.Objects;

public record CancelOrderCommand(OrderId orderId, String reason) {

    public CancelOrderCommand {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(reason, "reason must not be null");
    }
}
