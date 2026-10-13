package com.enterprise.starter.order.adapter.inbound.rest.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Serdeable
public record OrderLineRequest(
        @NotBlank String productId,
        @Min(1) int quantity,
        @NotNull @DecimalMin(value = "0.00", inclusive = true) BigDecimal unitPrice) {
}
