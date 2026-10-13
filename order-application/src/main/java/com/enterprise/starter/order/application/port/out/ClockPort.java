package com.enterprise.starter.order.application.port.out;

import java.time.Instant;

/**
 * Outbound port for retrieving the current wall-clock time.
 *
 * <p>Keeping time behind a port lets use cases be tested with deterministic
 * clocks without reaching for static JDK methods.
 */
public interface ClockPort {

    Instant now();
}
