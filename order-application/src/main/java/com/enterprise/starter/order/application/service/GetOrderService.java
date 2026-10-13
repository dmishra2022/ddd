package com.enterprise.starter.order.application.service;

import com.enterprise.starter.order.application.port.in.GetOrderUseCase;
import com.enterprise.starter.order.application.port.out.OrderRepository;
import com.enterprise.starter.order.domain.exception.OrderNotFoundException;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;

public class GetOrderService implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order get(OrderId id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
