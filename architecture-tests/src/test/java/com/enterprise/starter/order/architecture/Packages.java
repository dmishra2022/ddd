package com.enterprise.starter.order.architecture;

/**
 * Single source of truth for the package layout the ArchUnit rules enforce.
 *
 * <p>Teams forking this starter to spin up a new bounded context only need
 * to change {@link #BASE}; every other constant is derived from it.
 */
final class Packages {

    static final String BASE = "com.enterprise.starter.order";

    static final String DOMAIN = BASE + ".domain";
    static final String DOMAIN_MODEL = DOMAIN + ".model";
    static final String DOMAIN_EVENT = DOMAIN + ".event";
    static final String DOMAIN_EXCEPTION = DOMAIN + ".exception";

    static final String APPLICATION = BASE + ".application";
    static final String APPLICATION_PORT_IN = APPLICATION + ".port.in";
    static final String APPLICATION_PORT_OUT = APPLICATION + ".port.out";
    static final String APPLICATION_SERVICE = APPLICATION + ".service";

    static final String ADAPTER_INBOUND = BASE + ".adapter.inbound";
    static final String ADAPTER_INBOUND_REST = ADAPTER_INBOUND + ".rest";

    static final String ADAPTER_OUTBOUND = BASE + ".adapter.outbound";
    static final String ADAPTER_OUTBOUND_PERSISTENCE = ADAPTER_OUTBOUND + ".persistence";
    static final String ADAPTER_OUTBOUND_TIME = ADAPTER_OUTBOUND + ".time";
    static final String ADAPTER_OUTBOUND_EVENTING = ADAPTER_OUTBOUND + ".eventing";

    static final String BOOTSTRAP = BASE + ".bootstrap";

    private Packages() {
    }
}
