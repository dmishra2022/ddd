package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Confirms the hexagonal rule actually fails when the dependency graph is
 * broken. Protects against a false green when the rule configuration is wrong.
 *
 * <p>Introduces a synthetic test fixture that violates the architecture
 * (see {@code violation} package under test resources) and asserts the rule
 * reports the breach. This test runs with its own importer so the fixture
 * never pollutes the production rule set.
 */
class HexagonalArchitectureSelfTest {

    @Test
    void rule_catches_adapter_calling_another_adapter() {
        // The fixture classes live under src/test/java to keep them out of production.
        // Do NOT filter tests here, otherwise ArchUnit will skip them.
        var importedClasses = new ClassFileImporter()
                .withImportOption(new DoNotIncludeGeneratedClasses())
                .importPackages("com.enterprise.starter.order.architecture.violationfixture");

        ArchRule rule = onionArchitecture()
                .domainModels("com.enterprise.starter.order.architecture.violationfixture.domain..")
                .applicationServices("com.enterprise.starter.order.architecture.violationfixture.application..")
                .adapter("rest", "com.enterprise.starter.order.architecture.violationfixture.adapter.rest..")
                .adapter("persistence", "com.enterprise.starter.order.architecture.violationfixture.adapter.persistence..")
                .withOptionalLayers(true);

        assertThatThrownBy(() -> rule.check(importedClasses))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Architecture Violation");
    }
}
