package com.enterprise.starter.order.application.port.out;

import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;

import java.util.Optional;

/**
 * Outbound port owning the persistence contract for the {@link Order} aggregate.
 *
 * <p>The adapter implementation lives in <code>order-adapter-persistence</code>
 * and is free to use any technology (JDBC, JPA, DynamoDB, ...). The application
 * layer only knows about this interface.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId id);
}
