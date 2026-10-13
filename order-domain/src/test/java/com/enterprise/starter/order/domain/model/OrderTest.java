package com.enterprise.starter.order.domain.model;

import com.enterprise.starter.order.domain.event.OrderCancelled;
import com.enterprise.starter.order.domain.event.OrderPlaced;
import com.enterprise.starter.order.domain.exception.InvalidOrderStateException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void placing_an_order_emits_order_placed_and_computes_total() {
        var line = new OrderLine(new ProductId("sku-1"), 2, Money.of("10.00", "USD"));

        Order order = Order.place(OrderId.newId(), List.of(line), NOW);

        assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.totalAmount()).isEqualTo(Money.of("20.00", "USD"));

        var events = order.drainEvents();
        assertThat(events).singleElement().isInstanceOf(OrderPlaced.class);
        assertThat(order.drainEvents()).isEmpty();
    }

    @Test
    void placing_an_order_without_lines_is_rejected() {
        assertThatThrownBy(() -> Order.place(OrderId.newId(), List.of(), NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one line");
    }

    @Test
    void cancelling_a_placed_order_emits_order_cancelled() {
        var order = Order.place(
                OrderId.newId(),
                List.of(new OrderLine(new ProductId("sku-1"), 1, Money.of("5.00", "EUR"))),
                NOW);
        order.drainEvents();

        order.cancel("customer request", NOW.plusSeconds(60));

        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.drainEvents()).singleElement().isInstanceOf(OrderCancelled.class);
    }

    @Test
    void cancelling_a_shipped_order_is_rejected() {
        var line = new OrderLine(new ProductId("sku-1"), 1, Money.of("5.00", "EUR"));
        var order = Order.rehydrate(OrderId.newId(), List.of(line), OrderStatus.SHIPPED, NOW);

        assertThatThrownBy(() -> order.cancel("too late", NOW))
                .isInstanceOf(InvalidOrderStateException.class);
    }

    @Test
    void cancelling_a_draft_order_is_rejected() {
        var line = new OrderLine(new ProductId("sku-1"), 1, Money.of("5.00", "EUR"));
        var order = Order.rehydrate(OrderId.newId(), List.of(line), OrderStatus.DRAFT, NOW);

        assertThatThrownBy(() -> order.cancel("whatever", NOW))
                .isInstanceOf(InvalidOrderStateException.class);
    }
}
