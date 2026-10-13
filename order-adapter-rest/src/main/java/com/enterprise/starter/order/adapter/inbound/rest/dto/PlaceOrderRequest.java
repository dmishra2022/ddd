package com.enterprise.starter.order.adapter.inbound.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Serdeable
public record PlaceOrderRequest(
        @NotBlank String currencyCode,
        @NotEmpty @Valid List<OrderLineRequest> lines) {
}
