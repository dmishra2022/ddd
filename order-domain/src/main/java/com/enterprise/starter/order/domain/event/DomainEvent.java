package com.enterprise.starter.order.domain.event;

import java.time.Instant;

/**
 * Marker type for domain events emitted by aggregates.
 *
 * <p>Sealed to make the full set discoverable to downstream code (publishers,
 * tests) at compile time. Each event is a small immutable record carrying
 * just enough state to describe <em>what happened</em>.
 */
public sealed interface DomainEvent permits OrderPlaced, OrderCancelled {

    Instant occurredAt();
}
