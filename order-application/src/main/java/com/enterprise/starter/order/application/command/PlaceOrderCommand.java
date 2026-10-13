package com.enterprise.starter.order.application.command;

import java.util.List;
import java.util.Objects;

public record PlaceOrderCommand(String currencyCode, List<OrderLineCommand> lines) {

    public PlaceOrderCommand {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");
        Objects.requireNonNull(lines, "lines must not be null");
        lines = List.copyOf(lines);
    }
}
