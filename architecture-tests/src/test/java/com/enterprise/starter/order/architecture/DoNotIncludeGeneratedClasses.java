package com.enterprise.starter.order.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

/**
 * Excludes framework-generated bean-definition classes from ArchUnit's import.
 *
 * <p>Micronaut's annotation processor emits classes like
 * <code>$OrderController$Definition$Intercepted</code>. They live in the same
 * package as the source class and syntactically "depend" on application and
 * domain types. Those references are an implementation detail of the DI
 * container, not a design decision in the codebase we want to govern.
 *
 * <p>Filtering them keeps the architecture rules focused on hand-written code.
 */
final class DoNotIncludeGeneratedClasses implements ImportOption {

    @Override
    public boolean includes(Location location) {
        String uri = location.asURI().toString();
        return !uri.contains("/$") && !uri.contains("$$");
    }
}
