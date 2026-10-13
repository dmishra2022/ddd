package com.enterprise.starter.order.application.service;

import com.enterprise.starter.order.application.command.CancelOrderCommand;
import com.enterprise.starter.order.application.command.OrderLineCommand;
import com.enterprise.starter.order.application.command.PlaceOrderCommand;
import com.enterprise.starter.order.application.service.support.FixedClock;
import com.enterprise.starter.order.application.service.support.InMemoryOrderRepository;
import com.enterprise.starter.order.application.service.support.RecordingEventPublisher;
import com.enterprise.starter.order.domain.event.OrderCancelled;
import com.enterprise.starter.order.domain.exception.OrderNotFoundException;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;
import com.enterprise.starter.order.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancelOrderServiceTest {

    private final Instant now = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void cancels_a_placed_order_and_publishes_event() {
        var repo = new InMemoryOrderRepository();
        var publisher = new RecordingEventPublisher();
        var clock = new FixedClock(now);
        var placeService = new PlaceOrderService(repo, publisher, clock);
        var cancelService = new CancelOrderService(repo, publisher, clock);

        Order placed = placeService.place(new PlaceOrderCommand(
                "USD",
                List.of(new OrderLineCommand("sku-1", 1, new BigDecimal("5.00")))));

        Order cancelled = cancelService.cancel(new CancelOrderCommand(placed.id(), "customer request"));

        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(publisher.published()).anyMatch(OrderCancelled.class::isInstance);
    }

    @Test
    void cancelling_an_unknown_order_raises_not_found() {
        var repo = new InMemoryOrderRepository();
        var service = new CancelOrderService(repo, new RecordingEventPublisher(), new FixedClock(now));

        assertThatThrownBy(() -> service.cancel(new CancelOrderCommand(OrderId.newId(), "no reason")))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
