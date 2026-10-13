package com.enterprise.starter.order.bootstrap;

import io.micronaut.runtime.Micronaut;

/**
 * Composition root for the Order Management service.
 *
 * <p>This is the only class allowed to pull together all adapters. Everything
 * else lives in its own module and is wired by the Micronaut DI container
 * at startup.
 */
public final class Application {

    private Application() {
    }

    public static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(Application.class)
                .start();
    }
}
