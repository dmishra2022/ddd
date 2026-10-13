package com.enterprise.starter.order.application.service;

import com.enterprise.starter.order.application.command.OrderLineCommand;
import com.enterprise.starter.order.application.command.PlaceOrderCommand;
import com.enterprise.starter.order.application.port.in.PlaceOrderUseCase;
import com.enterprise.starter.order.application.port.out.ClockPort;
import com.enterprise.starter.order.application.port.out.DomainEventPublisher;
import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.domain.model.Money;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;
import com.enterprise.starter.order.domain.model.OrderLine;
import com.enterprise.starter.order.domain.model.ProductId;

import java.util.Currency;
import java.util.List;

public class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final DomainEventPublisher eventPublisher;
    private final ClockPort clock;

    public PlaceOrderService(
            OrderRepository orderRepository,
            DomainEventPublisher eventPublisher,
            ClockPort clock) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public Order place(PlaceOrderCommand command) {
        Currency currency = Currency.getInstance(command.currencyCode());
        List<OrderLine> lines = command.lines().stream()
                .map(line -> toDomainLine(line, currency))
                .toList();

        Order order = Order.place(OrderId.newId(), lines, clock.now());
        Order persisted = orderRepository.save(order);
        eventPublisher.publishAll(persisted.drainEvents());
        return persisted;
    }

    private static OrderLine toDomainLine(OrderLineCommand line, Currency currency) {
        return new OrderLine(
                new ProductId(line.productId()),
                line.quantity(),
                new Money(line.unitPrice(), currency));
    }
}
