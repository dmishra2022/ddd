package com.enterprise.starter.order.bootstrap;

import com.enterprise.starter.order.adapter.inbound.rest.dto.OrderResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * End-to-end smoke test exercising the full hexagonal flow:
 * HTTP controller -> inbound port -> use case -> outbound port -> JDBC + H2.
 */
@MicronautTest
class OrderApiIntegrationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void place_get_cancel_round_trip() {
        var placePayload = Map.of(
                "currencyCode", "USD",
                "lines", List.of(Map.of(
                        "productId", "sku-abc",
                        "quantity", 2,
                        "unitPrice", new BigDecimal("12.50"))));

        HttpResponse<OrderResponse> placed = client.toBlocking().exchange(
                HttpRequest.POST("/api/v1/orders", placePayload),
                OrderResponse.class);
        assertThat(placed.getStatus().getCode()).isEqualTo(HttpStatus.CREATED.getCode());
        OrderResponse placedBody = placed.body();
        assertThat(placedBody).isNotNull();
        assertThat(placedBody.status()).isEqualTo("PLACED");
        assertThat(placedBody.id()).isNotBlank();
        String orderId = placedBody.id();

        OrderResponse fetched = client.toBlocking().retrieve(
                HttpRequest.GET("/api/v1/orders/" + orderId),
                OrderResponse.class);
        assertThat(fetched.status()).isEqualTo("PLACED");
        assertThat(fetched.lines()).hasSize(1);
        assertThat(fetched.totalAmount()).isEqualByComparingTo(new BigDecimal("25.00"));

        OrderResponse cancelled = client.toBlocking().retrieve(
                HttpRequest.POST(
                        "/api/v1/orders/" + orderId + "/cancel",
                        Map.of("reason", "customer request")),
                OrderResponse.class);
        assertThat(cancelled.status()).isEqualTo("CANCELLED");
    }

    @Test
    void unknown_order_returns_404_problem_json() {
        HttpClientResponseException ex = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(
                        HttpRequest.GET("/api/v1/orders/00000000-0000-0000-0000-000000000000"),
                        OrderResponse.class));
        assertThat(ex.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    void invalid_payload_returns_400() {
        var invalidPayload = Map.of("currencyCode", "USD", "lines", List.of());

        HttpClientResponseException ex = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().retrieve(
                        HttpRequest.POST("/api/v1/orders", invalidPayload),
                        OrderResponse.class));
        assertThat(ex.getStatus().getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
    }
}
