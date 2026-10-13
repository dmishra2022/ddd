package com.enterprise.starter.order.application.service;

import com.enterprise.starter.order.application.command.OrderLineCommand;
import com.enterprise.starter.order.application.command.PlaceOrderCommand;
import com.enterprise.starter.order.application.service.support.FixedClock;
import com.enterprise.starter.order.application.service.support.InMemoryOrderRepository;
import com.enterprise.starter.order.application.service.support.RecordingEventPublisher;
import com.enterprise.starter.order.domain.event.OrderPlaced;
import com.enterprise.starter.order.domain.model.Money;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceOrderServiceTest {

    @Test
    void places_order_persists_and_publishes_events() {
        var repo = new InMemoryOrderRepository();
        var publisher = new RecordingEventPublisher();
        var clock = new FixedClock(Instant.parse("2026-01-01T00:00:00Z"));
        var service = new PlaceOrderService(repo, publisher, clock);

        var command = new PlaceOrderCommand(
                "USD",
                List.of(new OrderLineCommand("sku-1", 2, new BigDecimal("10.00"))));

        Order order = service.place(command);

        assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.totalAmount()).isEqualTo(Money.of("20.00", "USD"));
        assertThat(repo.findById(order.id())).isPresent();
        assertThat(publisher.published()).singleElement().isInstanceOf(OrderPlaced.class);
    }
}
