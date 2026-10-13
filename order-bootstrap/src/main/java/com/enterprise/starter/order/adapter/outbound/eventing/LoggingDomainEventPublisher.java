package com.enterprise.starter.order.adapter.outbound.eventing;

import com.enterprise.starter.order.application.port.out.DomainEventPublisher;
import com.enterprise.starter.order.domain.event.DomainEvent;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Starter-grade {@link DomainEventPublisher} that writes each event to the
 * log at INFO. Replace with a Kafka / outbox / message-bus implementation
 * when the service needs to publish events externally.
 */
@Singleton
public class LoggingDomainEventPublisher implements DomainEventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingDomainEventPublisher.class);

    @Override
    public void publishAll(List<? extends DomainEvent> events) {
        for (DomainEvent event : events) {
            LOG.info("domain.event type={} payload={}", event.getClass().getSimpleName(), event);
        }
    }
}
