package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * The application layer owns the use cases and the port definitions. It
 * depends on the domain layer and nothing else. It is specifically forbidden
 * from leaking into adapters or framework packages.
 */
@AnalyzeClasses(
        packages = Packages.BASE,
        importOptions = {
                ImportOption.DoNotIncludeTests.class,
                DoNotIncludeGeneratedClasses.class
        })
public class ApplicationLayerTest {

    @ArchTest
    static final ArchRule application_does_not_depend_on_adapters =
            noClasses()
                    .that().resideInAPackage(Packages.APPLICATION + "..")
                    .should().dependOnClassesThat().resideInAPackage(Packages.BASE + ".adapter..")
                    .as("Application must not depend on any adapter package");

    @ArchTest
    static final ArchRule application_does_not_depend_on_framework =
            noClasses()
                    .that().resideInAPackage(Packages.APPLICATION + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "io.micronaut..",
                            "io.netty..",
                            "jakarta.inject..",
                            "jakarta.persistence..",
                            "jakarta.validation..",
                            "jakarta.annotation..",
                            "javax.persistence..",
                            "javax.sql..",
                            "java.sql..",
                            "org.flywaydb..",
                            "com.zaxxer.hikari..",
                            "reactor..")
                    .as("Application must stay framework-agnostic. "
                            + "Wire use cases from a @Factory in the composition root instead.");

    @ArchTest
    static final ArchRule inbound_ports_live_in_the_right_package =
            classes()
                    .that().haveSimpleNameEndingWith("UseCase")
                    .and().areInterfaces()
                    .should().resideInAPackage(Packages.APPLICATION_PORT_IN + "..")
                    .as("Inbound ports (interfaces named *UseCase) must live under application.port.in");

    @ArchTest
    static final ArchRule application_services_implement_inbound_ports =
            classes()
                    .that().resideInAPackage(Packages.APPLICATION_SERVICE + "..")
                    .and().haveSimpleNameEndingWith("Service")
                    .should().resideInAPackage(Packages.APPLICATION_SERVICE + "..")
                    .as("Application use-case services must live under application.service");
}
