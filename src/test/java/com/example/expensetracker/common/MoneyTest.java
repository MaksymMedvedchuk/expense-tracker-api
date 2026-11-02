package com.example.expensetracker.common;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void ofShouldCreateMoneyWhenAmountAndCurrencyCodeAreValid() {
        Money money = Money.of("12.50", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("12.50"));
        assertThat(money.currency()).isEqualTo(Currency.getInstance("UAH"));
    }

    @Test
    void ofShouldThrowExceptionWhenCurrencyCodeIsUnknown() {
        assertThatThrownBy(() -> Money.of("12.50", "XYZ"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ofShouldThrowExceptionWhenAmountIsNotNumber() {
        assertThatThrownBy(() -> Money.of("abc", "UAH"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void constructorShouldThrowExceptionWhenAmountIsNull() {
        assertThatThrownBy(() -> new Money(null, Currency.getInstance("UAH")))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("amount must not be null");
    }

    @Test
    void constructorShouldThrowExceptionWhenCurrencyIsNull() {
        assertThatThrownBy(() -> new Money(BigDecimal.TEN, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("currency must not be null");
    }

    @Test
    void equalsShouldIgnoreScaleWhenAmountsAreNumericallyEqual() {
        Money withOneDigit = Money.of("100.0", "UAH");
        Money withTwoDigits = Money.of("100.00", "UAH");

        assertThat(withOneDigit).isEqualTo(withTwoDigits);
        assertThat(withOneDigit.hashCode()).isEqualTo(withTwoDigits.hashCode());
    }

    @Test
    void equalsShouldReturnFalseWhenCurrenciesDiffer() {
        Money hryvnias = Money.of("10.00", "UAH");
        Money dollars = Money.of("10.00", "USD");

        assertThat(hryvnias).isNotEqualTo(dollars);
    }

    @Test
    void constructorShouldRoundToCurrencyScaleWhenAmountHasExtraDigits() {
        Money money = Money.of("100.005", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void constructorShouldRoundHalfUpWhenPrecedingDigitIsOdd() {
        Money money = Money.of("100.015", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.02"));
    }

    @Test
    void constructorShouldRoundHalfDownWhenPrecedingDigitIsEven() {
        Money money = Money.of("100.025", "UAH");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100.02"));
    }

    @Test
    void constructorShouldUseZeroScaleWhenCurrencyHasNoMinorUnits() {
        Money money = Money.of("100.4", "JPY");

        assertThat(money.amount()).isEqualTo(new BigDecimal("100"));
    }

    @Test
    void constructorShouldUseThreeDigitScaleWhenCurrencyHasThreeMinorUnits() {
        Money money = Money.of("1.5", "BHD");

        assertThat(money.amount()).isEqualTo(new BigDecimal("1.500"));
    }
}
