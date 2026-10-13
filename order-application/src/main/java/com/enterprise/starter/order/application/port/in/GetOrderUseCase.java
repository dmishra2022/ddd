package com.enterprise.starter.order.application.port.in;

import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;

public interface GetOrderUseCase {

    Order get(OrderId id);
}
