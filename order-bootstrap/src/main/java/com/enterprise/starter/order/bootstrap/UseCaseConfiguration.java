package com.enterprise.starter.order.bootstrap;

import com.enterprise.starter.order.application.port.in.CancelOrderUseCase;
import com.enterprise.starter.order.application.port.in.GetOrderUseCase;
import com.enterprise.starter.order.application.port.in.PlaceOrderUseCase;
import com.enterprise.starter.order.application.port.out.ClockPort;
import com.enterprise.starter.order.application.port.out.DomainEventPublisher;
import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.application.service.CancelOrderService;
import com.enterprise.starter.order.application.service.GetOrderService;
import com.enterprise.starter.order.application.service.PlaceOrderService;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

/**
 * Composition-root wiring for the application layer.
 *
 * <p>Keeping the <code>@Factory</code> here lets the application module stay
 * free of any DI-container annotations ({@code jakarta.inject}, Micronaut, ...).
 * Every use case is still exposed to the container as a singleton bean, but
 * the only module aware of Micronaut's existence is the bootstrap.
 */
@Factory
public class UseCaseConfiguration {

    @Singleton
    PlaceOrderUseCase placeOrderUseCase(
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher,
            ClockPort clock) {
        return new PlaceOrderService(orderRepository, eventPublisher, clock);
    }

    @Singleton
    GetOrderUseCase getOrderUseCase(OrderRepository orderRepository) {
        return new GetOrderService(orderRepository);
    }

    @Singleton
    CancelOrderUseCase cancelOrderUseCase(
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher,
            ClockPort clock) {
        return new CancelOrderService(orderRepository, eventPublisher, clock);
    }
}
