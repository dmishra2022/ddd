package com.enterprise.starter.order.application.service.support;

import com.enterprise.starter.order.application.port.out.ClockPort;

import java.time.Instant;

public final class FixedClock implements ClockPort {

    private final Instant instant;

    public FixedClock(Instant instant) {
        this.instant = instant;
    }

    @Override
    public Instant now() {
        return instant;
    }
}
