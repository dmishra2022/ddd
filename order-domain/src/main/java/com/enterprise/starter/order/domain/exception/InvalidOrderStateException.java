package com.enterprise.starter.order.domain.exception;

import com.enterprise.starter.order.domain.model.OrderStatus;

/**
 * Thrown when an operation is not valid for the current state of an aggregate,
 * e.g. attempting to cancel an already-shipped order.
 */
public final class InvalidOrderStateException extends DomainException {

    private final OrderStatus currentStatus;

    public InvalidOrderStateException(OrderStatus currentStatus, String message) {
        super(message);
        this.currentStatus = currentStatus;
    }

    public OrderStatus currentStatus() {
        return currentStatus;
    }
}
