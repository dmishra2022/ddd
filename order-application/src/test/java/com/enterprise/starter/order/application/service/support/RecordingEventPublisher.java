package com.enterprise.starter.order.application.service.support;

import com.enterprise.starter.order.application.port.out.DomainEventPublisher;
import com.enterprise.starter.order.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public final class RecordingEventPublisher implements DomainEventPublisher {

    private final List<DomainEvent> published = new ArrayList<>();

    @Override
    public void publishAll(List<? extends DomainEvent> events) {
        published.addAll(events);
    }

    public List<DomainEvent> published() {
        return List.copyOf(published);
    }
}
