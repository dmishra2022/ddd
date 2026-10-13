package com.enterprise.starter.order.domain.model;

import com.enterprise.starter.order.domain.event.DomainEvent;
import com.enterprise.starter.order.domain.event.OrderCancelled;
import com.enterprise.starter.order.domain.event.OrderPlaced;
import com.enterprise.starter.order.domain.exception.InvalidOrderStateException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate root for the Order Management bounded context.
 *
 * <p>The aggregate guards two invariants:
 * <ol>
 *   <li>An order must have at least one line item.</li>
 *   <li>All line items share a single currency; the total is kept consistent
 *       with the lines at construction time.</li>
 * </ol>
 *
 * <p>State transitions are only possible through the aggregate's methods;
 * invalid transitions raise {@link InvalidOrderStateException}. Domain events
 * raised during a transaction are collected on the aggregate and drained by
 * the use case after it is successfully persisted.
 */
public final class Order {

    private final OrderId id;
    private final List<OrderLine> lines;
    private final Money totalAmount;
    private OrderStatus status;
    private final Instant createdAt;
    private final List<DomainEvent> pendingEvents = new ArrayList<>();

    private Order(
            OrderId id,
            List<OrderLine> lines,
            Money totalAmount,
            OrderStatus status,
            Instant createdAt) {
        this.id = id;
        this.lines = lines;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Factory for a brand-new order that is immediately placed by the customer.
     *
     * @param id          identifier assigned by the caller (use case decides policy)
     * @param lines       at least one line item
     * @param placedAt    event timestamp supplied by the use case
     */
    public static Order place(OrderId id, List<OrderLine> lines, Instant placedAt) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        Objects.requireNonNull(placedAt, "placedAt must not be null");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least one line");
        }

        List<OrderLine> copy = List.copyOf(lines);
        Money total = computeTotal(copy);
        Order order = new Order(id, new ArrayList<>(copy), total, OrderStatus.PLACED, placedAt);
        order.pendingEvents.add(new OrderPlaced(id, total, placedAt));
        return order;
    }

    /**
     * Rehydration constructor used by outbound adapters when loading from storage.
     * No invariants about status transitions are evaluated and no domain events
     * are emitted.
     */
    public static Order rehydrate(
            OrderId id,
            List<OrderLine> lines,
            OrderStatus status,
            Instant createdAt) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("A rehydrated order must contain at least one line");
        }
        List<OrderLine> copy = List.copyOf(lines);
        Money total = computeTotal(copy);
        return new Order(id, new ArrayList<>(copy), total, status, createdAt);
    }

    public void cancel(String reason, Instant cancelledAt) {
        Objects.requireNonNull(reason, "reason must not be null");
        Objects.requireNonNull(cancelledAt, "cancelledAt must not be null");
        switch (status) {
            case PLACED -> {
                this.status = OrderStatus.CANCELLED;
                pendingEvents.add(new OrderCancelled(id, reason, cancelledAt));
            }
            case DRAFT, CANCELLED, SHIPPED -> throw new InvalidOrderStateException(
                    status,
                    "Order %s cannot be cancelled while in status %s".formatted(id.asString(), status));
        }
    }

    public OrderId id() {
        return id;
    }

    public List<OrderLine> lines() {
        return Collections.unmodifiableList(lines);
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public OrderStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    /**
     * Returns the events raised since the last call and clears the internal buffer.
     * Called by the use case after the aggregate is persisted so events are only
     * published once per transaction.
     */
    public List<DomainEvent> drainEvents() {
        List<DomainEvent> drained = List.copyOf(pendingEvents);
        pendingEvents.clear();
        return drained;
    }

    private static Money computeTotal(List<OrderLine> lines) {
        Currency currency = lines.getFirst().unitPrice().currency();
        Money running = Money.zero(currency);
        for (OrderLine line : lines) {
            running = running.add(line.subtotal());
        }
        return running;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Order other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Order{id=%s, status=%s, lines=%d, total=%s}"
                .formatted(id.asString(), status, lines.size(), totalAmount);
    }
}
