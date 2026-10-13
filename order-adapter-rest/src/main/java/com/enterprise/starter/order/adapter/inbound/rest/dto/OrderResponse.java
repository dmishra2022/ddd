package com.enterprise.starter.order.adapter.inbound.rest.dto;

import com.enterprise.starter.order.domain.model.Order;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Serdeable
public record OrderResponse(
        String id,
        String status,
        String currencyCode,
        BigDecimal totalAmount,
        Instant createdAt,
        List<Line> lines) {

    @Serdeable
    public record Line(String productId, int quantity, BigDecimal unitPrice) {
    }

    public static OrderResponse from(Order order) {
        List<Line> lines = order.lines().stream()
                .map(l -> new Line(l.productId().value(), l.quantity(), l.unitPrice().amount()))
                .toList();
        return new OrderResponse(
                order.id().asString(),
                order.status().name(),
                order.totalAmount().currency().getCurrencyCode(),
                order.totalAmount().amount(),
                order.createdAt(),
                lines);
    }
}
