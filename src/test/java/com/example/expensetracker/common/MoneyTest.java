package com.example.expensetracker.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void ofShouldCreateMoneyFromAmountAndCurrencyCode() {
        Money money = Money.of("12.50", "UAH");

        assertThat(money.amount()).isEqualByComparingTo("12.50");
        assertThat(money.currency()).isEqualTo(Currency.getInstance("UAH"));
    }

    @Test
    void ofShouldRejectUnknownCurrencyCode() {
        assertThatThrownBy(() -> Money.of("12.50", "XYZ"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructorShouldRejectNullAmount() {
        assertThatThrownBy(() -> new Money(null, Currency.getInstance("UAH")))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @Test
    void constructorShouldRejectNullCurrency() {
        assertThatThrownBy(() -> new Money(BigDecimal.TEN, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("currency must not be null");
    }
}
