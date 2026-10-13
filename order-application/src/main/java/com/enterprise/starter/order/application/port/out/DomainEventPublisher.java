package com.enterprise.starter.order.application.port.out;

import com.enterprise.starter.order.domain.event.DomainEvent;

import java.util.List;

/**
 * Outbound port for publishing domain events emitted during a use case.
 *
 * <p>The starter ships a logging implementation; teams swap it for Kafka,
 * SNS, outbox-pattern-into-table, etc. without touching the application layer.
 */
public interface DomainEventPublisher {

    void publishAll(List<? extends DomainEvent> events);
}
