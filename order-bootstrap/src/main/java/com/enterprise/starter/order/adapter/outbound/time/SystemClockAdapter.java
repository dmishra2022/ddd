package com.enterprise.starter.order.adapter.outbound.time;

import com.enterprise.starter.order.application.port.out.ClockPort;
import jakarta.inject.Singleton;

import java.time.Clock;
import java.time.Instant;

/**
 * Default implementation of the {@link ClockPort} outbound port backed by the
 * system UTC clock. Shipped in the bootstrap module as a sensible default;
 * teams may replace it in test or specialized environments.
 */
@Singleton
public class SystemClockAdapter implements ClockPort {

    private final Clock clock = Clock.systemUTC();

    @Override
    public Instant now() {
        return clock.instant();
    }
}
