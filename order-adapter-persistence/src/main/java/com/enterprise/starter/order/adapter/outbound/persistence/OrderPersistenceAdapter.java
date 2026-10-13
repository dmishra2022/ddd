package com.enterprise.starter.order.adapter.outbound.persistence;

import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of the outbound {@link OrderRepository} port.
 *
 * <p>Owns the mapping between the domain aggregate and the two underlying
 * tables ({@code orders}, {@code order_lines}). Each save is executed inside
 * a transaction so parent + children either commit together or roll back
 * together.
 */
@Singleton
public class OrderPersistenceAdapter implements OrderRepository {

    private final OrderJdbcRepository orderJdbc;
    private final OrderLineJdbcRepository lineJdbc;

    public OrderPersistenceAdapter(
            OrderJdbcRepository orderJdbc,
            OrderLineJdbcRepository lineJdbc) {
        this.orderJdbc = orderJdbc;
        this.lineJdbc = lineJdbc;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderRecord orderRecord = OrderEntityMapper.toRecord(order);
        List<OrderLineRecord> lineRecords = OrderEntityMapper.toLineRecords(order);

        if (orderJdbc.existsById(orderRecord.id())) {
            orderJdbc.update(orderRecord);
            lineJdbc.deleteByOrderId(orderRecord.id());
        } else {
            orderJdbc.save(orderRecord);
        }
        lineJdbc.saveAll(lineRecords);
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(OrderId id) {
        return orderJdbc.findById(id.value())
                .map(record -> OrderEntityMapper.toDomain(
                        record,
                        lineJdbc.findByOrderIdOrderByLineNumber(record.id())));
    }
}
