package com.enterprise.starter.order.adapter.outbound.persistence;

import com.enterprise.starter.order.domain.model.Money;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;
import com.enterprise.starter.order.domain.model.OrderLine;
import com.enterprise.starter.order.domain.model.OrderStatus;
import com.enterprise.starter.order.domain.model.ProductId;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

/**
 * Translates between the domain aggregate and the JDBC persistence records.
 *
 * <p>Lives in the persistence adapter so the domain layer stays unaware of
 * table schemas, column names, or JDBC concerns.
 */
final class OrderEntityMapper {

    private OrderEntityMapper() {
    }

    static OrderRecord toRecord(Order order) {
        return new OrderRecord(
                order.id().value(),
                order.status().name(),
                order.totalAmount().currency().getCurrencyCode(),
                order.totalAmount().amount(),
                order.createdAt());
    }

    static List<OrderLineRecord> toLineRecords(Order order) {
        List<OrderLine> lines = order.lines();
        return java.util.stream.IntStream.range(0, lines.size())
                .mapToObj(i -> toLineRecord(order.id().value(), i, lines.get(i)))
                .toList();
    }

    private static OrderLineRecord toLineRecord(UUID orderId, int index, OrderLine line) {
        return new OrderLineRecord(
                UUID.randomUUID(),
                orderId,
                index,
                line.productId().value(),
                line.quantity(),
                line.unitPrice().amount());
    }

    static Order toDomain(OrderRecord record, List<OrderLineRecord> lineRecords) {
        Currency currency = Currency.getInstance(record.currencyCode());
        List<OrderLine> lines = lineRecords.stream()
                .map(lr -> toDomainLine(lr, currency))
                .toList();
        return Order.rehydrate(
                new OrderId(record.id()),
                lines,
                OrderStatus.valueOf(record.status()),
                record.createdAt());
    }

    private static OrderLine toDomainLine(OrderLineRecord record, Currency currency) {
        return new OrderLine(
                new ProductId(record.productId()),
                record.quantity(),
                new Money(record.unitPrice(), currency));
    }
}
