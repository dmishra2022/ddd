package com.enterprise.starter.order.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void adding_amounts_in_the_same_currency_works() {
        var a = Money.of("1.50", "USD");
        var b = Money.of("2.75", "USD");

        assertThat(a.add(b)).isEqualTo(Money.of("4.25", "USD"));
    }

    @Test
    void adding_amounts_in_different_currencies_fails() {
        assertThatThrownBy(() -> Money.of("1.00", "USD").add(Money.of("1.00", "EUR")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void amount_is_normalized_to_currency_scale() {
        assertThat(Money.of("1.5", "USD")).isEqualTo(Money.of("1.50", "USD"));
    }

    @Test
    void multiply_by_negative_is_rejected() {
        assertThatThrownBy(() -> Money.of("1.00", "USD").multiply(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
