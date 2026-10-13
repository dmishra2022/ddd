package com.enterprise.starter.order.adapter.inbound.rest.error;

import io.micronaut.serde.annotation.Serdeable;

/**
 * Minimal RFC 7807 problem+json representation.
 *
 * <p>Kept intentionally small; teams can extend it per-service without touching
 * the use case layer.
 */
@Serdeable
public record ProblemDetail(String type, String title, int status, String detail, String instance) {

    public static ProblemDetail of(int status, String title, String detail) {
        return new ProblemDetail("about:blank", title, status, detail, null);
    }
}
