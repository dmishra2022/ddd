package com.enterprise.starter.order.adapter.inbound.rest;

import com.enterprise.starter.order.adapter.inbound.rest.dto.CancelOrderRequest;
import com.enterprise.starter.order.adapter.inbound.rest.dto.OrderLineRequest;
import com.enterprise.starter.order.adapter.inbound.rest.dto.OrderResponse;
import com.enterprise.starter.order.adapter.inbound.rest.dto.PlaceOrderRequest;
import com.enterprise.starter.order.application.command.CancelOrderCommand;
import com.enterprise.starter.order.application.command.OrderLineCommand;
import com.enterprise.starter.order.application.command.PlaceOrderCommand;
import com.enterprise.starter.order.application.port.in.CancelOrderUseCase;
import com.enterprise.starter.order.application.port.in.GetOrderUseCase;
import com.enterprise.starter.order.application.port.in.PlaceOrderUseCase;
import com.enterprise.starter.order.domain.model.Order;
import com.enterprise.starter.order.domain.model.OrderId;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;

import java.net.URI;

/**
 * Inbound REST adapter.
 *
 * <p>Contract:
 * <ul>
 *   <li>Depends only on inbound ports from the application layer.</li>
 *   <li>Translates HTTP ↔ application commands/queries. No business logic.</li>
 *   <li>Domain exceptions are mapped to problem+json by dedicated handlers.</li>
 * </ul>
 */
@Validated
@Controller("/api/v1/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrder;
    private final GetOrderUseCase getOrder;
    private final CancelOrderUseCase cancelOrder;

    public OrderController(
            PlaceOrderUseCase placeOrder,
            GetOrderUseCase getOrder,
            CancelOrderUseCase cancelOrder) {
        this.placeOrder = placeOrder;
        this.getOrder = getOrder;
        this.cancelOrder = cancelOrder;
    }

    @Post
    public HttpResponse<OrderResponse> place(@Valid @Body PlaceOrderRequest request) {
        var command = toCommand(request);
        Order order = placeOrder.place(command);
        return HttpResponse.created(OrderResponse.from(order))
                .headers(h -> h.location(URI.create("/api/v1/orders/" + order.id().asString())));
    }

    @Get("/{id}")
    public OrderResponse get(@PathVariable String id) {
        Order order = getOrder.get(OrderId.of(id));
        return OrderResponse.from(order);
    }

    @Post("/{id}/cancel")
    public OrderResponse cancel(@PathVariable String id, @Valid @Body CancelOrderRequest request) {
        Order order = cancelOrder.cancel(new CancelOrderCommand(OrderId.of(id), request.reason()));
        return OrderResponse.from(order);
    }

    private static PlaceOrderCommand toCommand(PlaceOrderRequest request) {
        var lines = request.lines().stream()
                .map(OrderController::toLineCommand)
                .toList();
        return new PlaceOrderCommand(request.currencyCode(), lines);
    }

    private static OrderLineCommand toLineCommand(OrderLineRequest request) {
        return new OrderLineCommand(request.productId(), request.quantity(), request.unitPrice());
    }
}
