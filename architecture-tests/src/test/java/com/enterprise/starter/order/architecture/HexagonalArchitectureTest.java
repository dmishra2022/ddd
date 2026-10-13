package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

/**
 * Executable architecture specification for the Order Management service and
 * every future bounded context that follows the same package layout.
 *
 * <p>Scans all production classes under {@value Packages#BASE} and asserts
 * that the dependency graph matches the hexagonal / ports-and-adapters model.
 *
 * <p>Treats this test as a tripwire: if someone imports a framework type
 * from the domain, or wires a repository directly into a controller, the
 * build fails loudly rather than letting drift accumulate.
 */
@AnalyzeClasses(
        packages = Packages.BASE,
        importOptions = {
                ImportOption.DoNotIncludeTests.class,
                DoNotIncludeGeneratedClasses.class
        })
public class HexagonalArchitectureTest {

    /**
     * Core hexagonal rule: domain is the center, application orchestrates,
     * every outside interaction is an adapter.
     */
    @ArchTest
    static final ArchRule hexagonal_boundaries_are_respected =
            onionArchitecture()
                    .domainModels(Packages.DOMAIN_MODEL + "..")
                    .domainServices(Packages.DOMAIN + "..")
                    .applicationServices(Packages.APPLICATION + "..")
                    .adapter("rest", Packages.ADAPTER_INBOUND_REST + "..")
                    .adapter("persistence", Packages.ADAPTER_OUTBOUND_PERSISTENCE + "..")
                    .adapter("time", Packages.ADAPTER_OUTBOUND_TIME + "..")
                    .adapter("eventing", Packages.ADAPTER_OUTBOUND_EVENTING + "..")
                    // The composition root is treated as a thin adapter in its own right:
                    // allowed to see the application layer (to wire use cases) but still
                    // forbidden from depending on any other adapter.
                    .adapter("bootstrap", Packages.BOOTSTRAP + "..")
                    .withOptionalLayers(true)
                    .as("Hexagonal boundaries: domain -> application -> adapters, "
                            + "adapters never depend on each other, "
                            + "domain and application never depend on adapters");
}
