package com.enterprise.starter.order.domain.exception;

/**
 * Base type for all exceptions thrown from the domain layer.
 *
 * <p>Inbound adapters translate these into transport-specific errors (HTTP 404,
 * 409, 422, ...). The domain itself knows nothing about transports.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
