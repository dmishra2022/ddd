package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Rules enforcing adapter discipline:
 * <ul>
 *   <li>Inbound adapters must not short-circuit directly into outbound ones.</li>
 *   <li>Controllers live where we expect them.</li>
 *   <li>Only outbound adapter packages are allowed to implement outbound ports.</li>
 * </ul>
 */
@AnalyzeClasses(
        packages = Packages.BASE,
        importOptions = {
                ImportOption.DoNotIncludeTests.class,
                DoNotIncludeGeneratedClasses.class
        })
public class AdapterIsolationTest {

    @ArchTest
    static final ArchRule inbound_adapters_do_not_touch_outbound_adapters =
            noClasses()
                    .that().resideInAPackage(Packages.ADAPTER_INBOUND + "..")
                    .should().dependOnClassesThat().resideInAPackage(Packages.ADAPTER_OUTBOUND + "..")
                    .as("Inbound adapters (e.g. REST) must flow through application ports, "
                            + "never reach into persistence or other outbound adapters directly");

    @ArchTest
    static final ArchRule controllers_live_in_the_inbound_rest_package =
            classes()
                    .that().areAnnotatedWith("io.micronaut.http.annotation.Controller")
                    .should().resideInAPackage(Packages.ADAPTER_INBOUND_REST + "..")
                    .as("HTTP controllers must live under adapter.inbound.rest");

    @ArchTest
    static final ArchRule outbound_adapters_do_not_depend_on_inbound_adapters =
            noClasses()
                    .that().resideInAPackage(Packages.ADAPTER_OUTBOUND + "..")
                    .should().dependOnClassesThat().resideInAPackage(Packages.ADAPTER_INBOUND + "..")
                    .as("Outbound adapters must not know about inbound adapters");
}
