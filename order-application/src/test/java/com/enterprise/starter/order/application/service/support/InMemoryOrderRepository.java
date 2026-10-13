package com.enterprise.starter.order.application.service.support;

import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Test double. Not part of the production code. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<OrderId, Order> store = new HashMap<>();

    @Override
    public Order save(Order order) {
        store.put(order.id(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(store.get(id));
    }
}
