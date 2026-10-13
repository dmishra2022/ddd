package com.enterprise.starter.order.adapter.inbound.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record CancelOrderRequest(@NotBlank String reason) {
}
