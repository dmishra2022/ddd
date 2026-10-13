package com.enterprise.starter.order.application.port.in;

import com.enterprise.starter.order.application.command.CancelOrderCommand;
import com.enterprise.starter.order.domain.model.Order;

public interface CancelOrderUseCase {

    Order cancel(CancelOrderCommand command);
}
