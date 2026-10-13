package com.enterprise.starter.order.application.service;

import com.enterprise.starter.order.application.command.CancelOrderCommand;
import com.enterprise.starter.order.application.port.in.CancelOrderUseCase;
import com.enterprise.starter.order.application.port.out.ClockPort;
import com.enterprise.starter.order.application.port.out.DomainEventPublisher;
import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.domain.exception.OrderNotFoundException;
import com.enterprise.starter.order.domain.model.Order;

public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;
    private final ClockPort clock;

    public CancelOrderService(
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher,
            ClockPort clock) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public Order cancel(CancelOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        order.cancel(command.reason(), clock.now());
        Order persisted = orderRepository.save(order);
        eventPublisher.publishAll(persisted.drainEvents());
        return persisted;
    }
}
