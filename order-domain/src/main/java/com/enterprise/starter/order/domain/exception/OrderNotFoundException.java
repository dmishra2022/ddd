package com.enterprise.starter.order.domain.exception;

import com.enterprise.starter.order.domain.model.OrderId;

public final class OrderNotFoundException extends DomainException {

    private final OrderId orderId;

    public OrderNotFoundException(OrderId orderId) {
        super("Order not found: " + orderId.asString());
        this.orderId = orderId;
    }

    public OrderId orderId() {
        return orderId;
    }
}
