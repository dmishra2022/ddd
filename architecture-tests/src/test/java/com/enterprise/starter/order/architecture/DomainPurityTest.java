package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * The domain layer must stay independent of any framework, transport,
 * persistence, or DI technology. These rules fail the build if a type under
 * {@code domain..} ever references forbidden packages.
 */
@AnalyzeClasses(
        packages = Packages.BASE,
        importOptions = {
                ImportOption.DoNotIncludeTests.class,
                DoNotIncludeGeneratedClasses.class
        })
public class DomainPurityTest {

    @ArchTest
    static final ArchRule domain_does_not_depend_on_micronaut =
            noClasses()
                    .that().resideInAPackage(Packages.DOMAIN + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "io.micronaut..",
                            "io.netty..",
                            "io.reactivex..",
                            "reactor..")
                    .as("Domain classes must not depend on Micronaut, Netty, or any reactive framework");

    @ArchTest
    static final ArchRule domain_does_not_depend_on_jakarta_or_javax_frameworks =
            noClasses()
                    .that().resideInAPackage(Packages.DOMAIN + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "jakarta.inject..",
                            "jakarta.persistence..",
                            "jakarta.validation..",
                            "jakarta.annotation..",
                            "javax.inject..",
                            "javax.persistence..",
                            "javax.validation..",
                            "javax.annotation..")
                    .as("Domain must not depend on DI, ORM, or validation jakarta/javax APIs");

    @ArchTest
    static final ArchRule domain_does_not_depend_on_jdbc_or_flyway =
            noClasses()
                    .that().resideInAPackage(Packages.DOMAIN + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "java.sql..",
                            "javax.sql..",
                            "org.flywaydb..",
                            "com.zaxxer.hikari..")
                    .as("Domain must not depend on JDBC or Flyway");

    @ArchTest
    static final ArchRule domain_does_not_depend_on_application_or_adapters =
            noClasses()
                    .that().resideInAPackage(Packages.DOMAIN + "..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            Packages.APPLICATION + "..",
                            Packages.BASE + ".adapter..")
                    .as("Domain must not know about application or adapter layers");
}
