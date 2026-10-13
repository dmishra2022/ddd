package com.enterprise.starter.order.adapter.inbound.rest.error;

import com.enterprise.starter.order.domain.exception.InvalidOrderStateException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces("application/problem+json")
@Singleton
@Requires(classes = {InvalidOrderStateException.class, ExceptionHandler.class})
public class InvalidOrderStateExceptionHandler
        implements ExceptionHandler<InvalidOrderStateException, HttpResponse<ProblemDetail>> {

    @Override
    public HttpResponse<ProblemDetail> handle(HttpRequest request, InvalidOrderStateException exception) {
        return HttpResponse.<ProblemDetail>status(
                        io.micronaut.http.HttpStatus.CONFLICT)
                .body(ProblemDetail.of(409, "Invalid order state", exception.getMessage()));
    }
}
