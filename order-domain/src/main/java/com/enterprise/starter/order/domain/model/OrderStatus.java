package com.enterprise.starter.order.domain.model;

/**
 * Lifecycle states for an {@link Order}.
 *
 * <p>Transition matrix enforced by the aggregate:
 * <pre>
 *   DRAFT     -> PLACED
 *   PLACED    -> CANCELLED | SHIPPED
 *   CANCELLED -> (terminal)
 *   SHIPPED   -> (terminal)
 * </pre>
 */
public enum OrderStatus {
    DRAFT,
    PLACED,
    CANCELLED,
    SHIPPED
}
