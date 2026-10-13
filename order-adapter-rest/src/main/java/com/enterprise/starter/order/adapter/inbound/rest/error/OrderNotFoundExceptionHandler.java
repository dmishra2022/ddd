package com.enterprise.starter.order.adapter.inbound.rest.error;

import com.enterprise.starter.order.domain.exception.OrderNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces("application/problem+json")
@Singleton
@Requires(classes = {OrderNotFoundException.class, ExceptionHandler.class})
public class OrderNotFoundExceptionHandler
        implements ExceptionHandler<OrderNotFoundException, HttpResponse<ProblemDetail>> {

    @Override
    public HttpResponse<ProblemDetail> handle(HttpRequest request, OrderNotFoundException exception) {
        return HttpResponse.notFound(
                ProblemDetail.of(404, "Order not found", exception.getMessage()));
    }
}
