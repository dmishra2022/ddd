package com.enterprise.starter.order.application.port.in;

import com.enterprise.starter.order.application.command.PlaceOrderCommand;
import com.enterprise.starter.order.domain.model.Order;

/**
 * Inbound port invoked by any driving adapter (REST, CLI, message consumer).
 *
 * <p>Keeping it as a narrow interface forces the adapter to depend on an
 * abstraction rather than on a concrete service, which keeps the application
 * layer swappable and the inbound adapter trivially testable.
 */
public interface PlaceOrderUseCase {

    Order place(PlaceOrderCommand command);
}
