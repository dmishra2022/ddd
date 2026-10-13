package com.enterprise.starter.order.architecture.violationfixture.adapter.rest;

import com.enterprise.starter.order.architecture.violationfixture.adapter.persistence.FixtureRepository;

/**
 * Deliberately broken: an inbound REST adapter directly references the
 * outbound persistence adapter. Used only by {@code HexagonalArchitectureSelfTest}
 * to prove the production rule actually catches this kind of drift.
 */
public final class FixtureController {

    private final FixtureRepository repository = new FixtureRepository();

    public void callDirectlyIntoPersistence() {
        repository.save();
    }
}
